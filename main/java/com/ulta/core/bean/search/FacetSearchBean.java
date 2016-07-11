package com.ulta.core.bean.search;



import com.ulta.core.bean.UltaBean;

public class FacetSearchBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2657251137362350457L;
	private String id;
	private String name;
	private int count;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
