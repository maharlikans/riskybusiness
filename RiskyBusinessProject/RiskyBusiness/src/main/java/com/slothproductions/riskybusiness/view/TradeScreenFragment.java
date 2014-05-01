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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.View.R;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Resource;

import java.util.HashMap;

/**
 * Created by User on 3/23/2014.
 */
public class TradeScreenFragment extends Fragment {

    Spinner mTypeTradeSpinner;
    Spinner mPlayerTradeSelectSpinner;
    ImageView mRequestTradeButton;
    ImageView mCancelTradeButton;
    ImageView mConfirmTradeButton;
    BoardScreen mBoardScreen;
    GameLoop mGameLoop;
    Player mCurrentPlayer;
    HashMap<Resource, ImageView> mResourceToTradeButtonOut;
    HashMap<Resource, ImageView> mResourceToCenterButton;
    HashMap<Resource, ImageView> mResourceToTradeButtonIn;
    HashMap<Resource, Integer> mResourcesOut;
    HashMap<Resource, Integer> mCurrentResources;
    HashMap<Resource, Integer> mResourcesIn;

    int mNumItemsAllowedIn;
    int mNumItemsIn;

    public enum TradeState {
        BANK_TRADE,
        PRIVATE_TRADE,
    }

    public enum PlayerTradeState {
        CONFIRMING_TRADE,
        REQUESTING_TRADE
    }

    TradeState mTradeState;
    PlayerTradeState mPlayerTradeState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "OnCreate was called in TradeScreenFragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trade_screen, parent, false);

        mBoardScreen = (BoardScreen)getActivity();
        mCurrentPlayer = mBoardScreen.getGameLoop().getCurrentPlayer();

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

        mCancelTradeButton = (ImageView)v.findViewById(R.id.cancel_trade_button);

        TableLayout tradeBack = (TableLayout)v.findViewById(R.id.tradeback);
        tradeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing!
            }
        });

        mCancelTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BoardScreen) getActivity()).onCancelTradeButtonPressed();
            }
        });

        mRequestTradeButton = (ImageView)v.findViewById(R.id.request_trade_button);
        mRequestTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO actually implement the requestTrade onClick listener
            }
        });

        mConfirmTradeButton = (ImageView)v.findViewById(R.id.confirm_trade_button);
        mConfirmTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO actually implement the confirmTradeButton
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

        mResourceToCenterButton = new HashMap<Resource, ImageView>();
        mResourceToCenterButton.put(Resource.BRICK,
                (ImageView)v.findViewById(R.id.bricktradeicon));
        mResourceToTradeButtonOut.put(Resource.GOLD,
                (ImageView)v.findViewById(R.id.goldtradeicon));
        mResourceToTradeButtonOut.put(Resource.ORE,
                (ImageView)v.findViewById(R.id.oretradeicon));
        mResourceToTradeButtonOut.put(Resource.WOOL,
                (ImageView)v.findViewById(R.id.sheeptradeicon));
        mResourceToTradeButtonOut.put(Resource.GRAIN,
                (ImageView)v.findViewById(R.id.wheattradeicon));
        mResourceToTradeButtonOut.put(Resource.LUMBER,
                (ImageView)v.findViewById(R.id.woodtradeicon));

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


        mResourcesOut = new HashMap<Resource, Integer>();
        mResourcesOut.put(Resource.BRICK, 0);
        mResourcesOut.put(Resource.GOLD, 0);
        mResourcesOut.put(Resource.ORE, 0);
        mResourcesOut.put(Resource.WOOL, 0);
        mResourcesOut.put(Resource.GRAIN, 0);
        mResourcesOut.put(Resource.LUMBER, 0);

        mCurrentResources = new HashMap<Resource, Integer>();
        HashMap<Resource, Integer> tempMap =
                (HashMap<Resource, Integer>)mGameLoop.getCurrentPlayer().getResources();
        // here I am duplicating the player resources HashMap so that I don't mess up the player
        // resources in the model
        for (Resource r : Resource.values()) {
            mCurrentResources.put(r, tempMap.get(r));
        }

        mResourcesIn = new HashMap<Resource, Integer>();
        mResourcesIn.put(Resource.BRICK, 0);
        mResourcesIn.put(Resource.GOLD, 0);
        mResourcesIn.put(Resource.ORE, 0);
        mResourcesIn.put(Resource.WOOL, 0);
        mResourcesIn.put(Resource.GRAIN, 0);
        mResourcesIn.put(Resource.LUMBER, 0);

        return v;
    }

    public void setPlayerTradeState(PlayerTradeState playerTradeState) {
        mPlayerTradeState = playerTradeState;
        if (mPlayerTradeState == PlayerTradeState.CONFIRMING_TRADE) {
            mTypeTradeSpinner.setVisibility(View.GONE);
            mRequestTradeButton.setVisibility(View.GONE);
            mConfirmTradeButton.setVisibility(View.VISIBLE);
        } else if (mPlayerTradeState == PlayerTradeState.REQUESTING_TRADE) {
            mTypeTradeSpinner.setVisibility(View.VISIBLE);
            mRequestTradeButton.setVisibility(View.VISIBLE);
            mConfirmTradeButton.setVisibility(View.GONE);
        }
    }

    public void setTradeState(TradeState tradeState) {
        mTradeState = tradeState;
        if (mTradeState == TradeState.BANK_TRADE) {
            mNumItemsAllowedIn = 0;
            mNumItemsIn = 0;
        } else if (mTradeState == TradeState.PRIVATE_TRADE) {
            mNumItemsAllowedIn = Integer.MAX_VALUE;
            mNumItemsIn = 0;
        }
    }
}
