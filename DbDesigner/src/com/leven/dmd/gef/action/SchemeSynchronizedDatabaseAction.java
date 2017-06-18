package com.leven.dmd.gef.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

/**
 * ��������ͬ������ݿ�Action
 * @author lifeng
 * 2012-7-18 ����03:26:36
 */
public class SchemeSynchronizedDatabaseAction extends Action {

	IEditorPart editor;
	boolean checked;

	public SchemeSynchronizedDatabaseAction(IEditorPart editor) {
		super(Messages.SchemeSynchronizedDatabaseAction_0, Action.AS_PUSH_BUTTON);
		this.editor = editor;
	}

	public void run() {
		if (editor instanceof SchemaDiagramEditor) {
			SchemaDiagramEditor schemaEditor = (SchemaDiagramEditor) editor;
			Schema schema = schemaEditor.getSchema();
			DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
			dialog.create();
			if(dialog.open()!=Dialog.OK){
				return;
			}
			DBConfigModel config = dialog.getDbConfigModel();
			if(!DBConfigUtil.isConfigAvailable(config)){
				 MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR);
				 mb.setText(Messages.SchemeSynchronizedDatabaseAction_1);
				 mb.setMessage(Messages.SchemeSynchronizedDatabaseAction_2);
				 mb.open();
				 return;
			 }
			if(schema!=null){
				StringBuffer failedTable = new StringBuffer();
				if(!schema.isPackageOpen()){
					 List<Table> tables = schema.getTables();
					 for(int i=0;i<tables.size();i++){
							 if(DbTableUtil.CheckTableExist(tables.get(i),config)){
									MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											SWT.ICON_QUESTION | SWT.YES | SWT.NO);
									mb.setText(Messages.SchemeSynchronizedDatabaseAction_1);
									mb.setMessage(Messages.SchemeSynchronizedDatabaseAction_4+tables.get(i).getName()+Messages.SchemeSynchronizedDatabaseAction_5);
							
									if(mb.open()==SWT.YES){
										if(DbTableUtil.createTableByDomian(tables.get(i),true,config)){
											tables.get(i).modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
										}else {
											failedTable.append(tables.get(i).getName() + ","); //$NON-NLS-1$
										}
									}
							 }else{
								 if(DbTableUtil.createTableByDomian(tables.get(i),false,config)){
										tables.get(i).modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
									}else {
										failedTable.append(tables.get(i).getName() + ","); //$NON-NLS-1$
									}
							 }
					 }
					 TablePackage tablePackage;
					 for(int i=0;i<schema.getTablePackages().size();i++){
						 tablePackage = schema.getTablePackages().get(i);
						 failedTable.append(createPackageTables(tablePackage,config));
					 }
				}else {
					 TablePackage tablePackage = schema.getOpenPackage();
					 failedTable.append(createPackageTables(tablePackage,config));
				}
				if(failedTable.length()>0){
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING);
					 mb.setText(Messages.SchemeSynchronizedDatabaseAction_1);
					 mb.setMessage(Messages.SchemeSynchronizedDatabaseAction_7+failedTable.substring(0, failedTable.length()-2));
					 mb.open();
				}else {
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION);
					 mb.setText(Messages.SchemeSynchronizedDatabaseAction_8);
					 mb.setMessage(Messages.SchemeSynchronizedDatabaseAction_9);
					 mb.open();
				}
			}
		}
	}
	/**
	 * 递归将主题下面的所有表重建
	 * @param tablePackage
	 * @param config
	 */
	private String createPackageTables(TablePackage tablePackage,DBConfigModel config){
		StringBuffer failedTable = new StringBuffer();
		List<Table> tables = tablePackage.getTables();
		 for(int i=0;i<tables.size();i++){
				 if(DbTableUtil.CheckTableExist(tables.get(i),config)){
						MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						mb.setText(Messages.SchemeSynchronizedDatabaseAction_1);
						mb.setMessage(Messages.SchemeSynchronizedDatabaseAction_4+tables.get(i).getName()+Messages.SchemeSynchronizedDatabaseAction_5);
				
						if(mb.open()==SWT.YES){
							if(DbTableUtil.createTableByDomian(tables.get(i),true,config)){
								tables.get(i).modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
							}else {
								failedTable.append(tables.get(i).getName() + ","); //$NON-NLS-1$
							}
						}
				 }else{
					 if(DbTableUtil.createTableByDomian(tables.get(i),false,config)){
							tables.get(i).modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
						}else {
							failedTable.append(tables.get(i).getName() + ","); //$NON-NLS-1$
						}
				 }
		 }
		 
		 List<TablePackage> tablePackages = tablePackage.getTablePackages();
		 for(TablePackage child : tablePackages){
			 failedTable.append(createPackageTables(child ,config));
		 }
		 return failedTable.toString();
	}
}