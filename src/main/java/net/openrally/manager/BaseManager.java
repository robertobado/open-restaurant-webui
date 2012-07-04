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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
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
		HttpPost request = new HttpPost(getCoreBaseAddress() + SLASH + path);

		addAcceptTypeAndContentTypeJson(request);

		addAuthorizationHeader(request);

		return request;
	}
	
	protected HttpGet generateBasicHttpGet(String path) {
		HttpGet request = new HttpGet(getCoreBaseAddress() + SLASH
				+ path);

		addAcceptTypeAndContentTypeJson(request);

		addAuthorizationHeader(request);

		return request;
	}
	
	protected HttpDelete generateBasicHttpDelete(String path) {
		HttpDelete request = new HttpDelete(getCoreBaseAddress() + SLASH
				+ path);

		addAcceptTypeAndContentTypeJson(request);

		addAuthorizationHeader(request);

		return request;
	}
	

	protected HttpPut generateBasicHttpPut(String path) {
		HttpPut request = new HttpPut(getCoreBaseAddress() + SLASH
				+ path);

		addAcceptTypeAndContentTypeJson(request);

		addAuthorizationHeader(request);

		return request;
	}
	
	private void addAcceptTypeJson(HttpRequestBase request){
		Header acceptHeader = new BasicHeader(HttpHeaders.ACCEPT,
				MediaType.APPLICATION_JSON);
		request.addHeader(acceptHeader);
	}
	
	private void addContentTypeJson(HttpRequestBase request){
		Header contentTypeHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON);
		request.addHeader(contentTypeHeader);
	}
	
	private void addAcceptTypeAndContentTypeJson(HttpRequestBase request){
		addAcceptTypeJson(request);
		addContentTypeJson(request);
	}
	
	private void addAuthorizationHeader(HttpRequestBase request){
		if (!StringUtils.isBlank(getAuthorizationToken())) {
			Header loginTokenHeader = new BasicHeader(
					ContainerRequest.AUTHORIZATION, getAuthorizationToken());
			request.addHeader(loginTokenHeader);
		}
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
