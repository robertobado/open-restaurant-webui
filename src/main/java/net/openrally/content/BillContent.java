package net.openrally.content;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.openrally.SessionStorage;
import net.openrally.entity.Bill;
import net.openrally.entity.BillItem;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.entity.Product;
import net.openrally.entity.Tax;
import net.openrally.manager.BillItemManager;
import net.openrally.manager.BillManager;
import net.openrally.manager.BillManager.Status;
import net.openrally.manager.ConfigurationManager;
import net.openrally.manager.ConsumptionIdentifierManager;
import net.openrally.manager.ProductManager;
import net.openrally.manager.TaxManager;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Template;

public class BillContent extends TabSheet {

	private static final long serialVersionUID = 2577372899439750845L;

	protected static final ThemeResource listIcon = new ThemeResource(
			"images/list.png");

	protected Panel openAndManagePanel;
	private HorizontalLayout openAndManageHorizontalLayout;
	private VerticalLayout closedBillsVerticalLayout;
	private VerticalLayout leftVerticalLayout;
	private VerticalLayout rightVerticalLayout;

	private Table closedConsumptionIdentifiersListTable;
	private Table productListTable;
	private Table openBillsListTable;
	private Table openBillItemsListTable;
	private Table closedBillItemsListTable;
	private Table closedBillsListTable;

	private BillManager billManager;
	private ProductManager productManager;
	private TaxManager taxManager;
	private ConsumptionIdentifierManager consumptionIdentifierManager;
	private BillItemManager billItemManager;
	private ConfigurationManager configurationManager;

	private Button openBillButton;

	private Button addProductButton;

	public BillContent(SessionStorage sessionStorage) {
		setSizeFull();
		billManager = new BillManager(sessionStorage);
		productManager = new ProductManager(sessionStorage);
		taxManager = new TaxManager(sessionStorage);
		consumptionIdentifierManager = new ConsumptionIdentifierManager(
				sessionStorage);
		billItemManager = new BillItemManager(sessionStorage);
		configurationManager = new ConfigurationManager(sessionStorage);

		openAndManageHorizontalLayout = new HorizontalLayout();
		openAndManageHorizontalLayout.setSizeFull();
		addTab(openAndManageHorizontalLayout, "Abertura e gerenciamento",
				listIcon);

		initializeLeftSection();
		initializeRightSection();

		openAndManageHorizontalLayout.setExpandRatio(leftVerticalLayout, 0.2f);
		openAndManageHorizontalLayout.setExpandRatio(rightVerticalLayout, 0.8f);

		initializeClosedBillsSection();

	}

	private void initializeClosedBillsSection() {
		closedBillsVerticalLayout = new VerticalLayout();
		closedBillsVerticalLayout.setSizeFull();
		addTab(closedBillsVerticalLayout, "Contas fechadas", listIcon);

		Label closedBills = new Label("Contas fechadas");
		closedBillsVerticalLayout.addComponent(closedBills);

		closedBillsListTable = new Table();

		// openBillsListTable.setWidth("100%");
		closedBillsListTable.setSizeFull();

		closedBillsListTable.setSelectable(true);
		closedBillsListTable.setImmediate(true);

		// turn on column reordering and collapsing
		closedBillsListTable.setColumnReorderingAllowed(true);
		closedBillsListTable.setColumnCollapsingAllowed(true);

		closedBillsListTable.setColumnHeader("consumptionIdentifier", "Mesa");
		closedBillsListTable.setColumnHeader("total", "Total");
		closedBillsListTable.setColumnHeader("openTimestamp", "Abertura");
		closedBillsListTable.setColumnHeader("closeTimestamp", "Fechamento");
		closedBillsListTable.setColumnHeader("reopenButton", "Reabrir");

		closedBillsListTable.setSizeFull();

		closedBillsListTable.addListener(ValueChangeEvent.class, this,
				"closedBillListSelectionChangeListener");

		closedBillsVerticalLayout.addComponent(closedBillsListTable);

		refreshClosedBillsList();

		Label billItemsLabel = new Label("Detalhes da conta");
		closedBillsVerticalLayout.addComponent(billItemsLabel);
		closedBillsVerticalLayout.setComponentAlignment(billItemsLabel,
				Alignment.TOP_CENTER);

		closedBillItemsListTable = new Table();
		closedBillItemsListTable.setSizeFull();

		closedBillItemsListTable.setSelectable(true);
		closedBillItemsListTable.setImmediate(true);

		// turn on column reordering and collapsing
		closedBillItemsListTable.setColumnReorderingAllowed(true);
		closedBillItemsListTable.setColumnCollapsingAllowed(true);

		closedBillItemsListTable.setColumnHeader("quantity", "Quantidade");
		closedBillItemsListTable.setColumnHeader("type", "Tipo");
		closedBillItemsListTable.setColumnHeader("description", "Descricão");
		closedBillItemsListTable.setColumnHeader("total", "Valor");
		closedBillItemsListTable.setColumnHeader("remove", "Remover");

		closedBillsVerticalLayout.addComponent(closedBillItemsListTable);

		refreshClosedBillItemsList();

		closedBillsVerticalLayout.setExpandRatio(closedBillsListTable, 0.5f);
		closedBillsVerticalLayout
				.setExpandRatio(closedBillItemsListTable, 0.5f);

	}

