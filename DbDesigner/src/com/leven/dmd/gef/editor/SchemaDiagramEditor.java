package com.leven.dmd.gef.editor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetSorter;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.action.CopyAction;
import com.leven.dmd.gef.action.FindNodeAction;
import com.leven.dmd.gef.action.ImportTableFromDBAction;
import com.leven.dmd.gef.action.PasteAction;
import com.leven.dmd.gef.action.RedoAction;
import com.leven.dmd.gef.action.SchemaBackToMainAction;
import com.leven.dmd.gef.action.SchemaContextMenuProvider;
import com.leven.dmd.gef.action.TableCreateAction;
import com.leven.dmd.gef.action.UndoAction;
import com.leven.dmd.gef.action.export.ExportDB2DllAction;
import com.leven.dmd.gef.action.export.ExportInformixDllAction;
import com.leven.dmd.gef.action.export.ExportOracleDllAction;
import com.leven.dmd.gef.directedit.StatusLineValidationMessageHandler;
import com.leven.dmd.gef.dnd.DataEditDropTargetListener;
import com.leven.dmd.gef.editor.ruler.EditorRulerProvider;
import com.leven.dmd.gef.editor.ruler.Ruler;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.part.factory.SchemaEditPartFactory;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;
import com.leven.dmd.pro.util.FileUtil;
import com.leven.dmd.pro.util.control.palette.CustomGraphicalEditorWithPalette;

