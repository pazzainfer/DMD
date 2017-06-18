package com.leven.dmd.pro.nav.prop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class NavigatorPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public NavigatorPropertyPage() {
	}

	@Override
	protected Control createContents(Composite parent) {
		Label lab = new Label(parent,SWT.NONE);
		lab.setText("sssss");
		return lab;
	}

	@Override
	public void applyData(Object data) {
		super.applyData(data);
	}

}
