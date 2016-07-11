package com.ulta.core.olapic.grid;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.about.LegalActivity;
import com.ulta.core.activity.about.PhotoSharingGuidelines;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.olapic.custom.ScrollViewExt;
import com.ulta.core.olapic.custom.ScrollViewListener;

import java.util.ArrayList;

public class QuiltView extends FrameLayout implements OnGlobalLayoutListener {

	public QuiltViewBase quilt;
	public ScrollViewExt scroll;
	public int padding = 5;
	public boolean isVertical = false;
	public ArrayList<View> views;
	private Adapter adapter;

	public void setGridListener(ScrollViewListener listener) {
		scroll.setScrollViewListener(listener);
	}

	public int indexOfPatch(View v) {
		return quilt.indexOfChild(v);
	}

	public QuiltView(Context context, boolean isVertical) {
		super(context);
		this.isVertical = isVertical;
		setup();
	}

	public QuiltView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.QuiltView);

		String orientation = a
				.getString(R.styleable.QuiltView_scrollOrientation);
		a.recycle();

		if (orientation != null) {
			if (orientation.equals("vertical")) {
				isVertical = true;
			} else {
				isVertical = false;
			}
		}
		setup();
	}

	public void setup() {
		views = new ArrayList<View>();

		if (isVertical) {
			scroll = new ScrollViewExt(this.getContext());
		} else {
			// scroll = new HorizontalScrollView(this.getContext());
			scroll = new ScrollViewExt(this.getContext());
		}
		quilt = new QuiltViewBase(getContext(), isVertical);
		LinearLayout olpaicLayout = new LinearLayout(this.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		olpaicLayout.setOrientation(LinearLayout.VERTICAL);
		olpaicLayout.setLayoutParams(params);
		FrameLayout bannerWithTermsAndFaqLayout = new FrameLayout(
				this.getContext());
		bannerWithTermsAndFaqLayout.setLayoutParams(params);
		bannerWithTermsAndFaqLayout.addView(getBanner());
		bannerWithTermsAndFaqLayout.addView(getTermsAndFaq());
		olpaicLayout.addView(bannerWithTermsAndFaqLayout);
		olpaicLayout.addView(quilt);
		scroll.addView(olpaicLayout);
		this.addView(scroll);

	}

	private LinearLayout getTermsAndFaq() {
		LinearLayout termsAndFaq = new LinearLayout(this.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		termsAndFaq.setLayoutParams(params);
		termsAndFaq.setPadding(20, 30, 10, 10);
		termsAndFaq.setOrientation(LinearLayout.HORIZONTAL);
		TextView termsTV = new TextView(this.getContext());
		termsTV.setText(getResources().getString(R.string.olapic_terms));
		termsTV.setTextColor(getResources().getColor(R.color.white));
		termsTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
		           getResources().getDimension(R.dimen.olapic_terms_text_Size));
		termsAndFaq.addView(termsTV);
		TextView partition = new TextView(this.getContext());
		partition.setText(getResources().getString(R.string.olapic_partition));
		partition.setTextColor(getResources().getColor(R.color.white));
		partition.setTextSize(12);
		termsAndFaq.addView(partition);
		TextView faqTV = new TextView(this.getContext());
		faqTV.setText(getResources().getString(R.string.olapic_faq));
		faqTV.setTextColor(getResources().getColor(R.color.white));
		faqTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
		           getResources().getDimension(R.dimen.olapic_terms_text_Size));
		termsAndFaq.addView(faqTV);

		termsTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent termsOrFAQIntent = new Intent(getContext(),
						LegalActivity.class);
				getContext().startActivity(termsOrFAQIntent);
			}
		});

		faqTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent termsOrFAQIntent = new Intent(getContext(),
						PhotoSharingGuidelines.class);
				getContext().startActivity(termsOrFAQIntent);
			}
		});

		return termsAndFaq;
	}

	private ImageView getBanner() {
		ImageView olapicBanner = new ImageView(this.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		olapicBanner.setLayoutParams(params);
		olapicBanner.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		olapicBanner.setAdjustViewBounds(true);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		olapicBanner.getLayoutParams().width = screenWidth;

		if (haveInternet()) {
			checkDensityAndSetImage(olapicBanner,
					WebserviceConstants.SOCIAL_GALLERY_BANNER, R.drawable.olapicbanner,
					"GiftCard", null, false);
		} else {
			olapicBanner.setImageResource(R.drawable.olapicbanner);
		}
//		olapicBanner.setImageResource(R.drawable.olapicbanner);
		return olapicBanner;
	}

	private DataSetObserver adapterObserver = new DataSetObserver() {
		public void onChanged() {
			super.onChanged();
			onDataChanged();
		}

		public void onInvalidated() {
			super.onInvalidated();
			onDataChanged();
		}

		public void onDataChanged() {
			setViewsFromAdapter(adapter);
		}
	};

	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
		adapter.registerDataSetObserver(adapterObserver);
		setViewsFromAdapter(adapter);
	}

	private void setViewsFromAdapter(Adapter adapter) {
		this.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			quilt.addPatch(adapter.getView(i, null, quilt));
		}
	}

	public void addPatchImages(ArrayList<ImageView> images) {

		for (ImageView image : images) {
			addPatchImage(image);
		}
	}

	public void addPatchImage(ImageView image) {

		// FrameLayout.LayoutParams params = new
		// FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
		// FrameLayout.LayoutParams.MATCH_PARENT);
		// image.setLayoutParams(params);
		image.setPadding(padding, padding, padding, padding);

		// LinearLayout wrapper = new LinearLayout(this.getContext());
		// wrapper.setPadding(padding, padding, padding, padding);
		// wrapper.addView(image);
		quilt.addPatch(image);
	}

	public void addPatchViews(ArrayList<View> views_a) {
		for (View view : views_a) {
			quilt.addPatch(view);
		}
	}

	public void addPatchView(View view) {
		quilt.addPatch(view);
	}

	public void addPatchesOnLayout() {
		for (View view : views) {
			quilt.addPatch(view);
		}
	}

	public void removeQuilt(View view) {
		quilt.removeView(view);
	}

	public void setChildPadding(int padding) {
		this.padding = padding;
	}

	public void refresh() {
		quilt.refresh();
	}

	public void setOrientation(boolean isVertical) {
		this.isVertical = isVertical;
	}

	@Override
	public void onGlobalLayout() {
		// addPatchesOnLayout();
	}
	protected boolean haveInternet() {
		NetworkInfo info = ((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
	}
	public void checkDensityAndSetImage(ImageView imageView, String url,
										int fallBackId, String from, ProgressBar progress, boolean isCacheImage) {


		int density = getResources().getDisplayMetrics().densityDpi;

		String appendDensity = "";

		if (density >= 400) {
			// "xxxhdpi";
				appendDensity =  getContext().getString(R.string.image_xxxhdpi);
		} else if (density >= 300 && density < 400) {
			// xxhdpi
				appendDensity = getContext().getString(R.string.image_xxhdpi);
		} else if (density >= 200 && density < 300) {
			// xhdpi
				appendDensity =  getContext().getString(R.string.image_xhdpi);
		} else if (density >= 150 && density < 200) {
			// hdpi
				appendDensity = getContext().getString(R.string.image_hdpi);

		} else if (density >= 100 && density < 150) {
			// mdpi
				appendDensity = getContext().getString(R.string.image_mdpi);

		} else {
			// hdpi
				appendDensity = getContext().getString(R.string.image_hdpi);
		}
		url = url.concat(appendDensity);
		url.trim();
		if (null != progress) {
			new AQuery(imageView).progress(progress).image(url, isCacheImage, isCacheImage, 0, fallBackId, null,
					AQuery.FADE_IN);
		} else {
			new AQuery(imageView).image(url, isCacheImage, isCacheImage, 0, fallBackId, null,
					AQuery.FADE_IN);
		}

	}
}
