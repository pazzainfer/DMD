package com.leven.dmd.gef.wizard.tableeditor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class IndexTableRefreshAction extends Action {
	private TableViewer viewer;
	private Object obj;
	
	public IndexTableRefreshAction(TableViewer viewer) {
		setText(Messages.IndexTableRefreshAction_0);
		this.viewer = viewer;
		this.setToolTipText(Messages.IndexTableRefreshAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.REFRESH));
	}
	
	public void run() {
		if(obj==null){
			viewer.refresh();
		}else {
			viewer.refresh(obj);
		}
	}

	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
}
