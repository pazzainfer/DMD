package com.leven.dmd.pro.nav.provider;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

public class SchemaNavigatorLinkHelper implements ILinkHelper {

	public IStructuredSelection findSelection(IEditorInput anInput) {
		return null;
	}

	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {
		//System.out.println(aSelection);
	}
}
