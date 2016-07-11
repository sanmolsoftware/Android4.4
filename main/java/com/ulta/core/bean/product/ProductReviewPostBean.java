/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

/**
 * @author viva
 *
 */
public class ProductReviewPostBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8167929255455613791L;
	private ProductReviewPostDetailsBean review;

	/**
	 * @return the review
	 */
	public ProductReviewPostDetailsBean getReview() {
		return review;
	}

	/**
	 * @param review the review to set
	 */
	public void setReview(ProductReviewPostDetailsBean review) {
		this.review = review;
	}
	
	
}
