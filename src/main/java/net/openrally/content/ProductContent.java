package net.openrally.content;

import net.openrally.SessionStorage;
import net.openrally.entity.Entity;
import net.openrally.entity.Product;
import net.openrally.manager.BaseEntityManager;
import net.openrally.manager.ProductManager;

public class ProductContent extends CRUDContent{

	private static final long serialVersionUID = -3112286751475588679L;

	public ProductContent(SessionStorage sessionStorage) {
		super(new ProductManager(sessionStorage));
	}

	protected void setListTableHeaders() {
		listTable.setColumnHeader("name", "Nome");
		listTable.setColumnHeader("description", "Descrição");
		listTable.setColumnHeader("price", "Preço");
		
		listTable.setColumnHeader(BaseEntityManager.CONTAINER_EDIT_PROPERTY,
				"Editar");
		listTable.setColumnHeader(BaseEntityManager.CONTAINER_DELETE_PROPERTY,
				"Removers");
	}

	@Override
	protected Class<? extends Entity> getEntityClass() {
		return Product.class;
	}

}
