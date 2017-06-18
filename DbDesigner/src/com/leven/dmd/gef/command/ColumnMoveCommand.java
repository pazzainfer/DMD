package com.leven.dmd.gef.command;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;

/**
 * ����ͬһ���е�λ�ñ������
 * 
 * @author lifeng 2012-7-11 ����02:34:44
 */
public class ColumnMoveCommand extends Command {

	private int oldIndex, newIndex;
	private Column childColumn;
	private Table parentTable;

	public ColumnMoveCommand() {
		super();
		this.setLabel(Messages.ColumnMoveCommand_0);
	}

	public ColumnMoveCommand(Column child, Table parent, int oldIndex,
			int newIndex) {
		this.childColumn = child;
		this.parentTable = parent;
		this.oldIndex = oldIndex;
		this.newIndex = newIndex;
		if (newIndex > oldIndex)
			newIndex--; // �������ǰ�ȱ�ɾ��
	}

	public void execute() {
		parentTable.switchColumn(childColumn, newIndex);
	}

	public void undo() {
		parentTable.switchColumn(childColumn, oldIndex);
	}

}