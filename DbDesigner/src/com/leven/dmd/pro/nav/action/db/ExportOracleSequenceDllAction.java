package com.leven.dmd.pro.nav.action.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.util.DbTableUtil;

public class ExportOracleSequenceDllAction extends Action {

	public static final String ID = "com.leven.dmd.pro.nav.action.db.ExportOracleSequenceDllAction"; //$NON-NLS-1$

	private static final String[] EXTENSIONS = new String[] { "*.dll", "*.sql", "*.txt"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	private Object obj;

	public ExportOracleSequenceDllAction(Object obj) {
		this.obj = obj;
		this.setText(Messages.ExportOracleSequenceDllAction_4);
		this.setToolTipText(Messages.ExportOracleSequenceDllAction_4);
		this.setId(ID);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.ORACLE));
	}



	public void run() {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.ExportOracleSequenceDllAction_6);
			return;
		}
		
		String fileContent = DbTableUtil.getSequenceSql((SequenceTemplate)obj, "oracle"); //$NON-NLS-1$

		if (savePath != null) {
			FileWriter fw = null;
			try {
				File file = new File(savePath);
				if(!file.exists()){
					file.createNewFile();
				}
				fw = new FileWriter(file);
				fw.write(fileContent);
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(fw != null){
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}