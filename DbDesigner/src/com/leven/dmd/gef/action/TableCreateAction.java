package com.leven.dmd.gef.action;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.wizard.CustomWizardDialog;
import com.leven.dmd.gef.wizard.TableAddWizard;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;
import com.leven.dmd.pro.util.DbTableUtil;

public class TableCreateAction extends EditorPartAction {
	public static final String ID = "dhcc_platform.gef.action.TableCreateAction"; //$NON-NLS-1$
	
	private Point location;
		
	public TableCreateAction(IEditorPart editor) {
		super(editor);
	}

	public TableCreateAction() {
		super(null);
	}

	public TableCreateAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	protected void init() {
		super.init();
		setId(ID);
		setToolTipText(Messages.TableCreateAction_1);
		setText(Messages.TableCreateAction_1);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.ADD_TABLE));
	}

	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		SchemaDiagramEditor editor = (SchemaDiagramEditor) getEditorPart();
		if (editor == null) {
			editor = (SchemaDiagramEditor) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		}
		
		Schema schema = (Schema)editor.getModel();
		int type;
		if(schema.isPackageOpen()){
			type = INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE;
		}else {
			type = INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER;
		}
		Table table = new Table(""); //$NON-NLS-1$
		table.setName(""); //$NON-NLS-1$
		CustomWizardDialog wd = new CustomWizardDialog(shell,new TableAddWizard(schema,table),false);
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
						mb.setText(Messages.TableCreateAction_3);
						mb.setMessage(Messages.TableCreateAction_4+table.getName()+Messages.TableCreateAction_5);
				
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
						 MessageBox mb = new MessageBox(shell,SWT.ICON_INFORMATION);
						 mb.setText(Messages.TableCreateAction_6);
						 mb.setMessage(Messages.TableCreateAction_7);
						 mb.open();
					 }else{
						 MessageBox mb = new MessageBox(shell,SWT.ICON_WARNING);
						 mb.setText(Messages.TableCreateAction_8);
						 mb.setMessage(Messages.TableCreateAction_2);
						 mb.open();
					 }
				 }else {
					 MessageBox mb = new MessageBox(shell,SWT.ICON_ERROR);
					 mb.setText(Messages.TableCreateAction_8);
					 mb.setMessage(Messages.TableCreateAction_9);
					 mb.open();
				 }
			}
		}
		table.setLocation(location);
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				if(type == INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
					TablePackage tablePackage = schema.getOpenPackage();
					table.setSchema(schema);
					tablePackage.addTable(table);
					NavigatorViewUtil.refresh(tablePackage);
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

	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
}