package net.openrally;

import net.openrally.composite.FooterBar;
import net.openrally.composite.HeaderBar;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MainApplication extends Application
{
	private static final long serialVersionUID = 701923553044752097L;
	
	private Window window;
    private VerticalLayout layout;
    private HeaderBar headerBar;
    private Panel mainArea;
    private FooterBar footerBar;

    @Override
    public void init()
    {
        window = new Window("Open Restaurant WebUI");
        setMainWindow(window);
        
        layout = new VerticalLayout();
        layout.setSizeFull();
        
        headerBar = new HeaderBar();
        layout.addComponent(headerBar);
        layout.setExpandRatio(headerBar, 0);
        
        mainArea = new Panel();
        mainArea.setSizeFull();
        layout.addComponent(mainArea);
        layout.setExpandRatio(mainArea, 1);
        
        footerBar = new FooterBar();
        layout.addComponent(footerBar);
        layout.setExpandRatio(footerBar, 0);
        
        Label mainAreaLabel = new Label("This is the main area");
        mainArea.addComponent(mainAreaLabel);

        getMainWindow().setContent(layout);
		setTheme("restaurantwebuitheme");
    }
    
}
