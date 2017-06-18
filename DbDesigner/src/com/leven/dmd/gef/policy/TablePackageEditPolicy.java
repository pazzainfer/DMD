package com.leven.dmd.gef.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leven.dmd.gef.command.TablePackageDeleteCommand;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.part.TablePackagePart;

public class TablePackageEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		TablePackagePart part = (TablePackagePart)getHost();
		Rectangle bounds = part.getFigure().getBounds().getCopy();
		Schema parent = (Schema)part.getParent().getModel();
		TablePackageDeleteCommand command = new TablePackageDeleteCommand();
		command.setSchema(parent);
		command.setTablePackage(part.getModel());
		return command;
	}

}
