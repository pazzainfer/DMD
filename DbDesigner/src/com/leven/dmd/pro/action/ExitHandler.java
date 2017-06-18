package com.leven.dmd.pro.action;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExitHandler implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	public void dispose() {

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerUtil.getActiveWorkbenchWindow(event).close(); 
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
