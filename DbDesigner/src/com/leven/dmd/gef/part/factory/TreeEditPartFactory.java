package com.leven.dmd.gef.part.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.SchemaTreeNode;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.part.SchemaTreeEditPart;
import com.leven.dmd.gef.part.SchemaTreeNodeEditPart;
import com.leven.dmd.gef.part.TablePackageTreeEditPart;
import com.leven.dmd.gef.part.TableTreeEditPart;

public class TreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if(model instanceof Schema){
			part = new SchemaTreeEditPart();
		} else if(model instanceof Table){
			part = new TableTreeEditPart();
		} else if(model instanceof TablePackage){
			part = new TablePackageTreeEditPart();
		} else if(model instanceof SchemaTreeNode){
			part = new SchemaTreeNodeEditPart();
		}
		
		if(part != null){
			part.setModel(model);
		}
		return part;
	}

}
