package com.leven.dmd.gef.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.wizard.RelationshipAddWizard;

/**
 * ��������-��������(��ͷ��β��)�������
 * 
 * @author lifeng 2012-7-11 ����02:46:51
 */
public class ReconnectPrimaryKeyCommand extends Command {

	protected Table sourceTable;
	protected Table targetTable;
	protected Relationship relationship;
	protected Table oldTargetTable;

	private Relationship newRelationship;
	private Relationship oldRelationship;
	
	private boolean finished = false;
	
	public ReconnectPrimaryKeyCommand() {
		super(Messages.ReconnectPrimaryKeyCommand_0);
	}

	public boolean canExecute() {
		boolean returnVal = true;
		Table foreignKeyTable = relationship.getForeignKeyTable();
		if (foreignKeyTable.equals(targetTable)) {
			returnVal = false;
		} else {
			List relationships = targetTable.getPrimaryKeyRelationships();
			for (int i = 0; i < relationships.size(); i++) {
				Relationship relationship = ((Relationship) (relationships
						.get(i)));

				if (relationship.getForeignKeyTable().equals(sourceTable)
						&& relationship.getPrimaryKeyTable().equals(
								targetTable)) {
					returnVal = false;
					break;
				}
			}
		}
		return returnVal;
	}

	public void execute() {
		if (targetTable != null) {
			Map<String,String> columnMap = new HashMap<String,String>();
			WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(),new RelationshipAddWizard(
					sourceTable,targetTable,columnMap));
			wd.create();
			if(wd.open()==0){
				finished = true;
				oldRelationship = relationship;
				newRelationship = new Relationship(relationship);
				
				newRelationship.setPrimaryKeyTable(targetTable);
				newRelationship.setForeignKeyColumn(columnMap.get("source")); //$NON-NLS-1$
				newRelationship.setPrimaryKeyColumn(columnMap.get("target")); //$NON-NLS-1$

				relationship.Reconnection(newRelationship);
			}
		}
	}


	public void undo() {
		if(finished){
			newRelationship.Reconnection(oldRelationship);
		}
	}
	public void redo() {
		if(finished){
			oldRelationship.Reconnection(newRelationship);
		}
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
		oldTargetTable = relationship.getPrimaryKeyTable();
		sourceTable = relationship.getForeignKeyTable();
	}
	public Relationship getRelationship() {
		return relationship;
	}
	public Table getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(Table sourceTable) {
		this.sourceTable = sourceTable;
	}
	public Table getTargetTable() {
		return targetTable;
	}
	public void setTargetTable(Table targetTable) {
		this.targetTable = targetTable;
	}
	public Table getOldTargetTable() {
		return oldTargetTable;
	}
	public void setOldTargetTable(Table oldTargetTable) {
		this.oldTargetTable = oldTargetTable;
	}
}