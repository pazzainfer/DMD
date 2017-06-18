package com.leven.dmd.gef.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Table;

/**
 * Ϊ�������еĲ�������
 * 
 * @author lifeng 2012-7-11 ����02:33:53
 */
public class ColumnCreateCommand extends Command {

	private Column column;
	private Table table;
	private int oldStatus;

	public ColumnCreateCommand() {
		super();
		this.setLabel(Messages.ColumnCreateCommand_0);
	}

	public void setColumn(Column column) {
		this.column = column;
		this.column.setName("COLUMN " + (table.getColumns().size() + 1)); //$NON-NLS-1$
		this.column.setType(ColumnType.VARCHAR.getType());
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * ִ�з���:�жϸ������Ƿ��������������������У�����У���������
	 */
	public void execute() {
		List<Column> columnList = table.getColumns();
		Column columnTemp;
		for (Iterator<Column> it = columnList.iterator(); it.hasNext();) {
			columnTemp = it.next();
			if (columnTemp.getName().equals(column.getName())) {
				return;
			}
		}
		oldStatus = table.getStatus();
		table.addColumn(column);
	}

	/**
	 * ��������
	 */
	public void undo() {
		table.removeColumn(column);
		table.modifyStatus(oldStatus);
	}

}