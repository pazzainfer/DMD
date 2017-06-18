package com.leven.dmd.gef.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.pro.util.ImageHelper;
import com.leven.dmd.pro.util.ImageKeys;

public class SchemaEditorInput implements IEditorInput {

	public Object getAdapter(Class adapter) {
		return null;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageHelper.getImageDescriptor(ImageKeys.SCHEMA);
	}

	public String getName() {
		return Messages.SchemaEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return Messages.SchemaEditorInput_0;
	}

}
