package net.openrally.content;

import java.io.IOException;

import net.openrally.MainApplication;
import net.openrally.SessionStorage;
import net.openrally.component.ReCaptcha;
import net.openrally.entity.NewCompany;
import net.openrally.manager.CompanyManager;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class SignUpContent extends VerticalLayout implements
Button.ClickListener{

	private static final long serialVersionUID = -4469208579760023380L;
	
	private ReCaptcha reCaptcha;
	
	private Button confirmButton;
	
	private ProgressIndicator progressIndicator;
	
	private Label createAccountText;
	
	public SignUpContent(){
		Label text = new Label("Sign up now to start managing your restaurant online!");
		text.setWidth("100%");
		addComponent(text);
		setComponentAlignment(text, Alignment.TOP_CENTER);
		
		try {
			reCaptcha = new ReCaptcha();
			reCaptcha.setWidth("100%");
			addComponent(reCaptcha);
			setComponentAlignment(reCaptcha, Alignment.TOP_CENTER);
		} catch (IOException e) {
			
		}
		
		confirmButton = new Button("Go");
		confirmButton.addListener(this);
		addComponent(confirmButton);
		setComponentAlignment(confirmButton, Alignment.TOP_CENTER);
		
	}

	public void buttonClick(ClickEvent event) {
		confirmButton.setEnabled(false);
		if(reCaptcha.verifyResponse()){
			removeAllComponents();
			createAccountText = new Label("Creating new account. Please wait...");
			addComponent(createAccountText);
			setComponentAlignment(createAccountText, Alignment.TOP_CENTER);
			
			progressIndicator = new ProgressIndicator();
			progressIndicator.setIndeterminate(true);
			progressIndicator.setPollingInterval(500);
			progressIndicator.setEnabled(true);
			progressIndicator.setVisible(true);
	        addComponent(progressIndicator);
	        setComponentAlignment(progressIndicator, Alignment.TOP_CENTER);
	        
	        SessionStorage sessionStorage = ((MainApplication) getApplication())
					.getSessionStorage();
	        
	        CompanyManager companyManager = new CompanyManager(sessionStorage);
	        
	        try {
				NewCompany newCompany = companyManager.createNewCompany();
				
		        removeAllComponents();
		        
		        Label success = new Label("Account created successfully");
		        addComponent(success);
		        setComponentAlignment(success, Alignment.TOP_CENTER);
		        
		        Label loginInstructions = new Label("Please log in with the following credentials");
		        addComponent(loginInstructions);
		        setComponentAlignment(loginInstructions, Alignment.TOP_CENTER);
		        
		        IndexedContainer accountCredentialsContainer = new IndexedContainer();
		        accountCredentialsContainer.addContainerProperty("Parameter",
		                String.class, Table.COLUMN_HEADER_MODE_HIDDEN);
		        accountCredentialsContainer.addContainerProperty("Value",
		                String.class, Table.COLUMN_HEADER_MODE_HIDDEN);
		        
		        Item companyIdItem = accountCredentialsContainer.addItem(0);
		        companyIdItem.getItemProperty("Parameter").setValue("Company Id");
                companyIdItem.getItemProperty("Value").setValue(newCompany.getCompanyId().toString());
		        
                Item usernameItem = accountCredentialsContainer.addItem(1);
		        usernameItem.getItemProperty("Parameter").setValue("Username");
                usernameItem.getItemProperty("Value").setValue(newCompany.getUsername());
                
                Item passwordItem = accountCredentialsContainer.addItem(2);
		        passwordItem.getItemProperty("Parameter").setValue("Password");
                passwordItem.getItemProperty("Value").setValue(newCompany.getPassword());
		        
		        Table credentialsTable = new Table("Account credentials", accountCredentialsContainer);
		        credentialsTable.setWidth("230px");
		        credentialsTable.setHeight("100px");
		        addComponent(credentialsTable);
		        setComponentAlignment(credentialsTable, Alignment.TOP_CENTER);
		        
			} catch (IOException e) {
				
			}
	        
	        progressIndicator.setEnabled(false);
			
		}
		else{
			confirmButton.setEnabled(true);
		}
	}

}
