package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class TableRowMoveDownToEndAction extends Action {
	private TableViewer viewer;
	/**
	 * ��ѡ�����ƶ����׵Ĳ���Action
	 * @param viewer
	 */
	public TableRowMoveDownToEndAction(TableViewer viewer) {
		setText(Messages.TableRowMoveDownToEndAction_0);
		this.viewer = viewer;
		this.setToolTipText(Messages.TableRowMoveDownToEndAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DOWN_TO_END));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DOWN_TO_END_DISABLED));
		this.setEnabled(false);
	}
	
	public void run() {
		Object selection = ((StructuredSelection)viewer.getSelection()).getFirstElement();
		ArrayList list = ((ArrayList)viewer.getInput());
		
	    int selectIndex = list.indexOf(selection);
	    if(selectIndex < (list.size()-1)){
	    	Object $this = list.get(selectIndex);
	    	list.remove(selectIndex);
	    	
	    	list.add($this);
	    	
	    	viewer.setInput(list);
	    	viewer.refresh();
	    }
	}
	
}
