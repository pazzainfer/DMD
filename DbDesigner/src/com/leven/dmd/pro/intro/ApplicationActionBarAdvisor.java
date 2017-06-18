package com.leven.dmd.pro.intro;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.Util;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.handlers.IActionCommandMappingService;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import com.leven.dmd.gef.action.OpenConsoleViewsAction;
import com.leven.dmd.pro.action.NewSchemaFileAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private final IWorkbenchWindow window;
	
	private StatusLineContributionItem statusLineItem;

	private NewSchemaFileAction newSchemaFileAction;
	
	private IWorkbenchAction closeAction;
	private IWorkbenchAction closeAllAction;
	private IWorkbenchAction saveAsAction;
	private IWorkbenchAction saveAllAction;
	
	private IWorkbenchAction undoAction;
	private IWorkbenchAction redoAction;
	
    private IWorkbenchAction newAction;
    private IWorkbenchAction quitAction;
    private IWorkbenchAction saveAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction helpAction;
    private IWorkbenchAction introAction;
    private IWorkbenchAction preferencesAction;
    
    private OpenConsoleViewsAction openConsoleViewsAction;

    private MenuManager perspectiveMenu = new MenuManager("&Open Perspective");
    private MenuManager viewMenu = new MenuManager("Show &View");

    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        window = configurer.getWindowConfigurer().getWindow();
    }

    protected void makeActions(IWorkbenchWindow window) {
    	statusLineItem = new StatusLineContributionItem("ModeContributionItem");
    	closeAction = ActionFactory.CLOSE.create(window);
    	register(closeAction);
    	closeAllAction = ActionFactory.CLOSE_ALL.create(window);
        register(closeAllAction);
        saveAsAction = ActionFactory.SAVE_AS.create(window);
        register(saveAsAction);
        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
        quitAction = ActionFactory.QUIT.create(window);
        register(quitAction);
        undoAction = ActionFactory.UNDO.create(window);
        register(undoAction);
        redoAction = ActionFactory.REDO.create(window);
        register(redoAction);
        
        newAction = ActionFactory.NEW_WIZARD_DROP_DOWN.create(window);
        register(newAction);
        
        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);
        
//        openFileAction = new OpenFileAction();
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        helpAction = ActionFactory.HELP_CONTENTS.create(window);
        register(helpAction);
        
        openConsoleViewsAction = new OpenConsoleViewsAction();
        register(openConsoleViewsAction);
        
        preferencesAction = ActionFactory.PREFERENCES.create(window);
        register(preferencesAction);

        perspectiveMenu.add(ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window));
	    viewMenu.add(ContributionItemFactory.VIEWS_SHORTLIST.create(window));
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	menuBar.add(createFileMenu());
    	menuBar.add(createEditMenu());
