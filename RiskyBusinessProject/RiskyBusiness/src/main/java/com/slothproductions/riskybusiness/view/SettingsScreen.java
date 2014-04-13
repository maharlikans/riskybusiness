package com.slothproductions.riskybusiness.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.View.R;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsScreen extends Activity {

    //Last toast variable is kept track of in order to cancel last toast upon creating new one
    private Toast mLastToast;

    // View objects
    Button options, htp, save, rtg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        // get options button and set behavior
        options = (Button)findViewById(R.id.optionsButton);
        htp = (Button)findViewById(R.id.htpButton);
        save = (Button)findViewById(R.id.saveButton);
        rtg = (Button)findViewById(R.id.rtgButton);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsScreen.this, OptionsScreen.class);
                startActivity(i);

            }
        });

        htp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsScreen.this, GameRules.class);
                startActivity(i);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createToast("Game Saved!", false);
            }
        });

        rtg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void createToast(String text, boolean isLong) {
        cancelToast();
        int length = Toast.LENGTH_SHORT;
        if (isLong) {
            length = Toast.LENGTH_LONG;
        }
        mLastToast = Toast.makeText(this, text, length);
        mLastToast.show();
    }

    boolean cancelToast() {
        if (mLastToast!= null) {
            mLastToast.cancel();
            return true;
        }
        return false;
    }
}