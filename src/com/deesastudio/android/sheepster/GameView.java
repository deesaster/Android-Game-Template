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
  private Handler                     mHandler;
  private int                         mGameState;
  
  public GameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);
    
    initHandler();
    initGameThread();
  }
  
  private void initGameThread() {
    mGameThread = new SheepsterGameThread(getHolder(), getContext(),  mHandler);
  }
  
  private void initHandler() {
    mHandler = new Handler() {
      @Override
      public void handleMessage(Message m) {
        switch(m.what) {
        case GameThread.MESSAGE_STATE_CHANGED:
          mGameState = m.arg2;
          
          if (mGameStateListener != null)
            mGameStateListener.onGameStateChanged(m.arg1, m.arg2);
          break;
        }
      }
    };
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
    if (mGameThread == null)
      initGameThread();
    
    mGameThread.setRunning(true);
    
    if(mGameState == GameThread.STATE_PAUSED)
      mGameThread.setState(GameThread.STATE_PAUSED);
    else
      mGameThread.setState(GameThread.STATE_READY);
    
    mGameThread.start();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    
    boolean retry = true;
    mGameThread.setRunning(false);
    while (retry) {
      try {
        mGameThread.join();
          retry = false;
      } catch (InterruptedException e) {
      }
    }
    mGameThread = null;
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
