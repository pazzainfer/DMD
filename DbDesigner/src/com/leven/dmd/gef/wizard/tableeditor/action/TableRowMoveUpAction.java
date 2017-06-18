package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class TableRowMoveUpAction extends Action {
	private TableViewer viewer;
	/**
	 * ��ѡ��������һ�еĲ���Action
	 * @param viewer
	 */
	public TableRowMoveUpAction(TableViewer viewer) {
		setText(Messages.TableRowMoveUpAction_0);
		this.viewer = viewer;
		this.setToolTipText(Messages.TableRowMoveUpAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.UP));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.UP_DISABLED));
		this.setEnabled(false);
	}
	public void run() {
		Object selection = ((StructuredSelection)viewer.getSelection()).getFirstElement();
		ArrayList list = ((ArrayList)viewer.getInput());
		
	    int selectIndex = list.indexOf(selection);
	    if(selectIndex > 0){
	    	Object $last = list.get(selectIndex-1);
	    	Object $this = list.get(selectIndex);
	    	list.remove(selectIndex);
	    	list.remove(selectIndex-1);
	    	
	    	list.add(selectIndex-1, $this);
	    	list.add(selectIndex, $last);
	    	
	    	viewer.setInput(list);
	    	viewer.refresh();
	    }
	}
	
}