	private void initializeRightSection() {
		rightVerticalLayout = new VerticalLayout();
		rightVerticalLayout.setSizeFull();
		openAndManageHorizontalLayout.addComponent(rightVerticalLayout);

		Label openConsumptionIdentifiersLabel = new Label("Contas abertas");
		rightVerticalLayout.addComponent(openConsumptionIdentifiersLabel);
		rightVerticalLayout.setComponentAlignment(
				openConsumptionIdentifiersLabel, Alignment.TOP_CENTER);

		openBillsListTable = new Table();

		// openBillsListTable.setWidth("100%");
		openBillsListTable.setSizeFull();

		openBillsListTable.setSelectable(true);
		openBillsListTable.setImmediate(true);

		// turn on column reordering and collapsing
		openBillsListTable.setColumnReorderingAllowed(true);
		openBillsListTable.setColumnCollapsingAllowed(true);

		openBillsListTable.setColumnHeader("consumptionIdentifier", "Mesa");
		openBillsListTable.setColumnHeader("total", "Total");
		openBillsListTable.setColumnHeader("openTimestamp", "Abertura");
		openBillsListTable.setColumnHeader("closeButton", "Fechar");
		openBillsListTable.setColumnHeader("printButton", "Imprimir");

		openBillsListTable.setSizeFull();

		openBillsListTable.addListener(ValueChangeEvent.class, this,
				"openBillListSelectionChangeListener");

		rightVerticalLayout.addComponent(openBillsListTable);

		refreshOpenBillsList();

		Label billItemsLabel = new Label("Detalhes da conta");
		rightVerticalLayout.addComponent(billItemsLabel);
		rightVerticalLayout.setComponentAlignment(billItemsLabel,
				Alignment.TOP_CENTER);

		openBillItemsListTable = new Table();
		openBillItemsListTable.setSizeFull();

		openBillItemsListTable.setSelectable(true);
		openBillItemsListTable.setImmediate(true);

		// turn on column reordering and collapsing
		openBillItemsListTable.setColumnReorderingAllowed(true);
		openBillItemsListTable.setColumnCollapsingAllowed(true);

		openBillItemsListTable.setColumnHeader("quantity", "Quantidade");
		openBillItemsListTable.setColumnHeader("type", "Tipo");
		openBillItemsListTable.setColumnHeader("description", "Descrição");
		openBillItemsListTable.setColumnHeader("total", "Valor");
		openBillItemsListTable.setColumnHeader("remove", "Remover");

		rightVerticalLayout.addComponent(openBillItemsListTable);

		refreshOpenBillItemsList();

		rightVerticalLayout.setExpandRatio(openBillsListTable, 0.5f);
		rightVerticalLayout.setExpandRatio(openBillItemsListTable, 0.5f);
	}

	private void refreshClosedBillsList() {
		Container closedBillsContainer = generateClosedBillsContainer();
		closedBillsListTable.setContainerDataSource(closedBillsContainer);

	}

	private void refreshClosedIdentifiersList() {
		Container closedConsumptionIdentifiersContainer = generateClosedConsumptionIdentifiersContainer();
		closedConsumptionIdentifiersListTable
				.setContainerDataSource(closedConsumptionIdentifiersContainer);
	}

	private void refreshOpenBillsList() {
		Container openBillsContainer = generateOpenBillsContainer();
		openBillsListTable.setContainerDataSource(openBillsContainer);
	}

