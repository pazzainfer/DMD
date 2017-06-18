package com.leven.dmd.gef.action;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.action.export.ExportImageInBarAction;
import com.leven.dmd.gef.editor.SchemaDiagramEditor;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

/**
 * Ϊ�༭����ӹ�������ӹ��߰�ť��ʵ����
 * 
 * @author lifeng 2012-7-11 ����02:24:56
 */
public class SchemaActionBarContributor extends ActionBarContributor {

	FlyoutChangeLayoutAction changeLayoutAction;
	ExportImageInBarAction exportImageAction;
	FindNodeAction findNodeAction;
	IEditorPart editor;
	IMenuManager menuManager = null;

	/**
	 * ���밴ť��
	 */
	private IMenuCreator menuCreator = new IMenuCreator() {
		private MenuManager dropDownMenuMgr;

		private void createDropDownMenuMgr() {
			if (this.dropDownMenuMgr == null) {
				this.dropDownMenuMgr = new MenuManager();

				ActionContributionItem item = new ActionContributionItem(
						SchemaActionBarContributor.this
								.getAction(GEFActionConstants.ALIGN_LEFT));
				this.dropDownMenuMgr.add(item);

				item = new ActionContributionItem(
						SchemaActionBarContributor.this
								.getAction(GEFActionConstants.ALIGN_RIGHT));
				this.dropDownMenuMgr.add(item);

				this.dropDownMenuMgr.add(new Separator());

				item = new ActionContributionItem(
						SchemaActionBarContributor.this
								.getAction(GEFActionConstants.ALIGN_TOP));
				this.dropDownMenuMgr.add(item);

				item = new ActionContributionItem(
						SchemaActionBarContributor.this
								.getAction(GEFActionConstants.ALIGN_MIDDLE));
				this.dropDownMenuMgr.add(item);

				item = new ActionContributionItem(
						SchemaActionBarContributor.this
								.getAction(GEFActionConstants.ALIGN_BOTTOM));
				this.dropDownMenuMgr.add(item);

				this.dropDownMenuMgr.add(new Separator());
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

	@Override
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		buildCustomAction();
		addAction(changeLayoutAction);
		addAction(exportImageAction);
		addAction(findNodeAction);

		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

		addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));

	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(IWorkbenchActionConstants.UNDO));
		toolBarManager.add(getAction(IWorkbenchActionConstants.REDO));
		toolBarManager.add(changeLayoutAction);
		toolBarManager.add(exportImageAction);
		toolBarManager.add(findNodeAction);

		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(
				GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getActionRegistry().getAction(
				GEFActionConstants.ZOOM_OUT));

		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		IAction alignAction = getAction(GEFActionConstants.ALIGN_LEFT);

//		IAction action = new Action("", alignAction.getImageDescriptor()) {
//		};
//		action.setMenuCreator(this.menuCreator);
//
//		ActionContributionItem item = new ActionContributionItem(action);
//		toolBarManager.add(item);

	}

	private void buildCustomAction() {
		changeLayoutAction = new FlyoutChangeLayoutAction(editor);
		changeLayoutAction.setToolTipText(Messages.SchemaActionBarContributor_0);
		changeLayoutAction.setId("com.leven.gef.action.ChangeLayoutAction"); //$NON-NLS-1$
		changeLayoutAction.setChecked(true);
		changeLayoutAction.checked = true;
		changeLayoutAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						ImageKeys.LAYOUT));
		changeLayoutAction.setDisabledImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						ImageKeys.LAYOUT));

		exportImageAction = new ExportImageInBarAction();
		exportImageAction.setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						ImageKeys.EXPORT_IMAGE));
		exportImageAction.setDisabledImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						ImageKeys.EXPORT_IMAGE_DISABLED));

		findNodeAction = new FindNodeAction();
		getActionRegistry().registerAction(findNodeAction);
	}

	protected IAction getAction(IEditorPart editor, String actionID) {
		if (editor instanceof SchemaDiagramEditor) {
			return ((SchemaDiagramEditor) editor).getAction(actionID);
		}
		return null;
	}

	@Override
	public void setActiveEditor(IEditorPart editor) {
		this.editor = editor;
		changeLayoutAction.setActiveEditor(editor);
		IActionBars actionBars = this.getActionBars();
		actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
				getAction(editor, ActionFactory.SELECT_ALL.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(),
				getAction(editor, ActionFactory.FIND.getId()));
		actionBars.updateActionBars();
		super.setActiveEditor(editor);

	}

	@Override
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(IWorkbenchActionConstants.PRINT);
        addGlobalActionKey(ActionFactory.COPY.getId());
        addGlobalActionKey(ActionFactory.PASTE.getId());
        addGlobalActionKey(ActionFactory.CUT.getId());
	}

}