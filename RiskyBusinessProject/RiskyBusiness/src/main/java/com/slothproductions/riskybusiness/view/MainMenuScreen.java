package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

        //Deals with the Start Button
        mStartNew = (ImageView)findViewById(R.id.start_new);
        mStartNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to main menu activity
                Intent i = new Intent(MainMenuScreen.this, GameSetupScreen.class);
                startActivity(i);
            }
        });

        //Deals with the Options Button
       /*mOptions = (ImageView)findViewById(R.id.options);
       mStartNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to Options Activity
                Intent i = new Intent(MainMenuScreen.this, OptionScreen.class);
                startActivity(i);
            }
        });*/



        //Deals with the exit button.
        mExit = (ImageView)findViewById(R.id.exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
