package com.leven.dmd.pro.nav.action.db;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.db.DBOperator;
import com.leven.dmd.pro.pref.dialog.DBTypeSelectDialog;
import com.leven.dmd.pro.util.DbTableUtil;
import com.leven.dmd.pro.util.FileUtil;

public class SingleDllExportAction extends Action {
	private Object obj;
	private static final String[] EXTENSIONS = new String[] { "*.ddl", "*.sql", "*.txt"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	/**
	 * 单一对象的导出功能
	 * @param obj
	 */
	public SingleDllExportAction(Object obj) {
		super();
		this.setText(Messages.SingleDllExportAction_0);
		this.setToolTipText(Messages.SingleDllExportAction_0);
		this.obj = obj;
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DLL));
	}

	@Override
	public void run() {
		DBTypeSelectDialog selectDialog = new DBTypeSelectDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		selectDialog.create();
		if(selectDialog.open()!=Dialog.OK){
			return;
		}
		String type = selectDialog.getDbType();
		
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.SingleDllExportAction_2);
			return;
		}
		if(savePath!=null){
			String fileContent = null;
			if(obj instanceof SequenceTemplate){
				fileContent = DbTableUtil.getSequenceSql((SequenceTemplate)obj, type);
			}else if(obj instanceof DBView){
				fileContent = DbTableUtil.getViewSql((DBView)obj, type);
			}else if(obj instanceof Tablespace){
				fileContent = DbTableUtil.getTablespaceSql((Tablespace)obj, type);
			}else if(obj instanceof Table){
				fileContent = DBOperator.getTableDDL((Table)obj, type);
			}else if(obj instanceof TablePackage){
				fileContent = DbTableUtil.getPackageDllContent((TablePackage)obj, type);
			}else {
				return;
			}
			if(FileUtil.makeNewFileWithContent(fileContent, savePath)){
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						getText(), Messages.SingleDllExportAction_3);
			}else {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						getText(), Messages.SingleDllExportAction_4);
			}
		}
	}
}
