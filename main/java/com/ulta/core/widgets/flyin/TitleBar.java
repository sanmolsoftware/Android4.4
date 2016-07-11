/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.widgets.flyin;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.util.log.Logger;

/**
 * The Class TitleBar.
 */
public class TitleBar extends LinearLayout implements OnClickListener {

	/** The context. */
	private Context context;

	/** The params. */
	LinearLayout.LayoutParams params;

	/** The main layout. */
	private LinearLayout mainLayout;

	/** The done icon. */
	private Button doneIcon;

	/** The menu icon. */
	private LinearLayout menuIcon;

	/** The title. */
	private TextView title;

	private ImageView logo;

	private LinearLayout menu;

	private TextView bagItemCount;

	private ImageView mSearchIcon;
	private ImageView mScanIcon;
	private FrameLayout mBasketIcon;
	private FrameLayout titleBarFrameLayout;

	/** The on done clicked listener. */
	private OnDoneClickedListener onDoneClickedListener;

	/** The on menu pressed listener. */
	private OnMenuPressedListener onMenuPressedListener;

	/** The on search clicked listener. */
	private OnSearchPressedListener onSearchClickedListener;

	/** The on scan pressed listener. */
	private OnScanPressedListener onScanPressedListener;

	/** The on bag pressed listener. */
	private OnBagPressedListener onBagPressedListener;

	/** The on title bar pressed listener. */
	private OnTitleBarPressed onTitleBarPressed;

	/**
	 * Instantiates a new title bar.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
		setCustomAttr(context, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.title_bar_done_icon:
			// start the main view of the module home,
			// if it is not currently on the screen.
			try {
				onDoneClickedListener = (OnDoneClickedListener) context;
				if (null != onDoneClickedListener) {
					onDoneClickedListener.onDoneClicked();
				}
			} catch (ClassCastException e) {
				throw new ClassCastException(context.getPackageName()
						.toString() + " must implement OnDoneClickedListener");
			}
			break;
		case R.id.title_bar_menu_icon:
			Logger.Log(">>> Menu Pressed TitleBAr");
			try {
				onMenuPressedListener = (OnMenuPressedListener) context;
				if (null != onMenuPressedListener) {
					onMenuPressedListener.onMenuPressed();
				}
			} catch (ClassCastException e) {
				throw new ClassCastException(context.getPackageName()
						.toString() + " must implement OnMenuPressedListener");
			}
			break;

		case R.id.title_searchIcon:
			Logger.Log(">>> Menu Pressed SearchIcon");
			try {
				onSearchClickedListener = (OnSearchPressedListener) context;
				if (null != onSearchClickedListener) {
					onSearchClickedListener.onSearchPressed();
				}
			} catch (ClassCastException e) {
				throw new ClassCastException(context.getPackageName()
						.toString() + " must implement OnSearchPressedListener");
			}
			break;

		case R.id.title_scanIcon:
			Logger.Log(">>> Scan Pressed SearchIcon");
			try {
				onScanPressedListener = (OnScanPressedListener) context;
				if (null != onScanPressedListener) {
					onScanPressedListener.onScanPressed();
				}
			} catch (ClassCastException e) {
				throw new ClassCastException(context.getPackageName()
						.toString() + " must implement onScanPressedListener");
			}
			break;

		case R.id.title_bagIcon:
			Logger.Log(">>> Scan Pressed SearchIcon");
			try {
				onBagPressedListener = (OnBagPressedListener) context;
				if (null != onBagPressedListener) {
					onBagPressedListener.onBagPressed();
				}
			} catch (ClassCastException e) {
				throw new ClassCastException(context.getPackageName()
						.toString() + " must implement onBagPressedListener");
			}
			break;

		case R.id.title_bar_layout:
			Logger.Log(">>> title bar Pressed SearchIcon");
			try {
				onTitleBarPressed = (OnTitleBarPressed) context;
				if (null != onBagPressedListener) {
					onTitleBarPressed.onTitleBarPressed();
				}
			} catch (ClassCastException e) {
				throw new ClassCastException(context.getPackageName()
						.toString() + " must implement onBagPressedListener");
			}
			break;
		}
	}

	/**
	 * Sets the custom attr.
	 * 
	 * @param context2
	 *            the context2
	 * @param attrs
	 *            the attrs
	 */
	private void setCustomAttr(Context context2, AttributeSet attrs) {
		final TypedArray a = context2.obtainStyledAttributes(attrs,
				R.styleable.TitleBar);
		final String customAttr = a
				.getString(R.styleable.TitleBar_done_visible);
		final String customAttrTitle = a.getString(R.styleable.TitleBar_title_ulta);
		final String customAttrLogo = a.getString(R.styleable.TitleBar_logo_ulta);
		final String customAttrMenu = a.getString(R.styleable.TitleBar_menu_option);
		setMenu(context2, customAttrMenu);
		setLogo(context2, customAttrLogo);
		setTitle(context2, customAttrTitle);
		setCustomAttr(context2, customAttr);
		a.recycle();

	}

