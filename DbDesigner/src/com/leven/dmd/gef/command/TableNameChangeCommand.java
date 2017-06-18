package com.leven.dmd.gef.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

/**
 * �޸ı���������
 * 
 * @author lifeng 2012-7-11 ����02:31:14
 */
public class TableNameChangeCommand extends Command {

	private Table table;
	private String name, oldName;

	public TableNameChangeCommand() {
		super();
		this.setLabel(Messages.TableNameChangeCommand_0);
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
		refreshView();
	}
	
	private void refreshView(){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
				if(registry!=null && registry instanceof SchemaNavigatorView){
					SchemaNavigatorView view = (SchemaNavigatorView)registry;
					view.getCommonViewer().refresh(table);
				}
				((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}