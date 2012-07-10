package net.openrally.entity;

import net.openrally.annotations.EntityId;
import net.openrally.annotations.Hidden;

public class BillItem extends Entity {
	
	@EntityId
	@Hidden
	private Long billItemId;
	
	private Long billId;
	private Long productId;
	private Double quantity;
	private Double unitPrice;
	
	public Long getBillItemId() {
		return billItemId;
	}
	public void setBillItemId(Long billItemId) {
		this.billItemId = billItemId;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
