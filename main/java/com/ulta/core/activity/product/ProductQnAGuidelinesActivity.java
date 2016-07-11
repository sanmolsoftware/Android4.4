/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;

/**
 * The Class LegalActivity.
 */
public class ProductQnAGuidelinesActivity extends UltaBaseActivity
{
	FrameLayout loadingLayout;
	WebView wvLegal;
	/*
	 * (non-Javadoc)
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivity(ProductQnAGuidelinesActivity.this);
		setContentView(R.layout.web_view_layout);
		setTitle("Q & A Guidelines");
		wvLegal = (WebView) findViewById(R.id.wvLegal);

		loadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
		wvLegal.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.VISIBLE);
		final WebSettings webSettings = wvLegal.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wvLegal.setClickable(false);
		wvLegal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		
			}
		});
		// 3.2 Release
		String urlToFire = "http://"
				+ WebserviceConstants.WEBSERVICES_SERVER_ADDRESS
				+ "/ulta/common/askUltaGuidelines.jsp";
		Log.e("Loading Url", urlToFire);
		wvLegal.loadUrl(urlToFire);
		wvLegal.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
				
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, final String url) {
				loadingLayout.setVisibility(View.GONE);
				wvLegal.setVisibility(View.VISIBLE);
			}
		});

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
