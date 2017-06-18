package com.leven.dmd.pro.nav.action.temp;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.internal.Workbench;

import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.action.temp.ColumnTemplateEditWizard;

@SuppressWarnings("restriction")
public class ColumnTemplateEditAction extends Action {
	public static final String ACTION_ID = "dhcc_platform.gef.action.ColumnTemplateEditAction"; //$NON-NLS-1$
	private Object obj;

	public ColumnTemplateEditAction(Object obj) {
		super();
		this.setText(Messages.ColumnTemplateEditAction_1);
		this.obj = obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.EDIT_IMAGE_PATH));
	}

	protected boolean calculateEnabled() {
		return true;
	}

	public void run() {
		super.run();
		WizardDialog wd = null;
		wd = new WizardDialog(Workbench.getInstance().getActiveWorkbenchWindow().getShell()
				,new ColumnTemplateEditWizard((ColumnTemplate)obj));
		wd.create();
		wd.open();
	}
}