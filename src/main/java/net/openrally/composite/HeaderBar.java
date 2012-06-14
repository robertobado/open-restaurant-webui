package net.openrally.composite;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class HeaderBar extends HorizontalLayout {

	private static final long serialVersionUID = 2825761939720453469L;
	
	private HeaderTopBar headerTopBar;
	private Panel leftPanel;
	private Panel rightPanel;
	
	public HeaderBar(){
		setStyleName("headerBar");
		setWidth("100%");
		setHeight("45px");
		
		leftPanel = new Panel();
		leftPanel.setWidth("30px");
		leftPanel.setStyleName("transparentPanel");
		addComponent(leftPanel);
		setExpandRatio(leftPanel, 0);
		
		headerTopBar = new HeaderTopBar();
        addComponent(headerTopBar);
        setExpandRatio(leftPanel, 1);
        
        rightPanel = new Panel();
        rightPanel.setStyleName("transparentPanel");
        rightPanel.setWidth("30px");
        addComponent(rightPanel);
        setExpandRatio(rightPanel, 0);
	}

}
