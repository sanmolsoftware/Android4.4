package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicLinksBean extends UltaBean{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7905108547254037522L;

	private OlapicPrevLinkBean prev;
	
	private OlapicNextLinkBean next;
	
	private OlapicFirstLinkBean first;
	
	

	public OlapicFirstLinkBean getFirst() {
		return first;
	}

	public void setFirst(OlapicFirstLinkBean first) {
		this.first = first;
	}

	public OlapicPrevLinkBean getPrev() {
		return prev;
	}

	public void setPrev(OlapicPrevLinkBean prev) {
		this.prev = prev;
	}

	public OlapicNextLinkBean getNext() {
		return next;
	}

	public void setNext(OlapicNextLinkBean next) {
		this.next = next;
	}

	
}
