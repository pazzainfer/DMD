package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class ColumnImportAction extends Action {
	private TableViewer viewer;
	private Map<String,Column> columnMap;
	private Schema schema;
	
	public ColumnImportAction(TableViewer viewer,Map<String,Column> columnMap, Schema schema) {
		setText(Messages.ColumnImportAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.schema = schema;
		this.setToolTipText(Messages.ColumnImportAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_IMPORT));
	}
	
	public void run() {
		ColumnImportWizard wizard = new ColumnImportWizard(schema,viewer,columnMap);
		WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(),wizard);
		dialog.create();
		dialog.open();
	}
	
}
