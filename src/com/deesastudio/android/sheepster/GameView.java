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
import com.deesastudio.android.game.listener.OnGameStateChangedListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
  
  private SheepsterGameThread         mGameThread;
  private OnGameStateChangedListener  mGameStateListener;
  
  public GameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);
    
    mGameThread = new SheepsterGameThread(holder, getContext(), new Handler() {
      @Override
      public void handleMessage(Message m) {
        switch(m.what) {
        case GameThread.MESSAGE_STATE_CHANGED:
          if (mGameStateListener != null)
            mGameStateListener.onGameStateChanged(m.arg1, m.arg2);
          break;
        }
      }
    });
  }
  
  public void setOnGameStateChangedListener(OnGameStateChangedListener listener) {
    mGameStateListener = listener;
  }
  
  public void pause() {
    mGameThread.pause();
  }
  
  
  public void start() {
    mGameThread.doStart();
  }
  /**
   * Standard window-focus override. Notice focus lost so we can pause on
   * focus lost. e.g. user switches to take a call.
   */
  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
      if (!hasWindowFocus)
        pause();
  }
  
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {
    mGameThread.setSurfaceSize(width, height);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    mGameThread.setRunning(true);
    mGameThread.start();
    mGameThread.setState(GameThread.STATE_READY);
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
