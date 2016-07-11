package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class BuyMoreSaveMoreAtgResponseBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4785196783560084828L;
	
	private String displayName;
	
	private String id;
	
	private String promoDescription;
	
	private String promoOfferDate;
	
	private String promoOfferAdbug;
	
	private String promoOfferLink;

	public String getPromoOfferLink() {
		return promoOfferLink;
	}

	public void setPromoOfferLink(String promoOfferLink) {
		this.promoOfferLink = promoOfferLink;
	}

	public String getPromoOfferAdbug() {
		return promoOfferAdbug;
	}

	public void setPromoOfferAdbug(String promoOfferAdbug) {
		this.promoOfferAdbug = promoOfferAdbug;
	}

	public String getPromoOfferDate() {
		return promoOfferDate;
	}

	public void setPromoOfferDate(String promoOfferDate) {
		this.promoOfferDate = promoOfferDate;
	}

	public String getPromoDescription() {
		return promoDescription;
	}

	public void setPromoDescription(String promoDescription) {
		this.promoDescription = promoDescription;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
