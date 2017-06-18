package com.leven.dmd.gef.wizard.tableeditor;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.util.constant.ITableViewerConstants;
import com.leven.dmd.pro.Activator;
/**
 * ���༭���ǩ�ṩ��
 * @author leven
 * 2012-8-23 ����08:14:34
 */
public class CustomTableViewerLabelProvider implements ITableLabelProvider  {

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
		Column p = (Column)element;
		if(columnIndex==ITableViewerConstants.COLUMN_ICON_INDEX){
			if(p.isPk()){
				return Activator.getImage(ImageKeys.LABEL_KEY);
			}else{
				return Activator.getImage(ImageKeys.LABEL_COLUMN);
			}
		}else if(columnIndex==ITableViewerConstants.COLUMN_ISPK_INDEX){
			if(p.isPk()){
				return Activator.getImage(ImageKeys.CHECKBOX_TRUE);
			}else{
				return Activator.getImage(ImageKeys.CHECKBOX_FALSE);
			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Column p = (Column)element;
		if(columnIndex == ITableViewerConstants.COLUMN_DESCRIPTION_INDEX){
			return p.getCnName();
		} else if(columnIndex == ITableViewerConstants.COLUMN_NAME_INDEX){
			return p.getName();
		} else if(columnIndex == ITableViewerConstants.COLUMN_TYPE_INDEX){
			return p.getType();
		} else if(columnIndex == ITableViewerConstants.COLUMN_LENGTH_INDEX){
			if(ColumnType.hasLength(p.getType())){
				return p.getLength()+"";
			}
		} else if(columnIndex == ITableViewerConstants.COLUMN_SCALE_INDEX){
			if(ColumnType.hasScale(p.getType())){
				return p.getScale()+"";
			}
		} else if(columnIndex == ITableViewerConstants.COLUMN_TEMP_INDEX){
			if(p.getColumnTemplate()!=null){
				return p.getColumnTemplate().getName();
			}
		}
		return null;
	}

}
