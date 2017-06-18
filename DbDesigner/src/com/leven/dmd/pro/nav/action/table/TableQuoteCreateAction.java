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
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.nav.view.SchemaNavigatorView;
import com.leven.dmd.pro.util.control.dialog.TableSelectDialog;

public class TableQuoteCreateAction extends Action {
	private Object obj;
	
	public TableQuoteCreateAction(Object obj) {
		super();
		this.setText(Messages.TableQuoteCreateAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(ImageKeys.ACTION_QUOTE));
	}
	
	@Override
	public void run() {
		Object value = ((INavigatorTreeNode)obj).getRoot();
		int type = ((INavigatorTreeNode)obj).getNodeType();
		if(value==null || !(value instanceof Schema)){
			return;
		}
		Schema schema = (Schema)value;
		TableSelectDialog dialog = new TableSelectDialog(Display.getCurrent().getActiveShell(),schema);
		Table table = null;
		dialog.create();
		if(dialog.open()==Dialog.OK){
			table = dialog.getData();
			if(table!=null){
				table.setQuotePath(new String(table.getPath()));
				table.setName(table.getName()+"_QUOTE"); //$NON-NLS-1$
				if(type==INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER){
					table.setPath(""); //$NON-NLS-1$
				}else {
					TablePackage tablePackage = schema.getTablePackagesMap().get(((TablePackage)(((INavigatorTreeNode)obj).getData())).getPath());
					table.setPath(tablePackage.getPath()+"/"+table.getName()); //$NON-NLS-1$
				}
				MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell()
						,SWT.ICON_INFORMATION);
				mb.setText(Messages.TableQuoteCreateAction_4);
				mb.setMessage(Messages.TableQuoteCreateAction_5);
				mb.open();
			}else {
				return;
			}
		}else {
			return;
		}
		if(table!=null){
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if(page!=null){
				try{
					schema = ((SchemaDiagramEditor)page.getActiveEditor()).getSchema();
					if(type == INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
						TablePackage tablePackage = schema.getTablePackagesMap().get(((TablePackage)(((INavigatorTreeNode)obj).getData())).getPath());
						table.setSchema(schema);
						tablePackage.addTable(table);
					}else if(type == INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER) {
						table.setSchema(schema);
						schema.addTable(table);
					}
					IViewPart registry = page.findView(SchemaNavigatorView.VIEW_ID);
					if(registry!=null && registry instanceof SchemaNavigatorView){
						SchemaNavigatorView view = (SchemaNavigatorView)registry;
						view.getCommonViewer().refresh(obj);
						NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TABLE);
					}
					((SchemaDiagramEditor)page.getActiveEditor()).setDirty(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}
