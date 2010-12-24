package com.deesastudio.android.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
  public static final int STATE_RUNNING = 1;
  public static final int STATE_PAUSED = 2;
  
  public static final int MESSAGE_STATE_CHANGED = 1;
  
  private SurfaceHolder mSurfaceHolder;
  private Context       mContext;
  private Handler       mHandler;
  
  private boolean       mRunning = false;
  private long          mLastTickTime;
  private int           mGameState;
  
  private int           mSurfaceWidth;
  private int           mSurfaceHeight;
  
  private int           mMaxFPS = 40;
  private long          mMaxTick = 1000/40;
  
  private Bitmap        mBackgroundBitmap;
  private boolean       mDrawBackground = false;
  
  public GameThread(SurfaceHolder holder, Context context, Handler handler) {
    mSurfaceHolder = holder;
    mContext = context;
    mHandler = handler;
  }
  
  public void setMaxFPS(int fps) {
    synchronized (mSurfaceHolder) {
      mMaxFPS = fps;
      mMaxTick = 1000/mMaxFPS;
    }
  }
  
  public void setBackgroundBitmap(Bitmap bg) {
    synchronized (mSurfaceHolder) {
      mBackgroundBitmap = bg;
    }
  }
  
  public void setDrawBackground(boolean drawBackground) {
    synchronized (mSurfaceHolder) {
      mDrawBackground = drawBackground;
    }
  }
  
  @Override
  public void run() {
    while(mRunning) {
      Canvas c = null;
      
      try {
        c = mSurfaceHolder.lockCanvas(null);
        
        synchronized(mSurfaceHolder) {
          if (mGameState == STATE_RUNNING) {
            updateGame();
          }
            
          doDraw(c);
        }
      } finally {
        if (c != null)
          mSurfaceHolder.unlockCanvasAndPost(c);
      }
    }
  }
  
  public void doStart() {
    synchronized(mSurfaceHolder) {
      mLastTickTime = System.currentTimeMillis();
      setState(STATE_RUNNING);
    }
  }
  
  public void pause() {
    synchronized(mSurfaceHolder) {
      if (mGameState == STATE_RUNNING)
        setState(STATE_PAUSED);
    }
  }
  
  public void unpause() {
    synchronized(mSurfaceHolder) {
      mLastTickTime = System.currentTimeMillis() + 100;
    }
    setState(STATE_RUNNING);
  }
  
  public void setState(int state) {
    synchronized(mSurfaceHolder) {
      int oldState = mGameState;
      mGameState = state;
      
      Message msg = mHandler.obtainMessage();
      msg.what = MESSAGE_STATE_CHANGED;
      msg.arg1 = oldState;
      msg.arg2 = mGameState;
      mHandler.sendMessage(msg);
    }
  }
  
  public void setRunning(boolean running) {
    synchronized (mSurfaceHolder) {
      mRunning = running;
      //setState(STATE_RUNNING);
    }
  }
  
  public void setSurfaceSize(int w, int h) {
    synchronized(mSurfaceHolder) {
      mSurfaceWidth = w;
      mSurfaceHeight = h;
    }
  }
  
  private void updateGame() {
    long now = System.currentTimeMillis();
    
    if (mLastTickTime > now)
      return;
    
    long elapsed = now - mLastTickTime;
    
    if (elapsed < mMaxTick)
      return;
    
    doUpdate(elapsed/1000);
  }
  
  protected void doUpdate(double interpolator) {
    
  }
  
  protected void doDraw(Canvas c) {
    if (mDrawBackground && mBackgroundBitmap != null)
      c.drawBitmap(mBackgroundBitmap, 0, 0, null);
  }
  
  protected int getSurfaceWidth() {
    return mSurfaceWidth;
  }
  
  protected int getSurfaceHeight() {
    return mSurfaceHeight;
  }
  
  protected Context getContext() {
    return mContext;
  }
  
  protected Handler getHandler() {
    return mHandler;
  }
}
