package com.mamlambo.supersimpledialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

public class SuperSimpleDialogsActivity extends Activity {

	private static final int MY_SUPER_SIMPLE_DIALOG_ID = 0;
	private static final int MY_SIMPLE_DIALOG_ID = 1;
	private static final int MY_DIALOG_ID = 2;

	private static final int MY_DATE_DIALOG_ID = 3;

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

	public void onDateDialogButtonClick(View v) {
		showDialog(MY_DATE_DIALOG_ID);
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
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getApplicationContext(),
									"Clicked OK!", Toast.LENGTH_SHORT).show();
							return;
						}
					});
			return builder.create();

		case MY_DIALOG_ID:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle(R.string.dialog_title);
			builder2.setIcon(android.R.drawable.btn_star);
			builder2.setMessage("");
			builder2.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getApplicationContext(),
									"Clicked OK!", Toast.LENGTH_SHORT).show();
							return;
						}
					});

			builder2.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getApplicationContext(),
									"Clicked Cancel!", Toast.LENGTH_SHORT)
									.show();
							return;
						}
					});

			return builder2.create();
			
			
		case MY_DATE_DIALOG_ID:
			DatePickerDialog dateDlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Time chosenDate = new Time();        
                    chosenDate.set(dayOfMonth, monthOfYear, year);
                    long dtDob = chosenDate.toMillis(true);
                    CharSequence strDate = DateFormat.format("MMMM dd, yyyy", dtDob);
                    Toast.makeText(SuperSimpleDialogsActivity.this, "Date picked: " + strDate, Toast.LENGTH_SHORT).show();
                }
            }, 2011,0, 1);
            
			dateDlg.setMessage("When's Your Birthday?");

			return dateDlg;
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
			// Some initialization needed. Date/time changes each time this
			// dialog is displayed, so update its contents in prepare (not
			// create)
			AlertDialog myDialog = (AlertDialog) dialog;
			SimpleDateFormat dFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			myDialog.setMessage("This dialog was launched at "
					+ dFormat.format(mCurrentTime));
			break;
			
		case MY_DATE_DIALOG_ID:
			DatePickerDialog dateDlg = (DatePickerDialog) dialog;
            int iDay,iMonth,iYear;

            // Always init the date picker to today's date
            Calendar cal = Calendar.getInstance();
            iDay = cal.get(Calendar.DAY_OF_MONTH);
            iMonth = cal.get(Calendar.MONTH);
            iYear = cal.get(Calendar.YEAR);
            dateDlg.updateDate(iYear, iMonth, iDay);
			break;
		}
		return;
	}

}