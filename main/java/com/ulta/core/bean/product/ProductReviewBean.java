/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;


import com.ulta.core.bean.UltaBean;



/**
 * The Class ProductReviewBean.
 *
 * @author viva
 */
public class ProductReviewBean extends UltaBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3384615679962517009L;


	private double rating;
	

	private double ratingDecimal;
	
	/** The reviews. */
	private int reviews;

	/**
	 * Gets the reviews.
	 *
	 * @return the reviews
	 */
	public int getReviews() {
		return reviews;
	}

	/**
	 * Sets the reviews.
	 *
	 * @param reviews the new reviews
	 */
	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the rating to set
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * Gets the rating decimal.
	 *
	 * @return the ratingDecimal
	 */
	public double getRatingDecimal() {
		return ratingDecimal;
	}

	/**
	 * Sets the rating decimal.
	 *
	 * @param ratingDecimal the ratingDecimal to set
	 */
	public void setRatingDecimal(double ratingDecimal) {
		this.ratingDecimal = ratingDecimal;
	}
	
	

}
