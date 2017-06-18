package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import com.leven.dmd.gef.command.RelationshipEditCommand;
import com.leven.dmd.gef.figures.CustomArrowConnection;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.policy.RelationshipEditPolicy;
import com.leven.dmd.gef.util.IColorConstants;
import com.leven.dmd.pro.util.control.palette.CustomGraphicalEditorWithPalette;
/**
 * ���ϵģ�Ϳ�����
 * @author 
 * lifeng 2012-7-11 ����03:32:57
 */
public class RelationshipPart extends PropertyAwareConnectionPart {
	private CommandStack fCommandStack = null;
	
	public RelationshipPart(IEditorPart editor) {
		super();
		fCommandStack = ((CustomGraphicalEditorWithPalette)editor).getCommandStack();
	}

	public void activate() {
		super.activate();
	}

	public void deactivate() {
		super.deactivate();
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN) {
			// ����������ģ����Ϣ
			Relationship relationship = (Relationship) getModel();
			RelationshipEditCommand command = new RelationshipEditCommand(relationship,this);
			fCommandStack.execute(command);
		}
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RelationshipEditPolicy());
	}

	protected IFigure createFigure() {
		PolylineConnection conn = new CustomArrowConnection((Relationship)getModel());
		conn.setAntialias(SWT.ON);
		return conn;
	}


	@Override
	public void refreshStartLabel(PropertyChangeEvent evt) {
		CustomArrowConnection conn = (CustomArrowConnection)getFigure();
		conn.getStartLabel().setText((String)evt.getNewValue());
		conn.setAntialias(SWT.ON);
		super.refreshStartLabel(evt);
	}
	public void refreshStartLabel(String label) {
		CustomArrowConnection conn = (CustomArrowConnection)getFigure();
		conn.getStartLabel().setText(label);
		conn.setAntialias(SWT.ON);
		refresh();
	}
	@Override
	public void refreshEndLabel(PropertyChangeEvent evt) {
		CustomArrowConnection conn = (CustomArrowConnection)getFigure();
		conn.getEndLabel().setText((String)evt.getNewValue());
		conn.setAntialias(SWT.ON);
		super.refreshStartLabel(evt);
	}
	public void refreshEndLabel(String label) {
		CustomArrowConnection conn = (CustomArrowConnection)getFigure();
		conn.getEndLabel().setText(label);
		conn.setAntialias(SWT.ON);
		refresh();
	}

	/**
	 * �����߱�ѡ��ʱ�Ĳ���:���߿�ӱ�Ϊ2,�޸���ɫ
	 */
	public void setSelected(int value) {
		super.setSelected(value);
		CustomArrowConnection conn = (CustomArrowConnection) getFigure();
		PolylineDecoration pd1 = new PolylineDecoration();
		if (value != EditPart.SELECTED_NONE){
			conn.setLineWidth(2);
			pd1.setLineWidth(2);
		}else{
			conn.setLineWidth(1);
			pd1.setLineWidth(1);
		}
		pd1.setForegroundColor(new Color(null,IColorConstants.ARROW_LINE));
		conn.setTargetDecoration(pd1);
	}

}