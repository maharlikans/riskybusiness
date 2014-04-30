package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.View.R;
import android.app.Activity;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.AudioManager;

public class OptionsScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_screen);

        //Points to the Seekbars in the OptionsScreen.xml file, so that it will work
        final SeekBar musicSeeker = (SeekBar)findViewById(R.id.music_volume_seekbar);
        final SeekBar volumeSeeker = (SeekBar) findViewById(R.id.sound_effect_seekbar);

        //Points to the TextViews so that the percentage volume can be displayed
        final TextView musicTextView = (TextView) findViewById(R.id.music_volume_value);
        final TextView soundTextView = (TextView) findViewById(R.id.sound_effect_value);

        MediaPlayer mediaPlayer;
        final AudioManager audioManager;

        //Sets the initial Progress for the volume at the default
       // musicTextView.setText(String.valueOf(musicSeeker.getProgress()) + '%');   //Sets the text view to 50% by default
        //soundTextView.setText(String.valueOf(volumeSeeker.getProgress()) + '%');

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        musicSeeker.setMax(maxVolume);
        musicSeeker.setProgress(curVolume);


        musicSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser == true)
                   // musicTextView.setText(String.valueOf(progress) + '%');
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        volumeSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // if(fromUser == true)
                   // soundTextView.setText(String.valueOf(progress) + '%');
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_options) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}