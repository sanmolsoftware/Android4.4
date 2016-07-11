/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean;

import java.util.List;

/**
 * The Class RewardsBean.
 */
public class RewardsBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5324857164266128043L;
	private List<String> alertList;
	private String balancePoints;
	private int currentYear;
	private double dollarSpendForHighestLevel;
	private int highestTierThreshold;
	private String memberSince;
	private int nextYear;
	private List<String> offerList;
	private String planId;
	private int platinumRewardPercent;
	private boolean platinum;
	private int pointRedeem;
	private double pointRedeemValue;
	private String pointsExpirationDate;
	private int pointsExpiring;
	private boolean requalified;
	private double ytdSpent;

	private List<CampaignOfferListBean> campaignOfferList;// offer details bean

	/**
	 * Gets the alert list.
	 * 
	 * @return the alert list
	 */
	public List<String> getAlertList() {
		return alertList;
	}

	/**
	 * Sets the alert list.
	 * 
	 * @param alertList
	 *            the new alert list
	 */
	public void setAlertList(List<String> alertList) {
		this.alertList = alertList;
	}

	/**
	 * Gets the balancePoints.
	 * 
	 * @return the balancePoints
	 */
	public String getBalancePoints() {
		return balancePoints;
	}

	/**
	 * Sets the balancePoints.
	 * 
	 * @param balancePoints
	 *            the balancePoints
	 */
	public void setBalancePoints(String balancePoints) {
		this.balancePoints = balancePoints;
	}

	/**
	 * Gets the offer list.
	 * 
	 * @return the offer list
	 */
	public List<String> getOfferList() {
		return offerList;
	}

	/**
	 * Sets the offer list.
	 * 
	 * @param offerList
	 *            the new offer list
	 */
	public void setOfferList(List<String> offerList) {
		this.offerList = offerList;
	}

	/**
	 * Gets the point redeem.
	 * 
	 * @return the point redeem
	 */
	public int getPointRedeem() {
		return pointRedeem;
	}

	/**
	 * Sets the point redeem.
	 * 
	 * @param pointRedeem
	 *            the new point redeem
	 */
	public void setPointRedeem(int pointRedeem) {
		this.pointRedeem = pointRedeem;
	}

	/**
	 * Gets the point redeem value.
	 * 
	 * @return the point redeem value
	 */
	public double getPointRedeemValue() {
		return pointRedeemValue;
	}

//	/**
//	 * Sets the point redeem value.
//	 *
//	 * @param pointRedeemValue
//	 *            the new point redeem value
//	 */
//	public void setPointRedeemValue(int pointRedeemValue) {
//		this.pointRedeemValue = pointRedeemValue;
//	}

	/**
	 * Gets the points expiration date.
	 * 
	 * @return the points expiration date
	 */
	public String getPointsExpirationDate() {
		return pointsExpirationDate;
	}

	/**
	 * Sets the points expiration date.
	 * 
	 * @param pointsExpirationDate
	 *            the new points expiration date
	 */
	public void setPointsExpirationDate(String pointsExpirationDate) {
		this.pointsExpirationDate = pointsExpirationDate;
	}

	/**
	 * Gets the points expiring.
	 * 
	 * @return the points expiring
	 */
	public int getPointsExpiring() {
		return pointsExpiring;
	}

	/**
	 * Sets the points expiring.
	 * 
	 * @param pointsExpiring
	 *            the new points expiring
	 */
	public void setPointsExpiring(int pointsExpiring) {
		this.pointsExpiring = pointsExpiring;
	}

	public void setMemberSince(String memberSince) {
		this.memberSince = memberSince;
	}

	public String getMemberSince() {
		return memberSince;
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}

	public double getDollarSpendForHighestLevel() {
		return dollarSpendForHighestLevel;
	}

	public void setDollarSpendForHighestLevel(double dollarSpendForHighestLevel) {
		this.dollarSpendForHighestLevel = dollarSpendForHighestLevel;
	}

	public int getHighestTierThreshold() {
		return highestTierThreshold;
	}

	public void setHighestTierThreshold(int highestTierThreshold) {
		this.highestTierThreshold = highestTierThreshold;
	}

	public int getNextYear() {
		return nextYear;
	}

	public void setNextYear(int nextYear) {
		this.nextYear = nextYear;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public int getPlatinumRewardPercent() {
		return platinumRewardPercent;
	}

	public void setPlatinumRewardPercent(int platinumRewardPercent) {
		this.platinumRewardPercent = platinumRewardPercent;
	}

	public boolean isPlatinum() {
		return platinum;
	}

	public void setPlatinum(boolean platinum) {
		this.platinum = platinum;
	}

	public boolean isRequalified() {
		return requalified;
	}

	public void setRequalified(boolean requalified) {
		this.requalified = requalified;
	}

	public double getYtdSpent() {
		return ytdSpent;
	}

	public void setYtdSpent(double ytdSpent) {
		this.ytdSpent = ytdSpent;
	}

	public void setPointRedeemValue(double pointRedeemValue) {
		this.pointRedeemValue = pointRedeemValue;
	}

	/**
	 * Gets the campaignOfferList
	 * 
	 * @return campaignOfferList
	 */

	public List<CampaignOfferListBean> getCampaignOfferList() {
		return campaignOfferList;
	}

	/**
	 * Sets the campaignOfferList
	 * 
	 * @param campaignOfferList
	 */
	public void setCampaignOfferList(
			List<CampaignOfferListBean> campaignOfferList) {
		this.campaignOfferList = campaignOfferList;

	}

}
