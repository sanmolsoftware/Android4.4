/**
 * 
 */
package com.ulta.core.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.widgets.CirclePageIndicator;
import com.ulta.core.widgets.PageIndicator;

public class CarouselActivity extends UltaBaseActivity {
	private ViewPager mCardsViewPager;
	ImageView imageView;
	RelativeLayout carouselImageLayout;
	PageIndicator mIndicator;
//	private LinearLayout mButtonLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		if (!Ulta.isTablet(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		setContentView(R.layout.carousel_layout);

		mCardsViewPager = (ViewPager) findViewById(R.id.carouselPager);
		mCardsViewPager.setAdapter(new CardsPagerAdapter());
		mCardsViewPager.setOffscreenPageLimit(3);

//		mButtonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mCardsViewPager);
		mIndicator.setActivity(this);
	}

	private class CardsPagerAdapter extends PagerAdapter {

		/*private int[] mCards = { R.drawable.carousel_1, R.drawable.carousel_2,
				R.drawable.carousel_3, R.drawable.carousel_4 };*/
		private int[] mCards = { R.drawable.welcome_screen };

		@SuppressWarnings("deprecation")
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View cardImageView = getLayoutInflater().inflate(
					R.layout.carousel_image, null);
			carouselImageLayout = (RelativeLayout) cardImageView
					.findViewById(R.id.carouselImageLayout);
			imageView = (ImageView) cardImageView
					.findViewById(R.id.carouselImage);
			imageView.setImageDrawable(getResources().getDrawable(
					mCards[position]));
			imageView.setTag(position);
			container.addView(cardImageView);
			
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {

					Intent mainIntent = new Intent(CarouselActivity.this,
							HomeActivity.class);
					mainIntent.putExtra("launch", "true");
					if (getSharedPreference()) {
						setSharedPreference();
						if (WebserviceConstants.isShowEnvironmentPopUp) {
							mainIntent = new Intent(CarouselActivity.this,
									SetEnvironmentActivity.class);
							mainIntent.putExtra("isFirstLaunch", true);
							startActivity(mainIntent);
							CarouselActivity.this.finish();
						} else {
							CarouselActivity.this.startActivity(mainIntent);
							CarouselActivity.this.finish();
						}
					} else {
						mainIntent.putExtra("isFirstLaunch", true);
						CarouselActivity.this.startActivity(mainIntent);
						CarouselActivity.this.finish();
					}
				
					
				}
			});
			
			return cardImageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mCards.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	public void onPagescrolledEvent(int position, float positionOffset) {

		/*mButtonLayout.removeAllViews();
		if (position == 3) {
			ImageView textView = new ImageView(this);
			textView.setImageResource(R.drawable.skip);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent mainIntent = new Intent(CarouselActivity.this,
							HomeActivity.class);
					mainIntent.putExtra("launch", "true");
					if (getSharedPreference()) {
						setSharedPreference();
						if (WebserviceConstants.isShowEnvironmentPopUp) {
							mainIntent = new Intent(CarouselActivity.this,
									SetEnvironmentActivity.class);
							mainIntent.putExtra("isFirstLaunch", true);
							startActivity(mainIntent);
							CarouselActivity.this.finish();
						} else {
							CarouselActivity.this.startActivity(mainIntent);
							CarouselActivity.this.finish();
						}
					} else {
						mainIntent.putExtra("isFirstLaunch", true);
						CarouselActivity.this.startActivity(mainIntent);
						CarouselActivity.this.finish();
					}
				}
			});

			mButtonLayout.addView(textView);

		}*/

	}

	private Boolean getSharedPreference() {
		SharedPreferences preferences = this.getSharedPreferences(
				"userdetails", MODE_PRIVATE);
		Boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
		return isFirstTime;
	}

	private void setSharedPreference() {
		SharedPreferences preferences = this.getSharedPreferences(
				"userdetails", MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.clear();
		edit.putBoolean("isFirstTime", false);
		edit.commit();
	}

}
