package net.openrally.restaurant.response.body;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class UserResponseBody {

	private Long userId;
	private String login;
	private List<Long> roles;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof UserResponseBody))
			return false;
		UserResponseBody otherRoleResponseBody = (UserResponseBody) other;
		if (null == otherRoleResponseBody.getUserId() || !otherRoleResponseBody.getUserId().equals(this.getUserId())) {
			return false;
		}
		if (!StringUtils.equals(otherRoleResponseBody.getLogin(),
				this.getLogin())) {
			return false;
		}

		if (null == roles && null != otherRoleResponseBody.roles) {
			return false;
		}

		if (null == otherRoleResponseBody.roles && null != roles) {
			return false;
		}

		if (!roles.containsAll(otherRoleResponseBody.roles)) {
			return false;
		}
		if (!otherRoleResponseBody.roles.containsAll(roles)) {
			return false;
		}
		return true;
	}
}
