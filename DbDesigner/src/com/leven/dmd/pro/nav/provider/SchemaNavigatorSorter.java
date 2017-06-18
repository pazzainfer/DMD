package com.leven.dmd.pro.nav.provider;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.leven.dmd.gef.model.Table;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;

public class SchemaNavigatorSorter extends ViewerSorter {

	public SchemaNavigatorSorter() {
	}

	public SchemaNavigatorSorter(Collator collator) {
		super(collator);
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if(e1 instanceof INavigatorTreeNode && e2 instanceof INavigatorTreeNode){
			if(e1 instanceof Table && e2 instanceof Table){
				int result = ((Table)e1).getSeqno() - ((Table)e2).getSeqno();
				if(result==0){
					result = collator.compare(((Table)e1).getName(), ((Table)e2).getName());
				}
				return result;
			}
			return ((INavigatorTreeNode)e1).getNodeType() - ((INavigatorTreeNode)e2).getNodeType();
		}
		return super.compare(viewer, e1, e2);
	}

}
