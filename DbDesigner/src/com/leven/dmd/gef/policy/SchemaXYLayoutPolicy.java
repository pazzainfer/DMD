package com.leven.dmd.gef.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.leven.dmd.gef.command.TableMoveCommand;
import com.leven.dmd.gef.command.TablePackageMoveCommand;
import com.leven.dmd.gef.command.TableResizeCommand;
import com.leven.dmd.gef.figures.TableFigure;
import com.leven.dmd.gef.figures.TablePackageFigure;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.part.TablePackagePart;
import com.leven.dmd.gef.part.TablePart;

public class SchemaXYLayoutPolicy extends XYLayoutEditPolicy {

	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}

	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		if (!(constraint instanceof Rectangle))
			return null;
		if (child instanceof TablePart){
			TablePart tablePart = (TablePart) child;
			Table table = tablePart.getTable();
			TableFigure figure = (TableFigure) tablePart.getFigure();
			Rectangle oldBounds = figure.getBounds();
			Rectangle newBounds = (Rectangle) constraint;
	
			if (oldBounds.width != newBounds.width && newBounds.width != -1)
				return null;
			if (oldBounds.height != newBounds.height && newBounds.height != -1)
				return null;
	
			TableMoveCommand command = new TableMoveCommand(table,
					oldBounds.getCopy(), newBounds.getCopy());
			return command;
		}else if (child instanceof TablePackagePart){
			TablePackagePart tablePart = (TablePackagePart) child;
			TablePackage table = tablePart.getTablePackage();
			TablePackageFigure figure = (TablePackageFigure) tablePart.getFigure();
			Rectangle oldBounds = figure.getBounds();
			Rectangle newBounds = (Rectangle) constraint;
	
			if (oldBounds.width != newBounds.width && newBounds.width != -1)
				return null;
			if (oldBounds.height != newBounds.height && newBounds.height != -1)
				return null;
	
			TablePackageMoveCommand command = new TablePackageMoveCommand(table,
					oldBounds.getCopy(), newBounds.getCopy());
			return command;
		}
		return null;
	}

	public Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
		IFigure fig = child.getFigure();
		Rectangle rectangle = (Rectangle) fig.getParent().getLayoutManager()
				.getConstraint(fig);
		if (rectangle == null) {
			rectangle = fig.getBounds();
		}
		return rectangle;
	}

	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	@Override
	public Command getCommand(Request request) {
		if (REQ_RESIZE_CHILDREN.equals(request.getType())) {
			Table table = (Table) ((TablePart) ((ChangeBoundsRequest) request).getEditParts().get(0)).getModel();
			IFigure figure = ((TablePart) ((ChangeBoundsRequest) request).getEditParts().get(0)).getFigure();
			TableResizeCommand cmd = new TableResizeCommand(table);
	        cmd.setNewHeight(figure.getBounds().height + ((ChangeBoundsRequest) request).getSizeDelta().height);
	        cmd.setNewWidth(figure.getBounds().width + ((ChangeBoundsRequest) request).getSizeDelta().width);
	        return cmd;
		}
		return super.getCommand(request);
	}
}