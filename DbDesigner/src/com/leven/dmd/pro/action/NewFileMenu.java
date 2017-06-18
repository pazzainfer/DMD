package com.leven.dmd.pro.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.BaseNewWizardMenu;

public class NewFileMenu extends BaseNewWizardMenu {
	private NewSchemaFileAction newSchemaFileAction;

	public NewFileMenu(IWorkbenchWindow window, String id) {
		super(window, id);
	}
	
	public NewFileMenu(IWorkbenchWindow window) {
        this(window, null);
        newSchemaFileAction = new NewSchemaFileAction();
    }
	
	private void fillMenu(IContributionManager innerMgr) {
        innerMgr.removeAll();

        IContributionItem[] items = getContributionItems();
        for (int i = 0; i < items.length; i++) {
            innerMgr.add(items[i]);
        }
    }
	
	protected void addItems(List list) {
		ArrayList shortCuts= new ArrayList();
    	addShortcuts(shortCuts);
    	list.add(new ActionContributionItem(newSchemaFileAction));
        if (!shortCuts.isEmpty()) {
        	list.addAll(shortCuts);
        }
	}

	
}
