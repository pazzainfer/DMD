package com.leven.dmd.gef.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.part.TablePart;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.wizard.CustomWizardDialog;
import com.leven.dmd.gef.wizard.TableEditWizard;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;
/**
 * ��༭����
 * @author leven
 * 2012-8-23 ����03:23:13
 */
public class TableEditCommand extends Command {

	protected Table table;
	protected Schema schema;

	private String oldTableName;
	private String oldTableCnName;
	private String oldTableDescription;
	private String newTableName;
	private String newTableCnName;
	private String newTableDescription;
	
	private int oldSeqno;
	private int newSeqno;
	private String oldTableSpace;
	private String newTableSpace;
	private String oldOtherSql;
	private String newOtherSql;
	private ArrayList newColumns = new ArrayList();
	private ArrayList oldColumns = new ArrayList();
	private ArrayList newIndexes = new ArrayList();
	private ArrayList oldIndexes = new ArrayList();
	private int oldStatus;
	private int newStatus;
	private TablePart part;
	private boolean finished = false;

	public TableEditCommand(Table table,Schema schema,TablePart part) {
		super();
		this.setLabel(Messages.TableEditCommand_0);
		this.table = table;
		this.schema = schema;
		this.part = part;
	}

	public boolean canExecute() {
		return true;
	}

	public void execute() {
		Map<String,Object> map = new HashMap<String,Object>();
		CustomWizardDialog wd = new CustomWizardDialog(Display.getCurrent().getActiveShell(),
				new TableEditWizard(schema,table,map),false);
		wd.setPageSize(600,300);
		wd.create();
		int result;
		if((result = wd.open())!=1){
			oldStatus = table.getStatus();
			finished = true;
			newColumns = (ArrayList)map.get("columns"); //$NON-NLS-1$
			newIndexes = (ArrayList)map.get("indexes"); //$NON-NLS-1$
			newTableName = (String)map.get("tableName"); //$NON-NLS-1$
			newTableCnName = (String)map.get("tableCnName"); //$NON-NLS-1$
			newTableDescription = (String)map.get("tableDescription"); //$NON-NLS-1$
			newTableSpace = (String)map.get("tableSpace"); //$NON-NLS-1$
			newSeqno = (Integer)map.get("tableSeqno"); //$NON-NLS-1$
			newOtherSql = (String)map.get("otherSql"); //$NON-NLS-1$
			
			Column columnTemp;
			oldTableName = new String(table.getName());
			oldTableCnName = new String(table.getCnName());
			oldTableSpace = new String(table.getTablespace());
			oldOtherSql = new String(table.getOtherSql());
			oldSeqno = table.getSeqno();
			oldTableDescription = new String(table.getDescription()==null?"":table.getDescription()); //$NON-NLS-1$

			table.modifyName(newTableName);
			table.modifyCnName(newTableCnName);
			table.modifyDescription(newTableDescription);
			table.setTablespace(newTableSpace);
			table.setOtherSql(newOtherSql);
			table.setSeqno(newSeqno);
			oldColumns = table.getColumns();
			oldIndexes = table.getIndexes();
			
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
			table.setIndexes(newIndexes);
			table.modifyStatus(newStatus);
			part.refresh();
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
							mb.setText(Messages.TableEditCommand_7);
							mb.setMessage(Messages.TableEditCommand_8+table.getName()+Messages.TableEditCommand_9);
					
							if(mb.open()==SWT.YES){
								if(DbTableUtil.createTableByDomian(table,true,config)){
									createOk = createOk && true;
									table.modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
								}else {
									createOk = createOk && false;
								}
							}
						}else{
							if(DbTableUtil.createTableByDomian(table,false,config)){
								createOk = createOk && true;
								table.modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
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
							 mb.setText(Messages.TableEditCommand_10);
							 mb.setMessage(Messages.TableEditCommand_11);
							 mb.open();
						}else {
							MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
							 mb.setText(Messages.TableEditCommand_7);
							 mb.setMessage(Messages.TableEditCommand_14);
							 mb.open();
						}
					 }else {
						 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
						 mb.setText(Messages.TableEditCommand_7);
						 mb.setMessage(Messages.TableEditCommand_13);
						 mb.open();
					 }
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			refreshView();
		}else {
			finished = false;
		}
	}

	public void redo() {
		if(finished){
			table.setIndexes(newIndexes);
			table.modifyName(newTableName);
			table.modifyCnName(newTableCnName);
			table.modifyDescription(newTableDescription);
			table.setTablespace(newTableSpace);
			table.setOtherSql(newOtherSql);
			table.setSeqno(newSeqno);
			ArrayList oldColumns = table.getColumns();
			Column columnTemp;
			
			for(int i=0;i<oldColumns.size();i++){
				columnTemp = (Column)oldColumns.get(i);
				table.removeColumn(columnTemp);
			}
			//����tableĬ�ϻ�����һ����ģ��Column,���԰�ʣ�µ��Ǹ�����ó�4�������ʱ�������Ƴ����
			Column remainColumn = null;
			if(table.getColumns().size()>0){
				remainColumn = (Column)table.getColumns().get(0);
			}
			for(int i=0;i<newColumns.size();i++){
				columnTemp = (Column)newColumns.get(i);
				table.addColumn(columnTemp);
			}
			table.removeColumn(remainColumn);
			
			table.modifyStatus(newStatus);
			part.refresh();
			refreshView();
		}
	}


	public void undo() {
		if(finished){
			table.setIndexes(oldIndexes);
			table.modifyName(oldTableName);
			table.modifyCnName(oldTableCnName);
			table.modifyDescription(oldTableDescription);
			table.setTablespace(oldTableSpace);
			table.setOtherSql(oldOtherSql);
			table.setSeqno(oldSeqno);
			Column columnTemp;
			
			ArrayList tableColumns = table.getColumns();
			for(int i=0;i<tableColumns.size();i++){
				columnTemp = (Column)tableColumns.get(i);
				table.removeColumn(columnTemp);
			}
			//����tableĬ�ϻ�����һ����ģ��Column,���԰�ʣ�µ��Ǹ�����ó�4�������ʱ�������Ƴ����
			Column remainColumn = null;
			if(table.getColumns().size()>0){
				remainColumn = (Column)table.getColumns().get(0);
			}
			for(int i=0;i<oldColumns.size();i++){
				columnTemp = (Column)oldColumns.get(i);
				table.addColumn(columnTemp);
			}
			table.removeColumn(remainColumn);
	
			table.modifyStatus(oldStatus);
			part.refresh();
			refreshView();
		}
	}
	private void refreshView(){
		NavigatorViewUtil.refresh(table);
	}
}