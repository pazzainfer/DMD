package com.leven.dmd.pro.pref.action;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.util.ImageKeys;

public class DBConfigDeleteAction extends Action {
	private TableViewer viewer;
	private Map<String,DBConfigModel> columnMap;

	public DBConfigDeleteAction(TableViewer viewer,Map<String,DBConfigModel> columnMap) {
		setText(Messages.DBConfigDeleteAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.setToolTipText(Messages.DBConfigDeleteAction_1);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.ACTION_DELETE));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.ACTION_DELETE_DISABLED));
		this.setEnabled(false);
	}

	public void run() {
		StructuredSelection select = (StructuredSelection) viewer
				.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.DBConfigDeleteAction_2);
			mb.setMessage(Messages.DBConfigDeleteAction_3);
			mb.open();
		}else{
			ArrayList columnList = ((ArrayList)viewer.getInput());
			DBConfigModel p = (DBConfigModel) select.getFirstElement();
			viewer.remove(p);
			columnList.remove(p);// smx�����û�����д��룬�ڵ���ͷ����ʱ����ӵļ�¼�ᶪʧ
			columnMap.remove(p.getName().toLowerCase());
		}
	}

}
