package com.leven.dmd.pro.nav.action.pack;

import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

public class TablePackageEditAction extends Action {
	private Object obj;
	
	public TablePackageEditAction(Object obj) {
		super();
		this.setText(Messages.TablePackageEditAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.EDIT_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object root = ((INavigatorTreeNode)obj).getRoot();
		if(root==null || !(root instanceof Schema)){
			return;
		}
		Schema schema = (Schema)root;
		TablePackage tablePackage = (TablePackage)obj;
		List<TablePackage> packageList = schema.getTablePackages();
		TablePackageEditWizard wizard = new TablePackageEditWizard(tablePackage,packageList);
		WizardDialog wd = new WizardDialog(Display.getCurrent().getActiveShell(),wizard);
		wd.create();
		wd.open();
		TablePackage tablePackage1;
		if((tablePackage1 = wizard.getTablePackage())!=null){
			tablePackage.setDescription(tablePackage1.getDescription());
			tablePackage.modifyName(tablePackage1.getName());
		}else {
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
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
