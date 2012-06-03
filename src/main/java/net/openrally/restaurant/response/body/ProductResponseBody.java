package net.openrally.restaurant.response.body;

import org.apache.commons.lang.StringUtils;

public class ProductResponseBody {
	
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
	
	public boolean equals(Object other){
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ProductResponseBody)) return false;
	    ProductResponseBody otherEntityBody = (ProductResponseBody)other;
	   
	    if(null == otherEntityBody.getProductId() || !otherEntityBody.getProductId().equals(this.getProductId())){
	    	return false;
	    }
	    if(!StringUtils.equals(otherEntityBody.getName(), this.getName())){
	    	return false;
	    }
	    if(!StringUtils.equals(otherEntityBody.getDescription(), this.getDescription())){
	    	return false;
	    }
	    if(null == otherEntityBody.getPrice() || !otherEntityBody.getPrice().equals(this.getPrice())){
	    	return false;
	    }
	    return true;
	}

}
