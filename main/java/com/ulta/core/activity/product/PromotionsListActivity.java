/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.PromotionBean;
import com.ulta.core.bean.product.atgResponseListBean;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionsListActivity extends UltaBaseActivity
{

	List<atgResponseListBean> promotionBeanList;
	private String flag;
	private String[] promotionList;
	private ListView listView;
	LinearLayout promotionListLayout ;
	FrameLayout loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promotions_list);
		initViews();
		Bundle bundleFromHomeActivity = getIntent().getExtras();
		// Date:7thMay,2013.Changed to display altText.(Sayak)
		if (null != getIntent().getAction()) {
			if (getIntent().getAction().equalsIgnoreCase("fromHome")) {
				if (null != getIntent().getExtras().getString("altText")) {
					setTitle(getIntent().getExtras().getString("altText"));
				}
			}
		}
		// till here
		if (null != bundleFromHomeActivity) {
			flag = bundleFromHomeActivity.getString("flag");
			invokePromotions(flag);
		}

	}

	private void initViews() {
		promotionListLayout = (LinearLayout) findViewById(R.id.promotionListLayout);
		loadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
		listView = (ListView) findViewById(R.id.promotionListView);
	}

	private void invokePromotions(String flag) {
		// Toast.makeText(PromotionsListActivity.this,"Firing Promotions web service",
		// 2000).show();
		InvokerParams<PromotionBean> invokerParams = new InvokerParams<PromotionBean>();
		invokerParams
				.setServiceToInvoke("ulta/mobile/catalog/MobileCatalogServices/getPromotions");
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateShippmentParameters1(flag));
		invokerParams.setUltaBeanClazz(PromotionBean.class);
		PurchaseHandler1 userCreationHandler = new PurchaseHandler1();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		}
		catch (UltaException ultaException) {
			Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}

	}

	private Map<String, String> populateShippmentParameters1(String flag) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("promotionType", flag);
		// urlParams.put("promotionType","B");
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	public class PurchaseHandler1 extends UltaHandler
	{
		public void handleMessage(Message msg) {
			Logger.Log("<PurchaseHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(getErrorMessage(), PromotionsListActivity.this);
				}
				catch (WindowManager.BadTokenException e) {
				}
				catch (Exception e) {
				}
			}
			else {
				Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				PromotionBean ultaBean = (PromotionBean) getResponseBean();

				List<String> errors = ultaBean.getErrorInfos();
				if (null != errors && !(errors.isEmpty()))
					try {
						notifyUser(errors.get(0), PromotionsListActivity.this);
					}
					catch (WindowManager.BadTokenException e) {
					}
					catch (Exception e) {
					}
				else {
					Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
							+ (getResponseBean()));
					if (null != ultaBean) {
						Logger.Log("<HomeActivity>" + "BeanPopulated");

						promotionBeanList = ultaBean.getAtgResponseList();
						if (null != promotionBeanList) {
							List<String> listOfPromotions = new ArrayList<String>();
							fnSetListofPromotions(listOfPromotions);
							loadingDialog.setVisibility(View.GONE);
							promotionListLayout.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		}
	}

	private void fnSetListofPromotions(List<String> listOfPromotions) {
		promotionList = listOfPromotions.toArray(new String[listOfPromotions
				.size()]);
		listView.setAdapter(new PromotionListAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
						String idClicked = promotionBeanList.get(position).getId();
				// Toast.makeText(PromotionsListActivity.this,
				// "Navigate to page with id: "+idClicked, 2000).show();
				Intent intentToSearchActivity = new Intent(
						PromotionsListActivity.this,
						UltaProductListActivity.class);
				intentToSearchActivity.setAction("fromPromotion");
				intentToSearchActivity.putExtra("promotionId", idClicked);
				startActivity(intentToSearchActivity);
			}
		});
	}

	public class PromotionListAdapter extends BaseAdapter
	{

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
				return promotionList.length;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
				return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
				return 0;
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.root_list_image_new, null);
			TextView item = (TextView) convertView
					.findViewById(R.id.root_menu_list_item_text);
			item.setText(promotionList[position]);
			return convertView;
		}
	}
}
