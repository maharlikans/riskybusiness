package com.slothproductions.riskybusiness.view;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;

public class BoardScreenMainFragment extends Fragment {

    private BoardScreen mBoardScreen;
    private Board mBoardData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoardScreen = new BoardScreen();
        mBoardData = new Board(4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);

        RelativeLayout mHexParent = (RelativeLayout)v.findViewById(R.id.hexParent);

        //loop through indices, check which resource in board, color appropriately using code similar to below
        ImageView iv = (ImageView)mHexParent.getChildAt(0);
        iv.setColorFilter(Color.BLUE);

        return v;
    }

}