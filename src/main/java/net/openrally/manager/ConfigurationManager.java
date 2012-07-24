package net.openrally.manager;

import java.io.IOException;
import java.lang.reflect.Type;

import net.openrally.SessionStorage;
import net.openrally.content.Configuration;
import net.openrally.entity.Entity;
import net.openrally.util.StringUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import com.vaadin.ui.Window.Notification;

public class ConfigurationManager extends BaseEntityManager{
	
	public static final String PATH = "configuration";

	private static final long serialVersionUID = 452650042167377525L;
	
	public ConfigurationManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}

	public Configuration getEntity(){
		try {
			HttpGet httpGet = generateBasicHttpGet(getPath());
			HttpResponse response = getHttpClient().execute(httpGet);
			String responseBody = StringUtilities
					.httpResponseAsString(response);
			
			Configuration entity = gson.fromJson(responseBody, Configuration.class);

			return entity;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Notification updateEntity(Configuration entity){
		String requestBody = gson.toJson(entity);
		
		HttpPut httpPut = generateBasicHttpPut(getPath());

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

	@Override
	protected Class<? extends Entity> getEntityClass() {
		return Configuration.class;
	}

	@Override
	protected String getPath() {
		return PATH;
	}

	@Override
	protected Type getEntityListClass() {
		return null;
	}

}
