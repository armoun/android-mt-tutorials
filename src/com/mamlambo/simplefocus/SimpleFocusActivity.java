package com.mamlambo.simplefocus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SimpleFocusActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void numClicked(View v) {
		
		Button butClicked = (Button)v;
		String strButton = butClicked.getText().toString();
		Toast.makeText(getApplicationContext(),
				"Clicked " + strButton, Toast.LENGTH_SHORT).show();
	}
}