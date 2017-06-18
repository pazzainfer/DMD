package com.leven.dmd.gef.editor;

import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.events.FocusEvent;

import com.leven.dmd.gef.directedit.ValidationMessageHandler;

public class ValidationEnabledGraphicalViewer extends ScrollingGraphicalViewer {

	private ValidationMessageHandler messageHandler;

	public ValidationEnabledGraphicalViewer(
			ValidationMessageHandler messageHandler) {
		super();
		this.messageHandler = messageHandler;
	}

	public ValidationMessageHandler getValidationHandler() {
		return messageHandler;
	}

	protected void handleFocusLost(FocusEvent fe) {
		super.handleFocusLost(fe);
		messageHandler.reset();
	}

}