package net.openrally.content.bill;

import net.openrally.content.BillContent;
import net.openrally.entity.Bill;
import net.openrally.manager.BillManager;
import net.openrally.manager.BillManager.Status;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class CloseBillModal extends Window{

	private static final long serialVersionUID = 2484880123855304163L;
	
	private TextField paxTextField;
	
	private Bill bill;
	
	private BillManager billManager;
	
	private BillContent billContent;
	
	public CloseBillModal(BillManager billManager, Bill bill, BillContent billContent){
		super("Fechar conta");
		setModal(true);
		setWidth("310px");
		setHeight("150px");
		setResizable(false);
	        
	    GridLayout layout = new GridLayout(2, 3);
	    addComponent(layout);
		
		Label paxText = new Label("Informe o número de clientes");
		layout.addComponent(paxText, 0, 0, 1, 0);
		layout.setComponentAlignment(paxText, Alignment.MIDDLE_CENTER);
		
		Label paxLabel = new Label("Pax");
		paxLabel.setWidth("100px");
		layout.addComponent(paxLabel, 0, 1);
		layout.setComponentAlignment(paxLabel, Alignment.TOP_RIGHT);
		
		paxTextField = new TextField();
		paxTextField.setValue(bill.getPax());
		layout.addComponent(paxTextField, 1, 1);
		layout.setComponentAlignment(paxTextField, Alignment.TOP_LEFT);
		
		Button loginButton = new Button("Fechar");
		loginButton.addListener(ClickEvent.class, this, "closeBillEventListener");
		loginButton.setStyleName("textCenter");
		layout.addComponent(loginButton, 0,2,1,2);
		
		this.bill = bill;
		this.billManager = billManager;
		this.billContent = billContent;
	}

	public void closeBillEventListener(ClickEvent event) {
		paxTextField.setComponentError(null);
		
		Integer pax = null;
		
		try{
			pax = Integer.parseInt(paxTextField.getValue().toString());
			if(pax <= 0){
				throw new Exception("Invalid pax value");
			}
		} catch(Exception e){
			paxTextField.setComponentError(new UserError(
					"Valor pax inválido"));
			return;
		}
		
		bill.setPax(pax);
		
		bill.setStatus(Status.CLOSED.toString());

		Notification notification = billManager.updateEntity(bill);

		getWindow().showNotification(notification);

		if (notification.getDelayMsec() > 0) {
			billContent.displayNotification(notification);
			billContent.refreshOpenBillsList();
			billContent.refreshClosedIdentifiersList();
			billContent.refreshOpenBillItemsList();
			billContent.refreshClosedBillsList();
			getParent().removeWindow(this);
		}
		else{
			getWindow().showNotification(notification);
		}
		
	}
}
