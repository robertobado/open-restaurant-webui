package net.openrally.content;

import net.openrally.SessionStorage;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.manager.ConsumptionIdentifierManager;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window.Notification;

public class ConsumptionIdentifierContent extends TabSheet implements SelectedTabChangeListener{

	private static final long serialVersionUID = -3112286751475588679L;
	
	private static final ThemeResource listIcon = new ThemeResource(
			"images/list.png");
	private static final ThemeResource addIcon = new ThemeResource(
			"images/add.png");
	
	private static final ThemeResource editIcon = new ThemeResource(
			"images/edit.png");

	private Panel addPanel;
	private Panel editPanel;
	private Panel listPanel;
	
	private Table listTable;
	
	private ConsumptionIdentifierManager consumptionIdentifierManager;
	
	private TextArea newEntityIdentifier;
	private TextArea newEntityDescription;
	
	private TextArea editEntityIdentifier;
	private TextArea editEntityDescription;
	private ConsumptionIdentifier editInstance;
	
	public ConsumptionIdentifierContent(SessionStorage sessionStorage){
		consumptionIdentifierManager = new ConsumptionIdentifierManager(sessionStorage);
		
		initializeListTab();
		initializeAddTab();
		
		setSizeFull();
	}
	
	private void initializeListTab(){		
		listTable = new Table();
		listTable.setSizeFull();
		
		listTable.setSelectable(true);
        listTable.setImmediate(true);
        
        refreshEntityList();

        // turn on column reordering and collapsing
        listTable.setColumnReorderingAllowed(true);
        listTable.setColumnCollapsingAllowed(true);
		
        listTable.setColumnHeader(ConsumptionIdentifierManager.PROPERTY_IDENTIFIER, "Identificador");
        listTable.setColumnHeader(ConsumptionIdentifierManager.PROPERTY_DESCRIPTION, "Descrição");
        
		listPanel = new Panel();
		listPanel.addComponent(listTable);
		addTab(listPanel, "Listagem", listIcon);
	}
	
	private void refreshEntityList(){
		// connect data source
        listTable.setContainerDataSource(consumptionIdentifierManager.getContainer(new EditButtonListener(), new DeleteButtonListener()));
	}
	
	private void initializeAddTab(){
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
	
	private void initializeEditTab(){
		if(null == editPanel){
			editPanel = new Panel();
		}
		else{
			editPanel.removeAllComponents();
		}
		
		addTab(editPanel, "Editar", editIcon);
		
		GridLayout grid = new GridLayout(2, 3);
		
		Label identifierLabel = new Label("Identificador: ");
		grid.addComponent(identifierLabel, 0, 0);
		grid.setComponentAlignment(identifierLabel, Alignment.MIDDLE_RIGHT);
		
		editEntityIdentifier = new TextArea();
		editEntityIdentifier.setRows(1);
		editEntityIdentifier.setValue(editInstance.getIdentifier());
		grid.addComponent(editEntityIdentifier, 1, 0);
		grid.setComponentAlignment(editEntityIdentifier, Alignment.MIDDLE_LEFT);
		
		Label descriptionLabel = new Label("Descrição: ");
		grid.addComponent(descriptionLabel, 0, 1);
		grid.setComponentAlignment(descriptionLabel, Alignment.MIDDLE_RIGHT);
		
		editEntityDescription = new TextArea();
		editEntityDescription.setValue(editInstance.getDescription());
		grid.addComponent(editEntityDescription, 1, 1);
		grid.setComponentAlignment(editEntityDescription, Alignment.MIDDLE_LEFT);
		
		Button submitButton = new Button("Salvar");
		
		submitButton.addListener(Button.ClickEvent.class, this, "saveEditEntityButtonClickListener");
		
		grid.addComponent(submitButton, 0, 2, 1, 2);
		grid.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);
		
		grid.setWidth("400px");
		
		editPanel.addComponent(grid);
		setSelectedTab(editPanel);
		
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            getWindow().showNotification("Selected tab: " + tab.getCaption());
        }
	}
	
	private class EditButtonListener implements Button.ClickListener{
		private static final long serialVersionUID = -6500155514004773112L;

		public void buttonClick(ClickEvent event) {
			editInstance = (ConsumptionIdentifier) ((Button) event.getComponent()).getData();
			initializeEditTab();
		}
	}
	
	private class DeleteButtonListener implements Button.ClickListener{

		private static final long serialVersionUID = -2248924472086014350L;

		public void buttonClick(ClickEvent event) {
			ConsumptionIdentifier consumptionIdentifier = (ConsumptionIdentifier) ((Button) event.getComponent()).getData();
			Notification notification = consumptionIdentifierManager.deleteEntity(consumptionIdentifier);
			getWindow().showNotification(notification);
			refreshEntityList();
		}
	}
	
	public void saveNewEntityButtonClickListener(Button.ClickEvent event){
		ConsumptionIdentifier consumptionIdentifier = new ConsumptionIdentifier();
		consumptionIdentifier.setIdentifier((String) newEntityIdentifier.getValue());
		consumptionIdentifier.setDescription((String) newEntityDescription.getValue());
		Notification notification = consumptionIdentifierManager.createEntity(consumptionIdentifier);
		getWindow().showNotification(notification);
		refreshEntityList();
	}
	
	public void saveEditEntityButtonClickListener(Button.ClickEvent event){
		editInstance.setIdentifier((String) editEntityIdentifier.getValue());
		editInstance.setDescription((String) editEntityDescription.getValue());
		Notification notification = consumptionIdentifierManager.updateEntity(editInstance);
		getWindow().showNotification(notification);
		
		if(notification.getDelayMsec() > 0){
			refreshEntityList();
			removeTab(getTab(editPanel));
			setSelectedTab(listPanel);
		}
	}

}
