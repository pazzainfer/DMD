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
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.util.control.dialog.TableSelectDialog;
/**
 * 数据表引用创建（创建一个表，引用另一个表的信息，只可更改表名）
 * @author leven
 * create at 2013-10-28 上午7:41:57
 */
public class TableQuoteCreateAction extends EditorPartAction {
	public static final String ID = "dhcc_platform.gef.action.TableQuoteCreateAction"; //$NON-NLS-1$
	
	private Point location;
		
	public TableQuoteCreateAction(IEditorPart editor) {
		super(editor);
	}

	public TableQuoteCreateAction() {
		super(null);
	}

	public TableQuoteCreateAction(IEditorPart editor, int style) {
		super(editor, style);
	}

	protected boolean calculateEnabled() {
		return true;
	}

	protected void init() {
		super.init();
		setId(ID);
		setToolTipText(Messages.TableQuoteCreateAction_0);
		setText(Messages.TableQuoteCreateAction_0);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.ACTION_QUOTE));
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
		TablePackage tablePackage = null;
		if(schema.isPackageOpen()){
			type = INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE;
			tablePackage = schema.getOpenPackage();
		}else {
			type = INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER;
		}
		TableSelectDialog dialog = new TableSelectDialog(Display.getCurrent().getActiveShell(),schema);
		dialog.create();
		Table table = null;
		if(dialog.open()==Dialog.OK){
			table = dialog.getData();
			if(table!=null){
				table.setQuotePath(new String(table.getPath()));
				table.setName(table.getName()+"_QUOTE"); //$NON-NLS-1$
				if(type==INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER){
					table.setPath(""); //$NON-NLS-1$
				}else {
					table.setPath(tablePackage.getPath()+"/"+table.getName()); //$NON-NLS-1$
				}
				MessageBox mb = new MessageBox(shell,SWT.ICON_INFORMATION);
				mb.setText(Messages.TableQuoteCreateAction_5);
				mb.setMessage(Messages.TableQuoteCreateAction_6);
				mb.open();
			}else {
				return;
			}
		}else {
			return;
		}
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if(page!=null){
			try{
				if(type == INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
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