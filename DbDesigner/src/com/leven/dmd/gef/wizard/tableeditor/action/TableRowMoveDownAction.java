package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class TableRowMoveDownAction extends Action {
	private TableViewer viewer;
	/**
	 * ��ѡ��������һ�еĲ���Action
	 * @param viewer
	 */
	public TableRowMoveDownAction(TableViewer viewer) {
		setText(Messages.TableRowMoveDownAction_0);
		this.viewer = viewer;
		this.setToolTipText(Messages.TableRowMoveDownAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DOWN));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.DOWN_DISABLED));
		this.setEnabled(false);
	}
	
	public void run() {
		Object selection = ((StructuredSelection)viewer.getSelection()).getFirstElement();
		ArrayList list = ((ArrayList)viewer.getInput());
		
	    int selectIndex = list.indexOf(selection);
	    if(selectIndex < (list.size()-1)){
	    	Object $next = list.get(selectIndex+1);
	    	Object $this = list.get(selectIndex);
	    	list.remove(selectIndex+1);
	    	list.remove(selectIndex);
	    	
	    	list.add(selectIndex, $next);
	    	list.add(selectIndex+1, $this);
	    	
	    	viewer.setInput(list);
	    	viewer.refresh();
	    }
	}
	
}
