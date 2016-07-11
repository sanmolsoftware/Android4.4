/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.product.DataBean;
import com.ulta.core.bean.product.ReviewBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ReviewAndRatingActivity.
 */
public class ReviewAndRatingActivity extends UltaBaseActivity {
	private ListView reviewsAndRatingList;
	private String id, productName;
	private String mProductPrice;
	private String mMiniDescription;
	private ReviewBean reviewBean;
	private List<DataBean> dataBean;
	private Spinner sortOption;
	private ImageView imgRating1;
	private ImageView imgRating2;
	private ImageView imgRating3;
	private ImageView imgRating4;
	private ImageView imgRating5;
	private TextView mProductpriceTextView;
	private TextView mProductDescriptionTextView;
	private TextView writeReviewTextView;
	private LinearLayout loadingDialog;
	private ArrayList<String> result;
	private TextView txtProductName;
	private String[] anArrayofStrings;
	ReivewAdapter newAdapter = new ReivewAdapter();
	private int currentPageNumber;
	private String sortBy = "created_date_desc";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_reviewandrating);
		setTitle("Customer Reviews");
		writeReviewTextView = (TextView) findViewById(R.id.writeReviewButton);
		sortOption = (Spinner) findViewById(R.id.spinnerSort);
		reviewsAndRatingList = (ListView) findViewById(R.id.reviewandratinglist);
		loadingDialog = (LinearLayout) findViewById(R.id.loadingDialog);
		if (null != getIntent().getExtras()) {
			id = getIntent().getExtras().getString("id");
			productName = getIntent().getExtras().getString("productName");
			mProductPrice = getIntent().getExtras().getString("productPrice");
			mMiniDescription = getIntent().getExtras().getString("productDesc");
		}

		txtProductName = (TextView) findViewById(R.id.txtProductName);
		txtProductName.setText(productName);
		mProductpriceTextView = (TextView) findViewById(R.id.txtProductPrice);
		mProductDescriptionTextView = (TextView) findViewById(R.id.txtProductDescription);

		try {
			mProductpriceTextView.setText(getResources().getString(
					R.string.dollar_sign)
					+ String.format("%.2f", Double.valueOf(mProductPrice)));
		} catch (NumberFormatException e) {
			mProductpriceTextView.setText(getResources().getString(
					R.string.dollar_sign)
					+ mProductPrice);
		} catch (NotFoundException e) {
			mProductpriceTextView.setText(getResources().getString(
					R.string.dollar_sign)
					+ mProductPrice);
		}

		mProductDescriptionTextView.setText(mMiniDescription);
		result = new ArrayList<String>();
		result.add("Recent");
		result.add("Oldest");
		result.add("Rating");
		result.add("Helpful");

		anArrayofStrings = new String[result.size()];
		result.toArray(anArrayofStrings);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				ReviewAndRatingActivity.this,
				android.R.layout.simple_spinner_item, anArrayofStrings);
		sortOption.setAdapter(spinnerArrayAdapter);
		sortOption.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int loc;
				loc = arg2;

				switch (loc) {
				case 1:
					sortBy = "created_date_asc";
					break;
				case 2:
					sortBy = "created_date_desc";
					break;
				case 3:
					sortBy = "review_rating_asc";
					break;
				case 4:
					sortBy = "helpful";
					break;
				default:
					sortBy = "2012/09/13";
					break;
				}
				loadingDialog.setVisibility(View.VISIBLE);
				fnInvokeRetrieveProductReviews(id, currentPageNumber);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		writeReviewTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent writeReviewIntent = new Intent(
						ReviewAndRatingActivity.this, WriteReviewActivity.class);
				writeReviewIntent.putExtra("id", id);
				writeReviewIntent.putExtra("productName", productName);
				writeReviewIntent.putExtra("productPrice", mProductPrice);
				writeReviewIntent.putExtra("prodMiniDescription",
						mMiniDescription);
				startActivity(writeReviewIntent);
			}
		});
		loadingDialog.setVisibility(View.VISIBLE);
		fnInvokeRetrieveProductReviews(id, currentPageNumber);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void fnInvokeRetrieveProductReviews(String id, int currentPage) {
		InvokerParams<ReviewBean> invokerParams = new InvokerParams<ReviewBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.DISPLAY_REVIEWS_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(fnPopulateRetrieveProductReviewsParameters(
						id, currentPage));
		invokerParams.setUltaBeanClazz(ReviewBean.class);
		RetrieveProductReviewsHandler retrieveProductReviewsHandler = new RetrieveProductReviewsHandler();
		invokerParams.setUltaHandler(retrieveProductReviewsHandler);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.POWER_REVIEWS_CONTEXT);
		invokerParams.setCookieHandlingSkip(true);

		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log(ultaException);
		}
	}

	private Map<String, String> fnPopulateRetrieveProductReviewsParameters(
			String id, int currentPage) {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("apikey", WebserviceConstants.API_KEY_POWER_REVIEWS);
		urlParams.put("page_id", id);
		urlParams.put("merchant_id",
				WebserviceConstants.MERCHANT_ID_POWER_REVIEWS);
		urlParams.put("page_size", WebserviceConstants.PAGE_SIZE_POWER_REVIEWS);
		urlParams.put("page", String.valueOf(currentPage));
		urlParams.put("sort", sortBy);
		return urlParams;
	}

	public class RetrieveProductReviewsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("reviews error" + (getErrorMessage()));
			loadingDialog.setVisibility(View.GONE);
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							ReviewAndRatingActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Logger.Log("<RetrieveProductDetailsHandler><handleMessage><getResponseBean>>"
						+ (getResponseBean()));
				reviewBean = (ReviewBean) getResponseBean();
				if (null == reviewBean) {
					/*
					 * final AlertDialog alertDialog = new AlertDialog.Builder(
					 * ReviewAndRatingActivity.this).create();
					 * alertDialog.setCancelable(false);
					 * alertDialog.setTitle("Sorry"); alertDialog
					 * .setMessage("There is no reviews for the product" +
					 * productName); alertDialog.setButton("OK", new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick(DialogInterface arg0, int
					 * arg1) { alertDialog.dismiss();
					 * 
					 * } }); alertDialog.show();
					 */

					String messageToBeDisplayed = "There is no reviews for the product"
							+ productName;
					final Dialog alertDialog = showAlertDialog(
							ReviewAndRatingActivity.this, "Sorry",
							messageToBeDisplayed, "Ok", "");
					alertDialog.show();

					mDisagreeButton.setVisibility(View.GONE);
					mAgreeButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							alertDialog.dismiss();
						}
					});
				}

				else

				if (null != reviewBean) {
					if (null == dataBean) {
						dataBean = reviewBean.getData();
						newAdapter = new ReivewAdapter();
						reviewsAndRatingList.setAdapter(newAdapter);
					} else {

						dataBean.addAll(reviewBean.getData());

						Logger.Log("<<<<" + reviewBean.getData().size());
						newAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	class ReivewAdapter extends BaseAdapter {
		public int getCount() {
			return dataBean.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.review_ratings_item, null);
			TextView headline = (TextView) convertView
					.findViewById(R.id.headline);
			TextView created_date = (TextView) convertView
					.findViewById(R.id.created_date);
			TextView comments = (TextView) convertView
					.findViewById(R.id.comments);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView rating = (TextView) convertView.findViewById(R.id.rating);
			TextView mVerifiedBuyerTextView = (TextView) convertView
					.findViewById(R.id.verifiedBuyerTextView);

			imgRating1 = (ImageView) convertView.findViewById(R.id.imgRating1);
			imgRating2 = (ImageView) convertView.findViewById(R.id.imgRating2);
			imgRating3 = (ImageView) convertView.findViewById(R.id.imgRating3);
			imgRating4 = (ImageView) convertView.findViewById(R.id.imgRating4);
			imgRating5 = (ImageView) convertView.findViewById(R.id.imgRating5);

			headline.setText(dataBean.get(position).getHeadline());
			created_date.setText(dataBean.get(position).getCreated_date());
			comments.setText(dataBean.get(position).getComments());
			name.setText("By" + "  " + dataBean.get(position).getName());
			rating.setText("(" + dataBean.get(position).getRating() + ")");

			if (null != dataBean) {

				if (null != dataBean.get(position)) {
					String verifiedBuyer = dataBean.get(position)
							.getReviewer_type();
					if (null != verifiedBuyer
							&& verifiedBuyer.equalsIgnoreCase("Verified Buyer")) {
						mVerifiedBuyerTextView.setVisibility(View.VISIBLE);
						mVerifiedBuyerTextView.setText(dataBean.get(position)
								.getReviewer_type());
					}
				}

				float imgRating = (float) (dataBean.get(position).getRating());

				if (imgRating == 0) {
					imgRating1.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 0.5) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_halfcolored);
					imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 1) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 1.5) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_halfcolored);
					imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 2) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 2.5) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3
							.setBackgroundResource(R.drawable.icon_star_halfcolored);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 3) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating4.setBackgroundResource(R.drawable.icon_star_gray);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 3.5) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating4
							.setBackgroundResource(R.drawable.icon_star_halfcolored);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 4) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating4
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating5.setBackgroundResource(R.drawable.icon_star_gray);
				} else if (imgRating == 4.5) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating4
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating5
							.setBackgroundResource(R.drawable.icon_star_halfcolored);

				} else if (imgRating == 5) {
					imgRating1
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating2
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating3
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating4
							.setBackgroundResource(R.drawable.icon_star_coloured);
					imgRating5
							.setBackgroundResource(R.drawable.icon_star_coloured);
				}

				int noOfProductsPerPage = Integer
						.parseInt(WebserviceConstants.PAGE_SIZE_POWER_REVIEWS);
				if (position == (currentPageNumber * noOfProductsPerPage + noOfProductsPerPage) - 1) {
					currentPageNumber++;
					loadingDialog.setVisibility(View.VISIBLE);
					fnInvokeRetrieveProductReviews(id, currentPageNumber);
				}
			}
			return convertView;

		}

	}

}
