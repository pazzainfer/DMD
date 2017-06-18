package com.leven.dmd.gef.tmpfile.model;

import java.io.Serializable;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
/**
 * 字段模板
 * @author leven
 * create at 2013-10-25 上午6:17:40
 */
@SuppressWarnings("serial")
public class ColumnTemplate implements Serializable,IPropertySource,INavigatorTreeNode {
	private String code;
	
	private String columnName;
	private String columnCnName;
	private ColumnType columnType;
	private int columnLength;
	private int columnScale;
	private String description="";
	
	private SchemaTemplate schemaTemplate;
	

	//预留属性
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;
	private Object tempField4;
	/**
	 * 是否外部引用模板
	 */
	private boolean isOutQuote = false;
	/**
	 * �ֶ�ģ��
	 */
	public ColumnTemplate(){
		
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ColumnTemplate){
			ColumnTemplate ct = (ColumnTemplate)obj;
			if(columnName.equals(ct.getColumnName()) && columnType.getType().equals(ct.getColumnType().getType()) && 
					columnLength==ct.getColumnLength() && columnScale==ct.getColumnScale()){
				return true;
			}
		}
		return false;
	}

	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public ColumnType getColumnType() {
		return columnType;
	}
	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}
	public int getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(int columnLength) {
		this.columnLength = columnLength;
	}
	public int getColumnScale() {
		return columnScale;
	}
	public void setColumnScale(int columnScale) {
		this.columnScale = columnScale;
	}

	public String getColumnCnName() {
		return columnCnName==null?"":columnCnName; //$NON-NLS-1$
	}

	public SchemaTemplate getSchemaTemplate() {
		return schemaTemplate;
	}

	public void setSchemaTemplate(SchemaTemplate schemaTemplate) {
		this.schemaTemplate = schemaTemplate;
	}

	public Object getEditableValue() {
		return this;
	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
			new PropertyDescriptor("columnName",Messages.ColumnTemplate_3), //$NON-NLS-1$
			new PropertyDescriptor("columnCnName",Messages.ColumnTemplate_5), //$NON-NLS-1$
			new PropertyDescriptor("columnType",Messages.ColumnTemplate_7), //$NON-NLS-1$
			new PropertyDescriptor("columnLength",Messages.ColumnTemplate_9), //$NON-NLS-1$
			new PropertyDescriptor("columnScale",Messages.ColumnTemplate_11), //$NON-NLS-1$
			new PropertyDescriptor("description",Messages.ColumnTemplate_13) //$NON-NLS-1$
		};
	}
	public Object getPropertyValue(Object id) {
		if(id.equals("columnName")){ //$NON-NLS-1$
			return this.getColumnName();
		}else if(id.equals("columnCnName")){ //$NON-NLS-1$
			return this.getColumnCnName();
		}else if(id.equals("columnType")){ //$NON-NLS-1$
			return this.getColumnType().getType();
		}else if(id.equals("columnLength")){ //$NON-NLS-1$
			return this.columnLength==0?"":(this.columnLength+""); //$NON-NLS-1$ //$NON-NLS-2$
		}else if(id.equals("columnScale")){ //$NON-NLS-1$
			return this.columnScale==0?"":(this.columnScale+""); //$NON-NLS-1$ //$NON-NLS-2$
		}else if(id.equals("description")){ //$NON-NLS-1$
			return this.getDescription();
		}
		return null;
	}
	public boolean isPropertySet(Object arg0) {
		return false;
	}
	public void resetPropertyValue(Object arg0) {
		
	}
	public void setPropertyValue(Object arg0, Object arg1) {
		
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
		return columnName;
	}

	public Object getRoot() {
		return schemaTemplate;
	}

	public Object getData() {
		return this;
	}

	public boolean hasChildren() {
		return false;
	}

	public int getNodeType() {
		return INavigatorNodeTypeConstants.TYPE_COLUMN_TEMPLATE_NODE;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description==null?"":description;
	}
	public String getDescription() {
		return description;
	}
	public void setColumnCnName(String columnCnName) {
		this.columnCnName = columnCnName==null?"":columnCnName;
	}

	public boolean isOutQuote() {
		return isOutQuote;
	}
	public void setOutQuote(boolean isOutQuote) {
		this.isOutQuote = isOutQuote;
	}
}