package net.openrally.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import net.openrally.SessionStorage;
import net.openrally.entity.User;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import com.google.gson.Gson;
import com.sun.jersey.spi.container.ContainerRequest;
import com.vaadin.ui.CustomComponent;

public class BaseManager extends CustomComponent{
	
	private static final long serialVersionUID = -2263384602853522836L;
	
	public static final String CORE_BASE_ADDRESS_PROPERTY_NAME = "restaurant.core.address";
	public static final String SESSION_USER_KEY = "session.user";
	public static final String SLASH = "/";

	private static Properties properties;
	protected static Gson gson = new Gson();
	private SessionStorage sessionStorage;

	public BaseManager(SessionStorage sessionStorage) {
		this.sessionStorage = sessionStorage;
	}

	public static Properties getProperties() {

		if (null == properties) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream("default.properties"));
			} catch (IOException e) {
			}
		}

		return properties;
	}

	public static String getProperty(String property) {
		return getProperties().getProperty(property);
	}

	public static String getCoreBaseAddress() {
		// return getProperty(CORE_BASE_ADDRESS_PROPERTY_NAME);
		return "http://localhost:8181";
	}

	protected HttpPost generateBasicHttpPost(String path) {
		HttpPost httpPost = new HttpPost(getCoreBaseAddress() + SLASH + path);

		Header acceptHeader = new BasicHeader(HttpHeaders.ACCEPT,
				MediaType.APPLICATION_JSON);
		httpPost.addHeader(acceptHeader);

		Header contentTypeHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON);
		httpPost.addHeader(contentTypeHeader);

		if (!StringUtils.isBlank(getAuthorizationToken())) {
			Header loginTokenHeader = new BasicHeader(
					ContainerRequest.AUTHORIZATION, getAuthorizationToken());
			httpPost.addHeader(loginTokenHeader);
		}

		return httpPost;
	}
	
	protected HttpGet generateBasicHttpGet(String path) {
		HttpGet httpGet = new HttpGet(getCoreBaseAddress() + SLASH
				+ path);

		Header acceptHeader = new BasicHeader(HttpHeaders.ACCEPT,
				MediaType.APPLICATION_JSON);
		httpGet.addHeader(acceptHeader);

		Header contentTypeHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON);
		httpGet.addHeader(contentTypeHeader);

		if (!StringUtils.isBlank(getAuthorizationToken())) {
			Header loginTokenHeader = new BasicHeader(
					ContainerRequest.AUTHORIZATION, getAuthorizationToken());
			httpGet.addHeader(loginTokenHeader);
		}

		return httpGet;
	}
	
	public SessionStorage getSessionStorage(){		
		return sessionStorage;
	}

	public User getSessionUser() {
		User sessionUser = (User) getSessionStorage().getSessionValue(SessionStorage.USER);
		if(null == sessionUser){
			sessionUser = new User();
			getSessionStorage().setSessionValue(SessionStorage.USER, sessionUser);
		}
		return sessionUser;
	}

	protected String getAuthorizationToken() {
		
		User sessionUser = getSessionUser();
		
		return sessionUser.getAuthorizationToken();
	}

	protected static HttpClient getHttpClient() {
		return new DefaultHttpClient();
	}
}
