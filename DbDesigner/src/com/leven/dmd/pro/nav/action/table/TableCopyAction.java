package com.leven.dmd.pro.nav.action.table;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.wizard.CustomWizardDialog;
import com.leven.dmd.gef.wizard.TableAddWizard;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;
import com.leven.dmd.pro.util.control.dialog.TableSelectDialog;

public class TableCopyAction extends Action {
	private Object obj;
	
	public TableCopyAction(Object obj) {
		super();
		this.setText(Messages.TableCopyAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.COPY_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		Object value = ((INavigatorTreeNode)obj).getRoot();
		int type = ((INavigatorTreeNode)obj).getNodeType();
		if(value==null || !(value instanceof Schema)){
			return;
		}
		Schema schema = (Schema)value;
		TableSelectDialog tableSelectDialog = new TableSelectDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell(),schema);
		tableSelectDialog.create();
		Table table;
		if(tableSelectDialog.open()==Dialog.OK){
			table = tableSelectDialog.getData();
			table.modifyName("CopyOf" + table.getName()); //$NON-NLS-1$
		}else {
			return;
		}
		
		TableAddWizard wizard = new TableAddWizard(schema,table);
		wizard.setWindowTitle(Messages.TableCopyAction_0);
		CustomWizardDialog wd = new CustomWizardDialog(Display.getCurrent().getActiveShell(),wizard,false);
		wd.setPageSize(600,300);
		int result;
		if((result = wd.open())==1){
			table = null;
			return;
		}
		if(table!=null){
			if(result == 2){
				DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
				dialog.create();
				if(dialog.open()!=Dialog.OK){
					return;
				}
				DBConfigModel config = dialog.getDbConfigModel();
				boolean configOk,createOk=true;
				if(!DBConfigUtil.isConfigAvailable(config)){
					configOk = false;
				}else {
					configOk = true;
				}
				if(configOk){
					if(DbTableUtil.CheckTableExist(table,config)){
						MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						mb.setText(Messages.TableCopyAction_2);
						mb.setMessage(Messages.TableCopyAction_3+table.getName()+Messages.TableCopyAction_4);
				
						if(mb.open()==SWT.YES){
							if(DbTableUtil.createTableByDomian(table,false,config)){
								createOk = createOk && true;
								table.setStatus(IEditorConstant.TABLE_STATUS_NORMAL);
							}else {
								createOk = createOk && false;
							}
						}
					}else{
						if(DbTableUtil.createTableByDomian(table,false,config)){
							createOk = createOk && true;
							table.setStatus(IEditorConstant.TABLE_STATUS_NORMAL);
						}else {
							createOk = createOk && false;
						}
					}
				}else {
					table.setStatus(IEditorConstant.TABLE_STATUS_ADDED);
				}
				 if(configOk){
					 if(createOk){
						 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
						 mb.setText(Messages.TableCopyAction_5);
						 mb.setMessage(Messages.TableCopyAction_6);
						 mb.open();
					 }else {
						 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_WARNING);
						 mb.setText(Messages.TableCopyAction_2);
						 mb.setMessage(Messages.TableCopyAction_7);
						 mb.open();
					 }
				 }else {
					 MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					 mb.setText(Messages.TableCopyAction_2);
					 mb.setMessage(Messages.TableCopyAction_8);
					 mb.open();
				 }
			}
		}
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				schema = ((SchemaDiagramEditor)page.getActiveEditor()).getSchema();
				if(type == INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
					TablePackage tablePackage = schema.getTablePackagesMap().get(((TablePackage)(((INavigatorTreeNode)obj).getData())).getName());
					table.setSchema(schema);
					tablePackage.addTable(table);
					NavigatorViewUtil.refresh(obj);
				}else if(type == INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER) {
					table.setSchema(schema);
					schema.addTable(table);
					NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
				}
				((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
