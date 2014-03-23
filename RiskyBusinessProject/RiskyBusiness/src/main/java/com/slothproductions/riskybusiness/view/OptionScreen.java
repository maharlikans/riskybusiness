package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.View.R;
/**
 * Created by Jacob on 2/20/14.
 */
public class OptionScreen extends Activity {

    ToggleButton mSoundButton;
    boolean variableSound;



    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_screen);

        mSoundButton = (ToggleButton)findViewById(R.id.toggle_sound);


     variableSound = mSoundButton.isChecked();

        mSoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                variableSound = b;
            }
        });

    }
}

