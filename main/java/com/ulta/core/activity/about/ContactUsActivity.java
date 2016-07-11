/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.about;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.widgets.flyin.OnPermissionCheck;

/**
 * The Class ContactUsActivity.
 */
public class ContactUsActivity extends UltaBaseActivity implements OnPermissionCheck {

	private TextView tvCall;
	private TextView tvMail;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contact_us);
		setActivity(ContactUsActivity.this);
		setTitle("Contact Us");
		tvCall = (TextView) findViewById(R.id.txt_call);
		tvMail = (TextView) findViewById(R.id.tvMail);
		tvCall.setPaintFlags(tvCall.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		tvMail.setPaintFlags(tvCall.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		LinearLayout emaiulta = (LinearLayout) findViewById(R.id.emailulta);
		setActivity(ContactUsActivity.this);

		emaiulta.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent(Intent.ACTION_SEND);
				 * intent.setType("plain/text");
				 * intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
				 * UltaConstants.EMAIL_CUSTOMER_CARE });
				 * intent.putExtra(Intent.EXTRA_SUBJECT, "Contact us");
				 * intent.putExtra(Intent.EXTRA_TEXT,
				 * UltaConstants.SENT_FROM_ANDROID);
				 * startActivity(Intent.createChooser(intent, "ULTA"));
				 */
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				intent.putExtra(Intent.EXTRA_EMAIL,
						new String[]{UltaConstants.EMAIL_CUSTOMER_CARE});
				try {
					intent.putExtra(
							Intent.EXTRA_SUBJECT,
							"Feedback from Ulta Beauty Android Version "
									+ getPackageManager().getPackageInfo(
									getPackageName(), 0).versionName);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				intent.putExtra(Intent.EXTRA_TEXT,
						UltaConstants.SENT_FROM_ANDROID);
				startActivity(Intent.createChooser(intent, "ULTA"));
			}
		});
		LinearLayout callLayout = (LinearLayout) findViewById(R.id.layout_call);
		callLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkForAppPermissions(getApplicationContext(), WebserviceConstants.PERMISSION_CALL_PHONE, WebserviceConstants.PHONE_REQUEST_CODE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_TITLE, WebserviceConstants.PERMISSION_CALL_PHONE_DIALOG_MESSAGE);
			}
		});

	}
	/**
	 * App permission check result for phone
	 *
	 * @param isSuccess
	 * @param permissionRequestCode
	 */
	@Override
	public void onPermissionCheckRequest(boolean isSuccess, int permissionRequestCode) {
		if (isSuccess) {
			if (permissionRequestCode == WebserviceConstants.PHONE_REQUEST_CODE) {
				Intent dialIntent = new Intent();
				dialIntent.setAction(Intent.ACTION_DIAL);
				dialIntent.setData(Uri.parse("tel:"
						+ WebserviceConstants.ULTA_PHONE_NUMBER));
				startActivity(dialIntent);

			}
		}

	}
}