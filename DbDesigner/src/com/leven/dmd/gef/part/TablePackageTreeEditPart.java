package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.eclipse.gef.EditPolicy;

import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.policy.TableEditPolicy;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class TablePackageTreeEditPart extends CustomTreeEditPart {
	
	@Override
	protected List getModelChildren() {
		TablePackage model = (TablePackage) getModel();
		return model.getTables();
	}
	
	protected void refreshVisuals() {
		TablePackage model = (TablePackage) getModel();
		setWidgetText(model.getName());
		
		setWidgetImage(Activator.getImage(ImageKeys.PACKAGE));
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(TablePackage.NAME))
			refreshChildren();
	}

	@Override
	public void deactivate() {
		super.deactivate();
	}
	
}
