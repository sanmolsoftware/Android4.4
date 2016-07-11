package com.ulta.core.activity.rewards;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;

public class RewardsWebViewActivity extends UltaBaseActivity {

	private WebView mWebView;
	private FrameLayout mLoadingLayout;
	private int mFromKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_layout);
		mWebView = (WebView) findViewById(R.id.wvLegal);
		mWebView.setVisibility(View.GONE);
		mLoadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
		mLoadingLayout.setVisibility(View.VISIBLE);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setDomStorageEnabled(true);
		
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
	  

		if (null != getIntent().getExtras()) {
			if (0 != getIntent().getExtras().getInt("rewardsFrom")) {
				mFromKey = getIntent().getExtras().getInt("rewardsFrom");
			}
		}

		loadUrlInWebView();

	}

	public void loadUrlInWebView() {

		switch (mFromKey) {

		// web view for Benifits
		case WebserviceConstants.FROM_BENIFITS:
			setTitle("Benefits");
			mWebView.loadUrl(deviceOptimizedBannerName(WebserviceConstants.BENIFITS_URL));

			break;

		// web view for Earnings
		case WebserviceConstants.FROM_EARNINGSANDREDEEMING:
			setTitle("Earning/Redeeming");
			mWebView.loadUrl(deviceOptimizedBannerName(WebserviceConstants.EARNINGS_URL));

			break;

		// web view for Platinum
		case WebserviceConstants.FROM_PLATINUM:
			setTitle("Platinum Program");
			mWebView.loadUrl(deviceOptimizedBannerName(WebserviceConstants.PLATINUMS_URL));

			break;

		default:
			break;
		}
		mLoadingLayout.setVisibility(View.GONE);
		mWebView.setVisibility(View.VISIBLE);
	}
	
	public String deviceOptimizedBannerName(String url)
	{
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append(url);

		int density = getResources().getDisplayMetrics().densityDpi;

		if (density >= 400) {
			// "xxxhdpi";
			urlBuffer.append("@XXXHDPI.png");
			

		} else if (density >= 300 && density < 400) {
			// xxhdpi
			urlBuffer.append("@XXHDPI.png");
			
		} else if (density >= 200 && density < 300) {
			// xhdpi
			
			urlBuffer.append("@XHDPI.png");

		} else if (density >= 150 && density < 200) {
			// hdpi
				
			urlBuffer.append("@HDPI.png");
			

		} else if (density >= 100 && density < 150) {
			// mdpi
			urlBuffer.append("@MDPI.png");

		} else {
			// default hdpi
			urlBuffer.append("@HDPI.png");
		}
		
		return urlBuffer.toString();
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
		if(mWebView.canGoBack())
		{
			mWebView.goBack();
		}
		else {
			finish();
		}
	}

}
