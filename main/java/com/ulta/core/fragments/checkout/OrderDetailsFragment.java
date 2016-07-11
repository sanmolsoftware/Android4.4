package com.ulta.core.fragments.checkout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.bean.checkout.CheckoutCartBean;
import com.ulta.core.bean.checkout.CheckoutCommerceItemBean;
import com.ulta.core.bean.checkout.CheckoutLoyaltyPointsPaymentGroupBean;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFragment extends ListFragment {
	/*private final static int REQUESTCODE_FREESAMPLES=0;
	private final static int REQUESTCODE_GIFTOPTION=1;
	private final static int REQUESTCODE_PAYMENTMETHOD=2;
	private final static int REQUESTCODE_BILLINGADDRESS=3;
	private final static int REQUESTCODE_PROMOCODE=4;
	private final static int REQUESTCODE_SHIPPINGADDRESS=5;
	private final static int REQUESTCODE_SHIPPINGMETHOD=6;*/
	/** The context. */
	Context context;

	/** The footer. */
	ViewGroup header,footer;

	/** The btn place order. */
	Button  btnPlaceOrder;

	/** The shipping method. */
	LinearLayout giftOption,freeSamples,paymentMethod,billingAddress,giftCode,shippingAddress,shippingMethod;

	/** The txt sub total. */
	TextView txtNoOfItems,txtShippingCharge,txtRawSubTotal,txtTax,txtSubTotal,txtRedeemedAmount;

	/** The chk gift option. */
	CheckBox chkFreeSamples,chkGiftOption;

	/** The str credit card number. */
	String strShippingMethod,strShippingAddress,strBillingAddress,strCreditCardType,strCreditCardNumber;

	/** The review order bean. */
	CheckoutCartBean reviewOrderBean;
	
	
	CheckoutLoyaltyPointsPaymentGroupBean checkOutLoyalityAmount;
	
	//private int quantity=0;
	private int i;

	/** The is card details complete. */
/*	private static boolean isCardDetailsComplete=false;*/

	/** The is billing address complete. */
/*	private static boolean isBillingAddressComplete=false;*/

	/** The is shipping address complete. */
	/*private static boolean isShippingAddressComplete=false;*/

	/** The is shipping method complete. */
	/*private static boolean isShippingMethodComplete=false;*/

	/** The is gift option complete. */
/*	private static boolean isGiftOptionComplete=false;*/

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ListView view= (ListView)inflater.inflate(R.layout.list, null);
		header = (ViewGroup)inflater.inflate(R.layout.submit_order_list_header, null, false);
		//getListView().addHeaderView(header, null, false); 

		footer = (ViewGroup)inflater.inflate(R.layout.submit_order_list_footer, null, false);
		view.addFooterView(footer, null, false); 

		
		txtNoOfItems=(TextView)footer.findViewById(R.id.txtNoOfItems);
		txtNoOfItems.setText("Some text");
		txtRawSubTotal=(TextView)footer.findViewById(R.id.tvSubmitorderRawTotal);
		txtShippingCharge=(TextView)footer.findViewById(R.id.tvSubmitorderShipping);
		txtTax=(TextView)footer.findViewById(R.id.tvSubmitorderTax);
		txtSubTotal=(TextView)footer.findViewById(R.id.tvSubmitorderTotal);
		txtRedeemedAmount=(TextView)footer.findViewById(R.id.tvRedeemedAmount);


		btnPlaceOrder=(Button)header.findViewById(R.id.placeOrder);

		//return inflater.inflate(R.layout.list, null);
		return view;

		//return super.onCreateView(inflater, container, savedInstanceState);
	}




	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		context=activity;
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	/**
	 * Sets the list footer data.
	 *
	 * @param object the new list footer data
	 */
	public void setListFooterData(CheckoutCartBean object){
		int quantity=0;
		double adjustedAmount=0;
		reviewOrderBean=(CheckoutCartBean) object;
		//3.2 Release
		checkOutLoyalityAmount=new CheckoutLoyaltyPointsPaymentGroupBean();
		Logger.Log(">>>>>>>>>> Loyality Point Bean size"+reviewOrderBean.getLoyaltyPointsPaymentGroups().size());
		if(reviewOrderBean.getLoyaltyPointsPaymentGroups().size()!=0){
			for(int i=0;i<reviewOrderBean.getLoyaltyPointsPaymentGroups().size();i++){
				checkOutLoyalityAmount=reviewOrderBean.getLoyaltyPointsPaymentGroups().get(i);
				adjustedAmount += Double.valueOf(checkOutLoyalityAmount.getAmount());
			}
		}
		Logger.Log(">>>>>>>>>> reviewOrderBean total"+reviewOrderBean.getOrderDetails().getTotal());
		txtNoOfItems.setText(">>>>>>>>>>>nothing>>>>>>");
		for(i=0;i<reviewOrderBean.getCommerceItems().size();i++){
			quantity=quantity+Integer.parseInt(reviewOrderBean.getCommerceItems().get(i).getQuantity());
			Logger.Log(">>>>>>>>>>>>>>>><<<<<<<<<<||||>>>>>>>><<<<<"+quantity+"----"+i);
		}
		//quantity=quantity/2;
//		txtNoOfItems.setText(quantity+"  Products");
		//txtNoOfItems.setText(reviewOrderBean.getCommerceItems().size()+"  Products");
		
		if(null!=reviewOrderBean.getCommerceItems() && null!=reviewOrderBean.getElectronicGiftCardCommerceItems()){
			txtNoOfItems.setText(quantity+reviewOrderBean.getElectronicGiftCardCommerceItems().size()+"  Products");
		}else if(null!=reviewOrderBean.getCommerceItems()){
			txtNoOfItems.setText(quantity+"  Products");
		}else if(null!=reviewOrderBean.getElectronicGiftCardCommerceItems()){
			txtNoOfItems.setText(reviewOrderBean.getElectronicGiftCardCommerceItems().size()+"  Products");
		}
		
		txtRawSubTotal.setText("$"+String.format("%.2f", Double.valueOf(reviewOrderBean.getOrderDetails().getRawSubtotal())));
		txtShippingCharge.setText("$"+String.format("%.2f", Double.valueOf(reviewOrderBean.getOrderDetails().getShipping())));
		txtTax.setText("$"+String.format("%.2f", Double.valueOf(reviewOrderBean.getOrderDetails().getTax())));
		//3.2Release
		if(adjustedAmount!=0){
			LinearLayout lytRedeemedAmount = (LinearLayout)footer.findViewById(R.id.linearLayout15);
			lytRedeemedAmount.setVisibility(View.VISIBLE);
			// Reported error on playstore : Fixed 
			txtRedeemedAmount.setText("-$"+String.format("%.2f",Double.parseDouble(checkOutLoyalityAmount.getAmount())));
		}
		if(reviewOrderBean.getLoyaltyPointsPaymentGroups().size()!=0 && adjustedAmount!=0){
			txtSubTotal.setText("$"+String.format("%.2f", Double.valueOf(reviewOrderBean.getOrderDetails().getTotal())-adjustedAmount));
		}
		else{		
			txtSubTotal.setText("$"+String.format("%.2f", Double.valueOf(reviewOrderBean.getOrderDetails().getTotal())));
		}
	}

	
	public void populateListData(CheckoutCartBean reviewOrderBean){
//		List<CheckoutCommerceItemBean> commerceItems=reviewOrderBean.getCommerceItems();
		
		List<CheckoutCommerceItemBean> combinedList=new ArrayList<CheckoutCommerceItemBean>();
		if(null!=reviewOrderBean.getCommerceItems() && null!=reviewOrderBean.getElectronicGiftCardCommerceItems()){
			combinedList.addAll(reviewOrderBean.getCommerceItems());
			combinedList.addAll(reviewOrderBean.getElectronicGiftCardCommerceItems());
		}else if(null!=reviewOrderBean.getCommerceItems()){
			combinedList.addAll(reviewOrderBean.getCommerceItems());
		}else if(null!=reviewOrderBean.getElectronicGiftCardCommerceItems()){
			combinedList.addAll(reviewOrderBean.getElectronicGiftCardCommerceItems());
		}
		
		setListAdapter(new SubmitOrderFragmentAdapter(combinedList));
	}


	/**
	 * The Class SubmitOrderFragmentAdapter.
	 */
	class SubmitOrderFragmentAdapter extends BaseAdapter{
		List<CheckoutCommerceItemBean> commerceItems;
		

		public SubmitOrderFragmentAdapter(
				List<CheckoutCommerceItemBean> commerceItems) {
			super();
			this.commerceItems = commerceItems;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
				return commerceItems.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
				return null;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
				return 0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			CheckoutCommerceItemBean bean=commerceItems.get(position);
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.submit_order_list_item, null);
			
			ImageView pdtImage=(ImageView) view.findViewById(R.id.submitOrderListItemImg);
			/*ImageView giftImage=(ImageView) view.findViewById(R.id.submitOrderListItemGiftImg);*/
			
			
			TextView pdtName=(TextView) view.findViewById(R.id.submitOrderListItemPdtName);
			TextView brand=(TextView) view.findViewById(R.id.submitOrderListItemBrandName);
			TextView quantity=(TextView) view.findViewById(R.id.submitOrderListItemQunatity);
			TextView price=(TextView) view.findViewById(R.id.submitOrderListItemPrice);
			 
			
			new AQuery(pdtImage).image(bean.getSmallImageUrl(), true, false, 200,
					R.drawable.dummy_product, null, AQuery.FADE_IN);
			/*if(bean.isGWP()){
				price.setText("FREE");
				giftImage.setVisibility(View.VISIBLE);
				price.setTextColor(color.roseBudCherry);
			}
			else if(null!=bean.getIsFreeSample() && bean.getIsFreeSample().equalsIgnoreCase("true")){
				price.setText("FREE");
				price.setTextColor(color.roseBudCherry);
			}*/
			/*else{*/
				price.setText(String.format("%.2f", Double.valueOf(bean.getAmount())));	
			/*}*/
			pdtName.setText(bean.getDisplayName());
			brand.setText(bean.getBrandName());
			quantity.setText("Quantity : "+bean.getQuantity());
			//price.setText(Double.valueOf(bean.getAmount()).toString());
			
			
			return view;
		}



	}

}

