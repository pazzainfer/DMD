package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IEditorPart;

import com.leven.dmd.gef.command.SchemaGoIntoPackageCommand;
import com.leven.dmd.gef.directedit.ExtendedDirectEditManager;
import com.leven.dmd.gef.directedit.LabelCellEditorLocator;
import com.leven.dmd.gef.directedit.TableNameCellEditorValidator;
import com.leven.dmd.gef.directedit.ValidationMessageHandler;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.editor.ValidationEnabledGraphicalViewer;
import com.leven.dmd.gef.figures.TablePackageFigure;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.policy.TablePackageDirectEditPolicy;
import com.leven.dmd.gef.policy.TablePackageEditPolicy;

public class TablePackagePart extends PropertyAwarePart {

	protected DirectEditManager manager;
	private IEditorPart editor;
	
	public TablePackagePart(IEditorPart editor) {
		super();
		this.editor = editor;
	}

	@Override
	protected IFigure createFigure() {
		TablePackage p = (TablePackage)getModel();
		Label label = new Label(p.getName());
		TablePackageFigure figure = new TablePackageFigure(label);
		figure.setLocation(p.getLocation());
		return figure;
	}

	protected void refreshVisuals() {
		TablePackageFigure figure = (TablePackageFigure)getFigure();
		Point location = figure.getLocation();
		Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
		SchemaDiagramPart parent = (SchemaDiagramPart)getParent();
		parent.setLayoutConstraint(this, figure, constraint);
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TablePackageEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new TablePackageDirectEditPolicy());
	}
	@Override
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_OPEN) {
			TablePackage tablePackage = (TablePackage)getModel();
			Schema schema = (Schema)this.getParent().getModel();
			schema.setLayoutManualAllowed(true);
			((SchemaDiagramEditor)editor).getCommandStack()
				.execute(new SchemaGoIntoPackageCommand(schema, tablePackage));
		}else if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			if (request instanceof DirectEditRequest
					&& !directEditHitTest(((DirectEditRequest) request)
							.getLocation().getCopy()))
				return;
			performDirectEdit();
		}
	}
	private boolean directEditHitTest(Point requestLoc) {
		TablePackageFigure figure = (TablePackageFigure) getFigure();
		Label nameLabel = figure.getNameLabel();
		nameLabel.translateToRelative(requestLoc);
		if (nameLabel.containsPoint(requestLoc))
			return true;
		return false;
	}

	protected void performDirectEdit() {
		if (manager == null) {
			ValidationEnabledGraphicalViewer viewer = (ValidationEnabledGraphicalViewer) getViewer();
			ValidationMessageHandler handler = viewer.getValidationHandler();

			TablePackageFigure figure = (TablePackageFigure) getFigure();
			Label nameLabel = figure.getNameLabel();
			manager = new ExtendedDirectEditManager(this, TextCellEditor.class,
					new LabelCellEditorLocator(nameLabel), nameLabel,
					new TableNameCellEditorValidator(handler));
		}
		manager.show();
	}

	public void handleNameChange(String value) {
		TablePackageFigure tableFigure = (TablePackageFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		label.setVisible(false);
		refreshVisuals();
	}
	public void revertNameChange() {
		TablePackageFigure tableFigure = (TablePackageFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		TablePackage table = getTablePackage();
		label.setText(table.getName());
		label.setVisible(true);
		refreshVisuals();
	}
	protected void handleBoundsChange(PropertyChangeEvent evt) {
		TablePackageFigure figure = (TablePackageFigure)getFigure();
		Rectangle constraint = (Rectangle) evt.getNewValue();
		SchemaDiagramPart parent = (SchemaDiagramPart)getParent();
		parent.setLayoutConstraint(this, figure, constraint);
	}
	protected void commitNameChange(PropertyChangeEvent evt) {
		TablePackageFigure figure = (TablePackageFigure)getFigure();
		Label label = figure.getNameLabel();
		label.setText(getTablePackage().getName());
		label.setVisible(true);
		refreshVisuals();
	}
	public TablePackage getTablePackage(){
		return (TablePackage)getModel();
	}
}
