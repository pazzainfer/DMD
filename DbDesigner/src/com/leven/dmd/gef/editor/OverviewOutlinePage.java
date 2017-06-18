package com.leven.dmd.gef.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.action.SchemaTreeContextMenuProvider;
import com.leven.dmd.gef.part.factory.TreeEditPartFactory;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

public class OverviewOutlinePage extends ContentOutlinePage implements
		IAdaptable {
	private static final int ID_OUTLINE = 0;
	private static final int ID_OVERVIEW = 1;
	private PageBook pageBook;
	private Control outline;
	private Canvas overview;
	private Thumbnail thumbnail;
	private IAction showOutlineAction;
	private IAction showOverviewAction;
	private SchemaDiagramEditor fEditor;
	private EditPartFactory fEditPartFactory;
	private ContextMenuProvider fContextMenuProvider;
	private Object fModel;

	public OverviewOutlinePage(SchemaDiagramEditor editor,
			EditPartFactory editPartFactory, Object model) {
		super(new TreeViewer());
		this.fEditor = editor;
		this.fEditPartFactory = editPartFactory;
		this.fModel = model;
	}

	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			return this.fEditor.getAdapter(type);
		}
		return null;
	}

	public void createControl(Composite parent) {
		this.pageBook = new PageBook(parent, 0);
		this.outline = getViewer().createControl(this.pageBook);
		this.overview = new Canvas(this.pageBook, 0);
		this.pageBook.showPage(this.outline);
		configureOutlineViewer();
		hookOutlineViewer();
		initializeOutlineViewer();
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(this.pageBook,
						"com.leven.dmd.workbench.help.outline_view"); //$NON-NLS-1$
	}

	protected void configureOutlineViewer() {
		getViewer().setEditDomain(this.fEditor.getEditDomain());
		getViewer().setEditPartFactory(new TreeEditPartFactory());
		fContextMenuProvider = new SchemaTreeContextMenuProvider(fEditor.getGraphicalViewer(), fEditor.getActionRegistry());
		getViewer().setContextMenu(this.fContextMenuProvider);

		IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
		this.showOutlineAction = new Action() {
			public void run() {
				OverviewOutlinePage.this.showPage(0);
				setToolTipText(Messages.OverviewOutlinePage_1);
			}
		};
		this.showOutlineAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						ImageKeys.OUTLINE));
		tbm.add(this.showOutlineAction);
		this.showOverviewAction = new Action() {
			public void run() {
				OverviewOutlinePage.this.showPage(1);
				setToolTipText(Messages.OverviewOutlinePage_2);
			}
		};
		this.showOverviewAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						ImageKeys.OVERVIEW));
		tbm.add(this.showOverviewAction);
		showPage(1);
	}

	protected void hookOutlineViewer() {
		this.fEditor.getSelectionSynchronizer().addViewer(getViewer());
	}

	public void initializeOutlineViewer() {
		getViewer().setContents(this.fModel);
	}

	protected void initializeOverview() {
		LightweightSystem lightweightSystem = new LightweightSystem(
				this.overview);
		RootEditPart rootEditPart = this.fEditor.getGraphicalViewer()
				.getRootEditPart();
		if ((rootEditPart instanceof ScalableFreeformRootEditPart)) {
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rootEditPart;
			this.thumbnail = new ScrollableThumbnail(
					(Viewport) root.getFigure());
			this.thumbnail.setBorder(new MarginBorder(3));
			this.thumbnail.setSource(root.getLayer("Printable Layers")); //$NON-NLS-1$
			lightweightSystem.setContents(this.thumbnail);
		}
	}

	protected void showPage(int id) {
		if (id == 0) {
			this.showOutlineAction.setChecked(true);
			this.showOverviewAction.setChecked(false);
			this.pageBook.showPage(this.outline);
			if (this.thumbnail != null)
				this.thumbnail.setVisible(false);
		} else if (id == 1) {
			if (this.thumbnail == null)
				initializeOverview();
			this.showOutlineAction.setChecked(false);
			this.showOverviewAction.setChecked(true);
			this.pageBook.showPage(this.overview);
			this.thumbnail.setVisible(true);
		}
	}

	protected void unhookOutlineViewer() {
		this.fEditor.getSelectionSynchronizer().removeViewer(getViewer());
	}

	public void dispose() {
		unhookOutlineViewer();
		if (this.thumbnail != null)
			this.thumbnail.deactivate();
		super.dispose();
	}

	public Control getControl() {
		return this.pageBook;
	}

	public void init(IPageSite pageSite) {
		super.init(pageSite);
		ActionRegistry registry = this.fEditor.getActionRegistry();
		IActionBars bars = pageSite.getActionBars();
		String id = ActionFactory.UNDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.REDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.DELETE.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
	}
}