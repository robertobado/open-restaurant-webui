package net.openrally.manager;

import java.awt.PageAttributes.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import com.google.gson.Gson;

public class BaseManager {
	public static final String CORE_BASE_ADDRESS_PROPERTY_NAME = "restaurant.core.address";
	public static final String SESSION_USER_KEY = "session.user";
	public static final String SLASH = "/";

	private static Properties properties;
	protected static Gson gson = new Gson();

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

	protected static HttpPost generateBasicHttpPost(String path) {
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

	public static User getSessionUser() {
		Session zkSession = Sessions.getCurrent();
		synchronized (zkSession) {
			User user = (User) zkSession.getAttribute(SESSION_USER_KEY);
			if (user == null) {
				user = new User();
				zkSession.setAttribute(SESSION_USER_KEY, user);
			}
			return user;
		}
	}

	protected static String getAuthorizationToken() {
		User user = getSessionUser();
		if (null == user) {
			return null;
		}

		return user.getAuthorizationToken();

	}

	protected static HttpClient getHttpClient() {
		return new DefaultHttpClient();
	}
}
