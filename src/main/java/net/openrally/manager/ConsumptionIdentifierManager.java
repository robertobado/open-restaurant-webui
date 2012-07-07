package net.openrally.manager;

import java.lang.reflect.Type;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.entity.Entity;

import com.google.gson.reflect.TypeToken;

public class ConsumptionIdentifierManager extends BaseEntityManager {

	private static final long serialVersionUID = 4070524836393555115L;

	public static final String PATH = "consumption-identifier";

	public ConsumptionIdentifierManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}

	@Override
	protected Class<? extends Entity> getEntityClass() {
		return ConsumptionIdentifier.class;
	}

	@Override
	protected String getPath() {
		return PATH;
	}

	@Override
	protected Type getEntityListClass() {
		return new TypeToken<List<ConsumptionIdentifier>>() {}.getType();
	}
}
