package com.leven.dmd.gef.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.command.TablePasteCommand;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.part.TablePart;
import com.leven.dmd.gef.part.TableTreeEditPart;
import com.leven.dmd.gef.util.IEditorConstant;

public class PasteAction extends SelectionAction {

	/**
	 * ͼԪճ��Action
	 * @param part
	 */
	public PasteAction(IWorkbenchPart part) {
		super(part);
		setId(ActionFactory.PASTE.getId());
        setText(Messages.PasteAction_0 + "@Ctrl+V"); //$NON-NLS-2$
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
        		getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
        		getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
	}

	@Override
	protected boolean calculateEnabled() {
		if (Clipboard.getDefault().getContents() != null) {
			if(!((List)Clipboard.getDefault().getContents()).isEmpty()){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void run() {
		List list = (List)Clipboard.getDefault().getContents();
        for(int i=0;i<list.size();i++){
        	EditPart part;
        	if(list.get(i) instanceof TableTreeEditPart){
	            part = (TableTreeEditPart)list.get(i);
        	} else {
        		part = (TablePart)list.get(i);
        	}
            Table model = (Table)part.getModel();
            Schema parent = (Schema)part.getParent().getModel();
            
            Table clone = new Table(model.getName()
            		,model.getPath(),model.getSchema());
            clone.modifyName("CopyOf" + model.getName() + (parent.getTables().size() + 1));
            clone.setCnName(Messages.PasteAction_3 + model.getCnName());
            clone.setStatus(IEditorConstant.TABLE_STATUS_ADDED);
            clone.setDescription(model.getDescription());
            clone.setLocation(model.getBounds().getLocation());
            
            Column columnTemp;
            for(Iterator<Column> it = model.getColumns().iterator();it.hasNext();){
            	columnTemp = it.next();
            	clone.addColumn(new Column(columnTemp.getName(), columnTemp.getType(), 
            			columnTemp.getLength(),columnTemp.getCnName(), columnTemp.isPk()));
            }
            
            TableIndex indexTemp;
            ArrayList<TableIndex> indexes = new ArrayList<TableIndex>();
            for(Iterator<TableIndex> it = model.getIndexes().iterator();it.hasNext();){
            	indexTemp = it.next();
            	indexes.add(new TableIndex(indexTemp.getName(), indexTemp.getColumns(), 
            			indexTemp.getType(),indexTemp.getComments()));
            }
            clone.setIndexes(indexes);
            
            TablePasteCommand tablePasteCommand = new TablePasteCommand(parent,clone);
            ((SchemaDiagramEditor)getWorkbenchPart()).getCommandStack().execute(tablePasteCommand);
        }
        
        Clipboard.getDefault().setContents(Collections.EMPTY_LIST);
        this.setEnabled(false);
	}

}