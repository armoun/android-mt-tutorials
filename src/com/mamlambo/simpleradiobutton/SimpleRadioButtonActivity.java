package com.mamlambo.simpleradiobutton;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SimpleRadioButtonActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        RadioGroup rGroup3 = (RadioGroup) findViewById(R.id.radio_group3);
        OnClickListener radio_listener = new OnClickListener() {   

			public void onClick(View v) {
				onRadioButtonClick(v);
			}
        };
        
        RadioButton button1 = new RadioButton(this);
        button1.setText(R.string.rad_option1);
        button1.setTextColor(Color.RED);
        button1.setOnClickListener(radio_listener);     
        rGroup3.addView(button1);

        RadioButton button2 = new RadioButton(this);
        button2.setText(R.string.rad_option2);
        button2.setTextColor(Color.GREEN);
        button2.setOnClickListener(radio_listener);  
        rGroup3.addView(button2);
        
        RadioButton button3 = new RadioButton(this);
        button3.setText(R.string.rad_option3);
        button3.setTextColor(Color.BLUE);
        button3.setOnClickListener(radio_listener);  
        rGroup3.addView(button3);
        
        rGroup3.check(button1.getId());
        
    }
    

    
    public void onRadioButtonClick(View v) {             
    	RadioButton button = (RadioButton) v;   
    	Toast.makeText(SimpleRadioButtonActivity.this, button.getText() + " was chosen.", Toast.LENGTH_SHORT).show();  
    }
    
}