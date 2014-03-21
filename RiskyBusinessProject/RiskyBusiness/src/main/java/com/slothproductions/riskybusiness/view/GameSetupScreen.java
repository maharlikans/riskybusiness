package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.View.R;

/**
 * Created by Kyle Maharlika on 3/19/14.
 */
public class GameSetupScreen extends Activity {
    static final int NUM_PLAYERS = 4;
    int numPlayersChosen;
    Spinner mNumPlayersSpinner;
    RadioGroup[] mPlayerChoicesGroups;
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

        // getting the spinner and setting its display parameters
        mNumPlayersSpinner = (Spinner)findViewById(R.id.num_players_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.num_players_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumPlayersSpinner.setAdapter(adapter);

        // find the player linear layouts to be used
        mPlayerLayouts = new LinearLayout[NUM_PLAYERS+1];

        mPlayerLayouts[1] = (LinearLayout)findViewById(R.id.player_1_linear);
        mPlayerLayouts[2] = (LinearLayout)findViewById(R.id.player_2_linear);
        mPlayerLayouts[3] = (LinearLayout)findViewById(R.id.player_3_linear);
        mPlayerLayouts[4] = (LinearLayout)findViewById(R.id.player_4_linear);

        // find the player radio groups to be used
        mPlayerChoicesGroups = new RadioGroup[NUM_PLAYERS+1];

        mPlayerChoicesGroups[1] = (RadioGroup)findViewById(R.id.radio_group_1);
        mPlayerChoicesGroups[2] = (RadioGroup)findViewById(R.id.radio_group_2);
        mPlayerChoicesGroups[3] = (RadioGroup)findViewById(R.id.radio_group_3);
        mPlayerChoicesGroups[4] = (RadioGroup)findViewById(R.id.radio_group_4);

        // implementing the behavior of the spinner
        mNumPlayersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int player;
                numPlayersChosen = Integer.parseInt((String)adapterView.getItemAtPosition(i));
                for (player = 1; player <= numPlayersChosen; player++) {
                    mPlayerLayouts[player].setVisibility(LinearLayout.VISIBLE);
                }

                for ( ; player <= NUM_PLAYERS; player++)
                    mPlayerLayouts[player].setVisibility(LinearLayout.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // left blank, should not do anything
            }
        });

        // get start game button and set behavior
        mStartGame = (Button)findViewById(R.id.start_game_button);

        mStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] decisions = new String[numPlayersChosen+1];
                // for the appropriate radio button groups
                // find out which radio button of each of those groups were pressed down
                for (int i = 1; i <= numPlayersChosen; i++) {
                    int selectedButtonId = mPlayerChoicesGroups[i].getCheckedRadioButtonId();
                    Log.d("OnClickButton" + i, "Selected button ID is: " + selectedButtonId);
                    RadioButton selectedButton = (RadioButton)findViewById(selectedButtonId);
                    if (selectedButton == null)
                        Log.d("OnClickButton" + i, "this shit is null");
                    decisions[i] = (String)selectedButton.getText();
                    Log.d("OnClick", decisions[i]);
                }
                // pass the decisions as an array of intents
            }
        });

        // get all resource ids and set buttons
    }
}
