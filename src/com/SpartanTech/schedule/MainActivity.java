package com.SpartanTech.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	int classCount;
	int tempClassCount;
	final Context context = this;
	LinearLayout classLayout;
	LinearLayout.LayoutParams Params;
	ScrollView scroll1;
	String assignmentIndex;
	String tempClassBin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scroll1 = (ScrollView) findViewById(R.id.classMainScroll);
		Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		loadClass();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.add_class:
			LayoutInflater li = LayoutInflater.from(context);
			View promptsView = li.inflate(R.layout.prompt_class, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);

			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
					.findViewById(R.id.classInput);

			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									writeClass(userInput.getText().toString());
									loadClass();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	OnClickListener classClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			TextView className = (TextView)findViewById(v.getId());
			assignmentIndex = className.getText().toString() + "assignment";
			Intent intent = new Intent(context, AssignmentActivity.class);
			intent.putExtra("Index", assignmentIndex);
			startActivity(intent);
		}

	};

	OnLongClickListener deleteClass = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			tempClassCount = 0;
			while (pref.contains("class" + Integer.toString(tempClassCount))) {
				if (tempClassCount > v.getId()) {
					tempClassBin = pref.getString(
							"class" + Integer.toString(tempClassCount),
							"Transfer Error");
					pref.edit()
							.remove("class" + Integer.toString(tempClassCount))
							.putString(
									"class"
											+ Integer
													.toString(tempClassCount - 1),
									tempClassBin).commit();
				} else if (tempClassCount == v.getId()) {
					pref.edit()
							.remove("class" + Integer.toString(tempClassCount))
							.commit();
				}
				tempClassCount = tempClassCount + 1;
			}
			loadClass();
			return false;
		}
	};

	private void loadClass() {
		scroll1.removeView(classLayout);
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		classLayout = new LinearLayout(this);
		classLayout.setOrientation(LinearLayout.VERTICAL);
		classLayout.setLayoutParams(Params);
		classCount = 0;
		while (pref.contains("class" + Integer.toString(classCount))) {
			TextView newClass = new TextView(this);
			newClass.setId(classCount);
			newClass.setOnClickListener(classClick);
			newClass.setOnLongClickListener(deleteClass);
			newClass.setTextSize(30);
			newClass.setText(pref.getString(
					"class" + Integer.toString(classCount), "Default"));
			classLayout.addView(newClass);
			classCount = classCount + 1;
		}
		scroll1.addView(classLayout);
	}

	private void writeClass(String userInput) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		pref.edit().putString("class" + classCount, userInput).commit();
	}

}
