package com.slothproductions.riskybusiness.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.TextView;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;

import java.util.ArrayList;

public class PlayerInfoFragment extends Fragment {

    private final static String TAG = "Player Info Overlay";

    private Board mBoardData;               //Model Board class used for the backend
    private BoardScreen mBoardScreen;       //BoardScreen that manages this fragment
    private android.app.Activity mActivity; //Activity that can be used instead of calling getActivity() every time

    private ArrayList<TextView> mResourcesAvailable;
    private int[] numResources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player_info, container, false);
        Log.d(TAG, "View inflated");

        //initialize variables based on the activity of superclass
        mActivity = getActivity();
        mBoardScreen = (BoardScreen)mActivity;
        initializeResources(v);

        return v;
    }

    public void initializeResources(View v) {
        mResourcesAvailable = new ArrayList<TextView>();
        mResourcesAvailable.add((TextView)v.findViewById(R.id.num_wood));
        mResourcesAvailable.add((TextView)v.findViewById(R.id.num_brick));
        mResourcesAvailable.add((TextView)v.findViewById(R.id.num_wool));
        mResourcesAvailable.add((TextView)v.findViewById(R.id.num_ore));
        mResourcesAvailable.add((TextView)v.findViewById(R.id.num_grain));
        mResourcesAvailable.add((TextView)v.findViewById(R.id.num_gold));
    }

    public void updateResourceValues() {
        //iterates through the num of resources, and assigns the text value to each one
        /*numResources = getResources();
        for (int i = 0; i < numResources.size(); i ++) {
            mResourcesAvailable.get(i).setText(numResources.get(i));
        }*/
    }
}
