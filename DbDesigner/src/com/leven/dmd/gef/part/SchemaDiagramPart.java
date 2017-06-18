package com.leven.dmd.gef.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.rulers.RulerProvider;

import com.leven.dmd.gef.figures.SchemaFigure;
import com.leven.dmd.gef.figures.TableFigure;
import com.leven.dmd.gef.figures.TablePackageFigure;
import com.leven.dmd.gef.layout.DelegatingLayoutManager;
import com.leven.dmd.gef.layout.GraphAnimation;
import com.leven.dmd.gef.layout.GraphLayoutManager;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.policy.SchemaContainerEditPolicy;

public class SchemaDiagramPart extends PropertyAwarePart {

	CommandStackListener stackListener = new CommandStackListener() {

		public void commandStackChanged(EventObject event) {
			if (delegatingLayoutManager.getActiveLayoutManager() instanceof GraphLayoutManager) {
				if (!GraphAnimation.captureLayout(getFigure()))
					return;
				while (GraphAnimation.step())
					getFigure().getUpdateManager().performUpdate();
				GraphAnimation.end();
			} else {
				getFigure().getUpdateManager().performUpdate();
			}
		}
	};
	private DelegatingLayoutManager delegatingLayoutManager;

	/**
	 * Adds this EditPart as a command stack listener, which can be used to call
	 * performUpdate() when it changes
	 */
	public void activate() {

		if (!isActive()) {
			getViewer().getEditDomain().getCommandStack()
					.addCommandStackListener(stackListener);
		}
		super.activate();

	}

	/**
	 * Removes this EditPart as a command stack listener
	 */
	public void deactivate() {
		if (!isActive())
			getViewer().getEditDomain().getCommandStack()
					.removeCommandStackListener(stackListener);
		super.deactivate();
	}

	protected IFigure createFigure() {
		Figure f = new SchemaFigure();
		delegatingLayoutManager = new DelegatingLayoutManager(this);
		f.setLayoutManager(delegatingLayoutManager);
		return f;
	}

	@Override
	protected void refreshVisuals() {
		Figure f = (Figure)this.getFigure();
	}
	
	public Schema getSchema() {
		return (Schema) getModel();
	}

	/**
	 * @return the children Model objects as a new ArrayList
	 */
	protected List getModelChildren() {
		List list = new ArrayList();
		if(getSchema().isPackageOpen()){
			TablePackage package1 = getSchema().getOpenPackage();
			list.addAll(package1.getTables());
			list.addAll(package1.getTablePackages());
		}else {
			list.addAll(getSchema().getTables());
			list.addAll(getSchema().getTablePackages());
		}
		return list;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable() {
		return false;
	}

	/**
	 * Creates EditPolicy objects for the EditPart. The LAYOUT_ROLE policy is
	 * left to the delegating layout manager
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new SchemaContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
	}

	@Override
	public void refresh() {
		super.refresh();
	}

	public boolean setTableModelBounds() {

		List tableParts = getChildren();
		Schema schema = getSchema();

		for (Iterator iter = tableParts.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if(obj instanceof TablePart){
				TablePart tablePart = (TablePart) obj;
				TableFigure tableFigure = (TableFigure) tablePart.getFigure();
	
				if (tableFigure == null){
					continue;
				}
				Rectangle bounds = tableFigure.getBounds().getCopy();
				Table table = tablePart.getTable();
				table.setBounds(bounds);
			}else if(obj instanceof TablePackagePart){
				TablePackagePart tablePackagePart = (TablePackagePart) obj;
				TablePackageFigure tablePackageFigure = (TablePackageFigure) tablePackagePart.getFigure();
	
				if (tablePackageFigure == null)
					continue;
				Rectangle bounds = tablePackageFigure.getBounds().getCopy();
				TablePackage tablePackage = tablePackagePart.getTablePackage();
				tablePackage.setBounds(bounds);
			}
		}

		return true;
	}

	public boolean setTableFigureBounds(boolean updateConstraint) {

		List tableParts = getChildren();
		Schema schema = getSchema();

		for (Iterator iter = tableParts.iterator(); iter.hasNext();) {
			Object part = iter.next();
			if(part instanceof TablePart){
				TablePart tablePart = (TablePart) part;
				Table table = tablePart.getTable();
	
				Rectangle bounds = table.getBounds();
				if (bounds == null) {
					return false;
				} else {
					TableFigure tableFigure = (TableFigure) tablePart.getFigure();
					if (tableFigure == null) {
						return false;
					} else {
						if (updateConstraint) {
							delegatingLayoutManager.setXYLayoutConstraint(
									tableFigure, new Rectangle(bounds.x, bounds.y,
											bounds.width, bounds.height));
						}
					}
				}
			}else if(part instanceof TablePackagePart){
				TablePackagePart tablePart = (TablePackagePart) part;
				TablePackage table = tablePart.getTablePackage();
	
				Rectangle bounds = table.getBounds();
				if (bounds == null) {
					return false;
				} else {
					TablePackageFigure tableFigure = (TablePackageFigure) tablePart.getFigure();
					if (tableFigure == null) {
						return false;
					} else {
						if (updateConstraint) {
							delegatingLayoutManager.setXYLayoutConstraint(
									tableFigure, new Rectangle(bounds.x, bounds.y,
											-1, -1));
						}
					}
				}
			}
		}
		return true;

	}

	protected void handleLayoutChange(PropertyChangeEvent evt) {
		Boolean layoutType = (Boolean) evt.getNewValue();
		boolean isManualLayoutDesired = layoutType.booleanValue();
		getFigure().setLayoutManager(delegatingLayoutManager);
	}

	public void setLayoutConstraint(EditPart child, IFigure childFigure,
			Object constraint) {
		super.setLayoutConstraint(child, childFigure, constraint);
	}

	protected void handleChildChange(PropertyChangeEvent evt) {
		super.handleChildChange(evt);
	}

	public Object getAdapter(Class adapter) {
	    if (adapter == SnapToHelper.class) {
	        List snapStrategies = new ArrayList();
	        Boolean val = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
	        if (val != null && val.booleanValue())
	            snapStrategies.add(new SnapToGuides(this));
	        val = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
	        if (val != null && val.booleanValue())
	            snapStrategies.add(new SnapToGeometry(this));
	        val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
	        if (val != null && val.booleanValue())
	            snapStrategies.add(new SnapToGrid(this));
	        
	        if (snapStrategies.size() == 0)
	            return null;
	        if (snapStrategies.size() == 1)
	            return (SnapToHelper)snapStrategies.get(0);

	        SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
	        for (int i = 0; i < snapStrategies.size(); i++)
	            ss[i] = (SnapToHelper)snapStrategies.get(i);
	        return new CompoundSnapToHelper(ss);
	    }
	    return super.getAdapter(adapter);
	}

}