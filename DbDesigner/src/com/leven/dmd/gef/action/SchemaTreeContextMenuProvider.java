package com.leven.dmd.gef.action;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.action.export.ExportImageAction;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;

/**
 * �༭��������-�Ҽ������˵���ʵ����
 * 
 * @author lifeng 2012-7-11 ����02:25:54
 */
public class SchemaTreeContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	/**
	 * Ϊָ����EditPartViewer���Actionע�� ��
	 * 
	 * @param viewer
	 * @param registry
	 */
	public SchemaTreeContextMenuProvider(EditPartViewer viewer,
			ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}
	
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);

		IAction action;
		action = getActionRegistry().getAction(GEFActionConstants.UNDO);
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(GEFActionConstants.REDO);
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(GEFActionConstants.DELETE);
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = getActionRegistry().getAction(ActionFactory.BACK.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);
		action = getActionRegistry().getAction(ActionFactory.FIND.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);
		
		action = getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_FIND, action);
	}
	
	private ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	public void setActionRegistry(ActionRegistry registry) {
		actionRegistry = registry;
	}

}