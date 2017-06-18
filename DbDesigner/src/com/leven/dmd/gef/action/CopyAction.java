package com.leven.dmd.gef.action;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.part.TablePart;

public class CopyAction extends SelectionAction{

	/**
	 * ͼԪ����Action
	 * @param part
	 */
    public CopyAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.CopyAction_0);
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
        		getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
        		getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
        setId(ActionFactory.COPY.getId());
    }

    @Override
    protected boolean calculateEnabled() {
    	List list = ((SchemaDiagramEditor)getWorkbenchPart()).getGraphicalViewer().getSelectedEditParts();
    	if(list != null && list.size() == 1){
    		if(list.get(0) instanceof TablePart){
    			return true;
    		}else {
    			return false;
    		}
    	} else {
    		return false;
    	}
    }

	@Override
	public void run() {
		Clipboard.getDefault().setContents(super.getSelectedObjects());
	}

}