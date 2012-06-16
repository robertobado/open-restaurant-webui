package net.openrally.content;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class SignUpContent extends HorizontalLayout {

	private static final long serialVersionUID = -4469208579760023380L;
	
	public SignUpContent(){
		Label text = new Label("Sign up now to start managing your restaurant online!");
		addComponent(text);
		setComponentAlignment(text, Alignment.TOP_CENTER);
		
	}

}
