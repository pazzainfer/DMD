package com.leven.dmd.pro.nav.action.space;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class TablespaceDeleteAction extends Action {
	private Object obj;
	
	public TablespaceDeleteAction(Object obj) {
		super();
		this.setText(Messages.TablespaceDeleteAction_0);
		this.obj = obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.DELETE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		super.run();
		MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setText(Messages.TablespaceDeleteAction_1);
		mb.setMessage(Messages.TablespaceDeleteAction_2);
		if(mb.open()==SWT.YES){
			Tablespace space = (Tablespace)obj;
			((Schema)space.getRoot()).removeTablespace(space);
			
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLESPACE);
		}
		
	}
}
