package net.openrally.composite;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;

public class HeaderBar extends HorizontalLayout{

	private static final long serialVersionUID = 2825761939720453469L;
	
	private Label mainLabel;
	
    private TabSheet tabSheet;
	
	public HeaderBar(HorizontalLayout mainArea){	
		setStyleName("headerBar");
		setWidth("100%");
		setHeight("40px");
        
        mainLabel = new Label("Open Restaurant");
        mainLabel.setStyleName("headerBarLabel");
        addComponent(mainLabel);
        setComponentAlignment(mainLabel, Alignment.TOP_LEFT);
        
        tabSheet = new HeaderTabBar(mainArea);
        addComponent(tabSheet);
        setComponentAlignment(tabSheet, Alignment.TOP_RIGHT);
	}
}
