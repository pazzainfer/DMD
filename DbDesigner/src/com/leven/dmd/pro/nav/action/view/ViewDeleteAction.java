package com.leven.dmd.pro.nav.action.view;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class ViewDeleteAction extends Action {
	private Object obj;
	
	public ViewDeleteAction(Object obj) {
		super();
		this.setText(Messages.ViewDeleteAction_0);
		this.obj = obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.DELETE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		super.run();
		MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setText(Messages.ViewDeleteAction_1);
		mb.setMessage(Messages.ViewDeleteAction_2);
		if(mb.open()==SWT.YES){
			DBView view = (DBView)obj;
			((Schema)view.getRoot()).removeDBView(view);
			
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_VIEW);
		}
		
	}
}
