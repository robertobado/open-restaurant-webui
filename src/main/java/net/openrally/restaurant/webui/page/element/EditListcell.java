package net.openrally.restaurant.webui.page.element;

import net.openrally.restaurant.webui.entity.EditableEntity;
import net.openrally.restaurant.webui.entity.Product;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Listcell;

public class EditListcell extends Listcell implements IdSpace, AfterCompose {

	private static final long serialVersionUID = -8044346654115984621L;
	
	private EditableEntity editableEntity;

	public EditableEntity getEditableEntity() {
		return editableEntity;
	}

	public void setEditableEntity(Product editableEntity) {
		this.editableEntity = editableEntity;
	}

	@Override
	public void afterCompose() {
		Executions.createComponents("/WEB-INF/composite/editComposite.zul",
				this, null);

		Selectors.wireVariables(this, this, null);
		Selectors.wireComponents(this, this, false);
		Selectors.wireEventListeners(this, this);
	}

	@Listen("onClick=#compositeButtonEdit")
	public void compositeButtonEdit() {
		System.out.println(">>>compositeButtonEdit click event fired");
		Events.postEvent(this, new ItemEditEvent());
	}
	
	// Customize Event
	public static final String ON_ITEM_EDIT = "onItemEdit";
	
	public class ItemEditEvent extends Event {

		private static final long serialVersionUID = -462313777727415236L;

		public ItemEditEvent() {
			super(ON_ITEM_EDIT, EditListcell.this);
		}
	}

}
