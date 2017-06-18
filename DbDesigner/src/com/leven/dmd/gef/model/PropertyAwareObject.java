package com.leven.dmd.gef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * 模型对象基类，包含属性变更监听处理对象。
 * @author lifeng
 * 2012-7-11 ����03:27:40
 */
@SuppressWarnings("serial")
public abstract class PropertyAwareObject extends Object implements
		Serializable,Cloneable {

	public static final String CHILD = "CHILD";
	public static final String CHILDREN = "CHILDREN";
	public static final String REORDER = "REORDER";
	public static final String BOUNDS = "BOUNDS";
	public static final String INPUT = "INPUT";
	public static final String OUTPUT = "OUTPUT";
	public static final String NAME = "NAME";
	public static final String LAYOUT = "LAYOUT";
	public static final String STATUS = "STATUS";
	public static final String CNNAME = "CNNAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String P_CONSTRAINT = "P_CONSTRAINT";
	public static final String REFRESH_COLUMN = "refresh_column";
	public static final String REFRESH = "REFRESH";
	//备用监听键
	public static final String TEMP_EVENT1 = "TEMP_EVENT1";
	public static final String TEMP_EVENT2 = "TEMP_EVENT2";
	public static final String TEMP_EVENT3 = "TEMP_EVENT3";
	public static final String TEMP_EVENT4 = "TEMP_EVENT4";
	

	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(
			this);

	protected PropertyAwareObject() {
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue) {
		listeners.firePropertyChange(prop, old, newValue);
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		listeners = new PropertyChangeSupport(this);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		PropertyAwareObject obj;
		obj = (PropertyAwareObject) super.clone();
		return obj;
	}
}
