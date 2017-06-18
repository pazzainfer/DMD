package com.leven.dmd.gef.action.export;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.Messages;

public class ExportImageInBarAction extends Action {
	public static final String ID = "com.leven.dmd.gef.action.export.ExportImageInBarAction"; //$NON-NLS-1$
	private static final String[] EXTENSIONS = new String[] { "*.bmp", "*.jpg", //$NON-NLS-1$ //$NON-NLS-2$
		"*.png" }; //$NON-NLS-1$

	private static final int[] TYPES = new int[] { SWT.IMAGE_BMP,
		SWT.IMAGE_JPEG, SWT.IMAGE_PNG };

	public ExportImageInBarAction(){
		init();
	}
	private void init() {
		setText(Messages.ExportImageInBarAction_4);
		this.setToolTipText(Messages.ExportImageInBarAction_4);
		setId(ID);
	}

	public void run() {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
				, SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.ExportImageInBarAction_6);
			return;
		}

		if (savePath != null) {
			int type = dialog.getFilterIndex();
			GraphicalViewer viewer = (GraphicalViewer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart()
					.getAdapter(GraphicalViewer.class);
			LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(
					LayerManager.ID); //��ȡ�̳�LayerManager��RootEditPart,��ȻҲ�в��̳�LayerManager��,����Ҫ�ж�һ��
			if (lm == null)
				return;
			IFigure f = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);//��ȡ��Ҫ����ΪͼƬ��figure,�����ȡRootEditPart�����ݲ�figure
			if (f == null)
				return;

			Image rootImage = ImageUtil.getFigureImage(f, f.getBounds().width,
					f.getBounds().height);
			ImageUtil.saveImage(rootImage, savePath, TYPES[type]); //����ImageUtil
			rootImage.dispose(); //�ͷ���Դ
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					getText(), Messages.ExportImageInBarAction_7);
		}
	}

}
