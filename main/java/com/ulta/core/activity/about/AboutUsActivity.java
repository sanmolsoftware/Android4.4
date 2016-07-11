/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.about;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;

import java.util.Calendar;

/**
 * The Class AboutUsActivity.
 */
public class AboutUsActivity extends UltaBaseActivity {

	/** The url to fire. */
	/** The web view catalog. */
	private WebView webViewCatalog;
	private FrameLayout loadingLayout;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_layout);
		setTitle("About");
		setActivity(AboutUsActivity.this);
		webViewCatalog = (WebView) findViewById(R.id.wvLegal);
		loadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
		loadingLayout.setVisibility(View.GONE);
		webViewCatalog.setVisibility(View.VISIBLE);
		// webViewCatalog.setWebViewClient(new WbViewClient());
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
		// webViewCatalog.loadUrl(urlToFire);
		String copyRight = "Copyright 2000-"
				+ Calendar.getInstance().get(Calendar.YEAR)
				+ " Ulta Beauty Salon, Cosmetics & Fragrance, Inc.";
		String version = "Version 4.0";
		try {
			version = "Version "
					+ getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		String htmlContent = "<html><head><style type=\"text/css\">div {text-align: center;color: rgb(32, 32, 32);font-family: Helvetica Neue, Times, serif;font-size: 43px;}h2 {text-align: center;color: rgb(0, 0, 0);font-family: Helvetica Neue, Times, serif;font-size: 28px;}h3 {	padding: 20px 20px 270px 180px;}</style></head><body><p style=\"text-align: center; margin-top: 38px;\"><img src=\"file:///android_asset/logo.png\" width=\"360\" height=\"150\"></p><div>"
				+ version
				+ "</div><div style=\"height: 55%;\"></div><h3></h3><h2>"
				+ copyRight + "</h2></body></html>";
		// webViewCatalog.loadData(htmlContent, "text/html", "UTF-8");

		webViewCatalog.loadDataWithBaseURL("file:///android_asset/",
				htmlContent, "text/html", "utf-8", null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (webViewCatalog.canGoBack() == true) {
					webViewCatalog.goBack();
				} else {
					finish();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * The Class WbViewClient.
	 *//*
	private class WbViewClient extends WebViewClient {

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
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
				loadingLayout.setVisibility(View.GONE);
			webViewCatalog.setVisibility(View.VISIBLE);
			view.setInitialScale((int) (101 * view.getScale()));
		}

	}*/

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
