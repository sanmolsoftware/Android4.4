/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * The class is a wrapper for {@link CaptureActivity}. override methods for
 * showing menu and disables first time help <br/>
 * <br/>
 * UPDATE ZXING LIBRARY NOTES:<br/>
 * 1. Copy the source both core and android client to src folder of this project<br/>
 * 2. Change {@link CaptureActivity} to a non final class<br/>
 * 3. Change access specifier for
 * {@linkplain CaptureActivity#showHelpOnFirstLaunch} from private to protected<br/>
 * 4. Update {@link IntentIntegrator} with this {@linkplain ScanView} instead of
 * {@link CaptureActivity}<br/>
 * 5. Change access specifier for {@link CaptureActivity#source} from private to
 * protected <br/>
 * 6. Change access specifier for enum {@link CaptureActivity#Source} from
 * private to protected <br/>
 */
public class ScanActivity extends CaptureActivity
{

	/**
	 * @see com.google.zxing.client.android.CaptureActivity#onCreateOptionsMenu(android
	 *      .view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * @see com.google.zxing.client.android.CaptureActivity#onOptionsItemSelected
	 *      (android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	/**
	 * @see com.google.zxing.client.android.CaptureActivity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * @see com.google.zxing.client.android.CaptureActivity#showHelpOnFirstLaunch()
	 */
//	@Override
//	protected boolean showHelpOnFirstLaunch() {
//		return true;
//	}

	/**
	 * @see com.google.zxing.client.android.CaptureActivity#handleDecodeExternally(com.google.zxing.Result,
	 *      com.google.zxing.client.android.result.ResultHandler,
	 *      android.graphics.Bitmap)
	 */
	@Override
	protected void handleDecodeExternally(final Result rawResult,
			ResultHandler resultHandler, final Bitmap barcode) {
		final ViewfinderView viewfinderView = getViewfinderView();
		final TextView statusView = (TextView) findViewById(R.id.status_view);
		viewfinderView.drawResultBitmap(barcode);
		final ViewGroup resultView = (ViewGroup) findViewById(R.id.result_view);
		resultView.setVisibility(View.VISIBLE);
		final View stat = resultView.getChildAt(0);
		stat.setVisibility(View.INVISIBLE);
//		final View shbtn = findViewById(R.id.shopper_button);
//		shbtn.setVisibility(View.GONE);
		statusView.setText("Scanned barcode");
		final ViewGroup buttonView = (ViewGroup) findViewById(R.id.result_button_view);
		buttonView.requestFocus();
		buttonView.setVisibility(View.GONE);
		Handler t = new Handler();
		t.postDelayed(new Runnable() {

			@Override
			public void run() {
				final Intent intent = new Intent(getIntent().getAction());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
				intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult
						.getBarcodeFormat().toString());
				final byte[] rawBytes = rawResult.getRawBytes();
				if ((rawBytes != null) && (rawBytes.length > 0)) {
					intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
				}
				final Message message = Message.obtain(getHandler(),
						R.id.return_scan_result);
				message.obj = intent;
				getHandler().sendMessage(message);
				viewfinderView.drawViewfinder();
				barcode.recycle();

			}
		}, 500);
	}
	
	@Override
	public void getBarcodeEntered(String barcode) {
		 Intent intent = new Intent(getIntent().getAction());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra(Intents.Scan.RESULT, barcode);
			intent.putExtra(Intents.Scan.RESULT_FORMAT, barcode);
			final byte[] rawBytes = barcode.getBytes();
			if ((rawBytes != null) && (rawBytes.length > 0)) {
				intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
			}
			final Message message = Message.obtain(getHandler(),
					R.id.return_scan_result);
			message.obj = intent;
			getHandler().sendMessage(message);
	}
}
