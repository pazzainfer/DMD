package com.leven.dmd.gef.editor.ruler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;

import com.leven.dmd.gef.editor.guide.Guide;

/**
 * ���
 */
public class Ruler implements Serializable {
	public static final String PROPERTY_CHILDREN = "children changed";
	public static final String PROPERTY_UNIT = "units changed";
	static final long serialVersionUID = 1;
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private int unit;
	private boolean horizontal;
	private List<Guide> guides = new ArrayList<Guide>();
	
	public Ruler(boolean isHorizontal) {
		this(isHorizontal, RulerProvider.UNIT_INCHES);
	}

	/**
	 * 
	 * @param isHorizontal
	 * @param unit
	 * ������ص�Ԫ,��ѡ�Ĳ�����:
	 * RulerProvider.UNIT_INCHES,
	 * RulerProvider.UNIT_CENTIMETERS,
	 * RulerProvider.UNIT_PIXELS
	 */
	public Ruler(boolean isHorizontal, int unit) {
		horizontal = isHorizontal;
		setUnit(unit);
	}

	public void addGuide(Guide guide) {
		if (!guides.contains(guide)) {
			guide.setHorizontal(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	// the returned list should not be modified
	public List<Guide> getGuides() {
		return guides;
	}

	public int getUnit() {
		return unit;
	}

	public boolean isHidden() {
		return false;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void removeGuide(Guide guide) {
		if (guides.remove(guide)) {
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public void setHidden(boolean isHidden) {

	}

	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			int oldUnit = unit;
			unit = newUnit;
			listeners.firePropertyChange(PROPERTY_UNIT, oldUnit, newUnit);
		}
	}
}