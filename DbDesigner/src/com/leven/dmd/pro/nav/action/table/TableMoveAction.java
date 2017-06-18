package com.leven.dmd.pro.nav.action.table;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
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
import com.leven.dmd.pro.util.control.dialog.TablePackageSelectDialog;

public class TableMoveAction extends Action {
	private Object obj;
	
	public TableMoveAction(Object obj) {
		super();
		this.setText(Messages.TableMoveAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.MOVE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object root = ((INavigatorTreeNode)obj).getRoot();
		if(root==null || !(root instanceof Schema)){
			return;
		}
		Schema schema = (Schema)root;
		Table table = (Table)obj;
		
		TablePackageSelectDialog tableSelectDialog = new TablePackageSelectDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell(),schema);
		tableSelectDialog.create();
		Object data = null;
		if(tableSelectDialog.open()==Dialog.OK){
			data = tableSelectDialog.getData();
		}else {
			return;
		}
		if(data != null){
			Object parent;
			if(data instanceof Schema){	//移动至主画布
				if((parent=table.getParent()) instanceof TablePackage){	//from TablePackage
					((TablePackage)parent).removeTable(table);
					schema.addTable(table);
					NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
					NavigatorViewUtil.refresh(parent);
				}else {
					return;
				}
			}else if(data instanceof TablePackage){	//移动至主题中
				TablePackage newPackage = (TablePackage)data;
				if((parent=table.getParent()) instanceof TablePackage){
					if(((TablePackage)parent).getPath().equals(((TablePackage)data).getPath())){
						return;
					}
					TablePackage pPackage = (TablePackage)parent;
					if(!pPackage.getName().equals(newPackage.getName())){
						pPackage.removeTable(table);
						newPackage.addTable(table);
						NavigatorViewUtil.refresh(pPackage);
						NavigatorViewUtil.refresh(newPackage);
					}
				}else {
					schema.removeTable(table);
					newPackage.addTable(table);
					NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
					NavigatorViewUtil.refresh(newPackage);
				}
			}
		}
	}

}
