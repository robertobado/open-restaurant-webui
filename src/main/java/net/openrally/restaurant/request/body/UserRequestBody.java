package net.openrally.restaurant.request.body;

import java.util.List;

public class UserRequestBody {
	
	private String login;
	
	private String password;
	
	private List<Long> roles;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Long> getRoles() {
		return roles;
	}
	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}
}
