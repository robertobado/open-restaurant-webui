package net.openrally.manager;

import java.lang.reflect.Type;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.Entity;
import net.openrally.entity.Product;

import com.google.gson.reflect.TypeToken;

public class ProductManager extends BaseEntityManager{
	
	private static final String PATH = "product";

	private static final long serialVersionUID = 5985862212545151188L;

	public ProductManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}
	
	@Override
	protected Class<? extends Entity> getEntityClass() {
		return Product.class;
	}
	
	@Override
	protected String getPath() {
		return PATH;
	}
	
	@Override
	protected Type getEntityListClass() {
		return new TypeToken<List<Product>>() {}.getType();
	}

	
}
