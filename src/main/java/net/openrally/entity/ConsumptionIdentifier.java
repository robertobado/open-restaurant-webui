package net.openrally.entity;

public class ConsumptionIdentifier {
	
	private Long consumptionIdentifierId; 
	private String identifier;
	private String description;
	
	public Long getConsumptionIdentifierId() {
		return consumptionIdentifierId;
	}
	public void setConsumptionIdentifierId(Long consumptionIdentifierId) {
		this.consumptionIdentifierId = consumptionIdentifierId;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
