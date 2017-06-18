package com.leven.dmd.gef.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnEditWizard;

public class ColumnEditCommand extends Command {

	private Column oldColumn;
	private Column newColumn;
	private Table table;
	private Schema schema;
	private int index;

	public ColumnEditCommand(Column column, Schema schema) {
		super();
		this.oldColumn = column;
		this.table = column.getTable();
		this.schema = schema;
		this.index = table.getColumns().indexOf(oldColumn);
		this.setLabel(Messages.ColumnEditAction_0);
	}

	public void execute() {
		Map<String,Column> map = new HashMap<String,Column>();
		
		ColumnEditWizard wizard = new ColumnEditWizard(schema,map,oldColumn.getTable().getColumnMap(),oldColumn);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),wizard);
		dialog.create();
		if(dialog.open()==Dialog.OK){
			newColumn = map.get(Messages.ColumnEditAction_4);
			table.removeColumn(oldColumn);
			table.addColumn(newColumn, index);
		}
	}

	public void undo() {
		table.removeColumn(newColumn);
		table.addColumn(oldColumn, index);
	}
	
	@Override
	public void redo() {
		table.removeColumn(oldColumn);
		table.addColumn(newColumn, index);
	}
}