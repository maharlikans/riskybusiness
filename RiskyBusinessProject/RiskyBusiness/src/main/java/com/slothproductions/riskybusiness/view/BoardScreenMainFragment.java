package com.slothproductions.riskybusiness.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Toast;

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