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
 * An action to undo the last command.
 */
public class UndoAction extends StackAction {

	/**
	 * Creates an <code>UndoAction</code> and associates it with the given
	 * editor.
	 * 
	 * @param editor
	 *            The editor this action is associated with.
	 */
	public UndoAction(IEditorPart editor) {
		super(editor);
	}

	/**
	 * Creates an <code>UndoAction</code> and associates it with the given
	 * editor.
	 * 
	 * @param part
	 *            The workbench part this action is associated with.
	 */
	public UndoAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return getCommandStack().canUndo();
	}

	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		setToolTipText( Messages.UndoAction_1); //$NON-NLS-1$ 
		setText(Messages.UndoAction_1 + "@Ctrl+Z"); //$NON-NLS-1$ 
		setId(ActionFactory.UNDO.getId());

		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		setDisabledImageDescriptor(sharedImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
	}

	/**
	 * Refreshes this action's text to use the last executed command's label.
	 */
	protected void refresh() {
		Command undoCmd = getCommandStack().getUndoCommand();
		setToolTipText(Messages.UndoAction_1 + "  " + getLabelForCommand(undoCmd)); //$NON-NLS-1$
		setText(Messages.UndoAction_1 + "  " + getLabelForCommand(undoCmd) + "@Ctrl+Z"); //$NON-NLS-1$ //$NON-NLS-2$
		super.refresh();
	}

	/**
	 * Undoes the last command.
	 */
	public void run() {
		getCommandStack().undo();
	}

}
