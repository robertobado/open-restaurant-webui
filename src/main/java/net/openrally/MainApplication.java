package net.openrally;

import com.vaadin.Application;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MainApplication extends Application
{
	private static final long serialVersionUID = 701923553044752097L;
	
	private Window window;
    private VerticalLayout layout;
    private HorizontalLayout header;
    private Panel mainArea;
    private Panel footer;

    @Override
    public void init()
    {
        window = new Window("Open Restaurant WebUI");
        setMainWindow(window);
        
        layout = new VerticalLayout();
        layout.setSizeFull();
        
        header = new HorizontalLayout();
        header.setStyleName("headerBar");
        header.setWidth("100%");
        layout.addComponent(header);
        layout.setExpandRatio(header, 0);
        
        mainArea = new Panel();
        mainArea.setSizeFull();
        layout.addComponent(mainArea);
        layout.setExpandRatio(mainArea, 1);
        
        footer = new Panel();
        layout.addComponent(footer);
        layout.setExpandRatio(footer, 0);
        
        Label headerLabel = new Label("Open Restaurantssssssssssssssssssssssssssssssssssss");
        header.addComponent(headerLabel);
        
        Label mainAreaLabel = new Label("This is the main area");
        mainArea.addComponent(mainAreaLabel);
        
        Label footerLabel = new Label("This is the footer");
        footer.addComponent(footerLabel);

        getMainWindow().setContent(layout);
		setTheme("restaurantwebuitheme");
    }
    
}
