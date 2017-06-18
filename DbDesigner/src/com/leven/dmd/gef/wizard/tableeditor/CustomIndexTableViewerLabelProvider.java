package com.leven.dmd.gef.wizard.tableeditor;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.model.IndexType;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.util.constant.ITableViewerConstants;
import com.leven.dmd.pro.Activator;
/**
 * ������༭���ǩ�ṩ��
 * @author leven
 * 2012-8-23 ����08:14:34
 */
public class CustomIndexTableViewerLabelProvider implements ITableLabelProvider  {

	public void addListener(ILabelProviderListener listener) {
		
	}

	public void dispose() {
		
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		
	}

	public Image getColumnImage(Object element, int columnIndex) {
		TableIndex p = (TableIndex)element;
		if(columnIndex==ITableViewerConstants.INDEX_NAME_INDEX){
			if(p.getType().equals(IndexType.UNIQUE.getType())){
				return Activator.getImage(ImageKeys.INDEX_UNIQUE);
			}else{
				return Activator.getImage(ImageKeys.INDEX_MODEL);
			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		TableIndex p = (TableIndex)element;
		if(columnIndex == ITableViewerConstants.INDEX_NAME_INDEX){
			return p.getName();
		} else if(columnIndex == ITableViewerConstants.INDEX_COLUMNS_INDEX){
			return p.getColumns();
		} else if(columnIndex == ITableViewerConstants.INDEX_TYPE_INDEX){
			return p.getType();
		} else if(columnIndex == ITableViewerConstants.INDEX_COMMENTS_INDEX){
			return p.getComments();
		}
		return null;
	}

}
