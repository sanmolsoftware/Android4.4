/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.PinchZoomActivity;
import com.ulta.core.bean.account.CouponBean;
import com.ulta.core.bean.account.CouponsBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class LegalActivity.
 */
public class ClubUltaActivity extends UltaBaseActivity {

	private LinearLayout imageViews, llmemberId, llRewardsCard;
	private List<CouponBean> coupons;
	private TextView tvmemberId;
	private String memberId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_ulta);
		setActivity(ClubUltaActivity.this);
		setTitle("The Club at Ulta");
		tvmemberId = (TextView) findViewById(R.id.tvmemberId);
		imageViews = (LinearLayout) findViewById(R.id.imageviews);
		llmemberId = (LinearLayout) findViewById(R.id.llmemberId);
		llRewardsCard = (LinearLayout) findViewById(R.id.lytViewMyMemberCard);
		/**
		 * WEBVIEW replaced by image view in 3.2 release Code commented for
		 * futur use
		 */
		// final WebView wvLegal = (WebView) findViewById(R.id.wvLegal);
		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("MemberId")) {
				tvmemberId.setText(getIntent().getExtras()
						.getString("MemberId"));
				memberId = getIntent().getExtras().getString("MemberId");
				llRewardsCard.setVisibility(View.VISIBLE);
				llRewardsCard.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent viewMyRewardCard = new Intent(
								ClubUltaActivity.this,
								ViewMyRewardsCardActivity.class);
						viewMyRewardCard.putExtra("from", "clubatulta");
						if (memberId != null) {
							viewMyRewardCard.putExtra("MemberId", memberId);
						}
						startActivity(viewMyRewardCard);
					}
				});
			} else {
				llmemberId.setVisibility(View.GONE);
			}
		}
		/**
		 * Web View replaced by image view in 3.2 release Code commented for
		 * future use
		 */
		/*
		 * wvLegal.setClickable(false); wvLegal.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * } }); wvLegal.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { return
		 * true; } }); wvLegal.loadUrl("file:///android_asset/ClubUlta.html");
		 */
		fnInvokeCouponImage();
	}

	private void fnInvokeCouponImage() {
		InvokerParams<CouponsBean> invokerParams = new InvokerParams<CouponsBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.COUPON_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(fnCouponImageParameters());
		invokerParams.setUltaBeanClazz(CouponsBean.class);
		CouponImageHandler couponImageHandler = new CouponImageHandler();
		invokerParams.setUltaHandler(couponImageHandler);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	private Map<String, String> fnCouponImageParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	public class CouponImageHandler extends UltaHandler {

		public void handleMessage(Message msg) {

			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							ClubUltaActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				// Logger.Log("<CouponImageHandler><handleMessage><getcouponBean>>"
				// + (getResponseBean()));
				CouponsBean couponsBean = (CouponsBean) getResponseBean();
				coupons = couponsBean.getCoupons();

				if (null != coupons) {

					for (int i = 0; i < couponsBean.getCoupons().size(); i++) {
						ImageView image = new ImageView(ClubUltaActivity.this);
						image.setId(i);
						new AQuery(image).image(coupons.get(i).getCouponUrl(),
								true, false, 200, R.drawable.dummy_product,
								null, AQuery.FADE_IN);
						imageViews.addView(image);
						final int temp = i;
						image.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {

								Intent ZoomIntent = new Intent(
										ClubUltaActivity.this,
										PinchZoomActivity.class);
								ZoomIntent.putExtra("imageUrl",
										coupons.get(temp).getCouponUrl());
								startActivity(ZoomIntent);
							}
						});
					}

				}

			}

		}
	}
}
