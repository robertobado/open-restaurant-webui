package net.openrally.composite;

import net.openrally.MainApplication;
import net.openrally.SessionStorage;
import net.openrally.content.BillContent;
import net.openrally.content.ConsumptionIdentifierContent;
import net.openrally.content.ProductContent;
import net.openrally.content.TaxContent;

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
	
	private static final ThemeResource billIcon = new ThemeResource(
			"images/bill16.png");
	
	private static final ThemeResource taxIcon = new ThemeResource(
			"images/tax16.png");


	private HorizontalLayout mainArea;

	public HeaderLoggedTabBar(HorizontalLayout mainArea) {
		addTab(new Panel(), "Mesas", tableIcon);
		addTab(new Panel(), "Produtos", productIcon);
		addTab(new Panel(), "Contas", billIcon);
		addTab(new Panel(), "Taxas", taxIcon);
		setHeight("34px");
		setWidth("320px");
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
		else if (tabPosition == 2) {
			mainArea.addComponent(new BillContent(
					sessionStorage));
		} else if(tabPosition == 3){
			mainArea.addComponent(new TaxContent(
					sessionStorage));
		}
	}
}