public class SchemaDiagramEditor extends CustomGraphicalEditorWithPalette
		implements CommandStackListener, ISelectionListener {

	public static final String EDITOR_ID = "com.leven.dmd.gef.SchemaEditor"; //$NON-NLS-1$
	private Schema schema;
	
	private String filePath = null;

	private PropertySheetPage undoablePropertySheetPage;

	private GraphicalViewer graphicalViewer;

	private List editPartActionIDs = new ArrayList();

	private List stackActionIDs = new ArrayList();

	private List editorActionIDs = new ArrayList();

	private OverviewOutlinePage overviewOutlinePage;

	private ActionRegistry actionRegistry;

	private DefaultEditDomain editDomain;

	private boolean isDirty;
	
	private RulerComposite rulerComp;
	private org.eclipse.gef.EditPartFactory editPartFactory;
	ContextMenuProvider contextMenuProvider;

	public SchemaDiagramEditor() {
		super("",Activator.getImage(ImageKeys.PALETTE));
		editDomain = new DefaultEditDomain(this);
		setEditDomain(editDomain);
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		getCommandStack().addCommandStackListener(this);

		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(this);

		createActions();
		getEditDomain().setPaletteRoot(getPaletteRoot());
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateActions(editPartActionIDs);
	}

	public void commandStackChanged(EventObject event) {
		updateActions(stackActionIDs);
		setDirty(getCommandStack().isDirty());
	}

	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	public void dispose() {
		getCommandStack().removeCommandStackListener(this);
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(this);
		getActionRegistry().dispose();
		super.dispose();
		NavigatorViewUtil.refreshViewInput(null);
	}

	public Object getAdapter(Class adapter) {
		if (adapter == GraphicalViewer.class || adapter == EditPartViewer.class)
			return getGraphicalViewer();
		else if (adapter == CommandStack.class)
			return getCommandStack();
		else if (adapter == EditDomain.class)
			return getEditDomain();
		else if (adapter == ActionRegistry.class)
			return getActionRegistry();
		else if (adapter == IPropertySheetPage.class)
			return getPropertySheetPage();
		else if (adapter == IContentOutlinePage.class)
			return getOverviewOutlinePage();
		else if (adapter == ZoomManager.class)
			return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart())
			 	.getZoomManager();
		
		return super.getAdapter(adapter);
	}

	public void doSave(IProgressMonitor monitor) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(out);
			objectOut.writeObject(schema);
			objectOut.close();
			IEditorInput input = getEditorInput();
			if(input instanceof FileStoreEditorInput){
				File file = new File(((FileStoreEditorInput)input).getURI());
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(schema);
				oos.flush();
				oos.close();
				getCommandStack().markSaveLocation();
			}else if(filePath!=null && !filePath.equals("")){ //$NON-NLS-1$
				File file = new File(filePath);
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(schema);
				oos.flush();
				oos.close();
				getCommandStack().markSaveLocation();
			} else {
				FileDialog fileDialog = new FileDialog(this.getEditorSite().getShell(),SWT.SAVE);
				fileDialog.setText(Messages.SchemaDiagramEditor_3);
				boolean newFile = false;
				if(schema.getName().equals(Messages.SchemaDiagramEditor_4)){
					newFile = true;
				}

				String[] filterExt = {"*.schema"}; //$NON-NLS-1$
				fileDialog.setFilterExtensions(filterExt);
				String result = fileDialog.open();
				if(result!=null && !result.equals("")){ //$NON-NLS-1$
					File file = new File(fileDialog.getFilterPath() + File.separator +fileDialog.getFileName());
					if(!file.exists()){
						file.createNewFile();
					}
					schema.setName(fileDialog.getFileName().substring(0,fileDialog.getFileName().length()-7));
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
					oos.writeObject(schema);
					oos.flush();
					oos.close();
					filePath = fileDialog.getFilterPath() + File.separator +fileDialog.getFileName();
					setPartName(fileDialog.getFileName());
					getCommandStack().markSaveLocation();
					if(newFile){
						NavigatorViewUtil.refreshViewInput(schema, null);
					}
				}else {
					filePath = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doSaveAs() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(out);
			objectOut.writeObject(schema);
			objectOut.close();
			FileDialog fileDialog = new FileDialog(this.getEditorSite().getShell(),SWT.SAVE);
			fileDialog.setText(Messages.SchemaDiagramEditor_5);

			String[] filterExt = {"*.schema"}; //$NON-NLS-1$
			fileDialog.setFilterExtensions(filterExt);
			String result = fileDialog.open();
			if(result!=null && !result.equals("")){ //$NON-NLS-1$
				File file = new File(fileDialog.getFilterPath() + File.separator +fileDialog.getFileName());
				if(!file.exists()){
					file.createNewFile();
				}
				schema.setName(fileDialog.getFileName().substring(0,fileDialog.getFileName().length()-7));
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(schema);
				oos.flush();
				oos.close();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public CommandStack getCommandStack() {
		return getEditDomain().getCommandStack();
	}

	public Schema getSchema() {
		return schema;
	}

	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if(input instanceof FileStoreEditorInput){
			File file = new File(((FileStoreEditorInput)input).getURI());
			try {
				setPartName(file.getName());
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				schema = (Schema) ois.readObject();
				ois.close();
			} catch (Exception e) {
				schema = new Schema(file.getName());
				NavigatorViewUtil.refreshViewInput(schema,null);
				return;
			}
		}else {
			this.setPartName(Messages.SchemaDiagramEditor_10);
			schema = new Schema(Messages.SchemaDiagramEditor_4);
			NavigatorViewUtil.refreshViewInput(schema,null);
			return;
		}
		NavigatorViewUtil.refreshViewInput(schema,null);
		try{
			SchemaTemplate schemaTemplate = schema.getSchemaTemplate();
			//将模板中的外部引用模板先移除
			List<ColumnTemplate> beRemoved = new ArrayList<ColumnTemplate>();
			for(ColumnTemplate temp : schemaTemplate.getColumnTemplates()){
				if(temp.isOutQuote()){
					beRemoved.add(temp);
				}
			}
			for(ColumnTemplate temp : beRemoved){
				schemaTemplate.removeColumnTemplate(temp);
			}
			//再获取外部引用路径中的模板文件，添加至Schema中来
			List<String> pathBeRemoved = new ArrayList<String>();
			for(String tempPath : schema.getOutTempPaths()){
				Object fileObj = FileUtil.readObjectFromFile(tempPath);
				if(fileObj!=null){
					if(fileObj instanceof Schema){
						SchemaTemplate template = ((Schema)fileObj).getSchemaTemplate();
						for(ColumnTemplate temp : template.getColumnTemplates()){
							temp.setOutQuote(true);
							schemaTemplate.addColumnTemplate(temp);
						}
					}else if(fileObj instanceof SchemaTemplate){
						SchemaTemplate template = (SchemaTemplate)fileObj;
						for(ColumnTemplate temp : template.getColumnTemplates()){
							temp.setOutQuote(true);
							schemaTemplate.addColumnTemplate(temp);
						}
					}
				}else {	//如果文件错误，则需要将该路径去除。
					pathBeRemoved.add(tempPath);
				}
			}
			for(String tempPath : pathBeRemoved){
				schema.getOutTempPaths().remove(tempPath);
			}
			//
			//遍历所有的表、字段，若有引用模板，则更新字段数据为模板数据
			for(Table t : schema.getAllTables()){
				for(Column c : t.getColumns()){
					checkColumn(c,schemaTemplate);
				}
			}
			schema.setPackagePath("");
		} catch (Exception e) {
			return;
		}
	}
	/**
	 * 检查字段，如果字段引用了模板，则更新字段数据为模板数据
	 * @param c
	 * @param schemaTemplate
	 */
	private void checkColumn(Column c ,SchemaTemplate schemaTemplate){
		if(c.getColumnTemplate()!=null){
			ColumnTemplate temp = schemaTemplate.getColumnTemplatesMap().get(c.getColumnTemplate().getCode());
			if(temp != null){
				c.setType(temp.getColumnType().getType());
				c.setLength(temp.getColumnLength());
				c.setScale(temp.getColumnScale());
				c.setColumnTemplate(temp);
				if(!c.isTempCanEdit()){	//模板字段可编辑的话，则不修改这两项属性
					c.setCnName(temp.getColumnCnName());
					c.setName(temp.getColumnName());
				}
			}
		} 
	}

	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new SchemaPaletteViewerProvider(editDomain);
	}

	protected void createGraphicalViewer(Composite parent) {
		rulerComp = new RulerComposite(parent, SWT.NONE);
		IEditorSite editorSite = getEditorSite();
		GraphicalViewer viewer = createViewer(rulerComp,editorSite);
		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer)viewer);

		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
		getEditDomain().addViewer(viewer);
		getSite().setSelectionProvider(viewer);
		viewer.setContents(schema);

		contextMenuProvider = new SchemaContextMenuProvider(viewer,
				getActionRegistry());
		viewer.setContextMenu(contextMenuProvider);
		getSite().registerContextMenu("com.leven.gef.editor.contextmenu", //$NON-NLS-1$
				contextMenuProvider, viewer);

		viewer.setProperty(RulerProvider.PROPERTY_VERTICAL_RULER,
				new EditorRulerProvider(new Ruler(false)));
		viewer.setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER,
				new EditorRulerProvider(new Ruler(true)));
		viewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, true);
		
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
		
		this.graphicalViewer = viewer;
		graphicalViewer.getControl().addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				Point pt = graphicalViewer.getControl().toControl(e.x, e.y);
				((TableCreateAction)getActionRegistry().getAction(TableCreateAction.ID)).setLocation(
						new org.eclipse.draw2d.geometry.Point(pt.x,pt.y));
			}
		});
		
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
	}
	
	private GraphicalViewer createViewer(Composite parent,IEditorSite editorSite) {
		StatusLineValidationMessageHandler validationMessageHandler = new StatusLineValidationMessageHandler(
				editorSite);
		GraphicalViewer viewer = new ValidationEnabledGraphicalViewer(
				validationMessageHandler);
		viewer.createControl(parent);

		// configure the viewer
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		viewer.addDropTargetListener(new DataEditDropTargetListener(viewer));

		// initialize the viewer with input
		editPartFactory = new SchemaEditPartFactory(this);
		viewer.setEditPartFactory(editPartFactory);

		return viewer;

	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)getGraphicalViewer().getRootEditPart();
		IAction action = new ZoomInAction(root.getZoomManager());
		getActionRegistry().registerAction(action);
		getSite().getKeyBindingService().registerAction(action);
		action = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(action);
		getSite().getKeyBindingService().registerAction(action);
		

		getGraphicalViewer().setRootEditPart(root);
		
	}
	@Override
	protected Control getGraphicalControl() {
		return this.rulerComp;
	}

	/**
	 * ע�ᰴť����
	 * @author leven
	 * 2012-9-18 ����02:33:18
	 * @return
	 */
	protected KeyHandler getCommonKeyHandler() {

		KeyHandler sharedKeyHandler = new KeyHandler();
		sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
				getAction(GEFActionConstants.DELETE));
		sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
				getAction(GEFActionConstants.DIRECT_EDIT));
		sharedKeyHandler.put(KeyStroke.getReleased((char)3, (int)'c', SWT.CTRL),
				getAction(ActionFactory.COPY.getId()));
		sharedKeyHandler.put(KeyStroke.getReleased((char)22, (int)'v', SWT.CTRL),
				getAction(ActionFactory.PASTE.getId()));
		sharedKeyHandler.put(KeyStroke.getPressed(SWT.BS, (int)'\b',SWT.CTRL),
				getAction(ActionFactory.BACK.getId()));
		
		sharedKeyHandler.put(KeyStroke.getReleased((char)6, (int)'f', SWT.CTRL),
				getAction(ActionFactory.FIND.getId()));
		sharedKeyHandler.put(KeyStroke.getReleased((char)9, (int)'i', SWT.CTRL),
				getAction(ActionFactory.IMPORT.getId()));

		sharedKeyHandler.put(KeyStroke.getPressed('a', 0x1, SWT.CTRL),
				 getAction(ActionFactory.SELECT_ALL.getId())); 

		return sharedKeyHandler;
	}

	public void setDirty(boolean dirty) {
		if (isDirty != dirty) {
			isDirty = dirty;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	protected void createActions() {
		super.createActions();
		IAction action;
		addStackAction(new UndoAction(this));
		addStackAction(new RedoAction(this));
		
		action = new DeleteAction((IWorkbenchPart) this);
		action.setText(Messages.SchemaDiagramEditor_13);
		addEditPartAction((DeleteAction)action);
	    getSelectionActions().add(action.getId());
		
		addEditorAction(new SaveAction(this));
		addEditorAction(new PrintAction(this));
		
		ActionRegistry registry = getActionRegistry();
		
		action = new FindNodeAction(this);
		addAction(action);
		action = new ImportTableFromDBAction(this);
		addAction(action);
		action = new SchemaBackToMainAction(this);
		addAction(action);
		
		action = new ExportOracleDllAction(this);
		addAction(action);
		action = new ExportDB2DllAction(this);
		addAction(action);
		action = new ExportInformixDllAction(this);
		addAction(action);
//		registry.registerAction(action);
		
		action = new DirectEditAction((IWorkbenchPart)this);
		registry.registerAction(action);
	    getSelectionActions().add(action.getId());

	    action = new SelectAllAction((IWorkbenchPart) this);
	    action.setText(Messages.SchemaDiagramEditor_14);
	    action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
	    		Activator.PLUGIN_ID, ImageKeys.SELECT_ALL));
		addAction(action);
		

	    CopyAction copyAction = new CopyAction((IWorkbenchPart) this);
	    addEditPartAction(copyAction);
	    getSelectionActions().add(copyAction.getId());
	    PasteAction pasteAction = new PasteAction((IWorkbenchPart) this);
	    addEditPartAction(pasteAction);
	    getSelectionActions().add(pasteAction.getId());
	    
	    TableCreateAction tableCreateAction = new TableCreateAction(this);
	    registry.registerAction(tableCreateAction);
	}

	protected void addEditPartAction(SelectionAction action) {
		getActionRegistry().registerAction(action);
		editPartActionIDs.add(action.getId());
	}

	protected void addStackAction(StackAction action) {
		getActionRegistry().registerAction(action);
		stackActionIDs.add(action.getId());
	}

	protected void addEditorAction(WorkbenchPartAction action) {
		getActionRegistry().registerAction(action);
		editorActionIDs.add(action.getId());
	}

	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}


	protected void updateActions(List actionIds) {
		for (Iterator ids = actionIds.iterator(); ids.hasNext();) {
			IAction action = getActionRegistry().getAction(ids.next());
			if (null != action && action instanceof UpdateAction)
				((UpdateAction) action).update();

		}
	}

	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null){
			actionRegistry = new ActionRegistry();
		}

		return actionRegistry;
	}

	protected OverviewOutlinePage getOverviewOutlinePage() {
		if (null == overviewOutlinePage && null != getGraphicalViewer()) {
			RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
			if (rootEditPart instanceof ScalableFreeformRootEditPart) {
				overviewOutlinePage = new OverviewOutlinePage(this,editPartFactory,schema);
			}
		}

		return overviewOutlinePage;
	}

	protected PropertySheetPage getPropertySheetPage() {
		if (null == undoablePropertySheetPage) {
			undoablePropertySheetPage = new PropertySheetPage(){
				public void createControl(Composite parent) {
					PropertySheetSorter sorter = new PropertySheetSorter() {
						public int compare(IPropertySheetEntry entryA, IPropertySheetEntry entryB) {
							return getCollator().compare(entryA.getDescription()==null?"":entryA.getDescription() //$NON-NLS-1$
									, entryB.getDescription()==null?"":entryB.getDescription()); //$NON-NLS-1$
						}
					};
					this.setSorter(sorter);
					super.createControl(parent);
				}  
			};
			undoablePropertySheetPage.setRootEntry(GEFPlugin
					.createUndoablePropertySheetEntry(getCommandStack()));
		}

		return undoablePropertySheetPage;
	}
	
	/*
	 */
	protected void firePropertyChange(int propertyId) {
		super.firePropertyChange(propertyId);
		updateActions(editorActionIDs);
	}

	@Override
	protected SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	public ContextMenuProvider getContextMenuProvider() {
		return contextMenuProvider;
	}

	public void setContextMenuProvider(ContextMenuProvider contextMenuProvider) {
		this.contextMenuProvider = contextMenuProvider;
	}

	@Override
	public Object getModel() {
		return schema;
	}

	public IAction getAction(String actionID) {
		return getActionRegistry().getAction(actionID);
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		return new PaletteViewerCreator().createPaletteRoot();
	}
}