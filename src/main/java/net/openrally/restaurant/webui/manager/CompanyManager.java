package net.openrally.restaurant.webui.manager;

import java.io.IOException;

import net.openrally.restaurant.core.util.StringUtilities;
import net.openrally.restaurant.response.body.CompanyResponseBody;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

public class CompanyManager extends BaseManager{
	
	private static final String PATH = "/company";
	
	public static CompanyResponseBody createNewCompany() throws ClientProtocolException, IOException{
		
		HttpPost httpPost = generateBasicHttpPost(PATH);

		HttpResponse response = getHttpClient().execute(httpPost);
		
		String responseBody = StringUtilities.httpResponseAsString(response);

		CompanyResponseBody entityResponseBody = gson.fromJson(responseBody,
				CompanyResponseBody.class);
		
		return entityResponseBody;
	}
}
