/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.os.Bundle;
import android.widget.ImageView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;


/**
 * The Class PlatinumProgramActivity.
 */
public class PlatinumProgramActivity extends UltaBaseActivity
{

	private ImageView platinumImageView;
	/*
	 * (non-Javadoc)
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.platinum_program);
		setTitle("Platinum Program");
		platinumImageView = (ImageView) findViewById(R.id.imageView1);
		
		platinumImageView.setBackgroundResource(R.drawable.platinum_mobile);
		
		/*new AQuery(platinumImageView).image(
				WebserviceConstants.SCENE_7_BENIFITS, true, true, 0,
				R.drawable.dummy_product, null,
				AQuery.FADE_IN);*/
		/**this code is present till 3.1 release and the web view replaced by image view in 3.2*/
		/*final WebView wvPlatinumRewards = (WebView) findViewById(R.id.wvPlatinumRewards);
		final WebSettings webSettings = wvPlatinumRewards.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wvPlatinumRewards.loadUrl("file:///android_asset/PlatinumRewards.html");*/
		registerForLogoutBroadcast();
	}

	@Override
	public void onLogout() {
		finish();
	}
}
