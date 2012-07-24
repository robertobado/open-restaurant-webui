package net.openrally.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.openrally.SessionStorage;
import net.openrally.annotations.EntityId;
import net.openrally.annotations.Hidden;
import net.openrally.entity.Entity;
import net.openrally.util.StringUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window.Notification;

public abstract class BaseEntityManager extends BaseManager {

	private static final long serialVersionUID = 4513193088185985238L;
	
	protected static final int FAILURE_NOTIFICATION_DISMISS_TIME = -1;

	protected static final int SUCCESS_NOTIFICATION_DISMISS_TIME = 3000;
	
	protected static final ThemeResource editIcon = new ThemeResource(
			"images/edit.png");
	protected static final ThemeResource deleteIcon = new ThemeResource(
			"images/delete.png");
	
	public static final Object CONTAINER_EDIT_PROPERTY = "containerEditProperty";
	
	public static final Object CONTAINER_DELETE_PROPERTY = "containerDeleteProperty";

	public BaseEntityManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}
	
	public Container getListContainer(Object editButtonClickListener, Object deleteButtonClickListener) {
		IndexedContainer container = new IndexedContainer();
		
		Class<? extends Entity> entityClass = getEntityClass();
		
		Field[] entityFields = entityClass.getDeclaredFields();
		
		Field idField = null;
		
		for(Field entityField : entityFields){
			
			// Save entity id field for later use
			if(entityField.isAnnotationPresent(EntityId.class)){
				idField = entityField;
				idField.setAccessible(true);
			}
			
			// Skip hidden fields from showing in list
			if(entityField.isAnnotationPresent(Hidden.class)){
				continue;
			}
			
			container.addContainerProperty(entityField.getName(), entityField.getType(), null);
		}
		
		if(null == idField){
			throw new RuntimeException("Class " + entityClass.getName() + " does not provide a EntityId markup required to generate list container");
		}
		
		container.addContainerProperty(CONTAINER_EDIT_PROPERTY, Button.class, null);
		container.addContainerProperty(CONTAINER_DELETE_PROPERTY, Button.class, null);

		List<? extends Entity> entityIntanceList = getEntityInsanceList();

		for (Entity entityInstance : entityIntanceList) {
			
			Item item = null;
			try {
				item = container.addItem((Long) idField.get(entityInstance));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			} 
			
			for(Field entityField : entityFields){
				
				// Skip hidden fields from showing in list
				if(entityField.isAnnotationPresent(EntityId.class) || entityField.isAnnotationPresent(Hidden.class)){
				continue;
				}
				
				entityField.setAccessible(true);
				
				try {
					item.getItemProperty(entityField.getName()).setValue(entityField.get(entityInstance));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			Button editButton = new Button();
			editButton.addListener(Button.ClickEvent.class, editButtonClickListener, "editButtonClickListener");
			editButton.setIcon(editIcon);
			editButton.setData(entityInstance);
			item.getItemProperty(CONTAINER_EDIT_PROPERTY).setValue(editButton);

			Button deleteButton = new Button();
			deleteButton.addListener(Button.ClickEvent.class, deleteButtonClickListener, "deleteButtonClickListener");
			deleteButton.setIcon(deleteIcon);
			deleteButton.setData(entityInstance);
			item.getItemProperty(CONTAINER_DELETE_PROPERTY).setValue(deleteButton);

		}

		container.sort(new Object[] { idField.getName() },
				new boolean[] { true });

		return container;
	}
	
	protected abstract Class<? extends Entity> getEntityClass();

	protected List<? extends Entity> getEntityInsanceList(String queryParams){
		HttpGet httpGet = generateBasicHttpGet(getPath() + "?" + queryParams);
		return getEntityInsanceList(httpGet);
	}
	
	public List<? extends Entity> getEntityInsanceList() {
		HttpGet httpGet = generateBasicHttpGet(getPath());
		return getEntityInsanceList(httpGet);
	}
	
	protected List<? extends Entity> getEntityInsanceList(HttpGet httpGet) {
		try {
			HttpResponse response = getHttpClient().execute(httpGet);
			String responseBody = StringUtilities
					.httpResponseAsString(response);

			Type listType = getEntityListClass();
			
			List<? extends Entity> entityList = gson.fromJson(responseBody, listType);

			return entityList;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new LinkedList<Entity>();
		} catch (IOException e) {
			e.printStackTrace();
			return new LinkedList<Entity>();
		}
	}
	
	protected abstract String getPath();

	protected abstract Type getEntityListClass();
	
	public Notification deleteEntity(Entity entity){
		
			HttpDelete httpDelete = generateBasicHttpDelete(getPath() + SLASH
					+ entity.getId());

			Notification notification;

			try {
				HttpResponse response = getHttpClient().execute(httpDelete);

				if (HttpStatus.SC_NO_CONTENT == response.getStatusLine().getStatusCode()) {
					notification = new Notification("Sucesso",
							"Entidade removida com sucesso",
							Notification.TYPE_HUMANIZED_MESSAGE);
					notification.setDelayMsec(SUCCESS_NOTIFICATION_DISMISS_TIME);
				} else if (HttpStatus.SC_CONFLICT == response.getStatusLine()
						.getStatusCode()) {
					notification = new Notification("Erro ao remover entidade",
							"Existem registros associados à entidade",
							Notification.TYPE_WARNING_MESSAGE);
					notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
				} else if (HttpStatus.SC_NOT_FOUND == response.getStatusLine()
						.getStatusCode()) {
					notification = new Notification("Erro ao remover entidade",
							"Entidade não encontrada", Notification.TYPE_ERROR_MESSAGE);
					notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
				} else {
					notification = new Notification("Erro ao remover entidade",
							"Erro desconhecido.", Notification.TYPE_ERROR_MESSAGE);
					notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
				}
			} catch (ClientProtocolException e) {
				notification = new Notification("Erro ao remover entidade",
						"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			} catch (IOException e) {
				notification = new Notification("Erro ao remover entidade",
						"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			}
			return notification;
		
	}

	public Notification createEntity(Entity entity) {
		
		String requestBody = gson.toJson(entity);

		HttpPost httpPost = generateBasicHttpPost(getPath());

		Notification notification;
		try {
			httpPost.setEntity(new StringEntity(requestBody, UTF_8));
			HttpResponse response = getHttpClient().execute(httpPost);

			if (HttpStatus.SC_CREATED == response.getStatusLine()
					.getStatusCode()) {
				notification = new Notification("Sucesso",
						"Entidade criada com sucesso",
						Notification.TYPE_HUMANIZED_MESSAGE);
				notification.setDelayMsec(SUCCESS_NOTIFICATION_DISMISS_TIME);
			} else {
				notification = new Notification("Erro ao criar entidade",
						"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			}

		} catch (Exception e) {
			notification = new Notification("Erro ao criar entidade",
					"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}

		return notification;
	}

	public Notification updateEntity(Entity entity) {
		String requestBody = gson.toJson(entity);
		
		HttpPut httpPut = generateBasicHttpPut(getPath() + SLASH + entity.getId());

		Notification notification;
		try {
			httpPut.setEntity(new StringEntity(requestBody, UTF_8));
			HttpResponse response = getHttpClient().execute(httpPut);

			if (HttpStatus.SC_OK == response.getStatusLine()
					.getStatusCode()) {
				notification = new Notification("Sucesso",
						"Entidade atualizada com sucesso",
						Notification.TYPE_HUMANIZED_MESSAGE);
				notification.setDelayMsec(SUCCESS_NOTIFICATION_DISMISS_TIME);
			} else {
				notification = new Notification("Erro ao atualizar entidade",
						"Erro desconhecido.", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			}

		} catch (Exception e) {
			notification = new Notification("Erro ao atualizar entidade",
					"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}

		return notification;
	}
	
	public Map<Long, ? extends Entity> getEntityMap() {
		List<? extends Entity> entityList = getEntityInsanceList();
		
		Map<Long, Entity> entityMap = new HashMap<Long, Entity>();
		
		for(Entity entity : entityList){
			entityMap.put(entity.getId(), entity);
		}
		
		return entityMap;
	}

}
