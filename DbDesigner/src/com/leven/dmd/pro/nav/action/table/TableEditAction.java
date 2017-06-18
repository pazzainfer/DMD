package com.leven.dmd.pro.nav.action.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.wizard.CustomWizardDialog;
import com.leven.dmd.gef.wizard.TableEditWizard;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

public class TableEditAction extends Action {
	private Object obj;
	
	public TableEditAction(Object obj) {
		super();
		this.setText(Messages.TableEditAction_0);
		this.obj = obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.EDIT_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object value = ((INavigatorTreeNode)obj).getRoot();
		if(value==null || !(value instanceof Schema)){
			return;
		}
		Schema schema = (Schema)value;
		Table table = (Table)obj;
		if(table.isQuote()){
			Table table1 = schema.getTableByPath(table.getQuotePath());
			if(table1!=null){
				table.setColumns(table1.getColumns());
				table.setIndexes(table1.getIndexes());
				table.setTablespace(table1.getTablespace());
				table.setOtherSql(table1.getOtherSql());
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		CustomWizardDialog wd = new CustomWizardDialog(Display.getCurrent().getActiveShell(),
				new TableEditWizard(schema,table,map),false);
		wd.setPageSize(600,300);
		wd.create();
		int result;
		if((result = wd.open())!=1){
			int newStatus;
			int oldStatus = table.getStatus();
			ArrayList newColumns = (ArrayList)map.get("columns"); //$NON-NLS-1$
			ArrayList newIndexes = (ArrayList)map.get("indexes"); //$NON-NLS-1$
			String newTableName = (String)map.get("tableName"); //$NON-NLS-1$
			String newTableCnName = (String)map.get("tableCnName"); //$NON-NLS-1$
			String newTableDescription = (String)map.get("tableDescription"); //$NON-NLS-1$
			String newTableSpace = (String)map.get("tableSpace"); //$NON-NLS-1$
			String newTableOtherSql = (String)map.get("otherSql"); //$NON-NLS-1$
			int newTableSeqno = (Integer)map.get("tableSeqno"); //$NON-NLS-1$
			
			Column columnTemp;

			table.setTablespace(newTableSpace);
			table.setSeqno(newTableSeqno);
			table.setOtherSql(newTableOtherSql);
			
			table.modifyName(newTableName);
			table.modifyCnName(newTableCnName);
			table.modifyDescription(newTableDescription);
			ArrayList<Column> oldColumns = table.getColumns();
			table.setIndexes(newIndexes);
			
			for(int i=0;i<oldColumns.size();i++){
				columnTemp = (Column)oldColumns.get(i);
				table.removeColumn(columnTemp);
			}
			//����tableĬ�ϻ�����һ����ģ��Column,���԰�ʣ�µ��Ǹ�����ó�4�������ʱ�������Ƴ����
			ArrayList<Column> remainColumn = new ArrayList<Column>();
			if(table.getColumns().size()>0){
				for(int i=0;i<table.getColumns().size();i++){
					remainColumn.add((Column)table.getColumns().get(i));
				}
			}
			for(int i=0;i<newColumns.size();i++){
				columnTemp = (Column)newColumns.get(i);
				columnTemp.setTable(table);
				table.addColumn(columnTemp);
			}
			for(int i=0;i<remainColumn.size();i++){
				table.removeColumn(remainColumn.get(i));
			}
			if(oldStatus == IEditorConstant.TABLE_STATUS_NORMAL){
				newStatus = IEditorConstant.TABLE_STATUS_CHANGED;
			} else {
				newStatus = oldStatus;
			}
			table.modifyStatus(newStatus);
			if(result == 2){
				try{
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
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),
									SWT.ICON_QUESTION | SWT.YES | SWT.NO);
							mb.setText(Messages.TableEditAction_6);
							mb.setMessage(Messages.TableEditAction_7+table.getName()+Messages.TableEditAction_8);
					
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
						table.modifyStatus(IEditorConstant.TABLE_STATUS_CHANGED);
					}
					if(configOk){
						if(createOk){
							 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
							 mb.setText(Messages.TableEditAction_9);
							 mb.setMessage(Messages.TableEditAction_10);
							 mb.open();
						}else {
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_WARNING);
							mb.setText(Messages.TableEditAction_6);
							mb.setMessage(Messages.TableEditAction_1);
							mb.open();
						}
					 }else {
						 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
						 mb.setText(Messages.TableEditAction_6);
						 mb.setMessage(Messages.TableEditAction_12);
						 mb.open();
					 }
				}catch(Exception e){
					return;
				}
			}
		}else {
			return;
		}
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
				if(registry!=null && registry instanceof SchemaNavigatorView){
					SchemaNavigatorView view = (SchemaNavigatorView)registry;
					view.getCommonViewer().refresh(obj);
				}
				((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
