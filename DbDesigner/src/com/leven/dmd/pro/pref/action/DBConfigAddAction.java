package com.leven.dmd.pro.pref.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;

import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.util.ImageKeys;

public class DBConfigAddAction extends Action {

	private TableViewer viewer;
	private Map<String,DBConfigModel> configMap;
	
	public DBConfigAddAction(TableViewer viewer,Map<String,DBConfigModel> configMap) {
		setText(Messages.DBConfigAddAction_0);
		this.viewer = viewer;
		this.configMap = configMap;
		this.setToolTipText(Messages.DBConfigAddAction_1);
		this.setImageDescriptor(Activator.getImageDescriptor(ImageKeys.ACTION_ADD));
	}
	public void run() {
		DBConfigModel p = null;
		Map<String,DBConfigModel> map = new HashMap<String,DBConfigModel>();
		
		DBConfigAddWizard wizard = new DBConfigAddWizard(map,configMap);
		WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(),wizard);
		dialog.create();
		if(dialog.open()==Dialog.OK){
			ArrayList columnList = ((ArrayList)viewer.getInput());
			p = map.get("data"); //$NON-NLS-1$
			if(p!=null){
				viewer.add(p);
			    columnList.add(p);
			    configMap.put(p.getName(), p);
			}
		}
	}
}
