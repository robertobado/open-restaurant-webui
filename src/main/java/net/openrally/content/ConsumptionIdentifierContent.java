package net.openrally.content;

import net.openrally.SessionStorage;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.manager.BaseEntityManager;
import net.openrally.manager.ConsumptionIdentifierManager;

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

public class ConsumptionIdentifierContent extends CRUDContent implements SelectedTabChangeListener{

	private static final long serialVersionUID = -3112286751475588679L;

	private TextArea newEntityIdentifier;
	private TextArea newEntityDescription;
	
	private TextArea editEntityIdentifier;
	private TextArea editEntityDescription;
	private ConsumptionIdentifier editInstance;
	
	public ConsumptionIdentifierContent(SessionStorage sessionStorage){
		super(new ConsumptionIdentifierManager(sessionStorage));
	}
	
	protected void setListTableHeaders(){
		listTable.setColumnHeader("identifier", "Identificador");
        listTable.setColumnHeader("description", "Descrição");
        listTable.setColumnHeader(BaseEntityManager.CONTAINER_EDIT_PROPERTY, "Editar");
        listTable.setColumnHeader(BaseEntityManager.CONTAINER_DELETE_PROPERTY, "Removers");
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
	
	public void editButtonClickListener(ClickEvent event) {
		editInstance = (ConsumptionIdentifier) ((Button) event.getComponent()).getData();
		initializeEditTab();
	}
	
	public void deleteButtonClickListener(ClickEvent event) {
		ConsumptionIdentifier consumptionIdentifier = (ConsumptionIdentifier) ((Button) event.getComponent()).getData();
		Notification notification = manager.deleteEntity(consumptionIdentifier);
		getWindow().showNotification(notification);
		refreshEntityList();
	}
	
	public void saveNewEntityButtonClickListener(Button.ClickEvent event){
		ConsumptionIdentifier consumptionIdentifier = new ConsumptionIdentifier();
		consumptionIdentifier.setIdentifier((String) newEntityIdentifier.getValue());
		consumptionIdentifier.setDescription((String) newEntityDescription.getValue());
		Notification notification = manager.createEntity(consumptionIdentifier);
		getWindow().showNotification(notification);
		refreshEntityList();
	}
	
	public void saveEditEntityButtonClickListener(Button.ClickEvent event){
		editInstance.setIdentifier((String) editEntityIdentifier.getValue());
		editInstance.setDescription((String) editEntityDescription.getValue());
		Notification notification = manager.updateEntity(editInstance);
		getWindow().showNotification(notification);
		
		if(notification.getDelayMsec() > 0){
			refreshEntityList();
			removeTab(getTab(editPanel));
			setSelectedTab(listPanel);
		}
	}

}
