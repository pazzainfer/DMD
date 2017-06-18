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
 * ��������-�������(��ͷ���׶�)�������
 * 
 * @author lifeng 2012-7-11 ����02:40:35
 */
public class RelationshipReconnectForeignKeyCommand extends Command {

	/** �µ�������� */
	protected Table sourceTable;
	/** �������� */
	protected Table targetTable;
	/** ��ϵ���� */
	protected Relationship relationship;
	/** �޸�ǰ����� */
	protected Table oldSourceTable;
	
	private Relationship newRelationship;
	private Relationship oldRelationship;
	
	private boolean finished = false;
	
	
	public RelationshipReconnectForeignKeyCommand() {
		super(Messages.RelationshipReconnectForeignKeyCommand_0);
	}

	/**
	 * ����Ƿ������������ӵ��������߹�ϵ�Ǹ��Ѵ��ڵĹ�ϵ
	 */
	public boolean canExecute() {

		boolean returnVal = true;

		Table primaryKeyTable = relationship.getPrimaryKeyTable();

		// �ж��Ƿ����ӵ�����
		if (primaryKeyTable.equals(sourceTable)) {
			returnVal = false;
		} else {
			List relationships = sourceTable.getForeignKeyRelationships();
			for (int i = 0; i < relationships.size(); i++) {
				Relationship relationship = ((Relationship) (relationships
						.get(i)));
				if (relationship.getPrimaryKeyTable().equals(targetTable)
						&& relationship.getForeignKeyTable().equals(
								sourceTable)) {
					returnVal = false;
					break;
				}
			}
		}
		return returnVal;
	}

	public void execute() {
		if (sourceTable != null) {
			Map<String,String> columnMap = new HashMap<String,String>();
			WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(),new RelationshipAddWizard(
					sourceTable,targetTable,columnMap));
			wd.create();
			if(wd.open()==0){
				finished = true;
				oldRelationship = relationship;
				newRelationship = new Relationship(relationship);
				newRelationship.setForeignKeyTable(sourceTable);
				newRelationship.setForeignKeyColumn(columnMap.get("target")); //$NON-NLS-1$
				newRelationship.setPrimaryKeyColumn(columnMap.get("source")); //$NON-NLS-1$
				relationship.Reconnection(newRelationship);
				/*oldSourceTable.removeForeignKeyRelationship(relationship);
				relationship.setForeignKeyTable(sourceTable);
				relationship.setForeignKeyColumn(columnMap.get("foreignColumn"));
				relationship.setPrimaryKeyColumn(columnMap.get("primaryColumn"));
				sourceTable.addForeignKeyRelationship(relationship);*/
			}else{
				finished = false;
			}
		}
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
		targetTable = relationship.getPrimaryKeyTable();
		oldSourceTable = relationship.getForeignKeyTable();
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
	public Table getOldSourceTable() {
		return oldSourceTable;
	}
	public void setOldSourceTable(Table oldSourceTable) {
		this.oldSourceTable = oldSourceTable;
	}
	public Relationship getRelationship() {
		return relationship;
	}
}