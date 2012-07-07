package net.openrally.content;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.openrally.manager.BaseEntityManager;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;

public abstract class CRUDContent extends TabSheet {

	private static final long serialVersionUID = 3235195943482378765L;
	
	protected static final ThemeResource listIcon = new ThemeResource(
			"images/list.png");
	protected static final ThemeResource addIcon = new ThemeResource(
			"images/add.png");
	
	protected static final ThemeResource editIcon = new ThemeResource(
			"images/edit.png");

	protected Panel addPanel;
	protected Panel editPanel;
	protected Panel listPanel;
	
	protected Table listTable;
	
	protected BaseEntityManager manager;
	
	private List<Entry<Field, Component>> newEntityFieldList;
	
	public abstract void deleteButtonClickListener(ClickEvent event);
	public abstract void editButtonClickListener(ClickEvent event);
	protected abstract void setListTableHeaders();
	
	protected CRUDContent(BaseEntityManager manager){
		this.manager = manager;
		
		initializeFieldLists();
				
		initializeListTab();
		initializeAddTab();
		
		setSizeFull();
	}
	
	protected void initializeListTab(){		
		listTable = new Table();
		listTable.setSizeFull();
		
		listTable.setSelectable(true);
        listTable.setImmediate(true);
        
        refreshEntityList();

        // turn on column reordering and collapsing
        listTable.setColumnReorderingAllowed(true);
        listTable.setColumnCollapsingAllowed(true);
		
        setListTableHeaders();
        
		listPanel = new Panel();
		listPanel.addComponent(listTable);
		addTab(listPanel, "Listagem", listIcon);
	}
	
	protected void initializeAddTab(){
		addPanel = new Panel();
		addTab(addPanel, "Nova", addIcon);
		
		GridLayout grid = new GridLayout(2, 3);
		
		Label identifierLabel = new Label("Identificador: ");
		grid.addComponent(identifierLabel, 0, 0);
		grid.setComponentAlignment(identifierLabel, Alignment.MIDDLE_RIGHT);
		
		newEntityIdentifier = new TextArea();
		newEntityIdentifier.setRows(1);
		grid.addComponent(newEntityIdentifier, 1, 0);
		grid.setComponentAlignment(newEntityIdentifier, Alignment.MIDDLE_LEFT);
		
		Label descriptionLabel = new Label("Descrição: ");
		grid.addComponent(descriptionLabel, 0, 1);
		grid.setComponentAlignment(descriptionLabel, Alignment.MIDDLE_RIGHT);
		
		newEntityDescription = new TextArea();
		grid.addComponent(newEntityDescription, 1, 1);
		grid.setComponentAlignment(newEntityDescription, Alignment.MIDDLE_LEFT);
		
		Button submitButton = new Button("Salvar");
		
		submitButton.addListener(Button.ClickEvent.class, this, "saveNewEntityButtonClickListener");
		
		grid.addComponent(submitButton, 0, 2, 1, 2);
		grid.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		grid.setWidth("400px");
		
		addPanel.addComponent(grid);
	}	
	
	private void initializeFieldLists(){
		newEntityFieldList = new LinkedList<Entry<Field, Component>>();
		
		Class entityClass = getEntityClass();
	}
	
	protected void refreshEntityList(){
        listTable.setContainerDataSource(manager.getListContainer(this, this));
	}
}
