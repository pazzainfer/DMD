package com.leven.dmd.gef.layout;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;

public class DummyEdgePart {
	protected void createEditPolicies() {
	}

	protected IFigure createFigure() {
		PolylineConnection conn = new PolylineConnection();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		conn.setVisible(false);
		return conn;
	}

}
