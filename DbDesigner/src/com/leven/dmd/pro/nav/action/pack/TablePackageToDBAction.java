package com.leven.dmd.pro.nav.action.pack;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

/**
 * 主题持久化至数据库
 * @author leven
 * create at 2013-10-30 上午1:34:55
 */
public class TablePackageToDBAction extends Action {

	private Object obj;

	public TablePackageToDBAction(Object obj) {
		super();
		this.obj = obj;
		this.setText(Messages.TablePackageToDBAction_0);
		this.setToolTipText(Messages.TablePackageToDBAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.INTO_DB));
	}

	public void run() {
		TablePackage tablePackage = null;
		if(obj instanceof TablePackage){
			tablePackage = (TablePackage)obj;
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
			 mb.setText(Messages.TablePackageToDBAction_2);
			 mb.setMessage(Messages.TablePackageToDBAction_3);
			 mb.open();
			 return;
		}
		String result = createPackageTables(tablePackage, config);
		if(result!=null && !"".equals(result)){ //$NON-NLS-1$
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING);
			 mb.setText(Messages.TablePackageToDBAction_2);
			 mb.setMessage(Messages.TablePackageToDBAction_6 + result.substring(0, result.length()-2));
			 mb.open();
		}
	}
	/**
	 * 递归将主题下面的所有表重建
	 * @param tablePackage
	 * @param config
	 * @return 失败的表
	 */
	private String createPackageTables(TablePackage tablePackage,DBConfigModel config){
		StringBuffer failedTable = new StringBuffer();
		List<Table> tables = tablePackage.getTables();
		 for(int i=0;i<tables.size();i++){
				 if(DbTableUtil.CheckTableExist(tables.get(i),config)){
						MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						mb.setText(Messages.TablePackageToDBAction_2);
						mb.setMessage(Messages.TablePackageToDBAction_8+tables.get(i).getName()+Messages.TablePackageToDBAction_9);
				
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
			 failedTable.append(createPackageTables(child ,config) + ",");  //$NON-NLS-1$
		 }
		 return failedTable.toString();
	}
}