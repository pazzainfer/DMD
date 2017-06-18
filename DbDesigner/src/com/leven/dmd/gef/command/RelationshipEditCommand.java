package com.leven.dmd.gef.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.part.RelationshipPart;
import com.leven.dmd.gef.wizard.RelationshipEditWizard;

/**
 * �༭�������ϵ
 * @author lifeng
 * 2012-7-11 ����02:48:09
 */
public class RelationshipEditCommand extends Command {

	protected Relationship relationship;

	private Map<String,String> columnMap = new HashMap<String,String>();
	private RelationshipPart relationshipPart;
	private boolean finished = false;

	public RelationshipEditCommand(Relationship relationship,RelationshipPart relationshipPart) {
		super(Messages.RelationshipEditCommand_0);
		this.relationship = relationship;
		this.relationshipPart = relationshipPart;
	}

	public boolean canExecute() {
		return true;
	}

	public void execute() {

		columnMap.put("target",  //$NON-NLS-1$
				relationship.getForeignKeyColumn()==null?"":relationship.getForeignKeyColumn()); //$NON-NLS-1$
		columnMap.put("source",  //$NON-NLS-1$
				relationship.getPrimaryKeyColumn()==null?"":relationship.getPrimaryKeyColumn()); //$NON-NLS-1$
		WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(),new RelationshipEditWizard(
				relationship.getPrimaryKeyTable(),relationship.getForeignKeyTable(),columnMap));
		wd.create();
		if(wd.open()!=1){
			finished = true;
			columnMap.put("oldTarget",relationship.getForeignKeyColumn()); //$NON-NLS-1$
			columnMap.put("oldSource",relationship.getPrimaryKeyColumn()); //$NON-NLS-1$
			relationship.setForeignKeyColumn(columnMap.get("target")); //$NON-NLS-1$
			relationship.setPrimaryKeyColumn(columnMap.get("source")); //$NON-NLS-1$
			relationshipPart.refreshStartLabel(columnMap.get("source")); //$NON-NLS-1$
			relationshipPart.refreshEndLabel(columnMap.get("target")); //$NON-NLS-1$
		}else {
			finished = false;
		}
	}

	public void redo() {
		if(finished){
			relationship.setForeignKeyColumn(columnMap.get("target")); //$NON-NLS-1$
			relationship.setPrimaryKeyColumn(columnMap.get("source")); //$NON-NLS-1$
			relationshipPart.refreshStartLabel(columnMap.get("source")); //$NON-NLS-1$
			relationshipPart.refreshEndLabel(columnMap.get("target")); //$NON-NLS-1$
		}
	}

	public void undo() {
		if(finished){
			relationship.setForeignKeyColumn(columnMap.get("oldTarget")); //$NON-NLS-1$
			relationship.setPrimaryKeyColumn(columnMap.get("oldSource")); //$NON-NLS-1$
			relationshipPart.refreshStartLabel(columnMap.get("oldSource")); //$NON-NLS-1$
			relationshipPart.refreshEndLabel(columnMap.get("oldTarget")); //$NON-NLS-1$
		}
	}
}