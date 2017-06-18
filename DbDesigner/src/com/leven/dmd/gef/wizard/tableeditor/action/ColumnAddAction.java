package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.wizard.tableeditor.CustomIndexViewer;
import com.leven.dmd.gef.wizard.tableeditor.CustomTableViewer;
import com.leven.dmd.pro.Activator;

public class ColumnAddAction extends Action {
	private TableViewer viewer;
	private Map<String,Column> columnMap;
	private Schema schema;
	private CustomTableViewer indexViewer;
	
	public ColumnAddAction(TableViewer viewer,Map<String,Column> columnMap, Schema schema,CustomTableViewer indexViewer) {
		setText(Messages.ColumnAddAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.schema = schema;
		this.indexViewer = indexViewer;
		this.setToolTipText(Messages.ColumnAddAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_ADD));
	}
	public void run() {
		Column p = new Column();
		Map<String,Column> map = new HashMap<String,Column>();
		
		ColumnAddWizard wizard = new ColumnAddWizard(schema,map,columnMap);
		WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(),wizard);
		dialog.create();
		if(dialog.open()==Dialog.OK){
			ArrayList columnList = ((ArrayList)viewer.getInput());
			p = map.get("data"); //$NON-NLS-1$
			if(p!=null){
				if(p.isPk()){
					indexViewer.getIndexViewer().refreshWhenColumnPK(p.getName());
				}
				viewer.add(p);
			    columnList.add(p);
			    columnMap.put(p.getName(), p);
			}
		}
	}
	
}
