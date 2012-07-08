package net.openrally.composite;

import net.openrally.MainApplication;
import net.openrally.SessionStorage;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;

public class HeaderBar extends HorizontalLayout {

	private static final long serialVersionUID = 2825761939720453469L;

	private static final ThemeResource loginIcon = new ThemeResource(
			"images/login.png");
	
	private static final ThemeResource logoutIcon = new ThemeResource(
			"images/logout.png");

	private Label mainLabel;

	private TabSheet anonymoustTabSheet;
	
	private TabSheet loggedTabSheet;

	private Button loginButton;
	
	private Button logoutButton;

	private Label spacer;
	
	private HorizontalLayout mainArea;

	public HeaderBar(HorizontalLayout mainArea) {
		setStyleName("headerBar");
		setWidth("100%");
		setHeight("40px");

		mainLabel = new Label("Open Restaurant");
		mainLabel.setStyleName("headerBarLabel");
		mainLabel.setWidth("212px");
		addComponent(mainLabel);
		setExpandRatio(mainLabel, 0);

		spacer = new Label("");
		spacer.setWidth("10px");
		addComponent(spacer);
		setExpandRatio(spacer, 1);

		anonymoustTabSheet = new HeaderAnonymousTabBar(mainArea);
		addComponent(anonymoustTabSheet);
		setExpandRatio(anonymoustTabSheet, 0);

		loginButton = new Button("Login");
		loginButton.setStyleName("headerBarLoginButton");
		loginButton.setIcon(loginIcon);
		loginButton.setWidth("100px");
		loginButton.addListener(new LoginButtonListener());
		addComponent(loginButton);
		setExpandRatio(loginButton, 0);
		setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
		
		loggedTabSheet = new HeaderLoggedTabBar(mainArea);
		
		logoutButton = new Button("Logout");
		logoutButton.setStyleName("headerBarLogoutButton");
		logoutButton.setIcon(logoutIcon);
		logoutButton.setWidth("100px");
		logoutButton.addListener(new LogoutButtonListener());
		
		this.mainArea = mainArea;

	}
	
	public void adjustAfterLogin(){
		removeComponent(anonymoustTabSheet);
		removeComponent(loginButton);
		
		addComponent(loggedTabSheet);
		setExpandRatio(loggedTabSheet, 0);
		
		addComponent(logoutButton);
		setExpandRatio(logoutButton, 0);
		setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);
	}
	
	public void adjustAfterLogout(){
		SessionStorage sessionStorage = ((MainApplication) getApplication())
				.getSessionStorage();
		sessionStorage.purge();
		
		removeComponent(loggedTabSheet);
		removeComponent(logoutButton);
		
		addComponent(anonymoustTabSheet);
		setExpandRatio(anonymoustTabSheet, 0);
		
		addComponent(loginButton);
		setExpandRatio(loginButton, 0);
		setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
		
		mainArea.removeAllComponents();
	}

	private class LoginButtonListener implements Button.ClickListener {

		private static final long serialVersionUID = -4873221119391775381L;

		public void buttonClick(ClickEvent event) {
			SessionStorage sessionStorage = ((MainApplication) getApplication())
					.getSessionStorage();
			LoginModal loginModal = new LoginModal(sessionStorage);
			getWindow().addWindow(loginModal);
		}
	}
	
	private class LogoutButtonListener implements Button.ClickListener{

		private static final long serialVersionUID = 7193498834776832636L;

		public void buttonClick(ClickEvent event) {
			adjustAfterLogout();
		}
	}
}
