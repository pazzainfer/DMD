package com.leven.dmd.gef.action;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.command.TableDeleteCommand;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.part.TablePart;

public class CutAction extends SelectionAction{

	/**
	 * ͼԪ����Action
	 * @param part
	 */
    public CutAction(IWorkbenchPart part) {
        super(part);
        setId(ActionFactory.CUT.getId());
        setText(Messages.CutAction_0);
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
        		getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
        setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
        		getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
    }

    @Override
    protected boolean calculateEnabled() {        
    	List list = ((SchemaDiagramEditor)getWorkbenchPart()).getGraphicalViewer().getSelectedEditParts();
    	if(list != null && list.size() == 1){
    		return true;
    	} else {
    		return false;
    	}
    }

    @Override
	public void run() {
    	TablePart part = (TablePart)((List)Clipboard.getDefault().getContents()).get(0);
    	TableDeleteCommand deleteTableCommand = new TableDeleteCommand((Schema)part.getParent().getModel(),part.getTable());
        ((SchemaDiagramEditor)getWorkbenchPart()).getCommandStack().execute(deleteTableCommand);
		Clipboard.getDefault().setContents(super.getSelectedObjects());
	}
}