package com.ulta.core.fragments.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.activity.account.AddShippingAddressActivity;
import com.ulta.core.activity.account.EditShippingaddessActivity;
import com.ulta.core.activity.account.MyAccountActivity;
import com.ulta.core.bean.account.DefaultShippingAddressBean;
import com.ulta.core.bean.checkout.AddressBean;
import com.ulta.core.bean.checkout.ShippingAddressesBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.displayUserErrorMessage;

public class ProfileAddressFragment extends Fragment {

	/** The Constant RADIO_BTN_ID_INDEX. */
	public final static int RADIO_BTN_ID_INDEX = 100;

	public final static int ADD_NEW_SHIPPING_ADDRESS_REQ_CODE = 1;

	public final static int EDIT_SHIPPING_ADDRESS_REQ_CODE = 2;

	/** The context. */
	Context context;

	ListView listView;
	ViewGroup footer;
	/** The selected address. */
	String selectedAddress;
	ProfileAddressesAdapter profileAddressesAdapter;
	LinearLayout addressLayout;
	LinearLayout extended_menu_list_main, loadingDialog;
	ShippingAddressesBean shippingAddressesBean;
	private List<AddressBean> shippingaddressesList;
	private ProgressDialog pd;
	private OnEditCompletion onEditCompletion;
	private OnDefaultObtained onDefaultObtained;
	private OnDeleteDone onDeleteDone;
	// private OnFragmentRetrieveDetailsSessionTimeOut onFragmentSessionTimeOut;
	private OnFragmentDeleteAddressTimeout onFragmentDeleteAddressTimeout;
	private String nickName;
	private int nickNamePosition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view1 = inflater.inflate(R.layout.address_profile_fragment,
				container, false);
		listView = (ListView) view1.findViewById(R.id.lists);

		loadingDialog = (LinearLayout) view1
				.findViewById(R.id.loadingprofileAddress);

