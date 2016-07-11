package com.ulta.core.activity.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.CustomGallery;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.BrandsActivity;
import com.ulta.core.activity.product.BuyMoreSaveMoreLandingActivity;
import com.ulta.core.activity.product.ExtendedActivity;
import com.ulta.core.activity.product.GWPLandingActivity;
import com.ulta.core.activity.product.PromotionsListActivity;
import com.ulta.core.activity.product.SpecialOffersActivity;
import com.ulta.core.activity.product.UltaProductDetailsActivity;
import com.ulta.core.activity.product.UltaProductListActivity;
import com.ulta.core.bean.product.CategoriesBean;
import com.ulta.core.bean.product.CategoryBean;
import com.ulta.core.bean.product.ShopPageSlideShowBean;
import com.ulta.core.bean.product.SlideShowBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopListActivity extends UltaBaseActivity implements
		OnSessionTimeOut {

	private ListView mShopListView;

	/** The category beans list. */
	List<CategoryBean> categoryBeansList;

	/** The categories bean. */
	private CategoriesBean categoriesBean;
	private List<ShopPageSlideShowBean> responseBeanList;
	private CustomGallery gallery;
	private int size = 1;
	// private LinearLayout bubblesLayout;
	private LinearLayout mShopProgressLayout;
	public boolean bubble = true;
	public boolean timer = true;
	private Handler handler = new Handler();
	private int PicPosition;
	private int PrevPicPosition = 0;
	ImageView mDefaultImageView;
	private String parameter;
	private String altText;
	private SlideShowBean mSlideshowBean;
	private SharedPreferences refreshTimeOutSharedPreferences;
	private Editor refreshTimeOutEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivity(ShopListActivity.this);
		setContentView(R.layout.activity_shop);

		setTitle(getResources().getString(R.string.shop_title));
		// mOffersImageView = (ImageView) findViewById(R.id.shop_offers_image);
		mShopListView = (ListView) findViewById(R.id.shop_category_list);
		mShopProgressLayout = (LinearLayout) findViewById(R.id.shoploadingDialog);
		mShopProgressLayout.setVisibility(View.VISIBLE);
		refreshTimeOutSharedPreferences = getSharedPreferences(
				WebserviceConstants.HOME_BANNER_REFRESH_SHAREDPREF,
				MODE_PRIVATE);
		refreshTimeOutEditor = refreshTimeOutSharedPreferences.edit();
		invokeRootCategoryDetails();
		trackAppState(this, WebserviceConstants.SHOP);
		// bubblesLayout = (LinearLayout) findViewById(R.id.bubbles_layout);
		populateCustomGallery();

		// setBanner();
		mShopListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int selectedPosition, long arg3) {

				if (selectedPosition != 0) {
					navigateToParticularCategory(selectedPosition - 1);
				}

			}
		});

		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras()
					.getSerializable("slideShowBean")) {
				mSlideshowBean = (SlideShowBean) getIntent().getExtras()
						.getSerializable("slideShowBean");
				setShopPageBannerAdapter(mSlideshowBean);
			} else {
				invokeShopPageSlideShow();
			}
		} else {
			invokeShopPageSlideShow();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (refreshTimeOutSharedPreferences.getBoolean(
				WebserviceConstants.IS_REFRESH_TIME_EXPIRED_SHOP_PAGE, false)) {
			invokeShopPageSlideShow();
			refreshTimeOutEditor.putBoolean(
					WebserviceConstants.IS_REFRESH_TIME_EXPIRED_SHOP_PAGE,
					false);
			refreshTimeOutEditor.commit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void populateCustomGallery() {

		View headerView = (View) getLayoutInflater().inflate(
				R.layout.shop_gallery, null);

		gallery = (CustomGallery) headerView.findViewById(R.id.gallery1);
		// gallery.setAnimationDuration(10000);
		mDefaultImageView = (ImageView) headerView
				.findViewById(R.id.defaultImage);

		mShopListView.addHeaderView(headerView);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
						String serviceToFire;
				//check if the service name is null or empty
				if (null != responseBeanList.get(arg2).getServiceName()&&responseBeanList.get(arg2).getServiceName().trim().length()>0) {
					serviceToFire = responseBeanList.get(arg2).getServiceName();
				} else {
					serviceToFire = "none";
				}

				if (serviceToFire.equalsIgnoreCase("Product Details")) {
					// Toast.makeText(HomeActivity.this,"Firing Product Details web service",
					// 2000).show();
					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					Intent intentForUltaProductDetailsActivity = new Intent(
							ShopListActivity.this,
							UltaProductDetailsActivity.class);
					intentForUltaProductDetailsActivity.putExtra("id",
							parameter);
					// Date:7thMay,2013.Changed to display altText.(Sayak)
					intentForUltaProductDetailsActivity.putExtra("altText",
							responseBeanList.get(arg2).getAltText());
					intentForUltaProductDetailsActivity.setAction("fromHome");
					startActivity(intentForUltaProductDetailsActivity);
				} else if (serviceToFire.equalsIgnoreCase("Promotions")) {
					// Toast.makeText(HomeActivity.this,"Firing Promotions web service",
					// 2000).show();
					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					if(parameter.equalsIgnoreCase("B"))
					{
						Intent buyMoreSaveMoreIntent = new Intent(ShopListActivity.this,
								BuyMoreSaveMoreLandingActivity.class);
						startActivity(buyMoreSaveMoreIntent);
					}
					else if(parameter.equalsIgnoreCase("G"))
					{
						Intent gwpIntent = new Intent(ShopListActivity.this,
								GWPLandingActivity.class);
						startActivity(gwpIntent);
					}
					else
					{
						Intent intentForPromotionsListActivity = new Intent(
								ShopListActivity.this, PromotionsListActivity.class);
						intentForPromotionsListActivity.putExtra("flag", parameter);
						// Date:7thMay,2013.Changed to display altText.(Sayak)
						intentForPromotionsListActivity.putExtra("altText",
								responseBeanList.get(arg2).getAltText());
						intentForPromotionsListActivity.setAction("fromHome");
						startActivity(intentForPromotionsListActivity);
						// invokePromotions(parameter);
					}
						
					
				
				} else if (serviceToFire
						.equalsIgnoreCase("Product Listing By Promo Id")) {
					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					Intent intentToSearchActivity = new Intent(
							ShopListActivity.this,
							UltaProductListActivity.class);
					intentToSearchActivity.setAction("fromPromotion");
					intentToSearchActivity.putExtra("promotionId", parameter);
					// Date:7thMay,2013.Changed to display altText.(Sayak)
					intentToSearchActivity.putExtra("altText", responseBeanList
							.get(arg2).getAltText());
					startActivity(intentToSearchActivity);
				} else if (serviceToFire
						.equalsIgnoreCase("Product Listing By Category Id")) {
					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					altText = responseBeanList.get(arg2).getAltText();
					Intent intent = new Intent(getApplicationContext(),
							UltaProductListActivity.class);
					intent.putExtra("catagoryIdFromRoot", parameter);
					intent.putExtra("catNam", altText);
					intent.putExtra("From", "ExtendedListActivity");
					startActivity(intent);

				} else if (serviceToFire.equalsIgnoreCase("Slides")) {
					Logger.Log("Slides");
				} else if (serviceToFire.equalsIgnoreCase("Static Content")) {
					Logger.Log("Static banner");
				} 
				else if (serviceToFire.equalsIgnoreCase("none")) {
					Logger.Log("Static banner");
				} 
				else if (serviceToFire
						.equalsIgnoreCase("Product Listing By Brand Name")) {
					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					String brandId = responseBeanList.get(arg2).getBrandId();
					Intent intentToSearchActivity = new Intent(
							ShopListActivity.this,
							UltaProductListActivity.class);
					intentToSearchActivity.setAction("fromHomeByBrand");
					intentToSearchActivity.putExtra("search", parameter);
					if (null != brandId && !brandId.equalsIgnoreCase("NA")) {
						intentToSearchActivity.putExtra("brandId", brandId);
					}
					// Date:7thMay,2013.Changed to display altText.(Sayak)
					intentToSearchActivity.putExtra("altText", responseBeanList
							.get(arg2).getAltText());
					startActivity(intentToSearchActivity);
				} else if (serviceToFire
						.equalsIgnoreCase("Product listing Spl code")) {

					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					Intent intentToSearchActivity = new Intent(
							ShopListActivity.this,
							UltaProductListActivity.class);
					intentToSearchActivity.setAction("fromHomeBySplCode");
					intentToSearchActivity.putExtra("search", parameter);
					// Date:7thMay,2013.Changed to display altText.(Sayak)
					intentToSearchActivity.putExtra("altText", responseBeanList
							.get(arg2).getAltText());
					startActivity(intentToSearchActivity);
				} else if (serviceToFire.equalsIgnoreCase(getResources()
						.getString(R.string.checkIfMobileWebView))) {
					parameter = responseBeanList.get(arg2)
							.getServiceParameters();
					Intent intentToWebViewActivity = new Intent(
							ShopListActivity.this, WebViewActivity.class);
					intentToWebViewActivity.setAction("fromHomeBySplCode");
					intentToWebViewActivity.putExtra("url", parameter);
					intentToWebViewActivity.putExtra("altText",
							responseBeanList.get(arg2).getAltText());
					intentToWebViewActivity.putExtra("navigateToWebView",
							WebserviceConstants.FROM_BLACK_FRIDAY);
					if(null!=responseBeanList.get(arg2).getTitle()) {
						intentToWebViewActivity.putExtra("title", responseBeanList.get(arg2).getTitle());
					}
					startActivity(intentToWebViewActivity);
				} else {
					Toast.makeText(ShopListActivity.this, "No Products Found",
							Toast.LENGTH_LONG).show();
				}
			}

		});
	}

	/**
	 * Invoke root category details.
	 */
	private void invokeRootCategoryDetails() {

		InvokerParams<CategoriesBean> invokerParams = new InvokerParams<CategoriesBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.ROOTCATEGORYDETAILS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(populateRetrieveRootCategoriesDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(CategoriesBean.class);
		RetrieveRootCategoriesDetailsHandler retrieveRootCategoriesDetailsHandler = new RetrieveRootCategoriesDetailsHandler();
		invokerParams.setUltaHandler(retrieveRootCategoriesDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);
			mShopProgressLayout.setVisibility(View.GONE);
		}

	}

	/**
	 * Populate retrieve root categories details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateRetrieveRootCategoriesDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	/**
	 * The Class RetrieveRootCategoriesDetailsHandler.
	 */
	public class RetrieveRootCategoriesDetailsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveRootCategoriesDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			// homePageAction.reportEvent("Fetching categories ended here");
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					askRelogin(ShopListActivity.this);
				} else {
					mShopProgressLayout.setVisibility(View.GONE);
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								ShopListActivity.this);
						setError(ShopListActivity.this, getErrorMessage());

					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				mShopProgressLayout.setVisibility(View.GONE);
				categoriesBean = (CategoriesBean) getResponseBean();
				List<CategoryBean> dummycat = new ArrayList<CategoryBean>();
				if (null != categoriesBean) {

					if (null != categoriesBean.getErrorInfos()) {
						setError(ShopListActivity.this, categoriesBean
								.getErrorInfos().get(0));
					}

					Logger.Log("<HomeActivity>" + "BeanPopulated");
					Logger.Log("<categoriesBean>" + categoriesBean);
					// 3.3 release Adding shop by brand category at the end of
					// the root categories
					CategoryBean brandCategoryBean = new CategoryBean();
					brandCategoryBean.setDisplayName("ShopByBrand");
					dummycat.add(brandCategoryBean);

					CategoryBean cat = new CategoryBean();
					for (int i = 0; i < categoriesBean.getCategories().size(); i++) {
						cat = categoriesBean.getCategories().get(i);
						dummycat.add(cat);
					}

					// 3.3 release Adding onSale category at the end of the root
					// categories
					CategoryBean onSaleCategoryBean = new CategoryBean();
					onSaleCategoryBean.setDisplayName("On Sale");
					dummycat.add(onSaleCategoryBean);

					categoryBeansList = dummycat;
					/*double version = 0.00;
					if (null != categoriesBean.getVersionInfo()
							&& null != categoriesBean.getVersionInfo()
									.getAndroidVersion()) {
						version = Double.parseDouble(categoriesBean
								.getVersionInfo().getAndroidVersion());
					}*/

					mShopProgressLayout.setVisibility(View.GONE);
					ShowShopListAdapter showShopListAdapter = new ShowShopListAdapter();
					mShopListView.setAdapter(showShopListAdapter);
					// displayPager();
				}
			}
		}
	}

	public class ShowShopListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return categoryBeansList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View shopListView, ViewGroup parent) {

			final ViewHolder holder;

			if (shopListView == null) { // If the View is not cached
				Context context = getApplicationContext();
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				shopListView = inflater.inflate(R.layout.shop_list, parent,
						false);
				holder.shopbyCategoryTextView = (TextView) shopListView
						.findViewById(R.id.shop_category_name);
				shopListView.setTag(holder);
			}

			else {
				holder = (ViewHolder) shopListView.getTag();
			}

			if (categoryBeansList.get(position).getDisplayName()
					.equalsIgnoreCase("ShopByBrand")) {
				holder.shopbyCategoryTextView.setText(getResources().getString(
						R.string.atozBrands));
			} else {
				holder.shopbyCategoryTextView.setText(categoryBeansList.get(
						position).getDisplayName());
			}

			return shopListView;
		}
	}

	public class ViewHolder {

		TextView shopbyCategoryTextView;
	}

	public void navigateToParticularCategory(int selectedPos) {
		// if(selectedPos!=0)
		// {
		// selectedPos=selectedPos-1;
		CategoryBean categoryBean = categoryBeansList.get(selectedPos);
		if (selectedPos == 0) {
			Intent intent = new Intent(ShopListActivity.this,
					BrandsActivity.class);
			startActivity(intent);
		}

		else if (selectedPos == categoryBeansList.size() - 1) {
			Intent intent = new Intent(ShopListActivity.this,
					SpecialOffersActivity.class);
//			intent.putExtra("id", "onSaleFormHomePage");
//			intent.putExtra("altText", "On Sale");
			startActivity(intent);
		} else {

			Intent intent = new Intent(ShopListActivity.this,
					ExtendedActivity.class);
			intent.putExtra("id", categoryBean.getId().toString());
			intent.putExtra("catname", categoryBean.getDisplayName().toString());
			UltaDataCache.getDataCacheInstance().setRootCategory(
					categoryBean.getDisplayName());
			startActivity(intent);
		}
		// }

	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

		if (isSuccess) {
			invokeRootCategoryDetails();
		} else {
			mShopProgressLayout.setVisibility(View.GONE);
		}
	}

	protected void invokeShopPageSlideShow() {

		InvokerParams<SlideShowBean> invokerParams = new InvokerParams<SlideShowBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.HOMEPAFEINFO_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateSlideShowParameters());
		invokerParams.setUltaBeanClazz(SlideShowBean.class);
		SlideShowHandler slideShowHandler = new SlideShowHandler();
		invokerParams.setUltaHandler(slideShowHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}

	}

	private Map<String, String> populateSlideShowParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		// urlParams.put("slideShowId","1800001");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		// urlParams.put("pageName", "");
		// urlParams.put("slotName", "");
		return urlParams;
	}

	public class SlideShowHandler extends UltaHandler {
		public void handleMessage(Message msg) {
			Logger.Log("<PurchaseHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			// homePageAction.reportEvent("Fetching slides ended");
			if (null != getErrorMessage()) {

				try {
					// notifyUser(Utility.formatDisplayError(getErrorMessage()),
					// HomeActivity.this);
					setError(ShopListActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				/*
				 * Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
				 * + (getResponseBean()));
				 */
				SlideShowBean ultaBean = (SlideShowBean) getResponseBean();
				// Toast.makeText(ShopListActivity.this, "Banner refreshed",
				// Toast.LENGTH_LONG).show();
				List<String> errors = ultaBean.getErrorInfos();
				if (null != errors && !(errors.isEmpty()))
					try {
						notifyUser(errors.get(0), ShopListActivity.this);
						setError(ShopListActivity.this, errors.get(0));
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				else {
					setShopPageBannerAdapter(ultaBean);
				}

			}
		}
	}

	public void setShopPageBannerAdapter(SlideShowBean slideShowBean) {
		if (null != slideShowBean) {
			Logger.Log("<HomeActivity>" + "BeanPopulated");
			responseBeanList = slideShowBean.getShopPageSlideShow();
			gallery.setVisibility(View.VISIBLE);
			// bubblesLayout.setVisibility(View.GONE);
			// if(null != responseBeanList){
			if (null != responseBeanList && !responseBeanList.isEmpty()) {
				size = responseBeanList.size();

				gallery.setAdapter(new ImageAdapter(ShopListActivity.this));

				for (int j = 0; j < size; j++) {
					ImageView imageBubble = new ImageView(ShopListActivity.this);
					// imageBubble.setImageResource(R.drawable.dot_unselected);
					imageBubble.setImageDrawable(getResources().getDrawable(
							R.drawable.dot_unselected));
					// imageBubble.setBackgroundResource(R.drawable.dot_unselected);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.gravity = Gravity.BOTTOM;
					imageBubble.setPadding(0, 0, 15, 0);
					imageBubble.setLayoutParams(params);
					// bubblesLayout.addView(imageBubble);
				}
				if (size == 0) {
					gallery.setVisibility(View.GONE);
					mDefaultImageView.setVisibility(View.VISIBLE);
				}
				if (size == 1) {
					bubble = false;
				} else {
					bubble = true;
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, 7000);
				}

			} else {
				gallery.setVisibility(View.GONE);
				mDefaultImageView.setVisibility(View.VISIBLE);
				// bubblesLayout.setVisibility(View.GONE);

			}
		}
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			myslideshow();
			handler.postDelayed(runnable, 7000);
		}
	};

	private void myslideshow() {
		PicPosition = gallery.getSelectedItemPosition();

		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.right_to_left);
		if (PicPosition == 0) {
			PicPosition = gallery.getSelectedItemPosition() + 1;
			PrevPicPosition = gallery.getSelectedItemPosition();
			gallery.startAnimation(animation);
			gallery.setSelection(PicPosition);

		} else if (PicPosition > PrevPicPosition) {
			PicPosition = gallery.getSelectedItemPosition() + 1;
			PrevPicPosition = gallery.getSelectedItemPosition();
			gallery.startAnimation(animation);
			if (PrevPicPosition == size - 1) {
				PrevPicPosition = gallery.getSelectedItemPosition();
				PicPosition = gallery.getSelectedItemPosition() - 1;
				gallery.setSelection(PicPosition);
			} else {
				gallery.setSelection(PicPosition);
			}
		} else {
			PicPosition = gallery.getSelectedItemPosition() - 1;
			PrevPicPosition = gallery.getSelectedItemPosition() - 1;
			gallery.startAnimation(animation);
			gallery.setSelection(PicPosition);
		}

	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		// private ImageView[] mImages;
		public ImageAdapter(Context c) {
			mContext = c;

			// mImages = new ImageView[size];
			TypedArray a = c.obtainStyledAttributes(R.styleable.BezelImageView);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.AppTheme_actionbarCompatItemHomeStyle, 0);
			a.recycle();
		}

		@Override
		public int getCount() {
			return responseBeanList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView i = new ImageView(mContext);

			if (null != responseBeanList && responseBeanList.size() != 0) {

				if (null != responseBeanList.get(position).getPath()) {
					String urlForAndroid = responseBeanList.get(position)
							.getPath();
					new AQuery(i).image(urlForAndroid, true, true, 0,
							R.drawable.home_header, null, AQuery.FADE_IN);
				}

				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = metrics.widthPixels;

				if (bubble) {
					i.setLayoutParams(new CustomGallery.LayoutParams(width,
							CustomGallery.LayoutParams.MATCH_PARENT));
				} else {
					i.setLayoutParams(new CustomGallery.LayoutParams(
							CustomGallery.LayoutParams.MATCH_PARENT,
							CustomGallery.LayoutParams.MATCH_PARENT));
				}
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				//i.setBackgroundResource(mGalleryItemBackground);

			} else {
				i.setImageResource(R.drawable.home_header);
				i.setLayoutParams(new CustomGallery.LayoutParams(
						CustomGallery.LayoutParams.MATCH_PARENT,
						CustomGallery.LayoutParams.MATCH_PARENT));
			}

			return i;
		}

	}
}
