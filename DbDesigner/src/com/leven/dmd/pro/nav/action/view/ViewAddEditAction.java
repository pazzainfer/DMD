package com.leven.dmd.pro.nav.action.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class ViewAddEditAction extends Action {
	private Object obj;
	
	public ViewAddEditAction(Object obj) {
		super();
		this.obj=obj;
		if(obj instanceof DBView){
			this.setText(Messages.ViewAddEditAction_2);
			this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.EDIT_IMAGE_PATH));
		}else {
			this.setText(Messages.ViewAddEditAction_3);
			this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.ADD_IMAGE_PATH));
		}
	}
	
	@Override
	public void run() {
		Object root = ((INavigatorTreeNode)obj).getRoot();
		if(root==null || !(root instanceof Schema)){
			return;
		}
		Schema schema = (Schema)root;
		
		DBView view = null;
		if(obj instanceof DBView){
			view = (DBView)obj;
		}
		ViewAddEditWizard wizard = new ViewAddEditWizard(schema,view);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),wizard);
		dialog.create();
		DBView newView = null;
		if(dialog.open()==Dialog.OK){
			newView = wizard.getData();
			if(newView==null){
				return;
			}
		}else {
			newView = null;
			return;
		}
		if(view==null){	//新增
			NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_VIEW);
		}else {
			NavigatorViewUtil.refresh(obj);
		}
		NavigatorViewUtil.setEditorDirty(true);
	}

}
