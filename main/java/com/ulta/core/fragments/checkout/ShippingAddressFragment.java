package com.ulta.core.fragments.checkout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.R.color;
import com.ulta.core.activity.checkout.PaymentMethodActivity;
import com.ulta.core.util.log.Logger;

//import android.widget.Toast;

public class ShippingAddressFragment extends Fragment {

	/** The Constant RADIO_BTN_ID_INDEX. */
	public final static int RADIO_BTN_ID_INDEX = 100;

	/** The context. */
	Context context;

	/** The radiobtn id. */
	private int radiobtnId = RADIO_BTN_ID_INDEX;

	private int defaultShippingAddressId = 0;

	/** The radio group. */
	private RadioGroup radioGroup;
	private RadioGroup defaultRadioGroup;

	/** The selected address. */
	private String selectedAddress;

	private int checkedId;
	private boolean isPaymentPage = false;
	private TextView select_different_address;
	private int defaultRadioButtonId;
	private boolean firstTime = true;

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
		View view = layoutInflater.inflate(R.layout.shipping_address_fragment,
				null);
		radioGroup = (RadioGroup) view.findViewById(R.id.addresses_radiogroup);
		defaultRadioGroup = (RadioGroup) view
				.findViewById(R.id.default_addresses_radiogroup);
		select_different_address = (TextView) view
				.findViewById(R.id.select_different_address);
		select_different_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						Drawable img = null;
				if (radioGroup.getVisibility() == View.VISIBLE) {
					radioGroup.setVisibility(View.GONE);
					img = getResources()
							.getDrawable(R.drawable.shop_arrow_down);
					img.setBounds(0, 0, 30, 30);
					// select_different_address.setCompoundDrawables(null, null,
					// R.drawable.arrow_down, null);
					select_different_address.setCompoundDrawables(null, null,
							img, null);

				} else {
					img = getResources().getDrawable(R.drawable.shop_arrow_up);
					img.setBounds(0, 0, 30, 30);
					select_different_address.setCompoundDrawables(null, null,
							img, null);

					radioGroup.setVisibility(View.VISIBLE);
				}
			}
		});

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
	public void addDefaultAddressRow(String name, String address1) {
		try {
			RadioButton radioButton = new RadioButton(context);
			radioButton.setGravity(Gravity.CENTER_VERTICAL);
			Drawable drawable = getResources().getDrawable(
					R.drawable.custom_btn_radio);
			drawable.setBounds(0, 0, 72, 72);
			radioButton.setButtonDrawable(android.R.color.transparent);
			radioButton.setCompoundDrawables(null, null, drawable, null);
			radioButton.setPadding(25, 25, 3, 10);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			radioButton.setLayoutParams(params);
			radioButton.setText(name + "\n" + address1);
			radioButton.setId(radiobtnId);
			defaultRadioButtonId = radiobtnId;
			radioButton.setChecked(true);
			setCheckedId(radiobtnId);
			radioButton.setTextColor(getResources().getColor(
					R.color.shipping_address_caption));
			radiobtnId++;
			defaultRadioGroup.addView(radioButton);
			defaultRadioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {

							RadioButton radioButton = (RadioButton) group
									.findViewById(checkedId);

							if (radioButton.isChecked()) {
								// Toast.makeText(context, "check" + checkedId,
								// Toast.LENGTH_SHORT).show();
								Logger.Log(">>> onCheckedChanged " + checkedId);
								setSelectedAddress(radioButton.getText()
										.toString());
								setCheckedId(checkedId);
								if (isPaymentPage) {
									PaymentMethodActivity payment = (PaymentMethodActivity) context;
									payment.setFilterForsecurityCode(checkedId);
								}
							} else {
								// Toast.makeText(
								// context,
								// radioGroup.getCheckedRadioButtonId()
								// + "uncheck" + checkedId,
								// Toast.LENGTH_SHORT).show();
								if (firstTime) {
									firstTime = false;
								} else {
									firstTime = true;
									radioGroup.clearCheck();
									radioButton.setChecked(true);
									setCheckedId(checkedId);
								}
								/*
								 * if (radioGroup.getCheckedRadioButtonId() ==
								 * -1) { radioButton.setChecked(true); }
								 */
							}
						}
					});

			// if (radioButton.getId() == 100 + getDefaultShippingAddressId()) {
			// radioButton.setChecked(true);
			// }
			addLine();// Add line between each row
			// RadioButton radioButtonDummy = new RadioButton(context);
			// radioButtonDummy.setVisibility(View.INVISIBLE);
			// defaultRadioGroup.addView(radioButtonDummy);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds the new row.
	 *
	 * @param name
	 *            the name
	 * @param address1
	 *            the address1
	 */
	public void addNewRow(String name, String address1) {
		select_different_address.setVisibility(View.VISIBLE);
		try {
			RadioButton radioButton = new RadioButton(context);
			radioButton.setGravity(Gravity.CENTER_VERTICAL);
			Drawable drawable = getResources().getDrawable(
					R.drawable.custom_btn_radio);
			drawable.setBounds(0, 0, 72, 72);
			radioButton.setButtonDrawable(android.R.color.transparent);
			radioButton.setCompoundDrawables(null, null, drawable, null);
			radioButton.setPadding(25, 25, 3, 10);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			radioButton.setLayoutParams(params);
			radioButton.setText(name + "\n" + address1);
			radioButton.setId(radiobtnId);
			radioButton.setTextColor(getResources().getColor(
					R.color.shipping_different_address));
			radiobtnId++;
			radioGroup.addView(radioButton);
			radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// Toast.makeText(context, "check",
							// Toast.LENGTH_SHORT)
							// .show();
							RadioButton radioButton = (RadioButton) group
									.findViewById(checkedId);
							if (null != radioButton) {
								if (radioButton.isChecked()) {
									RadioButton defaultradioButton = ((RadioButton) defaultRadioGroup
											.findViewById(defaultRadioGroup
													.getCheckedRadioButtonId()));
									if (null != defaultradioButton) {
										defaultradioButton.setChecked(false);
									}
									Logger.Log(">>> onCheckedChanged "
											+ checkedId);
									setSelectedAddress(radioButton.getText()
											.toString());
									setCheckedId(checkedId);
									if (isPaymentPage) {
										PaymentMethodActivity payment = (PaymentMethodActivity) context;
										payment.setFilterForsecurityCode(checkedId);
									}
								}
							}
						}
					});
			// if (radioButton.getId() == 100 + getDefaultShippingAddressId()) {
			// radioButton.setChecked(true);
			// }
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
}
