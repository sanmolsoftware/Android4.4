package com.ulta.core.activity.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.search.SearchBean;
import com.ulta.core.bean.search.SearchResultsBean;
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

import static com.ulta.core.conf.UltaConstants.HOW_MANY;

public class GWPLandingActivity extends UltaBaseActivity {

	private ListView promotionsListView;
	private PromotionListAdapter mPromotionListAdapter;
	List<SearchResultsBean> products;
	private int previousSortSelection;
	private String sortSearch = "";
	private AlertDialog mSortDialog;
	private LinearLayout mSortLayout;
	private FrameLayout mSpeicalOfferLoading;
	int count = 0;
	int pageNum = 1;
	private List<SearchResultsBean> listWithAllProducts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_more_save_more_promotions);

		setTitle("GWP");
		promotionsListView = (ListView) findViewById(R.id.promotionsListView);
		mSortLayout = (LinearLayout) findViewById(R.id.sortLayout);
		mSpeicalOfferLoading = (FrameLayout) findViewById(R.id.specialOfferLoading);
		listWithAllProducts = new ArrayList<SearchResultsBean>();
		invokePromotionsList();

		mSortLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSortOptionsDialog();
			}
		});
	}

	/**
	 * Invoke Promotions.
	 * 
	 */
	private void invokePromotionsList() {

		InvokerParams<SearchBean> invokerParams = new InvokerParams<SearchBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.FETCH_PRODUCTS_FOR_GWP_FROM_SALE);
		invokerParams.setHttpMethod(HttpMethod.POST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateBuyMoreSaveMoreParameters());
		invokerParams.setUltaBeanClazz(SearchBean.class);

		PromotionsHandler promotionsHandler = new PromotionsHandler();
		invokerParams.setUltaHandler(promotionsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			mSpeicalOfferLoading.setVisibility(View.GONE);
			Logger.Log("<GWPLANDINGActivity><GWPLanding><UltaException>>"
					+ ultaException);
		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 */
	private Map<String, String> populateBuyMoreSaveMoreParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "2");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("pageNumber", "" + pageNum);
		urlParams.put("howMany", "10");
		urlParams.put("sortBy", sortSearch);
		urlParams.put("atg-rest-return-form-handler-properties", "true");
		urlParams.put("categoryDimId", "");
		urlParams.put("brandDimIds", "");
		urlParams.put("promotionDimIds", "");
		urlParams.put("minPrice", "");
		urlParams.put("maxPrice", "");

		return urlParams;
	}

	/**
	 * The Class LoginHandler.
	 */
	public class PromotionsHandler extends UltaHandler {

		/**
		 * Handle message.
		 * 
		 * @param msg
		 *            the msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			Logger.Log("<PromotionsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));

			if (null != getErrorMessage()) {
				mSpeicalOfferLoading.setVisibility(View.GONE);
				if (!getErrorMessage().startsWith("401")) {
					try {
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								GWPLandingActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				}
			} else {
				mSortLayout.setVisibility(View.VISIBLE);
				mSpeicalOfferLoading.setVisibility(View.GONE);
				// Logger.Log("<Promotions><handleMessage><getResponseBean>>"
				// + (getResponseBean()));
				SearchBean productsSearched = (SearchBean) getResponseBean();
				products = new ArrayList<SearchResultsBean>();

				products = productsSearched.getSearchResults();
				count = productsSearched.getTotalNoOfProducts();

				if (null != products) {
					listWithAllProducts.addAll(products);
				}
				if (null == mPromotionListAdapter) {
					mPromotionListAdapter = new PromotionListAdapter();
					promotionsListView.setAdapter(mPromotionListAdapter);
				} else {
					mPromotionListAdapter.notifyDataSetChanged();
				}

			}
		}
	}

	public class PromotionListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listWithAllProducts.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final int selectedPosition = position;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.promotions_text, null);
			}
			TextView promotionsDisplayName = (TextView) convertView
					.findViewById(R.id.promotionTextView);
			TextView promotionsDescriptionName = (TextView) convertView
					.findViewById(R.id.promotionDescriptionTextView);
			TextView promotionsExpiryDateName = (TextView) convertView
					.findViewById(R.id.promotionValidityTextView);
			ImageView promotionsImageView = (ImageView) convertView
					.findViewById(R.id.promotionsImageView);
			ImageView promotionsBadgeImageView = (ImageView) convertView
					.findViewById(R.id.promotionsBadgeImageView);
			LinearLayout promotionsLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.promotionsLinearLayout);
			if (position == (listWithAllProducts.size() - 1)
					&& position < (count - 1)
					&& !(count < Integer.parseInt(HOW_MANY))) {
				mSpeicalOfferLoading.setVisibility(View.VISIBLE);
				pageNum++;
				invokePromotionsList();
			} else {
				if (null != listWithAllProducts.get(position).getBrandName()) {
					promotionsDisplayName.setText(listWithAllProducts.get(
							position).getBrandName());
					promotionsDisplayName.setTypeface(
							setHelveticaRegulartTypeFace(), Typeface.BOLD);
				}

				if (null != listWithAllProducts.get(position)
						.getPromoOfferDate()
						&& null != listWithAllProducts.get(position)
								.getPromoOfferLink()) {
					promotionsExpiryDateName.setText(listWithAllProducts.get(
							position).getPromoOfferDate());
					promotionsDescriptionName.setText(listWithAllProducts.get(
							position).getPromoOfferLink());
				} else {
					if (null != listWithAllProducts.get(position)
							.getPromoDescription()) {
						promotionsDescriptionName.setText(listWithAllProducts
								.get(position).getPromoDescription());
					}
				}

				if (null != listWithAllProducts.get(position)
						.getMediumImageUrl()) {
					new AQuery(promotionsImageView).image(listWithAllProducts
							.get(position).getMediumImageUrl(), true, true, 0,
							R.drawable.ulta_logo_icon, null, AQuery.FADE_IN);
				} else {
					promotionsImageView
							.setBackgroundResource(R.drawable.ulta_logo_icon);
				}

				if (null != listWithAllProducts.get(position).getBadgeName()) {
					setBadgeImage(promotionsBadgeImageView, listWithAllProducts
							.get(position).getBadgeName());
				}

				promotionsLinearLayout
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent ultaProductListIntent = new Intent(
										GWPLandingActivity.this,
										UltaProductListActivity.class);
								ultaProductListIntent.putExtra(
										"promotionId",
										listWithAllProducts.get(
												selectedPosition)
												.getPromotionId());
								ultaProductListIntent
										.setAction("fromPromotion");
								ultaProductListIntent.putExtra("page", "gwp");
								String altText = "";

								if (null != listWithAllProducts.get(
										selectedPosition).getBrandName()) {
									altText = listWithAllProducts.get(
											selectedPosition).getBrandName();
								}
								ultaProductListIntent.putExtra("altText",
										altText);
								startActivity(ultaProductListIntent);

							}
						});
			}

			return convertView;
		}

		private void setBadgeImage(ImageView imgBadgeImage, String badgeName) {

			imgBadgeImage.setVisibility(View.VISIBLE);
			if (badgeName != null && !badgeName.isEmpty()) {
				imgBadgeImage.setVisibility(View.VISIBLE);
				if (badgeName.equals("onlineOnly_badge")) {
					imgBadgeImage.setImageResource(R.drawable.online_badge);
				} else if (badgeName.equals("isNew_badge")) {
					imgBadgeImage.setImageResource(R.drawable.badge_whats_new);
				} else if (badgeName.equals("gwp_badge")) {
					imgBadgeImage.setImageResource(R.drawable.badge_free_gift);
				} else if (badgeName.equals("onSale_badge")) {
					imgBadgeImage.setImageResource(R.drawable.badge_sale);
				} else if (badgeName.equals("ultaExclusive_badge")) {
					imgBadgeImage
							.setImageResource(R.drawable.badge_ulta_exclusive);
				} else if (badgeName.equals("ultaPick_badge")) {
					imgBadgeImage.setImageResource(R.drawable.badge_ulta_pick);
				} else if (badgeName.equals("fanFavorite_badge")) {
					imgBadgeImage.setImageResource(R.drawable.badge_fan_fave);
				} else if (badgeName.equals("inStoreOnly_badge")) {
					imgBadgeImage.setImageResource(R.drawable.badge_instore);
				} else if (badgeName.equals("comingSoon_badge")) {
					imgBadgeImage
							.setImageResource(R.drawable.badge_coming_soon);
				}

			} else {
				imgBadgeImage.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * Method to show sort options.
	 */
	public void showSortOptionsDialog() {
		final List<String> listToSort = new ArrayList<String>();
		listToSort.add("Best Seller");
		listToSort.add("New Arrivals");
		listToSort.add("Top Rated");
		final ArrayAdapter<String> adapter = new SpinnerAddapter(this,
				android.R.layout.simple_list_item_1, listToSort);
		final AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Sort By");
		b.setSingleChoiceItems(adapter, previousSortSelection,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						previousSortSelection = which;
						if (listToSort.get(which).equals("Best Seller")) {
							sortSearch = "bestSellers";
						} else if (listToSort.get(which).equals("New Arrivals")) {
							sortSearch = "isNew";
						} else if (listToSort.get(which).equals("Top Rated")) {
							sortSearch = "topRated";
						}
						mSpeicalOfferLoading.setVisibility(View.VISIBLE);
						invokePromotionsList();
					}
				});

		mSortDialog = b.create();
		mSortDialog.show();
	}

	class SpinnerAddapter extends ArrayAdapter<String> {
		int textViewResourceId;
		Context context;
		List<String> list;

		public SpinnerAddapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			this.textViewResourceId = textViewResourceId;
			this.context = context;
			this.list = objects;
		}

		@Override
		public View getView(int position, final View convertView,
				ViewGroup parent) {
			int pos = 3;
			if (position == pos) {
				LinearLayout lytButton = new LinearLayout(context);
				LayoutParams buttons = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lytButton.setGravity(Gravity.CENTER_HORIZONTAL);
				Button cancel = new Button(context);
				cancel.setText("Cancel");
				// cancel.setWidth(400);
				cancel.setLayoutParams(buttons);

				lytButton.addView(cancel);
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mSortDialog.dismiss();
					}
				});
				return lytButton;
			} else {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				TextView view = (TextView) inflater.inflate(textViewResourceId,
						null);
				view.setText(list.get(position));
				return view;
			}
		}

		@Override
		public int getCount() {
			return list.size() + 1;
		}
	}

}
