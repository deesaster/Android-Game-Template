package com.deesastudio.android.sheepster;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.deesastudio.android.game.GameThread;
import com.deesastudio.android.game.listener.OnGameStateChangedListener;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
  
  private SheepsterGame               mGame;
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
    mGame = new SheepsterGame(getHolder(), getContext(),  mHandler);
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
    mGame.pause();
  }
  
  
  public void start() {
    mGame.doStart();
  }
  
  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    if (!hasWindowFocus)
      pause();
  }
  
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {
    mGame.setSurfaceSize(width, height);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (mGame == null)
      initGameThread();
    
    mGame.setRunning(true);
    
    if(mGameState == GameThread.STATE_PAUSED)
      mGame.setState(GameThread.STATE_PAUSED);
    else
      mGame.setState(GameThread.STATE_READY);
    
    mGame.start();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    
    boolean retry = true;
    mGame.setRunning(false);
    while (retry) {
      try {
        mGame.join();
          retry = false;
      } catch (InterruptedException e) {
      }
    }
    mGame = null;
  }
}
