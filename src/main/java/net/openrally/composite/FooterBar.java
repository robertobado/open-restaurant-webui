package net.openrally.composite;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class FooterBar extends HorizontalLayout {

	private static final long serialVersionUID = 4291992502733507167L;
	
	private Label footerLabel;
	
	public FooterBar(){
		footerLabel = new Label("Open Restaurant v 0.1.0");
        addComponent(footerLabel);
	}

}
