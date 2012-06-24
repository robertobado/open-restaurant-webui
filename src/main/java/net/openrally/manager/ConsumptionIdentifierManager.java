package net.openrally.manager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.openrally.SessionStorage;
import net.openrally.entity.ConsumptionIdentifier;
import net.openrally.entity.ConsumptionIdentifierList;
import net.openrally.util.RandomGenerator;
import net.openrally.util.StringUtilities;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

public class ConsumptionIdentifierManager extends BaseManager {
	
	private static final long serialVersionUID = 4070524836393555115L;
	
	private static final ThemeResource editIcon = new ThemeResource(
			"images/edit.png");
	private static final ThemeResource deleteIcon = new ThemeResource(
			"images/delete.png");
	
	public static final String PATH = "consumption-identifier";

    public static final Object PROPERTY_IDENTIFIER = "identifier";
    public static final Object PROPERTY_DESCRIPTION = "description";
    public static final Object PROPERTY_EDIT = "edit";
    public static final Object PROPERTY_DELETE = "delete";

	public ConsumptionIdentifierManager(SessionStorage sessionStorage){
		super(sessionStorage);
	}
	
	public Container getContainer(ClickListener editButtonListener, ClickListener deleteButtonListener){
		IndexedContainer container = new IndexedContainer();
		
        container.addContainerProperty(PROPERTY_IDENTIFIER, String.class,
                null);
        container.addContainerProperty(PROPERTY_DESCRIPTION, String.class,
                null);
        container.addContainerProperty(PROPERTY_EDIT, Button.class,
                null);
        container.addContainerProperty(PROPERTY_DELETE, Button.class,
                null);
        
        List<ConsumptionIdentifier> consumptionIdentifiers = getConsumptionIdentifiers();
        
        for (ConsumptionIdentifier consumptionIdentifier : consumptionIdentifiers) {
            Item item = container.addItem(consumptionIdentifier.getConsumptionIdentifierId());
            item.getItemProperty(PROPERTY_IDENTIFIER).setValue(consumptionIdentifier.getIdentifier());
            item.getItemProperty(PROPERTY_DESCRIPTION).setValue(consumptionIdentifier.getDescription());
            
            Button editButton = new Button();
            editButton.addListener(editButtonListener);
            editButton.setIcon(editIcon);
            item.getItemProperty(PROPERTY_EDIT).setValue(editButton);
            
            Button deleteButton = new Button();
            deleteButton.addListener(deleteButtonListener);
            deleteButton.setIcon(deleteIcon);
            item.getItemProperty(PROPERTY_DELETE).setValue(deleteButton);
            
        }
        
        container.sort(new Object[] { PROPERTY_IDENTIFIER },
                new boolean[] { true });
		
		return container;
	}
	
	private List<ConsumptionIdentifier> getConsumptionIdentifiers(){
		List<ConsumptionIdentifier> list = new LinkedList<ConsumptionIdentifier>();
		ConsumptionIdentifier consumptionIdentifierA = new ConsumptionIdentifier();
		ConsumptionIdentifier consumptionIdentifierB = new ConsumptionIdentifier();
		ConsumptionIdentifier consumptionIdentifierC = new ConsumptionIdentifier();
		
		consumptionIdentifierA.setConsumptionIdentifierId(1L);
		consumptionIdentifierB.setConsumptionIdentifierId(2L);
		consumptionIdentifierC.setConsumptionIdentifierId(3L);
		
		consumptionIdentifierA.setIdentifier(RandomGenerator.generateString(10));
		consumptionIdentifierB.setIdentifier(RandomGenerator.generateString(10));
		consumptionIdentifierC.setIdentifier(RandomGenerator.generateString(10));
		
		consumptionIdentifierA.setDescription(RandomGenerator.generateString(200));
		consumptionIdentifierB.setDescription(RandomGenerator.generateString(200));
		consumptionIdentifierC.setDescription(RandomGenerator.generateString(200));
		
		list.add(consumptionIdentifierA);
		list.add(consumptionIdentifierB);
		list.add(consumptionIdentifierC);
		
		
		HttpGet httpGet = generateBasicHttpGet(PATH);
		try {
			HttpResponse response = getHttpClient().execute(httpGet);
			String responseBody = StringUtilities.httpResponseAsString(response);
			ConsumptionIdentifierList consumptionIdentifierResponseBody = gson.fromJson(responseBody,
					ConsumptionIdentifierList.class);
			
			if(list.size() > 0){
				return list;
			}
			return consumptionIdentifierResponseBody.getList();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new LinkedList<ConsumptionIdentifier>();
		} catch (IOException e) {
			e.printStackTrace();
			return new LinkedList<ConsumptionIdentifier>();
		}
	}

}
