package com.leven.dmd.gef.action;

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.util.control.palette.CustomGraphicalEditorWithPalette;

public class FindNodeAction extends EditorPartAction {
		
	public FindNodeAction(IEditorPart editor) {
		super(editor);
	}

	public FindNodeAction() {
		super(null);
	}

	public FindNodeAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	protected void init() {
		super.init();
		setId(ActionFactory.FIND.getId());
		setToolTipText(Messages.FindNodeAction_0);
		setText(Messages.FindNodeAction_0+"@Ctrl+F");
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.SEARCH));
	}

	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		CustomGraphicalEditorWithPalette editor = (CustomGraphicalEditorWithPalette) getEditorPart();

		if (editor == null) {
			editor = (CustomGraphicalEditorWithPalette) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		}
		FindGraphicalNodeDialog dialog = new FindGraphicalNodeDialog(shell,
				editor);

		dialog.open();
	}
}