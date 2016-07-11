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
 * The Class EarningRedeemingActivity.
 */
public class EarningRedeemingActivity extends UltaBaseActivity
{
	ImageView imgView1, imgView2;
	/*
	 * (non-Javadoc)
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.earning_redeeming);
		setTitle("Earning/Redeeming");
		imgView1=(ImageView)findViewById(R.id.img_earn_redeem_points);
		if(getIntent().getExtras()!=null && getIntent().getExtras().getString("key").equals("benefits"))
		{
			setTitle("Benefits");
			imgView1.setBackgroundResource(R.drawable.benefits_mobile);
			
			/*new AQuery(imgView1).image(
					WebserviceConstants.SCENE_7_BENIFITS, true, true, 0,
					R.drawable.dummy_product, null,
					AQuery.FADE_IN);*/
		}
		else
		{
			imgView1.setBackgroundResource(R.drawable.earn_redeem_mobile);
			/*new AQuery(imgView1).image(
					WebserviceConstants.SCENE_7_EARNINGS, true, true, 0,
					R.drawable.dummy_product, null,
					AQuery.FADE_IN);*/
			
		}
		registerForLogoutBroadcast();
	}

	@Override
	public void onLogout() {
		finish();
	}

}