	private void refreshOpenBillItemsList() {
		Container billItemContainer = generateOpenBillItemContainer();
		openBillItemsListTable.setContainerDataSource(billItemContainer);
	}

	private void refreshClosedBillItemsList() {
		Container billItemContainer = generateClosedBillItemContainer();
		closedBillItemsListTable.setContainerDataSource(billItemContainer);
	}

	private void initializeLeftSection() {
		leftVerticalLayout = new VerticalLayout();
		leftVerticalLayout.setSizeFull();
		openAndManageHorizontalLayout.addComponent(leftVerticalLayout);

		Label closedListTableLabel = new Label("Mesas fechadas");
		leftVerticalLayout.addComponent(closedListTableLabel);
		leftVerticalLayout.setComponentAlignment(closedListTableLabel,
				Alignment.TOP_CENTER);

		closedConsumptionIdentifiersListTable = new Table();
		closedConsumptionIdentifiersListTable.setWidth("100%");
		closedConsumptionIdentifiersListTable.setHeight("50%");

		closedConsumptionIdentifiersListTable.setSelectable(true);
		closedConsumptionIdentifiersListTable.setImmediate(true);

		// turn on column reordering and collapsing
		closedConsumptionIdentifiersListTable.setColumnReorderingAllowed(true);
		closedConsumptionIdentifiersListTable.setColumnCollapsingAllowed(true);

		closedConsumptionIdentifiersListTable
				.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);

		closedConsumptionIdentifiersListTable.setSizeFull();

		closedConsumptionIdentifiersListTable.addListener(
				ValueChangeEvent.class, this,
				"closedListTableSelectionListener");

		leftVerticalLayout.addComponent(closedConsumptionIdentifiersListTable);

		refreshClosedIdentifiersList();

		openBillButton = new Button("Abrir conta");
		openBillButton.addListener(Button.ClickEvent.class, this,
				"openBillEventListener");
		openBillButton.setEnabled(false);

		leftVerticalLayout.addComponent(openBillButton);
		leftVerticalLayout.setComponentAlignment(openBillButton,
				Alignment.TOP_CENTER);

		Label productListTableLabel = new Label("Lançar produto");
		leftVerticalLayout.addComponent(productListTableLabel);
		leftVerticalLayout.setComponentAlignment(productListTableLabel,
				Alignment.TOP_CENTER);

		productListTable = new Table();
		productListTable.setWidth("100%");
		productListTable.setHeight("50%");

		productListTable.setSelectable(true);
		productListTable.setImmediate(true);

		productListTable.setColumnHeader("name", "Produto");
		productListTable.setColumnHeader("price", "Preço");

		productListTable.setSizeFull();

		productListTable.addListener(ValueChangeEvent.class, this,
				"productListSelectionChangeEventListener");

		leftVerticalLayout.addComponent(productListTable);

		refreshProductList();

		addProductButton = new Button("Adicionar produto");
		addProductButton.addListener(Button.ClickEvent.class, this,
				"addProductEventListener");
		addProductButton.setEnabled(false);
		leftVerticalLayout.addComponent(addProductButton);
		leftVerticalLayout.setComponentAlignment(addProductButton,
				Alignment.TOP_CENTER);

