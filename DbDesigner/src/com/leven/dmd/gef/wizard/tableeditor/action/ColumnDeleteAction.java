package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.wizard.tableeditor.CustomIndexViewer;
import com.leven.dmd.gef.wizard.tableeditor.CustomTableViewer;
import com.leven.dmd.pro.Activator;

public class ColumnDeleteAction extends Action {
	private TableViewer viewer;
	private Map<String,Column> columnMap;
	private CustomTableViewer indexViewer;

	public ColumnDeleteAction(TableViewer viewer,Map<String,Column> columnMap,CustomTableViewer indexViewer) {
		setText(Messages.ColumnDeleteAction_0);
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.indexViewer = indexViewer;
		this.setToolTipText(Messages.ColumnDeleteAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_DELETE));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.COLUMN_DELETE_DISABLED));
		this.setEnabled(false);
	}

	public void run() {
		StructuredSelection select = (StructuredSelection) viewer
				.getSelection();
		if(select.isEmpty()){
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_ERROR);
			mb.setText(Messages.ColumnDeleteAction_2);
			mb.setMessage(Messages.ColumnDeleteAction_3);
			mb.open();
		}else{
			ArrayList columnList = ((ArrayList)viewer.getInput());
//			Column p = (Column) select.getFirstElement();
			Column p;
			for(Iterator<Column> it = select.iterator();it.hasNext();){
				p = it.next();
				if(p.isPk()){
					indexViewer.getIndexViewer().refreshWhenColumnNotPK(p.getName());
				}
				viewer.remove(p);
				columnList.remove(p);// smx�����û�����д��룬�ڵ���ͷ����ʱ����ӵļ�¼�ᶪʧ
				columnMap.remove(p.getName().toLowerCase());
			}
		}
	}

}
