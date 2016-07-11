package com.ulta.core.fragments.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ulta.R;
import com.ulta.core.activity.account.MyOrderDetailsActivity;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.checkout.CheckoutCartBean;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.List;

public class OrderStatusAddressFragment extends Fragment {
    View addressView;


    /**
     * The shipping address layout.
     */
    LinearLayout shippingAddressLayout;
    /**
     * The review order bean.
     */
    CheckoutCartBean reviewOrderBean;

    /**
     * The billing address layout.
     */
    LinearLayout billingAddressLayout, reviewOrderShipping_method;
    /**
     * The str shipping postal code.
     */
    String strShippingFirstName = "", strShippingLastName = "",
            strShippingAddressLine1 = "", strShippingState = "",
            strShippingCity = "", strShippingPostalCode = "";

    /**
     * The str billing postal code.
     */
    String strBillingFirstName = "", strBillingLastName = "",
            strBillingAddressLine1 = "", strBillingState = "",
            strBillingCity = "", strBillingPostalCode = "";
    /**
     * The str card expiry year.
     */
    String strCardType = "", strCardNumber = "", strCardExpiryMonth = "",
            strCardExpiryYear = "", strCreditCardType = "";

    /**
     * The text card details line2.
     */
    TextView textShippingAddressLine1, textShippingAddressLine2,
            textBillingAddressLine1, textBillingAddressLine2,
            textCardDetailsLine1, textCardDetailsLine2, txtShippingMethod;
    ImageView cardTypeImage;

