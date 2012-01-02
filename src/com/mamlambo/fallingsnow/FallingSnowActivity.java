package com.mamlambo.fallingsnow;


import android.app.Activity;
import android.os.Bundle;

public class FallingSnowActivity extends Activity {
	private FallingSnowView snowView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snowView = new FallingSnowView(this);
        setContentView(snowView);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        snowView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snowView.pause();
    }
}