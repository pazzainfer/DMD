package com.leven.dmd.gef.editor;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;

public class SchemaPaletteViewerProvider extends PaletteViewerProvider {

	/**
	 * implicit constructor
	 */
	public SchemaPaletteViewerProvider(EditDomain graphicalViewerDomain) {
		super(graphicalViewerDomain);
	}

	protected void configurePaletteViewer(PaletteViewer viewer) {
		super.configurePaletteViewer(viewer);
		viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
				viewer));
	}

}