package net.openrally.content;

import java.io.IOException;

import net.openrally.component.ReCaptcha;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
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
	        
	        CompanyResponseBody newCompany = CompanyManager.createNewCompany();
	        
	        progressIndicator.setEnabled(false);
			
		}
		else{
			confirmButton.setEnabled(true);
		}
	}

}
