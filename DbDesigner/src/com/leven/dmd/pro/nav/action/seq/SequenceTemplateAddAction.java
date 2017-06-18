package com.leven.dmd.pro.nav.action.seq;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.action.seq.SequenceTemplateAddWizard;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class SequenceTemplateAddAction extends Action {
	private Object obj;
	
	public SequenceTemplateAddAction(Object obj) {
		super();
		this.setText(Messages.SequenceTemplateAddAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.ADD_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		if(obj==null || !(obj instanceof INavigatorTreeNode)){
			return;
		}
		SchemaTemplate schemaTemplate = ((Schema)((INavigatorTreeNode)obj).getRoot()).getSchemaTemplate();
		WizardDialog wd = null;
		wd = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),new SequenceTemplateAddWizard(schemaTemplate));
		wd.setPageSize(500,300);
		
		wd.create();
		if(wd.open()==Dialog.OK){
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_SEQUENCE);
			NavigatorViewUtil.setEditorDirty(true);
		}
	}

}
