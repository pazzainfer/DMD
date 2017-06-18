package com.leven.dmd.pro.nav.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class NavigatorTreeNode implements INavigatorTreeNode,Serializable {
	private INavigatorTreeNode parent;
	private List children = new ArrayList();
	private Object data;
	private Object root;
	private int type;
	private String name;
	
	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	public NavigatorTreeNode(String name,Object data,Object root,int type){
		this.data = data;
		this.root = root;
		this.name = name;
		this.type = type;
	}
	public NavigatorTreeNode(String name,Object root,int type,PropertyChangeListener listener){
		this.root = root;
		this.name = name;
		this.type = type;
		listeners.addPropertyChangeListener(listener);
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

	public INavigatorTreeNode getParent() {
		return parent;
	}
	public List getChildren() {
		return children;
	}
	public boolean addChild(Object obj){
		return children.add(obj);
	}
	public Object getRoot() {
		return root;
	}
	public Object getData() {
		return data;
	}
	public boolean hasChildren() {
		return children!=null && !children.isEmpty();
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public int getNodeType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
