package com.leven.dmd.gef.command;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Table;

/**
 * ɾ��������߹�ϵ�Ĳ�������
 * 
 * @author lifeng 2012-7-11 ����02:38:07
 */
public class RelationshipDeleteCommand extends Command {

	private Table foreignKeySource;
	private Table primaryKeyTarget;
	private Relationship relationship;

	public RelationshipDeleteCommand() {
		super();
		this.setLabel(Messages.RelationshipDeleteCommand_0);
	}

	public RelationshipDeleteCommand(Table foreignKeySource,
			Table primaryKeyTarget, Relationship relationship) {
		super();
		this.foreignKeySource = foreignKeySource;
		this.primaryKeyTarget = primaryKeyTarget;
		this.relationship = relationship;
	}

	public void execute() {
		foreignKeySource.removeForeignKeyRelationship(relationship);
		primaryKeyTarget.removePrimaryKeyRelationship(relationship);
		relationship.setForeignKeyTable(null);
		relationship.setPrimaryKeyTable(null);
	}

	public void undo() {
		relationship.setForeignKeyTable(foreignKeySource);
		relationship.setForeignKeyTable(primaryKeyTarget);
		foreignKeySource.addForeignKeyRelationship(relationship);
		primaryKeyTarget.addPrimaryKeyRelationship(relationship);
	}

}