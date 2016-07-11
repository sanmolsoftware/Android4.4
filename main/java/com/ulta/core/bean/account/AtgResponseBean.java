/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *
 */

package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;


/**
 * The Class AtgResponseBean  to read AppConfigHomePageSectionSlotInfo flags.
 *
 * @author Infosys
 */

public class AtgResponseBean extends UltaBean {


    private static final long serialVersionUID = 2271465675330492226L;

    private String Encrpytion_Key;

    private String connectionTimeout;

    private String isDyantraceEnabled;


    private String AndroidApp_ConversantTag;

    private String AndroidApp_Fabric;

    private String AndroidApp_CashStar;

    private String bannerRefreshTime;

    private String prodImageLarge;


    private String responseTime;

    private String showGiftCards;

    private String weeklyAdURL;

    private String showOlapicGallery;

    private String testStream;

    private String scanCC;

    private String applyUltaCC;
    private String manageAccountCBCC;
    private String manageAccountNoCard;
    private String manageAccountPLCC;

    public String getScanCC() {
        return scanCC;
    }

    public void setScanCC(String scanCC) {
        this.scanCC = scanCC;
    }

    private String messageInfo;

    private String messageDetails;

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(String messageDetails) {
        this.messageDetails = messageDetails;
    }


    public String getTestStream() {
        return testStream;
    }

    public void setTestStream(String testStream) {
        this.testStream = testStream;
    }

    public String getShowGiftCards() {
        return showGiftCards;
    }

    public void setShowGiftCards(String showGiftCards) {
        this.showGiftCards = showGiftCards;
    }

    public String getShowOlapicGallery() {
        return showOlapicGallery;
    }

    public void setShowOlapicGallery(String showOlapicGallery) {
        this.showOlapicGallery = showOlapicGallery;
    }

    public String getEncrpytion_Key() {
        return Encrpytion_Key;
    }

    public void setEncrpytion_Key(String encrpytion_Key) {
        Encrpytion_Key = encrpytion_Key;
    }

    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(String connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getIsDyantraceEnabled() {
        return isDyantraceEnabled;
    }

    public void setIsDyantraceEnabled(String isDyantraceEnabled) {
        this.isDyantraceEnabled = isDyantraceEnabled;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getBannerRefreshTime() {
        return bannerRefreshTime;
    }

    public void setBannerRefreshTime(String bannerRefreshTime) {
        this.bannerRefreshTime = bannerRefreshTime;
    }


    public String getAndroidApp_ConversantTag() {
        return AndroidApp_ConversantTag;
    }

    public void setAndroidApp_ConversantTag(String AndroidApp_ConversantTag) {
        AndroidApp_ConversantTag = AndroidApp_ConversantTag;
    }

    public String getAndroidApp_Fabric() {
        return AndroidApp_Fabric;
    }

    public void setAndroidApp_Fabric(String AndroidApp_Fabric) {
        AndroidApp_Fabric = AndroidApp_Fabric;
    }

    public String getAndroidApp_CashStar() {
        return AndroidApp_CashStar;
    }

    public void setAndroidApp_CashStar(String AndroidApp_CashStar) {
        AndroidApp_CashStar = AndroidApp_CashStar;
    }

    public String getProdImageLarge() {
        return prodImageLarge;
    }

    public void setProdImageLarge(String prodImageLarge) {
        this.prodImageLarge = prodImageLarge;
    }

    public String getManageAccountCBCC() {
        return manageAccountCBCC;
    }

    public void setManageAccountCBCC(String manageAccountCBCC) {
        this.manageAccountCBCC = manageAccountCBCC;
    }

    public String getManageAccountNoCard() {
        return manageAccountNoCard;
    }

    public void setManageAccountNoCard(String manageAccountNoCard) {
        this.manageAccountNoCard = manageAccountNoCard;
    }

    public String getManageAccountPLCC() {
        return manageAccountPLCC;
    }

    public void setManageAccountPLCC(String manageAccountPLCC) {
        this.manageAccountPLCC = manageAccountPLCC;
    }

    public String getApplyUltaCC() {
        return applyUltaCC;
    }

    public void setApplyUltaCC(String applyUltaCC) {
        this.applyUltaCC = applyUltaCC;
    }

    public String getWeeklyAdURL() {
        return weeklyAdURL;
    }

    public void setWeeklyAdURL(String weeklyAdURL) {
        this.weeklyAdURL = weeklyAdURL;
    }
}
