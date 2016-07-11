/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;



import com.ulta.core.bean.UltaBean;



/**
 * The Class FreeSamplesDetailBean.
 */
public class FreeSamplesDetailBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8442108463185056596L;

	/** The brands details. */
	private BrandDetailsBean brandsDetails;
	
	/** The sample details. */
	private SampleDetailsBean sampleDetails;

	/** compulsoryFreeSample flag*/
	private boolean compulsoryFreeSample;
	
/** The sku details. */
private SkuDetailsBean skuDetails;
	
	/**
	 * Gets the sku details.
	 *
	 * @return the sku details
	 */
	public SkuDetailsBean getSkuDetails() {
		return skuDetails;
	}

	/**
	 * Sets the sku details.
	 *
	 * @param skuDetails the new sku details
	 */
	public void setSkuDetails(SkuDetailsBean skuDetails) {
		this.skuDetails = skuDetails;
	}

	
	/**
	 * Gets the brands details.
	 *
	 * @return the brandsDetails
	 */
	public BrandDetailsBean getBrandsDetails() {
		return brandsDetails;
	}
	
	/**
	 * Sets the brands details.
	 *
	 * @param brandsDetails the brandsDetails to set
	 */
	public void setBrandsDetails(BrandDetailsBean brandsDetails) {
		this.brandsDetails = brandsDetails;
	}
	
	/**
	 * Gets the sample details.
	 *
	 * @return the sampleDetails
	 */
	public SampleDetailsBean getSampleDetails() {
		return sampleDetails;
	}
	
	/**
	 * Sets the sample details.
	 *
	 * @param sampleDetails the sampleDetails to set
	 */
	public void setSampleDetails(SampleDetailsBean sampleDetails) {
		this.sampleDetails = sampleDetails;
	}

	public boolean isCompulsoryFreeSample() {
		return compulsoryFreeSample;
	}

	public void setCompulsoryFreeSample(boolean compulsoryFreeSample) {
		this.compulsoryFreeSample = compulsoryFreeSample;
	}
}
