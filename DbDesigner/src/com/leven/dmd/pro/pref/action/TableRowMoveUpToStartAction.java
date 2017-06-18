package com.leven.dmd.pro.pref.action;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;

public class TableRowMoveUpToStartAction extends Action {
	private TableViewer viewer;
	/**
	 * ��ѡ�����ƶ��v��Ĳ���Action
	 * @param viewer
	 */
	public TableRowMoveUpToStartAction(TableViewer viewer) {
		setText(Messages.TableRowMoveUpToStartAction_0);
		this.viewer = viewer;
		this.setToolTipText(Messages.TableRowMoveUpToStartAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.UP_TO_START));
		this.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.UP_TO_START_DISABLED));
		this.setEnabled(false);
	}
	
	public void run() {
		Object selection = ((StructuredSelection)viewer.getSelection()).getFirstElement();
		ArrayList list = ((ArrayList)viewer.getInput());
		
	    int selectIndex = list.indexOf(selection);
	    if(selectIndex > 0){
	    	Object $this = list.get(selectIndex);
	    	list.remove(selectIndex);
	    	
	    	list.add(0, $this);
	    	
	    	viewer.setInput(list);
	    	viewer.refresh();
	    }
	}
	
}
