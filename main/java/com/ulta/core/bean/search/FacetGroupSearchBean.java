package com.ulta.core.bean.search;


import com.ulta.core.bean.UltaBean;

import java.util.List;

public class FacetGroupSearchBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8786635739250505783L;
	private String id;
	private String name;
	private List<FacetSearchBean> facetList;
	
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
	public List<FacetSearchBean> getFacetList() {
		return facetList;
	}
	public void setFacetList(List<FacetSearchBean> facetList) {
		this.facetList = facetList;
	}
}
