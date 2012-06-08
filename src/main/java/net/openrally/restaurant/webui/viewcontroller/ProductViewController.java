package net.openrally.restaurant.webui.viewcontroller;

import java.util.LinkedList;
import java.util.List;

import net.openrally.restaurant.core.util.RandomGenerator;
import net.openrally.restaurant.webui.entity.Product;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class ProductViewController {

	private Product selectedItem;

	public Product getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Product selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<Product> getProductList() {
		return generateProducts();
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

	@Command
	@NotifyChange("productList")
	public void deleteProduct(@BindingParam("product") Product product) {
		List<Product> productList = generateProducts();
		
		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).getProductId()
					.equals(product.getProductId())) {
				productList.remove(i);
				return;
			}
		}
	}
	
	@GlobalCommand
	@NotifyChange("productList")
	public void updateProductList() {
	}

}
