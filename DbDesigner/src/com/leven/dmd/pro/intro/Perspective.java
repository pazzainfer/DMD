package com.leven.dmd.pro.intro;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.leven.dmd.pro.nav.view.SchemaNavigatorView;

public class Perspective implements IPerspectiveFactory {
	public static final String ID = "com.leven.dmd.pro.perspective";
	public static final String OUTLINE_VIEW_ID = "org.eclipse.ui.views.ContentOutline";
	public static final String PROPERTY_VIEW_ID = "org.eclipse.ui.views.PropertySheet";
	
	
	public void createInitialLayout(IPageLayout layout){
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		String editorArea = layout.getEditorArea();
		
		layout.addView(SchemaNavigatorView.VIEW_ID, IPageLayout.LEFT, 0.4f, editorArea);
		String logInfoFolderID = "position.statlog";
		IFolderLayout bottomFolder = layout.createFolder(logInfoFolderID, IPageLayout.BOTTOM, 0.5f,
				SchemaNavigatorView.VIEW_ID);
		bottomFolder.addView(OUTLINE_VIEW_ID);
		bottomFolder.addView(PROPERTY_VIEW_ID);
		
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.6f, editorArea);
	}
}
