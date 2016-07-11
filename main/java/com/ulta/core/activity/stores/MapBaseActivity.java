/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.activity.stores;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentResult;
import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.OnLogout;
import com.ulta.core.activity.SearchActivity;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.about.AboutUsActivity;
import com.ulta.core.activity.about.LegalActivity;
import com.ulta.core.activity.about.PrivacyPolicyActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.account.MyAccountActivity;
import com.ulta.core.activity.account.MyOrderHistoryActivity;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.myprofile.FavoritesActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.activity.product.UltaProductListActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.activity.rewards.GiftCardsTabActivity;
import com.ulta.core.activity.rewards.NonSignedInRewardsActivity;
import com.ulta.core.bean.StatusOnlyResponseBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.OmnitureTracking;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.util.scan.ScanIntentIntegrator;
import com.ulta.core.widgets.flyin.OnBagPressedListener;
import com.ulta.core.widgets.flyin.OnMenuPressedListener;
import com.ulta.core.widgets.flyin.OnScanPressedListener;
import com.ulta.core.widgets.flyin.OnSearchPressedListener;
import com.ulta.core.widgets.flyin.OnTitleBarPressed;
import com.ulta.core.widgets.flyin.TitleBar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.displayUserErrorMessage;

/**
 * The Class MapBaseActivity.
 */
