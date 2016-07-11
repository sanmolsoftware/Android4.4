package com.ulta.core.activity.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ulta.R;
import com.ulta.core.Ulta;
import com.ulta.core.activity.SetEnvironmentActivity;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.widgets.CirclePageIndicator;
import com.ulta.core.widgets.PageIndicator;

public class LearnMoreActivity extends UltaBaseActivity {
    ImageView imageView;
    LinearLayout imageLayout;
    Button joinNowBtn;
    private String[] learnMoreURLS = {WebserviceConstants.BENIFITS_URL, WebserviceConstants.EARNINGS_URL, WebserviceConstants.PLATINUMS_URL};
    private FrameLayout mProgressLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Ulta.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_learn_more);
        setTitle("Ultamate Rewards");
        init();
    }

    private void init() {
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        mProgressLayout = (FrameLayout) findViewById(R.id.progressLayout);
        populateImagesInLayout();
        joinNowBtn = (Button) findViewById(R.id.joinNowBtn);
        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ViewTreeObserver observer = imageLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                checkImageVisibility();
            }
        });
    }

    private void checkImageVisibility() {
        int height = imageLayout.getHeight();
        if (height > 9500) {
            mProgressLayout.setVisibility(View.GONE);
        }
    }

    private void populateImagesInLayout() {
        imageLayout.removeAllViews();
        for (int i = 0; i < learnMoreURLS.length; i++) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater
                    .inflate(R.layout.learnore_image_layout, null);
            imageView = (ImageView) view
                    .findViewById(R.id.carouselImage);
            checkDensityAndSetImage(imageView,
                    learnMoreURLS[i],
                    0, "LearnMore", null, false);
            imageLayout.addView(view, i);
        }
    }
}
