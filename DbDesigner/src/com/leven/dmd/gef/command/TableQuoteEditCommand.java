package com.leven.dmd.gef.command;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.Messages;
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
 * 引用表的编辑命令
 * @author leven
 * create at 2013-10-30 上午8:04:27
 */
public class TableQuoteEditCommand extends Command {

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
	
	private TablePart part;
	private boolean finished = false;
	private int oldStatus;
	private int newStatus;

	public TableQuoteEditCommand(Table table,Schema schema,TablePart part) {
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
			finished = true;
			oldStatus = table.getStatus();
			newTableName = (String)map.get("tableName"); //$NON-NLS-1$
			newTableCnName = (String)map.get("tableCnName"); //$NON-NLS-1$
			newTableDescription = (String)map.get("tableDescription"); //$NON-NLS-1$
			newSeqno = (Integer)map.get("tableSeqno"); //$NON-NLS-1$
			
			oldTableName = new String(table.getName());
			oldTableCnName = new String(table.getCnName());
			oldSeqno = table.getSeqno();
			oldTableDescription = new String(table.getDescription()==null?"":table.getDescription()); //$NON-NLS-1$

			if(oldStatus == IEditorConstant.TABLE_STATUS_NORMAL){
				newStatus = IEditorConstant.TABLE_STATUS_CHANGED;
			} else {
				newStatus = oldStatus;
			}
			table.modifyName(newTableName);
			table.modifyCnName(newTableCnName);
			table.modifyDescription(newTableDescription);
			table.setSeqno(newSeqno);
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
								if(DbTableUtil.createTableByDomian(table,false,config)){
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
			table.modifyName(newTableName);
			table.modifyCnName(newTableCnName);
			table.modifyDescription(newTableDescription);
			table.setSeqno(newSeqno);
			table.modifyStatus(newStatus);
			part.refresh();
			refreshView();
		}
	}


	public void undo() {
		if(finished){
			table.modifyName(oldTableName);
			table.modifyCnName(oldTableCnName);
			table.modifyDescription(oldTableDescription);
			table.setSeqno(oldSeqno);
			
			table.modifyStatus(oldStatus);
			part.refresh();
			refreshView();
		}
	}
	private void refreshView(){
		NavigatorViewUtil.refresh(table);
	}
}