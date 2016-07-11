package com.ulta.core.activity.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.WebViewActivity;
import com.ulta.core.bean.product.TaxonomyBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExtendedActivity extends UltaBaseActivity {
	String id;
	LinearLayout loadingDialog, linearTaxonymy;
	TaxonomyBean taxonomyBean;
	List<String> groupList;
	List<Double> groupListId;
	List<String> childList;
	Map<String, List<String>> categoryCollection;
	ExpandableListView expListView;
	Bundle extrasPositionTile;
	String omnitPageName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extended_activity);
		// OmniInit();
		setActivity(ExtendedActivity.this);
		extrasPositionTile = getIntent().getExtras();
		getView();

		if (null != extrasPositionTile) {
			id = extrasPositionTile.getString("id");
			setTitle(extrasPositionTile.getString("catname"));
			omnitPageName = extrasPositionTile.getString("catname");
			creatingPageName(omnitPageName);
			if (id != null) {
				loadingDialog.setVisibility(View.VISIBLE);
				fnInvokeRetrieveExtendedMenuListHandlerParameters(id);

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UltaDataCache.getDataCacheInstance().getPlpHashMapOfImages().clear();
	}

	private void creatingPageName(String categoryname) {

		trackAppState(this, WebserviceConstants.CATEGORY + categoryname);
	}

	private void getView() {
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		linearTaxonymy = (LinearLayout) findViewById(R.id.LinearLayoutTaxonomy);
		expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
	}

	private void fnInvokeRetrieveExtendedMenuListHandlerParameters(String id) {
		InvokerParams<TaxonomyBean> invokerParams = new InvokerParams<TaxonomyBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.FETCH_TAXONOMY_DETAILS);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(fnPopulateExtendedMenuListHandlerParameters(id));
		invokerParams.setUltaBeanClazz(TaxonomyBean.class);
		ExtendedMenuListHandler extendedMenuListHandler = new ExtendedMenuListHandler();
		invokerParams.setUltaHandler(extendedMenuListHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<ExtendedMenuListFragment><fnInvokeRetrieveExtendedMenuListHandlerParameters()><UltaException>>"
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

	private Map<String, String> fnPopulateExtendedMenuListHandlerParameters(
			String id) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("categoryId", id);
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		return urlParams;
	}

	/*
	 * Handler to handle the response from the web service
	 */
	/**
	 * The Class ExtendedMenuListHandler.
	 */
	public class ExtendedMenuListHandler extends UltaHandler {

		/**
		 * Handle message.
		 *
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<ExtendedMenuListHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {

				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							ExtendedActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}

			} else {
				loadingDialog.setVisibility(View.GONE);
				taxonomyBean = (TaxonomyBean) getResponseBean();

				groupList = new ArrayList<String>();
				groupListId = new ArrayList<Double>();
				for (int i = 0; i < taxonomyBean.getAtgResponse().size(); i++) {
					if (null != taxonomyBean.getAtgResponse().get(i)
							.getTotalNoOfProducts()) {
						if (taxonomyBean.getAtgResponse().get(i).getName()
								.equalsIgnoreCase("Fragrance Finder")) {
							groupList.add(taxonomyBean.getAtgResponse().get(i)
									.getName());
						} else {
							//If section is gift card hide the number of products
							if(taxonomyBean
									.getAtgResponse().get(i)
									.getName().equalsIgnoreCase("Gift Cards"))
							{
								groupList.add(taxonomyBean.getAtgResponse().get(i)
										.getName());


							}
							else {
								groupList.add(taxonomyBean.getAtgResponse().get(i)
										.getName()
										+ "\n"
										+ taxonomyBean.getAtgResponse().get(i)
										.getTotalNoOfProducts()
										+ " Products");
							}
						}
					} else {
						groupList.add(taxonomyBean.getAtgResponse().get(i)
								.getName());
					}
					groupListId.add(taxonomyBean.getAtgResponse().get(i)
							.getCategoryDimensionId());
				}

				categoryCollection = new LinkedHashMap<String, List<String>>();

				for (int i = 0; i < groupList.size(); i++) {
					String CategoryName = groupList.get(i);
					List<String> subCategoryName = new ArrayList<String>();

					for (int j = 0; j < taxonomyBean.getAtgResponse().get(i)
							.getSubCategories().size(); j++) {
						if (null != taxonomyBean.getAtgResponse().get(i)
								.getSubCategories().get(j)
								.getTotalNoOfProducts()) {
							subCategoryName.add(taxonomyBean.getAtgResponse()
									.get(i).getSubCategories().get(j).getName()
									+ "\n\t\t\t"
									+ taxonomyBean.getAtgResponse().get(i)
									.getSubCategories().get(j)
									.getTotalNoOfProducts()
									+ " Products");
						} else
							subCategoryName
									.add(taxonomyBean.getAtgResponse().get(i)
											.getSubCategories().get(j)
											.getName());
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(
								metrics);
						int width = metrics.widthPixels;
						expListView.setIndicatorBounds(width
								- GetPixelFromDips(50), width
								- GetPixelFromDips(10));

					}
					categoryCollection.put(CategoryName, subCategoryName);
				}
				Logger.Log(categoryCollection);
				final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
						ExtendedActivity.this, groupList, categoryCollection);
				expListView.setAdapter(expListAdapter);

				expListView.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
												View v, int groupPosition, int childPosition,
												long id) {
						creatingPageName(omnitPageName
								+ ":"
								+ taxonomyBean.getAtgResponse()
								.get(groupPosition).getName()
								+ ":"
								+ taxonomyBean.getAtgResponse()
								.get(groupPosition).getSubCategories()
								.get(childPosition).getName());
						int intChildId = (int) taxonomyBean.getAtgResponse()
								.get(groupPosition).getSubCategories()
								.get(childPosition).getCategoryDimensionId();
						String childId = intChildId + "";
						Intent intent = new Intent(getApplicationContext(),
								UltaProductListActivity.class);
						intent.putExtra("catagoryIdFromRoot", childId);
						intent.putExtra("catNam",
								taxonomyBean.getAtgResponse()
										.get(groupPosition).getSubCategories()
										.get(childPosition).getName());
						intent.putExtra("From", "ExtendedListActivity");
						startActivity(intent);
						return true;
					}
				});
				expListView.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
												View v, int groupPosition, long id) {
						creatingPageName(omnitPageName
								+ ":"
								+ taxonomyBean.getAtgResponse()
								.get(groupPosition).getName());
						if (taxonomyBean.getAtgResponse().get(groupPosition)
								.getSubCategories() == null
								|| taxonomyBean.getAtgResponse()
								.get(groupPosition).getSubCategories()
								.size() == 0) {
							String name = ""
									+ taxonomyBean.getAtgResponse()
									.get(groupPosition).getName();
							if (name.equalsIgnoreCase("Fragrance Finder")) {
								Intent intentToWebViewActivity = new Intent(ExtendedActivity.this, WebViewActivity.class);
								intentToWebViewActivity.putExtra("url", WebserviceConstants.FRAGRANCE_URL);
								intentToWebViewActivity.putExtra("navigateToWebView", WebserviceConstants.FROM_BLACK_FRIDAY);
								intentToWebViewActivity.putExtra("title", "Fragrance Finder");
								startActivity(intentToWebViewActivity);
							} else {
								int intChildId = (int) taxonomyBean
										.getAtgResponse().get(groupPosition)
										.getCategoryDimensionId();
								//if the section is for gift card
								if(taxonomyBean
										.getAtgResponse().get(groupPosition)
										.getName().equalsIgnoreCase("Gift Cards")||intChildId==102946)
								{
									//Navigate to cash star SDK page
									Intent giftCardIntent = null;
									if(WebserviceConstants.isCashStarSDKEnabled) {
										// App has to leverage cash star sdk to show page
										giftCardIntent = new Intent(ExtendedActivity.this, CashStarHomeUI.class);
										startActivity(giftCardIntent);
									}
									else
									{
										//Navigate to cash star web page
										giftCardIntent = new Intent(ExtendedActivity.this, WebViewActivity.class);
										giftCardIntent.putExtra("navigateToWebView", WebserviceConstants.FROM_GIFT_CARD);
										giftCardIntent.putExtra("title", getResources().getString(R.string.webViewTitle_cashStar));
										giftCardIntent.putExtra("url", UltaDataCache.getDataCacheInstance().getGiftCardUrl());
										startActivity(giftCardIntent);
									}

								}
								else {
									Intent intent = new Intent(
											getApplicationContext(),
											UltaProductListActivity.class);
									String childId = intChildId + "";
									intent.putExtra("catagoryIdFromRoot", childId);
									intent.putExtra("catNam", taxonomyBean
											.getAtgResponse().get(groupPosition)
											.getName());
								/*
								 * Toast.makeText(getBaseContext(),
								 * taxonomyBean.
								 * getAtgResponse().get(groupPosition
								 * ).getCategoryDimensionId()+"",
								 * Toast.LENGTH_SHORT).show();
								 */
									intent.putExtra("From", "ExtendedListActivity");
									startActivity(intent);
								}
							}
							return true;
						} else {
							return false;
						}

					}
				});

			}
		}

		public int GetPixelFromDips(float pixels) {
			// Get the screen's density scale
			final float scale = getResources().getDisplayMetrics().density;
			// Convert the dps to pixels, based on density scale
			return (int) (pixels * scale + 0.5f);
		}
	}
}
