package com.android.project.photo_album;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;



public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog mDialog = new DatePickerDialog(getActivity(), this,
				year, month, day);
		mDialog.setTitle(getResources().getString(R.string.set_date));
		mDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources()
				.getString(R.string.set), mDialog);
		
		
		mDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources()
				.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
		});
		
		return mDialog;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {

		// set date in format yyyy-mm-DD

		StringBuilder birth = new StringBuilder();
		birth.append(year).append("-");

		monthOfYear += 1;
		if (monthOfYear < 10) {
			birth.append("0").append(monthOfYear);
		} else {
			birth.append(monthOfYear);
		}

		birth.append("-");
		if (dayOfMonth < 10) {
			birth.append("0").append(dayOfMonth);
		} else {
			birth.append(dayOfMonth);
		}

		
		((EditText) getActivity().findViewById(R.id.created_at))
				.setText(birth);
	}

}