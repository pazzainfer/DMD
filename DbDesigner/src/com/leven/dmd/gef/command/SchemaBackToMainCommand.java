package com.leven.dmd.gef.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.PaletteViewerCreator;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;

public class SchemaBackToMainCommand extends Command {

	private Schema schema;
	private TablePackage tablePackage;

	public SchemaBackToMainCommand(Schema schema,TablePackage tablePackage) {
		super(Messages.SchemaBackToMainCommand_0);
		this.schema = schema;
		this.tablePackage = tablePackage;
	}

	
	@Override
	public boolean canExecute() {
		return schema.isPackageOpen();
	}


	public void execute() {
		schema.commitPackageClose();
		//refreshPalette();
		((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor())
		.getAction(ActionFactory.BACK.getId()).setEnabled(false);
	}

	public void undo() {
		schema.commitPackageOpen(tablePackage);
		//refreshPalette();
		((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor())
		.getAction(ActionFactory.BACK.getId()).setEnabled(true);
	}
	
	private void refreshPalette(){
		((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor())
		.getEditDomain().setPaletteRoot(new PaletteViewerCreator().createPaletteRoot(schema));
	}
}