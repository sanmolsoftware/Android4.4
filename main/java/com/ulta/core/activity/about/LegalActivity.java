/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;

/**
 * The Class LegalActivity.
 */
public class LegalActivity extends UltaBaseActivity {
	FrameLayout loadingLayout;
	WebView wvLegal;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (UltaDataCache.getDataCacheInstance().isQuestionSubmitted()) {
			finish();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivity(LegalActivity.this);
		setContentView(R.layout.web_view_layout);
		setTitle("Legal");
		wvLegal = (WebView) findViewById(R.id.wvLegal);

		loadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
		wvLegal.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.VISIBLE);
		final WebSettings webSettings = wvLegal.getSettings();
		webSettings.setPluginState(WebSettings.PluginState.ON);
		wvLegal.setClickable(false);
		wvLegal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
			}
		});
		wvLegal.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("mailto")) {
					String[] email = url.split(":");
					Intent send = new Intent(Intent.ACTION_SEND);
					send.putExtra(Intent.EXTRA_EMAIL, email[1]);
					send.putExtra(Intent.EXTRA_SUBJECT, "subject");
					send.putExtra(Intent.EXTRA_TEXT, "message");
					send.setType("message/rfc822");
					startActivity(Intent.createChooser(send,
							"Choose an Email client :"));
				} else {
					view.loadUrl(url);
				}

				return true;
			}

			@Override
			public void onPageFinished(WebView view, final String url) {
				loadingLayout.setVisibility(View.GONE);
				wvLegal.setVisibility(View.VISIBLE);
			}
		});

		// 3.2 Release
		String urlToFire = "http://"
				+ WebserviceConstants.WEBSERVICES_SERVER_ADDRESS_PROD
				+ "/ulta/common/user_agreement_mobile_content.html";
		wvLegal.loadUrl(urlToFire);
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
		if(wvLegal.canGoBack())
		{
			wvLegal.goBack();
		}
		else {
			finish();
		}
	}
}
