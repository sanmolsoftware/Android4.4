/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.UltaProductDetailsActivity;
import com.ulta.core.bean.olapic.OlapicImageDetailStreamBean;
import com.ulta.core.bean.olapic.OlapicImageDetailsBaseImageBean;
import com.ulta.core.bean.olapic.OlapicImageDetailsBean;
import com.ulta.core.bean.olapic.OlapicImageDetailsDataBean;
import com.ulta.core.bean.olapic.OlapicImageDetailsEmbeddedBean;
import com.ulta.core.bean.olapic.OlapicImageDetailsImagesBean;
import com.ulta.core.bean.olapic.OlapicImageDetailsInnerEmbeddedBean;
import com.ulta.core.bean.product.BrandDetailsBean;
import com.ulta.core.bean.product.ProductBean;
import com.ulta.core.bean.product.ProductHeaderBean;
import com.ulta.core.bean.product.ProductSkuBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * The Class OlapicImageDetailsActivity .
 *
 */

public class OlapicImageDetailsActivity extends UltaBaseActivity implements
		OnClickListener {

	private ImageView avatarImageView;

	private TextView nicknameTextView;

	private ImageView olapicDetailImageView;

	private TextView captionTextView;

	private ImageView shopThisLookImageView;

	private LinearLayout shopThisLookLayout;

	private LinearLayout olapicDialog;

	private TextView shopThisLookHeadingTxtView;

	private int position;

	private OlapicImageDetailsBean olapicImageDetailsBean;

	private List<OlapicImageDetailStreamBean> olapicImageDetailStreamBean;

	private OlapicImageDetailsEmbeddedBean olapicImageDetailsEmbeddedBean;

	private OlapicImageDetailsImagesBean olapicImageDetailsImagesBean;

	private OlapicImageDetailsDataBean olapicImageDetailsDataBean;

	private OlapicImageDetailsInnerEmbeddedBean olapicImageDetailsInnerEmbeddedBean;

	private OlapicImageDetailsBaseImageBean olapicImageDetailsBaseImageBean;

	private String productId;
	private static String productIds[], shopThisLookImage[];

	private String shopThisLookLink;

	private ImageView shareIcon;

	private String imageLink;

	private String nickName;

	private String avatar_url;

	private String captionSet;
	private LinearLayout shopThisLookLinearLayout;
	private ProductHeaderBean productHeaderBean;
	private boolean fromPDP = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.olapic_image_details);
		if (getIntent().getExtras() != null) {
			position = getIntent().getExtras().getInt("index");
			imageLink = getIntent().getExtras().getString("imageLink");
			nickName = getIntent().getExtras().getString("nickName");
			avatar_url = getIntent().getExtras().getString("avatar_url");
			captionSet = getIntent().getExtras().getString("caption");
			fromPDP = getIntent().getExtras().getBoolean("fromPDP");
		}

		titleBar = (TitleBar) findViewById(R.id.titlebar);
		
		setTitle("Shop The Look");
		
		avatarImageView = (ImageView) findViewById(R.id.avatar_image);
		nicknameTextView = (TextView) findViewById(R.id.olapic_details_nick_name);
		olapicDetailImageView = (ImageView) findViewById(R.id.olapic_details_image);
		captionTextView = (TextView) findViewById(R.id.caption_textView);
		shopThisLookImageView = (ImageView) findViewById(R.id.shop_this_look_image);
		shopThisLookLayout = (LinearLayout) findViewById(R.id.shop_this_look_layout);
		shareIcon = (ImageView) findViewById(R.id.shareIcon);
		olapicDialog = (LinearLayout) findViewById(R.id.olapicDialog);
		shopThisLookHeadingTxtView = (TextView) findViewById(R.id.shop_this_look_heading);
		shopThisLookLinearLayout = (LinearLayout) findViewById(R.id.shopThisLookLayout);
		/* parseStreamsAll(); */
		populatedImagesAndDetails();

		shareIcon.setOnClickListener(this);

	}

	@Override
	protected void onPause() {
		super.onPause();

		UltaDataCache.getDataCacheInstance().setShopThisLookHref(null);
	}

	public void populatedImagesAndDetails() {

		super.onResume();
		try {

			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int screenWidth = metrics.widthPixels;

			// - padding;
			olapicDetailImageView.getLayoutParams().height = screenWidth;
			new AQuery(olapicDetailImageView).image(imageLink, true, true, 0,
					R.drawable.dummy_product, null, AQuery.FADE_IN);

			new AQuery(avatarImageView).image(avatar_url, true, true, 0,
					R.drawable.dummy_product, null, AQuery.FADE_IN);
			nicknameTextView.setText(nickName);
			nicknameTextView.setPaintFlags(nicknameTextView.getPaintFlags()
					| Paint.UNDERLINE_TEXT_FLAG);
			captionTextView.setText(captionSet);
			olapicDialog.setVisibility(View.VISIBLE);
			invokeShopThisLookImage();
		} catch (Exception e) {
			olapicDialog.setVisibility(View.GONE);
		}

	}



	public void invokeShopThisLookImage() {

		InvokerParams<OlapicImageDetailsBean> invokerParams = new InvokerParams<OlapicImageDetailsBean>();
		UltaDataCache.getDataCacheInstance().setOlapic(true);

		if (fromPDP) {
			invokerParams.setServiceToInvoke(UltaDataCache
					.getDataCacheInstance().getShopThisLookPDPHref()
					.get(position)
					+ "?");
		} else {
			invokerParams.setServiceToInvoke(UltaDataCache
					.getDataCacheInstance().getShopThisLookHref().get(position)
					+ "?");
		}
		invokerParams.setHttpMethod(HttpMethod.GET_OLAPIC);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(fnPopulateOlapicImagesHandlerParameters(
				WebserviceConstants.OLAPIC_VERSION,
				WebserviceConstants.OLAPIC_AUTH_TOKEN));
		invokerParams.setUltaBeanClazz(OlapicImageDetailsBean.class);
		OlapicImageDetailsHandler olapicImagesHandler = new OlapicImageDetailsHandler();
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.SHOP_THIS_LOOK);
		invokerParams.setUltaHandler(olapicImagesHandler);
		invokerParams.setCookieHandlingSkip(true);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<OlapicImageDetailsActivity><invokeShopThisLookImage()><UltaException>>"
					+ ultaException);
			shopThisLookLayout.setVisibility(View.GONE);
			olapicDialog.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @param id
	 *            the id
	 * @return the map
	 */
	private Map<String, String> fnPopulateOlapicImagesHandlerParameters(
			String id, String authToken) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("&auth_token", authToken);
		urlParams.put("version", id);
		return urlParams;
	}

	public class OlapicImageDetailsHandler extends UltaHandler {

		public void handleMessage(Message msg) {

			if (null != getErrorMessage()) {
				try {
					Logger.Log("ERROR");

					olapicDialog.setVisibility(View.GONE);
					shopThisLookLayout.setVisibility(View.GONE);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
					olapicDialog.setVisibility(View.GONE);
				}
			} else {

				Logger.Log("<OlapicImageDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));

				olapicImageDetailsBean = (OlapicImageDetailsBean) getResponseBean();

				if (null != olapicImageDetailsBean) {
					olapicImageDetailsDataBean = olapicImageDetailsBean
							.getData();

					if (null != olapicImageDetailsDataBean) {

						olapicImageDetailsEmbeddedBean = olapicImageDetailsDataBean
								.get_embedded();

						if (null != olapicImageDetailsEmbeddedBean) {

							olapicImageDetailStreamBean = olapicImageDetailsEmbeddedBean
									.getStream();
						}

						productIds = new String[olapicImageDetailStreamBean
								.size()];
						shopThisLookImage = new String[olapicImageDetailStreamBean
								.size()];
						for (int i = 0; i < olapicImageDetailStreamBean.size(); i++) {
							olapicImageDetailsInnerEmbeddedBean = olapicImageDetailStreamBean
									.get(i).get_embedded();

							productId = olapicImageDetailStreamBean.get(i)
									.getTag_based_key();

							productIds[i] = olapicImageDetailStreamBean.get(i)
									.getTag_based_key();

							if (null != olapicImageDetailsInnerEmbeddedBean) {

								olapicImageDetailsBaseImageBean = olapicImageDetailsInnerEmbeddedBean
										.getBase_image();

								if (null != olapicImageDetailsBaseImageBean) {
									olapicImageDetailsImagesBean = olapicImageDetailsBaseImageBean
											.getImages();

									if (null != olapicImageDetailsImagesBean) {
										shopThisLookLink = olapicImageDetailsImagesBean
												.getMobile();
										shopThisLookImage[i] = shopThisLookLink;

									}

								}
							}

						}
						populateShopThisLookDetails();
					}

				}

			}

		}
	}



	@Override
	public void onClick(View v) {

		if (v == shareIcon) {
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/plain");
			sendIntent.putExtra(Intent.EXTRA_TEXT, imageLink
					+ " Look what I found in Ulta Beauty");
			startActivity(Intent.createChooser(sendIntent, "www.ulta.com"));

		}

	}

	private void populateShopThisLookDetails() {
		shopThisLookLinearLayout.removeAllViews();
		for (int i = 0; i < productIds.length; i++) {
			if (null != productIds[i]) {
				fnInvokeRetrieveProductDetails(productIds[i]);

			}
		}
	}

	/**
	 * Method to populate invoker params and fire the web service.
	 * 
	 * @param id
	 *            the id
	 */
	private void fnInvokeRetrieveProductDetails(String id) {
		InvokerParams<ProductBean> invokerParams = new InvokerParams<ProductBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.PRODUCTDETAILS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(fnPopulateRetrieveProductDetailsHandlerParameters(id));
		invokerParams
				.setAkamaiURLParameters(fnPopulateAkamaiRetrieveProductDetailsParameters(id));


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
	 * @param id
	 *            the id
	 * @return the map
	 */
	private Map<String, String> fnPopulateRetrieveProductDetailsHandlerParameters(
			String id) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("arg1", id);
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
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				try {
					Logger.Log("ERROR");

					if (!getErrorMessage().startsWith("Service Temporarily Unavailable")) {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								OlapicImageDetailsActivity.this);
					}

					setError(OlapicImageDetailsActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				shopThisLookHeadingTxtView.setVisibility(View.VISIBLE);
				olapicDialog.setVisibility(View.GONE);
				Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				ProductBean productBean = (ProductBean) getResponseBean();

				if (null != productBean) {
					if (null != productBean.getErrorInfos()) {
						setError(OlapicImageDetailsActivity.this, productBean
								.getErrorInfos().get(0));
					}
					View view = getLayoutInflater().inflate(
							R.layout.olapic_shop_this_look_view, null);

					TextView tvItemName = (TextView) view
							.findViewById(R.id.tvItemName);
					TextView tvBrandName = (TextView) view
							.findViewById(R.id.tvBrandName);
					ImageView imgItemImage = (ImageView) view
							.findViewById(R.id.imgItemImage);
					TextView tvSalePrice = (TextView) view
							.findViewById(R.id.tvSalePrice);
					ProgressBar product_list_title_Progress_Bar = (ProgressBar) view
							.findViewById(R.id.product_list_title_Progress_Bar);

					List<ProductSkuBean> listOfProductSkuBeans = productBean
							.getSkuDetails();
					if (null != listOfProductSkuBeans) {
						ProductSkuBean productSkuBean = null;
						for (int i = 0; i < listOfProductSkuBeans.size(); i++) {
							if (listOfProductSkuBeans.get(i).isDefaultSku()) {
								productSkuBean = listOfProductSkuBeans.get(i);
							}

						}

						if (null != productSkuBean) {
							new AQuery(imgItemImage).image(
									productSkuBean.getSmallImageUrl(), true,
									false, 200, R.drawable.dummy_product, null,
									AQuery.FADE_IN);
							if (0 != productSkuBean.getSalePrice()) {
								tvSalePrice.setText("$"
										+ String.format("%.2f", Double
												.valueOf(productSkuBean
														.getSalePrice())));
							} else {
								tvSalePrice.setText("$"
										+ String.format("%.2f", Double
												.valueOf(productSkuBean
														.getListPrice())));
							}
							product_list_title_Progress_Bar
									.setVisibility(View.GONE);
						}
					}
					// brand name
					BrandDetailsBean brandDetailsBean = productBean
							.getBrandDetails();
					if (null != brandDetailsBean) {
						tvBrandName.setText(brandDetailsBean.getBrandName());
					}
					ImageView imgRating1 = (ImageView) view
							.findViewById(R.id.imgRating1);
					ImageView imgRating2 = (ImageView) view
							.findViewById(R.id.imgRating2);
					ImageView imgRating3 = (ImageView) view
							.findViewById(R.id.imgRating3);
					ImageView imgRating4 = (ImageView) view
							.findViewById(R.id.imgRating4);
					ImageView imgRating5 = (ImageView) view
							.findViewById(R.id.imgRating5);

					TextView tvRating = (TextView) view
							.findViewById(R.id.tvRating);
					ImageView imgBadgeImage = (ImageView) view
							.findViewById(R.id.BadgeImage);



					// displayname
					productHeaderBean = productBean.getProductHeader();
					if (null != productHeaderBean) {
						tvItemName.setText(productHeaderBean.getDisplayName());
					}
					if (null != productBean.getProductReview()) {


						Logger.Log((float) productBean.getProductReview()
								.getRating());
						float rating = (float) productBean.getProductReview()
								.getRating();
						tvRating.setText("("
								+ productBean.getProductReview().getReviews()
								+ ")");

						if (rating == 0) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);


						} else if (rating == 0.5) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_halfcolored); // make
							// it
							// half
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);
						} else if (rating == 1) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);

						} else if (rating == 1.5) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
							// it
							// half
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);
						} else if (rating == 2) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);

						} else if (rating == 2.5) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
							// it
							// half
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);

						} else if (rating == 3) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_gray);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);

						} else if (rating == 3.5) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
							// it
							// half
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);

						} else if (rating == 4) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_gray);

						} else if (rating == 4.5) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_halfcolored);// make
						} else if (rating == 5) {
							imgRating1
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating2
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating3
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating4
									.setBackgroundResource(R.drawable.icon_star_coloured);
							imgRating5
									.setBackgroundResource(R.drawable.icon_star_coloured);
						}
					}
					if (null != productHeaderBean) {
						for (int i = 0; i < productHeaderBean.getBadgeList()
								.size(); i++) {

							if (null != productHeaderBean.getBadgeList().get(i)
									.getBadgeName()) {
								String BadgeName = productHeaderBean
										.getBadgeList().get(i).getBadgeName();
								imgBadgeImage.setVisibility(View.VISIBLE);
								if (BadgeName.equals("isNew_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_whats_new_big);
								} else if (BadgeName.equals("gwp_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_free_gift_big);
								} else if (BadgeName.equals("onSale_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_sale_big);
								} else if (BadgeName
										.equals("ultaExclusive_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_ulta_exclusive_big);
								} else if (BadgeName.equals("ultaPick_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_ulta_pick_big);
								} else if (BadgeName
										.equals("fanFavorite_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_fan_fave_big);
								} else if (BadgeName
										.equals("inStoreOnly_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_instore_big);
								} else if (BadgeName.equals("onlineOnly_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.online_badge_big);
								} else if (BadgeName.equals("comingSoon_badge")) {
									imgBadgeImage
											.setImageResource(R.drawable.badge_coming_soon_big);
								}

							}
						}
						view.setTag(productHeaderBean.getId());
					}

					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									OlapicImageDetailsActivity.this,
									UltaProductDetailsActivity.class);
							try {

								intent.putExtra("idFromOlapicImageDetails", v
										.getTag().toString());
							} catch (Exception e) {
								e.printStackTrace();
							}

							startActivity(intent);
						}
					});
					shopThisLookLinearLayout.addView(view);

				}
			}
		}
	}

}
