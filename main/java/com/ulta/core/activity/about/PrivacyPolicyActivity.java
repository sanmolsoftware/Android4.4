/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.about;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;

/**
 * The Class PrivacyPolicyActivity.
 */
public class PrivacyPolicyActivity extends UltaBaseActivity
{
	/** The web view catalog. */
	private WebView webViewCatalog;

	/** The url to fire. */
	private String urlToFire;

	private FrameLayout loadingLayout;

	@Override
	protected void onResume() {
		super.onResume();
		if(UltaDataCache.getDataCacheInstance().isQuestionSubmitted())
		{
			finish();
		}
	}
	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_layout);
		setTitle("Privacy Policy");
		setActivity(PrivacyPolicyActivity.this);
		urlToFire = "http://"
				+ WebserviceConstants.WEBSERVICES_SERVER_ADDRESS_PROD
				+ "/mobile/app_site_privacy.html";
			//	"//ulta/common/privacyPolicyContent.jsp";
		webViewCatalog = (WebView) findViewById(R.id.wvLegal);
		loadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
		webViewCatalog.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.VISIBLE);
		webViewCatalog.setWebViewClient(new WbViewClient());
		webViewCatalog.getSettings().setJavaScriptEnabled(true);
		webViewCatalog.getSettings().setBuiltInZoomControls(false);
		webViewCatalog.getSettings().setUseWideViewPort(true);
		webViewCatalog.getSettings().setSupportMultipleWindows(true);
		webViewCatalog.getSettings().setJavaScriptCanOpenWindowsAutomatically(
				true);
		webViewCatalog.getSettings().setLoadsImagesAutomatically(true);
		webViewCatalog.getSettings().setLightTouchEnabled(true);
		webViewCatalog.getSettings().setDomStorageEnabled(true);
		webViewCatalog.getSettings().setLoadWithOverviewMode(true);
		webViewCatalog.loadUrl(urlToFire);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( event.getAction() == KeyEvent.ACTION_DOWN ) {
			switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if ( webViewCatalog.canGoBack() == true ) {
						webViewCatalog.goBack();
					}
					else {
						finish();
					}
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * The Class WbViewClient.
	 */
	private class WbViewClient extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.contains("mailto"))
			{
				String []email=url.split(":");
				Intent send = new Intent(Intent.ACTION_SEND);
				send.putExtra(Intent.EXTRA_EMAIL, email[1]);		  
				send.putExtra(Intent.EXTRA_SUBJECT, "subject");
				send.putExtra(Intent.EXTRA_TEXT, "message");
				send.setType("message/rfc822");
				startActivity(Intent.createChooser(send, "Choose an Email client :"));
			}
			else
			{
				view.loadUrl(url);
			}

			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			loadingLayout.setVisibility(View.GONE);
			webViewCatalog.setVisibility(View.VISIBLE);
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
		if(webViewCatalog.canGoBack())
		{
			webViewCatalog.goBack();
		}
		else {
			finish();
		}
	}
}
