/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.BrandBean;
import com.ulta.core.bean.product.BrandsBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BrandsActivity extends UltaBaseActivity implements
		OnTouchListener, OnClickListener,
		android.content.DialogInterface.OnClickListener {

	// constants
	/**
	 * The Constant BRAND_INDEX.
	 */
	private final static int BRAND_INDEX = 2000;

	/**
	 * The letterIndexMap.
	 */
	private Map<String, Object[]> LetterIndexMap = new HashMap<String, Object[]>();

	/**
	 * The letterArray.
	 */
	String letterArray[] = { "ULTA", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "#" };

	/* private int count; */

	/**
	 * The letterSizePerIndex.
	 */
	private int letterSizePerIndex;

	// view elements
	/**
	 * The brandsLayout.
	 */
	private LinearLayout brandsLayout;

	/**
	 * The loadingDialogLayout.
	 */
	private LinearLayout loadingDialogLayout;

	/**
	 * The alphabetOverlaylayout.
	 */
	private FrameLayout alphabetOverlaylayout;

	/**
	 * The brandsGridView.
	 */
	/* private GridView brandsGridView; */

	/**
	 * The List View
	 */
	private ListView listView;

	/**
	 * The sideIndex.
	 */
	private LinearLayout sideIndex;

	/*	*//**
	 * The noDataText.
	 */
	/*
	 * private TextView noDataText;
	 */

	// For loading images of brands
	/**
	 * The imageLoader.
	 */

	/**
	 * The gestureDetector.
	 */
	private GestureDetector gestureDetector;

	/**
	 * The gestureListener.
	 */
	private OnTouchListener gestureListener;

	/**
	 * The isScrolling.
	 */
	private boolean isScrolling;

	// for side index computations
	// x and y coordinates within our side index
	/**
	 * The sideIndexX.
	 */
	private static float sideIndexX;

	/**
	 * The sideIndexY.
	 */
	private static float sideIndexY;
	// height of side index
	/**
	 * The sideIndexHeight.
	 */
	private int sideIndexHeight;
	// number of items in the side index
	/**
	 * The indexListSize.
	 */
	private int indexListSize;

	// Data
	// list with items for side index
	/**
	 * The indexList.
	 */
	private List<Object[]> indexList = null;

	/**
	 * The brandList.
	 */
	private List<BrandBean> brandList;

	/**
	 * The brandImg.
	 */

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brands_activity);
		omniInit();
		setTitle("Shop by Brands");
		setActivity(BrandsActivity.this);
		setViews();
		startFetchingBrands();
	}

	private void omniInit() {
		trackAppState(this, WebserviceConstants.SHOP_BY_BRANDS);
	}

	private void startFetchingBrands() {
		loadingDialogLayout.setVisibility(View.VISIBLE);
		// noDataText.setVisibility(View.INVISIBLE);
		invokeShopByBrands();
	}

	/**
	 * Initializing the child view of the main layout.
	 */
	public void setViews() {
		brandsLayout = (LinearLayout) findViewById(R.id.brands_body_layout);
		brandsLayout.setVisibility(View.INVISIBLE);
		loadingDialogLayout = (LinearLayout) findViewById(R.id.brands_loadingDialog);
		loadingDialogLayout.setVisibility(View.VISIBLE);
		sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		listView = (ListView) findViewById(R.id.lvbrandsList);
	}

	public void displayBrands() {
		loadingDialogLayout.setVisibility(View.INVISIBLE);
		brandsLayout.setVisibility(View.VISIBLE);
		listView.setAdapter(new ImageAdapter());
		gestureDetector = new GestureDetector(BrandsActivity.this,
				new SideIndexGestureListener());

		final ViewTreeObserver vto = sideIndex.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			public void onGlobalLayout() {
				final ViewTreeObserver obs = sideIndex.getViewTreeObserver();
				sideIndexHeight = sideIndex.getHeight();
				letterSizePerIndex = sideIndexHeight / 28;

				createSideIndex();
				obs.removeGlobalOnLayoutListener(this);
			}
		});
	}

	/**
	 * The adapter which populates the brand grid.
	 * 
	 * @author Infosys
	 */
	public class ImageAdapter extends BaseAdapter {

		/**
		 * @see android.widget.Adapter#getCount()
		 */
		public int getCount() {
			return brandList.size() + 1;
		}

		/**
		 * @see android.widget.Adapter#getCount()
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			final LayoutInflater li = getLayoutInflater();
			view = li.inflate(R.layout.brand_item, null);
			setItemView(view, position);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent productListIntent = new Intent(BrandsActivity.this,
							UltaProductListActivity.class);
					productListIntent.putExtra("fromShopByBrandsPage",
							"fromShopByBrandsPage");
					int position = v.getId() - BRAND_INDEX;
					productListIntent.putExtra("selectedBrandId", brandList
							.get(position).getBrandId());
					productListIntent.putExtra("altText",
							brandList.get(position).getBrandName());
					startActivity(productListIntent);
				}
			});
			return view;

		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public void setItemView(View view, int position) {
			final TextView brandText = (TextView) view
					.findViewById(R.id.icon_text);
			// brandText.setVisibility(TextView.VISIBLE);

			if (position == 0) {
				for (int i = 0; i < brandList.size(); i++) {
					if (brandList.get(i).getBrandName()
							.equalsIgnoreCase("ULTA")) {
						brandText.setText(brandList.get(i).getBrandName());
						view.setId(BRAND_INDEX + i);
						break;
					}

				}
			} else {
				if (brandList.get(position - 1).getBrandName()
						.equalsIgnoreCase("ULTA")) {
					brandText.setVisibility(View.GONE);
					view.setVisibility(View.GONE);
				} else {
					brandText.setText(brandList.get(position - 1)
							.getBrandName());
					view.setId(BRAND_INDEX + position - 1);
				}
			}
			// view.setOnClickListener(BrandsActivity.this);

		}
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}

	public void onClick(View v) {
	}

	/**
	 * shows alert messages to the user.
	 * 
	 * @param title
	 *            - the title of the alert message
	 * @param messagefree
	 *            - the message
	 */
	public void showUserMessage(String title, String message) {
		/*
		 * final AlertDialog.Builder builder = new AlertDialog.Builder(
		 * BrandsActivity.this);
		 * builder.setMessage(message).setTitle(title).setCancelable(false)
		 * .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int id) {
		 * dialog.dismiss(); } }); final AlertDialog alert = builder.create();
		 * alert.show();
		 */

		final Dialog alert = showAlertDialog(BrandsActivity.this, title,
				message, "OK", "");
		alert.show();
		mDisagreeButton.setVisibility(View.GONE);
		mAgreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alert.dismiss();
			}
		});
	}

	/**
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface,
	 *      int)
	 */
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}

	/**
	 * Gesture listener for the side Index.
	 * 
	 * @author Infosys
	 */
	class SideIndexGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent,
		 *      android.view.MotionEvent, float, float)
		 */
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			isScrolling = true;
			// we know already coordinates of first touch
			// we know as well a scroll distance
			sideIndexX = sideIndexX - distanceX;
			sideIndexY = sideIndexY - distanceY;

			// when the user scrolls within our side index
			// we can show for every position in it a proper
			// item in the country list
			if ((sideIndexX >= 0) && (sideIndexY >= 0)) {
				displayListItem();
				// layout.setVisibility(View.INVISIBLE);
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		/**
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
		 */
		public boolean onDown(MotionEvent e) {
			// Log.d("GestureDetector --> onDown");
			return true;
		}
	}

	/**
	 * creates the side Index.
	 * 
	 * @param dataList
	 *            - the data that is indexed, the content of the grid, ie the
	 *            brand names
	 * @return the index List, ie the list of indices or starting letters of the
	 *         brands names
	 */
	private ArrayList<Object[]> createIndex(ArrayList<BrandBean> dataList) {
		final ArrayList<Object[]> tmpIndexList = new ArrayList<Object[]>();
		Object[] tmpIndexItem = null;
		final ArrayList<String> lettersAdded = new ArrayList<String>();
		int tmpPosition = 0;
		String tmpLetter = "";
		lettersAdded.add(tmpLetter);
		String currentLetter = null;
		String strItem = null;
		for (int j = 0; j < dataList.size(); j++) {
			strItem = dataList.get(j).getBrandName();
			// get the first letter
			currentLetter = strItem.substring(0, 1);
			// every time new letters comes
			// save it to index list
			if (!lettersAdded.contains(currentLetter)
					&& !lettersAdded.contains(currentLetter.toUpperCase())
					&& !currentLetter.equals(tmpLetter)) {
				// create the object for the previous letter
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = tmpLetter; // the letter comes first
				tmpIndexItem[1] = tmpPosition; //
				tmpIndexItem[2] = j - 1;// the index of last word for the just
				// previous letter
				lettersAdded.add(tmpLetter);
				tmpLetter = currentLetter; // replacing tmpLetter with latest
				// letter found
				tmpPosition = j + 1; // the start index of the word
				// corresponding to the next letter
				tmpIndexList.add(tmpIndexItem);

			}
		}

		// save also last letter
		tmpIndexItem = new Object[3];
		tmpIndexItem[0] = tmpLetter;
		tmpIndexItem[1] = tmpPosition - 1;
		tmpIndexItem[2] = dataList.size() - 1;
		tmpIndexList.add(tmpIndexItem);

		// and remove first temporary empty entry
		if ((null != tmpIndexList) && (tmpIndexList.size() > 0)) {
			tmpIndexList.remove(0);
		}

		// /extras
		tmpIndexItem = tmpIndexList.get(0);
		String letter = "ULTA";
		LetterIndexMap.put(letter, tmpIndexItem);
		if (tmpIndexList.size() == 28) {
			LetterIndexMap.put("#", tmpIndexList.get(27));
		}
		boolean isAFound = false;
		/* count = 0; */
		for (int i = 0; i < tmpIndexList.size(); i++) {
			tmpIndexItem = new Object[3];
			tmpIndexItem = tmpIndexList.get(i);
			letter = (String) tmpIndexItem[0];
			if (letter.equalsIgnoreCase("A") && !isAFound) {
				isAFound = true;
				/* count++; */
			}

			if (isAFound) {
				final Object[] check = LetterIndexMap.get(letter.toUpperCase());
				if (null == check) {
					LetterIndexMap.put(letter.toUpperCase(), tmpIndexItem);
				}

			}

		}
		final Collection<Object[]> c = LetterIndexMap.values();
		final Iterator<Object[]> i = c.iterator();
		while (i.hasNext()) {
			tmpIndexItem = (Object[]) i.next();
		}
		return tmpIndexList;
	}

	/**
	 * Displays the alphabet selected on the overlay and scrolls the list to the
	 * words staring from the selected alphabet *.
	 */
	public void displayListItem() {
		// compute number of pixels for every side index item
		double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

		pixelPerIndexItem = (int) Math.floor((double) sideIndex.getHeight()
				/ indexListSize);

		pixelPerIndexItem = letterSizePerIndex;

		// compute the item index for given event position belongs to
		final int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

		// compute minimal position for the item in the list
		final int minPosition = (int) (itemPosition * pixelPerIndexItem);

		if (itemPosition >= 28) {
			return;
		}
		final String letter = letterArray[itemPosition];

		// get the item (we can do it since we know item index)
		Object[] indexItem;

		indexItem = LetterIndexMap.get(letter);

		if (null == indexItem) {
			return;
		}

		alphabetOverlaylayout = (FrameLayout) findViewById(R.id.toast_layout_root2);
		// alphabetOverlaylayout.setVisibility(View.INVISIBLE);
		final TextView alphabetText = (TextView) alphabetOverlaylayout
				.findViewById(R.id.text2);
		alphabetText.setText(indexItem[0].toString());

		final int indexMin = Integer.parseInt(indexItem[1].toString());

		final double pixelPerSubitem = pixelPerIndexItem;// / indexDelta;
		final int subitemPosition = (int) (indexMin + (sideIndexY - minPosition)
				/ pixelPerSubitem);

		// brandsGridView.setSelection(subitemPosition);
		listView.setSelection(subitemPosition);
	}

	/**
	 * creates the side Index view.
	 */
	public void createSideIndex() {

		sideIndex.removeAllViews();
		TextView alphabetInSideIndexText = null;

		indexList = createIndex((ArrayList<BrandBean>) brandList);
		indexListSize = indexList.size();

		int letterSize = (int) Math.floor((double) sideIndex.getHeight()
				/ indexListSize);

		letterSize = letterSizePerIndex;

		for (int index = 0; index < letterArray.length; index++) {
			alphabetInSideIndexText = new TextView(BrandsActivity.this);
			alphabetInSideIndexText.setText(letterArray[index]);
			alphabetInSideIndexText.setGravity(Gravity.CENTER);
			alphabetInSideIndexText.setTextSize(8);
			alphabetInSideIndexText.setTextColor(Color.BLACK);
			alphabetInSideIndexText.setTypeface(Typeface.SERIF);
			alphabetInSideIndexText.setHeight(letterSize);
			alphabetInSideIndexText.setMinHeight(letterSize);
			sideIndex.addView(alphabetInSideIndexText);
		}

		createGestureListener();
		sideIndex.setOnTouchListener(gestureListener);
	}

	/**
	 * creates the gesture listener for the side index.
	 */
	public void createGestureListener() {
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					sideIndexX = event.getX();
					sideIndexY = event.getY();
					displayListItem();
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isScrolling) {
						if (null != alphabetOverlaylayout) {
							alphabetOverlaylayout.setVisibility(View.INVISIBLE);
						}
					}
					;
				}
				return false;
			}
		};
	}

	/**
	 * Invoke ShopByBrands.
	 */
	private void invokeShopByBrands() {
		InvokerParams<BrandsBean> invokerParams = new InvokerParams<BrandsBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.SHOP_BY_BRANDS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateShopByBrandsParameters());
		invokerParams.setUltaBeanClazz(BrandsBean.class);
		ShopByBrandsHandler userCreationHandler = new ShopByBrandsHandler();
		invokerParams.setUltaHandler(userCreationHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> populateShopByBrandsParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		urlParams.put("startChar", "");
		urlParams.put("endChar", "");
		return urlParams;
	}

	/**
	 * Once the ShopByBrands WS completes and return with response, the control
	 * flow return back to this handler. The handler hence performs the UI
	 * modifications, i.e displays the brands, based on the response returned.
	 */
	public class ShopByBrandsHandler extends UltaHandler {
		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			loadingDialogLayout.setVisibility(View.INVISIBLE);
			brandsLayout.setVisibility(View.VISIBLE);
			// noDataText.setVisibility(View.INVISIBLE);
			if (null != getErrorMessage()) {
				try {
					notifyUser(getErrorMessage(), BrandsActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				BrandsBean brandsBean = (BrandsBean) getResponseBean();
				brandList = brandsBean.getAtgResponse();
				Collections.sort(brandList, new Comparator<BrandBean>() {
					public int compare(BrandBean s1, BrandBean s2) {
						return s1.getBrandName().compareToIgnoreCase(
								s2.getBrandName());
					}
				});
				sortNumbersAtBottom();
				displayBrands();
			}
		}
	}

	private void sortNumbersAtBottom() {
		for (int i = 0; i < brandList.size(); i++) {
			i = 0;
			if (!Character.isLetter(brandList.get(i).getBrandName().charAt(0))) {
				BrandBean bb = brandList.get(i);
				brandList.remove(i);
				brandList.add(bb);
			} else {
				break;
			}

		}
	}
}
