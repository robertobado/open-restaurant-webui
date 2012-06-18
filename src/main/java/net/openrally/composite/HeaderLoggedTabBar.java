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

public class HeaderLoggedTabBar extends TabSheet implements SelectedTabChangeListener {
	
	private static final long serialVersionUID = -4963393899592912280L;
	
	private static final ThemeResource tableIcon = new ThemeResource(
			"images/table.png");
	
	private static final ThemeResource productIcon = new ThemeResource(
			"images/product.png");
	
	private static final ThemeResource userIcon = new ThemeResource(
			"images/user.png");

	private HorizontalLayout mainArea;
	
	public HeaderLoggedTabBar(HorizontalLayout mainArea) {
		addTab(new Panel(), "Mesas", tableIcon);
		addTab(new Panel(), "Produtos", productIcon);
		addTab(new Panel(), "Usuários", userIcon);
		setHeight("34px");
		setWidth("300px");
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

		if (tabPosition == 0) {
			Label label = new Label("Mesas");
			mainArea.addComponent(label);
		} else if (tabPosition == 1) {
			Label label = new Label("Produtos");
			mainArea.addComponent(label);
		} else if (tabPosition == 2) {
			Label label = new Label("Usuários");
			mainArea.addComponent(label);
		}

	}
}
