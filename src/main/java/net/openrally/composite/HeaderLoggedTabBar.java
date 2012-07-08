package net.openrally.composite;

import net.openrally.MainApplication;
import net.openrally.SessionStorage;
import net.openrally.content.ConsumptionIdentifierContent;
import net.openrally.content.ProductContent;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

public class HeaderLoggedTabBar extends TabSheet implements SelectedTabChangeListener{

	private static final long serialVersionUID = -4963393899592912280L;

	private static final ThemeResource tableIcon = new ThemeResource(
			"images/table.png");

	private static final ThemeResource productIcon = new ThemeResource(
			"images/product.png");

	private HorizontalLayout mainArea;

	public HeaderLoggedTabBar(HorizontalLayout mainArea) {
		addTab(new Panel(), "Mesas", tableIcon);
		addTab(new Panel(), "Produtos", productIcon);
		setHeight("34px");
		setWidth("168px");
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

		SessionStorage sessionStorage = ((MainApplication) getApplication())
				.getSessionStorage();

		if (tabPosition == 0) {
			mainArea.addComponent(new ConsumptionIdentifierContent(
					sessionStorage));
		} else if (tabPosition == 1) {
			mainArea.addComponent(new ProductContent(
					sessionStorage));
		}
	}
}
