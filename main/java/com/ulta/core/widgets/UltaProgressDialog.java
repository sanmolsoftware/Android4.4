package com.ulta.core.widgets;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.ulta.R;

public class UltaProgressDialog extends ProgressDialog {

	public UltaProgressDialog(Context context, String msg) {
		super(context);
		setupDialog(context);
		setMessage(msg);
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@SuppressWarnings("deprecation")
	private void setupDialog(Context context) {
		if (Build.VERSION.SDK_INT >= 21) {
			setIndeterminateDrawable(context.getDrawable(R.drawable.progressdialog_loadingcolor));
		} else {
			setIndeterminateDrawable(context.getResources().getDrawable(
					R.drawable.progressdialog_loadingcolor));
		}
		this.setCancelable(false);
	}
}
