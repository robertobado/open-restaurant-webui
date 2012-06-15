package net.openrally.composite;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class HeaderBar extends HorizontalLayout {

	private static final long serialVersionUID = 2825761939720453469L;
	
	private HeaderTopBar headerTopBar;
	
	private Label mainLabel;
	
	public HeaderBar(){
		setStyleName("headerBar");
		setWidth("100%");
		setHeight("81px");
		
//		headerTopBar = new HeaderTopBar();
//        addComponent(headerTopBar);
//        setExpandRatio(headerTopBar, 1);
        
        mainLabel = new Label("Open Restaurant");
        
	}

}
