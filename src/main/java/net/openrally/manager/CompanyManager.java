package net.openrally.manager;

public class CompanyManager {
	
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
