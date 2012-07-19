package net.openrally.manager;

import java.io.IOException;

import net.openrally.SessionStorage;
import net.openrally.entity.NewCompany;
import net.openrally.util.StringUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import com.google.gson.Gson;

public class CompanyManager extends BaseManager {
	
	private static final long serialVersionUID = 7017437307034514645L;

	private static final String PATH = "company";
	
	private Gson gson;
	
	public CompanyManager(SessionStorage sessionStorage){
		super(sessionStorage);
		gson = new Gson();
	}
	
	public NewCompany createNewCompany() throws ClientProtocolException, IOException{

		HttpPost httpPost = generateBasicHttpPost(PATH);

		HttpResponse response = getHttpClient().execute(httpPost);

		String responseBody = StringUtilities.httpResponseAsString(response);

		NewCompany entityResponseBody = gson.fromJson(responseBody,
				NewCompany.class);

		return entityResponseBody;
	}
}
