package com.leven.dmd.pro.nav.provider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class SchemaNavigatorLabelProvider implements ILabelProvider {

	public void addListener(ILabelProviderListener listener) {

	}

	public void dispose() {

	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

	public Image getImage(Object element) {
		if(element instanceof INavigatorTreeNode){
			int type = ((INavigatorTreeNode)element).getNodeType();
			if(type==INavigatorNodeTypeConstants.TYPE_ROOT){
				return null;
//				return ImageHelper.getImage(ImageKeys.NAVIGATOR_VIEW);
			} else if(type==INavigatorNodeTypeConstants.TYPE_SCHEMA_FILE){
				return ImageHelper.getImage(ImageKeys.SCHEMA);
			} else if(type==INavigatorNodeTypeConstants.TYPE_TABLE_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_PACKAGE_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_PACKAGE_NODE){
				return ImageHelper.getImage(ImageKeys.PACKAGE);
			} else if(type==INavigatorNodeTypeConstants.TYPE_TABLE_NODE){
				int status = ((Table)element).getStatus();
				if(status == IEditorConstant.TABLE_STATUS_ADDED){
					return ImageHelper.getImage(ImageKeys.TABLE_ADDED);
				}else if(status == IEditorConstant.TABLE_STATUS_NORMAL){
					return ImageHelper.getImage(ImageKeys.TABLE_NORMAL);
				}else if(status == IEditorConstant.TABLE_STATUS_CHANGED){
					return ImageHelper.getImage(ImageKeys.TABLE_CHANGED);
				}
				return ImageHelper.getImage(ImageKeys.TABLE);
			} else if(type==INavigatorNodeTypeConstants.TYPE_COLUMN_NODE){
				boolean isPk = ((Column)element).isPk();
				if(isPk){
					return ImageHelper.getImage(ImageKeys.COLUMN_PK);
				}
				return ImageHelper.getImage(ImageKeys.COLUMN);
			} else if(type==INavigatorNodeTypeConstants.TYPE_TEMPALTE_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_COLUMN_TEMPLATE_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_SEQUENCE_TEMPLATE_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_COLUMN_TEMPLATE_NODE){
				return ImageHelper.getImage(ImageKeys.COLUMN_TEMPLATE);
			} else if(type==INavigatorNodeTypeConstants.TYPE_SEQUENCE_TEMPLATE_NODE){
				return ImageHelper.getImage(ImageKeys.SEQUENCE_TEMPLATE);
			} else if(type==INavigatorNodeTypeConstants.TYPE_VIEW_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_VIEW_NODE){
				return ImageHelper.getImage(ImageKeys.VIEW);
			} else if(type==INavigatorNodeTypeConstants.TYPE_TABLESPACE_FOLDER){
				return ImageHelper.getImage(ImageKeys.FOLDER);
			} else if(type==INavigatorNodeTypeConstants.TYPE_TABLESPACE_NODE){
				return ImageHelper.getImage(ImageKeys.TABLESPACE);
			}
		}
		return ImageHelper.getImage(ImageKeys.NAVIGATOR_VIEW);
	}

	public String getText(Object element) {
		if(element instanceof INavigatorTreeNode){
			if(element instanceof Table){
				return ((Table)element).getCnName() + "(" + ((Table)element).getName() + ")";
			}else if(element instanceof Column){
				return ((Column)element).getCnName() + "(" + ((Column)element).getName() + ")";
			}
			return ((INavigatorTreeNode)element).getName().toString();
		}
		return null;
	}

}
