package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.IEditorPart;

import com.leven.dmd.gef.command.TableEditCommand;
import com.leven.dmd.gef.command.TableQuoteEditCommand;
import com.leven.dmd.gef.directedit.ExtendedDirectEditManager;
import com.leven.dmd.gef.directedit.LabelCellEditorLocator;
import com.leven.dmd.gef.directedit.TableNameCellEditorValidator;
import com.leven.dmd.gef.directedit.ValidationMessageHandler;
import com.leven.dmd.gef.editor.ValidationEnabledGraphicalViewer;
import com.leven.dmd.gef.figures.TableFigure;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.policy.TableContainerEditPolicy;
import com.leven.dmd.gef.policy.TableDirectEditPolicy;
import com.leven.dmd.gef.policy.TableEditPolicy;
import com.leven.dmd.gef.policy.TableLayoutEditPolicy;
import com.leven.dmd.gef.policy.TableNodeEditPolicy;
import com.leven.dmd.gef.util.IEditorConstant;
import com.leven.dmd.pro.util.control.palette.CustomGraphicalEditorWithPalette;
/**
 * ��ģ�Ϳ�����
 * 
 * @author lifeng 2012-7-11 ����03:34:02
 */
public class TablePart extends PropertyAwarePart implements NodeEditPart {
	private CommandStack fCommandStack = null;
	
	public TablePart(IEditorPart editor) {
		super();
		fCommandStack = ((CustomGraphicalEditorWithPalette)editor).getCommandStack();
	}

	protected DirectEditManager manager;

	// ******************* Life-cycle related methods *********************/

	public void activate() {
		super.activate();
	}

	public void deactivate() {
		super.deactivate();
	}

	// ******************* Model related methods *********************/

	/**
	 * Returns the Table model object represented by this EditPart
	 */
	public Table getTable() {
		return (Table) getModel();
	}

	/**
	 * @return the children Model objects as a new ArrayList
	 */
	protected List getModelChildren() {
		return getTable().getColumns();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections() {
		return getTable().getForeignKeyRelationships();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections() {
		return getTable().getPrimaryKeyRelationships();
	}

	// ******************* Editing related methods *********************/

	/**
	 * Creates edit policies and associates these with roles
	 */
	protected void createEditPolicies() {

		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new TableNodeEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TableLayoutEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new TableContainerEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new TableEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new TableDirectEditPolicy());

	}

	// ******************* Direct editing related methods *********************/

	/**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			if (request instanceof DirectEditRequest
					&& !directEditHitTest(((DirectEditRequest) request)
							.getLocation().getCopy()))
				return;
			performDirectEdit();
		}
		//双击图元调用编辑命令
		if (request.getType() == RequestConstants.REQ_OPEN) {
			Table tableModel = (Table) getModel();
			Schema schema = (Schema)getParent().getModel();
			if(tableModel.isQuote()){
				TableQuoteEditCommand command = new TableQuoteEditCommand(tableModel,schema,this);
				fCommandStack.execute(command);
			}else {
				TableEditCommand command = new TableEditCommand(tableModel,schema,this);
				fCommandStack.execute(command);
			}
		}
	}

	private boolean directEditHitTest(Point requestLoc) {
		TableFigure figure = (TableFigure) getFigure();
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

			TableFigure figure = (TableFigure) getFigure();
			Label nameLabel = figure.getNameLabel();
			manager = new ExtendedDirectEditManager(this, TextCellEditor.class,
					new LabelCellEditorLocator(nameLabel), nameLabel,
					new TableNameCellEditorValidator(handler));
		}
		manager.show();
	}

	/**
	 * @param handles
	 *            the name change during an edit
	 */
	public void handleNameChange(String value) {
		TableFigure tableFigure = (TableFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		label.setVisible(false);
		refreshVisuals();
	}

	/**
	 * Reverts to existing name in model when exiting from a direct edit
	 * (possibly before a commit which will result in a change in the label
	 * value)
	 */
	public void revertNameChange() {
		TableFigure tableFigure = (TableFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		Table table = getTable();
		label.setText(table.getCnName().equals("")?table.getName():table.getCnName());
		label.setVisible(true);
		refreshVisuals();
	}

	// ******************* Miscellaneous stuff *********************/

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString() {
		return getModel().toString();
	}

	// ******************* Listener related methods *********************/

	/**
	 * Handles change in name when committing a direct edit
	 */
	protected void commitNameChange(PropertyChangeEvent evt) {
		TableFigure tableFigure = (TableFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		label.setText(getTable().getName());
		label.setVisible(true);
		refreshVisuals();
	}
	protected void commitCnNameChange(PropertyChangeEvent evt) {
		TableFigure tableFigure = (TableFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		label.setText(getTable().getCnName());
		label.setVisible(true);
		refreshVisuals();
	}

	@Override
	protected void handleStatusChange(PropertyChangeEvent evt) {
		TableFigure tableFigure = (TableFigure) getFigure();
		Label label = tableFigure.getNameLabel();
		Table.changeLabelColor(label,(Integer)evt.getNewValue());
		refreshVisuals();
	}

	/**
	 * handles change in bounds, to be overridden by subclass
	 */
	protected void handleBoundsChange(PropertyChangeEvent evt) {
		TableFigure tableFigure = (TableFigure) getFigure();
		Rectangle constraint = (Rectangle) evt.getNewValue();
		SchemaDiagramPart parent = (SchemaDiagramPart) getParent();
		parent.setLayoutConstraint(this, tableFigure, constraint);
	}

	// ******************* Layout related methods *********************/

	/**
	 * Creates a figure which represents the table
	 */
	protected IFigure createFigure() {
		Table table = getTable();
		Label label = new Label(table.getCnName().equals("")?table.getName():table.getCnName());
		TableFigure tableFigure = new TableFigure(label,IEditorConstant.TABLE_STATUS_NORMAL);
		tableFigure.setLocation(table.getLocation()==null?new Point(1,1):table.getLocation());
		return tableFigure;
	}

	/**
	 * Reset the layout constraint, and revalidate the content pane
	 */
	protected void refreshVisuals() {
		TableFigure tableFigure = (TableFigure) getFigure();
		int status = getTable().getStatus();
		Point location = tableFigure.getLocation();
		tableFigure.getNameLabel().setText(getTable().getCnName().equals("")?getTable().getName():getTable().getCnName());
		Table.changeLabelColor(tableFigure.getNameLabel(),status);
		if(getParent()!=null && ((Schema)getParent().getModel()).isPackageOpen()){
			SchemaDiagramPart parent = (SchemaDiagramPart) getParent();
			Rectangle constraint = new Rectangle(location.x, location.y, -1, -1);
			parent.setLayoutConstraint(this, tableFigure, constraint);
		}
	}

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane() {
		TableFigure figure = (TableFigure) getFigure();
		return figure.getColumnsFigure();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
//		return new TopAnchor(getFigure());
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
//		return new TopAnchor(getFigure());
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
//		return new BottomAnchor(getFigure());
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
//		return new BottomAnchor(getFigure());
		return new ChopboxAnchor(getFigure());
	}

	public void setSelected(int value) {
		super.setSelected(value);
		TableFigure tableFigure = (TableFigure) getFigure();
		if (value != EditPart.SELECTED_NONE)
			tableFigure.setSelected(true);
		else
			tableFigure.setSelected(false);
		tableFigure.repaint();
	}
}