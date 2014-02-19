package com.android.project.utils;

import com.android.project.photo_album.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

public class CustomProgressDialog extends Dialog {

	// private Context mContext;
	View v;

	public CustomProgressDialog(Context context) {

		super(context);
		// mContext = context;
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.custom_dialog_box);
		v = getWindow().getDecorView();
		v.setBackgroundResource(android.R.color.transparent);

		setCancelable(false);

	}


}
