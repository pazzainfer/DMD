package com.leven.dmd.gef.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

/**
 * �޸ı���������
 * 
 * @author lifeng 2012-7-11 ����02:31:14
 */
public class TablePackageNameChangeCommand extends Command {

	private TablePackage table;
	private String name, oldName;

	public TablePackageNameChangeCommand() {
		super();
		this.setLabel(Messages.TablePackageNameChangeCommand_0);
	}

	/**
	 * ִ�з���:�жϻ������Ƿ�������޸ĺ������ı�����У���������
	 */
	public void execute() {
		List<TablePackage> tableList = table.getSchema().getTablePackages();
		TablePackage tableTemp;
		for (Iterator<TablePackage> it = tableList.iterator(); it.hasNext();) {
			tableTemp = it.next();
			if (tableTemp.getName().trim().equals(name.trim())) {
				return;
			}
		}
		table.modifyName(name);
		refreshView();
	}

	public boolean canExecute() {
		if (name != null) {
			return true;
		} else {
			name = oldName;
			return false;
		}
	}

	private void refreshView(){
		NavigatorViewUtil.refresh(table);
	}
	public void setName(String string) {
		this.name = string;
	}

	public void setOldName(String string) {
		oldName = string;
	}

	public void setTablePackage(TablePackage table) {
		this.table = table;
	}

	/**
	 * ��������
	 */
	public void undo() {
		table.modifyName(oldName);
		refreshView();
	}

}