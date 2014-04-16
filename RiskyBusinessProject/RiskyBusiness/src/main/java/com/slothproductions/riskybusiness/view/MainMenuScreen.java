package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.media.MediaPlayer;
import com.View.R;

public class MainMenuScreen extends Activity {

    private ImageView mExit;
    private ImageView mGameRules;
    private ImageView mLoadSaved;
    private ImageView mOptions;
    private ImageView mStartNew;
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);

        song = MediaPlayer.create(MainMenuScreen.this, R.raw.mainmenusong);
        song.start();
        //Deals with the Start Button
        mStartNew = (ImageView)findViewById(R.id.start_new);
        mStartNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to main menu activity
                Intent i = new Intent(MainMenuScreen.this, GameSetupScreen.class);
                song.release(); //stops song
                startActivity(i);
            }
        });

        LinearLayout buttonParent = (LinearLayout)mStartNew.getParent();

        mGameRules = (ImageView)buttonParent.getChildAt(2);
        mGameRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to Game Rules activity
                Intent i = new Intent(MainMenuScreen.this, GameRules.class);
                startActivity(i);
            }
        });

        //Deals with the Options Button
        mOptions = (ImageView)buttonParent.getChildAt(3);
        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to Options Activity
                Intent i = new Intent(MainMenuScreen.this, OptionsScreen.class);
                startActivity(i);
            }
        });

        //Deals with the exit button.
        mExit = (ImageView)findViewById(R.id.exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.release();
                finish();
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