    /**
     * The context.
     */
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addressView = inflater.inflate(R.layout.order_details_address_fragment,
                null);
        initViews(addressView);
        setAddress();
        return addressView;
    }


    public OrderStatusAddressFragment(CheckoutCartBean object, Context context) {
        reviewOrderBean = (CheckoutCartBean) object;
        this.context = context;
    }


    private void initViews(View addressView) {
        shippingAddressLayout = (LinearLayout) addressView
                .findViewById(R.id.reviewOrderShippningAddressLayout_fragment);
        textShippingAddressLine1 = (TextView) addressView
                .findViewById(R.id.txtShippingAddress1_fragment);
        textShippingAddressLine2 = (TextView) addressView
                .findViewById(R.id.txtShippingAddress2_fragment);
        textBillingAddressLine1 = (TextView) addressView
                .findViewById(R.id.txtBillingAddress1_fragment);
        textBillingAddressLine2 = (TextView) addressView
                .findViewById(R.id.txtBillingAddress2_fragment);
        textCardDetailsLine1 = (TextView) addressView
                .findViewById(R.id.txtCardDetails1_fragment);
        textCardDetailsLine2 = (TextView) addressView
                .findViewById(R.id.txtCardDetails2_fragment);
        billingAddressLayout = (LinearLayout) addressView
                .findViewById(R.id.reviewOrderBillingAddressLayout_fragment);
        txtShippingMethod = (TextView) addressView
                .findViewById(R.id.txtShippingMethod);
        reviewOrderShipping_method = (LinearLayout) addressView
                .findViewById(R.id.reviewOrderShipping_method);
        cardTypeImage = (ImageView) addressView.findViewById(R.id.cardTypeImage);
    }

    /**
     * Sets the address.
     */
    private void setAddress() {
        cardTypeImage.setVisibility(View.GONE);
        MyOrderDetailsActivity orderDetailsActivity = (MyOrderDetailsActivity) context;
        if (UltaDataCache.getDataCacheInstance().isOnlyEgiftCard()) {
            shippingAddressLayout.setVisibility(View.GONE);
        }
        if (null != reviewOrderBean.getHardGoodShippingGroups()
                && !reviewOrderBean.getHardGoodShippingGroups().isEmpty()) {
            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getFirstName())
                strShippingFirstName = reviewOrderBean
                        .getHardGoodShippingGroups().get(0).getFirstName();
            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getLastName())
                strShippingLastName = reviewOrderBean
                        .getHardGoodShippingGroups().get(0).getLastName();
            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getAddress1())
                strShippingAddressLine1 = reviewOrderBean
                        .getHardGoodShippingGroups().get(0).getAddress1();
            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getState())
                strShippingState = reviewOrderBean.getHardGoodShippingGroups()
                        .get(0).getState();
            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getCity())
                strShippingCity = reviewOrderBean.getHardGoodShippingGroups()
                        .get(0).getCity();
            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getPostalCode())
                strShippingPostalCode = reviewOrderBean
                        .getHardGoodShippingGroups().get(0).getPostalCode();

            textShippingAddressLine1.setText(strShippingFirstName + " "
                    + strShippingLastName);
            textShippingAddressLine2.setText(strShippingAddressLine1 + " "
                    + strShippingCity + " " + strShippingState + " "
                    + strShippingPostalCode);

            if (null != reviewOrderBean.getHardGoodShippingGroups().get(0)
                    .getShippingMethod()) {
                String shippingMethod = "Free Shipping";
                if (reviewOrderBean.getHardGoodShippingGroups().get(0)
                        .getShippingMethod().equals("ups_next_day")) {
                    shippingMethod = "UPS Next Day Air";
                } else if (reviewOrderBean.getHardGoodShippingGroups().get(0)
                        .getShippingMethod().equals("ups_second_day")) {
                    shippingMethod = "UPS Next Day Air";
                } else {
                    shippingMethod = "Standard Ground Shipping";
                }
                txtShippingMethod.setText(shippingMethod);
                reviewOrderShipping_method.setVisibility(View.VISIBLE);
            }
        }
        if (null != reviewOrderBean.getCreditCardPaymentGroups()
                && !(reviewOrderBean.getCreditCardPaymentGroups().isEmpty())) {
            billingAddressLayout.setVisibility(View.VISIBLE);
            strBillingFirstName = reviewOrderBean.getCreditCardPaymentGroups()
                    .get(0).getFirstName();
            if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                    .getLastName())
                strBillingLastName = reviewOrderBean
                        .getCreditCardPaymentGroups().get(0).getLastName();
            if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                    .getAddress1())
                strBillingAddressLine1 = reviewOrderBean
                        .getCreditCardPaymentGroups().get(0).getAddress1();
            if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                    .getCity())
                strBillingCity = reviewOrderBean.getCreditCardPaymentGroups()
                        .get(0).getCity();
            if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                    .getState())
                strBillingState = reviewOrderBean.getCreditCardPaymentGroups()
                        .get(0).getState();
            if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                    .getPostalCode())
                strBillingPostalCode = reviewOrderBean
                        .getCreditCardPaymentGroups().get(0).getPostalCode();
            textBillingAddressLine1.setText(strBillingFirstName + " "
                    + strBillingLastName);
            textBillingAddressLine2.setText(strBillingAddressLine1 + " "
                    + strBillingCity + " " + strBillingState + " "
                    + strBillingPostalCode);
            if (null != reviewOrderBean.getGiftCardPaymentGroups()
                    && !reviewOrderBean.getGiftCardPaymentGroups().isEmpty()) {
                strCardType = reviewOrderBean.getGiftCardPaymentGroups().get(0)
                        .getPaymentMethod();
                strCardNumber = reviewOrderBean.getGiftCardPaymentGroups()
                        .get(0).getGiftCardNumber();

                strCardType = getCardtype(strCardType);

                textCardDetailsLine1.setText(strCardType
                        + " "
                        + strCardNumber
                        + "\n"
                        + "Amount : "
                        + "$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean
                                .getGiftCardPaymentGroups().get(0)
                                .getAmount())));
                // This method will get the new values to variables from credit
                // card details
                getCreditCardInfo();
                cardTypeImage.setVisibility(View.VISIBLE);
                if (null != getCardFromType(strCreditCardType)) {
                    LoadImageFromWebOperations(getCardFromType(strCreditCardType).getCardImage());
                }
                textCardDetailsLine2.setText(strCardType
                        + " : "
                        + strCardNumber
                        + "\n"
                        + "Amount : "
                        + "$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean
                                .getCreditCardPaymentGroups().get(0)
                                .getAmount())));

            } else {
                // This method will get the new values to variables from credit
                // card details
                getCreditCardInfo();
                cardTypeImage.setVisibility(View.VISIBLE);
                if (null != getCardFromType(strCreditCardType)) {
                    LoadImageFromWebOperations(getCardFromType(strCreditCardType).getCardImage());
                }
                textCardDetailsLine1.setText(strCardType
                        + " : "
                        + strCardNumber
                        + "\n"
                        + "Amount : "
                        + "$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean
                                .getCreditCardPaymentGroups().get(0)
                                .getAmount())));
                textCardDetailsLine2.setVisibility(View.GONE);

            }
        } else if (reviewOrderBean.getCreditCardPaymentGroups() != null
                && reviewOrderBean.getCreditCardPaymentGroups().isEmpty()
                && null != reviewOrderBean.getGiftCardPaymentGroups()
                && !(reviewOrderBean.getGiftCardPaymentGroups().isEmpty())) {
            billingAddressLayout.setVisibility(View.GONE);
            strCardType = reviewOrderBean.getGiftCardPaymentGroups().get(0)
                    .getPaymentMethod();
            cardTypeImage.setVisibility(View.VISIBLE);
            cardTypeImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.giftcard_image,null));
            strCardType = getCardtype(strCardType);
            strCardNumber = reviewOrderBean.getGiftCardPaymentGroups().get(0)
                    .getGiftCardNumber();
            textCardDetailsLine1.setText(strCardType
                    + " "
                    + strCardNumber
                    + "\n"
                    + "Amount : "
                    + "$"
                    + String.format(
                    "%.2f",
                    Double.valueOf(reviewOrderBean
                            .getGiftCardPaymentGroups().get(0)
                            .getAmount())));
            if (reviewOrderBean.getPaypalPaymentGroups() != null
                    && !reviewOrderBean.getPaypalPaymentGroups().isEmpty()) {
                textCardDetailsLine1.append("\n"
                        + "PayPal "
                        + reviewOrderBean.getPaypalPaymentGroups().get(0)
                        .getEmailId()
                        + "\n"
                        + "Amount : "
                        + "$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean
                                .getPaypalPaymentGroups().get(0)
                                .getAmount())));

            }

            textCardDetailsLine2.setVisibility(View.GONE);

            textCardDetailsLine2.setVisibility(View.GONE);
        } else if (reviewOrderBean.getPaypalPaymentGroups() != null
                && !reviewOrderBean.getPaypalPaymentGroups().isEmpty()) {
            // paypalImage.setVisibility(View.VISIBLE);
            billingAddressLayout.setVisibility(View.GONE);
            textCardDetailsLine1.setText("PayPal Account");
            if (reviewOrderBean.getPaypalPaymentGroups().get(0).getEmailId() != null
                    && !reviewOrderBean.getPaypalPaymentGroups().get(0)
                    .getEmailId().isEmpty()) {
                textCardDetailsLine2.setText(reviewOrderBean
                        .getPaypalPaymentGroups().get(0).getEmailId());

                if (null != reviewOrderBean.getPaypalPaymentGroups().get(0)) {
                    if (null != reviewOrderBean.getPaypalPaymentGroups().get(0)
                            .getAmount()) {
                        textCardDetailsLine2.append("\n"
                                + "Amount : "
                                + "$"
                                + String.format(
                                "%.2f",
                                Double.valueOf(reviewOrderBean
                                        .getPaypalPaymentGroups()
                                        .get(0).getAmount())));
                    }

                }
            }
        }
    }

    private String getCardtype(String strCardTypeValue) {
        if (strCardTypeValue.equalsIgnoreCase("creditcard"))
            strCardTypeValue = "Credit Card";
        return strCardTypeValue;
    }

    private void getCreditCardInfo() {
        strCardType = reviewOrderBean.getCreditCardPaymentGroups().get(0)
                .getPaymentMethod();

        strCardType = getCardtype(strCardType);

        if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                .getCreditCardNumber()) {
            String numberOfCross = "";
            strCardNumber = reviewOrderBean.getCreditCardPaymentGroups().get(0)
                    .getCreditCardNumber();
            for (int i = 0; i < strCardNumber.length() - 4; i++) {
                numberOfCross = numberOfCross + "x";
            }
            strCardNumber = numberOfCross
                    + strCardNumber.substring(strCardNumber.length() - 4,
                    strCardNumber.length());
        }
        if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                .getExpirationMonth())
            strCardExpiryMonth = reviewOrderBean.getCreditCardPaymentGroups()
                    .get(0).getExpirationMonth();
        if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                .getExpirationYear())
            strCardExpiryYear = reviewOrderBean.getCreditCardPaymentGroups()
                    .get(0).getExpirationYear();
        if (null != reviewOrderBean.getCreditCardPaymentGroups().get(0)
                .getCreditCardType()) {
            strCreditCardType = reviewOrderBean.getCreditCardPaymentGroups()
                    .get(0).getCreditCardType();
        }
    }

    /**
     * Download image from url using Aquery and set to the card type
     *
     * @param url
     */
    public void LoadImageFromWebOperations(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            AQuery androidQuery = new AQuery(context);
            androidQuery.ajax(url, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap bitmap, AjaxStatus status) {
//                    cardTypeImage.setImageBitmap(bitmap);
                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 62, false));
                    cardTypeImage.setImageDrawable(drawable);
                }
            });
        } catch (Exception e) {
            Logger.Log("<UltaException>>"
                    + e);
        }
    }

    /**
     * General class which will identify the card from the card type
     *
     * @param cardType
     * @return
     */
    public CreditCardInfoBean getCardFromType(String cardType) {
        CreditCardInfoBean singleCardInfo = null,identifiedCardBean=null;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo) {
                        if (cardType.equalsIgnoreCase(singleCardInfo.getCardType())) {
                            identifiedCardBean=singleCardInfo;
                        }
                    }
                }
            }
        }
        return identifiedCardBean;
    }
}
