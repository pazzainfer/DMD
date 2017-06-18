package com.leven.dmd.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.leven.dmd.gef.command.ColumnAddCommand;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.part.TablePart;

public class TableContainerEditPolicy extends ContainerEditPolicy {

	protected Command getCreateCommand(CreateRequest request) {
		Object newObject = request.getNewObject();
		if (!(newObject instanceof Column)) {
			return null;
		}

		TablePart tablePart = (TablePart) getHost();
		Table table = tablePart.getTable();
		Column column = (Column) newObject;
		ColumnAddCommand command = new ColumnAddCommand();
		command.setTable(table);
		command.setColumn(column);
		return command;
	}

}