package net.openrally.entity;

import java.util.List;

public class EntityList<T extends Entity> {
	List<T> list;
	
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
