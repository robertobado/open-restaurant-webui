package net.openrally.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.Response.Status;

import net.openrally.SessionStorage;
import net.openrally.entity.User;
import net.openrally.requestbody.LoginRequestBody;
import net.openrally.responsebody.LoginResponseBody;
import net.openrally.util.StringUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class LoginManager extends BaseManager {

	private static final long serialVersionUID = 3941308840730651604L;
	private static final String PATH = "login";

	public LoginManager(SessionStorage sessionStorage) {
		super(sessionStorage);
	}

	public boolean checkLogin(Long companyId, String username,
			String password) {

		HttpPost httpPost = generateBasicHttpPost(PATH);

		LoginRequestBody loginRequestBody = new LoginRequestBody();

		loginRequestBody.setCompanyId(companyId);
		loginRequestBody.setLogin(username);
		loginRequestBody.setPassword(password);

		String requestBody = gson.toJson(loginRequestBody);

		try {
			httpPost.setEntity(new StringEntity(requestBody));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpResponse response;
		try {
			response = getHttpClient().execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (response.getStatusLine().getStatusCode() == Status.OK
				.getStatusCode()) {

			String responseBody;
			try {
				responseBody = StringUtilities
						.httpResponseAsString(response);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			LoginResponseBody loginResponseBody = gson.fromJson(responseBody,
					LoginResponseBody.class);

			String authorizationToken = loginResponseBody.getToken();

			User user = getSessionUser();
			user.setAuthorizationToken(authorizationToken);
			user.setLoggedIn(true);

			return true;
		} else {
			return false;
		}

	}
}
