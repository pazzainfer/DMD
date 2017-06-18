package com.leven.dmd.gef.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Table;

/**
 * �޸ı���������
 * 
 * @author lifeng 2012-7-11 ����02:31:14
 */
public class ChangeTableNameCommand extends Command {

	private Table table;
	private String name, oldName;

	public ChangeTableNameCommand() {
		super();
		this.setLabel(Messages.ChangeTableNameCommand_0);
	}

	/**
	 * ִ�з���:�жϻ������Ƿ�������޸ĺ������ı�����У���������
	 */
	public void execute() {
		List<Table> tableList = table.getSchema().getTables();
		Table tableTemp;
		for (Iterator<Table> it = tableList.iterator(); it.hasNext();) {
			tableTemp = it.next();
			if (tableTemp.getCnName().trim().equals(name.trim())) {
				return;
			}
		}
		table.modifyCnName(name);
	}

	public boolean canExecute() {
		if (name != null) {
			return true;
		} else {
			name = oldName;
			return false;
		}
	}

	public void setName(String string) {
		this.name = string;
	}

	public void setOldName(String string) {
		oldName = string;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * ��������
	 */
	public void undo() {
		table.modifyCnName(oldName);
	}

}