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

public class BoardScreenMainFragment extends Fragment {

    private BoardScreen mBoardScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBoardScreen = new BoardScreen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);

        RelativeLayout mHexParent = (RelativeLayout)v.findViewById(R.id.hexParent);

        ImageView iv = (ImageView)mHexParent.getChildAt(0);
        iv.setColorFilter(Color.BLUE);

        return v;
    }

}