package com.deesastudio.android.sheepster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.deesastudio.android.game.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
  
  private SheepsterGameThread    mGameThread;
  
  public GameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);
    
    mGameThread = new SheepsterGameThread(holder, getContext(), new Handler() {
      @Override
      public void handleMessage(Message m) {
        // Use for pushing back messages.
      }
    });
  }

  /**
   * Standard window-focus override. Notice focus lost so we can pause on
   * focus lost. e.g. user switches to take a call.
   */
  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
      if (!hasWindowFocus)
        mGameThread.pause();
  }
  
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {
    mGameThread.setSurfaceSize(width, height);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    // start the thread here so that we don't busy-wait in run()
    // waiting for the surface to be created
    mGameThread.setRunning(true);
    mGameThread.start();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    // we have to tell thread to shut down & wait for it to finish, or else
    // it might touch the Surface after we return and explode
    boolean retry = true;
    mGameThread.setRunning(false);
    while (retry) {
      try {
        mGameThread.join();
          retry = false;
      } catch (InterruptedException e) {
      }
    }
  }
  
  class SheepsterGameThread extends GameThread {

    private Bitmap tempB;
    
    public SheepsterGameThread(SurfaceHolder holder, Context context, Handler handler) {
      super(holder, context, handler);
      setMaxFPS(25);
      setBackgroundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu_background));
      setDrawBackground(true);
      
      tempB = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
    }
    
    @Override
    protected void doDraw(Canvas c) {
      super.doDraw(c);
      
      c.drawBitmap(tempB, 0, 0, null);
    }
    
    @Override
    protected void doUpdate(double interpolator) {
      super.doUpdate(interpolator);
    }
  }
}
