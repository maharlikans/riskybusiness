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
import android.widget.Button;


public class GameRules extends Activity {

    private Button mBtnEPTurn;
    private Button mBtnResources;
    private Button mBtnTrading;
    private Button mBtnBuilding;
    private Button mBtnMilitary;
    private Button mBtnAttacking;
    private Button mBtnWinning;

    ImageView item = new ImageView(getActivity());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamerules_screen);

        mBtnEPTurn = (Button)findViewById(R.id.eachPlayersTurnButton);
        mBtnEPTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("eachplayersturn", "drawable", getActivity().getPackageName()));

            }
        });

        mBtnResources = (Button)findViewById(R.id.resourcesButton);
        mBtnResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("resources", "drawable", getActivity().getPackageName()));

            }
        });

        mBtnTrading = (Button)findViewById(R.id.tradingButton);
        mBtnTrading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("trading", "drawable", getActivity().getPackageName()));

            }
        });

        mBtnBuilding = (Button)findViewById(R.id.buildingButton);
        mBtnBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("building", "drawable", getActivity().getPackageName()));

            }
        });

        mBtnMilitary = (Button)findViewById(R.id.militaryButton);
        mBtnMilitary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("military", "drawable", getActivity().getPackageName()));

            }
        });

        mBtnAttacking = (Button)findViewById(R.id.attackingButton);
        mBtnAttacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("attacking", "drawable", getActivity().getPackageName()));

            }
        });

        mBtnWinning = (Button)findViewById(R.id.winningButton);
        mBtnWinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               item.setImageResource(getResources().getIdentifier("winning", "drawable", getActivity().getPackageName()));
            }
        });
    }

}
