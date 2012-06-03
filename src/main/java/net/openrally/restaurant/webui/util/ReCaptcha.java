package net.openrally.restaurant.webui.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.openrally.restaurant.core.util.StringUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ReCaptcha {

	private static final String RECAPTCHA_PRIVATE_KEY = "6LdsPdISAAAAAPkDExKnV8WfPIjIPPX6Dyp1gNdP";
	private static final String RECAPTCHA_PUBLIC_KEY = "6LdsPdISAAAAADi6njqenU34pDjbdQMBK2ETzVd3";
	private static final String CHALLENGE_PREFIX = "<input type=\"hidden\" name=\"recaptcha_challenge_field\" id=\"recaptcha_challenge_field\" value=\"";
	private static final String IMAGE_PREFIX = "http://api.recaptcha.net/image?c=";
	private static final String SECURE_IMAGE_PREFIX = "https://api.recaptcha.net/image?c=";

	private String challenge;

	public ReCaptcha() throws ClientProtocolException, IOException {

		HttpGet httpGet = new HttpGet(
				"http://api.recaptcha.net/noscript?k=" + RECAPTCHA_PUBLIC_KEY);

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response = httpClient.execute(httpGet);

		String responseBody = StringUtilities.httpResponseAsString(response);

		challenge = retriveChallenge(responseBody);

	}

	private String retriveChallenge(String responseBody) {
		int startPosition = responseBody.indexOf(CHALLENGE_PREFIX)
				+ CHALLENGE_PREFIX.length();
		int endPosition = responseBody.indexOf("\"", startPosition);

		return responseBody.substring(startPosition, endPosition);
	}

	public String getImagePath() {
		return IMAGE_PREFIX + challenge;
	}

	public String getSecureImagePath() {
		return SECURE_IMAGE_PREFIX + challenge;
	}

	public boolean verifyResponse(String ipAddress, String response)
			throws ClientProtocolException, IOException {
		
		if(StringUtils.isBlank(response)){
			return false;
		}
		
		HttpPost httpPost = new HttpPost(
				"http://www.google.com/recaptcha/api/verify");

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("privatekey",
				RECAPTCHA_PRIVATE_KEY));
		nameValuePairs.add(new BasicNameValuePair("remoteip", ipAddress));
		nameValuePairs.add(new BasicNameValuePair("challenge", challenge));
		nameValuePairs.add(new BasicNameValuePair("response", response));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.defaultCharset()));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse httpResponse = httpClient.execute(httpPost);

		String responseBody = StringUtilities
				.httpResponseAsString(httpResponse);

		return StringUtils.equals("true\nsuccess\n", responseBody);

	}

}
