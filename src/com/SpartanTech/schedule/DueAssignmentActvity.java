package com.SpartanTech.schedule;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DueAssignmentActvity extends Activity {

	private Context context = this;
	private int tempClassCount;
	private int tempAssignmentCount;
	private String tempClassName;
	private ScrollView scroll;
	private LinearLayout dueLayout;
	private LinearLayout.LayoutParams Params;
	private Calendar currentCal;
	private Calendar assignmentCal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_due_assignment);
		scroll = (ScrollView) findViewById(R.id.dueMainScroll);
		Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		getAssignments();
	}

	private boolean checkDates(Long Index) {
		currentCal = Calendar.getInstance();
		assignmentCal = Calendar.getInstance();
		assignmentCal.setTimeInMillis(Index);
		if (currentCal.get(Calendar.DAY_OF_YEAR) + 1 == assignmentCal
				.get(Calendar.DAY_OF_YEAR)) {
			return true;
		}
		return false;
	}

	private void getAssignments() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		scroll.removeView(dueLayout);
		dueLayout = new LinearLayout(this);
		dueLayout.setOrientation(LinearLayout.VERTICAL);
		dueLayout.setLayoutParams(Params);
		tempClassCount = 0;
		while (pref.contains("class" + Integer.toString(tempClassCount))) {
			tempClassName = pref.getString(
					"class" + Integer.toString(tempClassCount), "")
					+ "assignment";
			tempAssignmentCount = 0;
			while (pref.contains(tempClassName
					+ Integer.toString(tempAssignmentCount))) {
				if (checkDates(pref
						.getLong(
								tempClassName
										+ pref.getString(
												tempClassName
														+ Integer
																.toString(tempAssignmentCount),
												"") + "MilliDate", 0))) {
					TextView newDisplay = new TextView(this);
					newDisplay.setId(tempAssignmentCount);
					newDisplay.setTextSize(20);
					newDisplay
							.setText(pref.getString(
									tempClassName
											+ Integer
													.toString(tempAssignmentCount),
									""));
					dueLayout.addView(newDisplay);
				}
				tempAssignmentCount = tempAssignmentCount + 1;
			}
			tempClassCount = tempClassCount + 1;
		}
		scroll.addView(dueLayout);
	}

}
