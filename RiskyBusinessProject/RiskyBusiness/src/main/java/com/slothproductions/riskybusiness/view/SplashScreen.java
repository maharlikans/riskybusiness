package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.View.R;

import android.os.Handler;


public class SplashScreen extends Activity {

    private FrameLayout mSplash;
    private ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;
    private int mProgressStatus;
    private Handler handler;
    private TextView mTapToStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mProgressStatus = 1;
        handler = new Handler();
        mTapToStart = (TextView)findViewById(R.id.tap_to_start);
        mSplash = (FrameLayout)findViewById(R.id.splashContainer);

        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                            if (mProgressStatus < 40) {
                                mTapToStart.setText("Loading Assets...");
                            }
                            else if (mProgressStatus < 65) {
                                mTapToStart.setText("Rendering Textures...");
                            }
                            else if (mProgressStatus < 99) {
                                mTapToStart.setText("Generating Board Layout...");
                            }
                            else {
                                mTapToStart.setText("Tap to Start");
                                mSplash.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Should proceed to main menu activity
                                        Intent i = new Intent(SplashScreen.this, MainMenuScreen.class);
                                        startActivity(i);
                                    }
                                });
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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

    /**
     * finish activity once splash screen displayed, so the app will not return to it.
     */
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

}
