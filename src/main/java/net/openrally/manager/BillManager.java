package net.openrally.manager;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.Bill;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.entity.Entity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.google.gson.reflect.TypeToken;
import com.vaadin.ui.Window.Notification;

public class BillManager extends BaseEntityManager {

	private static final long serialVersionUID = -6808350509728759186L;
	
	public static enum Status {
		OPEN("open"), CLOSED("closed");

		private String value;

		private Status(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
	
	private ConsumptionIdentifierManager consumptionIdentifierManager;

	public BillManager(SessionStorage sessionStorage) {
		super(sessionStorage);
		consumptionIdentifierManager = new ConsumptionIdentifierManager(sessionStorage);
	}

	private static final String PATH = "bill";

	@Override
	protected Class<? extends Entity> getEntityClass() {
		return Bill.class;
	}

	@Override
	protected String getPath() {
		return PATH;
	}

	@Override
	protected Type getEntityListClass() {
		return new TypeToken<List<Bill>>() {}.getType();
	}
	
	@SuppressWarnings("unchecked")
	public List<Bill> getListBillsByStatus(Status status){
		String queryParams = "status=" + status.value;
		return (List<Bill>) getEntityInsanceList(queryParams);
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumptionIdentifier> getListOfClosedConsumptionIdentifiers(){
		List<Bill> openBills = getListBillsByStatus(Status.OPEN);
		List<ConsumptionIdentifier> consumptionIdentifiers = (List<ConsumptionIdentifier>) consumptionIdentifierManager.getEntityInsanceList();
		
		List<ConsumptionIdentifier> noOpenBillsConsumptionIdentifiers = new LinkedList<ConsumptionIdentifier>(consumptionIdentifiers);
		
		for(Bill bill : openBills){
			for(ConsumptionIdentifier consumptionIdentifier : consumptionIdentifiers){
				if(consumptionIdentifier.getConsumptionIdentifierId() == bill.getConsumptionIdentifierId()){
					noOpenBillsConsumptionIdentifiers.remove(consumptionIdentifier);
				}
			}
		}
		
		return noOpenBillsConsumptionIdentifiers;
	}

	public Notification openBillAtConsumptionIdentifier(Long consumptionIdentifierId) {
		Bill entityRequestBody = new Bill();

		entityRequestBody.setConsumptionIdentifierId(consumptionIdentifierId);
		entityRequestBody.setStatus(Status.OPEN.toString());
		entityRequestBody.setPax(1);
		
		String requestBody = gson.toJson(entityRequestBody);
		
		HttpPost httpPost = generateBasicHttpPost(PATH);

		Notification notification;

		try {
			httpPost.setEntity(new StringEntity(requestBody, UTF_8));
			HttpResponse response = getHttpClient().execute(httpPost);

			if (HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()) {
				notification = new Notification("Sucesso",
						"Mesa aberta com sucesso",
						Notification.TYPE_HUMANIZED_MESSAGE);
				notification.setDelayMsec(SUCCESS_NOTIFICATION_DISMISS_TIME);
			} else {
				notification = new Notification("Erro ao abrir mesa",
						"Erro desconhecido.", Notification.TYPE_ERROR_MESSAGE);
				notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
			}
		} catch (Exception e) {
			notification = new Notification("Erro ao abrir mesa",
					"Erro desconhecido", Notification.TYPE_ERROR_MESSAGE);
			notification.setDelayMsec(FAILURE_NOTIFICATION_DISMISS_TIME);
		}
		
		return notification;
		
	}

}
