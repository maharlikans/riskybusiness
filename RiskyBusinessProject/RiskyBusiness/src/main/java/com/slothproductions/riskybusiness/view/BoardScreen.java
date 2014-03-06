package com.slothproductions.riskybusiness.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout.LayoutParams;

import com.View.R;
import com.slothproductions.riskybusiness.model.DiceRoll;


public class BoardScreen extends FragmentActivity {

    private Fragment mBoardScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_screen);

        FragmentManager fm = getSupportFragmentManager();
        mBoardScreenFragment = fm.findFragmentById(R.id.BoardContainer);

        if (mBoardScreenFragment == null) {
            mBoardScreenFragment = new BoardScreenMainFragment();
            fm.beginTransaction()
                    .add(R.id.BoardContainer, mBoardScreenFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.board_screen, menu);
        return true;
    }

    public void placeCornerObject(MotionEvent event) {
        ImageView mTempCity = new ImageView(this);
        mTempCity.setId((int)System.currentTimeMillis());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

        int x = (int)event.getX();
        int y = (int)event.getY();

        lp.leftMargin = x;
        lp.topMargin = y;

        mTempCity.setImageResource(getResources().getIdentifier("city", "drawable", getPackageName()));
        RelativeLayout mMainBoardFragment = (RelativeLayout)findViewById(R.id.mainBoardFragment);
        mMainBoardFragment.addView(mTempCity, lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        placeCornerObject(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("KEYPRESSED", "back button was pressed");
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Save Game?");
        alertDialog.setMessage("Do you want to save this game?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Save the game state here
                Toast.makeText(BoardScreen.this, "Game Saved",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BoardScreen.this, "Game was not saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //return to screen
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        Log.d("ONSTART", "On Start Called");
        super.onStart();
        Log.d("ONSTART", "On Start Finished");
    }
}
