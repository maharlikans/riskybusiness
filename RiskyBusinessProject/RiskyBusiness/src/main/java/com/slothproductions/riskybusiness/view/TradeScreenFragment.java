package com.slothproductions.riskybusiness.view;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.View.R;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 3/23/2014.
 */
public class TradeScreenFragment extends Fragment {

    Spinner mTypeTradeSpinner;
    Spinner mPlayerTradeSelectSpinner;
    ImageView mCancelButton;
    BoardScreen mBoardScreen;
    GameLoop mGameLoop;
    HashMap<Resource, ImageView> mResourceToTradeButtonOut;
    HashMap<Resource, ImageView> mResourceToTradeButtonIn;
    HashMap<Resource, Integer> mResourcesOut;
    HashMap<Resource, Integer> mCurrentResources;
    HashMap<Resource, Integer> mResourcesIn;


    enum TradeState {
        BANK_TRADE,
        PRIVATE_TRADE
    }

    TradeState mTradeState = TradeState.BANK_TRADE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "OnCreate was called in TradeScreenFragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade_screen, parent, false);

        mTypeTradeSpinner = (Spinner)v.findViewById(R.id.type_trade_spinner);
        ArrayAdapter<CharSequence> typeTradeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.type_trade_array, android.R.layout.simple_spinner_item);
        typeTradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeTradeSpinner.setAdapter(typeTradeAdapter);

        mTypeTradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = (String)adapterView.getItemAtPosition(i);
                if (itemSelected.equals("Bank Trade"))
                    mTradeState = TradeState.BANK_TRADE;
                else if (itemSelected.equals("Private Trade"))
                    mTradeState = TradeState.PRIVATE_TRADE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        mPlayerTradeSelectSpinner = (Spinner)v.findViewById(R.id.type_trade_spinner);
        ArrayAdapter<CharSequence> playerTradeSelectAdapter =
                ArrayAdapter.createFromResource(getActivity(),
                R.array.player_names, android.R.layout.simple_spinner_item);
        playerTradeSelectAdapter.
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPlayerTradeSelectSpinner.setAdapter(typeTradeAdapter);

        mCancelButton = (ImageView)v.findViewById(R.id.cancel_trade_button);

        TableLayout tradeBack = (TableLayout)v.findViewById(R.id.tradeback);
        tradeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing!
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BoardScreen)getActivity()).onCancelTradeButtonPressed();
            }
        });

        // Initialize the tradeButtons into the map
        // TODO REFACTOR ALL THE RESOURCE NAMES TO BE UNIFORM

        mResourceToTradeButtonOut = new HashMap<Resource, ImageView>();
        mResourceToTradeButtonOut.put(Resource.BRICK,
                (ImageView)v.findViewById(R.id.trade_out_brick));
        mResourceToTradeButtonOut.put(Resource.GOLD,
                (ImageView)v.findViewById(R.id.trade_out_gold));
        mResourceToTradeButtonOut.put(Resource.ORE,
                (ImageView)v.findViewById(R.id.trade_out_ore));
        mResourceToTradeButtonOut.put(Resource.WOOL,
                (ImageView)v.findViewById(R.id.trade_out_sheep));
        mResourceToTradeButtonOut.put(Resource.GRAIN,
                (ImageView)v.findViewById(R.id.trade_out_wheat));
        mResourceToTradeButtonOut.put(Resource.LUMBER,
                (ImageView)v.findViewById(R.id.trade_out_wood));

        mResourceToTradeButtonIn = new HashMap<Resource, ImageView>();
        mResourceToTradeButtonOut.put(Resource.BRICK,
                (ImageView)v.findViewById(R.id.trade_in_brick));
        mResourceToTradeButtonOut.put(Resource.GOLD,
                (ImageView)v.findViewById(R.id.trade_in_gold));
        mResourceToTradeButtonOut.put(Resource.ORE,
                (ImageView)v.findViewById(R.id.trade_in_ore));
        mResourceToTradeButtonOut.put(Resource.WOOL,
                (ImageView)v.findViewById(R.id.trade_in_sheep));
        mResourceToTradeButtonOut.put(Resource.GRAIN,
                (ImageView)v.findViewById(R.id.trade_in_wheat));
        mResourceToTradeButtonOut.put(Resource.LUMBER,
                (ImageView)v.findViewById(R.id.trade_in_wood));
        HashMap<Resource, Integer> mResourcesOut;
        HashMap<Resource, Integer> mCurrentResources;
        HashMap<Resource, Integer> mResourcesIn;

        return v;
    }

}
