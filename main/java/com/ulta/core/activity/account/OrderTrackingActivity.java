/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class OrderTrackingActivity extends UltaBaseActivity
{
	/** String tracking URL declaration */
	private String trackingURL;
	/** Progress dialog declaration */
	private ProgressDialog pd;
	/** Web view declaration */
	private WebView webView;
	private boolean redirect, loadingFinished;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("TrackingURL")) {
				trackingURL = getIntent().getExtras().getString("TrackingURL");
			}
		}
		trackAppState(
				OrderTrackingActivity.this,
				WebserviceConstants.ACCOUNT_ORDER_HISTORY_TRACK);
		if (trackingURL != null && !trackingURL.isEmpty()) {
			webView = (WebView) findViewById(R.id.webview);
			// webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(trackingURL);
			pd = new ProgressDialog(OrderTrackingActivity.this);
			setProgressDialogLoadingColor(pd);
			pd.setMessage(LOADING_PROGRESS_TEXT);
			pd.setCancelable(false);
			pd.show();

			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view,
						String urlNewString) {
					if (!loadingFinished) {
						redirect = true;
					}

					loadingFinished = false;
					webView.loadUrl(urlNewString);
					return true;
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					try {
						if (!redirect) {
							loadingFinished = true;
						}

						if (loadingFinished && !redirect) {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
						}
						else {
							redirect = false;
						}
					} catch (Exception e) {
									e.printStackTrace();
					}

				}
			});
		}
		else {
			finish();
		}
	}
	public void setUpToolBar() {
		Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);

		if (null != toolbarTop) {
			toolbarTop.setNavigationIcon(R.drawable.hamburger_icon);
			toolbarTop.setNavigationOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					sideMenuClick();
				}
			});
		}
	}
	@Override
	public void onBackPressed() {
		if(webView.canGoBack())
		{
			webView.goBack();
		}
		else {
			finish();
		}
	}
}
