package com.SpartanTech.schedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmDelete extends DialogFragment {
	public static ConfirmDelete newInstance(int title, String type) {
		ConfirmDelete frag = new ConfirmDelete();
		Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("type", type);
        frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 int title = getArguments().getInt("title");
		 final String type = getArguments().getString("type");

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if(type == "assignment"){
								((AssignmentActivity) getActivity())
										.doPositiveClick();
								}else if(type == "class"){
									((MainActivity) getActivity()).doPositiveClick();
								}
							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if(type == "assignment"){
									((AssignmentActivity) getActivity())
											.doNegativeClick();
									}else if(type == "class"){
										((MainActivity) getActivity()).doNegativeClick();
									}
							}
						}).create();
	}

}
