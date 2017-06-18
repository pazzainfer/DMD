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
 * 引用外部模板文件
 * @author leven
 * create at 2013-10-25 上午5:01:25
 */
public class TemplateQuoteFromFileAction extends Action {
	private Object obj;
	
	public TemplateQuoteFromFileAction(Object obj) {
		super();
		this.setText(Messages.TemplateQuoteFromFileAction_0);
		this.obj=obj;
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.IMPORT_IMAGE_PATH));
	}
	
	@Override
	public void run() {
		if(obj==null || !(obj instanceof INavigatorTreeNode)){
			return;
		}
		Schema schema = ((Schema)((INavigatorTreeNode)obj).getRoot());
		SchemaTemplate schemaTemplate = schema.getSchemaTemplate();
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
					getText(), Messages.TemplateQuoteFromFileAction_2);
			return;
		}
		if(filePath!=null){
			for(String quotePath : schema.getOutTempPaths()){
				if(filePath.equals(quotePath)){
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.TemplateQuoteFromFileAction_1);
					mb.setMessage(Messages.TemplateQuoteFromFileAction_5);
					mb.open();
					return;
				}
			}
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
						temp.setOutQuote(true);
						schemaTemplate.addColumnTemplate(temp);
					}
					schema.getOutTempPaths().add(filePath);
					NavigatorViewUtil.refreshRootFolder(new int[]{2,3});
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							getText(), Messages.TemplateQuoteFromFileAction_3);
				}else {
					MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
					mb.setText(Messages.TemplateQuoteFromFileAction_1);
					mb.setMessage(Messages.TemplateQuoteFromFileAction_7);
					mb.open();
				}
			}catch(Exception e){
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						getText(), Messages.TemplateQuoteFromFileAction_4);
				return;
			}
		}
	}

}
