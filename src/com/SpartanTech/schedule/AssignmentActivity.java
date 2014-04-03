package com.SpartanTech.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
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

public class AssignmentActivity extends Activity {

	final Context context = this;
	ScrollView scroll;
	LinearLayout assignmentLayout;
	String assignmentIndex;
	String tempAssignmentBin;
	int tempAssignmentCount;
	int assignmentCount;
	int assignmentDeleteId;
	LinearLayout.LayoutParams Params;
	String assignmentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			assignmentDate = pref.getString(assignmentIndex + + v.getId() + "assignmentDate", null);

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
			TextView newDisplay = new TextView(this);
			newDisplay.setId(assignmentCount);
			newDisplay.setOnClickListener(assignmentClick);
			newDisplay.setOnLongClickListener(deleteAssignment);
			newDisplay.setTextSize(15);
			newDisplay.setText(pref.getString(
					assignmentIndex + Integer.toString(assignmentCount),
					"Default"));
			assignmentLayout.addView(newDisplay);
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
	void showConfirm() {
	    DialogFragment newFragment = ConfirmDelete.newInstance(R.string.confirm_delete_assignment, "assignment");
	    newFragment.show(getFragmentManager(), "dialog");
	}

	public void doPositiveClick() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		tempAssignmentCount = 0;
		while (pref.contains(assignmentIndex
				+ Integer.toString(tempAssignmentCount))) {
			if (tempAssignmentCount > assignmentDeleteId) {
				tempAssignmentBin = pref.getString(assignmentIndex
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
	}

	public void doNegativeClick() {
	    
	}
}