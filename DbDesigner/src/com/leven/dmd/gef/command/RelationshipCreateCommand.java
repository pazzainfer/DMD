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
 * ���һ�������ϵ
 * @author lifeng
 * 2012-7-11 ����02:48:09
 */
public class RelationshipCreateCommand extends Command {

	protected Relationship relationship;
	protected Table foreignTable;
	protected Table primaryTable;
	
	private boolean finished = false;

	public RelationshipCreateCommand() {
		super(Messages.RelationshipCreateCommand_0);
	}

	public boolean canExecute() {

		boolean returnValue = true;
		if (foreignTable.equals(primaryTable)) {
			returnValue = false;
		} else {
			if (primaryTable == null) {
				return false;
			} else {
				List<Relationship> relationships = primaryTable.getPrimaryKeyRelationships();
				for (int i = 0; i < relationships.size(); i++) {
					Relationship currentRelationship = relationships.get(i);
					if (currentRelationship.getForeignKeyTable().equals(
							foreignTable)) {
						returnValue = false;
						break;
					}
				}
			}
		}
		return returnValue;
	}

	public void execute() {
		Map<String,String> columnMap = new HashMap<String,String>();
		WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(),new RelationshipAddWizard(
				primaryTable,foreignTable,columnMap));
		wd.create();
		if(wd.open()!=1){
			relationship = new Relationship(foreignTable, primaryTable,
					columnMap.get("target"),columnMap.get("source")); //$NON-NLS-1$ //$NON-NLS-2$
			finished = true;
		}else {
			finished = false;
		}
	}

	public Table getForeignTable() {
		return foreignTable;
	}

	public Table getPrimaryTable() {
		return primaryTable;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public void redo() {
		foreignTable.addForeignKeyRelationship(relationship);
		primaryTable.addPrimaryKeyRelationship(relationship);
	}

	public void setForeignTable(Table foreignTable) {
		this.foreignTable = foreignTable;
	}

	public void setPrimaryTable(Table primaryTable) {
		this.primaryTable = primaryTable;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	public void undo() {
		if(finished){
			foreignTable.removeForeignKeyRelationship(relationship);
			primaryTable.removePrimaryKeyRelationship(relationship);
		}
	}

}