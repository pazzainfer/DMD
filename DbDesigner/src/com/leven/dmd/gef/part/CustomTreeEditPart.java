package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.leven.dmd.gef.model.PropertyAwareObject;

public abstract class CustomTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {

	public void activate() {
		super.activate();
		((PropertyAwareObject) getModel()).addPropertyChangeListener(this);
	}
	
	public void deactivate() {
		((PropertyAwareObject) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		
	}
}