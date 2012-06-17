package net.openrally.component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.openrally.MainApplication;
import net.openrally.SessionStorage;
import net.openrally.util.StringUtilities;

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

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ReCaptcha extends VerticalLayout {

	private static final String JPG_MIME_TYPE = "image/jpeg";

	private static final String CHALLENGE_GENERATOR_URL = "http://api.recaptcha.net/noscript?k=";

	private static final String VERIFY_URL = "http://www.google.com/recaptcha/api/verify";

	private static final long serialVersionUID = -1687423696268482964L;

	private static final String RECAPTCHA_PRIVATE_KEY = "6LdsPdISAAAAAPkDExKnV8WfPIjIPPX6Dyp1gNdP";
	private static final String RECAPTCHA_PUBLIC_KEY = "6LdsPdISAAAAADi6njqenU34pDjbdQMBK2ETzVd3";
	private static final String CHALLENGE_PREFIX = "<input type=\"hidden\" name=\"recaptcha_challenge_field\" id=\"recaptcha_challenge_field\" value=\"";
	private static final String IMAGE_PREFIX = "http://api.recaptcha.net/image?c=";
	private static final String SECURE_IMAGE_PREFIX = "https://api.recaptcha.net/image?c=";

	private String challenge;

	private Label instruction;

	private ExternalResource challengeImage;

	private Embedded embeddedRecaptcha;

	private TextField userInput;

	public ReCaptcha() throws IOException {

		retrieveCaptcha();

		buildLayout();

	}

	private void retrieveCaptcha() throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(CHALLENGE_GENERATOR_URL
				+ RECAPTCHA_PUBLIC_KEY);

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response = httpClient.execute(httpGet);

		String responseBody = StringUtilities.httpResponseAsString(response);

		challenge = retriveChallenge(responseBody);
	}

	private void buildLayout() {

		instruction = new Label("Please type the following words:");
		addComponent(instruction);
		setComponentAlignment(instruction, Alignment.TOP_CENTER);

		challengeImage = new ExternalResource(getImagePath());
		challengeImage.setMIMEType(JPG_MIME_TYPE);

		embeddedRecaptcha = new Embedded("ReCaptcha", challengeImage);
		addComponent(embeddedRecaptcha);
		setComponentAlignment(embeddedRecaptcha, Alignment.TOP_CENTER);

		userInput = new TextField();
		userInput.setImmediate(true);
		addComponent(userInput);
		setComponentAlignment(userInput, Alignment.TOP_CENTER);
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

	public boolean verifyResponse() {

		String response = userInput.getValue().toString();
		
		SessionStorage sessionStorage = ((MainApplication) getApplication())
		.getSessionStorage();

		String ipAddress = (String) sessionStorage.getSessionValue(SessionStorage.USER_IP_ADDRESS);

		if (StringUtils.isBlank(response)) {
			userInput.setComponentError(new UserError(
					"Please type in the words below."));
			return false;
		}

		HttpPost httpPost = new HttpPost(VERIFY_URL);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("privatekey",
				RECAPTCHA_PRIVATE_KEY));
		nameValuePairs.add(new BasicNameValuePair("remoteip", ipAddress));
		nameValuePairs.add(new BasicNameValuePair("challenge", challenge));
		nameValuePairs.add(new BasicNameValuePair("response", response));

		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset
				.defaultCharset()));

		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpPost);

			String responseBody = StringUtilities
					.httpResponseAsString(httpResponse);

			boolean result = StringUtils
					.equals("true\nsuccess\n", responseBody);

			if (!result) {
				refresh();
				userInput.setComponentError(new UserError(
						"Words don't match. Please try again"));
			}

			return result;
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	public void refresh() throws ClientProtocolException, IOException {
		removeAllComponents();

		retrieveCaptcha();

		buildLayout();

	}

}
