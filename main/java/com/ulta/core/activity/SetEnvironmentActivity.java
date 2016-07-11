/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */


package com.ulta.core.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.caching.UltaDataCache;

import org.w3c.dom.Text;

public class SetEnvironmentActivity extends Activity implements OnClickListener, TextWatcher {

    private Dialog mEnvironmentDialog;
    private boolean isSecurityEnabled = true;
    private String mServerContext = "rest/v2/bean";
    LinearLayout productionEnvironments, qaEnvironmens, devEnvironments, mCustomEnvironments;
    Intent mainActivityIntent;
    private EditText mCustomEnvNameET, mCustomEnvRestET, mCustomEnvSecureET;
    private Button mCustomEnvDoneButton;
    private TextView mErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_environment);

        mEnvironmentDialog = new Dialog(SetEnvironmentActivity.this);
        mEnvironmentDialog.setContentView(R.layout.environment_list);
        mEnvironmentDialog.setTitle("Select Environment");

        productionEnvironments = (LinearLayout) mEnvironmentDialog.findViewById(R.id.productionEnvironments);
        qaEnvironmens = (LinearLayout) mEnvironmentDialog.findViewById(R.id.qaEnvironments);
        devEnvironments = (LinearLayout) mEnvironmentDialog.findViewById(R.id.devEnvironments);
        mCustomEnvironments = (LinearLayout) mEnvironmentDialog.findViewById(R.id.customEnvironments);

        TextView productionEnvironmentTV = (TextView) mEnvironmentDialog
                .findViewById(R.id.productionEnvironmentTV);
        TextView apiEnvironmentTV = (TextView) mEnvironmentDialog
                .findViewById(R.id.qaEnvironmentTV);
        TextView otherEnvironmentTV = (TextView) mEnvironmentDialog
                .findViewById(R.id.devEnvironmentTV);
        TextView customEnvironmentTV = (TextView) mEnvironmentDialog
                .findViewById(R.id.customEnvironmentTV);


        TextView prodTextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.prodTextView);
        TextView prodA_Txtview = (TextView) mEnvironmentDialog
                .findViewById(R.id.prod_A);
        TextView prodB_Txtview = (TextView) mEnvironmentDialog
                .findViewById(R.id.prod_B);

        TextView stressTextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.stressTextView);
        TextView stagingTextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.stagingTextView);
        TextView UATTextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.api_uat);


        TextView qa01TextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.qa01TextView);
        TextView qa02TextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.qa02TextView);
        TextView qa03TextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.qa03TextView);

        TextView da3TextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.da3TextView);
        TextView da02TextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.da02TextView);
        TextView da01TextView = (TextView) mEnvironmentDialog
                .findViewById(R.id.da01TextView);

        //Custom Env
        mCustomEnvNameET = (EditText) mEnvironmentDialog.findViewById(R.id.customEnvNameET);
        mCustomEnvNameET.addTextChangedListener(this);
        mCustomEnvRestET = (EditText) mEnvironmentDialog.findViewById(R.id.customEnvRestET);
        mCustomEnvRestET.addTextChangedListener(this);
        mCustomEnvSecureET = (EditText) mEnvironmentDialog.findViewById(R.id.customEnvSecureET);
        mCustomEnvSecureET.addTextChangedListener(this);
        mErrorText = (TextView) mEnvironmentDialog.findViewById(R.id.errorText);

        mCustomEnvDoneButton = (Button) mEnvironmentDialog.findViewById(R.id.customEnvDoneButton);


        mEnvironmentDialog.show();
        mEnvironmentDialog.setCancelable(false);

        productionEnvironmentTV.setOnClickListener(this);
        apiEnvironmentTV.setOnClickListener(this);
        otherEnvironmentTV.setOnClickListener(this);
        customEnvironmentTV.setOnClickListener(this);
        mCustomEnvDoneButton.setOnClickListener(this);

        prodTextView.setOnClickListener(this);
        prodA_Txtview.setOnClickListener(this);
        prodB_Txtview.setOnClickListener(this);

        stressTextView.setOnClickListener(this);
        stagingTextView.setOnClickListener(this);
        UATTextView.setOnClickListener(this);

        da01TextView.setOnClickListener(this);
        da02TextView.setOnClickListener(this);
        da3TextView.setOnClickListener(this);

        qa01TextView.setOnClickListener(this);
        qa02TextView.setOnClickListener(this);
        qa03TextView.setOnClickListener(this);


    }

    private void expandAndCollapseEnvironmentLayout(LinearLayout environmentLayout) {
        if (null != environmentLayout) {
            if (environmentLayout.getVisibility() == View.VISIBLE) {
                environmentLayout.setVisibility(View.GONE);
            } else {
                environmentLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void customEnvDoneClick() {
        String envName = mCustomEnvNameET.getText().toString().trim();
        String restValue = mCustomEnvRestET.getText().toString().trim();
        String secureValue = mCustomEnvSecureET.getText().toString().trim();
        if (envName.isEmpty() || envName.length() == 0 | restValue.isEmpty() || restValue.length() == 0 || secureValue.isEmpty() || secureValue.length() == 0) {
            mErrorText.setText("Please fill all fields");
            mErrorText.setVisibility(View.VISIBLE);
        } else {
            mServerContext = restValue;
            UltaDataCache.getDataCacheInstance().setServerAddress(envName);
            navigateToApplication();

        }
    }

    @Override
    public void onClick(View v) {

        mainActivityIntent = new Intent(SetEnvironmentActivity.this,
                HomeActivity.class);

        switch (v.getId()) {

            case R.id.productionEnvironmentTV:
                expandAndCollapseEnvironmentLayout(productionEnvironments);
                break;
            case R.id.qaEnvironmentTV:
                expandAndCollapseEnvironmentLayout(qaEnvironmens);
                break;
            case R.id.devEnvironmentTV:
                expandAndCollapseEnvironmentLayout(devEnvironments);
                break;
            case R.id.customEnvironmentTV:
                expandAndCollapseEnvironmentLayout(mCustomEnvironments);
                break;
            case R.id.customEnvDoneButton:
                customEnvDoneClick();
                break;
            // Production, PROD -A , PROD - B point to api.ulta.com
            case R.id.prodTextView:
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.prodServerAddress);
                navigateToApplication();
                break;

            case R.id.prod_A:
                mServerContext = "rest/bean";
                WebserviceConstants.isULTA_SITE_VALUE = true;
                WebserviceConstants.ULTA_SITE_VALUE = "A";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.prodServerAddress);
                navigateToApplication();
                break;
            case R.id.prod_B:
                mServerContext = "rest/bean";
                WebserviceConstants.isULTA_SITE_VALUE = true;
                WebserviceConstants.ULTA_SITE_VALUE = "B";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.prodServerAddress);
                navigateToApplication();
                break;
            case R.id.stressTextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.stressServerAddress);
                navigateToApplication();
                break;
            case R.id.stagingTextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.stagingServerAddress);
                navigateToApplication();
                break;
            case R.id.api_uat:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.UATAddress);
                navigateToApplication();
                break;

            case R.id.qa01TextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.qa01ServerAddress);
                navigateToApplication();
                break;
            case R.id.qa02TextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.qa02ServerAddress);
                navigateToApplication();
                break;
            case R.id.qa03TextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.qa03ServerAddress);
                navigateToApplication();
                break;

            case R.id.da01TextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.da01ServerAddress);
                navigateToApplication();
                break;
            case R.id.da02TextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.da02ServerAddress);
                navigateToApplication();
                break;
            case R.id.da3TextView:

                mServerContext = "rest/bean";
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.da03ServerAddress);
                navigateToApplication();
                break;
            default:
                UltaDataCache.getDataCacheInstance().setServerAddress(
                        WebserviceConstants.prodServerAddress);
                navigateToApplication();
                break;
        }
    }

    private void navigateToApplication() {
        setSharedPreference();
        mEnvironmentDialog.dismiss();
        finish();
        mainActivityIntent.putExtra("isFirstLaunch", true);
        startActivity(mainActivityIntent);
    }

    private void setSharedPreference() {
        SharedPreferences preferences = this.getSharedPreferences(
                "userdetails", MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.clear();
        edit.putBoolean("isFirstTime", false);
        edit.putBoolean("isSecurityEnabled", isSecurityEnabled);
        edit.putString("serverAddress", UltaDataCache.getDataCacheInstance().getServerAddress());
        edit.putString("serverContext", mServerContext);
        edit.putBoolean("isULTA_SITE_VALUE", WebserviceConstants.isULTA_SITE_VALUE);
        edit.putString("ULTA_SITE_VALUE", WebserviceConstants.ULTA_SITE_VALUE);

        edit.commit();

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.hashCode() == mCustomEnvNameET.getText().hashCode() ||
                editable.hashCode() == mCustomEnvRestET.getText().hashCode() ||
                editable.hashCode() == mCustomEnvSecureET.getText().hashCode()) {
            mErrorText.setVisibility(View.GONE);
        }
    }
}
