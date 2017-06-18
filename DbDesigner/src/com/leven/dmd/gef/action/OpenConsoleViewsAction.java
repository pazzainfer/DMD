package com.leven.dmd.gef.action;

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.IImagePath;
import com.leven.dmd.pro.console.ConsoleFactory;

public class OpenConsoleViewsAction extends EditorPartAction {
	public static final String ACTION_ID = "com.leven.platform.OpenConsoleViewsAction"; //$NON-NLS-1$
		
	public OpenConsoleViewsAction(IEditorPart editor) {
		super(editor);
	}

	public OpenConsoleViewsAction() {
		super(null);
	}

	public OpenConsoleViewsAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	protected void init() {
		super.init();
		setId(ACTION_ID);
		setToolTipText(Messages.OpenConsoleViewsAction_0);
		setText(Messages.OpenConsoleViewsAction_1);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, IImagePath.OBJ_CONSOLE));
	}

	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(IConsoleConstants.ID_CONSOLE_VIEW);
			ConsoleFactory cf = new ConsoleFactory();
			cf.openConsole();
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}