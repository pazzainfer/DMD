package com.leven.dmd.gef.command;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

/**
 * ɾ��һ�еĲ�������
 * 
 * @author lifeng 2012-7-11 ����02:37:23
 */
public class ColumnDeleteCommand extends Command {

	private Table table;
	private Column column;
	private int index = -1;
	private int oldStatus;
	
	private boolean finished = false;

	public boolean canExecute() {
		if (table.getColumns().size() > 1) {
			return true;
		}
		return false;
	}

	public void execute() {
		finished = primExecute();
	}

	protected boolean primExecute() {
		for(Iterator<Relationship> it = table.getForeignKeyRelationships().iterator();it.hasNext();){
			if(column.getName().equals(it.next().getPrimaryKeyColumn())){
				MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
				mb.setText(Messages.ColumnDeleteCommand_0);
				mb.setMessage(Messages.ColumnDeleteCommand_1);
				mb.open();
				return false;
			}
		}
		for(Iterator<Relationship> it = table.getPrimaryKeyRelationships().iterator();it.hasNext();){
			if(column.getName().equals(it.next().getPrimaryKeyColumn())){
				MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
				mb.setText(Messages.ColumnDeleteCommand_0);
				mb.setMessage(Messages.ColumnDeleteCommand_1);
				mb.open();
				return false;
			}
		}
		index = table.getColumns().indexOf(column);
		table.removeColumn(column);
		oldStatus = table.getStatus();
		if(table.getStatus()==IEditorConstant.TABLE_STATUS_NORMAL){
			table.setStatus(IEditorConstant.TABLE_STATUS_CHANGED);
		}
		refreshView();
		return true;
	}

	public void redo() {
		if(finished){
			index = table.getColumns().indexOf(column);
			table.removeColumn(column);
			oldStatus = table.getStatus();
			if(table.getStatus()==IEditorConstant.TABLE_STATUS_NORMAL){
				table.setStatus(IEditorConstant.TABLE_STATUS_CHANGED);
			}
			refreshView();
		}
	}

	public void setTable(Table ta) {
		table = ta;
	}

	public void setColumn(Column co) {
		column = co;
	}

	public void undo() {
		if(finished){
			table.addColumn(column, index);
			table.modifyStatus(oldStatus);
			refreshView();
		}
	}

	private void refreshView(){
		NavigatorViewUtil.refresh(table);
	}
}