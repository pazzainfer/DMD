package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import org.eclipse.gef.EditPolicy;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.policy.TableEditPolicy;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class TableTreeEditPart extends CustomTreeEditPart {
	
	protected void refreshVisuals() {
		Table model = (Table) getModel();
		setWidgetText(model.getCnName() + "(" + model.getName() + ")");
		int status = model.getStatus();
		
		if(status==IEditorConstant.TABLE_STATUS_NORMAL){
			setWidgetImage(Activator.getImage(ImageKeys.TABLE_NORMAL));
		}else if(status==IEditorConstant.TABLE_STATUS_CHANGED){
			setWidgetImage(Activator.getImage(ImageKeys.TABLE_CHANGED));
		}else if(status==IEditorConstant.TABLE_STATUS_ADDED){
			setWidgetImage(Activator.getImage(ImageKeys.TABLE_ADDED));
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Table.NAME))
			refreshChildren();
	}

	@Override
	public void deactivate() {
		super.deactivate();
	}
}