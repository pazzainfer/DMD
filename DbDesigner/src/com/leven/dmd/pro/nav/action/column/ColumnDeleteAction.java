package com.leven.dmd.pro.nav.action.column;

import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

public class ColumnDeleteAction extends Action {
	private Object obj;
	
	public ColumnDeleteAction(Object obj) {
		super();
		this.setText(Messages.ColumnDeleteAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.DELETE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object data = ((INavigatorTreeNode)obj).getData();
		if(data==null || !(data instanceof Column)){
			return;
		}
		Column column = (Column)data;
		Table table = column.getTable();
		MessageBox mb1 = new MessageBox(Display.getCurrent().getActiveShell(),SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		mb1.setText(Messages.ColumnDeleteAction_0);
		mb1.setMessage(Messages.ColumnDeleteAction_4);
		if(mb1.open()==SWT.YES){
			for(Iterator<Relationship> it = table.getForeignKeyRelationships().iterator();it.hasNext();){
				if(column.getName().equals(it.next().getPrimaryKeyColumn())){
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.ColumnDeleteAction_1);
					mb.setMessage(Messages.ColumnDeleteAction_2);
					mb.open();
					return;
				}
			}
			for(Iterator<Relationship> it = table.getPrimaryKeyRelationships().iterator();it.hasNext();){
				if(column.getName().equals(it.next().getPrimaryKeyColumn())){
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.ColumnDeleteAction_1);
					mb.setMessage(Messages.ColumnDeleteAction_2);
					mb.open();
					return;
				}
			}
			table.removeColumn(column);
			if(table.getStatus()==IEditorConstant.TABLE_STATUS_NORMAL){
				table.setStatus(IEditorConstant.TABLE_STATUS_CHANGED);
			}
			
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if(page!=null){
				try{
					IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
					if(registry!=null && registry instanceof SchemaNavigatorView){
						SchemaNavigatorView view = (SchemaNavigatorView)registry;
						view.getCommonViewer().refresh(table);
					}
					((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}
