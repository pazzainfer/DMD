package com.leven.dmd.gef.command;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.wizard.CustomWizardDialog;
import com.leven.dmd.gef.wizard.TableAddWizard;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;
/**
 * ��ӱ���󵽻�ͼ���������
 * 
 * @author lifeng 2012-7-11 ����02:50:14
 */
public class TableAddCommand extends Command {

	private Schema schema;
	private Table table;
	private TablePackage tablePackage;

	public TableAddCommand() {
		super(Messages.TableAddCommand_0);
	}

	/**
	 * ִ�в���-����֤����ı��Ƿ��Ѵ���(�Ա����Ƿ�����Ϊ׼)
	 */
	public void execute() {
		List<Table> tableList = schema.getTables();
		Table tableTemp;
		for (Iterator<Table> it = tableList.iterator(); it.hasNext();) {
			tableTemp = it.next();
			if (tableTemp.getName().equals(table.getName())) {
				return;
			}
		}
		int result = 1;
		if(table==null){
			return;
		}
		if (table.getName() == null) {// ����ӵ��������ʱ
			this.table.setName("TABLE" + (schema.getTables().size() + 1)); //$NON-NLS-1$
			this.table.setCnName(""); //$NON-NLS-1$
			this.table.setStatus(IEditorConstant.TABLE_STATUS_ADDED);
			if (table.getColumns().size() < 2) {
				Column column1 = new Column("FIELD1", ColumnType.VARCHAR,Messages.TableAddCommand_4,false); //$NON-NLS-1$
				column1.setLength(10);
				table.addColumn(column1);
			}
			
			CustomWizardDialog wd = new CustomWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
					,new TableAddWizard(schema,table),false);
			wd.setPageSize(600,300);
			if((result = wd.open())==1){
				table = null;
			}
		}
		if(table!=null){
			if(result == 2){
				DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
				dialog.create();
				if(dialog.open()!=Dialog.OK){
					return;
				}
				DBConfigModel config = dialog.getDbConfigModel();
				boolean configOk,createOk=true;
				if(!DBConfigUtil.isConfigAvailable(config)){
					configOk = false;
				}else {
					configOk = true;
				}
				if(configOk){
					if(DbTableUtil.CheckTableExist(table,config)){
						MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						mb.setText(Messages.TableAddCommand_5);
						mb.setMessage(Messages.TableAddCommand_6+table.getName()+Messages.TableAddCommand_7);
				
						if(mb.open()==SWT.YES){
							if(DbTableUtil.createTableByDomian(table,false,config)){
								createOk = createOk && true;
								table.setStatus(IEditorConstant.TABLE_STATUS_NORMAL);
							}else {
								createOk = createOk && false;
							}
						}
					}else{
						if(DbTableUtil.createTableByDomian(table,false,config)){
							createOk = createOk && true;
							table.setStatus(IEditorConstant.TABLE_STATUS_NORMAL);
						}else {
							createOk = createOk && false;
						}
					}
				}else {
					table.setStatus(IEditorConstant.TABLE_STATUS_ADDED);
				}
				 if(configOk){
					 if(createOk){
						 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
						 mb.setText(Messages.TableAddCommand_8);
						 mb.setMessage(Messages.TableAddCommand_9);
						 mb.open();
					 }else {
						 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_WARNING);
						 mb.setText(Messages.TableAddCommand_5);
						 mb.setMessage(Messages.TableAddCommand_1);
						 mb.open();
					 }
				 }else {
					 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					 mb.setText(Messages.TableAddCommand_5);
					 mb.setMessage(Messages.TableAddCommand_11);
					 mb.open();
				 }
			}
			if(schema.isPackageOpen() && 
					(tablePackage=schema.getOpenPackage())!=null){
				tablePackage.addTable(table);
			}
			this.table.setSchema(schema);
			schema.addTable(table);
			refreshView();
		}
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
}