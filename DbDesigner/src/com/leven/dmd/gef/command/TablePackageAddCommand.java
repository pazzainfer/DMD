package com.leven.dmd.gef.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
/**
 * 主题添加
 * 
 * @author lifeng 
 * 2012-7-11 02:50:14
 */
public class TablePackageAddCommand extends Command {

	private Schema schema;
	private TablePackage tablePackage;

	public TablePackageAddCommand() {
		super(Messages.TablePackageAddCommand_0);
	}

	public void execute() {
		List<TablePackage> packageList = schema.getTablePackages();
		if(tablePackage == null || tablePackage.getName() == null){
			tablePackage = new TablePackage("Package" + packageList.size(), schema); //$NON-NLS-1$
			schema.addTablePackage(tablePackage);
		}else {
			if(!packageList.contains(tablePackage)){
				schema.addTablePackage(tablePackage);
			}
		}
		refreshView();
	}
	
	
	@Override
	public boolean canExecute() {
		if(schema.isPackageOpen() && 
				(tablePackage=schema.getOpenPackage())!=null){
			return false;
		}
		return super.canExecute();
	}

	public void undo() {
		schema.removeTablePackage(tablePackage);
		refreshView();
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	
	public void setTablePackage(TablePackage table) {
		this.tablePackage = table;
	}
	
	private void refreshView(){
		NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_PACKAGE);
	}
}