package net.openrally.manager;

import java.lang.reflect.Type;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.Entity;
import net.openrally.entity.Tax;

import com.google.gson.reflect.TypeToken;

public class TaxManager extends BaseEntityManager {

	private static final long serialVersionUID = -1454437769648741722L;
	
	public static final String PATH = "tax";

	public TaxManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}
	
	@Override
	protected Class<? extends Entity> getEntityClass() {
		return Tax.class;
	}

	@Override
	protected String getPath() {
		return PATH;
	}

	@Override
	protected Type getEntityListClass() {
		return new TypeToken<List<Tax>>() {}.getType();
	}

}
