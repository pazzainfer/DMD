package com.leven.dmd.pro.exp;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.leven.dmd.pro.Activator;

public class GefUiPlugin extends AbstractUIPlugin
{
  public static final String PLUGIN_ID = Activator.PLUGIN_ID;
  private static GefUiPlugin plugin;

  public GefUiPlugin()
  {
    plugin = this;
  }

  public void start(BundleContext context)
    throws Exception
  {
    super.start(context);
  }

  public void stop(BundleContext context)
    throws Exception
  {
    plugin = null;
    super.stop(context);
  }

  public static GefUiPlugin getDefault()
  {
    return plugin;
  }

  public void setErrorStatusMessage(String messageInfo)
  {
    IWorkbenchWindow window = getDefault().getWorkbench().getActiveWorkbenchWindow();
    if (window == null) return;
    IWorkbenchPage activePage = window.getActivePage();
    if (activePage == null) return;
    IEditorPart editorPart = activePage.getActiveEditor();
    if (editorPart == null) return;
    IStatusLineManager status = editorPart.getEditorSite
      ().getActionBars().getStatusLineManager();
    status.setErrorMessage(messageInfo);
  }
}