package com.leven.dmd.gef.action;

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class OpenOutlineViewsAction extends EditorPartAction {
	public static final String ACTION_ID = "com.leven.platform.OpenOutlineViewsAction"; //$NON-NLS-1$
		
	public OpenOutlineViewsAction(IEditorPart editor) {
		super(editor);
	}

	public OpenOutlineViewsAction() {
		super(null);
	}

	public OpenOutlineViewsAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	protected void init() {
		super.init();
		setId(ACTION_ID);
		setToolTipText(Messages.OpenOutlineViewsAction_1);
		setText(Messages.OpenOutlineViewsAction_1);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.SHOW_OUTLINE_VIEW));
	}

	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.ContentOutline"); //$NON-NLS-1$
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}