package com.leven.dmd.gef.figures;

import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.swt.graphics.Color;

import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.util.IColorConstants;
/**
 * 
 * @author Felix.Lee
 * 2016年6月16日 下午10:27:20
 */
public class CustomArrowConnection extends PolylineConnection{

	private Label endLabel;
	private Label startLabel;
	private ConnectionEndpointLocator endLocator;
	private ConnectionEndpointLocator startLocator;
	
	public CustomArrowConnection(Relationship relationship) {
		super();
		PolylineDecoration decoration = new PolylineDecoration();
		decoration.setForegroundColor(new Color(null,IColorConstants.ARROW_LINE));
		decoration.setTolerance(5);
		this.setTargetDecoration(decoration);
		this.setForegroundColor(new Color(null,IColorConstants.ARROW_LINE));
		this.setLineWidthFloat(1.5f);
		
		endLocator = new ConnectionEndpointLocator(this, true);
		endLocator.setVDistance(5);
		startLocator = new ConnectionEndpointLocator(this,false); 
		startLocator.setVDistance(5);
		
		endLabel = new Label(relationship.getForeignKeyColumn()==null?"":relationship.getForeignKeyColumn());
		startLabel = new Label(relationship.getPrimaryKeyColumn()==null?"":relationship.getPrimaryKeyColumn());

        this.add(endLabel, endLocator);  
        this.add(startLabel, startLocator);
	}

	public Label getEndLabel() {
		return endLabel;
	}
	public void setEndLabel(Label endLabel) {
		this.endLabel = endLabel;
	}
	public Label getStartLabel() {
		return startLabel;
	}
	public void setStartLabel(Label startLabel) {
		this.startLabel = startLabel;
	}
	public ConnectionEndpointLocator getEndLocator() {
		return endLocator;
	}
	public void setEndLocator(ConnectionEndpointLocator endLocator) {
		this.endLocator = endLocator;
	}
	public ConnectionEndpointLocator getStartLocator() {
		return startLocator;
	}
	public void setStartLocator(ConnectionEndpointLocator startLocator) {
		this.startLocator = startLocator;
	}
}
