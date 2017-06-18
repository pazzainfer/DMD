package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class IndexAddAction extends Action {
	private TableViewer viewer;
	private Map<String,TableIndex> indexMap;
	
	public IndexAddAction(TableViewer viewer,Map<String,TableIndex> indexMap) {
		setText(Messages.IndexAddAction_0);
		this.viewer = viewer;
		this.indexMap = indexMap;
		this.setToolTipText(Messages.IndexAddAction_0);
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.INDEX_ADD));
	}
	public void run() {
	    //�½�һ��ʵ�����
		ArrayList indexList = ((ArrayList)viewer.getInput());
		int size = indexList==null?0:indexList.size() + 1;
		TableIndex p = new TableIndex(Messages.IndexAddAction_2 + size);
	    
	    viewer.add(p);
	    indexList.add(p);//smx�����û�����д��룬�ڵ����ͷ����ʱ����ӵļ�¼�ᶪʧ
	    indexMap.put(p.getName(), p);
	}
	
}
