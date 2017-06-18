package com.leven.dmd.pro.nav.action;

import org.eclipse.jface.action.Action;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class NavigatorRefreshAction extends Action {
	private Object obj;
	
	public NavigatorRefreshAction(Object obj) {
		super();
		this.obj = obj;
		this.setText(Messages.NavigatorRefreshAction_0);
		this.setImageDescriptor(ImageHelper.getImageDescriptor(ImageKeys.ACTION_REFRESH));
	}
	
	@Override
	public void run() {
		if(obj instanceof Object[]){
			Object[] objs = (Object[])obj;
			for(int i=0;i<objs.length;i++){
				NavigatorViewUtil.refresh(objs[i]);
			}
		}else {
			NavigatorViewUtil.refresh(obj);
		}
	}

}
