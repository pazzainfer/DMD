package com.leven.dmd.gef.action;

import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.command.SchemaBackToMainCommand;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class SchemaBackToMainAction extends EditorPartAction {
		
	public SchemaBackToMainAction(IEditorPart editor) {
		super(editor);
	}

	public SchemaBackToMainAction() {
		super(null);
	}

	public SchemaBackToMainAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected void init() {
		super.init();
		setId(ActionFactory.BACK.getId());
		setToolTipText(Messages.SchemaBackToMainAction_0);
		setText(Messages.SchemaBackToMainAction_0+"@Ctrl+Backspace");
		this.setEnabled(false);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.BACK_TO_SCHEMA));
		this.setDisabledImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.BACK_TO_SCHEMA_DISABLED));
	}

	protected boolean calculateEnabled() {
		try {
			Schema schema = ((SchemaDiagramEditor)this.getEditorPart()).getSchema();
			if(schema.isPackageOpen()){
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void run() {
		Schema schema = ((SchemaDiagramEditor)this.getEditorPart()).getSchema();
		if(schema.isPackageOpen()){
			((SchemaDiagramEditor)this.getEditorPart()).getCommandStack()
				.execute(new SchemaBackToMainCommand(schema,schema.getOpenPackage()));
		}
		((SchemaDiagramEditor)this.getEditorPart()).getAction(ActionFactory.BACK.getId()).setEnabled(false);
	}
}