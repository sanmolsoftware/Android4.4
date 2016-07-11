/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.mobile.Analytics;
import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.account.OlapicImageDetailsActivity;
import com.ulta.core.activity.account.OlapicUploadActivity;
import com.ulta.core.bean.favourites.FavoritesComponentBean;
import com.ulta.core.bean.olapic.OlapicInnerEmbeddedBean;
import com.ulta.core.bean.olapic.OlapicProductDetailsBean;
import com.ulta.core.bean.olapic.OlapicProductDetailsDataBean;
import com.ulta.core.bean.olapic.OlapicProductDetailsEmbeddedBean;
import com.ulta.core.bean.olapic.OlapicProductDetailsImagesBean;
import com.ulta.core.bean.olapic.OlapicProductDetailsMediaBean;
import com.ulta.core.bean.olapic.OlapicUploadMediaBean;
import com.ulta.core.bean.olapic.OlapicUploadMediaDataBean;
import com.ulta.core.bean.olapic.OlapicUploadSuccessBean;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.BrandDetailsBean;
import com.ulta.core.bean.product.CartBean;
import com.ulta.core.bean.product.CategoryBean;
import com.ulta.core.bean.product.CommerceItemBean;
import com.ulta.core.bean.product.ComponentBean;
import com.ulta.core.bean.product.ProductBean;
import com.ulta.core.bean.product.ProductFeatureBean;
import com.ulta.core.bean.product.ProductHeaderBean;
import com.ulta.core.bean.product.ProductReviewBean;
import com.ulta.core.bean.product.ProductSkuBean;
import com.ulta.core.bean.product.ProductSkuGWPBean;
import com.ulta.core.bean.product.ProductsInListBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.ImageDownloader;
import com.ulta.core.util.ImageDownloader.ImageDownloadListener;
import com.ulta.core.util.OmnitureTracking;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.TitleBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.modifyImageResolution;

/**
 * The Class UltaProductDetailsActivity.
 */
