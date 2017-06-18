package com.leven.dmd.gef.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;

/**
 * �������������ֶ����͵Ĳ�������
 * 
 * @author lifeng 2012-7-11 ����02:49:22
 */
public class ColumnResetNameTypeCommand extends Command {

	private Column source;
	private String name, oldName;
	private String type, oldType;
	private String length,oldLength;

	private Table table;

	public ColumnResetNameTypeCommand(Table parent) {
		super(Messages.ColumnResetNameTypeCommand_0);
		table = parent;
	}

	public void execute() {
		List<Column> columnList = table.getColumns();
		Column columnTemp;
		for (Iterator<Column> it = columnList.iterator(); it.hasNext();) {
			columnTemp = it.next();
			if (columnTemp.getCnName().trim().equals(name.trim())) {
				return;
			}
		}
		source.setCnName(name);
		source.setType(type);
		source.setLength(length.equals("")?0:Integer.parseInt(length)); //$NON-NLS-1$
	}

	public boolean canExecute() {
		if (name != null && type != null) {
			return true;
		} else {
			name = oldName;
			type = oldType;
			length = oldLength;
			return false;
		}
	}

	/**
	 * Sets the new Column name
	 * 
	 * @param string
	 *            the new name
	 */
	public void setNameType(String string) {
		String oldName = this.name;
		String oldType = this.type;

		if (string != null) {
			int colonIndex = string.indexOf(':');
			if (colonIndex >= 0) {
				name = string.substring(0, colonIndex);
				if (string.length() > colonIndex + 1) {
					String types[] = string.substring(colonIndex + 1).split("("); //$NON-NLS-1$
					this.type = types[0];
					this.length = types[1].split("")[0]; //$NON-NLS-1$
				}
			}
		}
		if (this.type == null) {
			this.name = oldName;
			this.type = oldType;
			this.length = oldLength;
		}
	}

	public void setOldName(String string) {
		oldName = string;
	}

	public void setSource(Column column) {
		source = column;
	}

	public void undo() {
		source.setName(oldName);
		source.setType(oldType);
		source.setLength(oldLength.equals("")?0:Integer.parseInt(oldLength)); //$NON-NLS-1$
	}

	public void setOldType(String type) {
		this.oldType = type;
	}

}