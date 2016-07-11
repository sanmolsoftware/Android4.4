/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */

package com.ulta.core.activity.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.account.BeautyPreferencesBean;
import com.ulta.core.bean.account.beautyPrefMapBean;
import com.ulta.core.bean.account.profilePrefMapBean;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.util.HashMap;
import java.util.Map;

public class BeautyPreferrencesActivity extends UltaBaseActivity implements
		OnDoneClickedListener
{

	private beautyPrefMapBean beauty_pref_map_bean;
	private profilePrefMapBean profile_pref_map_bean;
	public String strGender = "";
	public String strSkinType = "";
	public String strHairColor = "";
	public String strCategories = "";
	public String strSkinCareCOncerns = "";
	public String strHairType = "";
	public String strSkinTone = "";
	public String strHairConcerns = "";
	public String strCategories1 = "";
	public String strSkinCareCOncerns1 = "";
	public String strHairType1 = "";
	public String strSkinTone1 = "";
	public String strHairConcerns1 = "";
	public TextView gender, skinType, hairColor, categories, skinCareConcern,
			hairType, skinTone, hairConcern;
	public LinearLayout mainLayout, loadingLayout, lytGender, lytSkinType,
			lytHairColor, lytCategories, lytSkinCareConcern, lytHairType,
			lytSkinTone, lytHairConcern;
	public ScrollView scrollView;
	private Button mDoneButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauty_pref);
		setTitle("Beauty Preference");
		setActivity(BeautyPreferrencesActivity.this);
		initViews();
		loadingLayout.setVisibility(View.VISIBLE);
		scrollView.setVisibility(View.GONE);
		invokeBeautyPreferences();
	}

	private void initViews() {
		scrollView = (ScrollView) findViewById(R.id.slytMain_Beauty_Pref);
		mainLayout = (LinearLayout) findViewById(R.id.lytMain_Beauty_Pref);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingDialog);
		gender = (TextView) findViewById(R.id.txtGender);
		skinType = (TextView) findViewById(R.id.txtSkinType);
		hairColor = (TextView) findViewById(R.id.txtHairColor);
		categories = (TextView) findViewById(R.id.txtCategoriesOfInterest);
		skinCareConcern = (TextView) findViewById(R.id.txtSkinCareConcern);
		hairType = (TextView) findViewById(R.id.txtHairType);
		skinTone = (TextView) findViewById(R.id.txtSkinTone);
		hairConcern = (TextView) findViewById(R.id.txtHairConcern);
		lytGender = (LinearLayout) findViewById(R.id.lytGender);
		lytSkinType = (LinearLayout) findViewById(R.id.lytSkinType);
		lytHairColor = (LinearLayout) findViewById(R.id.lytHairColor);
		lytCategories = (LinearLayout) findViewById(R.id.lytCategoriesOfInterest);
		lytSkinCareConcern = (LinearLayout) findViewById(R.id.lytSkinCareConcern);
		lytHairType = (LinearLayout) findViewById(R.id.lytHairType);
		lytSkinTone = (LinearLayout) findViewById(R.id.lytSkinTone);
		lytHairConcern = (LinearLayout) findViewById(R.id.lytHairConcerns);
		mDoneButton = (Button) findViewById(R.id.doneBtn);
		
		
		mDoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadingLayout.setVisibility(View.VISIBLE);
				scrollView.setVisibility(View.GONE);
				invokeUpdateBeautyPreference();
			}
		});

	}

	private void invokeBeautyPreferences() {
		InvokerParams<BeautyPreferencesBean> invokerParams = new InvokerParams<BeautyPreferencesBean>();
		invokerParams
				.setServiceToInvoke("atg/userprofiling/B2CProfileFormHandler/beautyPreferences?");
		invokerParams.setHttpMethod(HttpMethod.GET);
		// invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(populateRetrieveBeautyPreferenceDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(BeautyPreferencesBean.class);
		RetrieveBeautyPreferenceDetailsHandler retrieveMyProfileDetailsHandler = new RetrieveBeautyPreferenceDetailsHandler();
		invokerParams.setUltaHandler(retrieveMyProfileDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		}
		catch (UltaException ultaException) {
			Logger.Log("<MyAccountActivity><invokeMyProfileDetails()><UltaException>>"
					+ ultaException);

		}
	}

	/**
	 * Populate retrieve my profile details handler parameters.
	 * 
	 * @return the map
	 */
	private Map<String, String> populateRetrieveBeautyPreferenceDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "1");
		return urlParams;
	}

	/**
	 * The Class RetrieveBeautyPreferenceDetailsHandler.
	 */
	public class RetrieveBeautyPreferenceDetailsHandler extends UltaHandler
	{

		public void handleMessage(Message msg) {
			Logger.Log("<RetrieveBeautyPreferenceDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							BeautyPreferrencesActivity.this);
				}
				catch (WindowManager.BadTokenException e) {
				}
				catch (Exception e) {
				}
			}
			else {
				BeautyPreferencesBean preferencebean = (BeautyPreferencesBean) getResponseBean();
				if (null != preferencebean) {
					try {
						profile_pref_map_bean = preferencebean
								.getBeautyPreferences().getProfilePrefMap();
						beauty_pref_map_bean = preferencebean
								.getBeautyPreferences().getBeautyPrefMap();
						setData();

					}
					catch (WindowManager.BadTokenException e) {
					}
					catch (Exception e) {
					}
				}
			}
		}

	}

	private void setData() {
		if (null != profile_pref_map_bean.getGender()) {

			strGender = profile_pref_map_bean.getGender();
			gender.setText(profile_pref_map_bean.getGender().substring(0, 1)
					.toUpperCase()
					+ profile_pref_map_bean.getGender().substring(1));
		}
		else {
			gender.setText("");
		}
		lytGender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final RadioGroup radioButtonGroup;
				final RadioButton radioButton[];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Gender");
				radioButtonGroup = new RadioGroup(
						BeautyPreferrencesActivity.this);
				radioButton = new RadioButton[beauty_pref_map_bean.getGender()
						.size()];
				radioButtonGroup.setOrientation(RadioGroup.HORIZONTAL);
				for (int i = 0; i < beauty_pref_map_bean.getGender().size(); i++) {
					radioButton[i] = new RadioButton(
							BeautyPreferrencesActivity.this);
					radioButton[i].setId(i + 1);
					String genderLocal = beauty_pref_map_bean.getGender().get(i);
					genderLocal = genderLocal.substring(0, 1).toUpperCase()
							+ genderLocal.substring(1);
					radioButton[i].setText(genderLocal);
					radioButtonGroup.addView(radioButton[i]);
				}
				scrView.addView(radioButtonGroup);
				alert.setView(scrView);
				if (null != profile_pref_map_bean.getGender()) {
					for (int i = 0; i < beauty_pref_map_bean.getGender().size(); i++) {
						if ((beauty_pref_map_bean.getGender().get(i))
								.equalsIgnoreCase(profile_pref_map_bean.getGender())) {
							radioButton[i].setChecked(true);
							break;
						}
					}
				}
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								strGender = "";
								if (radioButtonGroup.getCheckedRadioButtonId() <= beauty_pref_map_bean
										.getGender().size()
										&& radioButtonGroup
												.getCheckedRadioButtonId() > 0) {
									int selectedId = radioButtonGroup
											.getCheckedRadioButtonId();
									strGender = radioButton[selectedId - 1]
											.getText().toString();
									gender.setText(strGender);
									strGender = strGender.toLowerCase();
								}
								else {
									gender.setText("");
								}

							}
						});
				alert.create().show();
			}
		});

		if (null != profile_pref_map_bean.getSkinType()) {
			skinType.setText(profile_pref_map_bean.getSkinType());
			strSkinType = profile_pref_map_bean.getSkinType();
		}
		else {
			skinType.setText("");
		}
		lytSkinType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final RadioGroup radioButtonGroup;
				final RadioButton radioButton[];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Skin Type");
				radioButtonGroup = new RadioGroup(
						BeautyPreferrencesActivity.this);
				radioButton = new RadioButton[beauty_pref_map_bean
						.getSkin_type().size()];
				radioButtonGroup.setOrientation(RadioGroup.VERTICAL);
				for (int i = 0; i < beauty_pref_map_bean.getSkin_type().size(); i++) {
					radioButton[i] = new RadioButton(
							BeautyPreferrencesActivity.this);
					radioButton[i].setId(i + 1);
					radioButton[i].setText(beauty_pref_map_bean.getSkin_type()
							.get(i));
					radioButtonGroup.addView(radioButton[i]);
				}
				scrView.addView(radioButtonGroup);
				alert.setView(scrView);
				if (null != profile_pref_map_bean.getSkinType()) {
					for (int i = 0; i < beauty_pref_map_bean.getSkin_type()
							.size(); i++) {
						if ((radioButton[i].getText().toString())
								.equalsIgnoreCase(profile_pref_map_bean
										.getSkinType())) {
							radioButton[i].setChecked(true);
							break;
						}
					}
				}
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								strSkinType = "";
								if (radioButtonGroup.getCheckedRadioButtonId() <= beauty_pref_map_bean
										.getSkin_type().size()
										&& radioButtonGroup
												.getCheckedRadioButtonId() > 0) {
									int selectedId = radioButtonGroup
											.getCheckedRadioButtonId();
									strSkinType = radioButton[selectedId - 1]
											.getText().toString();
									skinType.setText(strSkinType);
								}
								else {
									skinType.setText("");
								}

							}
						});
				alert.create().show();
			}
		});

		if (null != profile_pref_map_bean.getHairColor()) {
			hairColor.setText(profile_pref_map_bean.getHairColor().substring(0, 1)
					.toUpperCase()+ profile_pref_map_bean.getHairColor().substring(1));
			strHairColor = profile_pref_map_bean.getHairColor();
		}
		else {
			hairColor.setText("");
		}

		lytHairColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final RadioGroup radioButtonGroup;
				final RadioButton radioButton[];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Hair Color");
				radioButtonGroup = new RadioGroup(
						BeautyPreferrencesActivity.this);
				radioButton = new RadioButton[beauty_pref_map_bean
						.getHair_color().size()];
				radioButtonGroup.setOrientation(RadioGroup.VERTICAL);
				for (int i = 0; i < beauty_pref_map_bean.getHair_color().size(); i++) {
					radioButton[i] = new RadioButton(
							BeautyPreferrencesActivity.this);
					radioButton[i].setId(i + 1);
					String hairClrLocal=beauty_pref_map_bean.getHair_color().get(i);
					hairClrLocal = hairClrLocal.substring(0, 1).toUpperCase()
							+ hairClrLocal.substring(1);
					radioButton[i].setText(hairClrLocal);
					radioButtonGroup.addView(radioButton[i]);
				}
				scrView.addView(radioButtonGroup);
				alert.setView(scrView);
				if (null != profile_pref_map_bean.getHairColor()) {
					for (int i = 0; i < beauty_pref_map_bean.getHair_color()
							.size(); i++) {
						if ((radioButton[i].getText().toString())
								.equalsIgnoreCase(profile_pref_map_bean
										.getHairColor())) {
							radioButton[i].setChecked(true);
							break;
						}
					}
				}
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								strHairColor = "";
								if (radioButtonGroup.getCheckedRadioButtonId() <= beauty_pref_map_bean
										.getHair_color().size()
										&& radioButtonGroup
												.getCheckedRadioButtonId() > 0) {
									int selectedId = radioButtonGroup
											.getCheckedRadioButtonId();
									strHairColor = radioButton[selectedId - 1]
											.getText().toString();
									hairColor.setText(strHairColor);
								}
								else {

									hairColor.setText("");
								}

							}
						});
				alert.create().show();
			}
		});

		if (null != profile_pref_map_bean.getCategories()) {
			for (int i = 0; i < profile_pref_map_bean.getCategories().size(); i++) {
				if (i == 0) {
					strCategories = profile_pref_map_bean.getCategories()
							.get(i);
					strCategories1 = profile_pref_map_bean.getCategories().get(
							i);
				}
				else {
					strCategories = strCategories + "\n"
							+ profile_pref_map_bean.getCategories().get(i);
					strCategories1 = strCategories1 + ","
							+ profile_pref_map_bean.getCategories().get(i);
				}
			}
			categories.setText(strCategories);
		}
		else {
			categories.setText("");
		}
		lytCategories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final CheckBox ck[] = new CheckBox[beauty_pref_map_bean
						.getCategories_of_interest().size()];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Categories of Interest");

				final LinearLayout lytCheckBox = new LinearLayout(
						BeautyPreferrencesActivity.this);
				lytCheckBox.setOrientation(LinearLayout.VERTICAL);

				for (int i = 0; i < beauty_pref_map_bean
						.getCategories_of_interest().size(); i++) {
					ck[i] = new CheckBox(BeautyPreferrencesActivity.this);
					ck[i].setText(beauty_pref_map_bean
							.getCategories_of_interest().get(i).toString());
					lytCheckBox.addView(ck[i]);
					if (null != strCategories
							&& !strCategories.equalsIgnoreCase("")) {
						String[] s = new String[strCategories.length()];
						s = strCategories.split("\n");
						for (int j = 0; j < s.length; j++) {
							if (s[j].equals(ck[i].getText())) {
								ck[i].setChecked(true);
							}
						}
					}
				}
				scrView.addView(lytCheckBox);
				alert.setView(scrView);
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int count = 0;
								strCategories = "";
								strCategories1="";
								for (int i = 0; i < beauty_pref_map_bean
										.getCategories_of_interest().size(); i++) {
									if (ck[i].isChecked()) {
										if (count == 0) {
											strCategories = beauty_pref_map_bean
													.getCategories_of_interest()
													.get(i).toString();
											strCategories1 = beauty_pref_map_bean
													.getCategories_of_interest()
													.get(i).toString();
											count++;
										}
										else {
											strCategories = strCategories
													+ "\n"
													+ beauty_pref_map_bean
															.getCategories_of_interest()
															.get(i).toString();
											strCategories1 = strCategories1
													+ ","
													+ beauty_pref_map_bean
															.getCategories_of_interest()
															.get(i).toString();
										}
									}

								}
								categories.setText(strCategories);
							}

						});
				alert.create().show();
			}
		});
		if (null != profile_pref_map_bean.getSkincareConcerns()) {
			for (int i = 0; i < profile_pref_map_bean.getSkincareConcerns()
					.size(); i++) {
				if (i == 0) {
					strSkinCareCOncerns = profile_pref_map_bean
							.getSkincareConcerns().get(i);
					strSkinCareCOncerns1 = profile_pref_map_bean
							.getSkincareConcerns().get(i);
				}
				else {
					strSkinCareCOncerns = strSkinCareCOncerns
							+ "\n"
							+ profile_pref_map_bean.getSkincareConcerns()
									.get(i);
					strSkinCareCOncerns1 = strSkinCareCOncerns1
							+ ","
							+ profile_pref_map_bean.getSkincareConcerns()
									.get(i);
				}
			}
			skinCareConcern.setText(strSkinCareCOncerns);
		}
		else {
			skinCareConcern.setText("");
		}
		lytSkinCareConcern.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final CheckBox ck[] = new CheckBox[beauty_pref_map_bean
						.getSkincare_concern().size()];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Skin Care Concerns");

				final LinearLayout lytCheckBox = new LinearLayout(
						BeautyPreferrencesActivity.this);
				lytCheckBox.setOrientation(LinearLayout.VERTICAL);

				for (int i = 0; i < beauty_pref_map_bean.getSkincare_concern()
						.size(); i++) {
					ck[i] = new CheckBox(BeautyPreferrencesActivity.this);
					ck[i].setText(beauty_pref_map_bean.getSkincare_concern()
							.get(i).toString());
					lytCheckBox.addView(ck[i]);
					if (null != strSkinCareCOncerns
							&& !strSkinCareCOncerns.equalsIgnoreCase("NA")) {
						String[] s = new String[strSkinCareCOncerns.length()];
						s = strSkinCareCOncerns.split("\n");
						for (int j = 0; j < s.length; j++) {
							if (s[j].equals(ck[i].getText())) {
								ck[i].setChecked(true);
							}
						}
					}
				}
				scrView.addView(lytCheckBox);
				alert.setView(scrView);
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int count = 0;
								strSkinCareCOncerns = "";
								strSkinCareCOncerns="";
								for (int i = 0; i < beauty_pref_map_bean
										.getSkincare_concern().size(); i++) {
									if (ck[i].isChecked()) {
										if (count == 0) {
											strSkinCareCOncerns = beauty_pref_map_bean
													.getSkincare_concern()
													.get(i).toString();
											strSkinCareCOncerns1 = beauty_pref_map_bean
													.getSkincare_concern()
													.get(i).toString();
											count++;
										}
										else {
											strSkinCareCOncerns = strSkinCareCOncerns
													+ "\n"
													+ beauty_pref_map_bean
															.getSkincare_concern()
															.get(i).toString();
											strSkinCareCOncerns1 = strSkinCareCOncerns1
													+ ","
													+ beauty_pref_map_bean
															.getSkincare_concern()
															.get(i).toString();
										}
									}

								}
								skinCareConcern.setText(strSkinCareCOncerns);
							}

						});
				alert.create().show();
			}
		});
		if (null != profile_pref_map_bean.getHairType()) {
			for (int i = 0; i < profile_pref_map_bean.getHairType().size(); i++) {
				if (i == 0) {
					strHairType = profile_pref_map_bean.getHairType().get(i);
					strHairType1 = profile_pref_map_bean.getHairType().get(i);
				}
				else {
					strHairType = strHairType + "\n"
							+ profile_pref_map_bean.getHairType().get(i);
					strHairType1 = strHairType1 + ","
							+ profile_pref_map_bean.getHairType().get(i);
				}
			}
			hairType.setText(strHairType);
		}
		else {
			hairType.setText("");
		}
		lytHairType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final CheckBox ck[] = new CheckBox[beauty_pref_map_bean
						.getHair_type().size()];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Hair Type");

				final LinearLayout lytCheckBox = new LinearLayout(
						BeautyPreferrencesActivity.this);
				lytCheckBox.setOrientation(LinearLayout.VERTICAL);

				for (int i = 0; i < beauty_pref_map_bean.getHair_type().size(); i++) {
					ck[i] = new CheckBox(BeautyPreferrencesActivity.this);
					ck[i].setText(beauty_pref_map_bean.getHair_type().get(i)
							.toString());
					lytCheckBox.addView(ck[i]);
					if (null != strHairType
							&& !strHairType.equalsIgnoreCase("NA")) {
						String[] s = new String[strHairType.length()];
						s = strHairType.split("\n");
						for (int j = 0; j < s.length; j++) {
							if (s[j].equals(ck[i].getText())) {
								ck[i].setChecked(true);
							}
						}
					}
				}
				scrView.addView(lytCheckBox);
				alert.setView(scrView);
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int count = 0;
								strHairType = "";
								strHairType1="";
								for (int i = 0; i < beauty_pref_map_bean
										.getHair_type().size(); i++) {
									if (ck[i].isChecked()) {
										if (count == 0) {
											strHairType = beauty_pref_map_bean
													.getHair_type().get(i)
													.toString();
											strHairType1 = beauty_pref_map_bean
													.getHair_type().get(i)
													.toString();
											count++;
										}
										else {
											strHairType = strHairType
													+ "\n"
													+ beauty_pref_map_bean
															.getHair_type()
															.get(i).toString();
											strHairType1 = strHairType1
													+ ","
													+ beauty_pref_map_bean
															.getHair_type()
															.get(i).toString();
										}
									}

								}
								hairType.setText(strHairType);
							}

						});
				alert.create().show();
			}
		});
		if (null != profile_pref_map_bean.getSkinTone()) {
			for (int i = 0; i < profile_pref_map_bean.getSkinTone().size(); i++) {
				if (i == 0) {
					strSkinTone = profile_pref_map_bean.getSkinTone().get(i);
					strSkinTone1 = profile_pref_map_bean.getSkinTone().get(i);
				}
				else {
					strSkinTone = strSkinTone + "\n"
							+ profile_pref_map_bean.getSkinTone().get(i);
					strSkinTone1 = strSkinTone1 + ","
							+ profile_pref_map_bean.getSkinTone().get(i);
				}
			}
			skinTone.setText(strSkinTone);
		}
		else {
			skinTone.setText("");
		}
		lytSkinTone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final CheckBox ck[] = new CheckBox[beauty_pref_map_bean
						.getSkin_tone().size()];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Skin Tone");

				final LinearLayout lytCheckBox = new LinearLayout(
						BeautyPreferrencesActivity.this);
				lytCheckBox.setOrientation(LinearLayout.VERTICAL);

				for (int i = 0; i < beauty_pref_map_bean.getSkin_tone().size(); i++) {
					ck[i] = new CheckBox(BeautyPreferrencesActivity.this);
					ck[i].setText(beauty_pref_map_bean.getSkin_tone().get(i)
							.toString());
					lytCheckBox.addView(ck[i]);
					if (null != strSkinTone
							&& !strSkinTone.equalsIgnoreCase("NA")) {
						String[] s = new String[strSkinTone.length()];
						s = strSkinTone.split("\n");
						for (int j = 0; j < s.length; j++) {
							if (s[j].equals(ck[i].getText())) {
								ck[i].setChecked(true);
							}
						}
					}
				}
				scrView.addView(lytCheckBox);
				alert.setView(scrView);
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int count = 0;
								strSkinTone = "";
								strSkinTone1="";
								for (int i = 0; i < beauty_pref_map_bean
										.getSkin_tone().size(); i++) {
									if (ck[i].isChecked()) {
										if (count == 0) {
											strSkinTone = beauty_pref_map_bean
													.getSkin_tone().get(i)
													.toString();
											strSkinTone1 = beauty_pref_map_bean
													.getSkin_tone().get(i)
													.toString();
											count++;
										}
										else {
											strSkinTone = strSkinTone
													+ "\n"
													+ beauty_pref_map_bean
															.getSkin_tone()
															.get(i).toString();
											strSkinTone1 = strSkinTone1
													+ ","
													+ beauty_pref_map_bean
															.getSkin_tone()
															.get(i).toString();
										}
									}

								}
								skinTone.setText(strSkinTone);
							}

						});
				alert.create().show();
			}
		});
		if (null != profile_pref_map_bean.getHairConcerns()) {
			for (int i = 0; i < profile_pref_map_bean.getHairConcerns().size(); i++) {
				if (i == 0) {
					strHairConcerns = profile_pref_map_bean.getHairConcerns()
							.get(i);
					strHairConcerns1 = profile_pref_map_bean.getHairConcerns()
							.get(i);
				}
				else {
					strHairConcerns = strHairConcerns + "\n"
							+ profile_pref_map_bean.getHairConcerns().get(i);
					strHairConcerns1 = strHairConcerns1 + ","
							+ profile_pref_map_bean.getHairConcerns().get(i);
				}
			}
			hairConcern.setText(strHairConcerns);
		}
		else {
			hairConcern.setText("");
		}
		lytHairConcern.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
						final CheckBox ck[] = new CheckBox[beauty_pref_map_bean
						.getHair_concern().size()];
				final ScrollView scrView = new ScrollView(
						BeautyPreferrencesActivity.this);
				AlertDialog.Builder alert = new AlertDialog.Builder(
						BeautyPreferrencesActivity.this);
				alert.setTitle("Select Your Hair Concern");

				final LinearLayout lytCheckBox = new LinearLayout(
						BeautyPreferrencesActivity.this);
				lytCheckBox.setOrientation(LinearLayout.VERTICAL);

				for (int i = 0; i < beauty_pref_map_bean.getHair_concern()
						.size(); i++) {
					ck[i] = new CheckBox(BeautyPreferrencesActivity.this);
					ck[i].setText(beauty_pref_map_bean.getHair_concern().get(i)
							.toString());
					lytCheckBox.addView(ck[i]);
					if (null != strHairConcerns
							&& !strHairConcerns.equalsIgnoreCase("NA")) {
						String[] s = new String[strHairConcerns.length()];
						s = strHairConcerns.split("\n");
						for (int j = 0; j < s.length; j++) {
							if (s[j].equals(ck[i].getText())) {
								ck[i].setChecked(true);
							}
						}
					}
				}
				scrView.addView(lytCheckBox);
				alert.setView(scrView);
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						});
				alert.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int count = 0;
								strHairConcerns = "";
								strHairConcerns1="";
								for (int i = 0; i < beauty_pref_map_bean
										.getHair_concern().size(); i++) {
									if (ck[i].isChecked()) {
										if (count == 0) {
											strHairConcerns = beauty_pref_map_bean
													.getHair_concern().get(i)
													.toString();
											strHairConcerns1 = beauty_pref_map_bean
													.getHair_concern().get(i)
													.toString();
											count++;
										}
										else {
											strHairConcerns = strHairConcerns
													+ "\n"
													+ beauty_pref_map_bean
															.getHair_concern()
															.get(i).toString();
											strHairConcerns1 = strHairConcerns1
													+ ","
													+ beauty_pref_map_bean
															.getHair_concern()
															.get(i).toString();
										}
									}

								}
								hairConcern.setText(strHairConcerns);
							}

						});
				alert.create().show();
			}
		});

		loadingLayout.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);

	}

	@Override
	public void onDoneClicked() {
		loadingLayout.setVisibility(View.VISIBLE);
		scrollView.setVisibility(View.GONE);
		invokeUpdateBeautyPreference();
	}

	private void invokeUpdateBeautyPreference() {
		InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
		invokerParams
				.setServiceToInvoke("atg/userprofiling/B2CProfileFormHandler/updateBeautyPreferences");
		invokerParams.setHttpMethod(HttpMethod.POST);
		// invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
		invokerParams.setHttpProtocol(HttpProtocol.http);
		invokerParams
				.setUrlParameters(populateUpdateBeautyPreferenceDetailsHandlerParameters());
		invokerParams.setUltaBeanClazz(UltaBean.class);
		UpdateBeautyPreferenceDetailsHandler updateMyProfileDetailsHandler = new UpdateBeautyPreferenceDetailsHandler();
		invokerParams.setUltaHandler(updateMyProfileDetailsHandler);
		try {
			new ExecutionDelegator(invokerParams);
		}
		catch (UltaException ultaException) {
			Logger.Log("<MyAccountActivity><invokeMyProfileDetails()><UltaException>>"
					+ ultaException);

		}
	}

	private Map<String, String> populateUpdateBeautyPreferenceDetailsHandlerParameters() {
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("atg-rest-output", "json");
		urlParams.put("atg-rest-depth", "0");
		urlParams.put("atg-rest-return-form-handler-exceptions", "true");
		urlParams.put("gender", strGender);
		urlParams.put("skinType", strSkinType);
		urlParams.put("hairColor", strHairColor);
		urlParams.put("categoriesOfInterest", strCategories1);
		urlParams.put("hairType", strHairType1);
		urlParams.put("skinTone", strSkinTone1);
		urlParams.put("hairConcerns", strHairConcerns1);
		urlParams.put("skinConcerns", strSkinCareCOncerns1);

		return urlParams;
	}

	public class UpdateBeautyPreferenceDetailsHandler extends UltaHandler
	{

		public void handleMessage(Message msg) {
			Logger.Log("<UpdateBeautyPreferenceDetailsHandler><handleMessage><getErrorMessage>>"
					+ (getErrorMessage()));
			if (null != getErrorMessage()) {
				try {
					notifyUser(Utility.formatDisplayError(getErrorMessage()),
							BeautyPreferrencesActivity.this);
					finish();
				}
				catch (WindowManager.BadTokenException e) {
				}
				catch (Exception e) {
				}
			}
			else {
				UltaBean preferencebean = (UltaBean) getResponseBean();
				if (null != preferencebean) {
					try {
						Toast.makeText(BeautyPreferrencesActivity.this,
								"Your Preferences have been saved", Toast.LENGTH_SHORT)
								.show();
						loadingLayout.setVisibility(View.GONE);
						scrollView.setVisibility(View.VISIBLE);
						invokeBeautyPreferences();
					}
					catch (WindowManager.BadTokenException e) {
					}
					catch (Exception e) {
					}
				}
			}
		}

	}
}
