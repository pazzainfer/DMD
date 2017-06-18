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

public class ColumnCopyTemplateAction extends Action {
	private TableViewer viewer;
	private Map<String,Column> columnMap;
	private Schema schema;
	
	public ColumnCopyTemplateAction(TableViewer viewer,Map<String,Column> columnMap, Schema schema) {
		setText(Messages.ColumnCopyTemplateAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.schema = schema;
		this.setToolTipText(Messages.ColumnCopyTemplateAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.TEMPLATE));
	}
	public void run() {
		ColumnCopyTemplateWizard wizard = new ColumnCopyTemplateWizard(schema,viewer,columnMap);
		WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(),wizard);
		dialog.create();
		dialog.open();
	}
	
}
