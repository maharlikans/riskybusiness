package com.slothproductions.riskybusiness.view;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.View.R;

/**
 * Created by User on 3/23/2014.
 */
public class TradeScreenFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "OnCreate was called in TradeScreenFragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade_screen, parent, false);
        ImageView cancelButton = (ImageView)v.findViewById(R.id.cancel_trade_button);

        LinearLayout tradeBack = (LinearLayout)v.findViewById(R.id.tradeback);
        tradeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BoardScreen)getActivity()).onCancelTradeButtonPressed();
            }
        });

        return v;
    }

}
