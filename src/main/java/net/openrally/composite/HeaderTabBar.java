package net.openrally.composite;

import net.openrally.content.SignUpContent;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

public class HeaderTabBar extends TabSheet implements SelectedTabChangeListener {

	private static final long serialVersionUID = -6509314446489691377L;

	private static final ThemeResource homeIcon = new ThemeResource(
			"images/home.png");
	private static final ThemeResource signUpIcon = new ThemeResource(
			"images/signup.png");

	private HorizontalLayout mainArea;

	public HeaderTabBar(HorizontalLayout mainArea) {
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
			mainArea.addComponent(new SignUpContent());
		} else {
			Label label = new Label("You have selected tab number "
					+ tabPosition);
			mainArea.addComponent(label);
		}

	}
}
