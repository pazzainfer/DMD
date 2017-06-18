package com.leven.dmd.pro.pref.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.pref.model.DBConfigModel;

public class DBConfigTableViewerLabelProvider implements ITableLabelProvider  {
	

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
		if(columnIndex==0){
			return Activator.getImage(ImageKeys.DBCONFIG);
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		DBConfigModel p = (DBConfigModel)element;
		if(columnIndex == 0){
			return p.getName();
		} else if(columnIndex == 1){
			return p.getType();
		}
		return null;
	}

}
