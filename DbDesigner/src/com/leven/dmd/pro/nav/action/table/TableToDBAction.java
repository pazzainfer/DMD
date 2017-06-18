package com.leven.dmd.pro.nav.action.table;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

/**
 * 数据表持久化至数据库
 * @author leven
 * create at 2013-10-30 上午1:34:55
 */
public class TableToDBAction extends Action {

	private Object obj;

	public TableToDBAction(Object obj) {
		super();
		this.obj = obj;
		this.setText(Messages.TableToDBAction_0);
		this.setToolTipText(Messages.TableToDBAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.INTO_DB));
	}

	public void run() {
		Table table = null;
		if(obj instanceof Table){
			table = (Table)obj;
		}else {
			return;
		}
		DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
		dialog.create();
		if(dialog.open()!=Dialog.OK){
			return;
		}
		DBConfigModel config = dialog.getDbConfigModel();
		if(!DBConfigUtil.isConfigAvailable(config)){
			 MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.TableToDBAction_2);
			 mb.setMessage(Messages.TableToDBAction_3);
			 mb.open();
			 return;
		}
		if(DbTableUtil.CheckTableExist(table,config)){
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			mb.setText(Messages.TableToDBAction_2);
			mb.setMessage(Messages.TableToDBAction_5+table.getName()+Messages.TableToDBAction_6);
	
			if(mb.open()==SWT.YES){
				if(DbTableUtil.createTableByDomian(table,true,config)){
					table.modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
				}else {
					MessageBox mb1 = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING);
					 mb1.setText(Messages.TableToDBAction_2);
					 mb1.setMessage(Messages.TableToDBAction_8);
					 mb1.open();
					 return;
				}
			}
		 }else{
			 if(DbTableUtil.createTableByDomian(table,false,config)){
				table.modifyStatus(IEditorConstant.TABLE_STATUS_NORMAL);
			}else {
				MessageBox mb1 = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING);
				 mb1.setText(Messages.TableToDBAction_2);
				 mb1.setMessage(Messages.TableToDBAction_8);
				 mb1.open();
				 return;
			}
		 }
	}
}