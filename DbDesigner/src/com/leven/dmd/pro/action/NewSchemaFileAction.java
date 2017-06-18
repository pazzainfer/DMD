package com.leven.dmd.pro.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.editor.SchemaEditorInput;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class NewSchemaFileAction extends Action {
	
	public NewSchemaFileAction() {
		super(Messages.NewSchemaFileAction_0);
        setImageDescriptor(ImageHelper.getImageDescriptor(ImageKeys.SCHEMA_NEW));
        setToolTipText(Messages.NewSchemaFileAction_1);
	}
	
	
	public void run() {
		try {
			IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					, new SchemaEditorInput(), SchemaDiagramEditor.EDITOR_ID);
			((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActiveEditor()).setDirty(true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
