package net.openrally.manager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.entity.ConsumptionIdentifierList;
import net.openrally.util.RandomGenerator;
import net.openrally.util.StringUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

public class ConsumptionIdentifierManager extends BaseManager {

	private static final int FAILURE_NOTIFICATION_DISMISS_TIME = -1;

	private static final int SUCCESS_NOTIFICATION_DISMISS_TIME = 3000;

	private static final long serialVersionUID = 4070524836393555115L;

	private static final ThemeResource editIcon = new ThemeResource(
			"images/edit.png");
	private static final ThemeResource deleteIcon = new ThemeResource(
			"images/delete.png");

	public static final String PATH = "consumption-identifier";

	public static final Object PROPERTY_IDENTIFIER = "identifier";
	public static final Object PROPERTY_DESCRIPTION = "description";
	public static final Object PROPERTY_EDIT = "edit";
	public static final Object PROPERTY_DELETE = "delete";

	public ConsumptionIdentifierManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}

	public Container getContainer(ClickListener editButtonListener,
			ClickListener deleteButtonListener) {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty(PROPERTY_IDENTIFIER, String.class, null);
		container
				.addContainerProperty(PROPERTY_DESCRIPTION, String.class, null);
		container.addContainerProperty(PROPERTY_EDIT, Button.class, null);
		container.addContainerProperty(PROPERTY_DELETE, Button.class, null);

		List<ConsumptionIdentifier> consumptionIdentifiers = getConsumptionIdentifiers();

		for (ConsumptionIdentifier consumptionIdentifier : consumptionIdentifiers) {
			Item item = container.addItem(consumptionIdentifier
					.getConsumptionIdentifierId());
			item.getItemProperty(PROPERTY_IDENTIFIER).setValue(
					consumptionIdentifier.getIdentifier());
			item.getItemProperty(PROPERTY_DESCRIPTION).setValue(
					consumptionIdentifier.getDescription());

			Button editButton = new Button();
			editButton.addListener(editButtonListener);
			editButton.setIcon(editIcon);
			editButton.setData(consumptionIdentifier);
			item.getItemProperty(PROPERTY_EDIT).setValue(editButton);

			Button deleteButton = new Button();
			deleteButton.addListener(deleteButtonListener);
			deleteButton.setIcon(deleteIcon);
			deleteButton.setData(consumptionIdentifier);
			item.getItemProperty(PROPERTY_DELETE).setValue(deleteButton);

		}

		container.sort(new Object[] { PROPERTY_IDENTIFIER },
				new boolean[] { true });

		return container;
	}

	private List<ConsumptionIdentifier> getConsumptionIdentifiers() {
		List<ConsumptionIdentifier> list = new LinkedList<ConsumptionIdentifier>();

		HttpGet httpGet = generateBasicHttpGet(PATH);
		try {
			HttpResponse response = getHttpClient().execute(httpGet);
			String responseBody = StringUtilities
					.httpResponseAsString(response);
			ConsumptionIdentifierList consumptionIdentifierResponseBody = gson
					.fromJson(responseBody, ConsumptionIdentifierList.class);

			return consumptionIdentifierResponseBody.getList();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new LinkedList<ConsumptionIdentifier>();
		} catch (IOException e) {
			e.printStackTrace();
			return new LinkedList<ConsumptionIdentifier>();
		}
	}

	public Notification deleteEntity(ConsumptionIdentifier consumptionIdentifier) {
		HttpDelete httpDelete = generateBasicHttpDelete(PATH + SLASH
				+ consumptionIdentifier.getConsumptionIdentifierId());

		Notification notification;

		try {
			HttpResponse response = getHttpClient().execute(httpDelete);

			if (HttpStatus.SC_NO_CONTENT == response.getStatusLine().getStatusCode()) {
				notification = new Notification("Sucesso",
						"Mesa removida com sucesso",
						Notification.TYPE_HUMANIZED_MESSAGE);
				notification.setDelayMsec(SUCCESS_NOTIFICATION_DISMISS_TIME);
			} else if (HttpStatus.SC_CONFLICT == response.getStatusLine()
					.getStatusCode()) {
				notification = new Notification("Erro ao remover mesa",
						"Existem registros associados à mesa",
						Notification.TYPE_WARNING_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			} else if (HttpStatus.SC_NOT_FOUND == response.getStatusLine()
					.getStatusCode()) {
				notification = new Notification("Erro ao remover mesa",
						"Mesa não encontrada", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			} else {
				notification = new Notification("Erro ao remover mesa",
						"Erro desconhecido. Status: " + response.getStatusLine()
						.getStatusCode(), Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			}
		} catch (ClientProtocolException e) {
			notification = new Notification("Erro ao remover mesa",
					"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
			notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
		} catch (IOException e) {
			notification = new Notification("Erro ao remover mesa",
					"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
			notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
		}
		return notification;
	}

	public Notification createEntity(ConsumptionIdentifier consumptionIdentifier) {
		String requestBody = gson.toJson(consumptionIdentifier);

		HttpPost httpPost = generateBasicHttpPost(PATH);

		Notification notification;
		try {
			httpPost.setEntity(new StringEntity(requestBody));
			HttpResponse response = getHttpClient().execute(httpPost);

			if (HttpStatus.SC_CREATED == response.getStatusLine()
					.getStatusCode()) {
				notification = new Notification("Sucesso",
						"Mesa criada com sucesso",
						Notification.TYPE_HUMANIZED_MESSAGE);
				notification.setDelayMsec(SUCCESS_NOTIFICATION_DISMISS_TIME);
			} else {
				notification = new Notification("Erro ao criar mesa",
						"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			}

		} catch (Exception e) {
			notification = new Notification("Erro ao criar mesa",
					"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}

		return notification;
	}

}
