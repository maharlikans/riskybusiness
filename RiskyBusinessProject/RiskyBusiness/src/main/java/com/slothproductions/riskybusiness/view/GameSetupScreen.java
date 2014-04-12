package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.View.R;

/**
 * Created by Kyle Maharlika on 3/19/14.
 *
 * Now you might ask "So Kyle, how did you plan on passing the data to the Board activity?"
 * Well, here's a list of the variable names you need to get from the Intent I pass.
 *
 * First you need to get the intent, then fetch the following (all arrays are indexed by 1, not 0):
 *     numPlayersChosen - integer containing the number of players chosen
 *     playerType       - a String array containing the type of player for each player
 *     numVictoryPoints - integer containing the number of victory points chosen
 *     variableBoard    - boolean value indicating whether or not the player wants variable board
 *                        generation or not
 *     attacksOn        - boolean value indicating whether or not the player wants attacks on or off
 *                        I make sure that if the player choose 0 points, that attacksOn must be true
 *     defaultColors - passes the integer values of the default colors for each player
 *
 *     fetch each using each of these String references
 *         GameSetupScreen.NUM_PLAYERS_CHOSEN
 *         GameSetupScreen.PLAYER_TYPES
 *         GameSetupScreen.NUM_VICTORY_POINTS
 *         GameSetupScreen.VARIABLE_BOARD
 *         GameSetupScreen.ATTACKS
 *         GameSetupScreen.COLORS
 *
 */
public class GameSetupScreen extends Activity {
    // Strings used to fetch from the intent
    public static final String
            NUM_PLAYERS_CHOSEN = "com.slothproductions.riskybusiness.numPlayersChosen";
    public static final String
            PLAYER_TYPES = "com.slothproductions.riskybusiness.playerTypes";
    public static final String
            NUM_VICTORY_POINTS = "com.slothproductions.riskybusiness.numVictoryPoints";
    public static final String
            VARIABLE_BOARD = "com.slothproductions.riskybusiness.variableBoard";
    public static final String
            ATTACKS = "com.slothproductions.riskybusiness.attacksOn";
    public static final String
            COLORS = "com.slothproductions.riskybusiness.defaultColors";

    // View objects
    Spinner mNumPlayersSpinner;
    RadioGroup[] mPlayerChoicesGroups;
    LinearLayout[] mPlayerLayouts;
    Spinner mVictoryPointsSpinner;
    ToggleButton mVariableBoardToggle;
    LinearLayout mAttacksLinear;
    ToggleButton mAttacksToggle;
    Button mStartGame;

    // Local data to be fetched from/determined by interactions with the view
    static final int NUM_PLAYERS = 4;
    int numPlayersChosen;
    String[] playerType;
    int numVictoryPoints;
    boolean variableBoard;
    boolean attacksOn;
    int[] defaultColors;

