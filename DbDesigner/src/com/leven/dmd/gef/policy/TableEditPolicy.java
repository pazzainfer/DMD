package com.leven.dmd.gef.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leven.dmd.gef.command.TableDeleteCommand;
import com.leven.dmd.gef.command.TablePackageDeleteCommand;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.part.TablePackageTreeEditPart;
import com.leven.dmd.gef.part.TablePart;
import com.leven.dmd.gef.part.TableTreeEditPart;

public class TableEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest request) {
		Object obj = getHost();
		if(obj instanceof TablePackageTreeEditPart){
			TablePackageTreeEditPart tablePart = (TablePackageTreeEditPart)obj ;
			Rectangle bounds = ((TablePackage)tablePart.getModel()).getBounds().getCopy();
			Schema parent = (Schema) (tablePart.getParent().getParent().getModel());
			TablePackageDeleteCommand deleteCmd = new TablePackageDeleteCommand();
			deleteCmd.setSchema(parent);
			deleteCmd.setTablePackage((TablePackage) (tablePart.getModel()));
			return deleteCmd;

		}else if(obj instanceof TableTreeEditPart){
			TableTreeEditPart tablePart = (TableTreeEditPart)obj ;
			Rectangle bounds = ((Table) (tablePart.getModel())).getBounds().getCopy();
			Schema parent = (Schema) (tablePart.getParent().getParent().getModel());
			TableDeleteCommand deleteCmd = new TableDeleteCommand();
			deleteCmd.setSchema(parent);
			deleteCmd.setTable((Table) (tablePart.getModel()));
			deleteCmd.setOriginalBounds(bounds);
			return deleteCmd;
		}else if(obj instanceof TablePart){
			TablePart tablePart = (TablePart)obj ;
			Rectangle bounds = tablePart.getFigure().getBounds().getCopy();
			Schema parent = (Schema) (tablePart.getParent().getModel());
			TableDeleteCommand deleteCmd = new TableDeleteCommand();
			deleteCmd.setSchema(parent);
			deleteCmd.setTable((Table) (tablePart.getModel()));
			deleteCmd.setOriginalBounds(bounds);
			return deleteCmd;
		} else {
			return null;
		}
	}

}