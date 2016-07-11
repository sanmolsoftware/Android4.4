/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.checkout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

/**
 * The Class LegalActivity.
 */
public class PayPalWebviewActivity extends UltaBaseActivity implements OnSessionTimeOut {
    private String myUrl;
    private String tokenId;
    private boolean redirect, loadingFinished;
    private WebView webView;
    private FrameLayout loadingLayout;
    private FrameLayout mTitleBarLayout;

    //private UemAction fetchPayPalDetailsAction;
    @Override
    protected void onResume() {
        super.onResume();
        if (UltaDataCache.getDataCacheInstance().isMovingBackInChekout()) {
            String targetScreen = UltaDataCache.getDataCacheInstance()
                    .getMoveBackTo();
            if (!("paypal".equalsIgnoreCase(targetScreen))) {
                finish();
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivity(PayPalWebviewActivity.this);
        setContentView(R.layout.web_view_layout);
        trackAppState(PayPalWebviewActivity.this, WebserviceConstants.CHECKOUT_PAYPAL);
        webView = (WebView) findViewById(R.id.wvLegal);
        webView.setVisibility(View.GONE);
        setTitle("PayPal Payment");
        disableMenu();
        mTitleBarLayout = (FrameLayout) findViewById(R.id.title_bar_layout);
        mTitleBarLayout.setVisibility(View.GONE);
        loadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);
        if (getIntent().getExtras() != null
                && getIntent().getExtras().getString("token") != null) {
            tokenId = getIntent().getExtras().getString("token");
        }
        // 3.3 Release
        if (tokenId != null) {
            /* myHandler = new Handler(); */
            loadPaypalLoginPage(webView, tokenId);
        } else {
            final String lblPositiveButton = "OK";
            final AlertDialog.Builder errorAlertDialogBuilder = new AlertDialog.Builder(
                    PayPalWebviewActivity.this);
            errorAlertDialogBuilder
                    .setMessage("We're sorry, you can't pay with PayPal right now, please checkout using another payment method");
            errorAlertDialogBuilder.setTitle("Sorry");
            errorAlertDialogBuilder.setCancelable(false);
            errorAlertDialogBuilder.setPositiveButton(lblPositiveButton,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            final AlertDialog errorAlertDialog = errorAlertDialogBuilder
                    .create();
            errorAlertDialog.show();
        }
    }

    @SuppressWarnings("deprecation")
    public void loadPaypalLoginPage(final WebView webView, String token) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		webSettings.setPluginsEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setSupportZoom(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webView.clearView();
		/*
		 * final ProgressDialog progressBar = ProgressDialog.show(
		 * PayPalWebviewActivity.this, "Please Wait", "Loading...");
		 */
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingFinished = false;
                Logger.Log("PayPalURl Loading###" + url);

                if (url.contains("/ulta/paypal/continue")) {
                    view.stopLoading();
                    loadingLayout.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);

                    if (isUltaCustomer(PayPalWebviewActivity.this)
                            || UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
							/*fetchPayPalDetailsAction = UemAction.enterAction(WebserviceConstants.ACTION_PAYPAL_PAYMENT_DETAILS);
							fetchPayPalDetailsAction.reportEvent("fetching Details of PayPal Payment");*/
                        invokePayPalPaymentDetails();
                    } else {
							/*invokePayPalPaymentDetails();*/
                        Logger.Log(">>>>>>>>take user to Sign in page");
                        Intent intentForLogin = new Intent(PayPalWebviewActivity.this,
                                LoginActivity.class);
                        intentForLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentForLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentForLogin.putExtra("origin", "basket");
                        intentForLogin.putExtra("fromCehckout", 2);
                        intentForLogin.putExtra("fromPayPal", true);
                        startActivity(intentForLogin);
                        finish();
                    }

                } else if (url.contains("/ulta/paypal/cancel")) {
                    loadingLayout.setVisibility(View.GONE);
                    final String lblPositiveButton = "OK";
                    final AlertDialog.Builder errorAlertDialogBuilder = new AlertDialog.Builder(
                            PayPalWebviewActivity.this);
                    errorAlertDialogBuilder
                            .setMessage("We're sorry, you can't pay with PayPal right now, please checkout using another payment method");
                    errorAlertDialogBuilder.setTitle("Sorry");
                    errorAlertDialogBuilder.setCancelable(false);
                    errorAlertDialogBuilder.setPositiveButton(
                            lblPositiveButton,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    final AlertDialog errorAlertDialog = errorAlertDialogBuilder
                            .create();
                    errorAlertDialog.show();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    loadingLayout.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                } else {
                    redirect = false;
                }
            }
        });


        SharedPreferences environmentSavedPref = Ulta.ultaInstance
                .getSharedPreferences("userdetails", 0);
        String serverAddress = environmentSavedPref.getString("serverAddress",
                WebserviceConstants.prodServerAddress);
        if (serverAddress
                .equals(WebserviceConstants.prodServerAddress)) {
//			myUrl = WebserviceConstants.EXPRESS_CHECKOUT_PRODUCTION_URL + token;
            myUrl = WebserviceConstants.EXPRESS_CHECKOUT_PRODUCTION_URL + "125ab2f4271c0571482fd829b70213234308af11";

        } else {
            myUrl = WebserviceConstants.EXPRESS_CHECKOUT_SANDBOX_URL + token;
        }
        webView.loadUrl(myUrl);
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
        if (isSuccess) {
            invokePayPalPaymentDetails();
        }
    }
}
