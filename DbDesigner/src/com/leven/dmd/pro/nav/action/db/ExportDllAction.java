package com.leven.dmd.pro.nav.action.db;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;

public class ExportDllAction extends Action {
	private Object obj;

	public ExportDllAction(Object obj) {
		super();
		this.setText(Messages.ExportDllAction_0);
		this.setToolTipText(Messages.ExportDllAction_0);
		this.obj = obj;
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DLL));
	}

	@Override
	public void run() {
		ExportDllWizard wizard = new ExportDllWizard(obj);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),wizard);
		dialog.create();
		dialog.open();
	}
}
