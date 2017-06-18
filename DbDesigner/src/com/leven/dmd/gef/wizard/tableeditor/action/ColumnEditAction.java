package com.leven.dmd.gef.wizard.tableeditor.action;

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

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.wizard.tableeditor.CustomTableViewer;
import com.leven.dmd.pro.Activator;

public class ColumnEditAction extends Action {
	private TableViewer viewer;
	private Map<String,Column> columnMap;
	private Schema schema;
	private CustomTableViewer indexViewer;
	
	public ColumnEditAction(TableViewer viewer,Map<String,Column> columnMap, Schema schema,CustomTableViewer indexViewer) {
		setText(Messages.ColumnEditAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.schema = schema;
		this.indexViewer = indexViewer;
		this.setToolTipText(Messages.ColumnEditAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_EDIT));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_EDIT_DISABLED));
		this.setEnabled(false);
	}
	public void run() {
		StructuredSelection select = (StructuredSelection) viewer
			.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.ColumnEditAction_2);
			mb.setMessage(Messages.ColumnEditAction_3);
			mb.open();
		}else{
			ArrayList columnList = ((ArrayList)viewer.getInput());
			Column p = (Column) select.getFirstElement();
			int selectIndex = columnList.indexOf(p);
			
			Map<String,Column> map = new HashMap<String,Column>();
			
			ColumnEditWizard wizard = new ColumnEditWizard(schema,map,columnMap,p);
			WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(),wizard);
			dialog.create();
			if(dialog.open()==Dialog.OK){
				Column newColumn = map.get(Messages.ColumnEditAction_4);
				if(p!=null){
					if(p.isPk() && !newColumn.isPk()){
						indexViewer.getIndexViewer().refreshWhenColumnNotPK(p.getName());
					}else if(!p.isPk() && newColumn.isPk()){
						indexViewer.getIndexViewer().refreshWhenColumnPK(p.getName());
					}
				    columnMap.remove(p.getName());
				    columnMap.put(newColumn.getName(), newColumn);
				    columnList.remove(p);
				    columnList.add(selectIndex, newColumn);
				    
				    viewer.setInput(columnList);
			    	viewer.refresh();
				}
			}
		}
	}
	
}
