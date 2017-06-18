package com.leven.dmd.gef.command;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.PaletteViewerCreator;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;

public class SchemaGoIntoPackageCommand extends Command {

	private Schema schema;
	private TablePackage tablePackage;

	public SchemaGoIntoPackageCommand(Schema schema,TablePackage tablePackage) {
		super(Messages.SchemaGoIntoPackageCommand_0);
		this.schema = schema;
		this.tablePackage = tablePackage;
	}

	
	@Override
	public boolean canExecute() {
		return true;
	}


	public void execute() {
		schema.commitPackageOpen(tablePackage);
		((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor())
			.getAction(ActionFactory.BACK.getId()).setEnabled(true);
		//refreshPalette();
	}

	public void undo() {
		schema.commitPackageClose();
		//refreshPalette();
		((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor())
		.getAction(ActionFactory.BACK.getId()).setEnabled(false);
	}
	
	private void refreshPalette(){
		((SchemaDiagramEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor())
		.getEditDomain().setPaletteRoot(new PaletteViewerCreator().createPaletteRoot(schema));
	}
}