    // doesn't actually do anything yet
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup_screen);

        // getting the spinner and setting its display parameters
        mNumPlayersSpinner = (Spinner)findViewById(R.id.num_players_spinner);
        ArrayAdapter<CharSequence> numPlayersAdapter = ArrayAdapter.createFromResource(this,
                R.array.num_players_array, android.R.layout.simple_spinner_item);

        numPlayersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumPlayersSpinner.setAdapter(numPlayersAdapter);

        // find the player linear layouts to be used
        // NOTE: this MUST be set up before the mNumPlayersSpinner's OnItemSelectedListener
        //       is set because its listener manipulates the visibility of the the layouts
        //       in this array
        mPlayerLayouts = new LinearLayout[NUM_PLAYERS+1];
        mPlayerLayouts[1] = (LinearLayout)findViewById(R.id.player_1_linear);
        mPlayerLayouts[2] = (LinearLayout)findViewById(R.id.player_2_linear);
        mPlayerLayouts[3] = (LinearLayout)findViewById(R.id.player_3_linear);
        mPlayerLayouts[4] = (LinearLayout)findViewById(R.id.player_4_linear);

        // implementing the behavior of the number of players spinner
        mNumPlayersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numPlayersChosen = Integer.parseInt((String)adapterView.getItemAtPosition(i));
                int player;
                for (player = 1; player <= numPlayersChosen; player++) {
                    mPlayerLayouts[player].setVisibility(LinearLayout.VISIBLE);
                }

                for ( ; player <= NUM_PLAYERS; player++)
                    mPlayerLayouts[player].setVisibility(LinearLayout.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> a) {
                // nothing
            }
        });

        // find the player radio groups to be used
        mPlayerChoicesGroups = new RadioGroup[NUM_PLAYERS+1];

        mPlayerChoicesGroups[1] = (RadioGroup)findViewById(R.id.radio_group_1);
        mPlayerChoicesGroups[2] = (RadioGroup)findViewById(R.id.radio_group_2);
        mPlayerChoicesGroups[3] = (RadioGroup)findViewById(R.id.radio_group_3);
        mPlayerChoicesGroups[4] = (RadioGroup)findViewById(R.id.radio_group_4);

        // set up the attacks toggle button and get its view
        // NOTE: this HAS to be set up before the victoryPoints spinner
        //       because the victory points spinner influences the visibility
        //       of this button
        mAttacksLinear = (LinearLayout)findViewById(R.id.attacks_linear);
        mAttacksToggle = (ToggleButton)findViewById(R.id.attacks_toggle);

        attacksOn = mAttacksToggle.isChecked();

        mAttacksToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                attacksOn = b;
            }
        });

        // set up the victory points spinner
        mVictoryPointsSpinner = (Spinner)findViewById(R.id.victory_points_spinner);

        ArrayAdapter<CharSequence> victoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.victory_points_array, android.R.layout.simple_spinner_item);

        victoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mVictoryPointsSpinner.setAdapter(victoryAdapter);

        mVictoryPointsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numVictoryPoints = Integer.parseInt((String)adapterView.getItemAtPosition(i));
                if(numVictoryPoints == 0) {
                    mAttacksLinear.setVisibility(View.GONE);
                    attacksOn = true;
                } else {
                    mAttacksLinear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> a) {
                // nothing
            }
        });

        // set up the variable board toggle button
        mVariableBoardToggle = (ToggleButton)findViewById(R.id.variable_board_toggle);

        variableBoard = mVariableBoardToggle.isChecked();

        mVariableBoardToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                variableBoard = b;
            }
        });

        // set the colors to be used for the players
        // TODO change the colors
        defaultColors = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.BLACK};

        // get start game button and set behavior
        mStartGame = (Button)findViewById(R.id.start_game_button);

        mStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerType = new String[numPlayersChosen+1];
                // for the appropriate radio button groups
                // find out which radio button of each of those groups were pressed down
                for (int i = 1; i <= numPlayersChosen; i++) {
                    int selectedButtonId = mPlayerChoicesGroups[i].getCheckedRadioButtonId();
                    // Log.d("OnClickButton" + i, "Selected button ID is: " + selectedButtonId);
                    RadioButton selectedButton = (RadioButton)findViewById(selectedButtonId);
                    /*if (selectedButton == null)
                        Log.d("OnClickButton" + i, "this shit is null");*/
                    playerType[i] = (String)selectedButton.getText();
//                    Log.d("OnClick", playerType[i]);
                }

//                Log.d("OnClick", "Num victory points is: " + numVictoryPoints);
//                Log.d("OnClick", "Board generation is variable: " + variableBoard);
//                Log.d("OnClick", "Attacks are on: " + attacksOn);

                // pass all the required data to the next activity and call the activity
                Intent intent = new Intent(GameSetupScreen.this,BoardScreen.class);
                intent.putExtra(NUM_PLAYERS_CHOSEN, numPlayersChosen);
                intent.putExtra(PLAYER_TYPES, playerType);
                intent.putExtra(NUM_VICTORY_POINTS, numVictoryPoints);
                intent.putExtra(VARIABLE_BOARD, variableBoard);
                intent.putExtra(ATTACKS, attacksOn);
                intent.putExtra(COLORS, defaultColors);
                startActivity(intent);
            }
        });

     }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