		leftVerticalLayout.setExpandRatio(
				closedConsumptionIdentifiersListTable, 0.5f);
		leftVerticalLayout.setExpandRatio(productListTable, 0.5f);
	}

	private void refreshProductList() {
		Container container = generateProductListContainer();
		productListTable.setContainerDataSource(container);
	}

	public Container generateClosedConsumptionIdentifiersContainer() {

		List<ConsumptionIdentifier> consumptionIdentifiers = billManager
				.getListOfClosedConsumptionIdentifiers();

		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("identifier", String.class, null);

		for (ConsumptionIdentifier consumptionIdentifier : consumptionIdentifiers) {
			Item item = null;
			try {
				item = container.addItem((Long) consumptionIdentifier.getId());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			item.getItemProperty("identifier").setValue(
					consumptionIdentifier.getIdentifier());
		}

		container.sort(new Object[] { "identifier" }, new boolean[] { true });

		return container;
	}

	@SuppressWarnings("unchecked")
	private Container generateProductListContainer() {
		List<Product> productList = (List<Product>) productManager
				.getEntityInsanceList();

		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("name", String.class, null);
		container.addContainerProperty("price", Double.class, null);

		for (Product product : productList) {
			Item item = null;
			try {
				item = container.addItem((Long) product.getId());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			item.getItemProperty("name").setValue(product.getName());
			item.getItemProperty("price").setValue(product.getPrice());
		}

		container.sort(new Object[] { "name" }, new boolean[] { true });

		return container;
	}

	@SuppressWarnings("unchecked")
	private Container generateOpenBillsContainer() {
		List<Bill> openBillList = billManager.getListBillsByStatus(Status.OPEN);
		Map<Long, ConsumptionIdentifier> consumptionIdentifierMap = (Map<Long, ConsumptionIdentifier>) consumptionIdentifierManager
				.getEntityMap();

		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("consumptionIdentifier", String.class,
				null);
		container.addContainerProperty("total", String.class, null);
		container.addContainerProperty("openTimestamp", String.class, null);
		container.addContainerProperty("closeButton", Button.class, null);
		container.addContainerProperty("printButton", Button.class, null);

		for (Bill bill : openBillList) {
			Item item = null;
			try {
				item = container.addItem((Long) bill.getId());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			ConsumptionIdentifier consumptionIdentifier = consumptionIdentifierMap
					.get(bill.getConsumptionIdentifierId());
			item.getItemProperty("consumptionIdentifier").setValue(
					consumptionIdentifier.getIdentifier());

			List<BillItem> billItemList = billItemManager
					.getBillItemsForBill(bill.getBillId());

			Double total = 0.0;

			for (BillItem billItem : billItemList) {
				total += (billItem.getQuantity() * billItem.getUnitPrice());
			}

			item.getItemProperty("total").setValue("R$ " + total);

			Date date = new Date(bill.getOpenTimestamp() * 1000L);
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			String formatted = format.format(date);
			item.getItemProperty("openTimestamp").setValue(formatted);

			Button closeButton = new Button("Fechar");
			closeButton.setData(bill);
			closeButton.addListener(Button.ClickEvent.class, this,
					"closeBillEventListener");
			item.getItemProperty("closeButton").setValue(closeButton);

			Button printButton = new Button("Imprimir");
			printButton.setData(bill);
			printButton.addListener(Button.ClickEvent.class, this,
					"printBillEventListener");
			item.getItemProperty("printButton").setValue(printButton);

		}

		container.sort(new Object[] { "name" }, new boolean[] { true });

		return container;
	}

	@SuppressWarnings("unchecked")
	private Container generateOpenBillItemContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("quantity", Double.class, null);
		container.addContainerProperty("type", String.class, null);
		container.addContainerProperty("description", String.class, null);
		container.addContainerProperty("total", Double.class, null);
		container.addContainerProperty("remove", Button.class, null);

		Long billId = (Long) openBillsListTable.getValue();

		if (null != billId) {

			List<BillItem> billItemList = billItemManager
					.getBillItemsForBill(billId);

			Map<Long, Product> productMap = (Map<Long, Product>) productManager
					.getEntityMap();

			Map<Long, Tax> taxMap = (Map<Long, Tax>) taxManager.getEntityMap();

			for (BillItem billItem : billItemList) {
				Item item = null;
				try {
					item = container.addItem((Long) billItem.getId());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				item.getItemProperty("quantity").setValue(
						billItem.getQuantity());

				if (BillItemManager.ItemType.PRODUCT.toString().equals(
						billItem.getType())) {
					item.getItemProperty("type").setValue("Produto");
					Product product = productMap.get(billItem.getReferenceId());
					item.getItemProperty("description").setValue(
							product.getName());
				} else if (BillItemManager.ItemType.TAX.toString().equals(
						billItem.getType())) {
					item.getItemProperty("type").setValue("Taxa");
					Tax tax = taxMap.get(billItem.getReferenceId());
					item.getItemProperty("description").setValue(tax.getName());
				}

				item.getItemProperty("total").setValue(
						billItem.getUnitPrice() * billItem.getQuantity());

				Button removeButton = new Button("Remover");
				removeButton.setData(billItem);
				removeButton.addListener(Button.ClickEvent.class, this,
						"removeBillItemEventListener");
				item.getItemProperty("remove").setValue(removeButton);

			}
		}

		return container;
	}

	@SuppressWarnings("unchecked")
	private Container generateClosedBillItemContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("quantity", Double.class, null);
		container.addContainerProperty("type", String.class, null);
		container.addContainerProperty("description", String.class, null);
		container.addContainerProperty("total", Double.class, null);

		Long billId = (Long) closedBillsListTable.getValue();

		if (null != billId) {

			List<BillItem> billItemList = billItemManager
					.getBillItemsForBill(billId);

			Map<Long, Product> productMap = (Map<Long, Product>) productManager
					.getEntityMap();
			Map<Long, Tax> taxMap = (Map<Long, Tax>) taxManager.getEntityMap();

			for (BillItem billItem : billItemList) {
				Item item = null;
				try {
					item = container.addItem((Long) billItem.getId());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				item.getItemProperty("quantity").setValue(
						billItem.getQuantity());

				if (BillItemManager.ItemType.PRODUCT.equals(billItem.getType())) {
					item.getItemProperty("type").setValue("Produto");
					Product product = productMap.get(billItem.getReferenceId());
					item.getItemProperty("description").setValue(
							product.getName());
				} else if (BillItemManager.ItemType.TAX.equals(billItem
						.getType())) {
					item.getItemProperty("type").setValue("Taxa");
					Tax tax = taxMap.get(billItem.getReferenceId());
					item.getItemProperty("description").setValue(tax.getName());
				}

				item.getItemProperty("total").setValue(
						billItem.getUnitPrice() * billItem.getQuantity());
			}
		}

		return container;
	}

	@SuppressWarnings("unchecked")
	private Container generateClosedBillsContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("consumptionIdentifier", String.class,
				null);
		container.addContainerProperty("total", String.class, null);
		container.addContainerProperty("openTimestamp", String.class, null);
		container.addContainerProperty("closeTimestamp", String.class, null);
		container.addContainerProperty("reopenButton", Button.class, null);

		Map<Long, ConsumptionIdentifier> consumptionIdentifierMap = (Map<Long, ConsumptionIdentifier>) consumptionIdentifierManager
				.getEntityMap();

		List<Bill> billList = billManager.getListBillsByStatus(Status.CLOSED);

		for (Bill bill : billList) {
			Item item = null;
			try {
				item = container.addItem((Long) bill.getId());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			ConsumptionIdentifier consumptionIdentifier = consumptionIdentifierMap
					.get(bill.getConsumptionIdentifierId());
			item.getItemProperty("consumptionIdentifier").setValue(
					consumptionIdentifier.getIdentifier());

			List<BillItem> billItemList = billItemManager
					.getBillItemsForBill(bill.getBillId());

			Double total = 0.0;

			for (BillItem billItem : billItemList) {
				total += (billItem.getQuantity() * billItem.getUnitPrice());
			}

			item.getItemProperty("total").setValue(total);

			Date date = new Date(bill.getOpenTimestamp() * 1000L);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			String formatted = format.format(date);
			item.getItemProperty("openTimestamp").setValue(formatted);

			date = new Date(bill.getCloseTimestamp() * 1000L);
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			formatted = format.format(date);
			item.getItemProperty("closeTimestamp").setValue(formatted);

			Button reopenButton = new Button("Reabrir");
			reopenButton.setData(bill);
			reopenButton.addListener(Button.ClickEvent.class, this,
					"reopenBillClickListener");
			item.getItemProperty("reopenButton").setValue(reopenButton);
		}

		container.sort(new Object[] { "closeTimestamp" },
				new boolean[] { true });

		return container;
	}

	public void openBillEventListener(Event event) {
		Long consumptionIdentifierId = (Long) closedConsumptionIdentifiersListTable
				.getValue();

		if (null == consumptionIdentifierId) {
			return;
		}

		Notification notification = billManager
				.openBillAtConsumptionIdentifier(consumptionIdentifierId);

		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			refreshClosedIdentifiersList();
			refreshOpenBillsList();
		}
	}

	public void closedListTableSelectionListener(ValueChangeEvent event) {
		Object value = event.getProperty().getValue();
		if (null == value) {
			openBillButton.setEnabled(false);
		} else {
			openBillButton.setEnabled(true);
		}
	}

	public void productListSelectionChangeEventListener(Event event) {
		Object product = productListTable.getValue();
		Object bill = openBillsListTable.getValue();

		if (bill != null && product != null) {
			addProductButton.setEnabled(true);
		} else {
			addProductButton.setEnabled(false);
		}

	}

	public void openBillListSelectionChangeListener(Event event) {
		Object product = productListTable.getValue();
		Object bill = openBillsListTable.getValue();

		if (bill != null && product != null) {
			addProductButton.setEnabled(true);
		} else {
			addProductButton.setEnabled(false);
		}

		refreshOpenBillItemsList();
	}

	public void closedBillListSelectionChangeListener(Event event) {
		refreshClosedBillItemsList();
	}

	public void addProductEventListener(Event event) {
		Long productId = (Long) productListTable.getValue();
		Long billId = (Long) openBillsListTable.getValue();

		if (null == productId || null == billId) {
			return;
		}

		BillItem billItem = new BillItem();
		billItem.setReferenceId(productId);
		billItem.setQuantity(1.0);
		billItem.setBillId(billId);

		Notification notification = billItemManager.createEntity(billItem);

		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			refreshOpenBillItemsList();
			refreshOpenBillsList();
			openBillsListTable.select(billId);
		}
	}

	public void closeBillEventListener(ClickEvent event) {
		Bill bill = (Bill) ((Button) event.getComponent()).getData();
		bill.setStatus(Status.CLOSED.toString());

		Notification notification = billManager.updateEntity(bill);

		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			refreshOpenBillsList();
			refreshClosedIdentifiersList();
			refreshOpenBillItemsList();
			refreshClosedBillsList();
		}
	}

	public void printBillEventListener(ClickEvent event) {
		
		Configuration configuration = configurationManager.getEntity();

		Bill bill = (Bill) event.getButton().getData();

		List<BillItem> billItems = billItemManager.getBillItemsForBill(bill
				.getBillId());
		billItems = summarizeBillItems(billItems);

		Map<String, Object> dataModel = new HashMap<String, Object>();

		fillDataModelWithGeneralAttributes(dataModel, billItems, configuration.getCompanyName());

		fillDataModelWithProductsAttributes(dataModel, billItems);
		
		fillDataModelWithTaxesAttributes(dataModel, billItems);

		try {
			String billPrintHtml = processTemplate(dataModel, configuration.getBillTemplate());

			// Create a window to hold bill html
			Window window = new Window("Conta");

			// Add content to new window
			window.addComponent(new Label(billPrintHtml, Label.CONTENT_XHTML));

			// Add the printing window as a new application-level
			// window
			getApplication().addWindow(window);

			// Open it as a popup window with no decorations
			getWindow().open(new ExternalResource(window.getURL()), "_blank",
					500, 200, // Width and height
					Window.BORDER_NONE); // No decorations

			// Print automatically when the window opens.
			// This call will block until the print dialog exits!
			window.executeJavaScript("print();");
		} catch (Exception e) {
			e.printStackTrace();
			Notification notification = new Notification(
					"Erro ao criar imprimir conta",
					"Verifique seu modelo de conta",
					Notification.TYPE_ERROR_MESSAGE);
			notification.setDelayMsec(-1);
			getWindow().showNotification(notification);
		}

	}

	public void reopenBillClickListener(ClickEvent event) {
		Bill bill = (Bill) ((Button) event.getComponent()).getData();
		bill.setStatus(Status.OPEN.toString());

		Notification notification = billManager.updateEntity(bill);

		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			refreshOpenBillsList();
			refreshClosedIdentifiersList();
			refreshOpenBillItemsList();
			refreshClosedBillsList();
		}
	}

	public void removeBillItemEventListener(ClickEvent event) {
		BillItem billItem = (BillItem) ((Button) event.getComponent())
				.getData();

		Notification notification = billItemManager.deleteEntity(billItem);

		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			refreshOpenBillItemsList();
			refreshOpenBillsList();
			openBillsListTable.select(billItem.getBillId());
		}
	}

	// Process a template using FreeMarker and print the results
	private String processTemplate(Map<String, Object> dataModel,
			String templateModel) throws Exception {

		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		String billPrintTemplate = "billPrintTemplate";
		stringTemplateLoader.putTemplate(billPrintTemplate, templateModel);

		freemarker.template.Configuration templateConfiguration = new freemarker.template.Configuration();
		templateConfiguration.setTemplateLoader(stringTemplateLoader);
		Template template = templateConfiguration
				.getTemplate(billPrintTemplate);

		Writer out = new StringWriter();
		template.process(dataModel, out);
		return out.toString();
	}

	private void fillDataModelWithGeneralAttributes(
			Map<String, Object> dataModel, List<BillItem> billItems, String companyName) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		String formatted = format.format(date);
		dataModel.put("current_date", formatted);
		
		Double totalAmount = 0.0;
		
		for(BillItem billItem : billItems){
			totalAmount += billItem.getQuantity() * billItem.getUnitPrice();
		}
		
		dataModel.put("grand_total", totalAmount);
		
		dataModel.put("company_name", companyName);
	}

	@SuppressWarnings("unchecked")
	private void fillDataModelWithProductsAttributes(
			Map<String, Object> dataModel, List<BillItem> summarizedBillItems) {

		List<Map<String, Object>> products = new LinkedList<Map<String, Object>>();

		Map<Long, Product> productMap = (Map<Long, Product>) productManager
				.getEntityMap();

		Double productsTotalPrice = 0.0;

		for (BillItem summarizedBillItem : summarizedBillItems) {
			if (!summarizedBillItem.getType().equals(
					BillItemManager.ItemType.PRODUCT.toString())) {
				continue;
			}

			Product product = productMap.get(summarizedBillItem
					.getReferenceId());

			Map<String, Object> productAttributes = new HashMap<String, Object>();
			productAttributes.put("quantity", summarizedBillItem.getQuantity());

			if (null != product) {
				productAttributes.put("name", product.getName());
				productAttributes.put("description", product.getDescription());
			}

			productAttributes.put("unit_price",
					summarizedBillItem.getUnitPrice());

			Double totalPrice = summarizedBillItem.getQuantity()
					* summarizedBillItem.getUnitPrice();
			productAttributes.put("total_price", totalPrice);

			products.add(productAttributes);

			productsTotalPrice += totalPrice;
		}

		dataModel.put("products", products);
		dataModel.put("products_total_price", productsTotalPrice);

	}
	
	@SuppressWarnings("unchecked")
	private void fillDataModelWithTaxesAttributes(
			Map<String, Object> dataModel, List<BillItem> summarizedBillItems) {

		List<Map<String, Object>> taxes = new LinkedList<Map<String, Object>>();

		Map<Long, Tax> taxMap = (Map<Long, Tax>) taxManager
				.getEntityMap();

		Double taxesTotalPrice = 0.0;

		for (BillItem summarizedBillItem : summarizedBillItems) {
			if (!summarizedBillItem.getType().equals(
					BillItemManager.ItemType.TAX.toString())) {
				continue;
			}

			Tax tax = taxMap.get(summarizedBillItem
					.getReferenceId());

			Map<String, Object> taxAttributes = new HashMap<String, Object>();
			taxAttributes.put("quantity", summarizedBillItem.getQuantity());

			if (null != tax) {
				taxAttributes.put("name", tax.getName());
				taxAttributes.put("description", tax.getDescription());
			}

			taxAttributes.put("unit_price",
					summarizedBillItem.getUnitPrice());

			Double totalPrice = summarizedBillItem.getQuantity()
					* summarizedBillItem.getUnitPrice();
			taxAttributes.put("total_price", totalPrice);

			taxes.add(taxAttributes);

			taxesTotalPrice += totalPrice;
		}

		dataModel.put("taxes", taxes);
		dataModel.put("taxes_total_price", taxesTotalPrice);

	}

	private List<BillItem> summarizeBillItems(List<BillItem> originalBillItems) {
		List<BillItem> summarizedBillItems = new LinkedList<BillItem>();

		for (BillItem originalBillItem : originalBillItems) {
			Boolean found = false;

			for (BillItem summarizedBillItem : summarizedBillItems) {

				if (summarizedBillItem.getType().equals(
						originalBillItem.getType())
						&& summarizedBillItem.getReferenceId().equals(
								originalBillItem.getReferenceId())
						&& summarizedBillItem.getUnitPrice().equals(
								originalBillItem.getUnitPrice())) {
					found = true;
					Double summarizedQuantity = summarizedBillItem
							.getQuantity();
					summarizedQuantity += originalBillItem.getQuantity();
					summarizedBillItem.setQuantity(summarizedQuantity);
				}

			}

			if (!found) {
				BillItem summarizedBillItem = new BillItem(originalBillItem);
				summarizedBillItems.add(summarizedBillItem);
			}
		}

		return summarizedBillItems;
	}

}
