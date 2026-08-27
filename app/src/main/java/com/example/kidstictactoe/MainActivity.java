package com.example.kidstictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Check if theme was selected
        Intent intent = getIntent();
        int theme = intent.getIntExtra("theme", -1);
        
        if (theme == -1) {
            // No theme selected, launch theme selection activity
            Intent themeIntent = new Intent(this, ThemeSelectionActivity.class);
            startActivity(themeIntent);
            finish();
        } else {
            // Theme selected, show game
            setContentView(new GameView(this, theme));
        }
    }
}
