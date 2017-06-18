package com.leven.dmd.gef.model;

import java.util.ArrayList;
import java.util.List;

import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;

@SuppressWarnings("serial")
public class SchemaTreeNode extends PropertyAwareObject implements INavigatorTreeNode {
	private String name;
	private int nodeType;
	private Object data;
	private Object root;
	private	INavigatorTreeNode parent;
	private List children = new ArrayList();
	
	public SchemaTreeNode() {}
	
	public SchemaTreeNode(String name, int nodeType) {
		super();
		this.name = name;
		this.nodeType = nodeType;
	}
	public INavigatorTreeNode getParent() {
		return parent;
	}
	public List getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public Object getData() {
		return data;
	}
	public Object getRoot() {
		return root;
	}
	public boolean hasChildren() {
		return children!=null && !children.isEmpty();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public void setValue(Object value) {
		this.data = value;
	}
	public void setParent(INavigatorTreeNode parent) {
		this.parent = parent;
	}
}
