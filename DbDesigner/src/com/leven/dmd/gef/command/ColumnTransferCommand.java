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
/**
 * �����ƶ����������еĲ�������
 * 
 * @author lifeng 2012-7-11 ����02:36:39
 */
public class ColumnTransferCommand extends Command {

	private Column columnToMove;
	private Table originalTable;
	private Table newTable;
	private int oldIndex, newIndex;
	private int sourceStatus,targetStatus;

	public ColumnTransferCommand() {
		super();
		this.setLabel(Messages.ColumnTransferCommand_0);
	}

	public ColumnTransferCommand(Column columnToMove,
			Table originalTable, Table newTable, int oldIndex, int newIndex) {
		super();
		this.columnToMove = columnToMove;
		this.originalTable = originalTable;
		this.newTable = newTable;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
		sourceStatus = originalTable.getStatus();
		targetStatus = newTable.getStatus();
	}

	/**
	 * ֻ�и����д���1һ�����ϵ�����ʱ�ſ�ִ��
	 */
	public boolean canExecute() {
		if (originalTable.getColumns().size() > 1){
			for(Iterator<Relationship> it = originalTable.getForeignKeyRelationships().iterator();it.hasNext();){
				if(columnToMove.getName().equals(it.next().getPrimaryKeyColumn())){
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.ColumnTransferCommand_1);
					mb.setMessage(Messages.ColumnTransferCommand_2);
					mb.open();
					return false;
				}
			}
			for(Iterator<Relationship> it = originalTable.getPrimaryKeyRelationships().iterator();it.hasNext();){
				if(columnToMove.getName().equals(it.next().getPrimaryKeyColumn())){
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.ColumnTransferCommand_1);
					mb.setMessage(Messages.ColumnTransferCommand_2);
					mb.open();
					return false;
				}
			}
			for(Iterator<Column> it = newTable.getColumns().iterator();it.hasNext();){
				if(columnToMove.getName().equals(it.next().getName())){
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.ColumnTransferCommand_1);
					mb.setMessage(Messages.ColumnTransferCommand_6+columnToMove.getName()+Messages.ColumnTransferCommand_7);
					mb.open();
					return false;
				}
			}
			return true;
		} else{
			return false;
		}
	}

	public void execute() {
		originalTable.removeColumn(columnToMove);
		newTable.addColumn(columnToMove, newIndex);
		if(originalTable.getStatus()==IEditorConstant.TABLE_STATUS_NORMAL){
			originalTable.setStatus(IEditorConstant.TABLE_STATUS_CHANGED);
		}
		if(newTable.getStatus()==IEditorConstant.TABLE_STATUS_NORMAL){
			newTable.setStatus(IEditorConstant.TABLE_STATUS_CHANGED);
		}
	}

	public void undo() {
		newTable.removeColumn(columnToMove);
		originalTable.addColumn(columnToMove, oldIndex);
		newTable.modifyStatus(sourceStatus);
		originalTable.modifyStatus(targetStatus);
	}

}