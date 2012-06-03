package net.openrally.restaurant.webui.manager;

import java.io.IOException;

import javax.ws.rs.core.Response.Status;

import net.openrally.restaurant.core.util.StringUtilities;
import net.openrally.restaurant.request.body.LoginRequestBody;
import net.openrally.restaurant.response.body.LoginResponseBody;
import net.openrally.restaurant.webui.entity.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class LoginManager extends BaseManager {

	private static final String PATH = "/login";

	public static boolean performLogin(Long companyId, String username, String password)
			throws ClientProtocolException, IOException {

		HttpPost httpPost = generateBasicHttpPost(PATH);

		LoginRequestBody loginRequestBody = new LoginRequestBody();

		loginRequestBody.setCompanyId(companyId);
		loginRequestBody.setLogin(username);
		loginRequestBody.setPassword(password);

		String requestBody = gson.toJson(loginRequestBody);

		httpPost.setEntity(new StringEntity(requestBody));

		HttpResponse response = getHttpClient().execute(httpPost);

		if (response.getStatusLine().getStatusCode() == Status.OK
				.getStatusCode()) {
			
			String responseBody = StringUtilities.httpResponseAsString(response);
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
