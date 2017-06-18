package com.leven.dmd.pro.action;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;

import com.leven.dmd.gef.editor.SchemaDiagramEditor;

public class OpenFileHandler implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	public void dispose() {

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		String filePath = "D:/temp/space/a.schema";
		try {
			IDE.openEditor(window.getActivePage(), new URI("file:///" + filePath), SchemaDiagramEditor.EDITOR_ID, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isHandled() {
		return false;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {

	}

}
