/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.account;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.olapic.OlapicBean;
import com.ulta.core.bean.olapic.OlapicDataBean;
import com.ulta.core.bean.olapic.OlapicEmbeddedBean;
import com.ulta.core.bean.olapic.OlapicImagesBean;
import com.ulta.core.bean.olapic.OlapicLinksBean;
import com.ulta.core.bean.olapic.OlapicMediaBean;
import com.ulta.core.bean.olapic.OlapicNextLinkBean;
import com.ulta.core.bean.olapic.OlapicUploadMediaBean;
import com.ulta.core.bean.olapic.OlapicUploadMediaDataBean;
import com.ulta.core.bean.olapic.OlapicUploadSuccessBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.olapic.custom.ScrollViewExt;
import com.ulta.core.olapic.custom.ScrollViewListener;
import com.ulta.core.olapic.grid.QuiltView;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;
import com.ulta.core.widgets.flyin.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class OlapicActivity .
 */

public class OlapicActivity extends UltaBaseActivity implements
		ScrollViewListener, OnDoneClickedListener {



	private RelativeLayout progressLayout;

	private OlapicBean olapicBean;

	private OlapicDataBean olapicDataBean;

	private OlapicEmbeddedBean olapicEmbeddedBean;

	private List<OlapicMediaBean> olapicMediaBean;

	private OlapicImagesBean olapicImagesBean;

	private OlapicLinksBean olapicLinksBean;

	private OlapicNextLinkBean olapicNextLinkBean;

	private OlapicUploadMediaBean olapicUploadMediaBean;

	private OlapicUploadMediaDataBean olapicUploadMediaDataBean;

	ImageView olapicImageView;

	private QuiltView quiltView;

	private String nextLinkService;

	private boolean isScrollable = false;

	private String olapicServiceId = "";

	private String caption = "";

	private String mediaId = "";

	private int count = 0;

	private final int ACTIVITY_SELECT_IMAGE = 1234;

	private String filePath;

	private byte[] byteArray;

	private Dialog uploadProgressDialog;



	private ArrayList<String> imageLink = new ArrayList<String>();
	private ArrayList<String> nickname = new ArrayList<String>();
	private ArrayList<String> avatar_url = new ArrayList<String>();
	private ArrayList<String> captionToBePassed = new ArrayList<String>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_olapic);
		setActivity(OlapicActivity.this);
		trackAppState(OlapicActivity.this, WebserviceConstants.HOME_PAGE_OLAPIC);
		titleBar = (TitleBar) findViewById(R.id.titlebar);


		if (null != UltaDataCache.getDataCacheInstance()
				.getOlpaicHomeGalleryHeadingText()) {
			setTitle(UltaDataCache.getDataCacheInstance()
					.getOlpaicHomeGalleryHeadingText());
		} else {
			setTitle("#Ulta Beauty GALLERY");
		}

		quiltView = (QuiltView) findViewById(R.id.quilt);
		progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);

		quiltView.setGridListener(this);


		if (null != UltaDataCache.getDataCacheInstance().getStreamId()
				&& !UltaDataCache.getDataCacheInstance().getStreamId()
						.equalsIgnoreCase("")) {
			olapicServiceId = UltaDataCache.getDataCacheInstance()
					.getStreamId();
		}
		invokeOlapicImages(olapicServiceId);
		UltaDataCache.getDataCacheInstance().setOlapic(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.olapic_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPause() {
		super.onPause();
		UltaDataCache.getDataCacheInstance().setOlapic(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(OlapicActivity.this, item.getTitle(), Toast.LENGTH_LONG)
				.show();
		return super.onOptionsItemSelected(item);

	}

	private void invokeOlapicImages(String olapicServiceId) {
		String olapicService = "";

		progressLayout.setVisibility(View.VISIBLE);
		InvokerParams<OlapicBean> invokerParams = new InvokerParams<OlapicBean>();
		if (isScrollable) {
			olapicService = olapicServiceId + "&";
		} else {
			olapicService = getResources().getString(R.string.olapic_service,
					olapicServiceId);
		}
		UltaDataCache.getDataCacheInstance().setOlapic(true);
		invokerParams.setServiceToInvoke(olapicService);
		invokerParams.setHttpMethod(HttpMethod.GET_OLAPIC);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(fnPopulateOlapicImagesHandlerParameters(
				WebserviceConstants.OLAPIC_VERSION,
				WebserviceConstants.OLAPIC_AUTH_TOKEN));
		invokerParams.setUltaBeanClazz(OlapicBean.class);
		OlapicImagesHandler olapicImagesHandler = new OlapicImagesHandler();
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
		invokerParams.setUltaHandler(olapicImagesHandler);
		invokerParams.setCookieHandlingSkip(true);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<OlapicActivity><invokeOlapicImages()><UltaException>>"
					+ ultaException);
		}
	}

	/**
	 * Method to populate the URL parameter map.
	 * 
	 * @param id
	 *            the id
	 * @return the map
	 */
	private Map<String, String> fnPopulateOlapicImagesHandlerParameters(
			String id, String authToken) {
		Map<String, String> urlParams = new HashMap<String, String>();

		if (isScrollable) {
			urlParams.put("&auth_token", authToken);
		} else {
			urlParams.put("auth_token", authToken);
		}
		urlParams.put("version", id);
		return urlParams;
	}

	public class OlapicImagesHandler extends UltaHandler {

		public void handleMessage(Message msg) {

			try {
				if (null != getErrorMessage()) {
					try {
						Logger.Log("ERROR");
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								OlapicActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
					}
				} else {

					Logger.Log("<OlapicImagesHandler><handleMessage><getResponseBean>>"
							+ (getResponseBean()));
					parseStreamsAll();
					olapicBean = (OlapicBean) getResponseBean();
					if (null != olapicBean) {
						olapicDataBean = olapicBean.getData();

						if (null != olapicDataBean) {
							olapicEmbeddedBean = olapicDataBean.get_embedded();

							if (null != olapicEmbeddedBean) {

								olapicMediaBean = olapicEmbeddedBean.getMedia();

								if (null != olapicMediaBean) {

									for (int i = 0; i < olapicMediaBean.size(); i++) {

										olapicImagesBean = olapicMediaBean.get(
												i).getImages();

										if (null != olapicImagesBean) {
											count = 0;
											nickname.add(olapicMediaBean.get(i)
													.get_embedded()
													.getUploader().getName());
											captionToBePassed
													.add(olapicMediaBean.get(i)
															.getCaption());
											imageLink.add(olapicMediaBean
													.get(i).getImages()
													.getMobile());

											avatar_url.add(olapicMediaBean
													.get(i).get_embedded()
													.getUploader()
													.getAvatar_url());
											inflateOlapicImages(i);

										}
									}

								}
							}

						}
					}
					// olapicProgressBar.setVisibility(View.GONE);
					progressLayout.setVisibility(View.GONE);
				}

			} catch (Exception e) {
				progressLayout.setVisibility(View.GONE);
			}

		}

	}

	public void inflateOlapicImages(int position) {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		olapicImageView = new ImageView(this);
		final ImageView iv = new ImageView(this);

		new AQuery(iv).image(olapicMediaBean.get(position).getImages()
				.getMobile(), true, true, 0, 0, null, AQuery.FADE_IN);

		quiltView.addPatchImage(iv);

		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = quiltView.indexOfPatch(iv);

				Intent olapicImageDetailsIntent = new Intent(
						OlapicActivity.this, OlapicImageDetailsActivity.class);
				olapicImageDetailsIntent.putExtra("imageLink",
						imageLink.get(index));
				olapicImageDetailsIntent.putExtra("nickName",
						nickname.get(index));
				olapicImageDetailsIntent.putExtra("avatar_url",
						avatar_url.get(index));
				olapicImageDetailsIntent.putExtra("caption",
						captionToBePassed.get(index));
				olapicImageDetailsIntent.putExtra("fromPDP", false);
				olapicImageDetailsIntent.putExtra("index", index);
				startActivity(olapicImageDetailsIntent);
			}
		});

	}

	@Override
	public void onScrollChanged(ScrollViewExt scrollView, int x, int y,
			int oldx, int oldy) {

		isScrollable = true;
		View view = (View) scrollView
				.getChildAt(scrollView.getChildCount() - 1);

		int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
				.getScrollY()));

		if (count == 0) {

			if (diff == 0) {

				if (null != olapicDataBean.get_links()) {
					if (null != olapicDataBean.get_links().getNext()) {
						if (null != olapicDataBean.get_links().getNext()
								.getHref()) {
							// olapicImageScrollView.setEnabled(false);
							nextLinkService = olapicDataBean.get_links()
									.getNext().getHref();
							invokeOlapicImages(nextLinkService);
							count++;
						}
					}

				}

			}
		}

	}


	public void invokeMediaIdService() {

		InvokerParams<OlapicUploadMediaBean> invokerParams = new InvokerParams<OlapicUploadMediaBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.OLAPIC_UPLOAD_MEDIA_FIRST);
		invokerParams.setHttpMethod(HttpMethod.POST_OLAPIC);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateMediaIdParameters());
		invokerParams.setUltaBeanClazz(OlapicUploadMediaBean.class);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
		OlapicUploadMediaHandler olapicUploadMediaHandler = new OlapicUploadMediaHandler();
		invokerParams.setUltaHandler(olapicUploadMediaHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
					+ ultaException);
			uploadProgressDialog.dismiss();
		}
	}

	private Map<String, String> populateMediaIdParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();

		if (null == UltaDataCache.getDataCacheInstance().getLoginName()) {
			SharedPreferences staySignedInSharedPref = getSharedPreferences(
					WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);
			String secretKey = staySignedInSharedPref.getString(
					WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
			String userName = staySignedInSharedPref.getString(
					WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");
			if (null != userName && !userName.trim().equalsIgnoreCase("")) {
				userName = Utility.decryptString(staySignedInSharedPref
						.getString(WebserviceConstants.STAY_SIGNED_IN_USERNAME,
								" "), secretKey);
				UltaDataCache.getDataCacheInstance().setLoginName(userName);
			}
		}

		urlParams.put("email", UltaDataCache.getDataCacheInstance()
				.getLoginName());
		urlParams.put("screen_name", UltaDataCache.getDataCacheInstance()
				.getLoginName());
		urlParams.put("avatar_url",
				getResources().getString(R.string.avatar_url));
		return urlParams;

	}

	public class OlapicUploadMediaHandler extends UltaHandler {

		public void handleMessage(Message msg) {

			try {
				if (null != getErrorMessage()) {
					try {
						uploadProgressDialog.dismiss();
						Logger.Log("ERROR");
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								OlapicActivity.this);
					} catch (WindowManager.BadTokenException e) {
						uploadProgressDialog.dismiss();
					} catch (Exception e) {
						uploadProgressDialog.dismiss();
					}
				} else {

					Logger.Log("<OlapicImagesHandler><handleMessage><getResponseBean>>"
							+ (getResponseBean()));

					olapicUploadMediaBean = (OlapicUploadMediaBean) getResponseBean();

					if (null != olapicUploadMediaBean) {
						olapicUploadMediaDataBean = olapicUploadMediaBean
								.getData();

						if (null != olapicUploadMediaDataBean) {
							mediaId = olapicUploadMediaDataBean.getId();
							uploadImageToOlapicService();
						}
					}
				}

			} catch (Exception e) {
				uploadProgressDialog.dismiss();
			}

		}

	}

	public void uploadImageToOlapicService() {

		InvokerParams<OlapicUploadSuccessBean> invokerParams = new InvokerParams<OlapicUploadSuccessBean>();
		invokerParams.setServiceToInvoke(getResources().getString(
				R.string.upload_image_url, mediaId));
		invokerParams.setHttpMethod(HttpMethod.MULTIPOST);
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams.setUrlParameters(populateUploadUrlParameters());
		invokerParams.setUltaBeanClazz(OlapicUploadSuccessBean.class);
		invokerParams.setImageByteArray(byteArray);
		invokerParams
				.setAdditionalRequestInformation(WebserviceConstants.OLAPIC);
		OlapicUploadSuccessHandler olapicUploadSuccessHandler = new OlapicUploadSuccessHandler();
		invokerParams.setUltaHandler(olapicUploadSuccessHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
					+ ultaException);
			uploadProgressDialog.dismiss();
		}
	}

	public Map<String, String> populateUploadUrlParameters() {

		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("caption", caption);
		urlParams.put("file", filePath);

	   /* urlParams.put(
				"stream_uri",
				getResources().getString(R.string.stream_uri,
						UltaDataCache.getDataCacheInstance().getStreamId()));*/

		return urlParams;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1234:
			if (resultCode == RESULT_OK) {
				uploadProgressDialog = new Dialog(OlapicActivity.this,
						R.style.ThemeDialogCustom);
				uploadProgressDialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				uploadProgressDialog
						.setContentView(R.layout.upload_progress_dialog);
				uploadProgressDialog.setCancelable(false);
				uploadProgressDialog.show();
				UltaDataCache.getDataCacheInstance().setOlapic(true);
				String filePath = data.getStringExtra("imagePath");
				caption = data.getStringExtra("caption");
				this.filePath = filePath;
				invokeMediaIdService();

			} else if (UltaDataCache.getDataCacheInstance().isFromCamera()) {
				UltaDataCache.getDataCacheInstance().setFromCamera(false);
				uploadProgressDialog = new Dialog(OlapicActivity.this,
						R.style.ThemeDialogCustom);
				uploadProgressDialog
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				uploadProgressDialog
						.setContentView(R.layout.upload_progress_dialog);
				uploadProgressDialog.setCancelable(false);
				uploadProgressDialog.show();
				UltaDataCache.getDataCacheInstance().setOlapic(true);
				this.filePath = UltaDataCache.getDataCacheInstance()
						.getCameraCapturedPath();
				caption = UltaDataCache.getDataCacheInstance()
						.getCameraCaption();
				invokeMediaIdService();
			}
		break;
		}

	}

	public class OlapicUploadSuccessHandler extends UltaHandler {

		public void handleMessage(Message msg) {
			uploadProgressDialog.dismiss();
			try {
				if (null != getErrorMessage()) {
					try {
						Logger.Log("ERROR");
						notifyUser(
								Utility.formatDisplayError(getErrorMessage()),
								OlapicActivity.this);
					} catch (WindowManager.BadTokenException e) {
					} catch (Exception e) {
						// uploadProgressDialog.dismiss();
					}
				} else {
					// uploadProgressDialog.dismiss();
					showUploadedSuccessfullyMessage();

				}

			} catch (Exception e) {
				// uploadProgressDialog.dismiss();
			}

		}

	}

	@Override
	public void onDoneClicked() {
		if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
			Intent intent = new Intent(OlapicActivity.this,
					OlapicUploadActivity.class);
			startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

		} else {
			Intent intentForLogin = new Intent(OlapicActivity.this,
					LoginActivity.class);
			startActivity(intentForLogin);
		}
	}

	public void showUploadedSuccessfullyMessage() {

		final Dialog alertDialog = showAlertDialog(
				OlapicActivity.this,
				"Upload Complete",
				"Your photo have succesfully been uploaded.Would you like to upload more photos?",
				"No", "Yes");

		mDisagreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
					Intent intent = new Intent(OlapicActivity.this,
							OlapicUploadActivity.class);
					startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

				} else {
					Intent intentForLogin = new Intent(OlapicActivity.this,
							LoginActivity.class);
					startActivity(intentForLogin);
				}
				alertDialog.cancel();
			}
		});
		mAgreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});

		alertDialog.setCancelable(false);
		// show it
		alertDialog.show();
	}

	public void setUpToolBar() {
		Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);

		if (null != toolbarTop) {
			toolbarTop.inflateMenu(R.menu.olapic_menu);
			toolbarTop.setNavigationIcon(R.drawable.hamburger_icon);
			toolbarTop
					.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem arg0) {
							if (arg0.toString().equals("UPLOAD")) {
								if (UltaDataCache.getDataCacheInstance()
										.isLoggedIn()) {
									Intent intent = new Intent(
											OlapicActivity.this,
											OlapicUploadActivity.class);
									startActivityForResult(intent,
											ACTIVITY_SELECT_IMAGE);

								} else {
									Intent intentForLogin = new Intent(
											OlapicActivity.this,
											LoginActivity.class);
									startActivity(intentForLogin);
								}
							}
							return false;
						}
					});
			toolbarTop.setNavigationOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					sideMenuClick();
				}
			});
		}
	}

	public void parseStreamsAll() {

		try {

			JSONObject jsonObj = new JSONObject(UltaDataCache
					.getDataCacheInstance().getOlapicResponse());

			JSONObject data = jsonObj.getJSONObject("data");

			if (null != data.getJSONObject("_embedded")) {

				JSONObject _embedded = data.getJSONObject("_embedded");

				JSONArray mediaArray = null;

				if (null != _embedded.getJSONArray("media")) {
					mediaArray = _embedded.getJSONArray("media");

					for (int i = 0; i < mediaArray.length(); i++) {

						JSONObject mediaObject = mediaArray.getJSONObject(i);

						if (null != mediaObject.getJSONObject("_embedded")) {
							JSONObject inner_embedded = mediaObject
									.getJSONObject("_embedded");

							if (null != inner_embedded
									.getJSONObject("streams:all")) {
								JSONObject streamsAllObject = inner_embedded
										.getJSONObject("streams:all");

								JSONObject _linkObject = streamsAllObject
										.getJSONObject("_links");

								JSONObject selfObject = _linkObject
										.getJSONObject("self");

								String href = selfObject.getString("href");
								UltaDataCache.getDataCacheInstance()
										.setShopThisLookHref(href);
							}

						}

					}

				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
