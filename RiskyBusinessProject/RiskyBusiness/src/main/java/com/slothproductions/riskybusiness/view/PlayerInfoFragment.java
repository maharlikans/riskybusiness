package com.slothproductions.riskybusiness.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;

public class PlayerInfoFragment extends Fragment {

    private final static String TAG = "Player Info Overlay";

    private Board mBoardData;               //Model Board class used for the backend
    private BoardScreen mBoardScreen;       //BoardScreen that manages this fragment
    private android.app.Activity mActivity; //Activity that can be used instead of calling getActivity() every time

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player_info, container, false);
        Log.d(TAG, "View inflated");

        //initialize variables based on the activity of superclass
        mActivity = getActivity();
        mBoardScreen = (BoardScreen)mActivity;

        return v;
    }


}
