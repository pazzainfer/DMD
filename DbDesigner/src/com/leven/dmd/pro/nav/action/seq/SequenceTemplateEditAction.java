package com.leven.dmd.pro.nav.action.seq;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.action.seq.SequenceTemplateEditWizard;

public class SequenceTemplateEditAction extends Action {
	public static final String ACTION_ID = "dhcc_platform.gef.action.SequenceTemplateEditAction"; //$NON-NLS-1$
	private Object obj;

	public SequenceTemplateEditAction(Object obj) {
		super();
		this.setText(Messages.SequenceTemplateEditAction_1);
		this.obj = obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.EDIT_IMAGE_PATH));
	}

	protected boolean calculateEnabled() {
		return true;
	}

	public void run() {
		super.run();
		WizardDialog wd = null;
		wd = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),new SequenceTemplateEditWizard((SequenceTemplate)this.obj));
		wd.create();
		wd.open();
	}
}