package com.leven.dmd.gef.tmpfile.model;

import java.io.Serializable;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;

@SuppressWarnings("serial")
public class SequenceTemplate implements Serializable,IPropertySource,INavigatorTreeNode {
	
	private String code;
	
	private String description;
	
	private String maxvalue;
	
	private String minvalue;
	
	private String startwith;
	
	private String incrementby;
	
	private boolean cycle;
	
	private String cache;
	
	private SchemaTemplate schemaTemplate;
	
	private Object tempField1;
	private Object tempField2;
	private Object tempField3;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaxvalue() {
		return maxvalue;
	}

	public void setMaxvalue(String maxvalue) {
		this.maxvalue = maxvalue;
	}

	public String getMinvalue() {
		return minvalue;
	}

	public void setMinvalue(String minvalue) {
		this.minvalue = minvalue;
	}

	public String getStartwith() {
		return startwith;
	}

	public void setStartwith(String startwith) {
		this.startwith = startwith;
	}

	public String getIncrementby() {
		return incrementby;
	}

	public void setIncrementby(String incrementby) {
		this.incrementby = incrementby;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public boolean isCycle() {
		return cycle;
	}

	public void setCycle(boolean cycle) {
		this.cycle = cycle;
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
			new PropertyDescriptor("code",Messages.SequenceTemplate_1), //$NON-NLS-1$
			new PropertyDescriptor("minvalue",Messages.SequenceTemplate_3), //$NON-NLS-1$
			new PropertyDescriptor("maxvalue",Messages.SequenceTemplate_5), //$NON-NLS-1$
			new PropertyDescriptor("startwith",Messages.SequenceTemplate_7), //$NON-NLS-1$
			new PropertyDescriptor("incrementby",Messages.SequenceTemplate_9), //$NON-NLS-1$
			new PropertyDescriptor("cache",Messages.SequenceTemplate_11), //$NON-NLS-1$
			new PropertyDescriptor("description",Messages.SequenceTemplate_13) //$NON-NLS-1$
		};
	}

	public Object getPropertyValue(Object id) {
		if(id.equals("code")){ //$NON-NLS-1$
			return this.code;
		}else if(id.equals("minvalue")){ //$NON-NLS-1$
			return this.minvalue;
		}else if(id.equals("maxvalue")){ //$NON-NLS-1$
			return this.maxvalue;
		}else if(id.equals("startwith")){ //$NON-NLS-1$
			return this.startwith;
		}else if(id.equals("incrementby")){ //$NON-NLS-1$
			return this.incrementby;
		}else if(id.equals("cache")){ //$NON-NLS-1$
			return this.cache;
		}else if(id.equals("description")){ //$NON-NLS-1$
			return this.description;
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
		return code;
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
		return INavigatorNodeTypeConstants.TYPE_SEQUENCE_TEMPLATE_NODE;
	}
}
