package com.leven.dmd.gef.editor;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;
/**
 * �Զ������߹���
 * @author leven
 * 2012-9-3 ����02:57:11
 */
public class CustomConnectionCreationToolEntry extends CreationToolEntry {
	public CustomConnectionCreationToolEntry(String label, String shortDesc,
			CreationFactory factory, ImageDescriptor iconSmall,
			ImageDescriptor iconLarge) {
		super(label, shortDesc, factory, iconSmall, iconLarge);
		setToolClass(CustomConnectionCreationTool.class);
		setUserModificationPermission(PERMISSION_NO_MODIFICATION);
	}
}
