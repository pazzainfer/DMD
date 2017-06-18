package com.leven.dmd.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;

import com.leven.dmd.gef.command.TableNameChangeCommand;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.part.TablePart;

public class TableDirectEditPolicy extends DirectEditPolicy {

	private String oldValue;

	protected Command getDirectEditCommand(DirectEditRequest request) {
		TableNameChangeCommand cmd = new TableNameChangeCommand();
		Table table = (Table) getHost().getModel();
		cmd.setTable(table);
		//cmd.setOldName(table.getName());
		cmd.setOldName(table.getCnName());
		CellEditor cellEditor = request.getCellEditor();
		cmd.setName((String) cellEditor.getValue());
		return cmd;
	}

	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		TablePart tablePart = (TablePart) getHost();
		tablePart.handleNameChange(value);
	}

	/**
	 * @param to
	 *            Saves the initial text value so that if the user's changes are
	 *            not committed then
	 */
	protected void storeOldEditValue(DirectEditRequest request) {

		CellEditor cellEditor = request.getCellEditor();
		oldValue = (String) cellEditor.getValue();
	}

	/**
	 * @param request
	 */
	protected void revertOldEditValue(DirectEditRequest request) {
		CellEditor cellEditor = request.getCellEditor();
		cellEditor.setValue(oldValue);
		TablePart tablePart = (TablePart) getHost();
		tablePart.revertNameChange();
	}
}