package com.leven.dmd.gef.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

public class DataEditDropTargetListener extends
		TemplateTransferDropTargetListener {

	public DataEditDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	protected CreationFactory getFactory(Object template) {
		return new DataElementFactory(template);
	}

}
