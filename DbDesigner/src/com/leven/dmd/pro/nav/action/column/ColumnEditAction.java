package com.leven.dmd.pro.nav.action.column;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnEditWizard;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

public class ColumnEditAction extends Action {
	private Object obj;
	
	public ColumnEditAction(Object obj) {
		super();
		this.setText(Messages.ColumnEditAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.EDIT_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object data = ((INavigatorTreeNode)obj).getData();
		if(data==null || !(data instanceof Column)){
			return;
		}
		Column column = (Column)data;
		Table table = column.getTable();
		
		Map<String,Column> map = new HashMap<String,Column>();
		
		ColumnEditWizard wizard = new ColumnEditWizard(table.getSchema(),map,table.getColumnMap(),column);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),wizard);
		dialog.create();
		if(dialog.open()==Dialog.OK){
			Column newColumn = map.get("data"); //$NON-NLS-1$
			int index = table.getColumns().indexOf(column);
			table.removeColumn(column);
			table.addColumn(newColumn, index);
		}else {
			return;
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
