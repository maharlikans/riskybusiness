package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.View.R;



public class MainMenuScreen extends Activity {

    private ImageView mExit;
    private ImageView mHighScores;
    private ImageView mLoadSaved;
    private ImageView mOptions;
    private ImageView mStartNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);

        mStartNew = (ImageView)findViewById(R.id.start_new);

        mStartNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to main menu activity
                Intent i = new Intent(MainMenuScreen.this, BoardScreen.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        return super.onOptionsItemSelected(menu);
    }

}