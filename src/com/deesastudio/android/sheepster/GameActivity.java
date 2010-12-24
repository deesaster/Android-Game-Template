package com.deesastudio.android.sheepster;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.deesastudio.android.game.GameThread;
import com.deesastudio.android.game.listener.OnGameStateChangedListener;

public class GameActivity extends Activity {

  private TextView                    mStatusTextView;
  private GameView                    mGameView;
  private OnGameStateChangedListener  mGameStateListener;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.game_layout);
    
    mStatusTextView = (TextView)findViewById(R.id.textStatus);
    mGameView = (GameView)findViewById(R.id.gameView);
    
    initGameStateListener();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    
    mGameView.setOnGameStateChangedListener(mGameStateListener);
  }
  
  @Override 
  protected void onPause() {
    mGameView.setOnGameStateChangedListener(null);
    
    super.onPause();
  }
  
  @Override
  protected void onStop() {
    super.onStop();
  }
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
  
  private void initGameStateListener() {
    mGameStateListener = new OnGameStateChangedListener() {
      
      @Override
      public void onGameStateChanged(int oldState, int newState) {
        mStatusTextView.setText("state=" + newState);
        
        switch(newState) {
        case GameThread.STATE_RUNNING:
          break;
        case GameThread.STATE_PAUSED:
          break;
        case GameThread.STATE_STOPPED:
          break;
        case GameThread.STATE_READY:
          mGameView.start();
          break;
        }
      }
    };
  }
}
