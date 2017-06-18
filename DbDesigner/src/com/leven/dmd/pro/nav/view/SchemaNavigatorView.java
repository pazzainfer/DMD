package com.leven.dmd.pro.nav.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class SchemaNavigatorView extends CommonNavigator implements PropertyChangeListener{
	public static final String VIEW_ID = "com.leven.dmd.pro.schemaNavigatorViewer";
	public static final String NAME = "NAME";
	
	private Schema schema;

	@Override
	protected Object getInitialInput() {
		return NavigatorViewUtil.getDefaultInput(this);
	}

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		
	}

	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
}
