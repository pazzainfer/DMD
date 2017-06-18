package com.leven.dmd.gef.action;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.action.export.ExportDB2DllAction;
import com.leven.dmd.gef.action.export.ExportDllAction;
import com.leven.dmd.gef.action.export.ExportImageAction;
import com.leven.dmd.gef.action.export.ExportInformixDllAction;
import com.leven.dmd.gef.action.export.ExportOracleDllAction;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.part.SchemaDiagramPart;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

/**
 * 画布上下文菜单提供者
 * @author lifeng 
 * 2012-7-11 下午02:25:54
 */
public class SchemaContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	/**
	 * 画布上下文菜单提供者
	 * @param viewer 画布控制器
	 * @param registry 动作注册表
	 */
	public SchemaContextMenuProvider(EditPartViewer viewer,
			ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}
	/**
	 * 创建上下文菜单
	 */
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		Schema schema = null;
		EditPart editPart = ((GraphicalViewer)this.getViewer()).getContents();
		if(editPart instanceof SchemaDiagramPart){
			schema = ((SchemaDiagramPart)editPart).getSchema();
		}
		IAction action;
		action = getActionRegistry().getAction(GEFActionConstants.UNDO);
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(GEFActionConstants.REDO);
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, 
				getActionRegistry().getAction(TableCreateAction.ID));
		TableQuoteCreateAction tableQuoteCreateAction = new TableQuoteCreateAction((IEditorPart)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, tableQuoteCreateAction);
		
		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(GEFActionConstants.DELETE);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		if(schema!=null && schema.isPackageOpen()){
			action = getActionRegistry().getAction(ActionFactory.BACK.getId());
			menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);
		}
		action = getActionRegistry().getAction(ActionFactory.FIND.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);
		
		action = getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);
		
		action = new Action(Messages.SchemaContextMenuProvider_0,AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.SHOW_OUTLINE_VIEW)){};
		action.setMenuCreator(this.showViewsMenuCreator);
		ActionContributionItem item = new ActionContributionItem(action);
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, item);
		
		action = new ExportDllAction(PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor());
//		action = new Action("DLL�ļ�����",AbstractUIPlugin.imageDescriptorFromPlugin(
//				Activator.PLUGIN_ID, ImageKeys.DLL)){};
//		action.setMenuCreator(this.dllExportMenuCreator);
//		item = new ActionContributionItem(action);
//		menu.appendToGroup(GEFActionConstants.GROUP_FIND, item);
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);

		SchemeSynchronizedDatabaseAction synchronizedAction = 
			new SchemeSynchronizedDatabaseAction(PlatformUI.getWorkbench().
					getActiveWorkbenchWindow().getActivePage().getActiveEditor());
		synchronizedAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
						Activator.PLUGIN_ID, ImageKeys.INTO_DB));
		
		menu.appendToGroup(GEFActionConstants.GROUP_REST,synchronizedAction);
		
		action = getActionRegistry().getAction(ActionFactory.IMPORT.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
		
		ExportImageAction exportImageAction = new ExportImageAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart());
		exportImageAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.EXPORT_IMAGE));
		exportImageAction.setDisabledImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, ImageKeys.EXPORT_IMAGE_DISABLED));

		menu.appendToGroup(GEFActionConstants.GROUP_REST,exportImageAction);
	}

	/**
	 * 打开视图的分值菜单创造者
	 */
	private IMenuCreator showViewsMenuCreator = new IMenuCreator() {
		private MenuManager dropDownMenuMgr;
		private void createDropDownMenuMgr() {
			if (this.dropDownMenuMgr == null) {
				this.dropDownMenuMgr = new MenuManager();

				ActionContributionItem item = new ActionContributionItem(new OpenOutlineViewsAction());
				this.dropDownMenuMgr.add(item);
				item = new ActionContributionItem(new OpenPropertiesViewsAction());
				this.dropDownMenuMgr.add(item);
			}
		}
		public void dispose() {
			if (this.dropDownMenuMgr != null) {
				this.dropDownMenuMgr.dispose();
				this.dropDownMenuMgr = null;
			}
		}
		public Menu getMenu(Control parent) {
			createDropDownMenuMgr();
			return this.dropDownMenuMgr.createContextMenu(parent);
		}

		public Menu getMenu(Menu parent) {
			createDropDownMenuMgr();
			Menu menu = new Menu(parent);
			IContributionItem[] items = this.dropDownMenuMgr.getItems();
			for (int i = 0; i < items.length; i++) {
				IContributionItem item = items[i];
				IContributionItem newItem = item;
				if ((item instanceof ActionContributionItem)) {
					newItem = new ActionContributionItem(
							((ActionContributionItem) item).getAction());
				}
				newItem.fill(menu, -1);
			}
			return menu;
		}
	};
	/**
	 * 打开Dll导出的分支菜单创造者
	 */
	private IMenuCreator dllExportMenuCreator = new IMenuCreator() {
		private MenuManager dropDownMenuMgr;
		private void createDropDownMenuMgr() {
			if (this.dropDownMenuMgr == null) {
				this.dropDownMenuMgr = new MenuManager();
				
				ActionContributionItem item = new ActionContributionItem(getActionRegistry()
						.getAction(ExportOracleDllAction.ID));
				this.dropDownMenuMgr.add(item);
				item = new ActionContributionItem(getActionRegistry()
						.getAction(ExportDB2DllAction.ID));
				this.dropDownMenuMgr.add(item);
				item = new ActionContributionItem(getActionRegistry()
						.getAction(ExportInformixDllAction.ID));
				this.dropDownMenuMgr.add(item);
			}
		}
		public void dispose() {
			if (this.dropDownMenuMgr != null) {
				this.dropDownMenuMgr.dispose();
				this.dropDownMenuMgr = null;
			}
		}
		public Menu getMenu(Control parent) {
			createDropDownMenuMgr();
			return this.dropDownMenuMgr.createContextMenu(parent);
		}
		
		public Menu getMenu(Menu parent) {
			createDropDownMenuMgr();
			Menu menu = new Menu(parent);
			IContributionItem[] items = this.dropDownMenuMgr.getItems();
			for (int i = 0; i < items.length; i++) {
				IContributionItem item = items[i];
				IContributionItem newItem = item;
				if ((item instanceof ActionContributionItem)) {
					newItem = new ActionContributionItem(
							((ActionContributionItem) item).getAction());
				}
				newItem.fill(menu, -1);
			}
			return menu;
		}
	};
	private ActionRegistry getActionRegistry() {
		return actionRegistry;
	}
	public void setActionRegistry(ActionRegistry registry) {
		actionRegistry = registry;
	}
}