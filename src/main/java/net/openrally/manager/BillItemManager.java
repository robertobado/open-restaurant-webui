package net.openrally.manager;

import java.lang.reflect.Type;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.BillItem;
import net.openrally.entity.Entity;

import com.google.gson.reflect.TypeToken;

public class BillItemManager extends BaseEntityManager {
	
	public enum ItemType {
	    PRODUCT("Product"),
	    TAX("Tax");
	    
	    private String type;
	    
	    ItemType(String type){
	    	this.type = type;
	    }
	    
	    public String toString(){
	    	return type;
	    }
	}

	private static final long serialVersionUID = 7344149701655567987L;
	private static final String PATH = "bill-item";

	public BillItemManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}

	@Override
	protected Class<? extends Entity> getEntityClass() {
		return BillItem.class;
	}

	@Override
	protected String getPath() {
		return PATH;
	}

	@Override
	protected Type getEntityListClass() {
		return new TypeToken<List<BillItem>>() {}.getType();
	}
	
	@SuppressWarnings("unchecked")
	public List<BillItem> getBillItemsForBill(Long billId){
		return (List<BillItem>) getEntityInsanceList("billId=" + billId);
	}

}
