package com.leven.dmd.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leven.dmd.gef.command.RelationshipDeleteCommand;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Table;

public class RelationshipEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest request) {
		Relationship relationship = (Relationship) getHost().getModel();
		Table primaryKeyTarget = relationship.getPrimaryKeyTable();
		Table foreignKeySource = relationship.getForeignKeyTable();
		RelationshipDeleteCommand deleteCmd = new RelationshipDeleteCommand(
				foreignKeySource, primaryKeyTarget, relationship);
		return deleteCmd;
	}

}