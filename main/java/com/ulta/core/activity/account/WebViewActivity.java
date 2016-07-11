package com.ulta.core.activity.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.CashStarHomeUI;
import com.ulta.core.activity.product.UltaProductDetailsActivity;
import com.ulta.core.activity.product.UltaProductListActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.Set;

public class WebViewActivity extends UltaBaseActivity {

    WebView webView;
    FrameLayout loadingLayout;
    private int mFromKey;
    private String mUrlFromIntent;
    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_layout);
        setActivity(WebViewActivity.this);
        webView = (WebView) findViewById(R.id.wvLegal);
        webView.setVisibility(View.GONE);
        loadingLayout = (FrameLayout) findViewById(R.id.aboutLoadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);

        if (null != getIntent().getExtras()) {
            if (0 != getIntent().getExtras().getInt("navigateToWebView")) {
                mFromKey = getIntent().getExtras().getInt("navigateToWebView");
            }
            if (null != getIntent().getExtras().getString("url")) {
                mUrlFromIntent = getIntent().getExtras().getString("url");
            }
            if (null != getIntent().getExtras().getString("title")) {
                mTitle = getIntent().getExtras().getString("title");
            }
        }
        setTitle(mTitle);

        loadUrlInWebView();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                switch (mFromKey) {
                    case WebserviceConstants.FROM_WEEKLYAd:
                        loadingLayout.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        performWeeklyAddShouldOverrideURlLoading(view, url);
                        return false;
                    case WebserviceConstants.FROM_ULTAMATE_CARD:
                        loadingLayout.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        return false;
                    case WebserviceConstants.FROM_BLACK_FRIDAY:
                        performWeeklyAdShouldOverrideURlLoading(view, url);

                        break;

                    case WebserviceConstants.FROM_GIFT_CARD:
                        performGiftCardShouldOverrideUrlLoading(view, url);
                        break;

                    case WebserviceConstants.FROM_PUSHNOTIFICATION:
                        performWeeklyAdShouldOverrideURlLoading(view, url);
                        break;

                    default:
                        break;
                }

                return true;

            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                loadingLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                switch (mFromKey) {

                    case WebserviceConstants.FROM_GIFT_CARD:
                        performGiftCardOnPageFinished(view, url);
                        break;
                    case WebserviceConstants.FROM_BLACK_FRIDAY:
                        performBlackFridayOnPageFinished(view, url);
                        break;
                    default:
                        break;
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
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

    public void loadUrlInWebView() {

        switch (mFromKey) {

            // web view for weeklyAd
            case WebserviceConstants.FROM_WEEKLYAd:
                webView.loadUrl(WebserviceConstants.URL_FOR_AD);

//                if (null != UltaDataCache.getDataCacheInstance().getWeeklyAdURL() &&
//                        !UltaDataCache.getDataCacheInstance().getWeeklyAdURL().isEmpty()) {
//                    Log.d("ififififi", "ififififif");
//                    webView.loadUrl(UltaDataCache.getDataCacheInstance().getWeeklyAdURL());
//                } else {
//                    Log.d("elselesl", "elselesl");
//                    webView.loadUrl(WebserviceConstants.URL_FOR_AD);
//                }

                break;

            // web view for Black Friday
            case WebserviceConstants.FROM_BLACK_FRIDAY:
                webView.loadUrl(mUrlFromIntent);

                break;

            // web view for Gift Cards
            case WebserviceConstants.FROM_GIFT_CARD:
                trackAppState(WebViewActivity.this, WebserviceConstants.HOME_PAGE_GIFT_CARD);
                webView.loadUrl(mUrlFromIntent);


                break;

            // web view for Push Notifications
            case WebserviceConstants.FROM_PUSHNOTIFICATION:
                webView.loadUrl(mUrlFromIntent);

                break;
            // web view for Ultamate credit card
            case WebserviceConstants.FROM_ULTAMATE_CARD:
                webView.loadUrl(mUrlFromIntent);
                break;

            default:
                break;
        }

    }

    public void performWeeklyAdShouldOverrideURlLoading(WebView view, String url) {
        SEOREWrite(view, url);
        Logger.Log("@@@@@:" + url);
    }

    public void SEOREWrite(WebView view, String URLData) {

        Uri uri = Uri.parse(URLData);
        Set<String> paramNames = uri.getQueryParameterNames();
        //New URL check
        if (paramNames.size() > 0) {
            if (paramNames.contains("n") || paramNames.contains("N")) {
                String nValue = null;
                if (null != uri.getQueryParameter("n")) {
                    nValue = uri.getQueryParameter("n");
                } else if (null != uri.getQueryParameter("N")) {
                    nValue = uri.getQueryParameter("N");
                }
                Intent intentToSearchActivity = new Intent(WebViewActivity.this,
                        UltaProductListActivity.class);
                intentToSearchActivity.putExtra("Nstate", nValue);
                startActivity(intentToSearchActivity);

            } else if ((paramNames.contains("productId") || paramNames.contains("productid")) && paramNames.contains("sku")) {
                String productId = null, skuId = null;
                productId = uri.getQueryParameter("productId");
                skuId = uri.getQueryParameter("sku");
                Intent intentPDP = new Intent(WebViewActivity.this,
                        UltaProductDetailsActivity.class);
                intentPDP.putExtra("id", productId);
                intentPDP.putExtra("skuIdFromWeeklyAd", skuId);
                startActivity(intentPDP);


            } else if (paramNames.contains("productId") || paramNames.contains("productid")) {
                String productId = null, skuId = null;
                productId = uri.getQueryParameter("productId");
                Intent intentPDP = new Intent(WebViewActivity.this,
                        UltaProductDetailsActivity.class);
                intentPDP.putExtra("id", productId);
                intentPDP.putExtra("skuIdFromWeeklyAd", skuId);
                startActivity(intentPDP);

            }
            /**
             * Existing URL check
             */
            else if (URLData.contains("http://www.ulta.com/ulta/browse/specialOfferDetail.jsp?promoId=")) {
                String promoId[] = URLData.split("=");
                Intent intentSpecialOffers = new Intent(WebViewActivity.this,
                        UltaProductListActivity.class);
                intentSpecialOffers.setAction("fromPromotion");
                intentSpecialOffers.putExtra("promotionId", promoId[1]);
                startActivity(intentSpecialOffers);
            } else if (URLData
                    .contains("http://www.ulta.com/ulta/browse/productDetail.jsp?productId=")) {
                String promoId[] = URLData.split("=");
                String productId = null, skuId = null;
                if (promoId[1].contains("&")) {
                    String prodId[] = promoId[1].split("&");
                    productId = prodId[0];
                    skuId = promoId[2];
                } else {
                    productId = promoId[1];
                }

                Intent intentPDP = new Intent(WebViewActivity.this,
                        UltaProductDetailsActivity.class);
                intentPDP.putExtra("id", productId);
                intentPDP.putExtra("skuIdFromWeeklyAd", skuId);
                startActivity(intentPDP);
            } else if (URLData.contains("http://www.ulta.com/ulta/a/")
                    && (URLData.contains("ciSelector=category") || URLData
                    .contains("ciSelector=leaf"))) {
                Intent intentToSearchActivity = new Intent(WebViewActivity.this,
                        UltaProductListActivity.class);
                String split[] = URLData.split("=");
                String catId[] = split[1].split("&");
                intentToSearchActivity.putExtra("search", catId[0]);
                startActivity(intentToSearchActivity);
            } else if (URLData.contains("http://www.ulta.com/ulta/a/")
                    && URLData.contains("ciSelector=searchResults")) {
                // For extracting keyword for the following type of urls
                // http://www.ulta.com/ulta/a/Benefit-Cosmetics/_/N-1z141ad?ciSelector=searchResults
                String extract[] = URLData.split("/a/");
                String searchPart[] = extract[1].split("/");
                Intent intentToSearchActivity = new Intent(WebViewActivity.this,
                        UltaProductListActivity.class);
                String search = searchPart[0];
                String categoryId = searchPart[1];
                if (!search.equals("_")) {
                    Logger.Log("SEARCH WORD" + search);
                    intentToSearchActivity.putExtra("search", search);
                    if (!categoryId.equals("_")) {
                        intentToSearchActivity.putExtra("categoryId", categoryId);
                    }
                    startActivity(intentToSearchActivity);
                } else {
                    // For extracting keyword from this type of urls
                    // http://www.ulta.com/ulta/a/_/Ntt-ultaeyewk50/Nty-1?Ntk=marketingKeyword&Ntx=mode+matchall&ciSelector=searchResults
                    String[] keyWord = categoryId.split("-");
                    if (keyWord.length != 0) {
                        Logger.Log("SEARCH WORD" + keyWord[1]);
                        intentToSearchActivity.putExtra("search", keyWord[1]);
                        startActivity(intentToSearchActivity);
                    }

                }
            } else if (URLData.contains("/giftcard/") || URLData.contains("/giftcards/")) {
                //load http://www.ulta.com/mobile/giftcard_mobile_app.html in webview from appconfig service


                if (WebserviceConstants.isCashStarSDKEnabled) {
                    // App has to leverage cash star sdk to show page
                    Intent giftCardIntent = new Intent(WebViewActivity.this, CashStarHomeUI.class);
                    startActivity(giftCardIntent);
                } else {
                    loadingLayout.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    view.loadUrl(UltaDataCache.getDataCacheInstance().getGiftCardUrl());

                }


            } else {
                loadingLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                view.loadUrl(URLData);
            }

        } else {

            if (URLData.contains("/brand/")) {
                //Call fetchProductsForKeyword() with  extract[1]
                String extract[] = URLData.split("/brand/");
                Intent intentToSearchActivity = new Intent(WebViewActivity.this,
                        UltaProductListActivity.class);
                if (extract.length != 0) {
                    intentToSearchActivity.putExtra("search", extract[1]);
                    startActivity(intentToSearchActivity);
                }

            } else if (URLData.contains("/giftcard/") || URLData.contains("/giftcards/")) {
                //load http://www.ulta.com/mobile/giftcard_mobile_app.html in webview from appconfig service

                if (WebserviceConstants.isCashStarSDKEnabled) {
                    // App has to leverage cash star sdk to show page
                    Intent giftCardIntent = new Intent(WebViewActivity.this, CashStarHomeUI.class);
                    startActivity(giftCardIntent);
                } else {
                    loadingLayout.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    view.loadUrl(UltaDataCache.getDataCacheInstance().getGiftCardUrl());
                }

            } else {
                loadingLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                view.loadUrl(URLData);
            }

        }


    }

    public void performGiftCardOnPageFinished(WebView view, String url) {

        String javascript = "javascript:function hideElement(element, index, array){element.style.display = 'none'; } var array = document.getElementsByClassName('logo');[].forEach.call(array,hideElement)";
        String javascript1 = "javascript:function hideElement(element, index, array){element.style.display = 'none'; } var array = document.getElementsByClassName('footer');[].forEach.call(array,hideElement)";

        view.loadUrl(javascript);
        view.loadUrl(javascript1);

        loadingLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

    }

    public void performBlackFridayOnPageFinished(WebView view, String url) {

        String javascript = "javascript:function hideElement(element, index, array){element.style.display = 'none'; } var array = document.getElementsByClassName('whitebar');[].forEach.call(array,hideElement)";
        String javascript1 = "javascript:function hideElement(element, index, array){element.style.margin= '0';}var array = document.getElementsByClassName('main-cont-wrapper');[].forEach.call(array,hideElement)";
        view.loadUrl(javascript);
        view.loadUrl(javascript1);
        loadingLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

    }

    public boolean performWeeklyAddShouldOverrideURlLoading(WebView view, String url) {

        view.loadUrl(url);


        if (view.getUrl().toLowerCase().equals(WebserviceConstants.GIFT_CLICK_ON_WEB.toLowerCase())) {

            if (WebserviceConstants.isCashStarSDKEnabled) {
                Intent cashStar = new Intent(WebViewActivity.this, CashStarHomeUI.class);
                startActivity(cashStar);
            }


        }

        if (view.getUrl().toLowerCase().contains("productId".toLowerCase())) {

            // Parse URL
            Uri uri = Uri.parse(view.getUrl());

            // Get values from URL parameters
            String productId = uri.getQueryParameter("productId");

            Intent ultaProductDetailsIntent = new Intent(
                    WebViewActivity.this,
                    UltaProductDetailsActivity.class);
            ultaProductDetailsIntent.putExtra("idFromSearch",
                    productId);
            ultaProductDetailsIntent.putExtra("fromSearch",
                    "search");
            startActivity(ultaProductDetailsIntent);

            return false;
        }
        return true;
    }

    public void performGiftCardShouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        view.loadUrl("javascript:document.getElementsByClassName('logo')[0].style.display = 'none';");
        view.loadUrl("javascript:document.getElementsByClassName('footer')[0].style.display = 'none';");
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
