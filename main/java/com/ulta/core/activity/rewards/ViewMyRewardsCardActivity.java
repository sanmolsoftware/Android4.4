/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.rewards;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

/**
 * This activity is defined to show the generated bar code to the user in his
 * view my rewards card added for the 3.2 release
 */
public class ViewMyRewardsCardActivity extends UltaBaseActivity implements
		OnDoneClickedListener
{
	/** String to hold the member id value */
	private String memberId;
	/** Declaration of text views */
	private TextView txtMemberId, txtRewardsProgram;
	/** Declaration of image view */
	private ImageView imgBarCode;
	/** Screen size attributes */
	int screenWidth, screenHeight;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		setContentView(R.layout.view_my_rewards_card);
		setViews();
	}

	// This method sets the views of the layout
	private void setViews() {
		// Initializing the views
		txtMemberId = (TextView) findViewById(R.id.txtRewardsMemberId);
		txtRewardsProgram = (TextView) findViewById(R.id.txtRewardsProgram);
		imgBarCode = (ImageView) findViewById(R.id.img_2D_bar_code);
		if (null != getIntent().getExtras()) {
			if (null != getIntent().getExtras().getString("MemberId")) {
				memberId = getIntent().getExtras().getString("MemberId");
			}
			if (null != getIntent().getExtras().getString("from")
					&& getIntent().getExtras().getString("from")
							.equals("clubatulta")) {
				txtRewardsProgram.setText("The Club at Ulta");
			}
		}
		if (memberId == null || memberId.isEmpty()) {
			notifyUser("Rewards card can not be generated",
					ViewMyRewardsCardActivity.this);
			finish();
		}
		txtMemberId.setText("Member ID# " + memberId);
		Bitmap bitmap = null;

		try {
			bitmap = encodeAsBitmap(memberId, BarcodeFormat.EAN_13,
					screenWidth, screenHeight / 3);
			// Setting the QR code generated
			imgBarCode.setImageBitmap(bitmap);

		}
		catch (WriterException e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method encodes the data as bitmap which takes string to be encoded,
	 * format for encryption,width and height as parameters
	 */
	private Bitmap encodeAsBitmap(String contentsToEncode,
			BarcodeFormat format, int img_width, int img_height)
			throws WriterException {

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = null;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height);
		}
		catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}
		if (result != null) {
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					// Below line of code generates black and white code
					pixels[offset + x] = result.get(x, y) ? 0xFF000000
							: 0xFFFFFFFF;
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		}
		else
			return null;
	}

	@Override
	public void onDoneClicked() {
		finish();

	}
}
