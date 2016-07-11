package com.ulta.core.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.activity.product.UltaProductDetailsActivity;
import com.ulta.core.activity.product.UltaProductListActivity;
import com.ulta.core.bean.search.SearchBean;
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

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends UltaBaseActivity {
	private EditText txtSearch;
	private Button btnClearSearch;

	private String sortSearch = "";
	private boolean fromScan = false;
	private String productId;
	private String skuId;

	/** The category id. */
	private String categoryId = "";

	/** The brand id. */
	private String brandId = "";
	private String colorId = "";

	/** The filter price. */
	private String minPrice = "";

	/** The filter price. */
	private String maxPrice = "";

	/** The filter promtoiton id. */
	private String promotionid = "";

	/** The sort param. */
	String sortParam = "";

	String[] multiSelectionFacerts;

	/** The count. */
	private int count;

	/** The page num. */
	private int pageNum = 1;

	// /** The product list adapter. */
	// private ProductListAdapter productListAdapter;

	AlertDialog sortDialog;
	AlertDialog filterDialog;
	private ProgressDialog pd;
	private FrameLayout searchLoadingLayout;
	private LinearLayout searchBarLayout;
	private String mSearchterm;
	private boolean mFromScan;

	// LinearLayout loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		setTitle("Search");
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		btnClearSearch = (Button) findViewById(R.id.btnClearSearch);
		searchLoadingLayout = (FrameLayout) findViewById(R.id.searchLoadingDialog);
		searchBarLayout = (LinearLayout) findViewById(R.id.searchBarLayout);
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(txtSearch, InputMethodManager.SHOW_IMPLICIT);

		btnClearSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txtSearch.setText("");
			}
		});

		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("scan")) {
				searchBarLayout.setVisibility(View.GONE);
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
				mFromScan = true;
				setTitle("");
				searchLoadingLayout.setVisibility(View.VISIBLE);
				if (null != getIntent().getExtras().getString("search")) {
					mSearchterm = getIntent().getExtras().getString("search");
				}
				fnInvokeSearch();
			}
		}

		txtSearch.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
		txtSearch.requestFocus();
		txtSearch.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View arg0, int arg1, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (arg1 == KeyEvent.KEYCODE_ENTER)) {
					hideKeyboard();
					if (txtSearch.length() == 0) {
						try {
							notifyUser(
									"Please enter the product to be searched",
									SearchActivity.this);
						} catch (WindowManager.BadTokenException e) {
						} catch (Exception e) {
						}

					} else {
						mSearchterm = txtSearch.getText().toString();
						fnInvokeSearch();
						// Intent intentToSearchActivity = new Intent(
						// SearchActivity.this,
						// UltaProductListActivity.class);
						// intentToSearchActivity.putExtra("search", txtSearch
						// .getText().toString());
						// startActivityForResult(intentToSearchActivity, 1);
						return true;
					}
				}
				return false;
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onActivityResult(int arg0, int resultCode, Intent arg2) {
		super.onActivityResult(arg0, resultCode, arg2);
		txtSearch.setText("");
		if (resultCode != RESULT_CANCELED) {
			try {
//					notifyUser("No products found", SearchActivity.this);
			} catch (WindowManager.BadTokenException e) {
			} catch (Exception e) {
			}
		} else
			finish();
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * Method to populate invoker params and fire the web service.
	 * 
	 * @param id
	 *            the id
	 */
	private void fnInvokeSearch() {
		searchLoadingLayout.setVisibility(View.VISIBLE);
		InvokerParams<SearchBean> invokerParams = new InvokerParams<SearchBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.KEYWORD_SEARCH_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);

		invokerParams.setUrlParameters(fnPopulateSearchParameters());
		invokerParams.setUltaBeanClazz(SearchBean.class);
		SearchHandler searchHandler = new SearchHandler();
		invokerParams.setUltaHandler(searchHandler);
		try {
			Logger.Log("<UltaProductListActivity><fnInvokeSearch()><Going to Execute the Delegator>>");
			new ExecutionDelegator(invokerParams);
			Logger.Log("<UltaProductListActivity><fnInvokeSearch()><Executed the Delegator>>");
		} catch (UltaException exception) {
			searchLoadingLayout.setVisibility(View.GONE);
			Logger.Log("<UltaProductListActivity><fnInvokeSearch()><UltaException>>"
					+ exception);
		}
	}

	private Map<String, String> fnPopulateSearchParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-depth", "2");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("searchTerm", mSearchterm);
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("pageNumber", pageNum + "");
		urlParams.put("howMany", "12");
		urlParams.put("sortBy", sortSearch);
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("categoryDimId", categoryId);
		urlParams.put("brandDimIds", brandId);
		urlParams.put("colorDimId", colorId);
		urlParams.put("promotionDimIds", promotionid);
		urlParams.put("minPrice", minPrice);
		urlParams.put("maxPrice", maxPrice);
		return urlParams;
	}

	private SearchBean productsSearched;

	public class SearchHandler extends UltaHandler {
		/**
		 * on Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<SearchHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			if (null != getErrorMessage()) {
				searchLoadingLayout.setVisibility(View.GONE);
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							SearchActivity.this);
					setError(SearchActivity.this, getErrorMessage());
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<SearchHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				productsSearched = (SearchBean) getResponseBean();

				if (null != productsSearched) {

					if (null != productsSearched.getRedirectInfo()) {
						productId = productsSearched.getRedirectInfo()
								.getProductId();
						skuId = productsSearched.getRedirectInfo().getSkuId();
					}
					count = productsSearched.getTotalNoOfProducts();

					if (count == 0) {
						trackAppState(SearchActivity.this,
								WebserviceConstants.SEARCH_FAILURE);
						searchLoadingLayout.setVisibility(View.GONE);
						/*
						 * final String lblPositiveButton = "OK"; final
						 * AlertDialog.Builder errorAlertDialogBuilder = new
						 * AlertDialog.Builder( SearchActivity.this); if
						 * (fromScan) { errorAlertDialogBuilder .setMessage(
						 * "Unable to locate product online. Sold in store only."
						 * ); } else { errorAlertDialogBuilder
						 * .setMessage("Search was unable to find any results."
						 * ); } errorAlertDialogBuilder.setTitle("Sorry");
						 * errorAlertDialogBuilder.setCancelable(false);
						 * errorAlertDialogBuilder.setPositiveButton(
						 * lblPositiveButton, new
						 * DialogInterface.OnClickListener() { public void
						 * onClick(DialogInterface dialog, int id) {
						 * dialog.dismiss(); // finish(); } }); final
						 * AlertDialog errorAlertDialog =
						 * errorAlertDialogBuilder .create();
						 * errorAlertDialog.show();
						 */

						String message = "";

						if (fromScan) {
							message = "Unable to locate product online. Sold in store only.";
						} else {
							message = "Search was unable to find any results.";
						}

						final Dialog alert = showAlertDialog(
								SearchActivity.this, "Sorry", message, "OK", "");
						if (!UltaDataCache.getDataCacheInstance()
								.isCalledOnlyOnce()) {
							UltaDataCache.getDataCacheInstance()
									.setCalledOnlyOnce(true);
							alert.show();
						} 
						alert.setCancelable(false);

						mDisagreeButton.setVisibility(View.GONE);
						mAgreeButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								UltaDataCache.getDataCacheInstance()
								.setCalledOnlyOnce(false);
								alert.dismiss();
							}
						});

					} else {
						trackAppState(SearchActivity.this,
								WebserviceConstants.SEARCH_SUCCESS);
						if (productsSearched.isRedirectToPDP()) {
							Intent ultaProductDetailsIntent = new Intent(
									SearchActivity.this,
									UltaProductDetailsActivity.class);
							ultaProductDetailsIntent.putExtra("idFromSearch",
									productId);
							ultaProductDetailsIntent.putExtra("skuId", skuId);
							ultaProductDetailsIntent.putExtra("fromSearch",
									"search");
							if (!UltaDataCache.getDataCacheInstance()
									.isCalledOnlyOnce()) {
								UltaDataCache.getDataCacheInstance()
										.setCalledOnlyOnce(true);
								startActivity(ultaProductDetailsIntent);
							}
							else if (!mFromScan) {
								startActivity(ultaProductDetailsIntent);
							}
							searchLoadingLayout.setVisibility(View.GONE);
							if (mFromScan) {
								finish();
							}
						} else {
							Intent ultaProductListIntent = new Intent(
									SearchActivity.this,
									UltaProductListActivity.class);
							ultaProductListIntent.putExtra("search", txtSearch
									.getText().toString().trim());

							ultaProductListIntent.putExtra("serachBean",
									productsSearched);
							startActivity(ultaProductListIntent);
							searchLoadingLayout.setVisibility(View.GONE);
						}
					}

				} else {
					searchLoadingLayout.setVisibility(View.GONE);
					Toast.makeText(SearchActivity.this, "search bean null",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
