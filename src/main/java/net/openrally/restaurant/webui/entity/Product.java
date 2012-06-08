package net.openrally.restaurant.webui.entity;

import net.openrally.restaurant.response.body.ProductResponseBody;

public class Product extends ProductResponseBody implements EditableEntity{

	@Override
	public String getEntityId() {
		return getProductId().toString();				
	}

}
