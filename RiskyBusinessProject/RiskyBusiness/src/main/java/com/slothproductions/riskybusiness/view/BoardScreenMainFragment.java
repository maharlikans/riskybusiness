package com.slothproductions.riskybusiness.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.DiceRoll;

public class BoardScreenMainFragment extends Fragment {

    private BoardScreen mBoardScreen;
    private Board mBoardData;
    private RelativeLayout mHexParent;
    private Button mBtnPause;
    private DiceRoll droll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoardScreen = new BoardScreen();
        mBoardData = new Board(4);

        Log.d("ONCREATE", "OnCreate was called");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);

        mHexParent = (RelativeLayout)v.findViewById(R.id.hexParent);

        mBtnPause = (Button)v.findViewById(R.id.pauseButton);
        mBtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPauseDialog();
            }
        });

        addColorsToBoard();

        Log.d("VIEWCALLED", "View was called");
        return v;
    }

    void addNumbersToBoard() {
        int l = mBoardData.hexes.size();
        for (int i = 0; i < l; i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            TextView tv = new TextView(getActivity());
            tv.setId((int)System.currentTimeMillis());
            LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            if (mBoardData.hexes.get(i).roll < 0) {
                continue;
            }
            else if (mBoardData.hexes.get(i).roll > 9) {
                lp.leftMargin = iv.getLeft()-35;
                lp.rightMargin = iv.getRight()+35;
                lp.topMargin = iv.getTop()+35;
                lp.bottomMargin = iv.getBottom()-35;
            }
            else {
                lp.leftMargin = iv.getLeft()-15;
                lp.rightMargin = iv.getRight()+15;
                lp.topMargin = iv.getTop()+35;
                lp.bottomMargin = iv.getBottom()-35;
            }
            tv.setText(Integer.toString(mBoardData.hexes.get(i).roll));
            tv.setTextSize(30);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(getResources().getColor(R.color.blue_background));
            mHexParent.addView(tv, lp);
        }
    }

    //loop through indices, check which resource in board, color appropriately using code similar to below
    void addColorsToBoard() {
        for (int i = 0; i < mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            switch(mBoardData.hexes.get(i).type) {
                case LUMBER:
                    iv.setColorFilter(Color.GREEN);
                    break;
                case BRICK:
                    iv.setColorFilter(Color.RED);
                    break;
                case WOOL:
                    iv.setColorFilter(Color.LTGRAY);
                    break;
                case GRAIN:
                    iv.setColorFilter(Color.GRAY);
                    break;
                case ORE:
                    iv.setColorFilter(Color.DKGRAY);
                    break;
                case DESERT:
                    iv.setColorFilter(Color.YELLOW);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ONSTART", "OnStart called");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                addNumbersToBoard();
            }
        }, 50);
    }

    public void showPauseDialog() {
        AlertDialog.Builder alertpauseDialog = new AlertDialog.Builder(getActivity());

        alertpauseDialog.setTitle("Pause Screen");
        alertpauseDialog.setMessage("Game Paused.");

        alertpauseDialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Returning to game...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        alertpauseDialog.show();
    }

}

