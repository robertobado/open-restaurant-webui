package net.openrally.restaurant.webui.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.openrally.restaurant.core.util.RandomGenerator;
import net.openrally.restaurant.webui.entity.Product;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;

public class ProductController extends SelectorComposer<Tabbox> {

	private static final long serialVersionUID = -7186783182440309573L;

	@Wire
	private Textbox contentTextBoxNewProductName;

	@Wire
	private Textbox contentTextBoxNewProductDescription;

	@Wire
	private Textbox contentTextBoxNewProductPrice;
	
	@Wire
	private Row contentNewProductRowMessage;

	@Listen("onClick=#contentButtonNewProductSave")
	public void contentButtonNewProductSave() throws ClientProtocolException,
			IOException {
		contentNewProductRowMessage.setVisible(false);
		
		validate();
		
		List<Product> productList = generateProducts();

		Product product = new Product();
		
		product.setName(contentTextBoxNewProductName.getValue());
		product.setDescription(contentTextBoxNewProductDescription.getValue());
		product.setPrice(Double.parseDouble(contentTextBoxNewProductPrice.getValue()));
		product.setProductId(productList.get(productList.size()-1).getProductId() + 1);
		
		productList.add(product);
		
		BindUtils.postGlobalCommand(null, null, "updateProductList", null);
		
		contentNewProductRowMessage.setVisible(true);

	}
	
	@Listen("onClick")
	public void onClickV1(Event event) {
		System.out.println(">>>onClickV1 fired");
	}
	
	@Listen("onItemEdit=button")
	public void onItemEditV1(Event event) {
		System.out.println(">>>onItemEditV1 fired");
	}
	
	@Listen("onItemEdit=tabbox#contentTabBoxProduct")
	public void onItemEditV2(Event fe) {
		System.out.println(">>>onItemEditV2 fired");
	}
	
	@Listen("onItemEdit=tabbox#contentTabBoxProduct #contentTabPanelsProduct #contentTabPanelList #contentListBoxProduct listitem editCell")
	public void onItemEditV3(Event fe) {
		System.out.println(">>>onItemEditV3 fired");
	}
	
	@Listen("onItemEdit=tabbox#contentTabBoxProduct tabpanels tabpanel listbox listitem editCell")
	public void onItemEditV4(Event fe) {
		System.out.println(">>>onItemEditV4 fired");
	}
	
	@SuppressWarnings("unchecked")
	private List<Product> generateProducts() {

		List<Product> productList = null;

		Session zkSession = Sessions.getCurrent();
		synchronized (zkSession) {
			productList = (List<Product>) zkSession.getAttribute("productList");
			if (null == productList) {
				Integer quantity = 10;

				productList = new LinkedList<Product>();

				for (Integer i = 0; i < quantity; i++) {
					Product product = new Product();

					product.setDescription("Description "
							+ RandomGenerator.generateString(5));
					product.setName("Name " + RandomGenerator.generateString(5));
					product.setProductId(i.longValue());
					product.setPrice(RandomGenerator.randomPositiveDouble(1000));

					productList.add(product);
				}
				zkSession.setAttribute("productList", productList);

			}
		}

		return productList;
	}

	public void validate() {
		if (StringUtils.isBlank(contentTextBoxNewProductName.getValue())) {
			throw new WrongValueException(contentTextBoxNewProductName,
					"Product name cannot be empty");
		}
		
		if (StringUtils.isBlank(contentTextBoxNewProductPrice.getValue())) {
			throw new WrongValueException(contentTextBoxNewProductPrice,
					"Product price cannot be empty");
		}
		
		try{
			Double.parseDouble(contentTextBoxNewProductPrice.getValue());
		}catch(NumberFormatException e){
			throw new WrongValueException(contentTextBoxNewProductPrice,
					"Invalid product price");
		}
	}

}
