package com.leven.dmd.gef.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnAddWizard;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

/**
 * Ϊ�������еĲ�������
 * 
 * @author lifeng 2012-7-11 ����02:33:53
 */
public class ColumnAddCommand extends Command {

	private Column column;
	private Table table;
	private int oldStatus;
	private boolean success = false;

	public ColumnAddCommand() {
		super();
		this.setLabel(Messages.ColumnAddCommand_0);
	}

	public void setColumn(Column column) {
		this.column = column;
		this.column.setName("COLUMN" + (table.getColumns().size() + 1)); //$NON-NLS-1$
		this.column.setType(ColumnType.VARCHAR.getType());
		this.column.setLength(10);
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * ִ�з���:�жϸ������Ƿ��������������������У�����У���������
	 */
	public void execute() {
		Map<String,Column> map = new HashMap<String,Column>();
		
		ColumnAddWizard wizard = new ColumnAddWizard(table.getSchema(),map,table.getColumnMap());
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),wizard);
		dialog.create();
		if(dialog.open()==Dialog.OK){
			this.column = map.get("data"); //$NON-NLS-1$
			if(column==null){
				success = false;
				return;
			}
			List<Column> columnList = table.getColumns();
			Column columnTemp;
			for (Iterator<Column> it = columnList.iterator(); it.hasNext();) {
				columnTemp = it.next();
				if (columnTemp.getName().equals(column.getName())) {
					success = false;
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.ColumnAddCommand_3);
					mb.setMessage(Messages.ColumnAddCommand_4+columnTemp.getName()+Messages.ColumnAddCommand_5);
					mb.open();
					return;
				}
			}
			oldStatus = table.getStatus();
			column.setTable(table);
			table.addColumn(column);
			success = true;
			refreshView();
		}else {
			success = false;
			this.column = null;
		}
	}

	/**
	 * ��������
	 */
	public void undo() {
		if(success){
			table.removeColumn(column);
			table.modifyStatus(oldStatus);
			refreshView();
		}
	}
	
	private void refreshView(){
		NavigatorViewUtil.refresh(table);
	}

}