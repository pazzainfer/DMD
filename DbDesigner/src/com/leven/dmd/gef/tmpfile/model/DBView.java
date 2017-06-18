package com.leven.dmd.gef.tmpfile.model;

import java.io.Serializable;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
/**
 * 数据库视图对象
 * @author Lifeng-Leven
 *
 */
public class DBView implements Serializable,IPropertySource,INavigatorTreeNode {
	private String name;
	private String description;
	private String querySql;
	private Schema schema;
	//预留属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	
	public DBView(String name){
		this.name = name;
	}
	public DBView(String name, Schema schema){
		this.name = name;
		this.schema = schema;
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
				new PropertyDescriptor("name",Messages.DBView_1) //$NON-NLS-1$
		};
	}

	public Object getPropertyValue(Object id) {
		if(id.equals("name")){ //$NON-NLS-1$
			return this.name;
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
		
	}

	public void setPropertyValue(Object id, Object value) {
		
	}

	public INavigatorTreeNode getParent() {
		return null;
	}

	public List getChildren() {
		return null;
	}

	public void setChildren(List children) {
		
	}

	public String getName() {
		return name;
	}

	public Object getRoot() {
		return schema;
	}

	public Object getData() {
		return this;
	}

	public boolean hasChildren() {
		return false;
	}

	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_VIEW_NODE;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQuerySql() {
		return querySql;
	}
	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}
}