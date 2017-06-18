package com.leven.dmd.pro.nav.provider;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.nav.domain.NavigatorTreeNode;

public class SchemaNavigatorContentProvider implements ITreeContentProvider {
	
	public SchemaNavigatorContentProvider(){
//		System.out.println(Platform.getInstallLocation()
//				.getURL().getPath());
	}


	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof INavigatorTreeNode){
			return ((INavigatorTreeNode)inputElement).getChildren().toArray();
		}
		return new Object[]{new NavigatorTreeNode("empty",null,null,INavigatorNodeTypeConstants.TYPE_ROOT)};
	}

	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof INavigatorTreeNode){
			return ((INavigatorTreeNode)parentElement).getChildren().toArray();
		}
		return null;
	}

	public Object getParent(Object element) {
		if(element instanceof INavigatorTreeNode){
			return ((INavigatorTreeNode)element).getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element instanceof INavigatorTreeNode){
			return ((INavigatorTreeNode)element).hasChildren();
		}
		return false;
	}

	public void dispose() {

	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}
}
