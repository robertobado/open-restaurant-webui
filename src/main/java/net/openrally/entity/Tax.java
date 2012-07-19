package net.openrally.entity;

import net.openrally.annotations.EntityId;
import net.openrally.annotations.Hidden;

public class Tax extends Entity {

	@EntityId
	@Hidden
	private Long taxId;
	
	private String name;
	private String description;
	private Double amount;
	private Boolean percentage;
	
	public Long getTaxId() {
		return taxId;
	}
	public void setTaxId(Long taxId) {
		this.taxId = taxId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Boolean getPercentage() {
		return percentage;
	}
	public void setPercentage(Boolean percentage) {
		this.percentage = percentage;
	}
	
}
