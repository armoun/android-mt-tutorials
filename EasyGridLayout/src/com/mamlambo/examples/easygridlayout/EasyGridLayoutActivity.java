package com.mamlambo.examples.easygridlayout;

import android.app.Activity;
import android.os.Bundle;

public class EasyGridLayoutActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // a colorful grid layout, for fun
        //setContentView(R.layout.main);
        
        // This layout starts with the 1 button and specially places the / button
        //setContentView(R.layout.number_pad);
        
        // This layout starts with the / button, no extra special placement needed
        setContentView(R.layout.number_pad2);
        
        // this has no column or row spans, no special placements
        //setContentView(R.layout.simple_numbers);
    }
}