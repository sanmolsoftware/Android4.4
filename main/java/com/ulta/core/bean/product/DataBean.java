package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class DataBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3256258524582565586L;
	private String headline;
	private String created_date;
	private String location;
	private double rating;
	private String comments;
	private String name;
	private String reviewer_type;
	
	public String getReviewer_type() {
		return reviewer_type;
	}
	public void setReviewer_type(String reviewer_type) {
		this.reviewer_type = reviewer_type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	
	
}
