package com.leven.dmd.gef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class DiagramGuide implements Serializable {

	/**
	 * Property used to notify listeners when the parts attached to a guide are
	 * changed
	 */
	public static final String PROPERTY_CHILDREN = "subparts changed"; //$NON-NLS-1$
	/**
	 * Property used to notify listeners when the guide is re-positioned
	 */
	public static final String PROPERTY_POSITION = "position changed"; //$NON-NLS-1$

	static final long serialVersionUID = 1;

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private Map map;
	private int position;
	private boolean horizontal;

	/**
	 * Empty default constructor
	 */
	public DiagramGuide() {
		// empty constructor
	}

	/**
	 * Constructor
	 * 
	 * @param isHorizontal
	 *            <code>true</code> if the guide is horizontal (i.e., placed on
	 *            a vertical ruler)
	 */
	public DiagramGuide(boolean isHorizontal) {
		setHorizontal(isHorizontal);
	}

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/**
	 * @return The Map containing all the parts attached to this guide, and
	 *         their alignments; the keys are LogicSubparts and values are
	 *         Integers
	 */
	public Map getMap() {
		if (map == null) {
			map = new Hashtable();
		}
		return map;
	}

	/**
	 * @return the set of all the parts attached to this guide; a set is used
	 *         because a part can only be attached to a guide along one edge.
	 */
	public Set getParts() {
		return getMap().keySet();
	}

	/**
	 * @return the position/location of the guide (in pixels)
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return <code>true</code> if the guide is horizontal (i.e., placed on a
	 *         vertical ruler)
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Sets the orientation of the guide
	 * 
	 * @param isHorizontal
	 *            <code>true</code> if this guide is to be placed on a vertical
	 *            ruler
	 */
	public void setHorizontal(boolean isHorizontal) {
		horizontal = isHorizontal;
	}

	/**
	 * Sets the location of the guide
	 * 
	 * @param offset
	 *            The location of the guide (in pixels)
	 */
	public void setPosition(int offset) {
		if (position != offset) {
			int oldValue = position;
			position = offset;
			listeners.firePropertyChange(PROPERTY_POSITION, new Integer(
					oldValue), new Integer(position));
		}
	}

}
