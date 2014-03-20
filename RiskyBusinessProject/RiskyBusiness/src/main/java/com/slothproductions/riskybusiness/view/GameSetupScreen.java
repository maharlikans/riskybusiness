package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.View.R;

/**
 * Created by Kyle Maharlika on 3/19/14.
 */
public class GameSetupScreen extends Activity {
    static final int NUM_PLAYERS = 4;
    Spinner mNumPlayersSpinner;
    TextView[] mPlayerTextViews;
    RadioGroup[] mPlayerChoicesGroup;
    LinearLayout[] mPlayerLayouts;
    int[] mDefaultColors;
    Spinner mVictoryPointsSpinner;
    ToggleButton mVariableBoardToggle;
    ToggleButton mAttacksToggle;
    Button mStartGame;

    // doesn't actually do anything yet
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup_screen);

        mPlayerLayouts = new LinearLayout[NUM_PLAYERS+1];

        mPlayerLayouts[1] = (LinearLayout)findViewById(R.id.player_1_linear);
        mPlayerLayouts[2] = (LinearLayout)findViewById(R.id.player_2_linear);
        mPlayerLayouts[3] = (LinearLayout)findViewById(R.id.player_3_linear);
        mPlayerLayouts[4] = (LinearLayout)findViewById(R.id.player_4_linear);

        // getting the spinner and setting its display parameters
        mNumPlayersSpinner = (Spinner)findViewById(R.id.num_players_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.num_players_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumPlayersSpinner.setAdapter(adapter);

        // getting all the player views

        // implementing the behavior
        mNumPlayersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int player;
                int number = Integer.parseInt((String)adapterView.getItemAtPosition(i));
                for (player = 1; player <= number; player++) {
                    mPlayerLayouts[player].setVisibility(LinearLayout.VISIBLE);
                }

                for ( ; player <= NUM_PLAYERS; player++)
                    mPlayerLayouts[player].setVisibility(LinearLayout.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // get all resource ids and set buttons
    }
}
