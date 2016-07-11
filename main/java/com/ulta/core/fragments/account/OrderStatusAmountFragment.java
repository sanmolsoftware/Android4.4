package com.ulta.core.fragments.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class OrderStatusAmountFragment extends Fragment {
    ViewGroup footer;
    /**
     * The txt sub total.
     */
    TextView txtNoOfItems, txtShippingCharge, txtRawSubTotal, txtTax,
            txtSubTotal, txtRedeemedAmount, tvSubmitorderSubTotal,
            tvSubmitorderCouponDiscount, tvAdditionalDiscount;
    /**
     * The review order bean.
     */
    CheckoutCartBean reviewOrderBean;
    CheckoutLoyaltyPointsPaymentGroupBean checkOutLoyalityAmount;
    LinearLayout reviewOrderAddressLayout;
    // private int quantity=0;
    private int i;
    ListView view;
    Context context;
    private LinearLayout couponDiscountLayout, additionalDicountLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (ListView) inflater.inflate(R.layout.list, null);
        footer = (ViewGroup) inflater.inflate(
                R.layout.order_status_amount_footer, null, false);
        view.addFooterView(footer, null, false);
        reviewOrderAddressLayout = (LinearLayout) footer
                .findViewById(R.id.reviewOrderAddressLayout);
        reviewOrderAddressLayout.setVisibility(View.GONE);
        couponDiscountLayout = (LinearLayout) footer.findViewById(R.id.couponDiscountLayout);
        additionalDicountLayout = (LinearLayout) footer.findViewById(R.id.additionalDicountLayout);
        txtNoOfItems = (TextView) footer.findViewById(R.id.txtNoOfItems);
        txtNoOfItems.setText("Some text");
        txtRawSubTotal = (TextView) footer
                .findViewById(R.id.tvSubmitorderRawTotal);
        txtShippingCharge = (TextView) footer
                .findViewById(R.id.tvSubmitorderShipping);
        txtTax = (TextView) footer.findViewById(R.id.tvSubmitorderTax);
        txtSubTotal = (TextView) footer.findViewById(R.id.tvSubmitorderTotal);
        tvSubmitorderSubTotal = (TextView) footer
                .findViewById(R.id.tvSubmitorderSubTotal);
        txtRedeemedAmount = (TextView) footer
                .findViewById(R.id.tvRedeemedAmount);
        tvSubmitorderCouponDiscount = (TextView) footer
                .findViewById(R.id.tvSubmitorderCouponDiscount);
        tvAdditionalDiscount = (TextView) footer.findViewById(R.id.tvAdditionalDiscount);

        setListFooterData(reviewOrderBean);
        populateListData(reviewOrderBean);
        return view;
    }

    public OrderStatusAmountFragment(CheckoutCartBean object) {
        reviewOrderBean = (CheckoutCartBean) object;
    }

    @Override
    public void onAttach(Activity activity) {
        context = activity;
        super.onAttach(activity);
    }

    /**
     * Sets the list footer data.
     *
     * @param object the new list footer data
     */
    public void setListFooterData(CheckoutCartBean reviewOrderBean) {
        int quantity = 0;
        double adjustedAmount = 0;
        // 3.2 Release
        if (null != reviewOrderBean) {
            checkOutLoyalityAmount = new CheckoutLoyaltyPointsPaymentGroupBean();
            if (null != reviewOrderBean.getLoyaltyPointsPaymentGroups()) {
                Logger.Log(">>>>>>>>>> Loyality Point Bean size"
                        + reviewOrderBean.getLoyaltyPointsPaymentGroups()
                        .size());
                if (reviewOrderBean.getLoyaltyPointsPaymentGroups().size() != 0) {
                    for (int i = 0; i < reviewOrderBean
                            .getLoyaltyPointsPaymentGroups().size(); i++) {
                        checkOutLoyalityAmount = reviewOrderBean
                                .getLoyaltyPointsPaymentGroups().get(i);
                        adjustedAmount += Double.valueOf(checkOutLoyalityAmount
                                .getAmount());
                    }
                }
            }
            if (null != reviewOrderBean.getOrderDetails()) {
                Logger.Log(">>>>>>>>>> reviewOrderBean total"
                        + reviewOrderBean.getOrderDetails().getTotal());
            }
            txtNoOfItems.setText(">>>>>>>>>>>nothing>>>>>>");
            if (null != reviewOrderBean.getCommerceItems()) {
                for (i = 0; i < reviewOrderBean.getCommerceItems().size(); i++) {
                    quantity = quantity
                            + Integer.parseInt(reviewOrderBean
                            .getCommerceItems().get(i).getQuantity());
                    Logger.Log(">>>>>>>>>>>>>>>><<<<<<<<<<||||>>>>>>>><<<<<"
                            + quantity + "----" + i);
                }
            }
            // quantity=quantity/2;
            // txtNoOfItems.setText(quantity+"  Products");
            // txtNoOfItems.setText(reviewOrderBean.getCommerceItems().size()+"  Products");

            if (null != reviewOrderBean.getCommerceItems()
                    && null != reviewOrderBean
                    .getElectronicGiftCardCommerceItems()) {
                txtNoOfItems.setText(quantity
                        + reviewOrderBean.getElectronicGiftCardCommerceItems()
                        .size() + "  Products");
            } else if (null != reviewOrderBean.getCommerceItems()) {
                txtNoOfItems.setText(quantity + "  Products");
            } else if (null != reviewOrderBean
                    .getElectronicGiftCardCommerceItems()) {
                txtNoOfItems.setText(reviewOrderBean
                        .getElectronicGiftCardCommerceItems().size()
                        + "  Products");
            }
            tvSubmitorderSubTotal.setText("$"
                    + String.format("%.2f", Double.valueOf(reviewOrderBean
                    .getOrderDetails().getRawSubtotal())));
            txtRawSubTotal.setText("$"
                    + String.format("%.2f", Double.valueOf(reviewOrderBean
                    .getOrderDetails().getRawSubtotal())));
            txtShippingCharge.setText("$"
                    + String.format("%.2f", Double.valueOf(reviewOrderBean
                    .getOrderDetails().getShipping())));
            if(Double.valueOf(reviewOrderBean.getOrderDetails().getShipping()).equals(0.0))
            {
                txtShippingCharge.setText("FREE");
            }
            txtTax.setText("$"
                    + String.format("%.2f", Double.valueOf(reviewOrderBean
                    .getOrderDetails().getTax())));
            // 3.2Release
            if (adjustedAmount != 0) {
                LinearLayout lytRedeemedAmount = (LinearLayout) footer
                        .findViewById(R.id.linearLayout15);
                lytRedeemedAmount.setVisibility(View.VISIBLE);
                // Reported error on playstore : Fixed
                txtRedeemedAmount.setText("-$"
                        + String.format("%.2f",
                        Double.parseDouble(checkOutLoyalityAmount
                                .getAmount())));
            }

            if (null != reviewOrderBean.getCouponDiscount()) {
                if (null != reviewOrderBean.getCouponDiscount()
                        .getTotalAdjustment()) {
                    if(null!=reviewOrderBean.getCouponDiscount().getOrderDiscount()&&
                            !Double.valueOf(reviewOrderBean.getCouponDiscount().getOrderDiscount()).equals(0.0)) {
                        couponDiscountLayout.setVisibility(View.VISIBLE);
                        tvSubmitorderCouponDiscount.setText("-$"
                                + String.format("%.2f", Double
                                .valueOf(reviewOrderBean
                                        .getCouponDiscount()
                                        .getTotalAdjustment())));
                    }
                }
            }

            if (null != reviewOrderBean.getLoyaltyPointsPaymentGroups()
                    && reviewOrderBean.getLoyaltyPointsPaymentGroups().size() != 0
                    && adjustedAmount != 0) {
                txtSubTotal.setText("$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean
                                .getOrderDetails().getTotal())
                                - adjustedAmount));
            } else {
                txtSubTotal.setText("$"
                        + String.format("%.2f", Double.valueOf(reviewOrderBean
                        .getOrderDetails().getTotal())));
            }

            //Additional Discount

            if (null!=reviewOrderBean.getOrderDetails()&&null != reviewOrderBean.getOrderDetails().getTieredDiscountAmount() && !reviewOrderBean.getOrderDetails().getTieredDiscountAmount().isEmpty()) {
                tvAdditionalDiscount.setText("-$"
                        + String.format("%.2f",
                        Double.valueOf(reviewOrderBean.getOrderDetails().getTieredDiscountAmount())));
                additionalDicountLayout.setVisibility(View.VISIBLE);

            }
        }
    }

    public void populateListData(CheckoutCartBean reviewOrderBean) {
        // List<CheckoutCommerceItemBean>
        // commerceItems=reviewOrderBean.getCommerceItems();

        List<CheckoutCommerceItemBean> combinedList = new ArrayList<CheckoutCommerceItemBean>();
        if (null != reviewOrderBean.getCommerceItems()
                && null != reviewOrderBean.getElectronicGiftCardCommerceItems()) {
            combinedList.addAll(reviewOrderBean.getCommerceItems());
            combinedList.addAll(reviewOrderBean
                    .getElectronicGiftCardCommerceItems());
        } else if (null != reviewOrderBean.getCommerceItems()) {
            combinedList.addAll(reviewOrderBean.getCommerceItems());
        } else if (null != reviewOrderBean.getElectronicGiftCardCommerceItems()) {
            combinedList.addAll(reviewOrderBean
                    .getElectronicGiftCardCommerceItems());
        }
        view.setAdapter(new SubmitOrderFragmentAdapter(combinedList));
        /* setListAdapter(); */
    }

    /**
     * The Class SubmitOrderFragmentAdapter.
     */
    class SubmitOrderFragmentAdapter extends BaseAdapter {
        List<CheckoutCommerceItemBean> commerceItems;

        public SubmitOrderFragmentAdapter(
                List<CheckoutCommerceItemBean> commerceItems) {
            super();
            this.commerceItems = commerceItems;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return commerceItems.size();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int arg0) {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            CheckoutCommerceItemBean bean = commerceItems.get(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.submit_order_list_item, null);

            ImageView pdtImage = (ImageView) view
                    .findViewById(R.id.submitOrderListItemImg);
			/*
			 * ImageView giftImage=(ImageView)
			 * view.findViewById(R.id.submitOrderListItemGiftImg);
			 */

            TextView pdtName = (TextView) view
                    .findViewById(R.id.submitOrderListItemPdtName);
            TextView brand = (TextView) view
                    .findViewById(R.id.submitOrderListItemBrandName);
            TextView quantity = (TextView) view
                    .findViewById(R.id.submitOrderListItemQunatity);
            TextView price = (TextView) view
                    .findViewById(R.id.submitOrderListItemPrice);

            new AQuery(pdtImage).image(bean.getSmallImageUrl(), true, false,
                    200, R.drawable.dummy_product, null, AQuery.FADE_IN);
			/*
			 * if(bean.isGWP()){ price.setText("FREE");
			 * giftImage.setVisibility(View.VISIBLE);
			 * price.setTextColor(color.roseBudCherry); } else
			 * if(null!=bean.getIsFreeSample() &&
			 * bean.getIsFreeSample().equalsIgnoreCase("true")){
			 * price.setText("FREE"); price.setTextColor(color.roseBudCherry); }
			 */
			/* else{ */
            price.setText(String.format("%.2f",
                    Double.valueOf(bean.getAmount())));
			/* } */
            pdtName.setText(bean.getDisplayName());
            brand.setText(bean.getBrandName());
            quantity.setText("Quantity : " + bean.getQuantity());
            // price.setText(Double.valueOf(bean.getAmount()).toString());

            return view;
        }

    }

}
