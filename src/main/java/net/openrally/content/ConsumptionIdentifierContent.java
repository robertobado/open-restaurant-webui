package net.openrally.content;

import net.openrally.SessionStorage;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.entity.Entity;
import net.openrally.manager.BaseEntityManager;
import net.openrally.manager.ConsumptionIdentifierManager;

public class ConsumptionIdentifierContent extends CRUDContent{

	private static final long serialVersionUID = -3112286751475588679L;

	public ConsumptionIdentifierContent(SessionStorage sessionStorage){
		super(new ConsumptionIdentifierManager(sessionStorage));
	}
	
	protected void setListTableHeaders(){
		listTable.setColumnHeader("identifier", "Identificador");
        listTable.setColumnHeader("description", "Descrição");
        listTable.setColumnHeader(BaseEntityManager.CONTAINER_EDIT_PROPERTY, "Editar");
        listTable.setColumnHeader(BaseEntityManager.CONTAINER_DELETE_PROPERTY, "Remover");
	}

	@Override
	protected Class<? extends Entity> getEntityClass() {
		return ConsumptionIdentifier.class;
	}
}
