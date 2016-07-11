/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *
 */

package com.ulta.core.activity.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.MyPurchasesBean;
import com.ulta.core.bean.account.OrderBean;
import com.ulta.core.bean.account.OrderHistoryBean;
import com.ulta.core.bean.account.TransactionDetailsBean;
import com.ulta.core.bean.checkout.CheckoutCartBean;
import com.ulta.core.bean.checkout.OrderTrackingBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class MyOrderHistoryActivity extends UltaBaseActivity implements
		OnSessionTimeOut {
	private ListView lvOrderItems;
	private List<OrderBean> listOfOrderBean;
	private List<OrderBean> listOfAllOrdersBean;
	private List<TransactionDetailsBean> transactionDetailsBean;
	private List<TransactionDetailsBean> listofAllTransactions;
	LinearLayout loadingDialog;
	private final int MAX_COUNT = 10;
	private int pageNum = 0, startIndex = 0;
	private OrderHistoryAdapter orderHistoryAdapter;
	private boolean transactionHistory = false;
	private int count;
	public boolean anonymousIdentifier = false;
	private ProgressDialog pd;

	private boolean mInvokePurchaseHistory;
	private boolean mInvokeOrderHistory;

	public void refreshPage() {
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		loadingDialog.setVisibility(View.VISIBLE);
		invokeOrderHistory(MAX_COUNT, pageNum);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_history_activity);
		setActivity(MyOrderHistoryActivity.this);
		setTitle("My Order History");
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		loadingDialog.setVisibility(View.VISIBLE);
		lvOrderItems = (ListView) findViewById(R.id.lvOrderItems);
		// 3.2 Release
		if (null != getIntent().getExtras()
				&& null != getIntent().getExtras()
						.get("orderStatusFromGiftTab")) {

			OrderBean orderBean = (OrderBean) getIntent().getExtras().get(
					"OrderStausBean");
			orderHistoryAdapter = new OrderHistoryAdapter(
					MyOrderHistoryActivity.this);
			listOfAllOrdersBean = new ArrayList<OrderBean>();
			listOfAllOrdersBean.add(orderBean);

			loadingDialog.setVisibility(View.GONE);
			lvOrderItems.setAdapter(orderHistoryAdapter);
			anonymousIdentifier = true;

		}

		else if (null != getIntent().getAction()) {
			if (getIntent().getAction().equalsIgnoreCase("PurchaseHistory")) {
				transactionHistory = true;
				setTitle("My Points History");
				listofAllTransactions = new ArrayList<TransactionDetailsBean>();
				invokePurchaseHistory(MAX_COUNT, pageNum);
			}
		}

		else {
			listOfAllOrdersBean = new ArrayList<OrderBean>();
			invokeOrderHistory(MAX_COUNT, startIndex);
		}
	};

	/**
	 * The Class CreditCardListAdapter.
	 */
	public class OrderHistoryAdapter extends BaseAdapter {

		/** The context. */
		@SuppressWarnings("unused")
		private Context context;

		/**
		 * Instantiates a new credit card list adapter.
		 *
		 * @param context
		 *            the context
		 * @param list
		 *            the list
		 */
		public OrderHistoryAdapter(Context context) {
			this.context = context;

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
				if (transactionHistory) {
				return listofAllTransactions.size();
			} else
				return listOfAllOrdersBean.size();

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
				LayoutInflater inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			LinearLayout itemLayout = (LinearLayout) inflater.inflate(
					R.layout.order_history_item, null);
			TextView orderId = (TextView) itemLayout.findViewById(R.id.orderId);
			TextView orderLastModifiedDate = (TextView) itemLayout
					.findViewById(R.id.orderLastModifiedDate);
			TextView orderState = (TextView) itemLayout
					.findViewById(R.id.orderState);
			TextView orderTotal = (TextView) itemLayout
					.findViewById(R.id.orderTotal);
			TextView orderTrackingNumber = (TextView) itemLayout
					.findViewById(R.id.orderTrackingNumber);
			ImageView image = (ImageView) itemLayout
					.findViewById(R.id.order_history_disclosure);

			if (transactionHistory) {
				if (position == (listofAllTransactions.size() - 1)
						&& position < (count - 1)
						&& listofAllTransactions.size() != count) {

					loadingDialog.setVisibility(View.VISIBLE);
					pageNum++;
					// pageNum=pageNum*MAX_COUNT;
					Logger.Log("position: " + position + " count: " + count
							+ " pagenum: " + pageNum);
					invokePurchaseHistory(MAX_COUNT, pageNum);

				}
				if (null != listofAllTransactions
						&& !listofAllTransactions.isEmpty()) {
					final TransactionDetailsBean orderBean = listofAllTransactions
							.get(position);
					String str = orderBean.getTransactionDate()
							.substring(0, 10);

					String str1 = orderBean.getTransactionDate().substring(24);
					String lastModifiedDate = str + " " + str1;
					orderId.setVisibility(View.GONE);
					image.setVisibility(View.GONE);
					orderLastModifiedDate.setText("Order Date: "
							+ lastModifiedDate);
					if(null!=orderBean.getStoreName()&&!orderBean.getStoreName().isEmpty()) {
						orderState.setText("Source: "
								+ orderBean.getStoreName());
					}
					else
					{
						orderState.setText("Source: "
								+ "NA");
					}
					orderTotal.setText("Type: "
							+ orderBean.getTransactionDescription());
					orderTrackingNumber.setText("Points: "
							+ orderBean.getPointBalance());

				}
			}

			else {
				if (position == (listOfAllOrdersBean.size() - 1)
						&& position < (count - 1)
						&& listOfAllOrdersBean.size() != count) {

					loadingDialog.setVisibility(View.VISIBLE);
					pageNum++;
					startIndex = pageNum * MAX_COUNT;
					Logger.Log("position: " + position + " count: " + count
							+ " pagenum: " + pageNum);
					if (startIndex < count) {
						invokeOrderHistory(MAX_COUNT, startIndex);
					}

				}
				if (null != listOfAllOrdersBean
						&& !listOfAllOrdersBean.isEmpty()) {
					final OrderBean orderBean = listOfAllOrdersBean
							.get(position);
					String str = orderBean.getLastModifiedDate().substring(0,
							10);
					String[] arr = str.split("-");
					String day = arr[2];
					String month = arr[1];
					String year = arr[0];
					String lastModifiedDate = day + "-" + month + "-" + year;
					orderId.setVisibility(View.VISIBLE);
					image.setVisibility(View.VISIBLE);
					orderId.setText("Order Number: " + orderBean.getId());
					orderLastModifiedDate.setText("Order Date: "
							+ lastModifiedDate);
					orderState.setText("Order State: " + orderBean.getState());
					orderTotal.setText("Order Total: "
							+ String.format("%.2f",
									Double.valueOf(orderBean.getTotal())));
					final List<OrderTrackingBean> trackingInfo = orderBean
							.getTrackingInfoList();
					if (null != trackingInfo && trackingInfo.size() != 0) {
						orderTrackingNumber.setVisibility(View.GONE);
						for (int i = 0; i < trackingInfo.size(); i++) {
							String trackingNo = trackingInfo.get(i)
									.getTrackingNumber();
							final String trackingURL = trackingInfo.get(i)
									.getTrackingURL();

							LinearLayout orderNoHolder = new LinearLayout(
									MyOrderHistoryActivity.this);
							orderNoHolder
									.setOrientation(LinearLayout.HORIZONTAL);
							TextView textViewForText = new TextView(
									MyOrderHistoryActivity.this);
							textViewForText.setText("Tracking Number :");
							textViewForText.setTextAppearance(
									getApplicationContext(),
									R.style.RiverBedRegular18);
							if (i != 0) {
								textViewForText.setVisibility(View.INVISIBLE);
							}
							TextView textViewForTrackingNum = new TextView(
									MyOrderHistoryActivity.this);
							if (!trackingNo.equals("Tracking Not Available")
									&& trackingNo != null) {
								SpannableString trackingLink = new SpannableString(
										trackingNo);
								trackingLink.setSpan(new UnderlineSpan(), 0,
										trackingLink.length(), 0);
								textViewForTrackingNum
										.setTextColor(getResources().getColor(
												R.color.jaffa));
								textViewForTrackingNum.setText(trackingLink);
								textViewForTrackingNum
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												Intent intentForOrderStatus = new Intent(
														MyOrderHistoryActivity.this,
														OrderTrackingActivity.class);
												intentForOrderStatus.putExtra(
														"TrackingURL",
														trackingURL);
												startActivity(intentForOrderStatus);
											}
										});
							} else {
								textViewForTrackingNum.setTextAppearance(
										getApplicationContext(),
										R.style.RiverBedRegular18);
								textViewForTrackingNum
										.setText("Tracking Not Available");
							}
							orderNoHolder.addView(textViewForText);
							orderNoHolder.addView(textViewForTrackingNum);
							LinearLayout childLayout = (LinearLayout) itemLayout
									.findViewById(R.id.childLayout);
							childLayout.addView(orderNoHolder);

						}
					}
					itemLayout.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pd = new ProgressDialog(MyOrderHistoryActivity.this);
							pd.setMessage(LOADING_PROGRESS_TEXT);
							setProgressDialogLoadingColor(pd);
							pd.setCancelable(false);
							pd.show();
							invokeOrderDetails(orderBean.getId());
						}
					});
				}
			}
			return itemLayout;
		}
	}

	private void invokePurchaseHistory(int noOfOrders, int pageNum) {
		InvokerParams<MyPurchasesBean> invokerParams = new InvokerParams<MyPurchasesBean>();
		invokerParams
				.setServiceToInvoke("ulta/loyalty/UltaLoyaltyFormHandler/transactionHistory");
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populatePurchaseHistoryHandlerParameters(
						noOfOrders, pageNum));
		invokerParams.setUltaBeanClazz(MyPurchasesBean.class);
		RetrievePurchaseHistoryHandler retrievePurchaseHistoryHandler = new RetrievePurchaseHistoryHandler();
		invokerParams.setUltaHandler(retrievePurchaseHistoryHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populatePurchaseHistoryHandlerParameters(
			int noOfOrders, int pageNum) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("txHistoryDays", String.valueOf(90));
		urlParams.put("numTransactions", String.valueOf(noOfOrders));
		urlParams.put("startIndex", String.valueOf(pageNum));
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		return urlParams;
	}

	public class RetrievePurchaseHistoryHandler extends UltaHandler {

		/**
		 * Handle message.
		 *
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveOrderHistoryHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					mInvokePurchaseHistory = true;
					askRelogin(MyOrderHistoryActivity.this);
				} else {
					loadingDialog.setVisibility(View.GONE);
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								MyOrderHistoryActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				loadingDialog.setVisibility(View.GONE);
				MyPurchasesBean purchaseBean = (MyPurchasesBean) getResponseBean();
				if (null != purchaseBean) {
					try {
						transactionDetailsBean = purchaseBean.getComponent()
								.getPurchaseHistory().getTransactionDetails();
						if (null != transactionDetailsBean) {
							listofAllTransactions
									.addAll(transactionDetailsBean);
						}

						if (listofAllTransactions.size() == 0) {
							/*AlertDialog alertDialog = new AlertDialog.Builder(
									MyOrderHistoryActivity.this).create();
							alertDialog.setTitle("Sorry");
							alertDialog
									.setMessage("Currently there are no orders placed by you.");
							alertDialog.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// here you can add functions
											finish();
										}
									});

							alertDialog.show();*/

							final Dialog alertDialog = showAlertDialog(
									MyOrderHistoryActivity.this, "Sorry",
									"Sorry there is no Points History.", "Ok", "");
							alertDialog.show();

							mDisagreeButton.setVisibility(View.GONE);
							mAgreeButton.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									finish();
								}
							});

						} else {
							if (null == orderHistoryAdapter) {
								orderHistoryAdapter = new OrderHistoryAdapter(
										MyOrderHistoryActivity.this);
								lvOrderItems.setAdapter(orderHistoryAdapter);
							} else {
								orderHistoryAdapter.notifyDataSetChanged();
							}
						}
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			}
		}
	}

	private void invokeOrderHistory(int numOrders, int pageNo) {
		InvokerParams<OrderHistoryBean> invokerParams = new InvokerParams<OrderHistoryBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.ORDER_HISTORY);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setUrlParameters(populateOrderHistoryHandlerParameters(
				numOrders, pageNo));
		invokerParams.setUltaBeanClazz(OrderHistoryBean.class);
		RetrieveOrderHistoryHandler retrieveOrderHistoryHandler = new RetrieveOrderHistoryHandler();
		invokerParams.setUltaHandler(retrieveOrderHistoryHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateOrderHistoryHandlerParameters(
			int numOrders, int pageNo) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("numOrders", String.valueOf(numOrders));
		urlParams.put("startIndex", String.valueOf(pageNo));

		// urlParams.put("atg-rest-return-form-handler-properties","TRUE");
		// urlParams.put("atg-rest-return-form-handler-exceptions","TRUE");
		// urlParams.put("txHistoryDays","90");
		// urlParams.put("userId","1450000");
		return urlParams;
	}

	/**
	 * The Class RetrievePaymentDetailsHandler.
	 */
	public class RetrieveOrderHistoryHandler extends UltaHandler {

		/**
		 * Handle message.
		 *
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveOrderHistoryHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					mInvokeOrderHistory = true;
					askRelogin(MyOrderHistoryActivity.this);
				} else {
					loadingDialog.setVisibility(View.GONE);
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								MyOrderHistoryActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				loadingDialog.setVisibility(View.GONE);
				OrderHistoryBean orderHistoryBean = (OrderHistoryBean) getResponseBean();

				if (null != orderHistoryBean) {
					try {

						listOfOrderBean = orderHistoryBean
								.getCompleteOrderHistory();
						if (null != listOfAllOrdersBean) {
							listOfAllOrdersBean.addAll(listOfOrderBean);
						}
						count = orderHistoryBean.getNumberOfOrder();
						if (listOfAllOrdersBean.size() == 0) {
							/*AlertDialog alertDialog = new AlertDialog.Builder(
									MyOrderHistoryActivity.this).create();
							alertDialog.setTitle("Sorry");
							alertDialog
									.setMessage("Currently there are no orders placed by you.");
							alertDialog.setButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// here you can add functions
											finish();
										}
									});

							alertDialog.show();*/

							final Dialog alertDialog = showAlertDialog(
									MyOrderHistoryActivity.this, "Sorry",
									"Currently there are no orders placed by you.", "Ok", "");
							alertDialog.show();

							mDisagreeButton.setVisibility(View.GONE);
							mAgreeButton.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									finish();
								}
							});
						} else {
							if (null == orderHistoryAdapter) {
								orderHistoryAdapter = new OrderHistoryAdapter(
										MyOrderHistoryActivity.this);
								lvOrderItems.setAdapter(orderHistoryAdapter);
							} else {
								orderHistoryAdapter.notifyDataSetChanged();
							}

						}

					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}

			}
		}
	}

	private void invokeOrderDetails(String id) {
		InvokerParams<CheckoutCartBean> invokerParams = new InvokerParams<CheckoutCartBean>();
		if (anonymousIdentifier) {
			// 3.2 Release
			invokerParams
					.setServiceToInvoke(WebserviceConstants.FETCH_ANONYMOUS_ORDER_DETAILS);
			invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		} else {
			invokerParams.setServiceToInvoke(WebserviceConstants.ORDER_DETAIL);
			invokerParams.setHttpProtocol(HttpProtocol.http);
		}
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams
				.setUrlParameters(populateOrderDetailsHandlerParameters(id));
		invokerParams.setUltaBeanClazz(CheckoutCartBean.class);
		OrderDetailsHandler orderDetailsHandler = new OrderDetailsHandler();
		invokerParams.setUltaHandler(orderDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateOrderDetailsHandlerParameters(String id) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("orderNumber", id);
		return urlParams;
	}

	/**
	 * The Class RetrievePaymentDetailsHandler.
	 */
	public class OrderDetailsHandler extends UltaHandler {

		/**
		 * Handle message.
		 *
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<OrderDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							MyOrderHistoryActivity.this);
					pd.dismiss();
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				CheckoutCartBean ultaBean = (CheckoutCartBean) getResponseBean();

				try {
					if (null != ultaBean) {
						Logger.Log("order success");
						Intent goToShippingMethod = new Intent(
								MyOrderHistoryActivity.this,
								MyOrderDetailsActivity.class);
						goToShippingMethod.putExtra("order", ultaBean);
						startActivity(goToShippingMethod);
						pd.dismiss();
					} else {
						Logger.Log("order failure");
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
		if (isSuccess) {
			if (mInvokeOrderHistory) {
				invokeOrderHistory(MAX_COUNT, pageNum);
			} else if (mInvokePurchaseHistory) {
				invokePurchaseHistory(MAX_COUNT, pageNum);
			}
		} else {
			loadingDialog.setVisibility(View.GONE);
		}
	}

}