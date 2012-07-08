package net.openrally.content;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.openrally.annotations.EntityId;
import net.openrally.entity.Entity;
import net.openrally.manager.BaseEntityManager;

import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;

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

	private List<Entry<Field, TextField>> newEntityFieldList;
	private List<Entry<Field, TextField>> editEntityFieldList;
	
	private Entity listEditInstance;

	protected abstract void setListTableHeaders();

	protected abstract Class<? extends Entity> getEntityClass();

	protected CRUDContent(BaseEntityManager manager) {
		this.manager = manager;

		initializeFieldLists();

		initializeListTab();
		initializeAddTab();

		setSizeFull();
	}

	protected void initializeListTab() {
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

	protected void initializeAddTab() {
		addPanel = new Panel();
		addTab(addPanel, "Nova", addIcon);

		GridLayout grid = new GridLayout(2, newEntityFieldList.size() + 1);

		int rownum = 0;
		for (Entry<Field, TextField> entry : newEntityFieldList) {
			Field field = entry.getKey();

			Label identifierLabel = new Label(field.getName());
			grid.addComponent(identifierLabel, 0, rownum);

			TextField inputField = entry.getValue();
			grid.addComponent(inputField, 1, rownum);

			rownum++;
		}

		Button submitButton = new Button("Salvar");

		submitButton.addListener(Button.ClickEvent.class, this,
				"saveNewEntityButtonClickListener");

		grid.addComponent(submitButton, 0, rownum, 1, rownum);
		grid.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);

		grid.setWidth("400px");

		addPanel.addComponent(grid);
	}

	protected void initializeEditTab() {

		if (null == editPanel) {
			editPanel = new Panel();
		} else {
			editPanel.removeAllComponents();
		}

		addTab(editPanel, "Editar", editIcon);

		GridLayout grid = new GridLayout(2, editEntityFieldList.size() + 1);

		int rownum = 0;
		for (Entry<Field, TextField> entry : editEntityFieldList) {
			Field field = entry.getKey();

			Label identifierLabel = new Label(field.getName());
			grid.addComponent(identifierLabel, 0, rownum);

			TextField inputField = entry.getValue();
			grid.addComponent(inputField, 1, rownum);

			rownum++;
		}

		Button submitButton = new Button("Salvar");

		submitButton.addListener(Button.ClickEvent.class, this,
				"saveEditEntityButtonClickListener");

		grid.addComponent(submitButton, 0, rownum, 1, rownum);
		grid.setComponentAlignment(submitButton, Alignment.MIDDLE_CENTER);

		grid.setWidth("400px");

		editPanel.addComponent(grid);
		setSelectedTab(editPanel);

	}

	private void initializeFieldLists() {
		newEntityFieldList = new LinkedList<Entry<Field, TextField>>();
		editEntityFieldList = new LinkedList<Entry<Field, TextField>>();

		Class<? extends Entity> entityClass = getEntityClass();

		Field[] declaredFields = entityClass.getDeclaredFields();

		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(EntityId.class)) {
				continue;
			}

			field.setAccessible(true);

			Entry<Field, TextField> newEntityFieldEntry = new SimpleEntry<Field, TextField>(
					field, new TextField());
			newEntityFieldList.add(newEntityFieldEntry);

			Entry<Field, TextField> editEntityFieldEntry = new SimpleEntry<Field, TextField>(
					field, new TextField());
			editEntityFieldList.add(editEntityFieldEntry);

		}
	}

	protected void refreshEntityList() {
		listTable.setContainerDataSource(manager.getListContainer(this, this));
	}

	public void deleteButtonClickListener(ClickEvent event) {
		Entity entity = (Entity) ((Button) event.getComponent()).getData();
		Notification notification = manager.deleteEntity(entity);
		getWindow().showNotification(notification);
		refreshEntityList();
	}

	public void saveNewEntityButtonClickListener(Button.ClickEvent event) {
		Entity newInstance = dynamicallyGenerateEntity(newEntityFieldList);
		
		Notification notification = manager.createEntity(newInstance);
		getWindow().showNotification(notification);
		refreshEntityList();
	}

	public void editButtonClickListener(ClickEvent event) {
		listEditInstance = (Entity) ((Button) event.getComponent())
				.getData();

		for (Entry<Field, TextField> entry : editEntityFieldList) {
			Field field = entry.getKey();
			TextField inputField = entry.getValue();

			try {
				inputField.setValue(field.get(listEditInstance).toString());
			} catch (ReadOnlyException e) {
				e.printStackTrace();
			} catch (ConversionException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		initializeEditTab();
	}

	public void saveEditEntityButtonClickListener(Button.ClickEvent event) {
		Entity editInstance = dynamicallyGenerateEntity(editEntityFieldList);
		
		editInstance.setId(listEditInstance.getId());
		
		Notification notification = manager.updateEntity(editInstance);
		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			refreshEntityList();
			removeTab(getTab(editPanel));
			setSelectedTab(listPanel);
		}
	}

	private Entity dynamicallyGenerateEntity(List<Entry<Field, TextField>> list) {
		
		Class<? extends Entity> entityClass = getEntityClass();

		Entity newInstance = null;

		try {
			newInstance = entityClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		for (Entry<Field, TextField> entry : list) {
			Field field = entry.getKey();
			TextField inputField = entry.getValue();

			Object value = null;
			if (field.getType() == String.class) {
				value = inputField.getValue();
			} else if (field.getType() == Double.class){
				value = Double.parseDouble((String) inputField.getValue());
			} else if (field.getType() == Long.class) {
				value = Long.parseLong((String) inputField.getValue());
			} else if (field.getType() == Integer.class) {
				value = Integer.parseInt((String) inputField.getValue());
			} else {
				throw new RuntimeException("Cannot convert type "
						+ field.getType().getName() + " for field "
						+ field.getName() + " while saving new entity");
			}

			try {
				field.set(newInstance, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		
		return newInstance;
	}
}
