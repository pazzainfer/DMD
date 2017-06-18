package com.leven.dmd.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leven.dmd.gef.command.ColumnDeleteCommand;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;

public class ColumnEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest request) {
		Table parent = (Table) (getHost().getParent().getModel());
		ColumnDeleteCommand deleteCmd = new ColumnDeleteCommand();
		deleteCmd.setTable(parent);
		deleteCmd.setColumn((Column) (getHost().getModel()));
		return deleteCmd;
	}
}