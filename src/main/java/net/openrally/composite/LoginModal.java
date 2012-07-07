package net.openrally.composite;

import net.openrally.MainApplication;
import net.openrally.SessionStorage;
import net.openrally.manager.LoginManager;

import org.apache.commons.lang.StringUtils;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class LoginModal extends Window implements Button.ClickListener{

	private static final long serialVersionUID = -2547731685050172729L;
	
	private static final ThemeResource loginIcon = new ThemeResource(
			"images/processlogin.png");
	
	private TextField companyIdTextField;
	
	private TextField usernameTextField;
	
	private PasswordField passwordField;
	
	private SessionStorage sessionStorage;
	
	public LoginModal(SessionStorage sessionStorage){
		super("Login");
		setModal(true);
		setWidth("310px");
		setHeight("190px");
		setResizable(false);
	        
	    GridLayout layout = new GridLayout(2, 5);
	    addComponent(layout);
		
		Label loginText = new Label("Bem vindo. Efetue o login abaixo:");
		layout.addComponent(loginText, 0, 0, 1, 0);
		layout.setComponentAlignment(loginText, Alignment.MIDDLE_CENTER);
		
		Label companyIdLabel = new Label("Company Id");
		companyIdLabel.setWidth("100px");
		layout.addComponent(companyIdLabel, 0, 1);
		layout.setComponentAlignment(companyIdLabel, Alignment.TOP_RIGHT);
		
		companyIdTextField = new TextField();
		companyIdTextField.setValue("1");
		layout.addComponent(companyIdTextField, 1, 1);
		layout.setComponentAlignment(companyIdTextField, Alignment.TOP_LEFT);
		
		Label usernameLabel = new Label("Username");
		usernameLabel.setWidth("100px");
		layout.addComponent(usernameLabel, 0, 2);
		layout.setComponentAlignment(usernameLabel, Alignment.TOP_RIGHT);
		
		usernameTextField = new TextField();
		usernameTextField.setValue("administrator");
		layout.addComponent(usernameTextField, 1, 2);
		layout.setComponentAlignment(usernameTextField, Alignment.TOP_LEFT);
		
		Label passwordLabel = new Label("Senha");
		passwordLabel.setWidth("100px");
		layout.addComponent(passwordLabel, 0, 3);
		layout.setComponentAlignment(passwordLabel, Alignment.TOP_RIGHT);
		
		passwordField = new PasswordField();
		passwordField.setValue("DZVMpKpjtY");
		layout.addComponent(passwordField, 1, 3);
		layout.setComponentAlignment(passwordField, Alignment.TOP_LEFT);
		
		Button loginButton = new Button("Login");
		loginButton.addListener(this);
		loginButton.setStyleName("textCenter");
		loginButton.setIcon(loginIcon);
		layout.addComponent(loginButton, 0,4,1,4);
		
		this.sessionStorage = sessionStorage;
	}

	public void buttonClick(ClickEvent event) {
		companyIdTextField.setComponentError(null);
		usernameTextField.setComponentError(null);
		passwordField.setComponentError(null);
		
		Boolean valid = true;
		
		String companyIdString = (String) companyIdTextField.getValue();
		
		Long companyId = null;
		
		if(StringUtils.isBlank(companyIdString)){
			companyIdTextField.setComponentError(new UserError(
					"Por favor, informe o company id"));
			valid = false;
		}
		else{
			try{
				companyId = Long.parseLong(companyIdString);
			} catch(NumberFormatException e){
				companyIdTextField.setComponentError(new UserError(
						"Company id em formato incorreto"));
				valid = false;
			}
		}
		
		String usernameString = (String) usernameTextField.getValue();
		
		if(StringUtils.isBlank(usernameString)){
			usernameTextField.setComponentError(new UserError(
					"Por favor, informe o username"));
			valid = false;
		}
		
		String passwordString = (String) passwordField.getValue();
		
		if(StringUtils.isBlank(passwordString)){
			passwordField.setComponentError(new UserError(
					"Por favor, informe a senha"));
			valid = false;
		}
		
		if(valid){
			LoginManager loginManager = new LoginManager(sessionStorage);
			
			boolean loginResult = loginManager.checkLogin(companyId, usernameString, passwordString);
			
			if(loginResult){
				MainApplication application = (MainApplication) getApplication();
				application.adjustAfterLogin();
				
				getParent().removeWindow(this);
				
			}
			else{
				getWindow().showNotification(
                        "Acesso negado",
                        "Verifique suas credenciais e tente novamente",
                        Notification.TYPE_ERROR_MESSAGE);
			}
		}
		
	}
}
