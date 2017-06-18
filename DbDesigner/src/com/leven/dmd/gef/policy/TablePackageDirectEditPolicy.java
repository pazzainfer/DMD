package com.leven.dmd.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.jface.viewers.CellEditor;

import com.leven.dmd.gef.command.TablePackageNameChangeCommand;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.part.TablePackagePart;

public class TablePackageDirectEditPolicy extends DirectEditPolicy {

	private String oldValue;

	protected Command getDirectEditCommand(DirectEditRequest request) {
		TablePackageNameChangeCommand cmd = new TablePackageNameChangeCommand();
		TablePackage table = (TablePackage) getHost().getModel();
		cmd.setTablePackage(table);
		//cmd.setOldName(table.getName());
		cmd.setOldName(table.getName());
		CellEditor cellEditor = request.getCellEditor();
		cmd.setName((String) cellEditor.getValue());
		return cmd;
	}

	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		TablePackagePart tablePart = (TablePackagePart) getHost();
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
		TablePackagePart tablePart = (TablePackagePart) getHost();
		tablePart.revertNameChange();
	}
}