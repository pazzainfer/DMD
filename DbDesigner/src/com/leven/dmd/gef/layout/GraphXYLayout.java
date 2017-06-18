package com.leven.dmd.gef.layout;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import com.leven.dmd.gef.part.SchemaDiagramPart;

public class GraphXYLayout extends FreeformLayout {

	private SchemaDiagramPart diagram;

	public GraphXYLayout(SchemaDiagramPart diagram) {
		this.diagram = diagram;
	}

	public void layout(IFigure container) {
		super.layout(container);
		diagram.setTableModelBounds();
	}

	public Object getConstraint(IFigure child) {
		Object constraint = constraints.get(child);
		if (constraint != null || constraint instanceof Rectangle) {
			return (Rectangle) constraint;
		} else {
			Rectangle currentBounds = child.getBounds();
			return new Rectangle(currentBounds.x, currentBounds.y, -1, -1);
		}
	}

}
