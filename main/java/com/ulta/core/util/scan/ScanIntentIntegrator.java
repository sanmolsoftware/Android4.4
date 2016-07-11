/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.google.zxing.integration.android.IntentIntegrator;
import com.ulta.core.activity.product.ScanActivity;

/**
 * Class that extends IntentIntegrator of Zxing lib.
 *
 * @author Infosys
 */
public final class ScanIntentIntegrator extends IntentIntegrator {
	/**
	 * The Constant PACKAGE.
	 */
    private static final String PACKAGE = "com.google.zxing.client.android";
    /**
	 * Instantiates a new scanIntentIntegrator.
	 */
   /* protected ScanIntentIntegrator() {
    }*/

    /**
     * See
     * {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence)}
     * -- same, but uses default English labels.
     */
    public static AlertDialog initiateScan(Activity activity) {
        return initiateScan(activity, DEFAULT_TITLE, DEFAULT_MESSAGE, DEFAULT_YES, DEFAULT_NO);
    }

    /**
     * See
     * {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence)}
     * -- same, but takes string IDs which refer to the {@link Activity}'s
     * resource bundle entries.
     */
    public static AlertDialog initiateScan(Activity activity, int stringTitle, int stringMessage, int stringButtonYes,
            int stringButtonNo) {
        return initiateScan(activity, activity.getString(stringTitle), activity.getString(stringMessage), activity
                .getString(stringButtonYes), activity.getString(stringButtonNo));
    }

    /**
     * See
     * {@link #initiateScan(Activity, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence)}
     * -- same, but scans for all supported barcode types.
     * 
     * @param stringTitle
     *            title of dialog prompting user to download Barcode Scanner
     * @param stringMessage
     *            text of dialog prompting user to download Barcode Scanner
     * @param stringButtonYes
     *            text of button user clicks when agreeing to download Barcode
     *            Scanner (e.g. "Yes")
     * @param stringButtonNo
     *            text of button user clicks when declining to download Barcode
     *            Scanner (e.g. "No")
     * @return an {@link AlertDialog} if the user was prompted to download the
     *         app, null otherwise
     */
    public static AlertDialog initiateScan(Activity activity, CharSequence stringTitle, CharSequence stringMessage,
            CharSequence stringButtonYes, CharSequence stringButtonNo) {

        return initiateScan(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo, ALL_CODE_TYPES);
    }

    /**
     * Invokes scanning.
     * 
     * @param stringTitle
     *            title of dialog prompting user to download Barcode Scanner
     * @param stringMessage
     *            text of dialog prompting user to download Barcode Scanner
     * @param stringButtonYes
     *            text of button user clicks when agreeing to download Barcode
     *            Scanner (e.g. "Yes")
     * @param stringButtonNo
     *            text of button user clicks when declining to download Barcode
     *            Scanner (e.g. "No")
     * @param stringDesiredBarcodeFormats
     *            a comma separated list of codes you would like to scan for.
     * @return an {@link AlertDialog} if the user was prompted to download the
     *         app, null otherwise
     * @throws InterruptedException
     *             if timeout expires before a scan completes
     */
    public static AlertDialog initiateScan(Activity activity, CharSequence stringTitle, CharSequence stringMessage,
            CharSequence stringButtonYes, CharSequence stringButtonNo, CharSequence stringDesiredBarcodeFormats) {

      //TODO Barcode scan
      Intent intentScan = new Intent(activity, ScanActivity.class);

        intentScan.addCategory(Intent.CATEGORY_DEFAULT);
        intentScan.setAction(PACKAGE + ".SCAN");


        try {
            activity.startActivityForResult(intentScan, REQUEST_CODE);
            return null;
        } catch (ActivityNotFoundException e) {
            return showDownloadDialog(activity, stringTitle, stringMessage, stringButtonYes, stringButtonNo);
        }
    }

    public static AlertDialog initiateScan(Activity activity, CharSequence scanMode) {
        Intent intentScan = new Intent(activity, ScanActivity.class);
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);
        intentScan.setAction(PACKAGE + ".SCAN");
        intentScan.putExtra("SCAN_MODE", scanMode);

        try {
            activity.startActivityForResult(intentScan, REQUEST_CODE);
            return null;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlertDialog initiateScanWithFormat(Activity activity, CharSequence scanFormat) {
        Intent intentScan = new Intent(activity, ScanActivity.class);
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);
        intentScan.setAction(PACKAGE + ".SCAN");
        intentScan.putExtra("SCAN_FORMATS", scanFormat);

        try {
            activity.startActivityForResult(intentScan, REQUEST_CODE);
            return null;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static AlertDialog showDownloadDialog(final Activity activity, CharSequence stringTitle,
            CharSequence stringMessage, CharSequence stringButtonYes, CharSequence stringButtonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
        downloadDialog.setTitle(stringTitle);
        downloadDialog.setMessage(stringMessage);
        downloadDialog.setPositiveButton(stringButtonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(stringButtonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


}
