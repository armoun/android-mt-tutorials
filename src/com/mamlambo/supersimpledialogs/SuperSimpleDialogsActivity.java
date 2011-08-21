package com.mamlambo.supersimpledialogs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SuperSimpleDialogsActivity extends Activity {

	private static final int MY_SUPER_SIMPLE_DIALOG_ID = 0;
	private static final int MY_SIMPLE_DIALOG_ID = 1;
	private static final int MY_DIALOG_ID = 2;
	
	private Date mCurrentTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onToastButtonClick(View v) {
		Toast.makeText(getApplicationContext(), "Clicked!", Toast.LENGTH_SHORT)
				.show();
	}

	public void onDialogButtonClick(View v) {

		Date dt = new Date(); // Gets the current date/time
		if (dt.getSeconds() % 3 == 0) {
			showDialog(MY_SUPER_SIMPLE_DIALOG_ID);
		} else if (dt.getSeconds() % 3 == 1) {
			showDialog(MY_SIMPLE_DIALOG_ID);
		} else {
			mCurrentTime = dt;
			showDialog(MY_DIALOG_ID);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case MY_SUPER_SIMPLE_DIALOG_ID:
			Dialog superSimpleDlg = new Dialog(this);
			superSimpleDlg.setTitle(R.string.dialog_title);
			return superSimpleDlg;

		case MY_SIMPLE_DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_title);
			builder.setMessage(R.string.dialog_message);
			builder.setIcon(android.R.drawable.btn_star);
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {   
			      public void onClick(DialogInterface dialog, int which) {   
			    		Toast.makeText(getApplicationContext(), "Clicked OK!", Toast.LENGTH_SHORT)
						.show();
			    	  return;   
			    } });  
			return builder.create();

		case MY_DIALOG_ID:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle(R.string.dialog_title);
			builder2.setIcon(android.R.drawable.btn_star);
			builder2.setMessage("");
			builder2.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {   
			      public void onClick(DialogInterface dialog, int which) {   
			    		Toast.makeText(getApplicationContext(), "Clicked OK!", Toast.LENGTH_SHORT)
						.show();
			    	  return;   
			    } });    

			builder2.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {   
			      public void onClick(DialogInterface dialog, int which) {   
			    		Toast.makeText(getApplicationContext(), "Clicked Cancel!", Toast.LENGTH_SHORT)
						.show();
			    	  return;   
			    } });    
			
			return builder2.create();
		}
		return null;

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case MY_SUPER_SIMPLE_DIALOG_ID:
			// Static dialog contents. No initialization needed
			break;
		case MY_SIMPLE_DIALOG_ID:
			// Static dialog contents. No initialization needed
			break;
		case MY_DIALOG_ID:
			// Some initialization needed. Date/time changes each time this dialog is displayed, so update its contents in prepare (not create)
	        AlertDialog myDialog = (AlertDialog) dialog;
	        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        myDialog.setMessage("This dialog was launched at " + dFormat.format(mCurrentTime));
			break;
		}
		return;
	}

}