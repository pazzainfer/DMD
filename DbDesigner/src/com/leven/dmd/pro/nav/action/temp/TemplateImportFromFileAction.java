package com.leven.dmd.pro.nav.action.temp;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.util.FileUtil;
/**
 * 导入外部模板文件
 * @author leven
 * create at 2013-10-25 上午5:01:08
 */
public class TemplateImportFromFileAction extends Action {
	private Object obj;
	
	public TemplateImportFromFileAction(Object obj) {
		super();
		this.setText(Messages.TemplateImportFromFileAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.IMPORT_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		if(obj==null || !(obj instanceof INavigatorTreeNode)){
			return;
		}
		SchemaTemplate schemaTemplate = ((Schema)((INavigatorTreeNode)obj).getRoot()).getSchemaTemplate();
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
				, SWT.OPEN);
		dialog.setFilterExtensions(new String[]{"*.schema"}); //$NON-NLS-1$
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String filePath = null;
		try {
			filePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.TemplateImportFromFileAction_2);
			return;
		}
		if(filePath!=null){
			try{
				Object fileObj = FileUtil.readObjectFromFile(filePath);
				SchemaTemplate template = null;
				if(fileObj instanceof Schema){
					template = ((Schema)fileObj).getSchemaTemplate();
				}else if(fileObj instanceof SchemaTemplate){
					template = (SchemaTemplate)fileObj;
				}
				if(template!=null){
					for(ColumnTemplate temp : template.getColumnTemplates()){
						schemaTemplate.addColumnTemplate(temp);
					}
					NavigatorViewUtil.refreshRootFolder(new int[]{2,3});
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							getText(), Messages.TemplateImportFromFileAction_3);
				}else {
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.TemplateImportFromFileAction_1);
					mb.setMessage(Messages.TemplateImportFromFileAction_5);
					mb.open();
				}
			}catch(Exception e){
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						getText(), Messages.TemplateImportFromFileAction_4);
				return;
			}
		}
	}

}
