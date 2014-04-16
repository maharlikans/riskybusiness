package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.View.R;


public class GameRules extends Activity {

    private ImageView mBtnEPTurn;
    private ImageView mBtnResources;
    private ImageView mBtnTrading;
    private ImageView mBtnBuilding;
    private ImageView mBtnMilitary;
    private ImageView mBtnAttacking;
    private ImageView mBtnWinning;

    ImageView item;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamerules_screen);

        item = (ImageView)findViewById(R.id.game_rule_image);
        item.setVisibility(View.INVISIBLE);

        mBtnEPTurn = (ImageView)findViewById(R.id.eachPlayersTurnButton);
        mBtnEPTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("eachplayersturn", "drawable", getPackageName()));
                item.setVisibility(View.VISIBLE);
            }
        });

        mBtnResources = (ImageView)findViewById(R.id.resourcesButton);
        mBtnResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("resources", "drawable", getPackageName()));
                item.setVisibility(View.VISIBLE);
            }
        });

        mBtnTrading = (ImageView)findViewById(R.id.tradingButton);
        mBtnTrading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("trading", "drawable", getPackageName()));
                item.setVisibility(View.VISIBLE);
            }
        });

        mBtnBuilding = (ImageView)findViewById(R.id.buildingButton);
        mBtnBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("building", "drawable", getPackageName()));
                item.setVisibility(View.VISIBLE);
            }
        });

        mBtnMilitary = (ImageView)findViewById(R.id.militaryButton);
        mBtnMilitary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("military", "drawable", getPackageName()));
                item.setVisibility(View.VISIBLE);
            }
        });

        mBtnAttacking = (ImageView)findViewById(R.id.attackingButton);
        mBtnAttacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setImageResource(getResources().getIdentifier("attacking", "drawable", getPackageName()));
                item.setVisibility(View.VISIBLE);
            }
        });

        mBtnWinning = (ImageView)findViewById(R.id.winningButton);
        mBtnWinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               item.setImageResource(getResources().getIdentifier("winning", "drawable", getPackageName()));
               item.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("KEYPRESSED", "Back Button was pressed");
            if (item.getVisibility() == View.INVISIBLE) {
                return super.onKeyDown(keyCode, event);
            }
            else {
                removeCurrentRule();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void removeCurrentRule() {
        item.setVisibility(View.INVISIBLE);
    }
}
