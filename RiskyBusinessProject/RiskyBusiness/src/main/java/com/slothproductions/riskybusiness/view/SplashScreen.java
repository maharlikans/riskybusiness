package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.View.R;



public class SplashScreen extends Activity {

    private FrameLayout mSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mSplash = (FrameLayout)findViewById(R.id.splashContainer);

        mSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should proceed to main menu activity
                Intent i = new Intent(SplashScreen.this, MainMenuScreen.class);
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
