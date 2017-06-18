package com.leven.dmd.pro.nav.action.pack;

import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class TablePackageAddAction extends Action {
	private Object obj;
	
	public TablePackageAddAction(Object obj) {
		super();
		this.setText(Messages.TablePackageAddAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.ADD_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object root = ((INavigatorTreeNode)obj).getRoot();
		if(root==null || !(root instanceof Schema)){
			return;
		}
		Schema schema = (Schema)root;
		TablePackage tablePackage;
		List<TablePackage> packageList = schema.getTablePackages();
		TablePackageAddWizard wizard = new TablePackageAddWizard(packageList);
		WizardDialog wd = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),wizard);
		wd.create();
		wd.open();
		if((tablePackage = wizard.getTablePackage())!=null){
			if(schema.isPackageOpen()){
				if(obj instanceof TablePackage){
					((TablePackage)obj).addTablePackage(tablePackage);
				}else {
					schema.getTablePackages().add(tablePackage);
					schema.getTablePackagesMap().put(tablePackage.getName(), tablePackage);
				}
			}else {
				if(obj instanceof TablePackage){
					((TablePackage)obj).addTablePackage(tablePackage);
				}else {
					schema.addTablePackage(tablePackage);
				}
			}
		}else {
			return;
		}
//		tablePackage.modifyBounds(new Rectangle(0,0,50,65));
		if(obj instanceof TablePackage){
			NavigatorViewUtil.refresh(obj);
		}else {
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_PACKAGE);
		}
		NavigatorViewUtil.setEditorDirty(true);
	}

}
