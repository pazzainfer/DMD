package com.leven.dmd.gef.action;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.leven.dmd.gef.Messages;

/**
 * An action to redo the last command.
 */
public class RedoAction extends StackAction {

	/**
	 * Creates a <code>RedoAction</code> and associates it with the given
	 * workbech part.
	 * 
	 * @param part
	 *            The workbench part this action is associated with.
	 */
	public RedoAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * Creates a <code>RedoAction</code> and associates it with the given
	 * editor.
	 * 
	 * @param editor
	 *            The editor this action is associated with.
	 */
	public RedoAction(IEditorPart editor) {
		super(editor);
	}

	/**
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return getCommandStack().canRedo();
	}

	/**
	 * Initializes this actions text and images.
	 */
	protected void init() {
		super.init();
		setToolTipText(Messages.RedoAction_1);
		setText(Messages.RedoAction_1 + "@Ctrl+Y"); //$NON-NLS-1$
		
		setId(ActionFactory.REDO.getId());

		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));
	}

	/**
	 * Refreshes this action's text to use the last undone command's label.
	 */
	protected void refresh() {
		Command redoCmd = getCommandStack().getRedoCommand();
		setToolTipText(Messages.RedoAction_1 + "  " + getLabelForCommand(redoCmd)); //$NON-NLS-1$
		setText(Messages.RedoAction_1 + "  " + getLabelForCommand(redoCmd) + "@Ctrl+Y"); //$NON-NLS-1$ //$NON-NLS-3$
		super.refresh();
	}

	/**
	 * Redoes the last command.
	 */
	public void run() {
		getCommandStack().redo();
	}

}
