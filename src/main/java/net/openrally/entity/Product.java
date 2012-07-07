package net.openrally.entity;

import net.openrally.annotations.EntityId;
import net.openrally.annotations.Hidden;

public class Product extends Entity{
	
	@EntityId
	@Hidden
	private Long productId;
	
	private String name;
	private String description;
	private Double price;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
