package com.leven.dmd.gef.editor;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		final String properties = "org.eclipse.ui.views.PropertySheet";
		final String outline = "org.eclipse.ui.views.ContentOutline";
		final String editorArea = layout.getEditorArea();
		IFolderLayout rightBottomFolder = layout.createFolder("RightBottom",
				IPageLayout.BOTTOM, 0.5f, "RightTop");
		rightBottomFolder.addView(outline);
	}

}
