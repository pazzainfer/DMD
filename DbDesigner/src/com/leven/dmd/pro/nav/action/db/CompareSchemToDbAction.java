package com.leven.dmd.pro.nav.action.db;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

/**
 *�������ģ���ĵ�
 */
public class CompareSchemToDbAction extends Action {

	private Object obj;

	public CompareSchemToDbAction(Object obj) {
		super(Messages.CompareSchemToDbAction_0, Action.AS_PUSH_BUTTON);
		this.obj = obj;
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.ACTION_COMPARE));
	}

	public void run() {
			Schema schema = (Schema)((INavigatorTreeNode)obj).getRoot();
			DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
			dialog.create();
			if(dialog.open()!=Dialog.OK){
				return;
			}
			DBConfigModel config = dialog.getDbConfigModel();
			if(!DBConfigUtil.isConfigAvailable(config)){
				 MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR);
				 mb.setText(Messages.CompareSchemToDbAction_1);
				 mb.setMessage(Messages.CompareSchemToDbAction_2);
				 mb.open();
				 return;
			}
			schema = DbTableUtil.checkTablesToDbBySchema(schema, config);
			
	}
}