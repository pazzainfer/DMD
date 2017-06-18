package com.leven.dmd.gef.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.ui.IEditorPart;

import com.leven.dmd.gef.command.ColumnEditCommand;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.util.control.palette.CustomGraphicalEditorWithPalette;
/**
 * �ֶ�ģ�Ϳ�����
 * @author lifeng
 * 2012-7-11 ����03:30:58
 */
public class ColumnPart extends PropertyAwarePart {

	protected DirectEditManager manager;
	private CommandStack fCommandStack = null;
	private Schema schema = null;

	public ColumnPart(IEditorPart editor) {
		super();
		fCommandStack = ((CustomGraphicalEditorWithPalette)editor).getCommandStack();
		schema = (Schema)((CustomGraphicalEditorWithPalette)editor).getModel();
	}

	protected IFigure createFigure() {
		Column column = (Column) getModel();
		String label = column.getLabelText();
		Label columnLabel = new Label(label);
		if(column.isPk()){
			columnLabel.setIcon(Activator.getImage(ImageKeys.LABEL_KEY));
		} else {
			columnLabel.setIcon(Activator.getImage(ImageKeys.LABEL_COLUMN));
		}
		return columnLabel;
	}

	/**
	 * Creats EditPolicies for the column label
	 */
	protected void createEditPolicies() {
//		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ColumnEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
	}

	/**
	 * Sets the width of the line when selected
	 */
	public void setSelected(int value) {
		super.setSelected(value);
	}
	
	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			Column column = (Column) getModel();
			fCommandStack.execute(new ColumnEditCommand(column, schema));
		}
	}

	protected void refreshVisuals() {
		Column column = (Column) getModel();
		Label columnLabel = (Label) getFigure();
		columnLabel.setText(column.getLabelText());
		if(column.isPk()){
			columnLabel.setIcon(Activator.getImage(ImageKeys.LABEL_KEY));
		} else {
			columnLabel.setIcon(Activator.getImage(ImageKeys.LABEL_COLUMN));
		}
	}

}