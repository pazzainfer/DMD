package com.leven.dmd.pro.nav.action.db;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.db.DBOperator;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.util.DbTableUtil;
import com.leven.dmd.pro.util.FileUtil;

public class ExportDB2DllAction extends Action {

	public static final String ID = "com.leven.dmd.pro.nav.action.db.ExportDB2DllAction"; //$NON-NLS-1$

	private static final String[] EXTENSIONS = new String[] { "*.dll", "*.sql", "*.txt"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	private Object obj;

	public ExportDB2DllAction(Object obj) {
		this.obj = obj;
		setText(Messages.ExportDB2DllAction_4);
		this.setToolTipText(Messages.ExportDB2DllAction_4);
		setId(ID);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.DB2));
	}

	public void run() {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell(), getText(), Messages.ExportDB2DllAction_6);
			return;
		}
		String fileContent = null;
		if(((INavigatorTreeNode)obj).getNodeType()==INavigatorNodeTypeConstants.TYPE_SCHEMA_FILE){
			Object root = ((INavigatorTreeNode)obj).getData();
			if(root==null || !(root instanceof Schema)){
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell(), getText(), Messages.ExportDB2DllAction_6);
				return;
			}
			fileContent = DbTableUtil.getSchemaDllContent((Schema)root,"db2"); //$NON-NLS-1$
		} else if(((INavigatorTreeNode)obj).getNodeType()==INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
			fileContent = DbTableUtil.getPackageDllContent((TablePackage)((INavigatorTreeNode)obj).getData(),"db2"); //$NON-NLS-1$
		} else if(((INavigatorTreeNode)obj).getNodeType()==INavigatorNodeTypeConstants.TYPE_TABLE_NODE){
			fileContent = DBOperator.getTableDDL((Table)((INavigatorTreeNode)obj).getData(),"db2"); //$NON-NLS-1$
		} else {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell(), getText(), Messages.ExportDB2DllAction_6);
			return;
		}

		if(FileUtil.makeNewFileWithContent(fileContent, savePath)){
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.ExportDB2DllAction_5);
		}else {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.ExportDB2DllAction_6);
		}
	}
}