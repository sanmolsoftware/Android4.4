/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.myprofile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.activity.product.UltaProductDetailsActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.bean.favourites.FavoritesComponentBean;
import com.ulta.core.bean.favourites.FavouritesInListBean;
import com.ulta.core.bean.favourites.MobileFavCartBean;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.CartBean;
import com.ulta.core.bean.product.CommerceItemBean;
import com.ulta.core.bean.product.ComponentBean;
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
import com.ulta.core.widgets.FavoritesPopUp;
import com.ulta.core.widgets.FavoritesPopUp.OnOptionsClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.conf.UltaConstants.REMOVING_PROGRESS_TEXT;

/**
 * The Class BeautyListActivity.
 */
public class FavoritesActivity extends UltaBaseActivity implements
		OnSessionTimeOut {

	/** The beauty list items. */
	private ListView beautyListItems;

	private ImageView ivOptions;
	private TextView tvItemNumber;

	/** The custom dialog. */
	private Dialog customDialog;

	/** The options pop up. */
	private FavoritesPopUp optionsPopUp;

	private List<FavouritesInListBean> listofFavouritesInListBean;

	private List<FavouritesInListBean> favouritesInList;

	private ProgressDialog pd, pdremove;

	/** The add to cart bean. */
	private AddToCartBean addToCartBean;

	private String skuId;
	private String productname;

	private LinearLayout emptyFavoriteLayout;
	private LinearLayout mainLayout;

	/** The loading layout. */
	private LinearLayout loadingDialog;

	private Button btnShopping;

	public void refreshPage() {
		loadingDialog.setVisibility(View.VISIBLE);

		mainLayout.setVisibility(View.GONE);
		emptyFavoriteLayout.setVisibility(View.GONE);

		fnInvokeFavorites();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.activity.UltaActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// isOnCreateCalled=true;
		super.onCreate(savedInstanceState);
		setActivity(FavoritesActivity.this);
		setContentView(R.layout.favorites_list_main);
		setTitle("Favorites");
		emptyFavoriteLayout = (LinearLayout) findViewById(R.id.emptyFavoriteLayout);
		mainLayout = (LinearLayout) findViewById(R.id.basketMainLayout);
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		btnShopping = (Button) findViewById(R.id.btnShopping);
		favouritesInList = new ArrayList<FavouritesInListBean>();
		// beautyListId = getIntent().getIntExtra("beautyListId", 0);
		pd = new ProgressDialog(FavoritesActivity.this);
		setProgressDialogLoadingColor(pd);
		pd.setMessage(LOADING_PROGRESS_TEXT);
		pd.setCancelable(false);

		pdremove = new ProgressDialog(FavoritesActivity.this);
		setProgressDialogLoadingColor(pdremove);
		pdremove.setMessage(REMOVING_PROGRESS_TEXT);
		pdremove.setCancelable(false);

		loadingDialog.setVisibility(View.VISIBLE);

		mainLayout.setVisibility(View.GONE);
		emptyFavoriteLayout.setVisibility(View.GONE);

		fnInvokeFavorites();

		btnShopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentForUltaHomeActivity = new Intent(
						FavoritesActivity.this, HomeActivity.class);

				startActivity(intentForUltaHomeActivity);
			}
		});

	}

	/**
	 * Method for setting the view elements.
	 */
	private void setView() {

		beautyListItems = (ListView) findViewById(R.id.beauty_list_items);
		ivOptions = (ImageView) findViewById(R.id.ivCreate);
		beautyListItems.setAdapter(new CustomListAdapter());

		ivOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				optionsPopUp = new FavoritesPopUp(FavoritesActivity.this);

				optionsPopUp.show(v);
				optionsPopUp
						.setOnOptionsClickedListener(new OnOptionsClickedListener() {

							@Override
							public void onOptionsClicked(int click) {
								if (click == 1) {

									optionsPopUp.dismiss();

								} else if (click == 2) {
									optionsPopUp.dismiss();

								} else if (click == 3) {
									optionsPopUp.dismiss();
									customDialog = new Dialog(
											FavoritesActivity.this,
											R.style.CustomDialogTheme);
									customDialog
											.setContentView(R.layout.share_popup);
									customDialog.show();

									LinearLayout llFilterByBrand = (LinearLayout) customDialog
											.findViewById(R.id.llFilterByBrand);
									LinearLayout llFilterByPrice = (LinearLayout) customDialog
											.findViewById(R.id.llFilterByPrice);
									LinearLayout llFilterByCategory = (LinearLayout) customDialog
											.findViewById(R.id.llFilterByCategory);
									Button btnCancel = (Button) customDialog
											.findViewById(R.id.btnCancel);

									btnCancel
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													customDialog.dismiss();
												}
											});
									llFilterByBrand
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													customDialog.dismiss();
												}
											});
									llFilterByPrice
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													customDialog.dismiss();

												}
											});
									llFilterByCategory
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													customDialog.dismiss();
												}
											});
								}
							}
						});
				optionsPopUp.show(v, 0, 0);
			}
		});

	}

	/**
	 * The Class CustomListAdapter.
	 */
	private class CustomListAdapter extends BaseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return favouritesInList.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = View.inflate(FavoritesActivity.this,
					R.layout.favorites_list_content, null);

			ImageView image = (ImageView) convertView
					.findViewById(R.id.beauty_list_image);
			new AQuery(image).image(favouritesInList.get(position)
					.getSmallImageUrl(), true, false, 200,
					R.drawable.dummy_product, null, AQuery.FADE_IN);

			TextView productName = (TextView) convertView
					.findViewById(R.id.beauty_list_txt_product_name);
			productName
					.setText(favouritesInList.get(position).getDisplayName());

			TextView brandName = (TextView) convertView
					.findViewById(R.id.beauty_list_txt_brand_name);
			brandName.setText(favouritesInList.get(position).getBrandName());

			float rating = (float) favouritesInList.get(position).getRating();
			ImageView imgRating1 = (ImageView) convertView
					.findViewById(R.id.imgRating1);
			ImageView imgRating2 = (ImageView) convertView
					.findViewById(R.id.imgRating2);
			ImageView imgRating3 = (ImageView) convertView
					.findViewById(R.id.imgRating3);
			ImageView imgRating4 = (ImageView) convertView
					.findViewById(R.id.imgRating4);
			ImageView imgRating5 = (ImageView) convertView
					.findViewById(R.id.imgRating5);
			TextView itemStatusMsg = (TextView) convertView
					.findViewById(R.id.txtItemStatus);
			if (rating == 0) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 0.5) {
				imgRating1
						.setBackgroundResource(R.drawable.icon_star_halfcolored); // make
																					// it
																					// half
				imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 1) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 1.5) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2
						.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
																					// it
																					// half
				imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 2) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 2.5) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3
						.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
																					// it
																					// half
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 3) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 3.5) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating4
						.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
																					// it
																					// half
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 4) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating4.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
			} else if (rating == 4.5) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating4.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating5
						.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
																					// it
																					// half
			} else if (rating == 5) {
				imgRating1.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating2.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating3.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating4.setBackgroundResource(R.drawable.icon_star_coloured);
				imgRating5.setBackgroundResource(R.drawable.icon_star_coloured);
			}

			TextView promotionalAdBug = (TextView) convertView
					.findViewById(R.id.tvPromotionalAdBugInFav);

			if (null != favouritesInList.get(position).getPromotionId()) {
				if (null != favouritesInList.get(position).getOfferDesc()) {
					if (favouritesInList.get(position).getOfferDesc()
							.equals("Special Free Gift with Purchase")) {
						promotionalAdBug.setVisibility(LinearLayout.VISIBLE);
						promotionalAdBug.setText(favouritesInList.get(position)
								.getOfferDesc());
					}
				}
			} else {
				promotionalAdBug.setVisibility(LinearLayout.GONE);
			}

			TextView reviewNumber = (TextView) convertView
					.findViewById(R.id.tvReviewNumber);
			reviewNumber.setText("("
					+ favouritesInList.get(position).getReviews() + ")");

			TextView tvActualPrice = (TextView) convertView
					.findViewById(R.id.tvActualPrice);
			tvActualPrice.setText("$"
					+ String.format("%.2f", Double.valueOf(favouritesInList
							.get(position).getListPrice())));

			tvItemNumber = (TextView) findViewById(R.id.tvItemNumber);
			tvItemNumber.setText("" + favouritesInList.size() + "");
			Logger.Log("<<<" + favouritesInList.size());

			Button btnBasket = (Button) convertView
					.findViewById(R.id.btnBasket);
			btnBasket.setPadding(20, 10, 20, 10);
			if (favouritesInList.get(position).isInStoreOnly()
					|| favouritesInList.get(position).isProductExpired()
					|| favouritesInList.get(position).getIsGWP() == 1
					|| favouritesInList.get(position).isComingSoon()) {
				btnBasket.setVisibility(View.GONE);

				if (favouritesInList.get(position).isInStoreOnly()) {
					itemStatusMsg.setVisibility(View.VISIBLE);
					itemStatusMsg.setText(getResources().getString(
							R.string.instore_only_msg));
				} else if (favouritesInList.get(position).isProductExpired()) {
					itemStatusMsg.setVisibility(View.VISIBLE);
					itemStatusMsg.setText(getResources().getString(
							R.string.item_not_available_msg));
				}
			}

			Button btnRemove = (Button) convertView
					.findViewById(R.id.btnRemove);
			btnRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pdremove.show();
					fnRemoveFavorites(favouritesInList.get(position)
							.getCatalogRefId());
				}
			});

			btnBasket.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pd.show();
					skuId = favouritesInList.get(position).getCatalogRefId();
					productname = favouritesInList.get(position)
							.getDisplayName();
					fnInvokeAddToCart(favouritesInList.get(position)
							.getProductId(), skuId);
				}
			});

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intentForUltaProductDetailsActivity = new Intent(
							FavoritesActivity.this,
							UltaProductDetailsActivity.class);
					intentForUltaProductDetailsActivity.putExtra(
							"idfromFavorites", favouritesInList.get(position)
									.getProductId());
					intentForUltaProductDetailsActivity.putExtra(
							"skuidfromFavorites", favouritesInList
									.get(position).getCatalogRefId());
					startActivity(intentForUltaProductDetailsActivity);
				}
			});

			return convertView;
		}
	}

	private void fnInvokeFavorites() {

		InvokerParams<MobileFavCartBean> invokerParams = new InvokerParams<MobileFavCartBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.VIEW_FAVORITES_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(fnFavoritesItemParameters());
		invokerParams.setUltaBeanClazz(MobileFavCartBean.class);
		FavoritesHandler favoritesHandler = new FavoritesHandler();
		invokerParams.setUltaHandler(favoritesHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaProductDetailsActivity><fnInvokeFavorites()><UltaException>>"
					+ ultaException);
			pdremove.dismiss();
		}
	}

	/**
	/**
	 * Method to populate the URL parameter map
	 * 
	 * @param id
	 */
	private Map<String, String> fnFavoritesItemParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();

		urlParams.put("atg-rest-depth", "1");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
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
			loadingDialog.setVisibility(View.GONE);
			if (pdremove != null && pdremove.isShowing()) {
				pdremove.dismiss();
			}
			/*
			 * if (pd != null && pd.isShowing()) { pd.dismiss(); }
			 */
			if (null != getErrorMessage()) {
				try {
					Logger.Log("ERROR");
					if (getErrorMessage().startsWith("401")) {
						// mLoginAction.reportError(
						// WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
						// mLoginAction.leaveAction();
						askRelogin(FavoritesActivity.this);
					} else {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								FavoritesActivity.this);
					}
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				MobileFavCartBean favouritesBean = (MobileFavCartBean) getResponseBean();
				Logger.Log("<<<<<<" + favouritesBean);

				if (null != favouritesBean.getMobileFavCart()
						.getFavItemDetails()) {
					listofFavouritesInListBean = favouritesBean
							.getMobileFavCart().getFavItemDetails();
					favouritesInList.addAll(listofFavouritesInListBean);
					Logger.Log("<<<<<<" + favouritesInList.size());
					if (favouritesInList.size() != 0) {
						emptyFavoriteLayout.setVisibility(View.GONE);
						mainLayout.setVisibility(View.VISIBLE);

						Logger.Log("<<<<<<" + listofFavouritesInListBean);

						setView();
					} else {
						mainLayout.setVisibility(View.GONE);
						emptyFavoriteLayout.setVisibility(View.VISIBLE);
					}

				} else {

					mainLayout.setVisibility(View.GONE);
					emptyFavoriteLayout.setVisibility(View.VISIBLE);
				}
			}
		}
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
		public void handleMessage(Message msg) {
			Logger.Log("<RemoveFavoritesHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			loadingDialog.setVisibility(View.GONE);
			FavoritesComponentBean mobileFavCartBean = (FavoritesComponentBean) getResponseBean();
			Logger.Log("<<<<<<" + mobileFavCartBean);
			// List<String> result = favouritesBean.getErrorInfos();

			if (null != mobileFavCartBean) {
				favouritesInList.clear();
				fnInvokeFavorites();
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

			} else {
				pdremove.dismiss();
				mainLayout.setVisibility(View.INVISIBLE);
				emptyFavoriteLayout.setVisibility(View.VISIBLE);
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							FavoritesActivity.this);

				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Fn invoke add to cart.
	 * 
	 * @param productId
	 *            the product id
	 * @param skuId
	 *            the sku id
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
	 * @param productId
	 *            the product id
	 * @param skuId
	 *            the sku id
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
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<AddToCartHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			loadingDialog.setVisibility(View.GONE);
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							FavoritesActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				UltaDataCache.getDataCacheInstance()
						.setAnonymousCheckout(false);
				Logger.Log("<AddToCartHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				addToCartBean = (AddToCartBean) getResponseBean();

				if (null != addToCartBean) {
					boolean isOutOfStock = false;
					if (addToCartBean.getErrorInfos() != null) {
						notifyUser(addToCartBean.getErrorInfos().get(0),
								FavoritesActivity.this);
					} else {
						ComponentBean componentBean = addToCartBean
								.getComponent();
						CartBean cartBean = componentBean.getCart();
						List<CommerceItemBean> listOfCommerceItemBean = cartBean
								.getCommerceItems();
						for (int loop = 0; loop < listOfCommerceItemBean.size(); loop++) {
							CommerceItemBean commerceItemBean = listOfCommerceItemBean
									.get(loop);
							if (commerceItemBean.getCatalogRefId()
									.equals(skuId)) {
								if (commerceItemBean.isOutOfStock()) {
									isOutOfStock = true;
									fnInvokeRemoveItem(commerceItemBean);
									break;
								}
							}
						}
					}
					if (!isOutOfStock) {
						if (addToCartBean.isResult()) {
							// Toast.makeText(UltaProductDetailsActivity.this,
							// productSkuBean.getDisplayName()+" successfully added to cart",
							// Toast.LENGTH_LONG).show();
							setItemCountInBasket(getItemCountInBasket() + 1);

							String messageToBeDisplayed = productname
									+ " is added to Bag";
							final Dialog alert = showAlertDialog(
									FavoritesActivity.this, "Bag",
									messageToBeDisplayed, "OK", "View Bag");

							alert.show();

							mAgreeButton
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											alert.dismiss();
										}
									});

							mDisagreeButton
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											Intent intent = new Intent(
													FavoritesActivity.this,
													ViewItemsInBasketActivity.class);
											startActivity(intent);

										}
									});

							/*
							 * final AlertDialog.Builder alert = new
							 * AlertDialog.Builder( FavoritesActivity.this);
							 * alert.setTitle("Bag");
							 * alert.setMessage(productname +
							 * " is added to Bag");
							 * alert.setNegativeButton("Ok", new
							 * DialogInterface.OnClickListener() {
							 * 
							 * @Override public void onClick( DialogInterface
							 * dialog, int which) {
							 * 
							 * dialog.dismiss(); } });
							 * alert.setPositiveButton("View Bag", new
							 * DialogInterface.OnClickListener() {
							 * 
							 * @Override public void onClick( DialogInterface
							 * dialog, int which) { Intent intent = new Intent(
							 * FavoritesActivity.this,
							 * ViewItemsInBasketActivity.class);
							 * startActivity(intent); } }); //
							 * alert.setMessage("Select Free Samples");
							 * alert.create().show();
							 */

						}
					} else {
						// call the remove item from basket service
						/*
						 * final AlertDialog.Builder alert = new
						 * AlertDialog.Builder( FavoritesActivity.this);
						 * alert.setTitle("Out Of Stock");
						 * alert.setMessage(productname +
						 * " is Out of Stock so can not be added to the Basket"
						 * ); alert.setPositiveButton("Ok", new
						 * DialogInterface.OnClickListener() {
						 * 
						 * @Override public void onClick(DialogInterface dialog,
						 * int which) {
						 * 
						 * dialog.dismiss(); } }); //
						 * alert.setMessage("Select Free Samples");
						 * alert.create().show();
						 */

						String messageToBeDisplayed = productname
								+ " is Out of Stock so can not be added to the Basket";
						final Dialog alert = showAlertDialog(
								FavoritesActivity.this, "Out Of Stock",
								messageToBeDisplayed, "OK", "");
						alert.show();

						mDisagreeButton.setVisibility(View.GONE);
						mAgreeButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								alert.dismiss();
							}
						});

					}
				}
			}
		}
	}

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
		public RemoveItemHandler(CommerceItemBean commerceBeanRemove) {
		}

		/**
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RemoveItemHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			loadingDialog.setVisibility(View.GONE);
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (null != getErrorMessage()) {

				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							FavoritesActivity.this);

				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}

			}
		}
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			fnInvokeFavorites();
		}
	}
}
