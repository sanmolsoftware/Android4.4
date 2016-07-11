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

/*import android.widget.HorizontalScrollView;*/

public class CouponActivity extends UltaBaseActivity
{
	private List<CouponBean> coupons;
	/*private ImageView couponImage;
	private HorizontalScrollView hvCoupon;*/
	private LinearLayout imageViews;
	private LinearLayout loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupon);

		/*hvCoupon = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		couponImage = (ImageView) findViewById(R.id.CouponImage);*/
		imageViews = (LinearLayout) findViewById(R.id.imageviews);
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		loadingDialog.setVisibility(View.VISIBLE);
		fnInvokeCouponImage();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Reported error on playstore : Fixed (Not required to stop as not started)
//		OmnitureMeasurement.stopActivity();
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
		}
		catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	private Map<String, String> fnCouponImageParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	public class CouponImageHandler extends UltaHandler
	{

		public void handleMessage(Message msg) {
			loadingDialog.setVisibility(View.GONE);
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							CouponActivity.this);
				}
				catch (WindowManager.BadTokenException e) {
				}
				catch (Exception e) {
				}
			}
			else {
				// Logger.Log("<CouponImageHandler><handleMessage><getcouponBean>>"
				// + (getResponseBean()));
				CouponsBean couponsBean = (CouponsBean) getResponseBean();
				coupons = couponsBean.getCoupons();

				if (null != coupons && !coupons.isEmpty()) {

					for (int i = 0; i < couponsBean.getCoupons().size(); i++) {
						LinearLayout llCoupons = new LinearLayout(
								CouponActivity.this);
						llCoupons.setOrientation(LinearLayout.VERTICAL);
						// llCoupons.setPadding(10, 10,10, 10);

						LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
						llCoupons.setLayoutParams(lparams);

						ImageView image = new ImageView(CouponActivity.this);
						TextView txtcoupon = new TextView(CouponActivity.this);
						txtcoupon.setTextColor(getResources().getColor(R.color.riverbed));
						txtcoupon.setTextSize(10);

						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.MATCH_PARENT);
						txtcoupon.setLayoutParams(layoutParams);

						image.setId(i);
						new AQuery(image).image(coupons.get(i).getCouponUrl(),
								true, false, 0, R.drawable.dummy_product,
								null, AQuery.FADE_IN);
						
						image.setLayoutParams(layoutParams);

						// txtcoupon.setText("       Valid in store and online."+"\n"+"Valid through"+
						// coupons.get(i).getCouponExpiry());

						llCoupons.addView(image);
						// llCoupons.addView(txtcoupon);
						imageViews.addView(llCoupons);
						final int temp = i;
						image.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
						
								Intent ZoomIntent = new Intent(
										CouponActivity.this,
										PinchZoomActivity.class);
								ZoomIntent.putExtra("imageUrl",
										coupons.get(temp).getCouponUrl());
								startActivity(ZoomIntent);
							}
						});
					}

				}
				// added else part to display message if coupons are not
				// available.

				else {
					notifyUser("There are no coupons available",
							CouponActivity.this);

				}

			}

		}
	}

}
