package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.leven.dmd.gef.model.PropertyAwareObject;

public abstract class PropertyAwarePart extends AbstractGraphicalEditPart
		implements PropertyChangeListener {

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

		if (PropertyAwareObject.CHILD.equals(property)) {
			handleChildChange(evt);
		}
		if (PropertyAwareObject.REORDER.equals(property)) {
			handleReorderChange(evt);
		} else if (PropertyAwareObject.OUTPUT.equals(property)) {
			handleOutputChange(evt);
		} else if (PropertyAwareObject.INPUT.equals(property)) {
			handleInputChange(evt);
		} else if (PropertyAwareObject.NAME.equals(property)) {
			commitNameChange(evt);
		} else if (PropertyAwareObject.CNNAME.equals(property)) {
			commitCnNameChange(evt);
		} else if (PropertyAwareObject.BOUNDS.equals(property)) {
			handleBoundsChange(evt);
		} else if (PropertyAwareObject.LAYOUT.equals(property)) {
			handleLayoutChange(evt);
		} else if (PropertyAwareObject.STATUS.equals(property)) {
			handleStatusChange(evt);
		} else if (PropertyAwareObject.CHILDREN.equals(property)) {
			handleChildrenChange(evt);
		} else if (PropertyAwareObject.REFRESH_COLUMN.equals(property)) {
			handleColumnRefresh(evt);
		} else if (PropertyAwareObject.REFRESH.equals(property)) {
			handleFigureRefresh(evt);
		}

		// we want direct edit name changes to update immediately
		// not use the Graph animation, if automatic layout is being used
		if (PropertyAwareObject.NAME.equals(property)) {
			GraphicalEditPart graphicalEditPart = (GraphicalEditPart) (getViewer()
					.getContents());
			IFigure partFigure = graphicalEditPart.getFigure();
			partFigure.getUpdateManager().performUpdate();
		}

	}


	private void handleInputChange(PropertyChangeEvent evt) {

		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (!((oldValue != null) ^ (newValue != null))) {
			throw new IllegalStateException(
					"Exactly one of old or new values must be non-null for INPUT event");
		}

		if (newValue != null) {
			// add new connection
			ConnectionEditPart editPart = createOrFindConnection(newValue);
			int modelIndex = getModelTargetConnections().indexOf(newValue);
			addTargetConnection(editPart, modelIndex);

		} else {

			// remove connection
			List children = getTargetConnections();

			ConnectionEditPart partToRemove = null;
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				ConnectionEditPart part = (ConnectionEditPart) iter.next();
				if (part.getModel() == oldValue) {
					partToRemove = part;
					break;
				}
			}

			if (partToRemove != null)
				removeTargetConnection(partToRemove);
		}

		getContentPane().revalidate();

	}

	private void handleOutputChange(PropertyChangeEvent evt) {

		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (!((oldValue != null) ^ (newValue != null))) {
			throw new IllegalStateException(
					"Exactly one of old or new values must be non-null for INPUT event");
		}

		if (newValue != null) {
			// add new connection
			ConnectionEditPart editPart = createOrFindConnection(newValue);
			int modelIndex = getModelSourceConnections().indexOf(newValue);
			addSourceConnection(editPart, modelIndex);

		} else {

			// remove connection
			List children = getSourceConnections();

			ConnectionEditPart partToRemove = null;
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				ConnectionEditPart part = (ConnectionEditPart) iter.next();
				if (part.getModel() == oldValue) {
					partToRemove = part;
					break;
				}
			}

			if (partToRemove != null)
				removeSourceConnection(partToRemove);
		}

		getContentPane().revalidate();

	}

	/**
	 * called when child added or removed
	 */
	protected void handleChildChange(PropertyChangeEvent evt) {

		// we could do this but it is not very efficient
		// refreshChildren();

		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (!((oldValue != null) ^ (newValue != null))) {
			throw new IllegalStateException(
					"Exactly one of old or new values must be non-null for CHILD event");
		}

		if (newValue != null) {
			// add new child
			EditPart editPart = createChild(newValue);
			int modelIndex = getModelChildren().indexOf(newValue);
			addChild(editPart, modelIndex);

		} else {

			List children = getChildren();

			EditPart partToRemove = null;
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				EditPart part = (EditPart) iter.next();
				if (part.getModel() == oldValue) {
					partToRemove = part;
					break;
				}
			}

			if (partToRemove != null)
				removeChild(partToRemove);
		}

		// getContentPane().revalidate();

	}
	protected void handleChildrenChange(PropertyChangeEvent evt) {
		List newValue = (List)evt.getNewValue();
		List oldValue = (List)evt.getOldValue();
		if (!((oldValue != null) ^ (newValue != null))) {
			throw new IllegalStateException(
			"Exactly one of old or new values must be non-null for CHILD event");
		}
		
		if (newValue != null) {
			List children = getChildren();
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				EditPart part = (EditPart) iter.next();
				removeChild(part);
			}
			for (Iterator iter = newValue.iterator(); iter.hasNext();) {
				Object thisModel = iter.next();
				EditPart editPart = createChild(thisModel);
				int modelIndex = getModelChildren().indexOf(thisModel);
				addChild(editPart, modelIndex);
			}
		} else {
			List children = getChildren();
			if(children!=null && children.size()>0){
				for (int i=0;i< children.size();i++) {
					EditPart part = (EditPart) children.get(i);
					removeChild(part);
				}
			}
		}
	}

	/**
	 * Called when columns are re-ordered within
	 */
	protected void handleReorderChange(PropertyChangeEvent evt) {
		refreshChildren();
		refreshVisuals();
	}

	/**
	 * @param subclass
	 *            decides what to do if layout property event is fired
	 */
	protected void handleLayoutChange(PropertyChangeEvent evt) {
	}
	protected void handleBoundsChange(PropertyChangeEvent evt) {
	}
	protected void commitNameChange(PropertyChangeEvent evt) {
	}
	protected void commitCnNameChange(PropertyChangeEvent evt) {
	}
	protected void handleStatusChange(PropertyChangeEvent evt) {
	}
	private void handleFigureRefresh(PropertyChangeEvent evt) {
			List newValue = (List)evt.getNewValue();
			if (newValue == null) {
				throw new IllegalStateException(
				"new values must be non-null for CHILD event");
			}
			List oldValue = getChildren();
			for (int i = oldValue.size()-1;i>=0;i--) {
				EditPart thisEditPart = (EditPart)oldValue.get(i);
				removeChild(thisEditPart);
			}
			for (Iterator iter = newValue.iterator(); iter.hasNext();) {
				Object thisModel = iter.next();
				EditPart editPart = createChild(thisModel);
				int modelIndex = getModelChildren().indexOf(thisModel);
				addChild(editPart, modelIndex);
			}
	}
	private void handleColumnRefresh(PropertyChangeEvent evt) {
		refreshVisuals();
	}

}