	public boolean setMenu(Context context2, String customAttrLogo) {
		if ("true".equals(customAttrLogo)) {
			menu.setVisibility(View.VISIBLE);
		} else if ("false".equals(customAttrLogo)) {
			menu.setVisibility(View.GONE);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(10, 0, 0, 0);
			title.setLayoutParams(params);
			params.gravity = Gravity.CENTER_VERTICAL;
		}

		return true;

	}

	public void disableDone() {
		doneIcon.setVisibility(View.GONE);

	}
	
	public void disableBagIcon(){
		mBasketIcon.setVisibility(View.GONE);
	}

	public void setUploadButtonText() {
		doneIcon.setText("Upload");
	}

	public void enableDone() {
		doneIcon.setVisibility(View.VISIBLE);
	}

	private boolean setLogo(Context context2, String customAttrLogo) {
		if ("true".equals(customAttrLogo)) {
			logo.setVisibility(View.VISIBLE);
		} else if ("false".equals(customAttrLogo)) {
			logo.setVisibility(View.GONE);
		}

		return true;

	}

	/**
	 * Sets the title.
	 * 
	 * @param context2
	 *            the context2
	 * @param customAttrTitle
	 *            the custom attr title
	 * @return true, if successful
	 */
	private boolean setTitle(Context context2, String customAttrTitle) {
		if (null != customAttrTitle) {
			title.setText(customAttrTitle);
		}

		return true;

	}

	/**
	 * Sets the title.
	 * 
	 * @param customAttrTitle
	 *            the custom attr title
	 * @return true, if successful
	 */
	public boolean setTitle(String customAttrTitle) {

		if (null == title) {
			throw new RuntimeException(
					context.getPackageName().toString()
							+ "Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
		} else {
			title.setText(customAttrTitle);
		}

		return true;

	}

	/**
	 * set basket count
	 */

	public void setBasketCount(int count) {
		if (count == 0) {
			bagItemCount.setVisibility(View.GONE);
		} else {
			if(count>99)
			{
				bagItemCount.setText("99+");
			}
			else
			{
				bagItemCount.setText("" + count);
			}
			bagItemCount.setVisibility(View.VISIBLE);
			
		}
	}

	/**
	 * Sets the custom attr.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param asset
	 *            the asset
	 * @return true, if successful
	 */
	public boolean setCustomAttr(Context ctx, String asset) {
		if ("true".equals(asset)) {
			// doneIcon.setVisibility(View.VISIBLE);
			enableDone();
		} else if ("false".equals(asset)) {
			// doneIcon.setVisibility(View.GONE);
			disableDone();
		}

		return true;
	}

	/**
	 * Called when a title bar is instantiated. Initial configuration are done.
	 */
	private void init() {

		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		inflateLayout();
		addView(mainLayout);
	}

	/**
	 * Inflates the tab bar layout. Instantiate the icons on the tab bar.
	 */
	private void inflateLayout() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (inflater != null) {
			mainLayout = (LinearLayout) inflater.inflate(R.layout.title_bar,
					null);
			logo = (ImageView) mainLayout.findViewById(R.id.title_bar_logo);
			menu = (LinearLayout) mainLayout
					.findViewById(R.id.title_bar_menu_icon);
			title = (TextView) mainLayout.findViewById(R.id.title_bar_title);
			doneIcon = (Button) mainLayout
					.findViewById(R.id.title_bar_done_icon);
			mSearchIcon = (ImageView) mainLayout
					.findViewById(R.id.title_searchIcon);
			mScanIcon = (ImageView) mainLayout
					.findViewById(R.id.title_scanIcon);
			mBasketIcon = (FrameLayout) mainLayout
					.findViewById(R.id.title_bagIcon);
			bagItemCount = (TextView) mainLayout
					.findViewById(R.id.basketCountTextView);
			titleBarFrameLayout = (FrameLayout) mainLayout
					.findViewById(R.id.title_bar_layout);
			if (null != doneIcon) {
				doneIcon.setOnClickListener(this);
			}
			menuIcon = (LinearLayout) mainLayout
					.findViewById(R.id.title_bar_menu_icon);
			if (null != menuIcon) {
				menuIcon.setOnClickListener(this);
			}
			if (null != mSearchIcon) {
				mSearchIcon.setOnClickListener(this);
			}
			if (null != mScanIcon) {
				mScanIcon.setOnClickListener(this);
			}
			if (null != mBasketIcon) {
				mBasketIcon.setOnClickListener(this);
			}
			if (null != titleBarFrameLayout) {
				titleBarFrameLayout.setOnClickListener(this);
			}
		}
		mainLayout.setLayoutParams(params);
	}

	/**
	 * Sets the on done clicked listener.
	 * 
	 * @param onDoneClickedListener
	 *            the new on done clicked listener
	 */
	public void setOnDoneClickedListener(
			OnDoneClickedListener onDoneClickedListener) {
		this.onDoneClickedListener = onDoneClickedListener;
	}

	public void displayCheckoutButton() {
		doneIcon.setText("Checkout");
	}

	public void displayNextButton() {
		doneIcon.setText("Next");
	}

	public void displayPreviewButton() {
		doneIcon.setText("Preview");
	}

	public void displayContinueButton() {
		doneIcon.setText("Continue");
	}

	public void displayApplyButton() {
		doneIcon.setText("Apply");
	}

	public void displayDoneButton() {
		doneIcon.setText("Done");
	}
}
