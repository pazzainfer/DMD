package com.leven.dmd.pro.nav.action;

import org.eclipse.jface.action.Action;

import com.leven.dmd.gef.tmpfile.util.SchemaTemplateConstants;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class NavigatorInputRefreshAction extends Action {

	public static final String ID = "com.leven.dmd.pro.nav.action.NavigatorInputRefreshAction"; //$NON-NLS-1$
	
	public NavigatorInputRefreshAction() {
		super();
		this.setText(Messages.NavigatorInputRefreshAction_1);
		this.setToolTipText(Messages.NavigatorInputRefreshAction_2);
		this.setImageDescriptor(Activator.getImageDescriptor(SchemaTemplateConstants.SYNCHRONIZE_IMAGE_PATH));
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void run() {
		NavigatorViewUtil.refreshViewInput(null);
	}

}
