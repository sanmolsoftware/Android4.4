package com.ulta.core.activity.account;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.account.BeautyPreferenceCategoryList;
import com.ulta.core.bean.account.NewBeautyPreferencesBean;
import com.ulta.core.bean.account.NewPrefValuesBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.net.WebserviceUtility;
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

public class BeautyPreferenceActivity extends UltaBaseActivity implements
		android.widget.RadioGroup.OnCheckedChangeListener {

	private List<String> beautyPrefHeaderCategories = new ArrayList<String>();

	private List<NewPrefValuesBean> mNewPrefValuesBean;

	private String currentCategoryName = "abc";
	private String previousCategoryName = "def";

	private String currentFirstChildName = "abc";
	private String previousFirstChildName = "def";

	private LinearLayout mCategoriesContainerLayout;
	private LinearLayout mFirstChildLayout;
	private LinearLayout mSecondChildLayout;

	private Button mSaveAndUpdateButton;
	private FrameLayout mBeautyPrefLoadingDialog;
	private ProgressDialog pDialog;

	private ArrayList<String> selectedIds = new ArrayList<String>();
	private ArrayList<String> unSelectedIds = new ArrayList<String>();
	private ArrayList<String> allIds = new ArrayList<String>();

	RadioGroup genderRadioGroup;
	String selectedGenderId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beautypref);
		setTitle(getResources().getString(R.string.my_account_beeauty_pref));
		mCategoriesContainerLayout = (LinearLayout) findViewById(R.id.categOriesContainerLayout);
		mSaveAndUpdateButton = (Button) findViewById(R.id.saveAndUpdateBeautyPref);
		mBeautyPrefLoadingDialog = (FrameLayout) findViewById(R.id.beautyPrefLoadingDialog);

		invokeBeautyPreference();

		mSaveAndUpdateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				saveAndUpdateBeautyPref();
			}
		});
	}

	private void invokeBeautyPreference() {
		InvokerParams<NewBeautyPreferencesBean> invokerParams = new InvokerParams<NewBeautyPreferencesBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.BEAUTY_PREF_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.GET);
	    invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		//invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(populateRetrieveBeautyPreferenceDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(NewBeautyPreferencesBean.class);
		RetrieveBeautyPreferenceDetailsHandler retrieveMyProfileDetailsHandler = new RetrieveBeautyPreferenceDetailsHandler();
		invokerParams.setUltaHandler(retrieveMyProfileDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<BeautyPrefActivity><invokeBeautyPreference()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateRetrieveBeautyPreferenceDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");

		return urlParams;
	}

	/**
	 * The Class RetrieveBeautyPreferenceDetailsHandler.
	 */
	public class RetrieveBeautyPreferenceDetailsHandler extends UltaHandler {

		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveBeautyPreferenceDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							BeautyPreferenceActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {

				NewBeautyPreferencesBean newBeautyPreferencesBean = (NewBeautyPreferencesBean) getResponseBean();

				BeautyPreferenceCategoryList beautyPreferenceCategoryList = newBeautyPreferencesBean
						.getNewBeautyPreferences();

				mNewPrefValuesBean = beautyPreferenceCategoryList
						.getNewPrefValues();

				for (int i = 0; i < mNewPrefValuesBean.size(); i++) {

					currentCategoryName = mNewPrefValuesBean.get(i)
							.getCategory();

					if (null != currentCategoryName
							&& !currentCategoryName
									.equalsIgnoreCase(previousCategoryName)) {
						System.out.println("Heading called");
						populateBeautyPrefHeaderAndFirstChild(currentCategoryName);
						beautyPrefHeaderCategories.add(currentCategoryName);
					}

					previousCategoryName = mNewPrefValuesBean.get(i)
							.getCategory();

				}

			}
		}

	}

	public void populateBeautyPrefHeaderAndFirstChild(String currentCategoryName) {

		TextView categoriesHeaderTextView = new TextView(this);
		categoriesHeaderTextView.setTypeface(setHelveticaRegulartTypeFace());
		categoriesHeaderTextView.setTextSize(25);
		categoriesHeaderTextView.setPadding(20, 20, 0, 25);
		categoriesHeaderTextView.setText(currentCategoryName);
		categoriesHeaderTextView.setTextColor(getResources().getColor(R.color.greyBackground));
		mFirstChildLayout = new LinearLayout(this);
		mFirstChildLayout.setOrientation(LinearLayout.VERTICAL);

		// LinearLayout.LayoutParams layoutParams = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);

		mFirstChildLayout.setBackgroundResource(R.drawable.rectangular_boarder);
		mCategoriesContainerLayout.addView(categoriesHeaderTextView);

		for (int i = 0; i < mNewPrefValuesBean.size(); i++) {

			currentFirstChildName = mNewPrefValuesBean.get(i).getPrefMaster();
			if (null != currentFirstChildName
					&& !currentFirstChildName
							.equalsIgnoreCase(previousFirstChildName)
					&& mNewPrefValuesBean.get(i).getCategory()
							.equalsIgnoreCase(currentCategoryName)) {
				System.out.println("First child called");
				populatepreferenceHeaderAndSecondChild(currentCategoryName,
						currentFirstChildName);
			}
			mBeautyPrefLoadingDialog.setVisibility(View.GONE);
			previousFirstChildName = mNewPrefValuesBean.get(i).getPrefMaster();

		}

		mCategoriesContainerLayout.addView(mFirstChildLayout);

	}

	public void populatepreferenceHeaderAndSecondChild(
			String currentCategoryName, String currentFirstChildName) {

		mSecondChildLayout = new LinearLayout(this);
		TextView categoriesHeaderTextView = new TextView(this);
		categoriesHeaderTextView.setTextSize(20);
		mSecondChildLayout.setPadding(40, 25, 0, 25);
		mSecondChildLayout.setOrientation(LinearLayout.VERTICAL);
		categoriesHeaderTextView.setText(currentFirstChildName);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// mSecondChildLayout.setBackground(getResources().getDrawable(
		// R.drawable.rectangular_boarder));

		// mCategoriesContainerLayout.addView(categoriesHeaderTextView);
		mSecondChildLayout.addView(categoriesHeaderTextView, layoutParams);

		for (int i = 0; i < mNewPrefValuesBean.size(); i++) {

			if (null != currentFirstChildName
					&& mNewPrefValuesBean.get(i).getPrefMaster()
							.equalsIgnoreCase(currentFirstChildName)
					&& mNewPrefValuesBean.get(i).getCategory()
							.equalsIgnoreCase(currentCategoryName)) {
				System.out.println("Inflater called");
				LayoutInflater inflater = LayoutInflater
						.from(BeautyPreferenceActivity.this);
				View inflatedLayout = inflater.inflate(
						R.layout.preference_mastervalue, null, false);
				TextView prefernceValueTextView = (TextView) inflatedLayout
						.findViewById(R.id.prefernceValueTextView);
				prefernceValueTextView.setText(mNewPrefValuesBean.get(i)
						.getPrefValues());
				CheckBox valuesCheckBox = (CheckBox) inflatedLayout
						.findViewById(R.id.preferenceValueCheckBox);
				allIds.add(mNewPrefValuesBean.get(i).getRelationshipId());
				Drawable drawable = getResources().getDrawable(R.drawable.beauty_pref_check_box);
				drawable.setBounds(0, 0, 60, 60);
				valuesCheckBox.setButtonDrawable(android.R.color.transparent);
				valuesCheckBox.setCompoundDrawables(drawable, null, null, null);
				valuesCheckBox.setCompoundDrawablePadding(15);
				valuesCheckBox.setPadding(35, 25, 3, 10);
				if (currentFirstChildName.equalsIgnoreCase("Gender")) {
					//check if the radio group is already created
					if(null==genderRadioGroup)
					{	
						genderRadioGroup = (RadioGroup) inflatedLayout
							.findViewById(R.id.genderRadioGroup);
					}
						RadioButton genderRadioButton = new RadioButton(this);
						int checkedId = i;
						genderRadioButton.setId(checkedId);
						drawable = getResources().getDrawable(R.drawable.beauty_pref_radio_button);
						drawable.setBounds(0, 0, 60, 60);
						genderRadioButton.setButtonDrawable(android.R.color.transparent);
						genderRadioButton.setCompoundDrawables(drawable, null, null, null);
						genderRadioButton.setCompoundDrawablePadding(15);
						genderRadioButton.setPadding(35, 25, 3, 10);
						
						checkedId++;
						// checkboxId++;
						genderRadioButton.setText(mNewPrefValuesBean.get(i)
								.getPrefValues());
						if (mNewPrefValuesBean.get(i).getIsSelected()
								.equalsIgnoreCase("true")) {
							genderRadioButton.setChecked(true);
							
							String relationshipId = mNewPrefValuesBean
									.get(i)
									.getRelationshipId();
							selectedGenderId=relationshipId;
							if (selectedIds.size() == 0) {
								selectedIds.add(relationshipId);

							} else {
								for (int k = 0; k < selectedIds
										.size(); k++) {
									if (!selectedIds.get(k)
											.equalsIgnoreCase(
													relationshipId)) {
										selectedIds
												.add(relationshipId);
										break;
										// i--;
									}
								}
							}
						}

						genderRadioGroup.addView(genderRadioButton);
						genderRadioGroup.setVisibility(View.VISIBLE);

					genderRadioGroup.setOnCheckedChangeListener(this);

					valuesCheckBox.setVisibility(View.GONE);
					prefernceValueTextView.setVisibility(View.GONE);
					mSecondChildLayout.addView(inflatedLayout, layoutParams);

				} else {

					valuesCheckBox.setId(i);
					// checkboxId++;
					if (mNewPrefValuesBean.get(i).getIsSelected()
							.equalsIgnoreCase("true")) {
						valuesCheckBox.setChecked(true);
						//to add already selected values
						String relationshipId = mNewPrefValuesBean
								.get(i)
								.getRelationshipId();
						if (selectedIds.size() == 0) {
							selectedIds.add(relationshipId);

						} else {
							for (int k = 0; k < selectedIds
									.size(); k++) {
								if (!selectedIds.get(k)
										.equalsIgnoreCase(
												relationshipId)) {
									selectedIds
											.add(relationshipId);
									break;
									// i--;
								}
							}
						}
					
					}

					valuesCheckBox
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {

									System.out.println("Checked id :"
											+ buttonView.getId());
									String relationshipId = mNewPrefValuesBean
											.get(buttonView.getId())
											.getRelationshipId();

									if (isChecked) {

										if (selectedIds.size() == 0) {
											selectedIds.add(relationshipId);

										} else {
											for (int i = 0; i < selectedIds
													.size(); i++) {
												if (!selectedIds.get(i)
														.equalsIgnoreCase(
																relationshipId)) {
													selectedIds
															.add(relationshipId);
													break;
													// i--;
												}
											}

										}
									} else {

										if (selectedIds
												.contains(relationshipId)) {
											selectedIds.remove(relationshipId);

										}
									}

								}
							});

					mSecondChildLayout.addView(inflatedLayout, layoutParams);
				}
			}
		}
		mFirstChildLayout.addView(mSecondChildLayout, layoutParams);
		// mCategoriesContainerLayout.addView(mSecondChildLayout);

	}

	private void saveAndUpdateBeautyPref() {
		invokeSaveAndUpdateBeautyPref();
	}

	private void invokeSaveAndUpdateBeautyPref() {
		pDialog = new ProgressDialog(this);
		setProgressDialogLoadingColor(pDialog);
		pDialog.setMessage("Saving..");
		pDialog.show();

		InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
		invokerParams
				.setServiceToInvoke(WebserviceConstants.SAVE_AND_UPDATE_BEAUTY_PREF_SERVICE);
		invokerParams.setHttpMethod(HttpMethod.POST);
	    invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
//		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(populateSaveAndUpdateHandlerParameters());
		invokerParams.setUltaBeanClazz(UltaBean.class);
		SaveAndUpdateBeautyPrefhandler saveAndUpdateBeautyPrefhandler = new SaveAndUpdateBeautyPrefhandler();
		invokerParams.setUltaHandler(saveAndUpdateBeautyPrefhandler);
		try {
			new ExecutionDelegator(invokerParams);
		} catch (UltaException ultaException) {
			Logger.Log("<BeautyPrefActivity><invokeSaveAndUpdateBeautyPref()><UltaException>>"
					+ ultaException);
			pDialog.dismiss();
		}
	}

	private Map<String, String> populateSaveAndUpdateHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");

		StringBuffer selectedIdsTobesaved = new StringBuffer();
		for (int i = 0; i < selectedIds.size(); i++) {
			selectedIdsTobesaved.append(getFirstValueFromRelationShipId(selectedIds.get(i)));
			selectedIdsTobesaved.append(",");
		}

		String selectedId = selectedIdsTobesaved.toString().substring(0,
				selectedIdsTobesaved.toString().length()-1);

		urlParams.put("selectedIds", selectedId);

		StringBuffer unSelectedIdsToBeSaved = new StringBuffer();
		if (selectedIds.size() == 0) {
			for (int i = 0; i < allIds.size(); i++) {
				unSelectedIdsToBeSaved.append(getFirstValueFromRelationShipId(allIds.get(i)));
				unSelectedIdsToBeSaved.append(",");
			}
		} else {
				for (int i = 0; i < allIds.size(); i++) {
					boolean isSelected=false;
					for (int j = 0; j < selectedIds.size(); j++) {						
						if (selectedIds.get(j).equalsIgnoreCase(allIds.get(i))) {
							isSelected=true;
						}
					}
					if(!isSelected)
					{
						unSelectedIds.add(allIds.get(i));
						unSelectedIdsToBeSaved.append(getFirstValueFromRelationShipId(allIds.get(i)));
						unSelectedIdsToBeSaved.append(",");
					}
			}
			
		}
		String unSelectedId = unSelectedIdsToBeSaved.toString().substring(0,
				unSelectedIdsToBeSaved.toString().length()-1);
		urlParams.put("unSelectedIds", unSelectedId);

		return urlParams;
	}

	/**
	 * The Class RetrieveBeautyPreferenceDetailsHandler.
	 */
	public class SaveAndUpdateBeautyPrefhandler extends UltaHandler {

		public void handleMessage(Message msg) {
			pDialog.dismiss();
			Logger.Log("<SaveAndUpdateBeautyPrefhandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							BeautyPreferenceActivity.this);
				} catch (WindowManager.BadTokenException e) {
				} catch (Exception e) {
				}
			} else {
				Toast.makeText(BeautyPreferenceActivity.this,
						"Data Saved SuccessFully", Toast.LENGTH_LONG).show();
				finish();
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		String relationshipId = mNewPrefValuesBean.get(checkedId)
				.getRelationshipId();
		//remove already selected radio button relationship id
		if(selectedIds.contains(selectedGenderId))
		{
			selectedIds.remove(selectedGenderId);
		}

		//check if the selected value is already there in selected array
		if(!selectedIds.contains(relationshipId))
		{
			if (selectedIds.size() == 0) {
				selectedIds.add(relationshipId);
				
			} else {
				for (int i = 0; i < selectedIds.size(); i++) {
					if (!selectedIds.get(i).equalsIgnoreCase(relationshipId)) {
						selectedIds.add(relationshipId);
						break;
						// i--;
					}
				}
			}
		}
			

	}
	
	private String getFirstValueFromRelationShipId(String relationshipId)
	{
		String [] relationshipArray=relationshipId.split(":");
		String relationshipIdValue="";
		if(null!=relationshipArray&&relationshipArray.length>0)
		{
			relationshipIdValue= relationshipArray[0];
		}
		return relationshipIdValue;
	}
}
