package com.SpartanTech.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int year = 0;
		int month = 0;
		int day = 0;
		Bundle dateInfoBundle = this.getArguments();
		java.util.Date inputDate = null;
		Calendar c = Calendar.getInstance();
		if (dateInfoBundle != null) {
			String dateInfo = dateInfoBundle.getString("dateInfo");
				try {
					inputDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(
							dateInfo);
					c.setTime(inputDate);
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}

		} else {
			c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		((AssignmentActivity) getActivity()).writeAssignmentDate(month, day, year);
	}
}