		footer = (ViewGroup) inflater.inflate(R.layout.footer_list_address,
				null, false);
		listView.addFooterView(footer, null, false);
		pd = new ProgressDialog(getActivity());
		pd.setMessage(LOADING_PROGRESS_TEXT);
		if (Build.VERSION.SDK_INT >= 21) {
			pd.setIndeterminateDrawable(context
					.getDrawable(R.drawable.progressdialog_loadingcolor));
		} else {
			pd.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.progressdialog_loadingcolor));
		}
		pd.setCancelable(false);

		Button btnAddNewaddress = (Button) footer
				.findViewById(R.id.btnAddNewAddress);
		btnAddNewaddress.setPadding(20, 20, 20, 20);
		btnAddNewaddress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentforAddNewShippingaddress = new Intent(
						getActivity(), AddShippingAddressActivity.class);

				startActivityForResult(intentforAddNewShippingaddress,
						ADD_NEW_SHIPPING_ADDRESS_REQ_CODE);
			}
		});

		return view1;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == activity.RESULT_OK
				&& (requestCode == ADD_NEW_SHIPPING_ADDRESS_REQ_CODE || requestCode == EDIT_SHIPPING_ADDRESS_REQ_CODE)) {
			refreshList();
			onEditCompletion.onEditCompleted();
			Intent intentMyAccount = new Intent(getActivity(),
					MyAccountActivity.class);
			intentMyAccount.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentMyAccount);

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Activity activity;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		context = activity;
	}

	public void refreshList() {
		if (profileAddressesAdapter != null) {
			profileAddressesAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Sets the selected address.
	 * 
	 * @param selectedAddress
	 *            the new selected address
	 */
	public void setSelectedAddress(String selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	/**
	 * Gets the selected address.
	 * 
	 * @return the selected address
	 */
	public String getSelectedAddress() {
		return selectedAddress;
	}

	public class ProfileAddressesAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return shippingaddressesList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflator = (LayoutInflater) getActivity()
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.profile_address_fragment,
					null);
			TextView rootItemText1 = (TextView) convertView
					.findViewById(R.id.textView1);
			TextView rootItemText2 = (TextView) convertView
					.findViewById(R.id.textView2);
			Button btnDelete = (Button) convertView
					.findViewById(R.id.btnDelete);

			rootItemText1.setText(shippingaddressesList.get(position)
					.getFirstName());
			rootItemText2.setText(shippingaddressesList.get(position)
					.getAddress1());
			btnDelete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (shippingaddressesList
							.get(position)
							.getId()
							.equalsIgnoreCase(
									shippingAddressesBean
											.getDefaultShippingAddressId())) {

						final AlertDialog.Builder alert = new AlertDialog.Builder(
								getActivity());
						alert.setNegativeButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alert.setMessage("You cannot delete default shipping address");
						alert.create().show();
					} else {
						final AlertDialog.Builder alert = new AlertDialog.Builder(
								getActivity());
						alert.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										pd.show();
										nickName = shippingaddressesList.get(
												position).getNickName();
										nickNamePosition = position;
										inokeDeleteAddressDetails(
												shippingaddressesList.get(
														position).getNickName(),
												position);
									}
								});
						alert.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alert.setMessage("Do you want to delete the Address Details ?");
						alert.create().show();
					}

				}
			});
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intentForEditaddress = new Intent(getActivity()
							.getApplicationContext(),
							EditShippingaddessActivity.class);
					intentForEditaddress.putExtra("AddressToEdit",
							shippingaddressesList.get(position));
					if (null != shippingAddressesBean
							.getDefaultShippingAddressId()) {
						intentForEditaddress.putExtra("defaultShippingId",
								shippingAddressesBean
										.getDefaultShippingAddressId());
					}
					startActivityForResult(intentForEditaddress,
							EDIT_SHIPPING_ADDRESS_REQ_CODE);
				}
			});
			return convertView;
		}

	}

	public void populateList() {
		if (null != pd && pd.isShowing()) {
			pd.show();
		}

		InvokerParams<ShippingAddressesBean> invokerParams = new InvokerParams<ShippingAddressesBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.LISTOF_SHIPPING_ADDRESS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(populateShippingaddressesDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(ShippingAddressesBean.class);
		RetrieveShippingaddressesHandler retrieveShippingaddressesDetailsHandler = new RetrieveShippingaddressesHandler();
		invokerParams.setUltaHandler(retrieveShippingaddressesDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<ProfileAddressFragment><populateList()><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Populate payment method details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateShippingaddressesDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "0");
		return urlParams;
	}

	/**
	 * The Class RetrievePaymentDetailsHandler.
	 */
	public class RetrieveShippingaddressesHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveShippingaddressesDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				// if (getErrorMessage().startsWith("401")) {
				// // onFragmentSessionTimeOut.OnFragmentSessionTimeOut(true);
				// } else {

				try {
					displayUserErrorMessage(null,
							Utility.formatDisplayError(getErrorMessage()),
							context, null);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
				// }

			} else {
				Logger.Log("<RetrieveShippingaddressesDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				shippingAddressesBean = (ShippingAddressesBean) getResponseBean();
				if (null != shippingAddressesBean) {
					if (null != shippingAddressesBean
							.getDefaultShippingAddressId()) {
						String defaultId = shippingAddressesBean
								.getDefaultShippingAddressId();
						for (int i = 0; i < shippingAddressesBean
								.getShippingAddresses().size(); i++) {
							if (defaultId.equals(shippingAddressesBean
									.getShippingAddresses())) {
								onDefaultObtained
										.OnDefaultObtained(shippingAddressesBean
												.getShippingAddresses().get(i));
								break;
							}
						}
					}
					Logger.Log("<ProfileAddressFragment>" + "BeanPopulated");
					shippingaddressesList = shippingAddressesBean
							.getShippingAddresses();

					profileAddressesAdapter = new ProfileAddressesAdapter();
					listView.setAdapter(profileAddressesAdapter);
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intentForEditaddress = new Intent(
									getActivity().getApplicationContext(),
									EditShippingaddessActivity.class);
							intentForEditaddress.putExtra("AddressToEdit",
									shippingaddressesList.get(position));
							startActivity(intentForEditaddress);
						}
					});
				}
			}
		}
	}

	public void deleteFromList(String nickname, int index) {
		inokeDeleteAddressDetails(nickname, index);
	}

	protected void inokeDeleteAddressDetails(String nickName, int index) {
		InvokerParams<DefaultShippingAddressBean> invokerParams = new InvokerParams<DefaultShippingAddressBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.DELETE_SHIPPING_ADDRESS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams
				.setUrlParameters(deleteShippingAddressDetailsHandlerParameters(nickName));
		invokerParams.setUltaBeanClazz(DefaultShippingAddressBean.class);
		RetrieveDeleteShippingAddressDetailsHandler retrieveDeleteShippingAddressDetailsHandler = new RetrieveDeleteShippingAddressDetailsHandler(
				index);
		invokerParams
				.setUltaHandler(retrieveDeleteShippingAddressDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<ProfileAddressFragment><inokeDeleteAddressDetails()><UltaException>>"
					+ ultaException);

		}

	}

	/**
	 * Populate prefferd shipping address details handler parameters.
	 * 
	 * @param address1
	 * @param index
	 * @return the map
	 */
	private Map<String, String> deleteShippingAddressDetailsHandlerParameters(
			String nickName) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
		urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("removeAddress", nickName);
		// urlParams.put("deleteAddrsNickName",nickName);
		return urlParams;
	}

	/**
	 * The Class RetrieveMyPreffredShippingAddressDetailsHandler.
	 */
	public class RetrieveDeleteShippingAddressDetailsHandler extends
			UltaHandler {

		int index;

		public RetrieveDeleteShippingAddressDetailsHandler(int index) {
			this.index = index;
		}

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveDeleteShippingAddressDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				if (getErrorMessage().startsWith("401")) {
					onFragmentDeleteAddressTimeout
							.OnFragmentDeleteDetailsSessionTimeOut(true,
									nickName, nickNamePosition);
				} else {
					try {
						displayUserErrorMessage(null,
								Utility.formatDisplayError(getErrorMessage()),
								context, null);
						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				String deletedId = shippingaddressesList.get(index).getId();

				if (deletedId.equals(shippingAddressesBean
						.getDefaultShippingAddressId())) {
					Logger.Log(">>>>>>>>>>>>>> inside the delete handler loop>>>>>>>>>>>>");

					onDeleteDone.OnDeleteDone();
					Toast.makeText(context, "Deletion Successful",
							Toast.LENGTH_SHORT).show();

					populateList();
				} else {
					Toast.makeText(context, "Deletion Successful",
							Toast.LENGTH_SHORT).show();
					populateList();
				}
			}
		}

	}

	public OnEditCompletion getOnEditCompletion() {
		return onEditCompletion;
	}

	public void setOnEditCompletion(OnEditCompletion onEditCompletion) {
		this.onEditCompletion = onEditCompletion;
	}

	public interface OnEditCompletion {
		public void onEditCompleted();
	}

	public interface OnDefaultObtained {
		public void OnDefaultObtained(AddressBean address);
	}

	public OnDefaultObtained getOnDefaultObtained() {
		return onDefaultObtained;
	}

	// public OnFragmentRetrieveDetailsSessionTimeOut
	// getOnFragmentSessionTimeOut() {
	// return onFragmentSessionTimeOut;
	// }
	//
	// public void setOnFragmentSessionTimeOut(
	// OnFragmentRetrieveDetailsSessionTimeOut onFragmentSessionTimeOut) {
	// this.onFragmentSessionTimeOut = onFragmentSessionTimeOut;
	// }

	public void setOnDefaultObtained(OnDefaultObtained onDefaultObtained) {
		this.onDefaultObtained = onDefaultObtained;
	}

	public OnDeleteDone getOnDeleteDone() {
		return onDeleteDone;
	}

	public void setOnDeleteDone(OnDeleteDone onDeleteDone) {
		this.onDeleteDone = onDeleteDone;
	}

	public interface OnDeleteDone {
		public void OnDeleteDone();
	}

	public interface OnFragmentRetrieveDetailsSessionTimeOut {
		public void OnFragmentSessionTimeOut(boolean isRetrieveShippingDetails);
	}

	public interface OnFragmentDeleteAddressTimeout {
		public void OnFragmentDeleteDetailsSessionTimeOut(
				boolean isDeleteShippingDetails, String nickName, int Position);
	}

	public OnFragmentDeleteAddressTimeout getOnFragmentDeleteAddressTimeout() {
		return onFragmentDeleteAddressTimeout;
	}

	public void setOnFragmentDeleteAddressTimeout(
			OnFragmentDeleteAddressTimeout onFragmentDeleteAddressTimeout) {
		this.onFragmentDeleteAddressTimeout = onFragmentDeleteAddressTimeout;
	}

}
