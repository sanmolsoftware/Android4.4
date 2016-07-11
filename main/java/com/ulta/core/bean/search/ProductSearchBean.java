package com.ulta.core.bean.search;



import com.ulta.core.bean.UltaBean;
import com.ulta.core.util.Utility;

public class ProductSearchBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1733656995267464470L;
	private static int IMAGE_HEIGHT=300;
	private static int IMAGE_WIDTH=300;
	private String url;  
	private String title;  
	private String text;  
	private String image_url;  
	private double price;  
	private float review_rating;  
	private int review_count;  
	private String product_id;  
	private double sku_id;  
	private String ad_bug;  
	private String brand;
	private int has_variant;
	private String url_title;
	private int rank;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		if(Utility.stringNullEmptyValidator(image_url)){
			String url=Utility.modifyImageResolution(image_url,IMAGE_HEIGHT, IMAGE_WIDTH);
			this.image_url = url;
		}else{
			this.image_url = image_url;
		}
		
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public float getReview_rating() {
		return review_rating;
	}

	public void setReview_rating(float review_rating) {
		this.review_rating = review_rating;
	}

	public int getReview_count() {
		return review_count;
	}

	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public double getSku_id() {
		return sku_id;
	}

	public void setSku_id(double sku_id) {
		this.sku_id = sku_id;
	}

	public String getAd_bug() {
		return ad_bug;
	}

	public void setAd_bug(String ad_bug) {
		this.ad_bug = ad_bug;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getHas_variant() {
		return has_variant;
	}

	public void setHas_variant(int has_variant) {
		this.has_variant = has_variant;
	}

	public String getUrl_title() {
		return url_title;
	}

	public void setUrl_title(String url_title) {
		this.url_title = url_title;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}
