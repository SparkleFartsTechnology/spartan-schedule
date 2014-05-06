package com.SpartanTech.schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentActivity extends Activity {

	private final Context context = this;
	private ScrollView scroll;
	private LinearLayout assignmentLayout;
	private String assignmentIndex;
	private String tempAssignmentBin;
	private String displayText;
	private String assignmentDate;
	private String assignmentDateName;
	private int tempAssignmentCount;
	private int assignmentCount;
	private int assignmentDeleteId;
	private LinearLayout.LayoutParams Params;
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	private Calendar cal;
	private DateFormat df;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		df = DateFormat.getDateInstance(DateFormat.SHORT);
		setContentView(R.layout.activity_assignment);
		scroll = (ScrollView) findViewById(R.id.assignmentMainScroll);
		Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			assignmentIndex = (String) bundle.get("Index");
		}
		loadAssignment();
	}

	OnClickListener assignmentClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			TextView assignmentText = (TextView) findViewById(v.getId());
			assignmentDateName = assignmentText.getText().toString();
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.show(getFragmentManager(), "Set Due Date");
		}
	};

	OnLongClickListener deleteAssignment = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			assignmentDeleteId = v.getId();
			showConfirm();
			return false;
		}

	};

	public void addAssignment(View v) {
		LayoutInflater li = LayoutInflater.from(context);
		View promptsView = li.inflate(R.layout.prompt_assignment, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.assignmentInput);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						writeAssignment(userInput.getText().toString());
						loadAssignment();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	public void setReminderTime(int hourOfDay, int minute) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);

		if (alarmMgr != null) {
			alarmMgr.cancel(alarmIntent);
		}

		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				alarmIntent);
	}

	public void setReminder(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "Select Time for Reminder");

	}

	private void loadAssignment() {
		scroll.removeView(assignmentLayout);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		assignmentLayout = new LinearLayout(this);
		assignmentLayout.setOrientation(LinearLayout.VERTICAL);
		assignmentLayout.setLayoutParams(Params);
		assignmentCount = 0;
		while (pref.contains(assignmentIndex
				+ Integer.toString(assignmentCount))) {
			displayText = pref.getString(
					assignmentIndex + Integer.toString(assignmentCount),
					"Default");
			TextView newDisplay = new TextView(this);
			newDisplay.setId(assignmentCount);
			newDisplay.setOnClickListener(assignmentClick);
			newDisplay.setOnLongClickListener(deleteAssignment);
			newDisplay.setTextSize(15);
			newDisplay.setText(displayText);
			if (pref.contains(assignmentIndex + displayText + "Date")) {
				LinearLayout dateLayout = new LinearLayout(this);
				TextView newDate = new TextView(this);
				dateLayout.setOrientation(LinearLayout.HORIZONTAL);
				newDate.setText(" Due Date:"
						+ pref.getString(
								assignmentIndex + displayText + "Date", ""));
				dateLayout.addView(newDisplay);
				dateLayout.addView(newDate);
				assignmentLayout.addView(dateLayout);
			} else {
				assignmentLayout.addView(newDisplay);
			}
			assignmentCount = assignmentCount + 1;
		}
		scroll.addView(assignmentLayout);
	}

	private void writeAssignment(String userInput) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		pref.edit().putString(assignmentIndex + assignmentCount, userInput)
				.commit();
	}

	public void writeAssignmentDate(int month, int day) {
		cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		assignmentDate = df.format(cal.getTime());
		System.out.println(assignmentDate);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		System.out.println(assignmentIndex + assignmentDateName + "Date");
		pref.edit()
				.putString(assignmentIndex + assignmentDateName + "Date",
						assignmentDate)
				.putLong(assignmentIndex + assignmentDateName + "MilliDate",
						cal.getTimeInMillis()).commit();
		loadAssignment();

	}

	void showConfirm() {
		DialogFragment newFragment = ConfirmDelete.newInstance(
				R.string.confirm_delete_assignment, "assignment");
		newFragment.show(getFragmentManager(), "dialog");
	}

	public void PositiveConfirm() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		tempAssignmentCount = 0;
		while (pref.contains(assignmentIndex
				+ Integer.toString(tempAssignmentCount))) {
			if (tempAssignmentCount > assignmentDeleteId) {
				tempAssignmentBin = pref
						.getString(
								assignmentIndex
										+ Integer.toString(tempAssignmentCount),
								"Transfer Error");
				pref.edit()
						.remove(assignmentIndex
								+ Integer.toString(tempAssignmentCount))
						.putString(
								assignmentIndex
										+ Integer
												.toString(tempAssignmentCount - 1),
								tempAssignmentBin).commit();
			} else if (tempAssignmentCount == assignmentDeleteId) {
				pref.edit()
						.remove(assignmentIndex
								+ Integer.toString(tempAssignmentCount))
						.commit();
			}
			tempAssignmentCount = tempAssignmentCount + 1;

		}
		loadAssignment();
		Toast.makeText(context, "Assignment Deleted", Toast.LENGTH_LONG).show();
	}
}