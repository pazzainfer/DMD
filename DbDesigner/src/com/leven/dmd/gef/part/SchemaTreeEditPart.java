package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.SchemaTreeNode;
import com.leven.dmd.gef.util.IEditorConstant;

public class SchemaTreeEditPart extends CustomTreeEditPart {

	// override
	protected List getModelChildren() {
		Schema schema = (Schema) getModel();
		List list = new ArrayList();
		
		SchemaTreeNode tables = new SchemaTreeNode(Messages.SchemaTreeEditPart_0,IEditorConstant.TREE_NODE_TYPE_TABLES);
		if(schema.isPackageOpen()){
			tables.setChildren(schema.getOpenPackage().getTables());
		}else {
			tables.setChildren(schema.getTables());
		}
		list.add(tables);
		SchemaTreeNode packages = new SchemaTreeNode(Messages.SchemaTreeEditPart_1,IEditorConstant.TREE_NODE_TYPE_PACKAGES);
		if(schema.isPackageOpen()){
			packages.setChildren(schema.getOpenPackage().getTablePackages());
		}else {
			packages.setChildren(schema.getTablePackages());
		}
		list.add(packages);
		return list;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Schema.CHILD)){
			this.refreshChildren();
			this.refreshVisuals();
		}
	}
}