public class MapBaseActivity extends FragmentActivity implements
		OnMenuPressedListener, OnItemClickListener, OnLogout,
		OnSearchPressedListener, OnScanPressedListener, OnBagPressedListener,
		OnTitleBarPressed {
	private Typeface helveticaRegularTypeface;
	/** The Constant OPTION_HOME. */
	private static final int OPTION_HOME = 1;

	/** The Constant OPTION_SCAN. */
	private static final int OPTION_FAVORITES = 2;

	/** The Constant OPTION_SHOP. */
	private static final int OPTION_SHOP = 3;

	/** The Constant OPTION_STORE. */
	private static final int OPTION_STORE = 4;

	/** The Constant OPTION_BASKET. */
	private static final int OPTION_BASKET = 7;

	/**
	 * 3.2 release OPTION_CHECK_ORDER_STATUS added for order status for
	 * anonymous user
	 */
	/** The Constant OPTION_GIFT_CARD. */
	private static final int OPTION_GIFT_CARD_BAL = 5;

	/** The Constant OPTION_GIFT_CARD. */
	private static final int OPTION_CHECK_ORDER_STATUS = 6;

	/** The Constant OPTION_BEAUTY_LIST. */
	private static final int OPTION_BEAUTY_LIST = 25;

	/** The Constant OPTION_MY_ACCOUNT. */
	private static final int OPTION_MY_ACCOUNT = 9;

	/** The Constant OPTION_HELP_AND_ABOUT_US. */
	private static final int OPTION_HELP_AND_ABOUT_US = 10;

	/** The Constant OPTION_CONTACT_US. */
	private static final int OPTION_CONTACT_US = 11;

	/** The Constant OPTION_USER_AGREEMENT. */
	private static final int OPTION_USER_AGREEMENT = 12;

	/** The Constant OPTION_PRIVACY_POLICY. */
	private static final int OPTION_PRIVACY_POLICY = 13;

	private static final int OPTION_CALL_SERVIE = 14;
	/** The Constant OPTION_LOGOUT. */
	private static final int OPTION_LOGOUT = 15;

	/** The title bar. */
	TitleBar titleBar;

	/** The pd. */
	private ProgressDialog pd;

	SeparatedListAdapter adapter;
	CustomAdapter miscAdapter;
	private static int itemCountInBasket;
	private static int activityIndicator = 0;
	// private EditText txtSearch;
	private SharedPreferences staySignedInSharedPreferences;
	private Editor staySignedInEditor;
	private UltaBaseActivity activity;
	// navigation drawer
	DrawerLayout drawerLayout;
	LinearLayout navigationDrawer;
	MapBaseActivity activityMap;

	public MapBaseActivity getActivityMap() {
		return activityMap;
	}

	public void setActivityMap(MapBaseActivity activityMap) {
		this.activityMap = activityMap;
	}

	@Override
	protected void onResume() {
		// invalidateSideMenu();
		setBasketCount(UltaDataCache.getDataCacheInstance().getItemsInBasket());
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.suse.android.sidemenu.SidemenuActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		if (!Ulta.isTablet(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		createMenuData();
		// setUpSideMenu();
	}

	/**
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 5000) {
			final IntentResult scanResult = ScanIntentIntegrator
					.parseActivityResult(requestCode, resultCode, data);
			if (scanResult != null) {
				final String barcode = scanResult.getContents();
				Logger.Log("Scanned barcode is " + barcode);
				Intent intentToSearchActivity = new Intent(
						MapBaseActivity.this, UltaProductListActivity.class);
				intentToSearchActivity.setAction("fromSearch");
				intentToSearchActivity.putExtra("search", barcode);
				intentToSearchActivity.putExtra("scan", "scan");
				startActivity(intentToSearchActivity);
			}
		}
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		// getSupportFragmentManager().findFragmentById(R.id.titlebar);
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		/*
		 * if(titleBar==null){ throw new UltaException(") }
		 */
		if (null == titleBar) {
			throw new RuntimeException(
					"Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
		} else {
			titleBar.setTitle(title);
		}

	}

	// /**
	// * Sets the up side menu.
	// */
	// private void setUpSideMenu() {
	// LayoutInflater inflater = (LayoutInflater)
	// getSystemService(LAYOUT_INFLATER_SERVICE);
	// mainLayout = (LinearLayout) inflater.inflate(R.layout.side_menu, null);
	// btnSearch = (Button) mainLayout.findViewById(R.id.btnSide_Menu_Search);
	// btnSearch.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Intent intentToSearchActivity = new Intent(
	// MapBaseActivity.this, SearchActivity.class);
	// startActivity(intentToSearchActivity);
	// }
	// });
	// ListView menu = (ListView) mainLayout.findViewById(R.id.menu_list);
	// adapter = new SeparatedListAdapter(this);
	// adapter.addSection(" ", new CustomAdapter(mainMenu, mainMenuImages));
	// adapter.addSection("My Data", new CustomAdapter(myDataMenu,
	// myDataMenuImages));
	// menu.setAdapter(adapter);
	// setBehindContentView(mainLayout);
	// setBehindOffset(60);
	// setBehindScrollScale(0.5f);
	// menu.setOnItemClickListener(this);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// if (isOpened()) {
		// toggle();
		// }
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(navigationDrawer);
		}
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.core.widgets.flyin.OnMenuPressedListener#onMenuPressed()
	 */
	@Override
	public void onMenuPressed() {
		// Logger.Log(">>> Menu Pressed UltaActivity2");
		// Utility.hideKeyBoard(MapBaseActivity.this, btnSearch);
		// toggle();
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(navigationDrawer);
		} else {
			drawerLayout.openDrawer(navigationDrawer);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// if (isOpened()) {
		// switch (position) {
		// case OPTION_HOME:
		// Intent intentForHome = new Intent(MapBaseActivity.this,
		// HomeActivity.class);
		// intentForHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intentForHome);
		// break;
		// case OPTION_FAVORITES:
		//
		// // ScanIntentIntegrator.initiateScan(UltaBaseActivity.this);
		//
		// if (isUltaCustomer(MapBaseActivity.this)) {
		// trackAppState(MapBaseActivity.this,
		// WebserviceConstants.FAVORITES_LOGGED_IN);
		// Intent intentForFavoritesActivity = new Intent(
		// MapBaseActivity.this, FavoritesActivity.class);
		// startActivity(intentForFavoritesActivity);
		//
		// } else {
		// trackAppState(MapBaseActivity.this,
		// WebserviceConstants.FAVORITES_LOGGED_OUT);
		// Intent intentForLogin = new Intent(MapBaseActivity.this,
		// NonSignedInRewardsActivity.class);
		// intentForLogin.putExtra("from", "fromSideMenufavorites");
		// startActivity(intentForLogin);
		//
		// }
		//
		// break;
		// case OPTION_STORE:
		// Intent intentForStoresActivity = new Intent(
		// MapBaseActivity.this, StoresActivity.class);
		// startActivity(intentForStoresActivity);
		// break;
		// case OPTION_SHOP:
		// if (activity instanceof ShopListActivity) {
		// toggle();
		// } else {
		// Intent intentForShopList = new Intent(MapBaseActivity.this,
		// ShopListActivity.class);
		// startActivity(intentForShopList);
		//
		// }
		// break;
		// case OPTION_BASKET:
		// if (activity instanceof ViewItemsInBasketActivity) {
		// toggle();
		// } else {
		// Intent intentForViewItemsInBasketActivity = new Intent(
		// MapBaseActivity.this,
		// ViewItemsInBasketActivity.class);
		// startActivity(intentForViewItemsInBasketActivity);
		// }
		// break;
		// // 3.2 release
		// case OPTION_GIFT_CARD_BAL:
		// if (activity instanceof GiftCardsTabActivity
		// && activityIndicator == 0) {
		// toggle();
		// } else {
		// Intent intentForBestSellers = new Intent(
		// MapBaseActivity.this, GiftCardsTabActivity.class);
		// activityIndicator = 0;
		// startActivity(intentForBestSellers);
		// }
		// break;
		// // 3.2 release
		// case OPTION_CHECK_ORDER_STATUS:
		// if (activity instanceof GiftCardsTabActivity
		// && activityIndicator == 1) {
		// toggle();
		// } else if (activity instanceof MyOrderHistoryActivity
		// && activityIndicator == 1) {
		// toggle();
		//
		// } else if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
		// trackAppState(this, WebserviceConstants.ORDER_STATUS_LOGGED_IN);
		// Intent intentForOrderStatus = new Intent(
		// MapBaseActivity.this, MyOrderHistoryActivity.class);
		// startActivity(intentForOrderStatus);
		// } else {
		// trackAppState(this, WebserviceConstants.ORDER_STATUS_LOGGED_OUT);
		// Intent intentForOrderStatus = new Intent(
		// MapBaseActivity.this, GiftCardsTabActivity.class);
		// intentForOrderStatus.putExtra("MenuKey", "FromSideMenu");
		// activityIndicator = 1;
		// startActivity(intentForOrderStatus);
		// }
		// break;
		// case OPTION_BEAUTY_LIST:
		//
		// break;
		// case OPTION_MY_ACCOUNT:
		// if (activity instanceof MyAccountActivity
		// || activity instanceof LoginActivity) {
		// toggle();
		// } else {
		// if (isUltaCustomer(MapBaseActivity.this)) {
		// Intent intentAccounts = new Intent(
		// MapBaseActivity.this, MyAccountActivity.class);
		// startActivity(intentAccounts);
		// } else {
		// Intent intentForLogin = new Intent(
		// MapBaseActivity.this, LoginActivity.class);
		// intentForLogin.putExtra("isfromUltaBase", 3);
		// startActivity(intentForLogin);
		// }
		// }
		// break;
		// // 4.2 changes
		// case OPTION_CALL_SERVIE: {
		// Intent dialIntent = new Intent();
		// dialIntent.setAction(Intent.ACTION_CALL);
		// dialIntent.setData(Uri.parse("tel:8669838582"));
		// startActivity(dialIntent);
		// break;
		// }
		// case OPTION_HELP_AND_ABOUT_US:
		// if (activity instanceof AboutUsActivity) {
		// toggle();
		// } else {
		// trackAppState(this, WebserviceConstants.ABOUT);
		// Intent intentForHelp = new Intent(MapBaseActivity.this,
		// AboutUsActivity.class);
		// startActivity(intentForHelp);
		// }
		// break;
		// case OPTION_CONTACT_US:
		// if (activity instanceof ContactUsActivity) {
		// toggle();
		// } else {
		// Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("plain/text");
		// intent.putExtra(Intent.EXTRA_EMAIL,
		// new String[] { UltaConstants.EMAIL_CUSTOMER_CARE });
		// try {
		// intent.putExtra(
		// Intent.EXTRA_SUBJECT,
		// "Feedback from ULTA Android Version "
		// + getPackageManager().getPackageInfo(
		// getPackageName(), 0).versionName);
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// }
		// intent.putExtra(Intent.EXTRA_TEXT,
		// UltaConstants.SENT_FROM_ANDROID);
		// startActivity(Intent.createChooser(intent, "ULTA"));
		// }
		// break;
		// case OPTION_USER_AGREEMENT:
		// if (activity instanceof LegalActivity) {
		// toggle();
		// } else {
		// trackAppState(this, WebserviceConstants.LEGAL);
		// Intent intentForLegal = new Intent(MapBaseActivity.this,
		// LegalActivity.class);
		// startActivity(intentForLegal);
		// }
		// break;
		// case OPTION_PRIVACY_POLICY:
		// if (activity instanceof PrivacyPolicyActivity) {
		// toggle();
		// } else {
		// trackAppState(this, WebserviceConstants.PRIVACY_POLICY);
		// Intent intentForprivacyPolicy = new Intent(
		// MapBaseActivity.this, PrivacyPolicyActivity.class);
		// startActivity(intentForprivacyPolicy);
		// }
		// break;
		// case OPTION_LOGOUT:
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// MapBaseActivity.this);
		// builder.setTitle("Sign Out")
		// .setMessage("Are you sure you want to sign out?")
		// .setCancelable(false)
		// .setNegativeButton("Cancel",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int id) {
		// dialog.cancel();
		// }
		// });
		// builder.setPositiveButton("Sign Out",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// pd = new ProgressDialog(MapBaseActivity.this);
		// pd.setMessage(LOADING_PROGRESS_TEXT);
		// pd.setCancelable(false);
		// pd.show();
		// invokeLogout();
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.show();
		// break;
		// default:
		// break;
		// }
		// }
	}

	/**
	 * Notify user.
	 * 
	 * @param message
	 *            the message
	 * @param context
	 *            the context
	 */
	protected void notifyUser(String message, Context context) {
		notifyUser(null, message, context, null);
	}

	/**
	 * Notify user.
	 * 
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @param context
	 *            the context
	 */
	protected void notifyUser(String title, String message, Context context) {
		notifyUser(title, message, context, null);
	}

	/**
	 * Method to display the error alert dialog.
	 * 
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @param context
	 *            the context
	 * @param intent
	 *            the intent
	 */
	protected void notifyUser(String title, String message,
			final Context context, final Intent intent) {
		displayUserErrorMessage(title, message, context, intent);
	}

	/**
	 * Method to know if the Ulta customer is logged in.
	 * 
	 * @param context
	 *            the context
	 * @return true, if is ulta customer
	 */
	protected boolean isUltaCustomer(Context context) {
		String rememberedUserName = Utility.retrieveFromSharedPreference(
				UltaConstants.REMEMBER_ME, context);
		if (UltaConstants.REMEMBER_CLICKED.equals(rememberedUserName)) {
			UltaDataCache.getDataCacheInstance().setLoggedIn(true);
		}
		return UltaDataCache.getDataCacheInstance().isLoggedIn();
	}

	/**
	 * Method to get the registered mail Id.
	 * 
	 * @param context
	 *            the context
	 * @return mailId
	 */
	protected String getRegisteredMailId(Context context) {
		String mailId = UltaConstants.EMPTY_STRING;
		mailId = Utility.retrieveFromSharedPreference(
				UltaConstants.LOGGED_MAIL_ID, context);
		Logger.Log("<Utility><getRegisteredMailId><mailId>>" + (mailId));
		return mailId != null ? mailId : UltaConstants.EMPTY_STRING;
	}

	/** The main menu. */
	private String[] mainMenu;

	/** The my data menu. */
	private String[] myDataMenu;

	/** The main menu images. */
	private int[] mainMenuImages, mainMenuHighlightedImages;

	/** The my data menu images. */
	private int[] myDataMenuImages, myDataMenuHighlightedImages;

	private String versionNo;

	/**
	 * Creates the menu data.
	 */
	private void createMenuData() {
		versionNo = "1.3";
		try {
			versionNo = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (itemCountInBasket > 0) {
			mainMenu = new String[] { "Home", "Favorites", "Shop", "Stores",
					"Giftcard Balance", "Order Status",
					"Bag" + "(" + itemCountInBasket + ")" };
		} else {
			mainMenu = new String[] { "Home", "Favorites", "Shop", "Stores",
					"Giftcard Balance", "Order Status", "Bag" };

		}
		// myDataMenu=new String[]{"My Account",};
		if (isUltaCustomer(MapBaseActivity.this)) {
			// miscMenu= new
			// String[]{"Help & About Us","Contact Us","Legal","Privacy Policy","Logout"};
			myDataMenu = new String[] { "My Account",
					"About ( v" + versionNo + " )", "Email Us", "Legal",
					"Privacy Policy", "Contact Us", "Logout" };
		} else {
			// miscMenu= new
			// String[]{"Help & About Us","Contact Us","Legal","Privacy Policy"};
			myDataMenu = new String[] { "My Account",
					"About ( v" + versionNo + " )", "Email Us", "Legal",
					"Privacy Policy", "Contact Us" };
		}

		mainMenuImages = new int[] { R.drawable.icon_home,
				R.drawable.icon_scan, R.drawable.icon_shop,
				R.drawable.icon_store, R.drawable.icon_giftcard,
				R.drawable.icon_myorderhistory, R.drawable.icon_basket };
		mainMenuHighlightedImages = new int[] { R.drawable.icon_home_active,
				R.drawable.add_fav_active, R.drawable.icon_store_active,
				R.drawable.icon_basket_active, R.drawable.icon_shop_active,
				R.drawable.icon_myorderhistory_active,
				R.drawable.icon_giftcard_active };
		myDataMenuImages = new int[] { R.drawable.icon_my_accounts,
				R.drawable.icon_help, R.drawable.icon_contact_us,
				R.drawable.icon_user_agreement, R.drawable.icon_privacy,
				R.drawable.icon_contact_us, R.drawable.icon_exit

		};
		myDataMenuHighlightedImages = new int[] {
				R.drawable.icon_my_accounts_active,
				R.drawable.icon_help_active, R.drawable.icon_email_active,
				R.drawable.icon_user_agreement_active,
				R.drawable.icon_privacy_active,
				R.drawable.icon_contact_us_active, R.drawable.icon_exit_active

		};

	}

	/**
	 * The Class CustomAdapter.
	 */
	public class CustomAdapter extends BaseAdapter {

		/** The options. */
		String[] options;

		/** The image ids. */
		int[] imageIds;
		String optionArea;

		/**
		 * Instantiates a new custom adapter.
		 * 
		 * @param options
		 *            the options
		 * @param imageIds
		 *            the image ids
		 */
		public CustomAdapter(String[] options, int[] imageIds, String optionArea) {
			super();
			this.options = options;
			this.imageIds = imageIds;
			this.optionArea = optionArea;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return options.length;
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
					R.layout.menu_item, null);
			LinearLayout rowLayout = (LinearLayout) itemLayout
					.findViewById(R.id.menu_row);
			if (options[position].contains("Contact Us")) {
				TextView t = (TextView) itemLayout
						.findViewById(R.id.menu_string);
				t.setText(options[position]);
				TextView t1 = (TextView) itemLayout
						.findViewById(R.id.menu_string_new);
				t1.setText(WebserviceConstants.ULTA_PHONE_NUMBER);
				t1.setVisibility(View.VISIBLE);

			}
			((TextView) (itemLayout.findViewById(R.id.menu_string)))
					.setText(options[position]);
			/*
			 * itemLayout.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { openPage(position); } });
			 */
			((ImageView) (itemLayout.findViewById(R.id.menu_icon)))
					.setImageResource(imageIds[position]);
//			if (activityMap instanceof StoresActivity
//					&& options[position].contains("Stores")) {
//				rowLayout.setBackgroundColor(getResources().getColor(
//						R.color.lightgrey));
//				if (optionArea.equalsIgnoreCase("Menu")) {
//					((ImageView) (itemLayout.findViewById(R.id.menu_icon)))
//							.setImageResource(mainMenuHighlightedImages[position]);
//
//				} else {
//					((ImageView) (itemLayout.findViewById(R.id.menu_icon)))
//							.setImageResource(myDataMenuHighlightedImages[position]);
//				}
//			}
			return itemLayout;
		}
	}

	/**
	 * The Class Header.
	 */
	class Header {

		/** The header. */
		String header;

		/** The level. */
		int level;
	}

	/**
	 * The Class SeparatedListAdapter.
	 */
	public class SeparatedListAdapter extends BaseAdapter {

		/** The sections. */
		public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();

		/** The headers. */
		public final ArrayAdapter<String> headers;

		/** The Constant TYPE_SECTION_HEADER. */
		public final static int TYPE_SECTION_HEADER = 0;

		/**
		 * Instantiates a new separated list adapter.
		 * 
		 * @param context
		 *            the context
		 */
		public SeparatedListAdapter(Context context) {
			headers = new ArrayAdapter<String>(context,
					R.layout.seperated_list_header);
		}

		/**
		 * Adds the section.
		 * 
		 * @param section
		 *            the section
		 * @param adapter
		 *            the adapter
		 */
		public void addSection(String section, Adapter adapter) {
			/*
			 * Header header=new Header(); header.header=section;
			 * header.level=level;
			 */
			this.headers.add(section);
			this.sections.put(section, adapter);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		public Object getItem(int position) {
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				// check if position inside this section
				if (position == 0)
					return section;
				if (position < size)
					return adapter.getItem(position - 1);

				// otherwise jump into next section
				position -= size;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		public int getCount() {
			// total together all sections, plus one for each section header
			int total = 0;
			for (Adapter adapter : this.sections.values())
				total += adapter.getCount() + 1;
			return total;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#getViewTypeCount()
		 */
		public int getViewTypeCount() {
			// assume that headers count as one, then total all sections
			int total = 1;
			for (Adapter adapter : this.sections.values())
				total += adapter.getViewTypeCount();
			return total;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#getItemViewType(int)
		 */
		public int getItemViewType(int position) {
			int type = 1;
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				// check if position inside this section
				if (position == 0)
					return TYPE_SECTION_HEADER;
				if (position < size)
					return type + adapter.getItemViewType(position - 1);

				// otherwise jump into next section
				position -= size;
				type += adapter.getViewTypeCount();
			}
			return -1;
		}

		/**
		 * Are all items selectable.
		 * 
		 * @return true, if successful
		 */
		public boolean areAllItemsSelectable() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#isEnabled(int)
		 */
		public boolean isEnabled(int position) {
			return (getItemViewType(position) != TYPE_SECTION_HEADER);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int sectionnum = 0;
			for (Object section : this.sections.keySet()) {
				Adapter adapter = sections.get(section);
				int size = adapter.getCount() + 1;

				// check if position inside this section
				if (position == 0)
					return headers.getView(sectionnum, convertView, parent);
				if (position < size) {
					View view = adapter.getView(position - 1, convertView,
							parent);
					return view;
				}
				position -= size;
				sectionnum++;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	/**
	 * Invoke logout.
	 */
	private void invokeLogout() {
		InvokerParams<StatusOnlyResponseBean> invokerParams = new InvokerParams<StatusOnlyResponseBean>();
		invokerParams.setServiceToInvoke(WebserviceConstants.LOGOUT_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateChangePasswordParameters());
		invokerParams.setUltaBeanClazz(StatusOnlyResponseBean.class);
		invokerParams.setUserSessionClearingRequired(true);
		LogoutHandler userCreationHandler = new LogoutHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<MyAccountActivity><invokeUserCreation><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> populateChangePasswordParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		// urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("atg-rest-depth", "1");

		return urlParams;
	}

	/**
	 * The Class LogoutHandler.
	 */
	public class LogoutHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<LogoutHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				notifyUser(Utility.formatDisplayError(getErrorMessage()),
						MapBaseActivity.this);
			} else {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				Logger.Log("<Logout><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				StatusOnlyResponseBean ultaBean = (StatusOnlyResponseBean) getResponseBean();
				String responseStatus = ultaBean.getResponseStatus();
				Logger.Log("<LogoutHandler><handleMessage><getResponseBean>>"
						+ responseStatus);
				// remember me is changed to unclicked state
				Utility.saveToSharedPreference(UltaConstants.REMEMBER_ME,
						UltaConstants.BLANK_STRING, getApplicationContext());
				UltaDataCache.getDataCacheInstance().setLoggedIn(false);
				setItemCountInBasket(0);
				UltaDataCache.getDataCacheInstance().setStaySignedIn(true);
				staySignedInSharedPreferences = getSharedPreferences(
						WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
						MODE_PRIVATE);
				staySignedInEditor = staySignedInSharedPreferences.edit();
				staySignedInEditor.putBoolean(
						WebserviceConstants.IS_STAY_SIGNED_IN, false);
				staySignedInEditor.putString(
						WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");
				staySignedInEditor.putString(
						WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");
				staySignedInEditor.putString(
						WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
				staySignedInEditor.putString(
						WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME, " ");
				staySignedInEditor.commit();
				Intent intentForHome = new Intent(MapBaseActivity.this,
						HomeActivity.class);
				intentForHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentForHome);
			}
		}
	}

	@Override
	public void onLogout() {
	}

	// private void invalidateSideMenu() {
	// createMenuData();
	// ListView menu = (ListView) mainLayout.findViewById(R.id.menu_list);
	// adapter = new SeparatedListAdapter(this);
	// adapter.addSection(" ", new CustomAdapter(mainMenu, mainMenuImages));
	// adapter.addSection("My Data", new CustomAdapter(myDataMenu,
	// myDataMenuImages));
	// menu.setAdapter(adapter);
	// }

	private LogoutBroadcastReciver logoutBroadcastReciver;

	public void registerForLogoutBroadcast() {
		logoutBroadcastReciver = new LogoutBroadcastReciver();
		registerReceiver(logoutBroadcastReciver, new IntentFilter(
				"com.ulta.core.action.LOGOUT"));
	}

	public class LogoutBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.ulta.core.action.LOGOUT")) {
				unregisterReceiver(logoutBroadcastReciver);
				finish();
			}
		}
	}

	/**
	 * Method to make the menu invisible. To be used in checkout pages
	 */
	public void disableMenu() {
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		if (null == titleBar) {
			throw new RuntimeException(
					"Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
		} else {
			titleBar.setMenu(MapBaseActivity.this, "false");
		}

	}

	/**
	 * method to make done button invisible. To be used while loading data from
	 * WS. So as to block the user from leaving the operation incomplete.
	 */
	public void disableDone() {
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		if (null == titleBar) {
			throw new RuntimeException(
					"Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
		} else {
			titleBar.disableDone();
		}

	}

	/**
	 * method to make done button visible.
	 */
	public void enableDone() {
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		if (null == titleBar) {
			throw new RuntimeException(
					"Your content must have a TitleBar whose id attribute is 'R.id.titlebar'");
		} else {
			titleBar.enableDone();
		}

	}

	public void setActivity(UltaBaseActivity activity) {
		this.activity = activity;
	}

	public UltaBaseActivity getActivity() {
		return activity;
	}

	public int getItemCountInBasket() {
		return itemCountInBasket;
	}

	public void setItemCountInBasket(int itemCountInBasket) {
		MapBaseActivity.itemCountInBasket = itemCountInBasket;
		// invalidateSideMenu();
	}

	@Override
	public void onBagPressed() {

		Intent intentForViewItemsInBasketActivity = new Intent(
				MapBaseActivity.this, ViewItemsInBasketActivity.class);
		startActivity(intentForViewItemsInBasketActivity);
	}

	@Override
	public void onScanPressed() {
		ScanIntentIntegrator.initiateScan(MapBaseActivity.this);

	}

	@Override
	public void onSearchPressed() {
		Intent intentToSearchActivity = new Intent(MapBaseActivity.this,
				SearchActivity.class);
		startActivity(intentToSearchActivity);
	}

	/**
	 * Sets basket count
	 */

	public void setBasketCount(int basketCount) {
		titleBar = (TitleBar) findViewById(R.id.titlebar);
		titleBar.setBasketCount(basketCount);
	}

	public void trackAppState(Activity activity, String pageName) {
		OmnitureTracking.startActivity(activity);
		OmnitureTracking.setPageName(pageName);
		OmnitureTracking.stopActivity();

	}

	public Typeface setHelveticaRegulartTypeFace() {
		try {
			helveticaRegularTypeface = Typeface.createFromAsset(getAssets(),
					"Helvetica_Reg.ttf");
			return helveticaRegularTypeface;
		} catch (Exception e) {
			helveticaRegularTypeface = Typeface.SANS_SERIF;
			return helveticaRegularTypeface;
		}
	}

	@Override
	public void onTitleBarPressed() {

	}

	public void setUpNavigationDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationDrawer = (LinearLayout) findViewById(R.id.navigation_drawer);
		ListView navigationList = (ListView) findViewById(R.id.naviagtion_list);
		adapter = new SeparatedListAdapter(this);
		adapter.addSection(" ", new CustomAdapter(mainMenu, mainMenuImages,
				"Menu"));
		adapter.addSection("MY DATA", new CustomAdapter(myDataMenu,
				myDataMenuImages, "Data"));
		navigationList.setAdapter(adapter);
		navigationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
						switch (position) {
				case OPTION_HOME:
					if (activity instanceof HomeActivity) {
						drawerLayout.closeDrawer(navigationDrawer);
					} else {
						drawerLayout.closeDrawer(navigationDrawer);
						Intent intentForHome = new Intent(MapBaseActivity.this,
								HomeActivity.class);
						intentForHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intentForHome);
					}
					break;
				case OPTION_FAVORITES:
					drawerLayout.closeDrawer(navigationDrawer);
					// ScanIntentIntegrator.initiateScan(UltaBaseActivity.this);

					if (isUltaCustomer(MapBaseActivity.this)) {
						trackAppState(MapBaseActivity.this,
								WebserviceConstants.FAVORITES_LOGGED_IN);
						Intent intentForFavoritesActivity = new Intent(
								MapBaseActivity.this, FavoritesActivity.class);
						startActivity(intentForFavoritesActivity);

					} else {
						Intent intentForLogin = new Intent(
								MapBaseActivity.this,
								NonSignedInRewardsActivity.class);
						intentForLogin
								.putExtra("from", "fromSideMenufavorites");
						startActivity(intentForLogin);

					}

					break;
				case OPTION_STORE:
					drawerLayout.closeDrawer(navigationDrawer);
					Intent intentForStoresActivity = new Intent(
							MapBaseActivity.this, StoresActivity.class);
					startActivity(intentForStoresActivity);
					break;
				case OPTION_SHOP:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof ShopListActivity) {
					} else {
						/*
						 * Intent intentForShop = new
						 * Intent(UltaBaseActivity.this,
						 * ShopExtendedMenuListActivity.class);
						 * startActivity(intentForShop);
						 */
						drawerLayout.closeDrawer(navigationDrawer);
						Intent intentForShopList = new Intent(
								MapBaseActivity.this, ShopListActivity.class);
						startActivity(intentForShopList);
					}
					break;
				case OPTION_BASKET:
					if (activity instanceof ViewItemsInBasketActivity) {
						drawerLayout.closeDrawer(navigationDrawer);
					} else {
						drawerLayout.closeDrawer(navigationDrawer);
						Intent intentForViewItemsInBasketActivity = new Intent(
								MapBaseActivity.this,
								ViewItemsInBasketActivity.class);
						startActivity(intentForViewItemsInBasketActivity);
					}
					break;
				// 3.2 release
				case OPTION_GIFT_CARD_BAL:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof GiftCardsTabActivity
							&& activityIndicator == 0) {
					} else {
						Intent intentForBestSellers = new Intent(
								MapBaseActivity.this,
								GiftCardsTabActivity.class);
						activityIndicator = 0;
						startActivity(intentForBestSellers);
					}
					break;
				// 3.2 release
				case OPTION_CHECK_ORDER_STATUS:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof GiftCardsTabActivity
							&& activityIndicator == 1) {
					} else if (activity instanceof MyOrderHistoryActivity
							&& activityIndicator == 1) {

					} else if (UltaDataCache.getDataCacheInstance()
							.isLoggedIn()) {
						trackAppState(MapBaseActivity.this,
								WebserviceConstants.ORDER_STATUS_LOGGED_IN);
						Intent intentForOrderStatus = new Intent(
								MapBaseActivity.this,
								MyOrderHistoryActivity.class);
						startActivity(intentForOrderStatus);
					} else {
						trackAppState(MapBaseActivity.this,
								WebserviceConstants.ORDER_STATUS_LOGGED_OUT);
						Intent intentForOrderStatus = new Intent(
								MapBaseActivity.this,
								GiftCardsTabActivity.class);
						intentForOrderStatus
								.putExtra("MenuKey", "FromSideMenu");
						activityIndicator = 1;
						startActivity(intentForOrderStatus);
					}
					break;
				case OPTION_BEAUTY_LIST:
					drawerLayout.closeDrawer(navigationDrawer);
					break;

				case OPTION_MY_ACCOUNT:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof MyAccountActivity
							|| activity instanceof LoginActivity) {
					} else {
						if (isUltaCustomer(MapBaseActivity.this)) {
							Intent intentAccounts = new Intent(
									MapBaseActivity.this,
									MyAccountActivity.class);
							startActivity(intentAccounts);
						} else {
							Intent intentForLogin = new Intent(
									MapBaseActivity.this, LoginActivity.class);
							intentForLogin.putExtra("isfromUltaBase", 3);
							intentForLogin.putExtra("myAccountfromUltaBase",
									true);
							startActivity(intentForLogin);
						}
					}
					break;
				// 4.2 changes
				case OPTION_CALL_SERVIE: {
					drawerLayout.closeDrawer(navigationDrawer);
					Intent dialIntent = new Intent();
					dialIntent.setAction(Intent.ACTION_DIAL);
					dialIntent.setData(Uri.parse("tel:"
							+ WebserviceConstants.ULTA_PHONE_NUMBER));
					startActivity(dialIntent);
					break;
				}
				case OPTION_HELP_AND_ABOUT_US:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof AboutUsActivity) {
					} else {
						trackAppState(MapBaseActivity.this,
								WebserviceConstants.ABOUT);
						Intent intentForHelp = new Intent(MapBaseActivity.this,
								AboutUsActivity.class);
						startActivity(intentForHelp);
					}
					break;
				case OPTION_CONTACT_US:
					drawerLayout.closeDrawer(navigationDrawer);

					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("plain/text");
					intent.putExtra(Intent.EXTRA_EMAIL,
							new String[] { UltaConstants.EMAIL_CUSTOMER_CARE });
					try {
						intent.putExtra(
								Intent.EXTRA_SUBJECT,
								"Feedback from Ulta Beauty Android Version "
										+ getPackageManager().getPackageInfo(
												getPackageName(), 0).versionName);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					intent.putExtra(Intent.EXTRA_TEXT,
							UltaConstants.SENT_FROM_ANDROID);
					startActivity(Intent.createChooser(intent, "ULTA"));
					break;
				case OPTION_USER_AGREEMENT:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof LegalActivity) {
					} else {
						trackAppState(MapBaseActivity.this,
								WebserviceConstants.LEGAL);
						Intent intentForLegal = new Intent(
								MapBaseActivity.this, LegalActivity.class);
						startActivity(intentForLegal);
					}
					break;
				case OPTION_PRIVACY_POLICY:
					drawerLayout.closeDrawer(navigationDrawer);
					if (activity instanceof PrivacyPolicyActivity) {
					} else {

						trackAppState(MapBaseActivity.this,
								WebserviceConstants.PRIVACY_POLICY);

						Intent intentForprivacyPolicy = new Intent(
								MapBaseActivity.this,
								PrivacyPolicyActivity.class);
						startActivity(intentForprivacyPolicy);
					}
					break;
				case OPTION_LOGOUT:
					drawerLayout.closeDrawer(navigationDrawer);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MapBaseActivity.this);

					builder.setTitle("Sign Out")
							.setMessage("Are you sure you want to sign out?")
							.setCancelable(false)
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					builder.setPositiveButton("Sign Out",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									pd = new ProgressDialog(
											MapBaseActivity.this);
									pd.setMessage(LOADING_PROGRESS_TEXT);
									pd.setCancelable(false);
									pd.show();
									invokeLogout();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();

					/*
					 * if
					 * (UltaDataCache.getDataCacheInstance().isStaySignedIn()) {
					 * Intent intentForHomeActivity = new Intent(
					 * UltaBaseActivity.this, HomeActivity.class);
					 * intentForHomeActivity
					 * .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					 * Intent.FLAG_ACTIVITY_NEW_TASK |
					 * Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					 * startActivity(intentForHomeActivity); } else { pd = new
					 * ProgressDialog(UltaBaseActivity.this);
					 * pd.setMessage(LOADING_PROGRESS_TEXT);
					 * pd.setCancelable(false); pd.show(); invokeLogout(); }
					 */
				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setUpNavigationDrawer();
	}
}
