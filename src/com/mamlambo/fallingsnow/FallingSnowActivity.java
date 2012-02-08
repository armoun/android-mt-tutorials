package com.mamlambo.fallingsnow;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class FallingSnowActivity extends Activity {
	private FallingSnowView snowView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        snowView = new FallingSnowView(this);
        FrameLayout frame = (FrameLayout) findViewById(R.id.snowPreview);
        frame.addView(snowView);

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
    
    public void onWallpaperSettings(View view) {
        Intent wallpaperSettings = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(wallpaperSettings);
    }
}