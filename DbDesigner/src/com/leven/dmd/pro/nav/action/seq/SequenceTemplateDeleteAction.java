package com.leven.dmd.pro.nav.action.seq;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class SequenceTemplateDeleteAction extends Action {
	private Object obj;
	
	public SequenceTemplateDeleteAction(Object obj) {
		super();
		this.setText(Messages.SequenceTemplateDeleteAction_0);
		this.obj = obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.DELETE_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		super.run();
		MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		mb.setText(Messages.SequenceTemplateDeleteAction_1);
		mb.setMessage(Messages.SequenceTemplateDeleteAction_2);
		if(mb.open()==SWT.YES){
			((SequenceTemplate)obj).getSchemaTemplate().removeSequenceTemplate((SequenceTemplate)obj);
			
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_SEQUENCE);
		}
		
	}
}
