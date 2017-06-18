package com.leven.dmd.pro.nav.action.table;

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

public class TableDeleteAction extends Action {
	private Object obj;
	
	public TableDeleteAction(Object obj) {
		super();
		this.setText(Messages.TableDeleteAction_0);
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
		Table table = (Table)obj;
		
		MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setText(Messages.TableDeleteAction_1);
		mb.setMessage(Messages.TableDeleteAction_2);
		Object parent;
		if(mb.open()==SWT.YES){
			if((parent=table.getParent()) instanceof TablePackage){
				((TablePackage)parent).removeTable(table);
			}else {
				schema.removeTable(table);
			}
		}else {
			return;
		}
		if(parent==null || parent instanceof Schema){
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
		} else {
			NavigatorViewUtil.refresh(parent);
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
		}
	}

}
