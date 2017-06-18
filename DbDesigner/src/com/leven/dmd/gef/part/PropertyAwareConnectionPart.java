package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import com.leven.dmd.gef.model.PropertyAwareObject;

public abstract class PropertyAwareConnectionPart extends
		AbstractConnectionEditPart implements PropertyChangeListener {

	public void activate() {
		super.activate();
		PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
		propertyAwareObject.addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
		propertyAwareObject.removePropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt) {

		String property = evt.getPropertyName();

		if (PropertyAwareObject.CHILD.equals(property))
			refreshChildren();
		else if (PropertyAwareObject.INPUT.equals(property))
			refreshTargetConnections();
		else if (PropertyAwareObject.OUTPUT.equals(property))
			refreshSourceConnections();

		((GraphicalEditPart) (getViewer().getContents())).getFigure()
				.revalidate();
	}

	protected void refreshStartLabel(PropertyChangeEvent evt) {
		((GraphicalEditPart) (getViewer().getContents())).getFigure()
			.revalidate();
	}
	protected void refreshEndLabel(PropertyChangeEvent evt) {
		((GraphicalEditPart) (getViewer().getContents())).getFigure()
			.revalidate();
	}

}