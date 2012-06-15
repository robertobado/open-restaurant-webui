package net.openrally.composite;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class HeaderTopBar extends HorizontalLayout {

	private static final long serialVersionUID = 2504711004567530291L;
	
	private Label headerLabel;
	
	public HeaderTopBar(){
		
		setStyleName("headerTopBar");
		setWidth("80%");
		setHeight("25px");
		
        headerLabel = new Label("Open Restaurant");
        headerLabel.setStyleName("headerBarLabel");
        addComponent(headerLabel);
		
	}

}
