package com.leven.dmd.pro.nav.action.column;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.gef.wizard.tableeditor.action.ColumnAddWizard;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

public class ColumnAddAction extends Action {
	private Object obj;
	
	public ColumnAddAction(Object obj) {
		super();
		this.setText(Messages.ColumnAddAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.ADD_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object root = ((INavigatorTreeNode)obj).getRoot();
		if(root==null || !(root instanceof Schema)){
			return;
		}
		Object data = ((INavigatorTreeNode)obj).getData();
		if(data==null || !(data instanceof Table)){
			return;
		}
		Table table = (Table)data;
		Schema schema = (Schema)root;
		Map<String,Column> map = new HashMap<String,Column>();
		ColumnAddWizard wizard = new ColumnAddWizard(schema,map,table.getColumnMap());
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),wizard);
		dialog.create();
		Column column = null;
		if(dialog.open()==Dialog.OK){
			column = map.get("data"); //$NON-NLS-1$
			if(column==null){
				return;
			}
			List<Column> columnList = table.getColumns();
			Column columnTemp;
			for (Iterator<Column> it = columnList.iterator(); it.hasNext();) {
				columnTemp = it.next();
				if (columnTemp.getName().equals(column.getName())) {
					return;
				}
			}
			column.setTable(table);
			table.addColumn(column);
		}else {
			column = null;
			return;
		}
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
				if(registry!=null && registry instanceof SchemaNavigatorView){
					SchemaNavigatorView view = (SchemaNavigatorView)registry;
					view.getCommonViewer().refresh(obj);
				}
				((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
//				((SchemaDiagramEditor)page.getActiveEditor()).doSave(null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
