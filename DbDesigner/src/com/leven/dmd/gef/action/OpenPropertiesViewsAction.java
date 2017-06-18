package com.leven.dmd.gef.action;

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class OpenPropertiesViewsAction extends EditorPartAction {
	public static final String ACTION_ID = "com.leven.platform.OpenPropertiesViewsAction"; //$NON-NLS-1$
		
	public OpenPropertiesViewsAction(IEditorPart editor) {
		super(editor);
	}

	public OpenPropertiesViewsAction() {
		super(null);
	}

	public OpenPropertiesViewsAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	protected void init() {
		super.init();
		setId(ActionFactory.FIND.getId());
		setToolTipText(Messages.OpenPropertiesViewsAction_1);
		setText(Messages.OpenPropertiesViewsAction_1);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.SHOW_PROPERTY_VIEW));
	}

	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}