package com.leven.dmd.gef.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class TablePackageDeleteCommand extends Command {

	private TablePackage tablePackage;
	private Schema schema;
	private Object parent;

	public TablePackageDeleteCommand() {
		super();
		this.setLabel(Messages.TablePackageDeleteCommand_0);
	}
	

	public void execute() {
		primExecute();
	}

	protected void primExecute() {
		MessageBox mb1 = new MessageBox(Display.getCurrent().getActiveShell(),SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		mb1.setText(Messages.TablePackageDeleteCommand_0);
		mb1.setMessage(Messages.TablePackageDeleteCommand_1);
		if(mb1.open()==SWT.YES){
			String path = tablePackage.getPath();
			parent = tablePackage.getParent();
			if(parent instanceof TablePackage){
				removePackageChildren(tablePackage, schema);
				((TablePackage)parent).removeTablePackage(tablePackage);
				NavigatorViewUtil.refresh(parent);
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
			}else {
				removePackageChildren(tablePackage, schema);
				schema.removeTablePackage(tablePackage);
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_PACKAGE);
			}
			if(schema.getPackagePath()!=null && schema.getPackagePath().contains(path)){
				schema.commitGoTop();
			}
		}
	}
	/**
	 * 删除主题下面所有表跟包
	 * @param tablePackage2
	 * @param schema2
	 */
	private void removePackageChildren(TablePackage tablePackage2,
			Schema schema2) {
		for(Table child : tablePackage2.getTables()){
			schema2.getAllTables().remove(child);
			schema2.getTablesMap().remove(child.getPath());
		}
		for(TablePackage child : tablePackage2.getTablePackages()){
			removePackageChildren(child,schema2);
			schema2.getTablePackagesMap().remove(child.getPath());
		}
	}


	@Override
	public boolean canUndo() {
		return false;
	}


	public boolean canRedo() {
		return false;
	}

	public void setTablePackage(Object a) {
		tablePackage = (TablePackage)a;
	}
	public void setSchema(Schema sa) {
		schema = sa;
	}
}