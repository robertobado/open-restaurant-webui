package net.openrally.content;

import net.openrally.SessionStorage;
import net.openrally.manager.ConsumptionIdentifierManager;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table;

public class ConsumptionIdentifierContent extends TabSheet implements SelectedTabChangeListener, ValueChangeListener{

	private static final long serialVersionUID = -3112286751475588679L;
	
	private static final ThemeResource listIcon = new ThemeResource(
			"images/list.png");
	private static final ThemeResource addIcon = new ThemeResource(
			"images/list.png");

	private Panel addPanel;
	private Panel listPanel;
	
	private Table listTable;
	
	private ConsumptionIdentifierManager consumptionIdentifierManager;
	
	private SessionStorage sessionStorage;
	
	private Long selectedElementId;
	
	public ConsumptionIdentifierContent(SessionStorage sessionStorage){
		
		this.sessionStorage = sessionStorage;
		
		consumptionIdentifierManager = new ConsumptionIdentifierManager(sessionStorage);
		
		initializeListTab();
		initializeAddTab();
		
		setSizeFull();
		addListener(this);
	}
	
	private void initializeListTab(){		
		listTable = new Table();
		listTable.setSizeFull();
		
		listTable.setSelectable(true);
        listTable.setImmediate(true);
        
        // connect data source
        listTable.setContainerDataSource(consumptionIdentifierManager.getContainer(new EditButtonListener(), new DeleteButtonListener()));

        // turn on column reordering and collapsing
        listTable.setColumnReorderingAllowed(true);
        listTable.setColumnCollapsingAllowed(true);
		
        listTable.setColumnHeader(ConsumptionIdentifierManager.PROPERTY_IDENTIFIER, "Identificador");
        listTable.setColumnHeader(ConsumptionIdentifierManager.PROPERTY_DESCRIPTION, "Descrição");
                
        listTable.addListener(this);
        
		listPanel = new Panel();
		listPanel.addComponent(listTable);
		addTab(listPanel, "Listagem", listIcon);
	}
	
	private void initializeAddTab(){
		addPanel = new Panel();
		addTab(addPanel, "Nova", addIcon);

		addPanel.addComponent(new Label("Novo!"));
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            getWindow().showNotification("Selected tab: " + tab.getCaption());
        }
	}
	
	public void valueChange(ValueChangeEvent event) {
		selectedElementId = (Long) event.getProperty().getValue();
    }
	
	private class EditButtonListener implements Button.ClickListener{

		private static final long serialVersionUID = -6500155514004773112L;

		public void buttonClick(ClickEvent event) {
			getWindow().showNotification("Botão editar clicado!");
		}
	}
	
	private class DeleteButtonListener implements Button.ClickListener{

		private static final long serialVersionUID = -2248924472086014350L;

		public void buttonClick(ClickEvent event) {
			getWindow().showNotification("Botão apagar clicado!");
		}
	}

}
