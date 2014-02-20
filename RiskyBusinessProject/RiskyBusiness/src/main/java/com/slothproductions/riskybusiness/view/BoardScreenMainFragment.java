package com.slothproductions.riskybusiness.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;

public class BoardScreenMainFragment extends Fragment {

    private BoardScreen mBoardScreen;
    private Board mBoardData;
    private RelativeLayout mHexParent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoardScreen = new BoardScreen();
        mBoardData = new Board(4);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);

        mHexParent = (RelativeLayout)v.findViewById(R.id.hexParent);
        mHexParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int l = mBoardData.hexes.size();
                for (int i = 1; i < l; i++) {
                    ImageView iv = (ImageView)mHexParent.getChildAt(i);
                    TextView tv = new TextView(getActivity());
                    tv.setId((int)System.currentTimeMillis());
                    LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                    if (mBoardData.hexes.get(i).roll < 0 || mBoardData.hexes.get(i).roll > 9) {
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
        });

        //loop through indices, check which resource in board, color appropriately using code similar to below
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
        return v;
    }

}