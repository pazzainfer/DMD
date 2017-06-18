package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class IndexDeleteAction extends Action {
	private TableViewer viewer;
	private Map<String,TableIndex> indexMap;

	public IndexDeleteAction(TableViewer viewer,Map<String,TableIndex> columnMap) {
		setText(Messages.IndexDeleteAction_0);
		this.viewer = viewer;
		this.indexMap = columnMap;
		this.setToolTipText(Messages.IndexDeleteAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.INDEX_DELETE));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.INDEX_DELETE_DISABLED));
		this.setEnabled(false);
	}

	public void run() {
		StructuredSelection select = (StructuredSelection) viewer
				.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.IndexDeleteAction_2);
			mb.setMessage(Messages.IndexDeleteAction_3);
			mb.open();
		}else{
			ArrayList indexList = ((ArrayList)viewer.getInput());
			TableIndex p = (TableIndex) select.getFirstElement();
			viewer.remove(p);
			indexList.remove(p);// smx�����û�����д��룬�ڵ����ͷ����ʱ����ӵļ�¼�ᶪʧ
			indexMap.remove(p.getName());
		}
	}

}
