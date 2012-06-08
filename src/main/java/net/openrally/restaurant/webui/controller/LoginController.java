package net.openrally.restaurant.webui.controller;

import java.io.IOException;

import net.openrally.restaurant.webui.manager.LoginManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class LoginController extends SelectorComposer<Div>{

	private static final long serialVersionUID = -6845590579672491650L;
	
	@Wire
	private Textbox contentTextBoxCompanyId;
	
	@Wire
	private Textbox contentTextBoxUsername;
	
	@Wire
	private Textbox contentTextBoxPassword;
	
	@Wire
	private Button contentButtonLoginGo;
	
	@Wire
	private Div contentDivErrorAlert;
	
	@Wire
	private Label contentLabelErrorMessage;
	
	private String redirectUrl;
	
	public void doAfterCompose(Div component) throws Exception {
		super.doAfterCompose(component);
		
		redirectUrl = Executions.getCurrent().getParameter("redirectUrl");
		
		if(StringUtils.isBlank(redirectUrl)){
			redirectUrl = "dashboard.zul";
		}
		
	}
	
	@Listen("onClick=#contentButtonLoginGo")
	public void contentButtonLoginGo() throws ClientProtocolException, IOException {
		validate();
		
		contentButtonLoginGo.setDisabled(true);
		
		Long companyId = Long.valueOf(contentTextBoxCompanyId.getValue());
		
		Boolean loginResult = LoginManager.performLogin(companyId, contentTextBoxUsername.getValue(), contentTextBoxPassword.getValue());
		
		contentButtonLoginGo.setDisabled(false);
		
		if(loginResult){
			contentDivErrorAlert.setVisible(false);			
			Executions.sendRedirect(redirectUrl);
		}
		else{
			contentDivErrorAlert.setVisible(true);
			contentLabelErrorMessage.setValue("Login failed! Please check your credentials and try again");
		}
		
	}
	
	public void validate(){
		if(StringUtils.isBlank(contentTextBoxCompanyId.getValue())){
			throw new WrongValueException(contentTextBoxCompanyId, "Company id cannot be empty");
		}
		
		try{
			Long.valueOf(contentTextBoxCompanyId.getValue());
		}catch(NumberFormatException e){
			throw new WrongValueException(contentTextBoxCompanyId, "Invalid company id format");
		}
		
		if(StringUtils.isBlank(contentTextBoxUsername.getValue())){
			throw new WrongValueException(contentTextBoxUsername, "Username cannot be empty");
		}
		
		if(StringUtils.isBlank(contentTextBoxPassword.getValue())){
			throw new WrongValueException(contentTextBoxPassword, "Password cannot be empty");
		}
	}

}
