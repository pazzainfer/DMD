package com.leven.dmd.gef.directedit;

import org.eclipse.ui.IEditorSite;

public class StatusLineValidationMessageHandler implements
		ValidationMessageHandler {

	private IEditorSite editorSite;

	public StatusLineValidationMessageHandler(IEditorSite editorSite) {
		this.editorSite = editorSite;
	}

	public void setMessageText(String text) {
		editorSite.getActionBars().getStatusLineManager().setErrorMessage(text);
	}

	public void reset() {
		editorSite.getActionBars().getStatusLineManager().setErrorMessage(null);
	}

}