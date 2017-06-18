package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;

import com.leven.dmd.gef.model.SchemaTreeNode;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.policy.TableEditPolicy;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class SchemaTreeNodeEditPart extends CustomTreeEditPart {

	@Override
	protected List getModelChildren() {
		SchemaTreeNode model = (SchemaTreeNode) getModel();
		List list = new ArrayList();
		if(model.hasChildren()){
			return model.getChildren();
		}
		return list;
	}

	public SchemaTreeNodeEditPart() {
		super();
	}
	
	protected void refreshVisuals() {
		SchemaTreeNode model = (SchemaTreeNode) getModel();
		setWidgetText(model.getName());
		int type = model.getNodeType();
		
		if(type==IEditorConstant.TREE_NODE_TYPE_SCHEMA){
			setWidgetImage(ImageHelper.getImage(ImageKeys.SCHEMA));
		}else if(type==IEditorConstant.TREE_NODE_TYPE_TABLES){
			setWidgetImage(ImageHelper.getImage(ImageKeys.TABLE_FOLDER));
		}else if(type==IEditorConstant.TREE_NODE_TYPE_PACKAGES){
			setWidgetImage(ImageHelper.getImage(ImageKeys.PACKAGE));
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableEditPolicy());
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Table.NAME))
			refreshChildren();
	}
	
}