@SuppressWarnings({"serial", "deprecation"})
public class UltaProductDetailsActivity extends UltaBaseActivity implements
        android.view.View.OnClickListener, ImageDownloadListener, Serializable {

    /**
     * The updated height of the zoomed image.
     */
    private static final int HEIGHT_IMAGE = 1024;

    public static final String EXTRA_IMAGE = "UltaproductDetails:image";
    /**
     * The updated widht of the zoomed image.
     */
    private static final int WIDHT_IMAGE = 1024;
    private static final int REQ_CODE_LOGIN = 0;
    private static final int REQ_CODE_UPLOAD = 111;
    private static final int EGC_REQUEST = 8;

    private String skuId;
    private String skuIdFromSearch;
    private String from;

    private LinearLayout llBadgeImages;
    /**
     * The select an option layout.
     */
    private LinearLayout selectAnOptionLayout;

    /**
     * The hs similar products.
     */
    private LinearLayout hsSimilarProducts;

    /**
     * The btn add to basket.
     */
    private Button btnAddToBasket;

    /** Button added for 3.5 enhancements RTI feature */
    // private Button btnFindInStore;

    /**
     * The btn add to beauty list.
     */
    // private Button btnAddToBeautyList;
    private ImageView btnAddToBeautyList;

    /** The ll inflate user reviews. */
    // private LinearLayout llInflateUserReviews;

    /**
     * Linear layout for questions n Answers
     */
    private LinearLayout llQuestionsAndAnswers;

    /**
     * The ll show description.
     */
    private LinearLayout llShowDescription;
    private LinearLayout llClickToViewDesc;

    private LinearLayout llShowFullDescription;

    private LinearLayout llHideFullDescription;

    /** The ll stock check. */
    // private LinearLayout llStockCheck;

    /**
     * The ll view product options.
     */
    private HorizontalScrollView llViewProductOptions;

    /**
     * The img product image.
     */
    private ImageView imgProductImage;

    // private ImageView imgViewRating1;
    // private ImageView imgViewRating2;
    // private ImageView imgViewRating3;
    // private ImageView imgViewRating4;
    // private ImageView imgViewRating5;
    // private TextView tvViewReviewNumber;
    private LinearLayout mReviewAndratingsTextView;
    private LinearLayout mRatingsLayout;

    /**
     * The img rating1.
     */
    private ImageView imgRating1;

    /**
     * The img rating2.
     */
    private ImageView imgRating2;

    /**
     * The img rating3.
     */
    private ImageView imgRating3;

    /**
     * The img rating4.
     */
    private ImageView imgRating4;

    /**
     * The img rating5.
     */
    private ImageView imgRating5;

    /**
     * The img sale flag.
     */
    private ImageView imgSaleFlag;

    /** The img small product image. */
    // private ImageView imgSmallProductImage;

    /**
     * The tv detailed description.
     */
    // private TextView tvDetailedDescription;

    private TextView mHazmatTextView;

    /** The tv brand name. */
    // private TextView tvBrandName;

    /**
     * The tv mini description.
     */
    private TextView tvMiniDescription;

    private TextView tvRating;

    /** The tv review numbers. */
    // private TextView tvReviewNumbers;

    /** The tv display name. */
    // private TextView tvDisplayName;

    /**
     * The text view for GC/EGC message.
     */
    private TextView tvMsgForGCandEGC;

    /**
     * The tv item id.
     */
    private TextView tvItemId;

    /**
     * The tv list price.
     */
    private TextView tvListPrice;

    /**
     * The tv sale price.
     */
    private TextView tvSalePrice;

    /**
     * The product bean.
     */
    private ProductBean productBean;

    /**
     * The product header bean.
     */
    private ProductHeaderBean productHeaderBean;

    /**
     * The product review bean.
     */
    private ProductReviewBean productReviewBean;

    /**
     * The list of product sku beans.
     */
    private List<ProductSkuBean> listOfProductSkuBeans;

    /**
     * The list of product feature bean.
     */
    @SuppressWarnings("unused")
    private List<ProductFeatureBean> listOfProductFeatureBean;

    /**
     * The category bean.
     */
    @SuppressWarnings("unused")
    private CategoryBean categoryBean;

    /**
     * The brand details bean.
     */
    private BrandDetailsBean brandDetailsBean;

    /**
     * The inflater.
     */
    private LayoutInflater inflater;

    /**
     * The id.
     */
    private String id;

    /**
     * The add to cart bean.
     */
    private AddToCartBean addToCartBean;

    /**
     * The product sku bean.
     */
    private ProductSkuBean productSkuBean;
    @SuppressWarnings("unused")
    private ProductsInListBean productInListBean;
    private TextView prodBrandName;
    private TextView tvFeature;
    private TextView tvPromotion;
    private TextView prodDisplayName;
    // private LinearLayout llReview;

    FrameLayout loadingDialog;
    private ProgressDialog pd;
    private ScrollView svMain;

    // private ImageView iconShare;
    private ImageView iconPdpShare;
    // private ImageView fbPdpImageView;
    private TextView pdpItemNumber;
    private TextView mRecentlyViewedHeaderTextView;
    private TextView mOlapicHeaderTextView;

    private TextView similarText;
    private LinearLayout similarLayout;
    // private int productAddedtoFavCheck=0;

    String productToShareImgURL;
    String productToShareLink;
    String productToShareText;
    String productToShareID;

    String productDescription;
    String productWarning;
    String productIngredients;
    String productDirections;
    private String productSpecification;
    String BadgeIdName = "";
    private boolean isRTIcheckAvailable;

    private String skuIdFromWeeklyAd;

    private OlapicProductDetailsBean olapicProductDetailsBean;

    private OlapicProductDetailsDataBean olapicProductDetailsDataBean;

    private OlapicProductDetailsEmbeddedBean olapicProductDetailsEmbeddedBean;

    private List<OlapicProductDetailsMediaBean> olapicProductDetailsMediaBean;

    private OlapicProductDetailsImagesBean olapicProductDetailsImagesBean;

    private OlapicInnerEmbeddedBean olapicInnerEmbeddedBean;

    private String olapicMobileImage;

	/* private String href; */

    private String status;

    private String urlForPinchZoomImage;

    private ImageView olapicImageView;

    private LinearLayout olapicImagesLayout;

    private View olapicView;

    private LinearLayout olapicImageLayout2;

    private boolean isComingSoon = false;

    private ImageView uploadBtn;

    private String mediaId;

    private String mMiniDescription;

    private String streamsId;

    private String filePath;

    private boolean enableBtns = false;

    private final int ACTIVITY_SELECT_IMAGE = 1234;

    private OlapicUploadMediaBean olapicUploadMediaBean;

    private OlapicUploadMediaDataBean olapicUploadMediaDataBean;

    private Dialog uploadProgressDialog;

    private String fromProdList = "";

    // private UemAction productDetailsAction;

    private boolean isColorvarianceClicked = false;

    private boolean isRecentlyViewedClicked = false;

    private boolean isGuestAlsoBoughtClicked = false;

    private ArrayList<String> mAvatarUrl = new ArrayList<String>();
    private ArrayList<String> mUserNameWhoUploaded = new ArrayList<String>();
    private ArrayList<String> mCaption = new ArrayList<String>();
    private ArrayList<String> mImageLink = new ArrayList<String>();

    private LinearLayout recentlyViewedImagesLayout;
    private LinearLayout recentlyViewedHeaderLayout;
    // private LinearLayout socialSharingLayout;
    private ImageView rtiIconImageView;
    // private TextView shareCancelLayout;

    private int OLAPIC_IMAGE_COUNT = -1;
    private SharedPreferences refreshTimeOutSharedPreferences;
    private String mSkuIdFromBasket, mSkuIdFromFavorites;
    WebView webView;
    private int imagePosition;

    private HorizontalScrollView mGuestAlsoBoughtHorizontalScrollView;
    private HorizontalScrollView mRecentlyViewedHorizontalScrollView;

    private LinearLayout gwpDetailsViewClick, llShowFullGWPLayout, llHideFullGWPLayout, gwpLayoutItems;
    int cellHeight = 0;
    ImageView trasnparentView;
    ViewGroup.LayoutParams params;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.Log(">>>>>>>>>>>>Entered Activity Result<<<<<<<<<<<<<<<<");
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_LOGIN) {
            if (isUltaCustomer(UltaProductDetailsActivity.this)) {
                fnInvokeFavorites();
            }

        } else if (resultCode == RESULT_OK && requestCode == EGC_REQUEST) {
            showAddToCartSuccess();
        } else if (resultCode == RESULT_OK && requestCode == REQ_CODE_UPLOAD) {
            Intent intent = new Intent(UltaProductDetailsActivity.this,
                    OlapicUploadActivity.class);
            startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

        }

        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    uploadProgressDialog = new Dialog(
                            UltaProductDetailsActivity.this,
                            R.style.ThemeDialogCustom);
                    uploadProgressDialog
                            .requestWindowFeature(Window.FEATURE_NO_TITLE);
                    uploadProgressDialog
                            .setContentView(R.layout.upload_progress_dialog);
                    uploadProgressDialog.setCancelable(false);
                    uploadProgressDialog.show();
                    UltaDataCache.getDataCacheInstance().setOlapic(true);
                    String filePath = data.getStringExtra("imagePath");
                    this.filePath = filePath;
                    invokeMediaIdService();

                } else if (UltaDataCache.getDataCacheInstance().isFromCamera()) {
                    uploadProgressDialog = new Dialog(
                            UltaProductDetailsActivity.this,
                            R.style.ThemeDialogCustom);
                    uploadProgressDialog
                            .requestWindowFeature(Window.FEATURE_NO_TITLE);
                    uploadProgressDialog
                            .setContentView(R.layout.upload_progress_dialog);
                    // uploadProgressDialog.setMessage("Uploading..");
                    uploadProgressDialog.setCancelable(false);
                    uploadProgressDialog.show();
                    UltaDataCache.getDataCacheInstance().setFromCamera(false);
                    UltaDataCache.getDataCacheInstance().setOlapic(true);
                    this.filePath = UltaDataCache.getDataCacheInstance()
                            .getCameraCapturedPath();
                    invokeMediaIdService();
                }
            break;
        }
        // Reported error on playstore : Fixed
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState the saved instance state
     */
    @SuppressWarnings("unused")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setActivity(UltaProductDetailsActivity.this);
        loadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
        pd = new ProgressDialog(UltaProductDetailsActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);
        setTitle("");
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        uploadBtn = (ImageView) findViewById(R.id.olapicUploadBtn);
        webView = (WebView) findViewById(R.id.prodWebview);
        UltaDataCache.getDataCacheInstance().setCalledOnlyOnce(false);
        fnSetViews();
        fnSetClickListeners();
        svMain = (ScrollView) findViewById(R.id.svMain);
        svMain.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.hsSimilarProducts).getParent()
                        .requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Bundle bundleFromUltaProductListActivity = getIntent().getExtras();
        if (null != getIntent().getAction()) {
            if (getIntent().getAction().equalsIgnoreCase("fromHome")) {
                if (null != getIntent().getExtras().getString("altText")) {
                    setTitle(getIntent().getExtras().getString("altText"));
                }
            }
            fromProdList = getIntent().getAction();
        }
        String url = "";
        if (null != bundleFromUltaProductListActivity) {
            id = bundleFromUltaProductListActivity.getString("id");
            skuIdFromWeeklyAd = bundleFromUltaProductListActivity
                    .getString("skuIdFromWeeklyAd");
            imagePosition = bundleFromUltaProductListActivity
                    .getInt("clickedPosition");
            // btnAddToBasket.setVisibility(View.VISIBLE);
            // url =
            // bundleFromUltaProductListActivity.getString("urlForTransition");
        }

        Bitmap bitmap = UltaDataCache.getDataCacheInstance()
                .getPlpHashMapOfImages().get(imagePosition);

        if (null != bitmap && fromProdList.equalsIgnoreCase("fromProductList")) {
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            if (null != drawable) {
                imgProductImage.setBackgroundDrawable(drawable);
            } else {
                imgProductImage.setImageBitmap(bitmap);
            }

        } else {
            if (!fromProdList.equalsIgnoreCase("fromProductList")) {
                imgProductImage.setBackgroundDrawable(null);
            } else {
                imgProductImage.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.dummy_product));
            }
        }

        ViewCompat.setTransitionName(imgProductImage, EXTRA_IMAGE);
        if (null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().getString("idFromBasket")) {
                id = getIntent().getExtras().getString("idFromBasket");
                if (null != getIntent().getExtras().getString("skuid")) {
                    mSkuIdFromBasket = getIntent().getExtras().getString(
                            "skuid");
                }
                // btnAddToBasket.setVisibility(View.GONE);
                btnAddToBasket.setEnabled(true);
            }
            if (null != getIntent().getExtras().getString("idfromFavorites")) {
                id = getIntent().getExtras().getString("idfromFavorites");
                // btnAddToBeautyList.setVisibility(View.GONE);
                btnAddToBeautyList.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.add_fav_active));
            }
            if (null != getIntent().getExtras().getString("skuidfromFavorites")) {
                mSkuIdFromFavorites = getIntent().getExtras().getString(
                        "skuidfromFavorites");
            }
        }

        if (null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().getString(
                    "idFromOlapicImageDetails")) {
                id = getIntent().getExtras().getString(
                        "idFromOlapicImageDetails");
                // btnAddToBasket.setVisibility(View.VISIBLE);
            }

        }

        if (null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().getString("idFromSearch")) {
                id = getIntent().getExtras().getString("idFromSearch");
            }
            if (null != getIntent().getExtras().getString("skuId")) {
                skuIdFromSearch = getIntent().getExtras().getString("skuId");
            }
            if (null != getIntent().getExtras().getString("fromSearch")) {
                from = getIntent().getExtras().getString("fromSearch");
            }

            if (null != getIntent().getExtras().getString("id")) {
                id = getIntent().getExtras().getString("id");
            }
            if (null != getIntent().getExtras()
                    .getString("searchScannedResult")) {
                id = getIntent().getExtras().getString("searchScannedResult");
            }

        }

        // trackEvarsUsingActionName(UltaProductDetailsActivity.this,
        // WebserviceConstants.PRODUCT_VIEW+","+WebserviceConstants.REAL_PRODUCT_VIEWS,
        // WebserviceConstants.PRODUCTS_KEY, ";"+id);

		/*
         * trackEvarsUsingActionName(UltaProductDetailsActivity.this,
		 * WebserviceConstants.REAL_PRODUCT_VIEWS,
		 * WebserviceConstants.PRODUCTS_KEY, ";"+id);
		 */

		/*
         * trackEvarsUsingActionName(UltaProductDetailsActivity.this,
		 * WebserviceConstants.REAL_PRODUCT_VIEWS,
		 * WebserviceConstants.PRODUCTS_KEY, ";"+id);
		 */

        // productDetailsAction = UemAction.enterAction("Product Details");
        //
        // productDetailsAction.reportEvent("ProductDetails Started");

        loadingDialog.setVisibility(View.VISIBLE);
        svMain.setVisibility(View.VISIBLE);
        fnInvokeRetrieveProductDetails(id);

        uploadBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                    Intent intent = new Intent(UltaProductDetailsActivity.this,
                            OlapicUploadActivity.class);
                    startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

                } else {
                    Intent intentForLogin = new Intent(
                            UltaProductDetailsActivity.this,
                            LoginActivity.class);
                    intentForLogin.putExtra("origin", "pdpOlapicUpload");
                    startActivityForResult(intentForLogin, REQ_CODE_UPLOAD);
                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null)
            webView.onPause();
        UltaDataCache.getDataCacheInstance().setOlapicProdDetails(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    /**
     * Method to set the OnClickListeners.
     */
    private void fnSetClickListeners() {
        imgProductImage.setOnClickListener(UltaProductDetailsActivity.this);
        btnAddToBasket.setOnClickListener(UltaProductDetailsActivity.this);
        btnAddToBeautyList.setOnClickListener(UltaProductDetailsActivity.this);
        // llStockCheck.setOnClickListener(UltaProductDetailsActivity.this);
        // llInflateUserReviews
        // .setOnClickListener(UltaProductDetailsActivity.this);
        llQuestionsAndAnswers
                .setOnClickListener(UltaProductDetailsActivity.this);
        llClickToViewDesc.setOnClickListener(UltaProductDetailsActivity.this);
        // btnSeeAllReviews.setOnClickListener(UltaProductDetailsActivity.this);

        hsSimilarProducts.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        iconPdpShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ShortenURLTask shortenUrlTask = new ShortenURLTask();
                shortenUrlTask.execute();

            }
        });

        // shareCancelLayout.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // //socialSharingLayout.setVisibility(View.GONE);
        //
        // }
        // });

        rtiIconImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent storeRTIIntent = new Intent(
                        UltaProductDetailsActivity.this, StoreRTIActivity.class);
                skuId = productSkuBean.getId();
                storeRTIIntent.putExtra("skuId", skuId);
                startActivity(storeRTIIntent);
            }
        });

    }

    private class ShortenURLTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder url = new StringBuilder();
            url.append(WebserviceConstants.SHORTENURL_LINK);
            url.append("&longUrl=");
            url.append(productToShareImgURL);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(new HttpGet(url.toString()));

                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String response = convertStreamToString(instream);
                    instream.close();
                    return response;
                }

            } catch (ClientProtocolException e) {
                httpClient.getConnectionManager().shutdown();
                e.printStackTrace();
            } catch (IOException e) {
                httpClient.getConnectionManager().shutdown();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (null != result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String shortUrl = jsonObject.getString("short_url");

                    if (null != shortUrl) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                getResources().getString(R.string.sharing_text,
                                        brandDetailsBean.getBrandName(),
                                        productSkuBean.getDisplayName(),
                                        shortUrl));
                        sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                productToShareText);
                        startActivity(Intent.createChooser(sendIntent,
                                "www.ulta.com"));
                    } else {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                getResources().getString(R.string.sharing_text,
                                        brandDetailsBean.getBrandName(),
                                        productSkuBean.getDisplayName(),
                                        productToShareImgURL));
                        sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                productToShareText);
                        startActivity(Intent.createChooser(sendIntent,
                                "www.ulta.com"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // socialSharingLayout.setVisibility(View.VISIBLE);
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        getResources().getString(R.string.sharing_text,
                                brandDetailsBean.getBrandName(),
                                productSkuBean.getDisplayName(),
                                productToShareImgURL));
                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        productToShareText);
                startActivity(Intent.createChooser(sendIntent, "www.ulta.com"));
            }

            super.onPostExecute(result);
        }

    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        is.close();

        return sb.toString();
    }

    /**
     * Method for setting up the view elements.
     */
    @SuppressWarnings("deprecation")
    private void fnSetViews() {
        refreshTimeOutSharedPreferences = getSharedPreferences(
                WebserviceConstants.HOME_BANNER_REFRESH_SHAREDPREF,
                MODE_PRIVATE);
        gwpLayoutItems = (LinearLayout) findViewById(R.id.gwpLayoutItems);
        gwpDetailsViewClick = (LinearLayout) findViewById(R.id.gwpDetailsViewClick);
        llShowFullGWPLayout = (LinearLayout) findViewById(R.id.llShowFullGWPLayout);
        llHideFullGWPLayout = (LinearLayout) findViewById(R.id.llHideFullGWPLayout);
        trasnparentView = (ImageView) findViewById(R.id.trasnparentView);
        selectAnOptionLayout = (LinearLayout) findViewById(R.id.selectAnOptionLayout);
        llShowDescription = (LinearLayout) findViewById(R.id.llShowDescription);
        llClickToViewDesc = (LinearLayout) findViewById(R.id.detailsViewClick);
        // llInflateUserReviews = (LinearLayout)
        // findViewById(R.id.llInflateUserReviews);
        mReviewAndratingsTextView = (LinearLayout) findViewById(R.id.reviewTextView);
        mReviewAndratingsTextView.setOnClickListener(this);
        llQuestionsAndAnswers = (LinearLayout) findViewById(R.id.llQuestionAndAnswers);
        hsSimilarProducts = (LinearLayout) findViewById(R.id.hsSimilarProducts);
        btnAddToBasket = (Button) findViewById(R.id.btnAddToBasket);
        btnAddToBeautyList = (ImageView) findViewById(R.id.btnAddToBeautyList);
        btnAddToBeautyList.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.add_fav_inactive));
        llShowFullDescription = (LinearLayout) findViewById(R.id.llShowFullDescriptionLayout);
        llHideFullDescription = (LinearLayout) findViewById(R.id.llHideFullDescriptionLayout);
        llViewProductOptions = (HorizontalScrollView) findViewById(R.id.llViewProductOptions);
        // llStockCheck = (LinearLayout) findViewById(R.id.llStockCheck);
        llBadgeImages = (LinearLayout) findViewById(R.id.llbadge_images);
        olapicImagesLayout = (LinearLayout) findViewById(R.id.olapic_images_layout);
        olapicView = (View) findViewById(R.id.olapic_above_header_view);
        olapicImageLayout2 = (LinearLayout) findViewById(R.id.olapicImageLayout2);

        // pdpShareHorizontalScrollView = (HorizontalScrollView)
        // findViewById(R.id.pdpShare_horizontalScrollview);
        iconPdpShare = (ImageView) findViewById(R.id.icon_pdp_share);
        // socialSharingLayout = (LinearLayout)
        // findViewById(R.id.socialSharingLayout);
        rtiIconImageView = (ImageView) findViewById(R.id.rti_imageview);
        // shareCancelLayout = (TextView) findViewById(R.id.shareLayoutCancel);

        // tvBrandName = (TextView) findViewById(R.id.tvBrandName);
        // tvDisplayName = (TextView) findViewById(R.id.tvDisplayName);
        tvItemId = (TextView) findViewById(R.id.tvItemId);
        tvListPrice = (TextView) findViewById(R.id.tvListPrice);
        tvSalePrice = (TextView) findViewById(R.id.tvSalePrice);
        tvMiniDescription = (TextView) findViewById(R.id.tvMiniDescription);
        // tvReviewNumbers = (TextView) findViewById(R.id.tvReviewNumbers);
        tvRating = (TextView) findViewById(R.id.tvRating);
        // tvViewReviewNumber = (TextView)
        // findViewById(R.id.tvViewReviewNumber);
        // tvDetailedDescription = (TextView)
        // findViewById(R.id.tvDetailedDescription);
        tvFeature = (TextView) findViewById(R.id.tvFeature);
        tvPromotion = (TextView) findViewById(R.id.tvPromotion);
        tvMsgForGCandEGC = (TextView) findViewById(R.id.tvMsgForGCandEGC);
        mHazmatTextView = (TextView) findViewById(R.id.pdpHazmatTextView);

        prodBrandName = (TextView) findViewById(R.id.prodBrandName);
        prodDisplayName = (TextView) findViewById(R.id.prodDisplayName);
        pdpItemNumber = (TextView) findViewById(R.id.pdpItemNumber);
        mRecentlyViewedHeaderTextView = (TextView) findViewById(R.id.recentlyViewedText);
        mOlapicHeaderTextView = (TextView) findViewById(R.id.olapicText);

        imgRating1 = (ImageView) findViewById(R.id.imgRating1);
        imgRating2 = (ImageView) findViewById(R.id.imgRating2);
        imgRating3 = (ImageView) findViewById(R.id.imgRating3);
        imgRating4 = (ImageView) findViewById(R.id.imgRating4);
        imgRating5 = (ImageView) findViewById(R.id.imgRating5);

        // imgViewRating1 = (ImageView) findViewById(R.id.imgViewRating1);
        // imgViewRating2 = (ImageView) findViewById(R.id.imgViewRating2);
        // imgViewRating3 = (ImageView) findViewById(R.id.imgViewRating3);
        // imgViewRating4 = (ImageView) findViewById(R.id.imgViewRating4);
        // imgViewRating5 = (ImageView) findViewById(R.id.imgViewRating5);

        imgProductImage = (ImageView) findViewById(R.id.imgItemImage);
        imgProductImage.setClickable(false);
        imgSaleFlag = (ImageView) findViewById(R.id.img_product_details_sale);
        // imgSmallProductImage = (ImageView)
        // findViewById(R.id.imgSmallProductImage);
        similarLayout = (LinearLayout) findViewById(R.id.SimilarlinearLayout13);
        similarText = (TextView) findViewById(R.id.SimilarText);
        btnAddToBasket.setClickable(false);

        recentlyViewedImagesLayout = (LinearLayout) findViewById(R.id.recently_viewed_images_layout);
        recentlyViewedHeaderLayout = (LinearLayout) findViewById(R.id.recentlyViewed_header);

        prodBrandName.setTypeface(setHelveticaRegulartTypeFace());
        prodDisplayName.setTypeface(setHelveticaRegulartTypeFace());
        tvListPrice.setTypeface(setHelveticaRegulartTypeFace());
        tvSalePrice.setTypeface(setHelveticaRegulartTypeFace());
        tvPromotion.setTypeface(setHelveticaRegulartTypeFace());

        tvMiniDescription.setTypeface(setHelveticaRegulartTypeFace());
        // tvDetailedDescription.setTypeface(setHelveticaRegulartTypeFace());
        tvItemId.setTypeface(setHelveticaRegulartTypeFace());
        pdpItemNumber.setTypeface(setHelveticaRegulartTypeFace());
        similarText.setTypeface(setHelveticaRegulartTypeFace());
        mRecentlyViewedHeaderTextView
                .setTypeface(setHelveticaRegulartTypeFace());
        mOlapicHeaderTextView.setTypeface(setHelveticaRegulartTypeFace());

        mOlapicHeaderTextView.setText(WebserviceConstants.OLAPIC_PDP_HEADING);
        mGuestAlsoBoughtHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.similarProductHorizontalScrollView);
        mRecentlyViewedHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.recentlyViewedScrollView);

		/*
         * if (null != UltaDataCache.getDataCacheInstance()
		 * .getOlpaicHomeGalleryHeadingText()) {
		 * mOlapicHeaderTextView.setText(UltaDataCache.getDataCacheInstance()
		 * .getOlpaicHomeGalleryHeadingText()); }
		 */

        // btnFindInStore = (Button) findViewById(R.id.btnFindInStore);
        // btnFindInStore.setOnClickListener(this);

        llShowFullDescription.setOnClickListener(this);
        llHideFullDescription.setOnClickListener(this);

        gwpDetailsViewClick.setOnClickListener(this);
        llShowFullGWPLayout.setOnClickListener(this);
        llHideFullGWPLayout.setOnClickListener(this);
    }

    /**
     * Method to inflate option elements.
     */
    private void fnSelectAnOptionInflator() {

        int counter = 0;
        // ProductSkuBean productSkuBean = null;
        selectAnOptionLayout.removeAllViews();
        for (int outerLoop = 0; outerLoop < listOfProductSkuBeans.size(); outerLoop++) {
            View view = getLayoutInflater().inflate(
                    R.layout.home_additional_content, null);
            productSkuBean = listOfProductSkuBeans.get(outerLoop);
            view.setId(outerLoop);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int selectedSKUid = v.getId();
                    /* rtiIconImageView.setVisibility(View.GONE); */
                    btnAddToBasket.setVisibility(View.VISIBLE);
                    productSkuBean.setDefaultSku(false);
                    productSkuBean = listOfProductSkuBeans.get(selectedSKUid);
                    productSkuBean.setDefaultSku(true);
                    selectAnOptionLayout.removeAllViews();
                    isColorvarianceClicked = true;
                    fnSetValues();
                    // fnSelectAnOptionInflator();
                }
            });
            ImageView imgProductImage = (ImageView) view
                    .findViewById(R.id.selectAnOptionImage);
            TextView tvProductDisplayName = (TextView) view
                    .findViewById(R.id.imageText);
            tvProductDisplayName.setTextColor(getResources().getColor(
                    R.color.blackgrey));
            // if(!productSkuBean.isDefaultSku())
            // {
            counter++;
            if (null != productSkuBean.getSmallImageUrl()) {
                // new AQuery(imgProductImage).image(
                // productSkuBean.getSmallImageUrl(), true, false, 200,
                // R.drawable.dummy_product, null, AQuery.FADE_IN);

                if (!refreshTimeOutSharedPreferences.getBoolean(
                        WebserviceConstants.PROD_LARGE_IMAGE_URL, false)) {
                    new AQuery(imgProductImage)
                            .image(productSkuBean.getSmallImageUrl(), true,
                                    false, 200, R.drawable.dummy_product, null,
                                    AQuery.FADE_IN);

                } else {
                    String largeImageURL = productSkuBean.getSmallImageUrl()
                            .replace("$sm$", "$lg$");
                    new AQuery(imgProductImage).image(largeImageURL, true,
                            false, 200, R.drawable.dummy_product, null,
                            AQuery.FADE_IN);
                }
            }
            // Logger.Log(productSkuBean.getFeaturetype());
            if (null != productSkuBean.getFeaturetype()) {
                tvProductDisplayName.setText(productSkuBean.getFeaturetype());
            }
            selectAnOptionLayout.addView(view);
            // }
        }
        if (counter == 1) {
            llViewProductOptions.setVisibility(LinearLayout.GONE);
        }

    }

    /**
     * Method to inflate similar products.
     */
    private void fnSimilarProductsInflator() {
        // Make the changes later when similar products are provided as part of
        // the response
        if (null != productBean.getRecommendedProducts()
                && !productBean.getRecommendedProducts().isEmpty()) {
            hsSimilarProducts.removeAllViews();
            similarLayout.setVisibility(View.VISIBLE);
            for (int outerLoop = 0; outerLoop < productBean
                    .getRecommendedProducts().size(); outerLoop++) {
                // for(int
                // outerLoop=0;outerLoop<listOfProductSkuBeans.size();outerLoop++)
                // {
                if (enableBtns == true) {
                    btnAddToBasket.setEnabled(true);
                    btnAddToBeautyList.setVisibility(View.VISIBLE);
                }
                View view = getLayoutInflater().inflate(
                        R.layout.inflate_product_options, null);
                ImageView selectAnOptionImage = (ImageView) view
                        .findViewById(R.id.selectAnOptionImage);
                /*
                 * ImageView imgSimilarProductRating = (ImageView) view
				 * .findViewById(R.id.imgSimilarProductRating);
				 */
                TextView tvSimilarProductName = (TextView) view
                        .findViewById(R.id.tvSimilarProductName);
                TextView tvSimilarProductBrandName = (TextView) view
                        .findViewById(R.id.tvSimilarProductBrandName);
                TextView tvSimilarProductPrice = (TextView) view
                        .findViewById(R.id.similarProductPrice);
                ImageView selectAnOptionbadgeImage = (ImageView) view
                        .findViewById(R.id.selectAnOptionbadgeImage);
                mRatingsLayout = (LinearLayout) view
                        .findViewById(R.id.imageRatingsLayout);

                fetchPowerReviewRatings(outerLoop, view);
                if (null != productBean.getRecommendedProducts().get(outerLoop)) {
                    tvSimilarProductPrice.setText("Price :$"
                            + String.format("%.2f", productBean
                            .getRecommendedProducts().get(outerLoop)
                            .getSalePrice()));
                }
                if (null != productBean.getRecommendedProducts().get(outerLoop)
                        .getBrandName()) {
                    tvSimilarProductBrandName.setText(productBean
                            .getRecommendedProducts().get(outerLoop)
                            .getBrandName());
                }
                if (null != productBean.getRecommendedProducts().get(outerLoop)
                        .getProductDisplayName()) {
                    tvSimilarProductName.setText(productBean
                            .getRecommendedProducts().get(outerLoop)
                            .getProductDisplayName());
                }

                tvSimilarProductName
                        .setTypeface(setHelveticaRegulartTypeFace());
                tvSimilarProductBrandName.setTypeface(
                        setHelveticaRegulartTypeFace(), Typeface.BOLD);
                tvSimilarProductPrice
                        .setTypeface(setHelveticaRegulartTypeFace());
                new AQuery(selectAnOptionImage).image(productBean
                                .getRecommendedProducts().get(outerLoop)
                                .getProductImageUrl(), true, false, 200,
                        R.drawable.dummy_product, null, AQuery.FADE_IN);
                if (null != productBean.getRecommendedProducts().get(outerLoop)
                        .getProductImageUrl()) {
                    if (!refreshTimeOutSharedPreferences.getBoolean(
                            WebserviceConstants.PROD_LARGE_IMAGE_URL, false)) {
                        new AQuery(selectAnOptionImage).image(productBean
                                        .getRecommendedProducts().get(outerLoop)
                                        .getProductImageUrl(), true, false, 200,
                                R.drawable.dummy_product, null, AQuery.FADE_IN);
                    } else {
                        String largeImageURL = productBean
                                .getRecommendedProducts().get(outerLoop)
                                .getProductImageUrl().replace("$sm$", "$lg$");
                        new AQuery(selectAnOptionImage).image(largeImageURL,
                                true, false, 200, R.drawable.dummy_product,
                                null, AQuery.FADE_IN);
                    }
                }
                if (null != productBean.getRecommendedProducts().get(outerLoop)
                        .getBadgeName()) {
                    setBadge(selectAnOptionbadgeImage, productBean
                            .getRecommendedProducts().get(outerLoop)
                            .getBadgeName());
                }
                /*
                 * if (null !=
				 * productBean.getRecommendedProducts().get(outerLoop)
				 * .getBadgeImgURL()) { new
				 * AQuery(selectAnOptionbadgeImage).image(productBean
				 * .getRecommendedProducts().get(outerLoop) .getBadgeImgURL(),
				 * true, false, 30, 0, null, AQuery.FADE_IN); }
				 */
                if (isGuestAlsoBoughtClicked || isRecentlyViewedClicked) {
                    svMain.scrollTo(0, titleBar.getBottom());
                    mGuestAlsoBoughtHorizontalScrollView.scrollTo(0,
                            (hsSimilarProducts.getTop() + hsSimilarProducts
                                    .getBottom()) / 2);
                }

                final int temp = outerLoop;
                selectAnOptionImage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pd.setMessage(LOADING_PROGRESS_TEXT);
                        pd.setCancelable(false);

                        if (null != productBean)
                            id = productBean.getRecommendedProducts().get(temp)
                                    .getProductId();
                        enableBtns = true;
                        loadingDialog.setVisibility(View.VISIBLE);
                        // btnFindInStore.setVisibility(View.GONE);
                        rtiIconImageView.setVisibility(View.VISIBLE);
                        llBadgeImages.removeAllViews();
                        BadgeIdName = "";
                        isGuestAlsoBoughtClicked = true;
                        isComingSoon = false;
                        imgProductImage.setBackgroundDrawable(null);
                        fnInvokeRetrieveProductDetails(id);
                    }
                });
                hsSimilarProducts.addView(view);
            }

        }
    }

    /**
     * Method to populate people also bought and recently viewed ratings
     */

    public void populateRatings(View view, boolean isHalfRating) {

        mRatingsLayout.setVisibility(View.VISIBLE);
        View ratingsView = getLayoutInflater().inflate(R.layout.rating_star,
                null);
        ImageView ratingsImageView = (ImageView) ratingsView
                .findViewById(R.id.rating_pdp);
        if (!isHalfRating) {
            ratingsImageView.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_star_coloured));
        } else {
            ratingsImageView.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_star_halfcolored));
        }
        mRatingsLayout.addView(ratingsView);
    }

    /**
     * Method to fetch power review ratings for peoplw also bought and recently
     * viewed
     */

    public void fetchPowerReviewRatings(int position, View view) {
        try {
            if (null != productBean.getRecommendedProducts().get(position)) {
                String powerReviewrating = productBean.getRecommendedProducts()
                        .get(position).getPowerReviewRating();
                boolean ishalfRating = false;
                if (null != powerReviewrating) {
                    String fullRating[] = powerReviewrating.split("\\.");

                    int fullratingValue = Integer.parseInt(fullRating[0]);
                    int halfratingValue = Integer.parseInt(fullRating[1]);

                    for (int i = 0; i < fullratingValue; i++) {
                        ishalfRating = false;
                        populateRatings(view, ishalfRating);
                    }
                    if (0 != halfratingValue) {
                        ishalfRating = true;
                        populateRatings(view, ishalfRating);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for Recently Viewed products
     */

    public void fnRecentlyViewedinflator() {

        if (null != productBean.getRecentlyViewedProducts()
                && !productBean.getRecentlyViewedProducts().isEmpty()) {
            recentlyViewedImagesLayout.removeAllViews();
            recentlyViewedHeaderLayout.setVisibility(View.VISIBLE);
            for (int outerLoop = 0; outerLoop < productBean
                    .getRecentlyViewedProducts().size(); outerLoop++) {

                if (enableBtns == true) {
                    btnAddToBasket.setEnabled(true);
                    btnAddToBeautyList.setVisibility(View.VISIBLE);
                }

                View view = getLayoutInflater().inflate(
                        R.layout.inflate_product_options, null);
                ImageView selectAnOptionImage = (ImageView) view
                        .findViewById(R.id.selectAnOptionImage);
                TextView tvSimilarProductName = (TextView) view
                        .findViewById(R.id.tvSimilarProductName);
                TextView tvSimilarProductBrandName = (TextView) view
                        .findViewById(R.id.tvSimilarProductBrandName);
                TextView tvSimilarProductPrice = (TextView) view
                        .findViewById(R.id.similarProductPrice);
                ImageView selectAnOptionbadgeImage = (ImageView) view
                        .findViewById(R.id.selectAnOptionbadgeImage);
                mRatingsLayout = (LinearLayout) view
                        .findViewById(R.id.imageRatingsLayout);

                fetchPowerReviewRatings(outerLoop, view);
                if (null != productBean.getRecentlyViewedProducts().get(
                        outerLoop)) {
                    tvSimilarProductPrice.setText("Price :$"
                            + String.format("%.2f", productBean
                            .getRecentlyViewedProducts().get(outerLoop)
                            .getSalePrice()));
                }
                if (null != productBean.getRecentlyViewedProducts()
                        .get(outerLoop).getBrandName()) {
                    tvSimilarProductBrandName.setText(productBean
                            .getRecentlyViewedProducts().get(outerLoop)
                            .getBrandName());
                }
                if (null != productBean.getRecentlyViewedProducts()
                        .get(outerLoop).getProductDisplayName()) {
                    tvSimilarProductName.setText(productBean
                            .getRecentlyViewedProducts().get(outerLoop)
                            .getProductDisplayName());
                }

                tvSimilarProductName
                        .setTypeface(setHelveticaRegulartTypeFace());
                tvSimilarProductBrandName.setTypeface(
                        setHelveticaRegulartTypeFace(), Typeface.BOLD);
                tvSimilarProductPrice
                        .setTypeface(setHelveticaRegulartTypeFace());
                new AQuery(selectAnOptionImage).image(productBean
                                .getRecentlyViewedProducts().get(outerLoop)
                                .getProductImageUrl(), true, false, 200,
                        R.drawable.dummy_product, null, AQuery.FADE_IN);

                if (null != productBean.getRecentlyViewedProducts()
                        .get(outerLoop).getBadgeName()) {
                    setBadge(selectAnOptionbadgeImage, productBean
                            .getRecentlyViewedProducts().get(outerLoop)
                            .getBadgeName());
                }

				/*
                 * if (null != productBean.getRecentlyViewedProducts()
				 * .get(outerLoop).getBadgeImgURL()) { new
				 * AQuery(selectAnOptionbadgeImage).image(productBean
				 * .getRecentlyViewedProducts().get(outerLoop)
				 * .getBadgeImgURL(), true, false, 30, 0, null, AQuery.FADE_IN);
				 * }
				 */

                if (isRecentlyViewedClicked || isGuestAlsoBoughtClicked) {
                    svMain.scrollTo(0, titleBar.getBottom());
                    mRecentlyViewedHorizontalScrollView
                            .scrollTo(
                                    0,
                                    (recentlyViewedImagesLayout.getTop() + recentlyViewedImagesLayout
                                            .getBottom()) / 2);
                }

                final int temp = outerLoop;
                selectAnOptionImage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        pd.setMessage(LOADING_PROGRESS_TEXT);
                        pd.setCancelable(false);
                        if (null != productBean
                                && null != productBean
                                .getRecentlyViewedProducts()) {
                            id = productBean.getRecentlyViewedProducts()
                                    .get(temp).getProductId();
                        }
                        enableBtns = true;
                        loadingDialog.setVisibility(View.VISIBLE);
                        llBadgeImages.removeAllViews();
                        BadgeIdName = "";
                        fnInvokeRetrieveProductDetails(id);
                        imgProductImage.setBackgroundDrawable(null);
                        isRecentlyViewedClicked = true;
                        isComingSoon = false;
                    }
                });
                recentlyViewedImagesLayout.addView(view);
            }

        }
        isRecentlyViewedClicked = false;
        isGuestAlsoBoughtClicked = false;

    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnAddToBasket:
                if (null != productSkuBean) {

                    if (productSkuBean.isInStoreOnly()) {
                        Log.d(" show()", " show()");
                        Intent storeRTIIntent = new Intent(
                                UltaProductDetailsActivity.this,
                                StoreRTIActivity.class);
                        skuId = productSkuBean.getId();
                        storeRTIIntent.putExtra("skuId", skuId);
                        startActivity(storeRTIIntent);
                    } else {
                        Log.d(" pd.show()", " pd.show()");
                        try {
                            pd.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.Log("sku Id" + productSkuBean.getId());
                        skuId = productSkuBean.getId();
                        fnInvokeAddToCart(productHeaderBean.getId(),
                                productSkuBean.getId());
                    }

                }

                break;

            case R.id.llStockCheck:

                break;

            case R.id.detailsViewClick:

                if (llShowFullDescription.getVisibility() == 0) {
                    llShowFullDescription.setVisibility(LinearLayout.GONE);
                    llHideFullDescription.setVisibility(LinearLayout.VISIBLE);
                    llShowDescription.setVisibility(LinearLayout.VISIBLE);
                    if (null != productHeaderBean
                            && null != productHeaderBean.getLongDescription()
                            && null != productHeaderBean.getDescription()) {
                        if (productHeaderBean.getLongDescription().toString()
                                .startsWith(productHeaderBean.getDescription())
                                || productHeaderBean
                                .getDescription()
                                .toString()
                                .startsWith(
                                        productHeaderBean
                                                .getLongDescription())) {
                            tvMiniDescription.setVisibility(TextView.GONE);
                        }
                    }

                } else {
                    llShowFullDescription.setVisibility(LinearLayout.VISIBLE);
                    llHideFullDescription.setVisibility(LinearLayout.GONE);
                    llShowDescription.setVisibility(LinearLayout.GONE);
                    tvMiniDescription.setVisibility(TextView.VISIBLE);
                }

                break;

            case R.id.llShowFullDescriptionLayout:

                if (null == productWarning) {
                    webView.loadUrl("javascript:document.getElementById('warningHeading').style.display = 'none'");
                    webView.loadUrl("javascript:document.getElementById('warningDescr').style.display = 'none'");
                }

                if (null == productSpecification) {
                    webView.loadUrl("javascript:document.getElementById('specHeading').style.display = 'none'");
                    webView.loadUrl("javascript:document.getElementById('specDescr').style.display = 'none'");
                }
                llShowFullDescription.setVisibility(LinearLayout.GONE);
                llHideFullDescription.setVisibility(LinearLayout.VISIBLE);
                llShowDescription.setVisibility(LinearLayout.VISIBLE);
                if (null != productSkuBean && null != productSkuBean.getLongDescription()
                        && null != productSkuBean.getDescription()) {
                    if (productSkuBean.getLongDescription().toString()
                            .startsWith(productSkuBean.getDescription())
                            || productSkuBean
                            .getDescription()
                            .toString()
                            .startsWith(
                                    productSkuBean.getLongDescription())) {
                        tvMiniDescription.setVisibility(TextView.GONE);
                    }
                }
                break;
            case R.id.llHideFullDescriptionLayout:
                llShowFullDescription.setVisibility(LinearLayout.VISIBLE);
                llHideFullDescription.setVisibility(LinearLayout.GONE);
                llShowDescription.setVisibility(LinearLayout.GONE);
                tvMiniDescription.setVisibility(TextView.VISIBLE);
                break;
            case R.id.btnAddToBeautyList:

                if (isUltaCustomer(UltaProductDetailsActivity.this)) {

                    // if(productAddedtoFavCheck==0){
                    try {
                        pd.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (UltaDataCache.getDataCacheInstance().isAddedTofavorites()) {
                        if (null != productSkuBean)
                            fnRemoveFavorites(productSkuBean.getId());
                    } else {
                        fnInvokeFavorites();
                    }

                } else {
                    Intent intentForLogin = new Intent(
                            UltaProductDetailsActivity.this, LoginActivity.class);
                    intentForLogin.putExtra("origin", "fromProductFavotitesTap");
                    intentForLogin.putExtra("isfromProductFavotitesTap", 4);
                    startActivityForResult(intentForLogin, REQ_CODE_LOGIN);
                    // startActivity(intentForLogin);

                }

                break;

            case R.id.imgItemImage:
                if (null != productSkuBean
                        && null != productSkuBean.getLargeImageUrl()) {

                    PinchZoomActivity.launch(UltaProductDetailsActivity.this,
                            view.findViewById(R.id.imgItemImage),
                            urlForPinchZoomImage, productSkuBean.getDisplayName(),
                            brandDetailsBean.getBrandName());

				/*
                 * Intent pinchZoomIntent = new Intent(
				 * UltaProductDetailsActivity.this, PinchZoomActivity.class);
				 * pinchZoomIntent.putExtra( "imageUrl", modifyImageResolution(
				 * productSkuBean.getLargeImageUrl(), HEIGHT_IMAGE,
				 * WIDHT_IMAGE)); pinchZoomIntent.putExtra("productname",
				 * productSkuBean.getDisplayName());
				 * pinchZoomIntent.putExtra("brandname",
				 * brandDetailsBean.getBrandName());
				 * startActivity(pinchZoomIntent);
				 */
                }
                break;

            case R.id.reviewTextView:

                if (null != productSkuBean) {

                    Intent reviewAndRatingsIntent = new Intent(
                            UltaProductDetailsActivity.this,
                            ReviewAndRatingActivity.class);
                    reviewAndRatingsIntent.putExtra("id", id);
                    reviewAndRatingsIntent.putExtra("productName",
                            productSkuBean.getDisplayName());
                    if (0.0 != productSkuBean.getSalePrice()) {
                        reviewAndRatingsIntent.putExtra("productPrice", ""
                                + productSkuBean.getSalePrice());
                    } else {
                        reviewAndRatingsIntent.putExtra("productPrice", ""
                                + productSkuBean.getListPrice());
                    }
                    reviewAndRatingsIntent
                            .putExtra("productDesc", mMiniDescription);
                    startActivity(reviewAndRatingsIntent);
                }
                break;
            case R.id.llQuestionAndAnswers:
                Intent intentQnA = new Intent(UltaProductDetailsActivity.this,
                        QuestionsAndAnswersActivity.class);
                intentQnA.putExtra("id", id);
                startActivity(intentQnA);

            case R.id.llShowFullGWPLayout:
                showFullGWPLayout();
                break;
            case R.id.llHideFullGWPLayout:

                hideFullGWPLayout();
                break;
            case R.id.gwpDetailsViewClick:
                if (llShowFullGWPLayout.getVisibility() == 0) {
                    showFullGWPLayout();
                } else {
                    hideFullGWPLayout();
                }
                break;
            default:
                break;
        }
    }

    private void showFullGWPLayout() {
        if (null != gwpLayoutItems) {
            ViewGroup.LayoutParams params = gwpLayoutItems.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            gwpLayoutItems.setLayoutParams(params);
            trasnparentView.setVisibility(View.GONE);
            llShowFullGWPLayout.setVisibility(View.GONE);
            llHideFullGWPLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideFullGWPLayout() {
        if (null != gwpLayoutItems) {
            final float scale = getResources().getDisplayMetrics().density;
            int pixels = (int) (70 * scale + 0.5f);
            ViewGroup.LayoutParams parameters = gwpLayoutItems.getLayoutParams();
            parameters.height = (2 * pixels) + (pixels / 2);
            gwpLayoutItems.setLayoutParams(parameters);
            trasnparentView.setVisibility(View.VISIBLE);
            llShowFullGWPLayout.setVisibility(View.VISIBLE);
            llHideFullGWPLayout.setVisibility(View.GONE);
        }
    }
    /**
     * Method to set the values to be displayed from the bean.
     */
    /**
     *
     */
    /**
     *
     */
    private void fnSetValues() {

        tvPromotion.setVisibility(View.GONE);
        tvMsgForGCandEGC.setVisibility(View.GONE);
        fnSimilarProductsInflator();
        fnRecentlyViewedinflator();
        UltaDataCache.getDataCacheInstance().setAddedTofavorites(false);
        if (null != listOfProductSkuBeans) {
            fnSelectAnOptionInflator();
            // fnSimilarProductsInflator();
            int count = 0;
            for (int loop = 0; loop < listOfProductSkuBeans.size(); loop++) {
                productSkuBean = listOfProductSkuBeans.get(loop);

                if (null != productSkuBean.getId()) {
                    if (productSkuBean.getId().equals(skuIdFromWeeklyAd)) {
                        count = 1;
                        break;
                    } else if (null != mSkuIdFromBasket
                            && productSkuBean.getId().equals(mSkuIdFromBasket)) {
                        count = 1;
                        mSkuIdFromBasket = null;
                        break;
                    } else if (null != mSkuIdFromFavorites
                            && productSkuBean.getId().equals(
                            mSkuIdFromFavorites)) {
                        count = 1;
                        mSkuIdFromFavorites = null;
                        break;
                    }
                }
            }
            if (count != 1) {
                for (int loop = 0; loop < listOfProductSkuBeans.size(); loop++) {
                    productSkuBean = listOfProductSkuBeans.get(loop);
                    if (null != productSkuBean) {
                        if (null != from && from.equalsIgnoreCase("search")) {
                            if (productSkuBean.isSelectedOne()) {
                                if (null != productSkuBean.getHazmatCode()) {
                                    if (productSkuBean.getHazmatCode()
                                            .equalsIgnoreCase("H")) {
                                        mHazmatTextView
                                                .setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                            }
                        } else {

                            if (productSkuBean.isDefaultSku()) {
                                if (null != productSkuBean.getHazmatCode()) {
                                    if (productSkuBean.getHazmatCode()
                                            .equalsIgnoreCase("H")) {
                                        mHazmatTextView
                                                .setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            from = "";
            setValuesForProduct();
            if (null != brandDetailsBean
                    && null != brandDetailsBean.getBrandName()) {
                setTitle(brandDetailsBean.getBrandName());
            }

            btnAddToBasket.setClickable(true);
        }

        if (null != brandDetailsBean && null != brandDetailsBean.getBrandName()) {
            // tvBrandName.setText(brandDetailsBean.getBrandName());
            prodBrandName.setText(brandDetailsBean.getBrandName());
        }

        if (null != productSkuBean) {
            if (null != productSkuBean.getDescription()) {
                mMiniDescription = decodeAscii(Utility
                        .removeHTML(productSkuBean.getDescription()
                                .toString()));
                tvMiniDescription.setText(mMiniDescription);
                llShowFullDescription.setVisibility(LinearLayout.VISIBLE);
                llHideFullDescription.setVisibility(LinearLayout.GONE);
                llShowDescription.setVisibility(LinearLayout.GONE);
                tvMiniDescription.setVisibility(TextView.VISIBLE);
            }
            // 3.3 release
            llBadgeImages.setVisibility(View.GONE);
            if (isColorvarianceClicked) {
                llBadgeImages.removeAllViews();
                btnAddToBasket.setEnabled(true);
                btnAddToBeautyList.setVisibility(View.VISIBLE);
                isColorvarianceClicked = false;
                BadgeIdName = "";
            }
            if (null != productSkuBean) {
                LinearLayout giftWithPurchaseLayout = (LinearLayout) findViewById(R.id.giftWithPurchaseLayout);
                if (null != productSkuBean.getGwpSkuList()) {
                    giftWithPurchaseLayout.setVisibility(View.VISIBLE);
                    List<ProductSkuGWPBean> gwpSkuList = productSkuBean.getGwpSkuList();
                    if (gwpSkuList.size() > 0) {

                        gwpLayoutItems = (LinearLayout) findViewById(R.id.gwpLayoutItems);
                        gwpLayoutItems.removeAllViews();
                        for (int i = 0; i < gwpSkuList.size(); i++) {
                            ProductSkuGWPBean productSkuGWPBean = gwpSkuList.get(i);
                            if (null != productSkuGWPBean) {
                                final View view = getLayoutInflater().inflate(
                                        R.layout.pdp_gwp_item_layout, null);
                                ImageView gwpImage = (ImageView) view.findViewById(R.id.gwpImage);
                                TextView gwpTextView = (TextView) view.findViewById(R.id.gwpTextView);
                                if (null != productSkuGWPBean.getPromoDescription()) {
                                    gwpTextView.setText(productSkuGWPBean.getPromoDescription());
                                }
                                if (null != productSkuGWPBean.getGwpSkuImageUrl()) {
                                    ProgressBar progress = (ProgressBar) view.findViewById(R.id.product_list_title_Progress_Bar);
                                    new AQuery(gwpImage).progress(progress).image(
                                            productSkuGWPBean.getGwpSkuImageUrl(), true, false, 200,
                                            R.drawable.dummy_product, null, AQuery.FADE_IN);
                                }
                                if (i == (gwpSkuList.size() - 1)) {
                                    View seperatorView = view.findViewById(R.id.seperatorView);
                                    seperatorView.setVisibility(View.GONE);
                                }
                                gwpLayoutItems.addView(view);
                            }

                        }
                        if (gwpSkuList.size() > 2) {
                            gwpDetailsViewClick.setVisibility(View.VISIBLE);
                            hideFullGWPLayout();
                        }

                    } else {
                        giftWithPurchaseLayout.setVisibility(View.GONE);
                    }
                } else {
                    giftWithPurchaseLayout.setVisibility(View.GONE);
                }

                if (productSkuBean.getBadgeList() != null
                        && productSkuBean.getBadgeList().size() != 0) {
                    // llBadgeImages.setVisibility(View.VISIBLE);
                    llBadgeImages.setVisibility(View.VISIBLE);
                    for (int i = 0; i < productSkuBean.getBadgeList().size(); i++) {
                        ImageView imgBadgeImage = new ImageView(
                                UltaProductDetailsActivity.this, null);
                        imgBadgeImage.setPadding(5, 5, 5, 20);
                        if (null != productSkuBean.getBadgeList().get(i)
                                .getBadgeName()) {
                            String BadgeName = productSkuBean.getBadgeList()
                                    .get(i).getBadgeName();
                            if (!BadgeIdName.equals(BadgeName)) {
                                BadgeIdName = BadgeName;
                                if (BadgeIdName.equals("isNew_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_whats_new_big);
                                } else if (BadgeIdName.equals("gwp_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_free_gift_big);
                                } else if (BadgeIdName.equals("onSale_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_sale_big);
                                } else if (BadgeIdName
                                        .equals("ultaExclusive_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_ulta_exclusive_big);
                                } else if (BadgeIdName.equals("ultaPick_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_ulta_pick_big);
                                } else if (BadgeIdName
                                        .equals("fanFavorite_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_fan_fave_big);
                                } else if (BadgeIdName
                                        .equals("inStoreOnly_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_instore_big);
                                } else if (BadgeIdName
                                        .equals("onlineOnly_badge")) {
                                    imgBadgeImage
                                            .setImageResource(R.drawable.online_badge_big);
                                } else if (BadgeIdName
                                        .equals("comingSoon_badge")) {
                                    isComingSoon = true;
                                    imgBadgeImage
                                            .setImageResource(R.drawable.badge_coming_soon_big);
                                    btnAddToBasket.setVisibility(View.VISIBLE);
                                    btnAddToBasket.setEnabled(false);
                                    btnAddToBasket
                                            .setText(getResources().getString(
                                                    R.string.btn_coming_soon));
                                    if (Build.VERSION.SDK_INT > 16) {
                                        btnAddToBasket
                                                .setBackground(getResources()
                                                        .getDrawable(
                                                                R.drawable.button_disabled_rectangular_border));
                                    } else {
                                        btnAddToBasket
                                                .setBackgroundResource(R.drawable.button_disabled_rectangular_border);
                                    }
                                    btnAddToBasket
                                            .setTextColor(getResources()
                                                    .getColor(
                                                            R.color.disabled_btn_text_color));
                                }

                                llBadgeImages.addView(imgBadgeImage);
                            }

                        }
                    }
                }
            }
            // 3.2 release hiding add to basket button when the item is GWP
            if (productHeaderBean.getIsGWP() == 1) {
                btnAddToBasket.setVisibility(View.INVISIBLE);
            }

            // setting long description.
            if (null != productSkuBean.getLongDescription()) {
                // tvDetailedDescription.setText(decodeAscii(Utility
                // .removeHTML(productHeaderBean.getLongDescription()
                // .toString())));
                productDescription = productSkuBean.getLongDescription();
            }
            if (null != productSkuBean.getDirections()) {
                // llDirections.setVisibility(LinearLayout.VISIBLE);
                productDirections = productSkuBean.getDirections();
            }
            if (productHeaderBean.isShowQandA()) {
                llQuestionsAndAnswers.setVisibility(View.VISIBLE);
            } else {
                llQuestionsAndAnswers.setVisibility(View.GONE);
            }
            if (null != productSkuBean.getIngredients()) {
                // llIngredients.setVisibility(LinearLayout.VISIBLE);
                productIngredients = productSkuBean.getIngredients();
            }

            if (null != productHeaderBean.getSpecialInstructions()) {
                // llSpecification.setVisibility(LinearLayout.VISIBLE);
                productSpecification = productHeaderBean
                        .getSpecialInstructions();
            }

            if (null != productHeaderBean.getWarnings()) {
                // llWarning.setVisibility(LinearLayout.VISIBLE);
                productWarning = productHeaderBean.getWarnings();
            }

            webView = (WebView) findViewById(R.id.prodWebview);
            String mime = "text/html";
            String encoding = "utf-8";
            // WebView view=new WebView(Mock.this);
            String html = null;
            try {
                html = Utility.readFromRawFile(UltaProductDetailsActivity.this,
                        R.raw.product_description_html);
            } catch (UltaException e) {
                e.printStackTrace();
            }

            if (null != productDescription) {
                /**
                 * for GMOB-3824 because of garbage value,commenting the below code
                 */
//                productDescription = Utility.uriEscape(productDescription);
                html = html.replace("$deul$", productDescription + "\n\n");
            } else {
                html = html.replace("$deul$", "");
            }
            if (null != productSpecification) {
//                productSpecification = Utility.uriEscape(productSpecification);
                html = html.replace("$spul$", productSpecification + "\n\n");
                html = html.replace("$sphul$", "Specification");
            } else {
                html = html.replace("$spul$", "");
                html = html.replace("$sphul$", "");
            }
            if (null != productIngredients) {
//                productIngredients = Utility.uriEscape(productIngredients);
                html = html.replace("$inul$", productIngredients + "\n\n");
                html = html.replace("$inhul$", "Ingredients");
            } else {
                html = html.replace("$inul$", "");
                html = html.replace("$inhul$", "");
            }
            if (null != productDirections) {
//                productDirections = Utility.uriEscape(productDirections);
                html = html.replace("$diul$", productDirections + "\n\n");
                html = html.replace("$dihul$", "Directions");
            } else {
                html = html.replace("$diul$", "");
                html = html.replace("$dihul$", "");
            }
            if (null != productWarning) {
//                productWarning = Utility.uriEscape(productWarning);
                html = html.replace("$waul$", productWarning + "\n\n");
                html = html.replace("$wahul$", "Warning");
            } else {
                html = html.replace("$waul$", "");
                html = html.replace("$wahul$", "");
            }

            webView.getSettings().setJavaScriptEnabled(true);
            // webView.getSettings().setPluginsEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setBuiltInZoomControls(true);
            // webView.loadData(html, mime, encoding);
            webView.loadDataWithBaseURL(null, html, mime, encoding, null);

        }
        if (null != productReviewBean) {

            // tvReviewNumbers.setText("(" + productReviewBean.getReviews() +
            // ")");
            Logger.Log((float) productReviewBean.getRating());
            float rating = (float) productReviewBean.getRating();
            tvRating.setText("(" + productReviewBean.getReviews() + ")");
            // tvViewReviewNumber.setText("(" + productReviewBean.getReviews()
            // + ")");

            if (rating == 0) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating2.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating3.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 0.5) {
                imgRating1
                        .setBackgroundResource(R.drawable.icon_star_halfcolored); // make
                // it
                // half
                imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_halfcolored);
                // imgViewRating2.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating3.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 1) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating3.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 1.5) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2
                        .setBackgroundResource(R.drawable.icon_star_halfcolored);// make
                // it
                // half
                imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_halfcolored);
                // imgViewRating3.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 2) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 2.5) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3
                        .setBackgroundResource(R.drawable.icon_star_halfcolored);// make
                // it
                // half
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3
                // .setBackgroundResource(R.drawable.icon_star_halfcolored);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 3) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating4.setBackgroundResource(R.drawable.icon_star_gray);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 3.5) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating4
                        .setBackgroundResource(R.drawable.icon_star_halfcolored);// make
                // it
                // half
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating4
                // .setBackgroundResource(R.drawable.icon_star_halfcolored);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 4) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating4.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating5.setBackgroundResource(R.drawable.icon_star_gray);

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating4
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating5.setBackgroundResource(R.drawable.icon_star_gray);
            } else if (rating == 4.5) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating4.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating5
                        .setBackgroundResource(R.drawable.icon_star_halfcolored);// make
                // it
                // half

                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating4
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating5
                // .setBackgroundResource(R.drawable.icon_star_halfcolored);
            } else if (rating == 5) {
                imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating4.setBackgroundResource(R.drawable.icon_star_coloured);
                imgRating5.setBackgroundResource(R.drawable.icon_star_coloured);
                //
                // imgViewRating1
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating2
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating3
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating4
                // .setBackgroundResource(R.drawable.icon_star_coloured);
                // imgViewRating5
                // .setBackgroundResource(R.drawable.icon_star_coloured);
            }
        }

        for (int loop = 0; loop < 2; loop++) {
            inflater = LayoutInflater.from(this);
            LinearLayout layout = (LinearLayout) inflater.inflate(
                    R.layout.inflate_user_reviews_product_details, null);
            TextView tvReviewTitle = (TextView) layout
                    .findViewById(R.id.tvReviewTitle);
            TextView tvReviewDate = (TextView) layout
                    .findViewById(R.id.tvReviewDate);
            TextView tvPersonName = (TextView) layout
                    .findViewById(R.id.tvPersonName);
            TextView tvReviewDescription = (TextView) layout
                    .findViewById(R.id.tvReviewDescription);
            ImageView imgReviewRating = (ImageView) layout
                    .findViewById(R.id.imgReviewRating);

            tvReviewTitle.setText("Title of the Review");
            tvReviewDate.setText("25th Jul 2012");
            tvPersonName.setText("Name of the person");
            tvReviewDescription
                    .setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
            imgReviewRating.setBackgroundResource(R.drawable.rating5);
            // llInflateUserReviews.addView(layout);

        }
    }

    @SuppressWarnings("deprecation")
    private void setValuesForProduct() {

        if (null != productSkuBean) {
            if (productSkuBean.isFavSku()) {
                btnAddToBeautyList.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.add_fav_active));
                UltaDataCache.getDataCacheInstance().setAddedTofavorites(true);
            } else {
                btnAddToBeautyList.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.add_fav_inactive));
                UltaDataCache.getDataCacheInstance().setAddedTofavorites(false);
            }

            if (null != productSkuBean) {
                if (null != productSkuBean.getId()) {
                    tvItemId.setText(productSkuBean.getId());
                }
            }

            if (0 != productSkuBean.getSalePrice()) {
                imgSaleFlag.setVisibility(ImageView.VISIBLE);
                if (null != productSkuBean) {
                    tvListPrice.setText("$"
                            + String.format("%.2f", Double
                            .valueOf(productSkuBean.getListPrice())));
                    tvListPrice.setPaintFlags(tvListPrice.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvSalePrice.setText("$"
                            + String.format("%.2f", Double
                            .valueOf(productSkuBean.getSalePrice())));
                }
                tvListPrice.setTextColor(getResources().getColor(
                        R.color.sale_price_color));
                tvSalePrice.setTextColor(getResources().getColor(
                        R.color.marked_down_orignal_price_color));
            } else {
                if (null != productSkuBean) {
                    tvSalePrice.setText("$"
                            + String.format("%.2f", Double
                            .valueOf(productSkuBean.getListPrice())));
                }
                tvSalePrice.setTextColor(getResources().getColor(
                        R.color.orignal_price_color));
                imgSaleFlag.setVisibility(ImageView.GONE);
            }

            if (null != productSkuBean.getLargeImageUrl()) {
                new AQuery(imgProductImage).image(
                        productSkuBean.getLargeImageUrl(), true, false, 200,
                        R.drawable.dummy_product, null, AQuery.FADE_IN);

                urlForPinchZoomImage = modifyImageResolution(
                        productSkuBean.getLargeImageUrl(), HEIGHT_IMAGE,
                        WIDHT_IMAGE);

                UltaDataCache.getDataCacheInstance().getPdpHashMapOfImages()
                        .clear();
                ImageDownloader imageDownloader = new ImageDownloader(
                        urlForPinchZoomImage, UltaProductDetailsActivity.this,
                        null, "prodDetails", 0);
                imageDownloader.execute();

            }

            // if (null != productSkuBean.getSmallImageUrl()) {
            // if (!refreshTimeOutSharedPreferences.getBoolean(
            // WebserviceConstants.PROD_LARGE_IMAGE_URL, false)) {
            // new AQuery(imgSmallProductImage)
            // .image(productSkuBean.getSmallImageUrl(), true,
            // false, 200, R.drawable.dummy_product, null,
            // AQuery.FADE_IN);
            // } else {
            // String largeImageURL = productSkuBean.getSmallImageUrl()
            // .replace("$sm$", "$lg$");
            // new AQuery(imgSmallProductImage).image(largeImageURL, true,
            // false, 200, R.drawable.dummy_product, null,
            // AQuery.FADE_IN);
            // }
            //
            // // new AQuery(imgSmallProductImage).image(
            // // productSkuBean.getSmallImageUrl(), true, false, 200,
            // // R.drawable.dummy_product, null, AQuery.FADE_IN);
            // }

            if (null != productSkuBean.getDisplayName()) {
                // tvDisplayName.setText(productSkuBean.getDisplayName());
                prodDisplayName.setText(productSkuBean.getDisplayName());
            }

            if (productSkuBean.isInStoreOnly() && isComingSoon) {
                btnAddToBasket.setEnabled(false);
                btnAddToBasket.setVisibility(View.VISIBLE);
                btnAddToBasket.setText(getResources().getString(
                        R.string.btn_coming_soon));
                if (Build.VERSION.SDK_INT > 16) {
                    btnAddToBasket.setBackground(getResources().getDrawable(
                            R.drawable.button_disabled_rectangular_border));
                } else {
                    btnAddToBasket
                            .setBackgroundResource(R.drawable.button_disabled_rectangular_border);
                }
                rtiIconImageView.setVisibility(View.VISIBLE);
                btnAddToBasket.setTextColor(getResources().getColor(
                        R.color.disabled_btn_text_color));
            } else if (productSkuBean.isInStoreOnly() && !isComingSoon) {
                btnAddToBasket.setEnabled(false);
                btnAddToBasket.setVisibility(View.VISIBLE);
                btnAddToBasket.setText(getResources().getString(
                        R.string.btn_in_store));
                if (Build.VERSION.SDK_INT > 16) {
                    btnAddToBasket.setBackground(getResources().getDrawable(
                            R.drawable.button_disabled_rectangular_border));
                } else {
                    btnAddToBasket
                            .setBackgroundResource(R.drawable.button_disabled_rectangular_border);
                }
                rtiIconImageView.setVisibility(View.VISIBLE);
                btnAddToBasket.setTextColor(getResources().getColor(
                        R.color.disabled_btn_text_color));
            } else {
                setPDPDefaultButtons();
            }
            if (isRTIcheckAvailable) {
                if (productSkuBean.getFisFlagValue() == 1
                        && !productSkuBean.isInStoreOnly()) {
                    // btnFindInStore.setVisibility(View.VISIBLE);
                    rtiIconImageView.setVisibility(View.VISIBLE);
                }
            }
            if (productSkuBean.isOnlineOnly()) {
                rtiIconImageView.setVisibility(View.GONE);
            }
            if (null != productSkuBean.getIsElectronicGiftCard()
                    && productSkuBean.getIsElectronicGiftCard()
                    .equalsIgnoreCase("true")) {
                btnAddToBeautyList.setVisibility(View.GONE);
            }
            if (productSkuBean.isOnSale()) {
                productSkuBean.setOfferType("B");
            }
            if (productSkuBean.isCouponEligible()) {
                tvMsgForGCandEGC.setVisibility(View.GONE);
            } else {
                tvMsgForGCandEGC.setVisibility(View.VISIBLE);
            }
            if (null != productSkuBean.getOfferDesc()
                    && !productSkuBean.getOfferDesc().isEmpty()) {
                tvPromotion.setVisibility(View.VISIBLE);
                tvPromotion.setText(productSkuBean.getOfferDesc());
            } else {
                tvPromotion.setText("");
                tvPromotion.setVisibility(View.GONE);

            }
            if (null != productSkuBean.getFeaturetype()) {
                tvFeature.setVisibility(View.VISIBLE);
                tvFeature.setText("" + productSkuBean.getFeaturetype() + "");

            } else {
                tvFeature.setVisibility(View.GONE);
            }
        } else {
            /*
			 * final String lblPositiveButton = "OK"; final AlertDialog.Builder
			 * errorAlertDialogBuilder = new AlertDialog.Builder(
			 * UltaProductDetailsActivity.this); errorAlertDialogBuilder
			 * .setMessage("This product is no longer available.");
			 * errorAlertDialogBuilder.setTitle("Sorry");
			 * errorAlertDialogBuilder.setCancelable(false);
			 * errorAlertDialogBuilder.setPositiveButton(lblPositiveButton, new
			 * DialogInterface.OnClickListener() { public void
			 * onClick(DialogInterface dialog, int id) { dialog.dismiss();
			 * finish(); } }); final AlertDialog errorAlertDialog =
			 * errorAlertDialogBuilder .create(); errorAlertDialog.show();
			 */

            final Dialog alertDialog = showAlertDialog(
                    UltaProductDetailsActivity.this, "Sorry",
                    "This product is no longer available.", "Ok", "");
            alertDialog.show();

            mDisagreeButton.setVisibility(View.GONE);
            mAgreeButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();
                }
            });
        }

    }

    /**
     * Method to populate invoker params and fire the web service.
     *
     * @param id the id
     */
    private void fnInvokeRetrieveProductDetails(String id) {
        InvokerParams<ProductBean> invokerParams = new InvokerParams<ProductBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.PRODUCTDETAILS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams
                .setUrlParameters(fnPopulateRetrieveProductDetailsHandlerParameters(id));
        invokerParams.setAkamaiURLParameters(fnPopulateAkamaiRetrieveProductDetailsParameters(id));
        invokerParams.setUltaBeanClazz(ProductBean.class);
        RetrieveProductDetailsHandler retrieveProductDetailsHandler = new RetrieveProductDetailsHandler();
        invokerParams.setUltaHandler(retrieveProductDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaProductDetailsActivity><fnInvokeRetrieveProductDetails()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param id the id
     * @return the map
     */
    private Map<String, String> fnPopulateRetrieveProductDetailsHandlerParameters(
            String id) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "0");
        urlParams.put("arg1", id);
        if (null != mSkuIdFromBasket) {
            urlParams.put("selectedSkuId", mSkuIdFromBasket);
        }
        if (null != from && from.equalsIgnoreCase("search")) {
            urlParams.put("selectedSkuId", skuIdFromSearch);
        }
        if (null != mSkuIdFromFavorites) {
            urlParams.put("selectedSkuId", mSkuIdFromFavorites);
        }
        return urlParams;
    }

    /**
     * Method to populate the Akami data cache URL parameter map.
     *
     * @param id the id
     * @return the map
     */
    private Map<String, String> fnPopulateAkamaiRetrieveProductDetailsParameters(
            String id) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("arg1", id);
       /* pass only arg1 as per jira doc for akamai
        if (null != mSkuIdFromBasket) {
            urlParams.put("selectedSkuId", mSkuIdFromBasket);
        }
        if (null != from && from.equalsIgnoreCase("search")) {
            urlParams.put("selectedSkuId", skuIdFromSearch);
        }
        if (null != mSkuIdFromFavorites) {
            urlParams.put("selectedSkuId", mSkuIdFromFavorites);
        }*/
        return urlParams;
    }

    /**
     * The Class RetrieveProductDetailsHandler.
     */
    public class RetrieveProductDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
			/*
			 * if (pd != null && pd.isShowing()) { pd.dismiss(); }
			 */
            loadingDialog.setVisibility(View.GONE);
            if (null != getErrorMessage()) {
                try {
                    Logger.Log("ERROR");
                    // productDetailsAction
                    // .reportError(
                    // getErrorMessage(),
                    // WebserviceConstants.DYN_ERRCODE_PRODUCT_DETAILS_PAGE);
                    // productDetailsAction.leaveAction();
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductDetailsActivity.this);
                    setError(UltaProductDetailsActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

                fnInvokeOlapicRefDetails();
                btnAddToBasket.setVisibility(View.VISIBLE);
                Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                productBean = (ProductBean) getResponseBean();

                if (null != productBean) {
                    if (null != productBean.getErrorInfos()) {
                        setError(UltaProductDetailsActivity.this, productBean
                                .getErrorInfos().get(0));
                    }
                    svMain.setVisibility(View.VISIBLE);
                    brandDetailsBean = productBean.getBrandDetails();
                    isRTIcheckAvailable = productBean
                            .getEnableStoreSearchinPDP();
                    categoryBean = productBean.getCategoryDetails();
                    listOfProductFeatureBean = productBean.getProductFeatures();
                    productHeaderBean = productBean.getProductHeader();
                    productReviewBean = productBean.getProductReview();
                    listOfProductSkuBeans = productBean.getSkuDetails();

                    fnSetValues();

                    if (null != productSkuBean) {
                        // productToShareImgURL = productSkuBean
                        // .getLargeImageUrl();
						/*
						 * trackAppState(UltaProductDetailsActivity.this, "PD:"
						 * + productSkuBean.getId() + " " +
						 * productBean.getBrandDetails().getBrandName() + "::" +
						 * productSkuBean.getDisplayName());
						 * 
						 * trackEvarsUsingActionName(UltaProductDetailsActivity.this
						 * , WebserviceConstants.REAL_PRODUCT_VIEWS,
						 * WebserviceConstants.PRODUCTS_KEY, ";"+id);
						 */

                        OmnitureTracking
                                .startActivity(UltaProductDetailsActivity.this);

                        Map<String, Object> omnitureData = new HashMap<String, Object>();
                        omnitureData.put(WebserviceConstants.PRODUCTS_KEY, ";"
                                + productSkuBean.getId());
                        omnitureData.put(WebserviceConstants.EVENT_KEY,
                                "prodView,event21");

                        String pageName = "PD:" + productSkuBean.getId() + " "
                                + productBean.getBrandDetails().getBrandName()
                                + "::" + productSkuBean.getDisplayName();

                        Analytics.trackState(pageName, omnitureData);
                        OmnitureTracking.stopActivity();

                        productToShareText = productSkuBean.getDisplayName();
                        UltaDataCache.getDataCacheInstance().setProductName(
                                productSkuBean.getDisplayName());
                        UltaDataCache.getDataCacheInstance().setProductSkuName(
                                productSkuBean.getFeaturetype());
                        UltaDataCache.getDataCacheInstance().setBrandName(
                                productBean.getBrandDetails().getBrandName());
                        productToShareID = productBean.getProductHeader()
                                .getId();
                        productToShareImgURL = "http://www.ulta.com/ulta/browse/productDetail.jsp?productId="
                                + productToShareID;
                    } else {
						/*
						 * final String lblPositiveButton = "OK"; final
						 * AlertDialog.Builder errorAlertDialogBuilder = new
						 * AlertDialog.Builder(
						 * UltaProductDetailsActivity.this);
						 * errorAlertDialogBuilder
						 * .setMessage("This product is no longer available.");
						 * errorAlertDialogBuilder.setTitle("Sorry");
						 * errorAlertDialogBuilder.setCancelable(false);
						 * errorAlertDialogBuilder.setPositiveButton(
						 * lblPositiveButton, new
						 * DialogInterface.OnClickListener() { public void
						 * onClick(DialogInterface dialog, int id) {
						 * dialog.dismiss(); finish(); } }); final AlertDialog
						 * errorAlertDialog = errorAlertDialogBuilder .create();
						 * errorAlertDialog.show();
						 */

                        final Dialog alertDialog = showAlertDialog(
                                UltaProductDetailsActivity.this, "Sorry",
                                "This product is no longer available.", "Ok",
                                "");
                        alertDialog.show();

                        mDisagreeButton.setVisibility(View.GONE);
                        mAgreeButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();
                                finish();
                            }
                        });

                    }

                    // productDetailsAction.reportEvent("ProductDetails success");
                    // productDetailsAction.leaveAction();
                }
            }
        }
    }

    /**
     * Fn invoke add to cart.
     *
     * @param productId the product id
     * @param skuId     the sku id
     */
    private void fnInvokeAddToCart(String productId, String skuId) {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.ADDITEMTOORDER_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(fnPopulateAddToCartHandlerParameters(
                productId, skuId));
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        AddToCartHandler addToCartHandler = new AddToCartHandler();
        invokerParams.setUltaHandler(addToCartHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaProductDetailsActivity><fnInvokeAddToCart()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param productId the product id
     * @param skuId     the sku id
     * @return the map
     */
    private Map<String, String> fnPopulateAddToCartHandlerParameters(
            String productId, String skuId) {

        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("productId", productId);// "xlsImpprod3250327"
        urlParams.put("skuId", skuId);// "2208227"
        urlParams.put("quantity", "1");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");

        return urlParams;
    }

	/*
	 * Handler to handle the response from the web service
	 */

    /**
     * The Class AddToCartHandler.
     */
    public class AddToCartHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<AddToCartHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            try {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductDetailsActivity.this);
                    setError(UltaProductDetailsActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                UltaDataCache.getDataCacheInstance()
                        .setAnonymousCheckout(false);
                Logger.Log("<AddToCartHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
				/*
				 * trackEvarsUsingActionName(UltaProductDetailsActivity.this,
				 * WebserviceConstants.EVENT_SC_ADD,
				 * WebserviceConstants.PRODUCTS_KEY, skuId);
				 */

                addToCartBean = (AddToCartBean) getResponseBean();
                if (null != addToCartBean) {
                    List<String> errors = addToCartBean.getErrorInfos();
                    if (null != errors && !(errors.isEmpty())) {
                        try {
                            notifyUser(errors.get(0),
                                    UltaProductDetailsActivity.this);
                            setError(UltaProductDetailsActivity.this,
                                    errors.get(0));
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }
                    } else {
                        StringBuffer productString = new StringBuffer();
                        ComponentBean componentBean = addToCartBean
                                .getComponent();
                        if (null != componentBean) {
                            if (null != componentBean.getCart()) {
                                CartBean cartBean = componentBean.getCart();
                                boolean isOutOfStock = false;
                                if (null != cartBean.getCommerceItems()) {
                                    List<CommerceItemBean> listOfCommerceItemBean = cartBean
                                            .getCommerceItems();

                                    for (int loop = 0; loop < listOfCommerceItemBean
                                            .size(); loop++) {
                                        CommerceItemBean commerceItemBean = listOfCommerceItemBean
                                                .get(loop);
                                        if (null != commerceItemBean) {
                                            if (commerceItemBean
                                                    .getCatalogRefId() != null
                                                    && commerceItemBean
                                                    .getCatalogRefId()
                                                    .equals(skuId)) {
                                                productString.append(";");
                                                productString
                                                        .append(commerceItemBean
                                                                .getCatalogRefId());
                                                if (commerceItemBean
                                                        .isOutOfStock()) {
                                                    isOutOfStock = true;
                                                    fnInvokeRemoveItem(commerceItemBean);
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                }

                                if (null != productString) {
                                    OmnitureTracking
                                            .startActivity(UltaProductDetailsActivity.this);

                                    Map<String, Object> omnitureData = new HashMap<String, Object>();
                                    omnitureData.put(
                                            WebserviceConstants.PRODUCTS_KEY,
                                            productString.toString());
                                    omnitureData.put(
                                            WebserviceConstants.EVENT_KEY,
                                            "scAdd");

                                    Analytics.trackAction(
                                            WebserviceConstants.EVENT_SC_ADD,
                                            omnitureData);
                                    OmnitureTracking.stopActivity();

									/*
									 * trackEvarsUsingActionName(
									 * UltaProductDetailsActivity.this,
									 * WebserviceConstants.EVENT_SC_ADD,
									 * WebserviceConstants.PRODUCTS_KEY,
									 * productString.toString());
									 */
                                }

                                if (!isOutOfStock) {
                                    if (addToCartBean.isResult()) {
                                        try {
                                            setItemCountInBasket(getItemCountInBasket() + 1);
                                            String messageToBeDisplayed = productSkuBean
                                                    .getDisplayName()
                                                    + " is added to your bag";

                                            final Dialog alertDialog = showAlertDialog(
                                                    UltaProductDetailsActivity.this,
                                                    "Bag",
                                                    messageToBeDisplayed, "OK",
                                                    "View Bag");
                                            alertDialog.show();

                                            mAgreeButton
                                                    .setOnClickListener(new OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                View arg0) {
                                                            alertDialog
                                                                    .dismiss();
                                                        }
                                                    });

                                            mDisagreeButton
                                                    .setOnClickListener(new OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                View v) {
                                                            Intent intent = new Intent(
                                                                    UltaProductDetailsActivity.this,
                                                                    ViewItemsInBasketActivity.class);
                                                            startActivity(intent);
                                                            alertDialog
                                                                    .dismiss();
                                                        }
                                                    });

                                        } catch (WindowManager.BadTokenException e) {
                                        } catch (Exception e) {
                                        }
                                    }
                                } else {

                                    try {
                                        // call the remove item from basket
                                        // service
										/*
										 * final AlertDialog.Builder alert = new
										 * AlertDialog.Builder(
										 * UltaProductDetailsActivity.this);
										 * alert.setTitle("Out Of Stock");
										 * alert.setMessage(productSkuBean
										 * .getDisplayName() +
										 * " is Out of Stock so can not be added to the Basket"
										 * ); alert.setPositiveButton( "Ok", new
										 * DialogInterface.OnClickListener() {
										 * 
										 * @Override public void onClick(
										 * DialogInterface dialog, int which) {
										 * dialog.dismiss(); } }); // alert.
										 * setMessage("Select Free Samples");
										 * alert.create().show();
										 */

                                        String messageToBeDisplayed = productSkuBean
                                                .getDisplayName()
                                                + " is Out of Stock so can not be added to the Basket";
                                        final Dialog alert = showAlertDialog(
                                                UltaProductDetailsActivity.this,
                                                "Out Of Stock",
                                                messageToBeDisplayed, "OK", "");
                                        alert.show();
                                        mDisagreeButton
                                                .setVisibility(View.GONE);

                                        mAgreeButton
                                                .setOnClickListener(new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {

                                                        alert.dismiss();
                                                    }
                                                });

                                    } catch (WindowManager.BadTokenException e) {
                                    } catch (Exception e) {
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * End of service
     */
    private void fnInvokeRemoveItem(CommerceItemBean commerceBeanRemove) {
        String relationshipId = commerceBeanRemove.getRelationshipId();
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.REMOVEITEM_FROM_ORDERBY_RELATIONSHIPID_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams
                .setUrlParameters(fnPopulateRemoveItemParameters(relationshipId));
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        RemoveItemHandler removeItemHandler = new RemoveItemHandler(
                commerceBeanRemove);
        invokerParams.setUltaHandler(removeItemHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ViewItemsInBasketActivity><fnInvokeRemoveItem()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Method to populate the URL parameter map
     *
     * @param id
     */
    private Map<String, String> fnPopulateRemoveItemParameters(
            String relationshipId) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");

        // xyz is the relationship id of the product which will be retrieved
        // from view cart response
        urlParams.put("removalRelationshipIdProxy", relationshipId);
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        return urlParams;
    }

    /*
     * Handler to handle the response from the web service
     */
    public class RemoveItemHandler extends UltaHandler {
        @SuppressWarnings("unused")
        private CommerceItemBean commerceBeanRemove;

        public RemoveItemHandler(CommerceItemBean commerceBeanRemove) {
            this.commerceBeanRemove = commerceBeanRemove;
        }

        /**
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RemoveItemHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {

                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductDetailsActivity.this);
                    setError(UltaProductDetailsActivity.this, getErrorMessage());

                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }

            }
        }
    }

    public String decodeAscii(String strWithAscii) {
        String returnString = strWithAscii;
        if (returnString != null) {
            returnString = returnString.replaceAll("&#64;", "@");
            returnString = returnString.replaceAll("&#169;", "�");
            returnString = returnString.replaceAll("&#174;", "�");
            returnString = returnString.replaceAll("&#231;", "�");
            returnString = returnString.replaceAll("&#8482;", "�");
            // returnString = returnString.replaceAll("&#201;", "�");
            returnString = returnString.replaceAll("&#201;", "É");
            returnString = returnString.replaceAll("&#233;", "�");
            returnString = returnString.replaceAll("&#39;", "'");
        }
        Logger.Log("rs" + returnString);
        return returnString;
    }

    private void fnInvokeFavorites() {

        InvokerParams<FavoritesComponentBean> invokerParams = new InvokerParams<FavoritesComponentBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.ADD_FAVORITES_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(fnFavoritesItemParameters());
        invokerParams.setUltaBeanClazz(FavoritesComponentBean.class);
        FavoritesHandler favoritesHandler = new FavoritesHandler();
        invokerParams.setUltaHandler(favoritesHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaProductDetailsActivity><fnInvokeFavorites()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Method to populate the URL parameter map
     *
     * @param id
     */
    private Map<String, String> fnFavoritesItemParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");

        // xyz is the relationship id of the product which will be retrieved
        // from view cart response
        if (null != productBean && null != productBean.getProductHeader())
            urlParams.put("productId", productBean.getProductHeader().getId());

        if (null != productSkuBean)
            urlParams.put("skuId", productSkuBean.getId());

        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("atg-rest-output", "json");
        urlParams.put("quantity", "1");
        return urlParams;

    }

    /*
     * Handler to handle the response from the web service
     */
    public class FavoritesHandler extends UltaHandler {

        /**
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<FavoritesHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }

			/*
			 * final AlertDialog.Builder alertFavorite = new
			 * AlertDialog.Builder( UltaProductDetailsActivity.this);
			 * alertFavorite.setTitle("Favorites");
			 * alertFavorite.setMessage(productSkuBean.getDisplayName() +
			 * " is added to Favorite"); alertFavorite.setPositiveButton("Ok",
			 * new DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * { productAddedtoFavCheck=1; dialog.dismiss(); } });
			 * alertFavorite.setNegativeButton("View Favorites", new
			 * DialogInterface.OnClickListener() {
			 * 
			 * @Override public void onClick(DialogInterface dialog, int which)
			 * {
			 */
			/*
			 * Intent intent = new Intent(UltaProductDetailsActivity.this,
			 * FavoritesActivity.class); startActivity(intent);
			 */
            btnAddToBeautyList.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.add_fav_active));
            UltaDataCache.getDataCacheInstance().setAddedTofavorites(true);
            Toast.makeText(UltaProductDetailsActivity.this,
                    "Item added to favorites", Toast.LENGTH_LONG).show();
			/*
			 * } }); // alert.setMessage("Select Free Samples");
			 * alertFavorite.create().show();
			 */

            if (null != getErrorMessage()) {

                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductDetailsActivity.this);

                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }

            } else {

                FavoritesComponentBean mobileFavCartBean = (FavoritesComponentBean) getResponseBean();

                if (null != mobileFavCartBean) {

                    if (null != mobileFavCartBean.getComponent()) {
                        if (null != mobileFavCartBean.getComponent()
                                .getMobileFavCart()) {
                            if (null != mobileFavCartBean.getComponent()
                                    .getMobileFavCart().getTotalNoOfProducts()) {
                                setFavoritesCountInNavigationDrawer(Integer
                                        .parseInt(mobileFavCartBean
                                                .getComponent()
                                                .getMobileFavCart()
                                                .getTotalNoOfProducts()));
                            }
                        }
                    }

                }

            }
        }
    }

    public void showAddToCartSuccess() {
        try {
            setItemCountInBasket(getItemCountInBasket() + 1);
            final AlertDialog.Builder alert = new AlertDialog.Builder(
                    UltaProductDetailsActivity.this);
            alert.setTitle("Basket");
            alert.setMessage(productSkuBean.getDisplayName()
                    + " is added to Basket");
            alert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
            alert.setNegativeButton("View Basket",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    UltaProductDetailsActivity.this,
                                    ViewItemsInBasketActivity.class);
                            startActivity(intent);
                        }
                    });
            alert.create().show();
        } catch (WindowManager.BadTokenException e) {
        } catch (Exception e) {
        }
    }

    public void fnInvokeOlapicRefDetails() {
        InvokerParams<OlapicProductDetailsBean> invokerParams = new InvokerParams<OlapicProductDetailsBean>();

        String olapicService = getResources().getString(
                R.string.olapic_prod_details);

        UltaDataCache.getDataCacheInstance().setOlapicProdDetails(true);
        invokerParams.setServiceToInvoke(olapicService);
        invokerParams.setHttpMethod(HttpMethod.GET_OLAPIC);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(fnPopulateOlapicImagesHandlerParameters(
                WebserviceConstants.OLAPIC_AUTH_TOKEN, id));
        invokerParams.setUltaBeanClazz(OlapicProductDetailsBean.class);
        OlapicImagesHandler olapicImagesHandler = new OlapicImagesHandler();
        invokerParams
                .setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
        invokerParams.setUltaHandler(olapicImagesHandler);
        invokerParams.setCookieHandlingSkip(true);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<OlapicActivity><invokeOlapicImages()><UltaException>>"
                    + ultaException);
        }

    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param id the id
     * @return the map
     */
    private Map<String, String> fnPopulateOlapicImagesHandlerParameters(
            String authToken, String productId) {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("auth_token", authToken);
        urlParams.put("tag_key", productId);
        urlParams.put("version", "v2.1");
        return urlParams;
    }

    public class OlapicImagesHandler extends UltaHandler {

        public void handleMessage(Message msg) {

            if (null != getErrorMessage()) {
                try {
                    Logger.Log("ERROR");
                  /*  notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductDetailsActivity.this);*/
                    setError(UltaProductDetailsActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

                try {
                    JSONObject jsonObj = new JSONObject(UltaDataCache
                            .getDataCacheInstance()
                            .getOlapicProdDetailsResponse());

                    JSONObject data = jsonObj.getJSONObject("data");

                    if (null != data) {
                        JSONObject _embedded = data.getJSONObject("_embedded");

                        if (null != _embedded) {
                            JSONObject mediaRecent = _embedded
                                    .getJSONObject("media:recent");

                            if (null != mediaRecent) {
                                JSONObject links = mediaRecent
                                        .getJSONObject("_links");

                                if (null != links) {
                                    JSONObject self = links
                                            .getJSONObject("self");

                                    if (null != self) {
                                        String olapicProdDetailsHRef = self
                                                .getString("href");

                                        if (null != olapicProdDetailsHRef) {

                                            String streamsIdAftrSplit[] = olapicProdDetailsHRef
                                                    .split("/");
                                            for (int i = 2; i < streamsIdAftrSplit.length; i++) {
                                                if (Character
                                                        .isDigit(streamsIdAftrSplit[i]
                                                                .charAt(0))
                                                        && Character
                                                        .isDigit(streamsIdAftrSplit[i]
                                                                .charAt(streamsIdAftrSplit[i]
                                                                        .length() - 1))) {
                                                    streamsId = streamsIdAftrSplit[i];
                                                }
                                            }

                                            invokeOlapicImages(olapicProdDetailsHRef);
                                        }
                                    }
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public void invokeOlapicImages(String href) {

        InvokerParams<OlapicProductDetailsBean> invokerParams = new InvokerParams<OlapicProductDetailsBean>();

        UltaDataCache.getDataCacheInstance().setOlapic(true);
        invokerParams.setServiceToInvoke(href + "?");
        invokerParams.setHttpMethod(HttpMethod.GET_OLAPIC);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams
                .setUrlParameters(fnPopulateOlapicImagesFinalHandlerParameters(
                        WebserviceConstants.OLAPIC_VERSION,
                        WebserviceConstants.OLAPIC_AUTH_TOKEN));
        invokerParams.setUltaBeanClazz(OlapicProductDetailsBean.class);
        OlapicFinalImagesHandler olapicFinalImagesHandler = new OlapicFinalImagesHandler();
        invokerParams
                .setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
        invokerParams.setUltaHandler(olapicFinalImagesHandler);
        invokerParams.setCookieHandlingSkip(true);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<OlapicActivity><invokeOlapicImages()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param id the id
     * @return the map
     */
    private Map<String, String> fnPopulateOlapicImagesFinalHandlerParameters(
            String id, String authToken) {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("&auth_token", authToken);
        urlParams.put("version", id);
        return urlParams;
    }

    public class OlapicFinalImagesHandler extends UltaHandler {
        public void handleMessage(Message msg) {

            if (null != getErrorMessage()) {
                try {
                    Logger.Log("ERROR");
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            UltaProductDetailsActivity.this);
                    setError(UltaProductDetailsActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {

                Logger.Log("<OlapicProductDetailsFinalImageHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                try {
                    JSONObject jsonObj = new JSONObject(UltaDataCache
                            .getDataCacheInstance()
                            .getOlapicProdDetailsResponse());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                parseStreamsAll();
                olapicProductDetailsBean = (OlapicProductDetailsBean) getResponseBean();

                if (null != olapicProductDetailsBean) {
                    olapicProductDetailsDataBean = olapicProductDetailsBean
                            .getData();

                    if (null != olapicProductDetailsDataBean) {
                        olapicImageLayout2.setVisibility(View.VISIBLE);
                        olapicView.setVisibility(View.VISIBLE);
                        olapicProductDetailsEmbeddedBean = olapicProductDetailsDataBean
                                .get_embedded();

                        if (null != olapicProductDetailsEmbeddedBean) {

                            olapicProductDetailsMediaBean = olapicProductDetailsEmbeddedBean
                                    .getMedia();

                            if (null != olapicProductDetailsMediaBean) {
                                for (int i = 0; i < olapicProductDetailsMediaBean
                                        .size(); i++) {

                                    status = olapicProductDetailsMediaBean.get(
                                            i).getStatus();

                                    mCaption.add(olapicProductDetailsMediaBean
                                            .get(i).getCaption());

                                    olapicInnerEmbeddedBean = olapicProductDetailsMediaBean
                                            .get(i).get_embedded();

                                    if (null != olapicInnerEmbeddedBean) {

                                        if (null != olapicInnerEmbeddedBean
                                                .getUploader()) {
                                            mUserNameWhoUploaded
                                                    .add(olapicInnerEmbeddedBean
                                                            .getUploader()
                                                            .getName());
                                            mAvatarUrl
                                                    .add(olapicInnerEmbeddedBean
                                                            .getUploader()
                                                            .getAvatar_url());
                                        }
                                    }

                                    if (null != status
                                            && status
                                            .equalsIgnoreCase("approved")) {
                                        if (null != olapicProductDetailsMediaBean
                                                .get(i)) {
                                            olapicProductDetailsImagesBean = olapicProductDetailsMediaBean
                                                    .get(i).getImages();

                                            if (null != olapicProductDetailsImagesBean) {
                                                UltaDataCache
                                                        .getDataCacheInstance()
                                                        .setOlapicProdDetails(
                                                                false);
                                                olapicMobileImage = olapicProductDetailsImagesBean
                                                        .getMobile();
                                                mImageLink
                                                        .add(olapicMobileImage);
                                                OLAPIC_IMAGE_COUNT++;
                                                populateOlapicImages(olapicMobileImage);
                                            }
                                        }

                                    }

                                }

                            }
                        }

                    }
                }

            }
        }

    }

    public void populateOlapicImages(String olapicMobileImage) {
        // olapicView.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = new LinearLayout(this);
        final LinearLayout finalLinearLayout = linearLayout;
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.olapic_productdetails, linearLayout);
        olapicImageView = (ImageView) linearLayout
                .findViewById(R.id.olapic_image_prod_details);

        new AQuery(olapicImageView).image(olapicMobileImage, true, true, 0,
                R.drawable.dummy_product, null, AQuery.FADE_IN);
        uploadBtn.setVisibility(View.VISIBLE);
        olapicImagesLayout.addView(linearLayout);

        finalLinearLayout.setId(OLAPIC_IMAGE_COUNT);

        olapicImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent pinchZoomIntent = new Intent(
                // UltaProductDetailsActivity.this,
                // PinchZoomActivity.class);
                // pinchZoomIntent.putExtra(
                // "imageUrl",
                // modifyImageResolution(olapicImageLink, HEIGHT_IMAGE,
                // WIDHT_IMAGE));
                final int position = finalLinearLayout.getId();

                Intent olapicImageDetailsIntent = new Intent(
                        UltaProductDetailsActivity.this,
                        OlapicImageDetailsActivity.class);
                olapicImageDetailsIntent.putExtra("imageLink",
                        mImageLink.get(position));
                olapicImageDetailsIntent.putExtra("nickName",
                        mUserNameWhoUploaded.get(position));
                olapicImageDetailsIntent.putExtra("avatar_url",
                        mAvatarUrl.get(position));
                olapicImageDetailsIntent.putExtra("caption",
                        mCaption.get(position));
                olapicImageDetailsIntent.putExtra("index", position);
                olapicImageDetailsIntent.putExtra("fromPDP", true);
                startActivity(olapicImageDetailsIntent);
            }
        });

    }

    public void invokeMediaIdService() {

        InvokerParams<OlapicUploadMediaBean> invokerParams = new InvokerParams<OlapicUploadMediaBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.OLAPIC_UPLOAD_MEDIA_FIRST);
        invokerParams.setHttpMethod(HttpMethod.POST_OLAPIC);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateMediaIdParameters());
        invokerParams.setUltaBeanClazz(OlapicUploadMediaBean.class);
        invokerParams
                .setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
        OlapicUploadMediaHandler olapicUploadMediaHandler = new OlapicUploadMediaHandler();
        invokerParams.setUltaHandler(olapicUploadMediaHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
                    + ultaException);
            uploadProgressDialog.dismiss();
        }
    }

    private Map<String, String> populateMediaIdParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();

        if (UltaDataCache.getDataCacheInstance().getLoginName() != null) {
            urlParams.put("email", UltaDataCache.getDataCacheInstance()
                    .getLoginName());
            urlParams.put("screen_name", UltaDataCache.getDataCacheInstance()
                    .getLoginName());
        } else {
            SharedPreferences staySignedInSharedPreferences = getSharedPreferences(
                    WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);
            String secretKey = staySignedInSharedPreferences.getString(
                    WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
            String userName = Utility.decryptString(
                    staySignedInSharedPreferences.getString(
                            WebserviceConstants.STAY_SIGNED_IN_USERNAME, " "),
                    secretKey);
            urlParams.put("email", userName);
            urlParams.put("screen_name", userName);

        }

        urlParams.put("avatar_url",
                getResources().getString(R.string.avatar_url));
        return urlParams;

    }

    public class OlapicUploadMediaHandler extends UltaHandler {

        public void handleMessage(Message msg) {

            try {
                if (null != getErrorMessage()) {
                    try {
                        uploadProgressDialog.dismiss();
                        Logger.Log("ERROR");
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                UltaProductDetailsActivity.this);
                        setError(UltaProductDetailsActivity.this,
                                getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                        uploadProgressDialog.dismiss();
                    } catch (Exception e) {
                        uploadProgressDialog.dismiss();
                    }
                } else {

                    Logger.Log("<OlapicImagesHandler><handleMessage><getResponseBean>>"
                            + (getResponseBean()));

                    olapicUploadMediaBean = (OlapicUploadMediaBean) getResponseBean();

                    if (null != olapicUploadMediaBean) {
                        olapicUploadMediaDataBean = olapicUploadMediaBean
                                .getData();

                        if (null != olapicUploadMediaDataBean) {
                            mediaId = olapicUploadMediaDataBean.getId();
                            uploadImageToOlapicService();
                        }
                    }
                }

            } catch (Exception e) {
                uploadProgressDialog.dismiss();
            }

        }

    }

    public void uploadImageToOlapicService() {

        InvokerParams<OlapicUploadSuccessBean> invokerParams = new InvokerParams<OlapicUploadSuccessBean>();
        invokerParams.setServiceToInvoke(getResources().getString(
                R.string.upload_image_url,mediaId));
        invokerParams.setHttpMethod(HttpMethod.MULTIPOST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateUploadUrlParameters());
        invokerParams.setUltaBeanClazz(OlapicUploadSuccessBean.class);
        invokerParams
                .setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
        OlapicUploadSuccessHandler olapicUploadSuccessHandler = new OlapicUploadSuccessHandler();
        invokerParams.setUltaHandler(olapicUploadSuccessHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
                    + ultaException);
            uploadProgressDialog.dismiss();
        }
    }

    public Map<String, String> populateUploadUrlParameters() {

        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("caption", "Testing");
        urlParams.put("file", filePath);
       /* urlParams.put("stream_uri",
                getResources().getString(R.string.stream_uri, streamsId));*/
        return urlParams;

    }

    public class OlapicUploadSuccessHandler extends UltaHandler {

        public void handleMessage(Message msg) {
            uploadProgressDialog.dismiss();
            try {
                if (null != getErrorMessage()) {
                    try {
                        Logger.Log("ERROR");
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                UltaProductDetailsActivity.this);
                        setError(UltaProductDetailsActivity.this,
                                getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                } else {
                    showUploadedSuccessfullyMessage();
                    // Toast.makeText(UltaProductDetailsActivity.this,
                    // "Image Uploaded Successfully", Toast.LENGTH_LONG)
                    // .show();
                }

            } catch (Exception e) {
            }

        }

    }

    public void showUploadedSuccessfullyMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                UltaProductDetailsActivity.this);
        alertDialogBuilder.setTitle("Upload Complete");

        alertDialogBuilder
                .setMessage(
                        "Your photo have succesfully been uploaded.Would you like to upload more photos?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (UltaDataCache.getDataCacheInstance()
                                        .isLoggedIn()) {
                                    Intent intent = new Intent(
                                            UltaProductDetailsActivity.this,
                                            OlapicUploadActivity.class);
                                    startActivityForResult(intent,
                                            ACTIVITY_SELECT_IMAGE);

                                } else {
                                    Intent intentForLogin = new Intent(
                                            UltaProductDetailsActivity.this,
                                            LoginActivity.class);
                                    startActivity(intentForLogin);
                                }
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void fnRemoveFavorites(String id) {

        InvokerParams<FavoritesComponentBean> invokerParams = new InvokerParams<FavoritesComponentBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.REMOVE_FAVORITES_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(fnRemoveFavoritesItemParameters(id));
        invokerParams.setUltaBeanClazz(FavoritesComponentBean.class);
        RemoveFavoritesHandler removeFavoritesHandler = new RemoveFavoritesHandler();
        invokerParams.setUltaHandler(removeFavoritesHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaProductDetailsActivity><fnInvokeFavorites()><UltaException>>"
                    + ultaException);
        }
    }

    /**
     * Method to populate the URL parameter map
     *
     * @param id
     */
    private Map<String, String> fnRemoveFavoritesItemParameters(String id) {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("atg-rest-depth", "1");
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("skuId", id);
        return urlParams;

    }

    /*
     * Handler to handle the response from the web service
     */
    public class RemoveFavoritesHandler extends UltaHandler {

        /**
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @SuppressWarnings("deprecation")
        public void handleMessage(Message msg) {
            Logger.Log("<RemoveFavoritesHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            FavoritesComponentBean mobileFavCartBean = (FavoritesComponentBean) getResponseBean();

            if (null != mobileFavCartBean) {

                if (null != mobileFavCartBean.getComponent()) {
                    if (null != mobileFavCartBean.getComponent()
                            .getMobileFavCart()) {
                        if (null != mobileFavCartBean.getComponent()
                                .getMobileFavCart().getTotalNoOfProducts()) {
                            setFavoritesCountInNavigationDrawer(Integer
                                    .parseInt(mobileFavCartBean.getComponent()
                                            .getMobileFavCart()
                                            .getTotalNoOfProducts()));
                        }
                    }
                }

            }

            btnAddToBeautyList.setBackgroundDrawable(getResources()
                    .getDrawable(R.drawable.add_fav_inactive));
            UltaDataCache.getDataCacheInstance().setAddedTofavorites(false);
            Toast.makeText(UltaProductDetailsActivity.this,
                    "Item removed from favorites", Toast.LENGTH_LONG).show();
        }
    }

    public static void launch(Activity activity, View transitionView,
                              String url, String id, int position) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, transitionView,
                        EXTRA_IMAGE);
        Intent ultaProductDetailsIntent = new Intent(activity,
                UltaProductDetailsActivity.class);
        ultaProductDetailsIntent.putExtra(EXTRA_IMAGE, url);
        ultaProductDetailsIntent.putExtra("urlForTransition", url);
        ultaProductDetailsIntent.putExtra("id", id);
        ultaProductDetailsIntent.putExtra("clickedPosition", position);
        ultaProductDetailsIntent.setAction("fromProductList");
        ActivityCompat.startActivity(activity, ultaProductDetailsIntent,
                options.toBundle());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UltaDataCache.getDataCacheInstance().setSetImageInPlpPosition(
                imagePosition);
        finish();
    }

    @Override
    public void onImageDownload() {
        imgProductImage.setClickable(true);

    }

    public void setBadge(ImageView imgBadgeImage, String BadgeIdName) {

        if (BadgeIdName.equals("isNew_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_whats_new);
        } else if (BadgeIdName.equals("gwp_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_free_gift);
        } else if (BadgeIdName.equals("onSale_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_sale);
        } else if (BadgeIdName.equals("ultaExclusive_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_ulta_exclusive);
        } else if (BadgeIdName.equals("ultaPick_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_ulta_pick);
        } else if (BadgeIdName.equals("fanFavorite_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_fan_fave);
        } else if (BadgeIdName.equals("inStoreOnly_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_instore);
        } else if (BadgeIdName.equals("onlineOnly_badge")) {
            imgBadgeImage.setImageResource(R.drawable.online_badge);
        } else if (BadgeIdName.equals("comingSoon_badge")) {
            imgBadgeImage.setImageResource(R.drawable.badge_coming_soon);
        }

    }

    public void setPDPDefaultButtons() {
        btnAddToBasket.setEnabled(true);
        btnAddToBasket.setText(getResources().getString(
                R.string.add_to_basket_btn));

        if (Build.VERSION.SDK_INT >= 21) {
            btnAddToBasket.setBackground(getResources().getDrawable(
                    R.drawable.primary_round));
        } else {
            btnAddToBasket
                    .setBackgroundResource(R.drawable.button_rectangular_border);
        }
        btnAddToBasket.setTextColor(getResources().getColor(R.color.melon));
    }

    public void parseStreamsAll() {

        try {
            UltaDataCache.getDataCacheInstance().setShopThisLookPDPHref(null);
			/* UltaDataCache.getDataCacheInstance().setShopThisLookHref(null); */
            JSONObject jsonObj = new JSONObject(UltaDataCache
                    .getDataCacheInstance().getOlapicProdDetailsResponse());

            JSONObject data = jsonObj.getJSONObject("data");

            if (null != data.getJSONObject("_embedded")) {

                JSONObject _embedded = data.getJSONObject("_embedded");

                JSONArray mediaArray = null;

                if (null != _embedded.getJSONArray("media")) {
                    mediaArray = _embedded.getJSONArray("media");

                    for (int i = 0; i < mediaArray.length(); i++) {

                        JSONObject mediaObject = mediaArray.getJSONObject(i);

                        if (null != mediaObject.getJSONObject("_embedded")) {
                            JSONObject inner_embedded = mediaObject
                                    .getJSONObject("_embedded");

                            if (null != inner_embedded
                                    .getJSONObject("streams:all")) {
                                JSONObject streamsAllObject = inner_embedded
                                        .getJSONObject("streams:all");

                                JSONObject _linkObject = streamsAllObject
                                        .getJSONObject("_links");

                                JSONObject selfObject = _linkObject
                                        .getJSONObject("self");

                                String href = selfObject.getString("href");
                                UltaDataCache.getDataCacheInstance()
                                        .setShopThisLookPDPHref(href);
                            }

                        }

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
