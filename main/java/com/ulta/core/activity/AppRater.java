package com.ulta.core.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ulta.R;

public class AppRater extends UltaBaseActivity {
	/* private final static String APP_TITLE = "ULTA"; */
	private final static String APP_PNAME = "com.ulta";

	private final static int DAYS_UNTIL_PROMPT = 60;
	private final static int LAUNCHES_UNTIL_PROMPT = 10;

	public static void app_did_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		SharedPreferences.Editor editor = prefs.edit();
		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}
		editor.commit();
	}

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		long launch_count = prefs.getLong("launch_count", 0);
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (prefs.getBoolean("Reminder", false)) {
				if (System.currentTimeMillis() >= date_firstLaunch
						+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
					showRateDialog(mContext, editor);
				}

			} else {
				if (!prefs.getBoolean("dontshowagain", false)) {
					showRateDialog(mContext, editor);
				}
			}
		}

		editor.commit();
	}

	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		String version = "4.0";
		try {
			version = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		final Dialog dialog = new Dialog(mContext,
				R.style.AppCompatAlertDialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.rate_ulta);

		TextView heading = (TextView) dialog.findViewById(R.id.heading);
		heading.setText(mContext.getResources().getString(R.string.rate_ulta)
				+ " " + version);
		TextView messageText = (TextView) dialog
				.findViewById(R.id.rate_ulta_message);
		String messgae = "If you enjoy using Ulta Beauty "
				+ version
				+ ", would you mind taking a moment to rate it? It won't take more than a minute. Thanks for your support!";
		messageText.setText(messgae);

		Button rate_ulta = (Button) dialog.findViewById(R.id.rate_ulta);
		rate_ulta.setText(mContext.getResources().getString(
				R.string.rate_ulta_caps)
				+ " " + version);
		rate_ulta.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=" + APP_PNAME)));
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});
		Button remind_me_later = (Button) dialog
				.findViewById(R.id.remind_me_later);
		remind_me_later.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences prefs = mContext.getSharedPreferences(
						"apprater", 0);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putLong("launch_count", 0);
				editor.putBoolean("Reminder", true);
				editor.commit();
				dialog.dismiss();
			}
		});
		Button no_thanks = (Button) dialog.findViewById(R.id.no_thanks);
		no_thanks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});

		/*
		 * final Dialog dialog = new Dialog(mContext);
		 * 
		 * dialog.setTitle("Rate " + APP_TITLE);
		 * 
		 * LinearLayout ll = new LinearLayout(mContext);
		 * ll.setOrientation(LinearLayout.VERTICAL);
		 * 
		 * TextView tv = new TextView(mContext); tv.setText(
		 * "We like getting feedback.  Please take a moment and rate our app");
		 * tv.setWidth(240); tv.setPadding(4, 0, 4, 10); ll.addView(tv);
		 * 
		 * Button b1 = new Button(mContext); b1.setText("Rate " + APP_TITLE);
		 * b1.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { mContext.startActivity(new
		 * Intent(Intent.ACTION_VIEW, Uri .parse("market://details?id=" +
		 * APP_PNAME))); if (editor != null) {
		 * editor.putBoolean("dontshowagain", true); editor.commit(); }
		 * dialog.dismiss(); } }); ll.addView(b1);
		 * 
		 * Button b2 = new Button(mContext); b2.setText("Remind me later");
		 * b2.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { SharedPreferences prefs =
		 * mContext.getSharedPreferences( "apprater", 0);
		 * SharedPreferences.Editor editor = prefs.edit();
		 * editor.putLong("launch_count", 0); editor.putBoolean("Reminder",
		 * true); editor.commit(); dialog.dismiss(); } }); ll.addView(b2);
		 * 
		 * Button b3 = new Button(mContext); b3.setText("No, thanks");
		 * b3.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { if (editor != null) {
		 * editor.putBoolean("dontshowagain", true); editor.commit(); }
		 * dialog.dismiss(); } }); ll.addView(b3);
		 * 
		 * dialog.setContentView(ll);
		 */
		dialog.show();
	}

}
