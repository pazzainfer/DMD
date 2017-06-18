package com.leven.dmd.gef.action.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.util.DbTableUtil;

public class ExportInformixDllAction extends SelectionAction {

	public static final String ID = "com.leven.dmd.gef.action.export.ExportInformixDllAction"; //$NON-NLS-1$

	private static final String[] EXTENSIONS = new String[] { "*.ddl", "*.sql", "*.txt"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public ExportInformixDllAction(IWorkbenchPart part) {
		super(part);
		this.setImageDescriptor(AbstractUIPlugin.
				imageDescriptorFromPlugin(Activator.PLUGIN_ID, ImageKeys.INFORMIX));
	}

	@Override
	protected boolean calculateEnabled() {
		IWorkbenchPart part = getWorkbenchPart();
		if (part instanceof GraphicalEditor) {
			return true;
		}else if (part instanceof ContentOutline) {
			return true;
		}
		return false;
	}

	protected void init() {
		super.init();
		setText(Messages.ExportInformixDllAction_4);
		this.setToolTipText(Messages.ExportInformixDllAction_4);
		setId(ID);
	}

	public void run() {
		FileDialog dialog = new FileDialog(getWorkbenchPart().getSite()
				.getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(getWorkbenchPart().getSite().getShell(),
					getText(), Messages.ExportInformixDllAction_6);
			return;
		}
		
		String fileContent = DbTableUtil.getSchemaDllContent(
				((SchemaDiagramEditor)getWorkbenchPart()).getSchema(),"informix"); //$NON-NLS-1$

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
				MessageBox mbox = new MessageBox(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION);
				mbox.setText(Messages.ExportInformixDllAction_8);
				mbox.setMessage(Messages.ExportInformixDllAction_9);
				mbox.open();
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