//        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        
        MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_file, IWorkbenchActionConstants.M_FILE);
        menuBar.add(windowMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        
	    windowMenu.add(perspectiveMenu);
        windowMenu.add(viewMenu);
        windowMenu.add(new Separator());
        windowMenu.add(preferencesAction);
        
        // Help
        menuBar.add(createHelpMenu());
    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
    	super.fillCoolBar(coolBar);  
        
        IToolBarManager toolBar = new ToolBarManager(coolBar.getStyle());  
        coolBar.add(toolBar);  
        toolBar.add(newSchemaFileAction);
//        toolBar.add(getAction("org.eclipse.ui.actionSet.openFiles"));
        toolBar.add(new Separator());
        toolBar.add(saveAction);
        toolBar.add(saveAllAction);
        toolBar.add(openConsoleViewsAction);
    }
    
    private MenuManager createFileMenu() {
        MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_file, IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        {
            String newText = IDEWorkbenchMessages.Workbench_new;
            String newId = ActionFactory.NEW.getId();
            MenuManager newMenu = new MenuManager(newText, newId);
            newMenu.setActionDefinitionId("org.eclipse.ui.file.newQuickMenu"); //$NON-NLS-1$
            newMenu.add(new Separator(newId));
            this.newSchemaFileAction = new NewSchemaFileAction();
            newMenu.add(this.newSchemaFileAction);
            newMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
            menu.add(newMenu);
        }
        menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
        menu.add(new Separator());

        menu.add(closeAction);
        menu.add(closeAllAction);
        menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
        menu.add(new Separator());
        menu.add(saveAction);
        menu.add(saveAsAction);
        menu.add(saveAllAction);
        menu.add(new Separator());

        menu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
        menu.add(new Separator());
        menu.add(getItem(
				ActionFactory.PRINT.getId(),
				ActionFactory.PRINT.getCommandId(),
				null, null, WorkbenchMessages.Workbench_print,
				WorkbenchMessages.Workbench_printToolTip, null));

        menu.add(new Separator());
        menu.add(getPropertiesItem());

        menu.add(ContributionItemFactory.REOPEN_EDITORS.create(getWindow()));
        menu.add(new GroupMarker(IWorkbenchActionConstants.MRU));
        menu.add(new Separator());
        
		ActionContributionItem quitItem = new ActionContributionItem(quitAction);
		quitItem.setVisible(!Util.isMac());
		menu.add(quitItem);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
    }
    
    private MenuManager createEditMenu() {
        MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_edit, IWorkbenchActionConstants.M_EDIT);
        menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));

        menu.add(undoAction);
        menu.add(redoAction);
        menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
        menu.add(new Separator());

        menu.add(getItem(
				ActionFactory.CUT.getId(),
				ActionFactory.CUT.getCommandId(),
				ISharedImages.IMG_TOOL_CUT,
				ISharedImages.IMG_TOOL_CUT_DISABLED,
				WorkbenchMessages.Workbench_cut,
				WorkbenchMessages.Workbench_cutToolTip, null));
        menu.add(getItem(
				ActionFactory.COPY.getId(),
				ActionFactory.COPY.getCommandId(),
				ISharedImages.IMG_TOOL_COPY,
				ISharedImages.IMG_TOOL_COPY_DISABLED,
				WorkbenchMessages.Workbench_copy,
				WorkbenchMessages.Workbench_copyToolTip, null));
        menu.add(getItem(
				ActionFactory.PASTE.getId(),
				ActionFactory.PASTE.getCommandId(),
				ISharedImages.IMG_TOOL_PASTE,
				ISharedImages.IMG_TOOL_PASTE_DISABLED,
				WorkbenchMessages.Workbench_paste,
				WorkbenchMessages.Workbench_pasteToolTip, null));
        menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
        menu.add(new Separator());

        menu.add(getItem(ActionFactory.DELETE.getId(),
        		ActionFactory.DELETE.getCommandId(),
        		ISharedImages.IMG_TOOL_DELETE,
        		ISharedImages.IMG_TOOL_DELETE_DISABLED,
        		WorkbenchMessages.Workbench_delete,
        		WorkbenchMessages.Workbench_deleteToolTip, 
        		IWorkbenchHelpContextIds.DELETE_RETARGET_ACTION));
        menu.add(getItem(
				ActionFactory.SELECT_ALL.getId(),
				ActionFactory.SELECT_ALL.getCommandId(),
				null, null, WorkbenchMessages.Workbench_selectAll,
				WorkbenchMessages.Workbench_selectAllToolTip, null));
        menu.add(new Separator());

        menu.add(getItem(
				ActionFactory.FIND.getId(),
				ActionFactory.FIND.getCommandId(),
				null, null, WorkbenchMessages.Workbench_findReplace,
				WorkbenchMessages.Workbench_findReplaceToolTip, null));
        menu.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
        menu.add(new Separator());

        menu.add(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));

        menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
        menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        return menu;
    }
    
    private IContributionItem getPropertiesItem() {
		return getItem(ActionFactory.PROPERTIES.getId(),
				ActionFactory.PROPERTIES.getCommandId(), null, null,
				WorkbenchMessages.Workbench_properties,
				WorkbenchMessages.Workbench_propertiesToolTip, null);
	}
    
    private IContributionItem getItem(String actionId, String commandId,
    		String image, String disabledImage, String label, String tooltip, String helpContextId) {
		ISharedImages sharedImages = getWindow().getWorkbench()
				.getSharedImages();

		IActionCommandMappingService acms = (IActionCommandMappingService) getWindow()
				.getService(IActionCommandMappingService.class);
		acms.map(actionId, commandId);

		CommandContributionItemParameter commandParm = new CommandContributionItemParameter(
				getWindow(), actionId, commandId, null, sharedImages
						.getImageDescriptor(image), sharedImages
						.getImageDescriptor(disabledImage), null, label, null,
				tooltip, CommandContributionItem.STYLE_PUSH, null, false);
		return new CommandContributionItem(commandParm);
	}
    private MenuManager createHelpMenu() {
		MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_help, IWorkbenchActionConstants.M_HELP);
		addSeparatorOrGroupMarker(menu, "group.intro"); //$NON-NLS-1$
		// See if a welcome or intro page is specified
		if (introAction != null) {
        	menu.add(introAction);
        } else {
        	if (window.getWorkbench().getIntroManager().hasIntro()) {
        		introAction = ActionFactory.INTRO.create(window);
        		register(introAction);
        		menu.add(introAction);
        	}
        }
		menu.add(helpAction);
		menu.add(new Separator());
		addSeparatorOrGroupMarker(menu, "group.assist");
		menu.add(new Separator());
		menu.add(aboutAction);
		return menu;
    }
    
    private void addSeparatorOrGroupMarker(MenuManager menu, String groupId) {
		String prefId = "useSeparator." + menu.getId() + "." + groupId; //$NON-NLS-1$ //$NON-NLS-2$
		boolean addExtraSeparators = IDEWorkbenchPlugin.getDefault()
				.getPreferenceStore().getBoolean(prefId);
		if (addExtraSeparators) {
			menu.add(new Separator(groupId));
		} else {
			menu.add(new GroupMarker(groupId));
		}
	}
	private IWorkbenchWindow getWindow() {
		return window;
	}

	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		statusLine.add(statusLineItem);
	}
    
}