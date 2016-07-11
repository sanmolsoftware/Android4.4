package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class BeautyClubPlanTypeBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3724996827931669643L;
	private String planDesc;
	private String planLevel;
	private String planType;
	private String repositoryId;
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}
	public String getPlanLevel() {
		return planLevel;
	}
	public void setPlanLevel(String planLevel) {
		this.planLevel = planLevel;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}
	public String getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	
	
}
