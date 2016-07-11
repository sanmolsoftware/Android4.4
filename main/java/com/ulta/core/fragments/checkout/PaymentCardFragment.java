/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */
package com.ulta.core.fragments.checkout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ulta.R;
import com.ulta.R.color;
import com.ulta.core.activity.checkout.PaymentMethodActivity;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;
import com.ulta.core.util.log.Logger;

import java.util.List;

/**
 * The Class AddressFragment.
 */
public class PaymentCardFragment extends Fragment {

    /** The Constant RADIO_BTN_ID_INDEX. */
    public final static int RADIO_BTN_ID_INDEX = 100;

    /** The context. */
    Context context;

    /** The radiobtn id. */
    private int radiobtnId = RADIO_BTN_ID_INDEX;

    private int defaultShippingAddressId = 0;

    List<PaymentDetailsBean> creditCardsList;

    /** The radio group. */
    private RadioGroup radioGroup;

    /** The selected address. */
    private String selectedAddress;

    private int checkedId;
    private boolean isPaymentPage = false;

    public void setCreditCardsList(List<PaymentDetailsBean> creditCardsList) {
        this.creditCardsList = creditCardsList;
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

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.address_fragment, null);
        radioGroup = (RadioGroup) view.findViewById(R.id.addresses_radiogroup);

        return view;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void addText(String text) {
        TextView tv = new TextView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setTextAppearance(context, R.style.RiverBedRegular18);
        tv.setTextSize(16);
        tv.setPadding(30, 0, 0, 0);
        radioGroup.addView(tv);
    }

    /**
     * Add Line between each row
     */
    public void addLine() {
        View lineView = new View(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        lineView.setLayoutParams(params);
        lineView.setBackgroundResource(R.drawable.divider);
        radioGroup.addView(lineView);
    }

    /**
     * Adds the new row.
     *
     * @param name
     *            the name
     * @param address1
     *            the address1
     */
    public void addNewRow(String name, String address1, boolean defaultValue, String cardNumber) {
        try {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setGravity(Gravity.CENTER_VERTICAL);
            Drawable drawableRight = getResources().getDrawable(
                    R.drawable.custom_btn_radio);
            drawableRight.setBounds(0, 0, 72, 72);
            radioButton.setButtonDrawable(android.R.color.transparent);
            Drawable drawableLeft = getResources().getDrawable(
                    R.drawable.creditcard_default);
            drawableLeft.setBounds(0, 0, 90, 72);
            radioButton.setCompoundDrawables(drawableLeft, null, drawableRight, null);
            radioButton.setPadding(25, 25, 3, 10);
            PaymentMethodActivity payment = (PaymentMethodActivity) context;
            CreditCardInfoBean cardInfoBean = payment.identifyCardType(cardNumber);
            if (null != cardInfoBean) {
                LoadImageFromWebOperations(cardInfoBean.getCardImage(), radioButton);
            }
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(params);
            radioButton.setText(name + "\n" + address1);
            radioButton.setId(radiobtnId);
            radioButton.setTextColor(Color.BLACK);
            radiobtnId++;
            radioGroup.addView(radioButton);
            radioGroup
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group,
                                                     int checkedId) {
                            RadioButton radioButton = (RadioButton) group
                                    .findViewById(checkedId);
                            if (radioButton.isChecked()) {
                                Logger.Log(">>> onCheckedChanged " + checkedId);
                                setSelectedAddress(radioButton.getText()
                                        .toString());
                                setCheckedId(checkedId);
                                if (isPaymentPage) {
                                    int idCreditCard = checkedId - 100;
                                    PaymentMethodActivity payment = (PaymentMethodActivity) context;
                                    payment.setFilterForsecurityCode(checkedId);
                                    if (payment.checkIfExpirationNeeded(creditCardsList.get(idCreditCard).getCreditCardType())) {
                                        if (payment.isCardExpired(
                                                creditCardsList.get(idCreditCard)
                                                        .getExpirationYear().trim(),
                                                Integer.parseInt(creditCardsList
                                                        .get(idCreditCard)
                                                        .getExpirationMonth()
                                                        .trim()))) {
                                            payment.showExpiryDialog();
                                            String radioButtonText = radioButton.getText().toString();
                                            Spannable radioButtonTextMsg = new SpannableString(radioButtonText);

                                            radioButtonTextMsg.setSpan(
                                                    new ForegroundColorSpan(
                                                            Color.RED),
                                                    radioButtonText.indexOf("-") + 1,
                                                    radioButtonTextMsg
                                                            .length(),
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            radioButton.setText(radioButtonTextMsg);
                                        }
                                    }
                                    payment.hideOrShowSecurityForSavedCards(creditCardsList
                                            .get(idCreditCard).getCreditCardNumber());
                                }
                            }
                        }
                    });
            if (defaultValue) {
                radioButton.setChecked(true);
            }
            /*
			 * if(radioButton.getId()== 100+getDefaultShippingAddressId()) {
			 * radioButton.setChecked(true); }
			 */
            addLine();// Add line between each row
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * Set default Shipping address Id
     *
     * @param defaultShippingAddressId
     */
    public void setDefaultShippingAddressId(int defaultShippingAddressId) {
        this.defaultShippingAddressId = defaultShippingAddressId;
    }

    /**
     * Get Default SHipping Address ID
     *
     * @return
     */
    public int getDefaultShippingAddressId() {
        return defaultShippingAddressId;
    }

    /**
     * Get Checked ID
     *
     * @return
     */
    public int getCheckedId() {
        return checkedId;
    }

    /**
     * Set Checked ID
     *
     * @param checkedId
     */
    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }

    public boolean isPaymentPage() {
        return isPaymentPage;
    }

    public void setPaymentPage(boolean isPaymentPage) {
        this.isPaymentPage = isPaymentPage;
    }

    /**
     * Download image from url using Aquery and set to the card type
     *
     * @param url
     */
    public void LoadImageFromWebOperations(String url, final RadioButton radioButton) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            AQuery androidQuery = new AQuery(context);
            androidQuery.ajax(url, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap bitmap, AjaxStatus status) {
                    Drawable drawableLeft = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 62, false));
                    drawableLeft.setBounds(0, 0, 90, 72);
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.custom_btn_radio);
                    drawableRight.setBounds(0, 0, 72, 72);
                    radioButton.setCompoundDrawables(drawableLeft, null, drawableRight, null);
                    radioButton.setCompoundDrawablePadding(15);
                }
            });
        } catch (Exception e) {
            Logger.Log("<UltaException>>"
                    + e);
        }
    }
}
