package com.leven.dmd.gef.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;

/**
 * �༭���Զ����򹤾ߵ�ʵ��
 * 
 * @author lifeng 2012-7-11 ����02:24:20
 */
public class FlyoutChangeLayoutAction extends Action {

	IEditorPart editor;
	boolean checked;

	public FlyoutChangeLayoutAction(IEditorPart editor) {
		super(Messages.FlyoutChangeLayoutAction_0, Action.AS_CHECK_BOX);
		this.editor = editor;
	}

	public void run() {
		if (editor instanceof SchemaDiagramEditor) {
			SchemaDiagramEditor schemaEditor = (SchemaDiagramEditor) editor;
			Schema schema = schemaEditor.getSchema();
			boolean isManual = schema.isLayoutManualDesired();
			schema.setLayoutManualDesired(!isManual);
			checked = !isManual;
			setChecked(checked);
		}
	}

	public boolean isChecked() {
		if (editor != null)
			return isChecked(editor);
		else
			return super.isChecked();
	}

	/**
	 * @see org.eclipse.jface.action.IAction#isChecked()
	 */
	public boolean isChecked(IEditorPart editor) {

		if (editor instanceof SchemaDiagramEditor) {
			SchemaDiagramEditor schemaEditor = (SchemaDiagramEditor) editor;
			Schema schema = schemaEditor.getSchema();
			boolean checkTrue = schema.isLayoutManualDesired();
			return (!checkTrue);
		} else {
			return false;
		}

	}

	public void setActiveEditor(IEditorPart editor) {
		this.editor = editor;
		boolean localChecked = isChecked(editor);

		// there appears to be a bug in the framework which necessitates this
		if (localChecked)
			firePropertyChange(CHECKED, Boolean.FALSE, Boolean.TRUE);
		else
			firePropertyChange(CHECKED, Boolean.TRUE, Boolean.FALSE);
	}

}