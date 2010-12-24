package com.deesastudio.android.sheepster;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.deesastudio.android.game.GameThread;
import com.deesastudio.android.game.listener.OnGameStateChangedListener;

public class GameActivity extends Activity {

  //private TextView              mStatusTextView;
  private GameView              mGameView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.game_layout);
    
    //mStatusTextView = (TextView)findViewById(R.id.textStatus);
    mGameView = (GameView)findViewById(R.id.gameView);
    mGameView.setOnGameStateChangedListener(new OnGameStateChangedListener() {
      
      @Override
      public void onGameStateChanged(int oldState, int newState) {
        //mStatusTextView.setText("state=" + newState);
        
        switch(newState) {
        case GameThread.STATE_RUNNING:
          break;
        case GameThread.STATE_PAUSED:
          break;
        case GameThread.STATE_STOPPED:
          break;
        case GameThread.STATE_READY:
          break;
        }
      }
    });
    
//    Button pause = (Button)findViewById(R.id.pauseGame);
//    pause.setOnClickListener(new OnClickListener() {
//      
//      @Override
//      public void onClick(View v) {
//        // TODO Auto-generated method stub
//        
//      }
//    });
    
    mGameView.start();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
  }
  
  @Override 
  protected void onPause() {
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
}
