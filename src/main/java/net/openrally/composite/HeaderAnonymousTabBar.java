package net.openrally.composite;

import net.openrally.content.SignUpContent;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

public class HeaderAnonymousTabBar extends TabSheet implements SelectedTabChangeListener {

	private static final long serialVersionUID = -6509314446489691377L;

	private static final ThemeResource homeIcon = new ThemeResource(
			"images/home.png");
	private static final ThemeResource signUpIcon = new ThemeResource(
			"images/signup.png");

	private HorizontalLayout mainArea;

	public HeaderAnonymousTabBar(HorizontalLayout mainArea) {
		addTab(new Panel(), "Home", homeIcon);
		addTab(new Panel(), "Sign Up", signUpIcon);
		setHeight("34px");
		setWidth("160px");
		setStyleName("headerBarTab");
		addListener(this);

		this.mainArea = mainArea;

	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabsheet = event.getTabSheet();
		Component selectedTab = tabsheet.getSelectedTab();
		Tab tab = tabsheet.getTab(selectedTab);
		int tabPosition = tabsheet.getTabPosition(tab);
		mainArea.removeAllComponents();

		if (tabPosition == 1) {
			SignUpContent signUpContent = new SignUpContent();
			signUpContent.setStyleName("signUpContent");
			mainArea.addComponent(signUpContent);
			mainArea.setComponentAlignment(signUpContent, Alignment.TOP_CENTER);
		} else {
			Label label = new Label("You have selected tab number "
					+ tabPosition);
			mainArea.addComponent(label);
		}

	}
}
