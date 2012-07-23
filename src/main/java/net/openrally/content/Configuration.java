package net.openrally.content;

import net.openrally.entity.Entity;


public class Configuration extends Entity{

	private Long loginTokenLifeTime;

	private String companyName;

	private String billTemplate;

	public Long getLoginTokenLifeTime() {
		return loginTokenLifeTime;
	}

	public void setLoginTokenLifeTime(Long loginTokenLifeTime) {
		this.loginTokenLifeTime = loginTokenLifeTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBillTemplate() {
		return billTemplate;
	}

	public void setBillTemplate(String billTemplate) {
		this.billTemplate = billTemplate;
	}
}
