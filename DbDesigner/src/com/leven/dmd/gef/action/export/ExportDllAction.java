package com.leven.dmd.gef.action.export;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.nav.action.db.ExportDllWizard;

public class ExportDllAction extends SelectionAction {

	public ExportDllAction(IWorkbenchPart part) {
		super(part);
		this.setText(Messages.ExportDllAction_0);
		this.setToolTipText(Messages.ExportDllAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DLL));
	}
	
	@Override
	protected boolean calculateEnabled() {
		IWorkbenchPart part = getWorkbenchPart();
		if (part instanceof GraphicalEditor) {
			return true;
		}else if (part instanceof ContentOutline) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		Schema schema = null;
		IWorkbenchPart part = getWorkbenchPart();
		if (part instanceof SchemaDiagramEditor) {
			schema = ((SchemaDiagramEditor)part).getSchema();
		}
		ExportDllWizard wizard = new ExportDllWizard(schema);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),wizard);
		dialog.create();
		dialog.open();
	}
}
