package com.leven.dmd.gef.tmpfile.model;

import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.leven.dmd.gef.Messages;

@SuppressWarnings("serial")
public class SchemaTemplateType implements Serializable, IPropertySource {
	private String type;
	private String description;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Object getEditableValue() {
		return this;
	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
				new PropertyDescriptor("type",Messages.SchemaTemplateType_1), //$NON-NLS-1$
				new PropertyDescriptor("description",Messages.SchemaTemplateType_3) //$NON-NLS-1$
		};
	}
	public Object getPropertyValue(Object id) {
		if(id.equals("type")){ //$NON-NLS-1$
			return this.getType();
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
}	
