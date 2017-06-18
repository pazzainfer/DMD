package com.leven.dmd.pro.pref.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.util.ImageKeys;

public class DBConfigEditAction extends Action {
	private TableViewer viewer;
	private Map<String,DBConfigModel> columnMap;
	
	public DBConfigEditAction(TableViewer viewer,Map<String,DBConfigModel> columnMap) {
		setText(Messages.DBConfigEditAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.setToolTipText(Messages.DBConfigEditAction_1);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.ACTION_EDIT));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.ACTION_EDIT_DISABLED));
		this.setEnabled(false);
	}
	public void run() {
		StructuredSelection select = (StructuredSelection) viewer
			.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.DBConfigEditAction_2);
			mb.setMessage(Messages.DBConfigEditAction_3);
			mb.open();
		}else{
			ArrayList columnList = ((ArrayList)viewer.getInput());
			DBConfigModel p = (DBConfigModel) select.getFirstElement();
			int selectIndex = columnList.indexOf(p);
			
			Map<String,DBConfigModel> map = new HashMap<String,DBConfigModel>();
			
			DBConfigEditWizard wizard = new DBConfigEditWizard(map,columnMap,p);
			WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(),wizard);
			dialog.create();
			if(dialog.open()==Dialog.OK){
				DBConfigModel newDBConfigModel = map.get("data"); //$NON-NLS-1$
				if(p!=null){
				    columnMap.remove(p.getName());
				    columnMap.put(newDBConfigModel.getName(), newDBConfigModel);
				    columnList.remove(p);
				    columnList.add(selectIndex, newDBConfigModel);
				    
				    viewer.setInput(columnList);
			    	viewer.refresh();
				}
			}
		}
	}
	
}
