package net.openrally.content;

import net.openrally.SessionStorage;
import net.openrally.entity.Entity;
import net.openrally.entity.Tax;
import net.openrally.manager.BaseEntityManager;
import net.openrally.manager.TaxManager;

public class TaxContent extends CRUDContent{

	private static final long serialVersionUID = -2852874952720724780L;

	public TaxContent(SessionStorage sessionStorage) {
		super(new TaxManager(sessionStorage));
	}
	
	protected void setListTableHeaders() {
		listTable.setColumnHeader("name", "Nome");
		listTable.setColumnHeader("description", "Descrição");
		listTable.setColumnHeader("amount", "Valor");
		listTable.setColumnHeader("percentage", "Percentual");
		
		listTable.setColumnHeader(BaseEntityManager.CONTAINER_EDIT_PROPERTY,
				"Editar");
		listTable.setColumnHeader(BaseEntityManager.CONTAINER_DELETE_PROPERTY,
				"Remover");
	}
	
	@Override
	protected Class<? extends Entity> getEntityClass() {
		return Tax.class;
	}

}
