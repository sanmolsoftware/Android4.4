package com.ulta.core.activity.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.PinchZoomActivity;
import com.ulta.core.bean.product.MobileCouponInfoBean;
import com.ulta.core.conf.WebserviceConstants;

import java.util.List;

public class MobileOffersActivity extends UltaBaseActivity {

	private ImageView mCouponImage;
	private String mCouponUrl;
	private ViewPager mCoupenViewPager;
	List<MobileCouponInfoBean> mMobileCouponInfoBeanList;
	TextView mCoupenPosition;
	int coupenPosition;
	//ArrayList<String> slideShowBean;
	String[] slideShowBean;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_offers);
		setTitle(getResources().getString(R.string.mobile_offer_title));
		if (null != getIntent().getExtras().getString("title")) {
			setTitle(getIntent().getExtras().getString("title"));
		}
		mCouponImage = (ImageView) findViewById(R.id.couponImage);
		mCoupenViewPager = (ViewPager) findViewById(R.id.coupenViewPager);

		trackAppState(this, WebserviceConstants.ACCOUNT_MOBILE_OFFERS);

		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("couponUrl")) {
				mCouponUrl = getIntent().getExtras().getString("couponUrl");
				new AQuery(mCouponImage).image(mCouponUrl, true, true, 0,
						R.drawable.dummy_product, null, AQuery.FADE_IN);
			}
			if (null != getIntent().getSerializableExtra("couponBean")) {
				slideShowBean = getIntent().getStringArrayExtra("couponBean");
				// mMobileCouponInfoBeanList = slideShowBean
				// .getMobileCouponInfo();
			}

			coupenPosition = getIntent().getIntExtra("position", 0);

		}
		mCoupenViewPager.setAdapter(new CoupenAdapter());
		mCoupenViewPager.setCurrentItem(coupenPosition);
		mCouponImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent ZoomIntent = new Intent(MobileOffersActivity.this,
						PinchZoomActivity.class);
				ZoomIntent.putExtra("imageUrl", mCouponUrl);
				startActivity(ZoomIntent);
			}
		});

	}

	private class CoupenAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View coupenImageView = getLayoutInflater().inflate(
					R.layout.coupon_common_view, null);
			ImageView imageView = (ImageView) coupenImageView
					.findViewById(R.id.carouselImage);
			ImageView arrow_left = (ImageView) coupenImageView
					.findViewById(R.id.arrow_left);
			ImageView arrow_right = (ImageView) coupenImageView
					.findViewById(R.id.arrow_right);
			LinearLayout coupenPositionLayout = (LinearLayout) coupenImageView
					.findViewById(R.id.coupenPositionLayout);
			if (slideShowBean.length == 1) {
				coupenPositionLayout.setVisibility(View.GONE);
			}
			if (position == 0) {
				arrow_left.setVisibility(View.INVISIBLE);
			} else if (position == slideShowBean.length - 1) {
				arrow_right.setVisibility(View.INVISIBLE);
			}
			final int positionOfArrow = position;
			arrow_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
								if (v.getVisibility() == View.VISIBLE) {
						mCoupenViewPager.setCurrentItem(positionOfArrow - 1);
					}
				}
			});
			arrow_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
								if (v.getVisibility() == View.VISIBLE) {
						mCoupenViewPager.setCurrentItem(positionOfArrow + 1);
					}
				}
			});
			//
			// if(arrow_right.getVisibility()==View.VISIBLE)
			// {
			// mCoupenViewPager.setCurrentItem(position+1);
			// }
			mCoupenPosition = (TextView) coupenImageView
					.findViewById(R.id.coupenPosition);
			mCoupenPosition.setText("Coupon " + (position + 1) + " of "
					+ slideShowBean.length);
			if (null != slideShowBean[position]
					&& null != slideShowBean[position]) {
				
				if (slideShowBean[position].startsWith("http")) {
					new AQuery(imageView)
					.image(slideShowBean[position], true, true, 0,
							R.drawable.dummy_product, null, AQuery.FADE_IN);
				} else {
					new AQuery(imageView)
					.image("http://"+slideShowBean[position], true, true, 0,
							R.drawable.dummy_product, null, AQuery.FADE_IN);
				}
				container.addView(coupenImageView);
			}
			else
			{
				coupenImageView.setVisibility(View.GONE);
			}
			// imageView.setImageDrawable(getResources().getDrawable(
			// mCards[position]));
			// imageView.setTag(position);
			
			return coupenImageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return slideShowBean.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
