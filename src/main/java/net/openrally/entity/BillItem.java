package net.openrally.entity;

import net.openrally.annotations.EntityId;
import net.openrally.annotations.Hidden;

public class BillItem extends Entity {
	
	@EntityId
	@Hidden
	private Long billItemId;
	
	private Long billId;
	private String type;
	private Long referenceId;
	private Double quantity;
	private Double unitPrice;
	
	public BillItem(BillItem originalBillItem) {
		this.billItemId = originalBillItem.billItemId;
		this.billId = originalBillItem.billId;
		this.type = originalBillItem.type;
		this.referenceId = originalBillItem.referenceId;
		this.quantity = originalBillItem.quantity;
		this.unitPrice = originalBillItem.unitPrice;
	}
	
	public BillItem() {
	}
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
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
