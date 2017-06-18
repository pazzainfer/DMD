package com.leven.dmd.pro.nav.action.db;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

/**
 * ��������ͬ������ݿ�Action
 * @author lifeng
 * 2012-7-18 ����03:26:36
 */
public class CreateTableToDatabaseAction extends Action {

	private Object obj;

	public CreateTableToDatabaseAction(Object obj) {
		super();
		this.obj = obj;
		this.setText(Messages.CreateTableToDatabaseAction_0);
		this.setToolTipText(Messages.CreateTableToDatabaseAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.INTO_DB));
	}

	public void run() {
		Schema schema = null;
		INavigatorTreeNode node = (INavigatorTreeNode)obj;
		schema = (Schema)node.getRoot();
		DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
		dialog.create();
		if(dialog.open()!=Dialog.OK){
			return;
		}
		DBConfigModel config = dialog.getDbConfigModel();
		if(!DBConfigUtil.isConfigAvailable(config)){
			 MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR);
			 mb.setText(Messages.CreateTableToDatabaseAction_2);
			 mb.setMessage(Messages.CreateTableToDatabaseAction_3);
			 mb.open();
			 return;
		}
		StringBuffer failedTable = new StringBuffer();
		List<Table> tables = schema.getAllTables();
		for(int i=0;i<tables.size();i++){
			if(DbTableUtil.CheckTableExist(tables.get(i),config)){
				MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setText(Messages.CreateTableToDatabaseAction_2);
				mb.setMessage(Messages.CreateTableToDatabaseAction_5+tables.get(i).getName()+Messages.CreateTableToDatabaseAction_6);
				
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
		if(failedTable.length()>0){
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING);
			 mb.setText(Messages.TablePackageToDBAction_2);
			 mb.setMessage(Messages.TablePackageToDBAction_6 + failedTable.substring(0, failedTable.length()-2));
			 mb.open();
		}else {
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION);
			 mb.setText(this.getText());
			 mb.setMessage("持久化至数据库完成!");
			 mb.open();
		}
	}
}