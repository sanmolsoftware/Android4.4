/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.widgets.PinchZoomView;

/**
 * This class is used to display the different images of a particular product.
 * 
 * @author Infosys
 */
public class PinchZoomActivity extends Activity {

	private TextView tvBrandName;
	private TextView tvDisplayName;
	private ImageView imgSmallProductImage;

	private LinearLayout mHeaderLayout;

	public static final String EXTRA_IMAGE = "PinchZommActivity:image";

	/**
	 * The imageUrlsList.
	 */
	private String imageUrl;

	/**
	 * The productZoomImage.
	 */
	private PinchZoomView productZoomImage;

	private String displayName;
	private String brandName;

	/**
	 * Called when the activity is created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_image_pinch_zoom);

		if (null != getIntent().getStringExtra("imageUrl")) {
			imageUrl = getIntent().getStringExtra("imageUrl");
		}

		if (null != getIntent().getStringExtra("productname")) {
			displayName = getIntent().getStringExtra("productname");
		}
		if (null != getIntent().getStringExtra("brandname")) {
			brandName = getIntent().getStringExtra("brandname");
		}

		setViews();
	}

	/**
	 * Method to initialize the views of the main layout.
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public void setViews() {

		mHeaderLayout = (LinearLayout) findViewById(R.id.llProductHeader);
		productZoomImage = (PinchZoomView) findViewById(R.id.imgProductImage);
		imgSmallProductImage = (ImageView) findViewById(R.id.imgSmallProductImage);
		tvBrandName = (TextView) findViewById(R.id.tvBrandName);
		tvDisplayName = (TextView) findViewById(R.id.tvDisplayName);
		tvDisplayName.setText(displayName);
		tvBrandName.setText(brandName);
		new AQuery(imgSmallProductImage).image(imageUrl, true, false, 1024,
				R.drawable.dummy_product, null, AQuery.FADE_IN);
		
		UltaDataCache.getDataCacheInstance().setPinchZoomHeaderLayout(mHeaderLayout);
		
		ViewCompat.setTransitionName(productZoomImage, EXTRA_IMAGE);

		productZoomImage.setVisibility(View.VISIBLE);

		productZoomImage.setTag(imageUrl);

		Bitmap bitmap = UltaDataCache.getDataCacheInstance()
				.getPdpHashMapOfImages().get(0);

		productZoomImage.setImageBitmap(bitmap);

		new AQuery(productZoomImage).image(imageUrl, true, false, 1024,
				R.drawable.dummy_product, bitmap, AQuery.FADE_IN);
		
	}

	public static void launch(Activity activity, View transitionView,
			String url, String prodName, String brandName) {
		ActivityOptionsCompat options = ActivityOptionsCompat
				.makeSceneTransitionAnimation(activity, transitionView,
						EXTRA_IMAGE);
		Intent pinchZoomIntent = new Intent(activity, PinchZoomActivity.class);
		pinchZoomIntent.putExtra(EXTRA_IMAGE, url);
		pinchZoomIntent.putExtra("imageUrl", url);
		pinchZoomIntent.putExtra("productname", prodName);
		pinchZoomIntent.putExtra("brandname", brandName);
		ActivityCompat.startActivity(activity, pinchZoomIntent,
				options.toBundle());
	}
}
