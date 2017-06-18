package com.leven.dmd.pro.nav.action.pack;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class TablePackageDeleteAction extends Action {
	private Object obj;
	
	public TablePackageDeleteAction(Object obj) {
		super();
		this.setText(Messages.TablePackageDeleteAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.DELETE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object root = ((INavigatorTreeNode)obj).getRoot();
		if(root==null || !(root instanceof Schema)){
			return;
		}
		Schema schema = (Schema)root;
		TablePackage tablePackage = (TablePackage)obj;
		
		MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setText(Messages.TablePackageDeleteAction_1);
		mb.setMessage(Messages.TablePackageDeleteAction_2);
		if(mb.open()==SWT.YES){
			String path = tablePackage.getPath();
			Object parent = ((INavigatorTreeNode)obj).getParent();
			if(parent instanceof TablePackage){
				removePackageChildren(tablePackage, schema);
				((TablePackage)parent).removeTablePackage(tablePackage);
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
				NavigatorViewUtil.refresh(parent);
			}else {
				removePackageChildren(tablePackage, schema);
				schema.removeTablePackage(tablePackage);
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_PACKAGE);
			}
			if(schema.getPackagePath()!=null && schema.getPackagePath().contains(path)){
				schema.commitGoTop();
			}
		}
	}

	/**
	 * 删除主题下面所有表跟包
	 * @param tablePackage2
	 * @param schema2
	 */
	private void removePackageChildren(TablePackage tablePackage2,
			Schema schema2) {
		for(Table child : tablePackage2.getTables()){
			schema2.getAllTables().remove(child);
			schema2.getTablesMap().remove(child.getPath());
		}
		for(TablePackage child : tablePackage2.getTablePackages()){
			removePackageChildren(child,schema2);
			schema2.getTablePackagesMap().remove(child.getPath());
		}
	}
}
