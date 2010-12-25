package com.deesastudio.android.sheepster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.deesastudio.android.game.GameThread;

public class SheepsterGame extends GameThread {

  private Bitmap tempB;
  
  public SheepsterGame(SurfaceHolder holder, Context context, Handler handler) {
    super(holder, context, handler);
    setMaxFPS(25);
    setBackgroundBitmap(BitmapFactory.decodeResource(context.getResources(),
        R.drawable.background_carbon));
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