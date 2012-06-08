package net.openrally.restaurant.webui.security;

import java.util.Map;

import net.openrally.restaurant.webui.entity.User;
import net.openrally.restaurant.webui.manager.BaseManager;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AccessValidator implements Initiator {

	@Override
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		User sessionUser = BaseManager.getSessionUser();

		if (!sessionUser.isLoggedIn()) {
			Executions.sendRedirect("login.zul?" + "redirectUrl" + "="
					+ args.get("redirectUrl"));
		}
	}

}
