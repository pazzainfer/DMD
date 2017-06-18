package com.leven.dmd.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.leven.dmd.gef.command.RelationshipCreateCommand;
import com.leven.dmd.gef.command.RelationshipReconnectForeignKeyCommand;
import com.leven.dmd.gef.command.RelationshipReconnectPrimaryKeyCommand;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.part.TablePart;

public class TableNodeEditPolicy extends GraphicalNodeEditPolicy {

	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		RelationshipCreateCommand cmd = new RelationshipCreateCommand();
		TablePart part = (TablePart) getHost();
		cmd.setForeignTable(part.getTable());
		request.setStartCommand(cmd);
		return cmd;
	}

	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		RelationshipCreateCommand cmd = (RelationshipCreateCommand) request
				.getStartCommand();
		TablePart part = (TablePart) request.getTargetEditPart();
		cmd.setPrimaryTable(part.getTable());
		return cmd;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getReconnectSourceCommand(ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {

		RelationshipReconnectForeignKeyCommand cmd = new RelationshipReconnectForeignKeyCommand();
		cmd.setRelationship((Relationship) request.getConnectionEditPart()
				.getModel());
		TablePart tablePart = (TablePart) getHost();
		cmd.setSourceTable(tablePart.getTable());
		return cmd;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getReconnectTargetCommand(ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		RelationshipReconnectPrimaryKeyCommand cmd = new RelationshipReconnectPrimaryKeyCommand();
		cmd.setRelationship((Relationship) request.getConnectionEditPart()
				.getModel());
		TablePart tablePart = (TablePart) getHost();
		cmd.setTargetTable(tablePart.getTable());
		return cmd;
	}

}