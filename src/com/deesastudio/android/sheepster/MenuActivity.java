package com.deesastudio.android.sheepster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.menu_layout);
    
    bindButtons();
  }
  
  private void bindButtons() {
    Button startGame = (Button)findViewById(R.id.btnStartGame);
    startGame.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        Intent gameIntent = new Intent(MenuActivity.this, GameActivity.class);
        startActivity(gameIntent);
      }
    });
  }
}