package com.leven.dmd.gef.command;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
/**
 * ��ӱ���󵽻�ͼ���������
 * 
 * @author lifeng 2012-7-11 ����02:50:14
 */
public class TablePasteCommand extends Command {

	private Schema schema;
	private Table table;
	private TablePackage tablePackage;

	public TablePasteCommand(Schema schema,Table table) {
		super(Messages.TablePasteCommand_0);
		this.schema = schema;
		this.table = table;
	}

	public void execute() {
		if(schema.isPackageOpen() && 
				(tablePackage=schema.getOpenPackage())!=null){
			tablePackage.addTable(table);
		}
		this.table.setSchema(schema);
		schema.addTable(table);
		refreshView();
	}
	
	public void undo() {
		schema.removeTable(table);
		if(schema.isPackageOpen() && tablePackage!=null){
			tablePackage.removeTable(table);
		}
		refreshView();
	}
	private void refreshView(){
		Object parent;
		if((parent=table.getParent()) instanceof Schema){
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
		}else {
			NavigatorViewUtil.refresh(parent);
		}
	}
	
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public TablePackage getTablePackage() {
		return tablePackage;
	}

	public void setTablePackage(TablePackage tablePackage) {
		this.tablePackage = tablePackage;
	}

	public Schema getSchema() {
		return schema;
	}

	public Table getTable() {
		return table;
	}
}