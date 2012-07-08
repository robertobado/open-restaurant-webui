package net.openrally;

import java.util.HashMap;
import java.util.Map;

public class SessionStorage {
	
	public static final String USER_IP_ADDRESS = "userIpAddress";

	public static final String USER = "user";

	public static final String AUTHORIZATION_TOKEN = "authorizationToken";
	
	private Map<String, Object> sessionMap;
	
	public SessionStorage(){
		sessionMap = new HashMap<String, Object>();
	}
	
	public Object getSessionValue(String sessionKey){
	
		return sessionMap.get(sessionKey);
	}
	
	public void setSessionValue(String sessionKey, Object sessionValue){
		sessionMap.put(sessionKey, sessionValue);
	}
	
	public void purge(){
		sessionMap = new HashMap<String, Object>();
	}
}
