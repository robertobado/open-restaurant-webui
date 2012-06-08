package net.openrally.restaurant.webui.controller;

import net.openrally.restaurant.webui.entity.User;
import net.openrally.restaurant.webui.manager.BaseManager;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public class HeaderController extends SelectorComposer<Div> {

	private static final long serialVersionUID = -1672648880330369612L;
	
	@Wire
	private Vbox headerVboxAdministration;
	
	@Wire
	private Label headerLabelLogin;
	
	@Wire
	private Label headerLabelSignUp;
	
	@Wire
	private Label headerLabelLogout;
	
	@Override
	public void doAfterCompose(Div comp) throws Exception {
	    super.doAfterCompose(comp);
	    
	    User sessionUser = BaseManager.getSessionUser();
	    
	    if(sessionUser.isLoggedIn()){
	    	headerLabelLogin.setVisible(false);
	    	headerLabelSignUp.setVisible(false);
	    }
	    else{
	    	headerLabelLogout.setVisible(false);
	    	headerVboxAdministration.setVisible(false);
	    }
	}

	@Listen("onClick=#headerLabelSignUp")
	public void headerLabelSignUp() {
		Executions.sendRedirect("signUp.zul");
	}
	
	@Listen("onClick=#headerLabelLogin")
	public void headerLabelLogin() {
		Executions.sendRedirect("login.zul");
	}
	
	@Listen("onClick=#headerLabelLogout")
	public void headerLabelLogout() {		
		Sessions.getCurrent().invalidate();
		Executions.sendRedirect("index.zul");
	}
	
	@Listen("onClick=#headerLabelProducts")
	public void headerLabelProducts() {	
		Executions.sendRedirect("products.zul");
	}
	
}
