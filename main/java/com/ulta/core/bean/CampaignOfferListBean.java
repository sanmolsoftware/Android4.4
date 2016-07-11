package com.ulta.core.bean;
/**
 * 
 * 
 * Bean class for campaign offer in rewards web service
 *
 */
public class CampaignOfferListBean extends UltaBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1608248939369190861L;
	private String campaignOfferId;
	private String campaignOfferMasterId;
	private String description;
	private boolean isActive;
	
	/**
	 * Getter for offer ID
	 * @return campaignOfferId
	 */
	public String getCampaignOfferId() {
		return campaignOfferId;
	}

	/**
	 * Setter for offer id
	 * @param campaignOfferId
	 */
	public void setCampaignOfferId(String campaignOfferId) {
		this.campaignOfferId = campaignOfferId;
	}
	
	/**
	 * Getter for master id
	 * @return campaignOfferMasterId
	 */
	public String getCampaignOffermasterId() {
		return campaignOfferMasterId;
	}

	/**
	 * setter for master id
	 * @param campaignOfferMasterId
	 */
	public void setCampaignOffermasterId(String campaignOfferMasterId) {
		this.campaignOfferMasterId = campaignOfferMasterId;
	}
	
	/**
	 * Getter for offer description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for offer description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * getter to get flag indicating offer is activated or not
	 * @return isActive
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * setter to set flag indicating offer is activated or not
	 * @param isActive
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
