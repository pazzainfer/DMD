package com.leven.dmd.gef.action.export;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import com.leven.dmd.gef.Messages;
/**
 * @author dzh
 * @date 2011-8-31 ����10:06:44
 */
public class ExportImageAction extends SelectionAction {

	public static final String ID = "com.leven.dmd.gef.action.export.ExportImageAction"; //$NON-NLS-1$

	private static final String[] EXTENSIONS = new String[] { "*.bmp", "*.jpg", //$NON-NLS-1$ //$NON-NLS-2$
			"*.png" }; //$NON-NLS-1$

	private static final int[] TYPES = new int[] { SWT.IMAGE_BMP,
			SWT.IMAGE_JPEG, SWT.IMAGE_PNG };

	public ExportImageAction(IWorkbenchPart part) {
		super(part);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		IWorkbenchPart part = getWorkbenchPart();
		if (part instanceof GraphicalEditor) {
			return true;
		}else if (part instanceof ContentOutline) {
			return true;
		}
		return false;
	}

	protected void init() {
		super.init();
		setText(Messages.ExportImageAction_4);
		this.setToolTipText(Messages.ExportImageAction_4);
		setId(ID);
	}

	public void run() {
		FileDialog dialog = new FileDialog(getWorkbenchPart().getSite()
				.getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(getText());
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(getWorkbenchPart().getSite().getShell(),
					getText(), Messages.ExportImageAction_6);
			return;
		}

		if (savePath != null) {
			int type = dialog.getFilterIndex();
			GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart()
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
					getText(), Messages.ExportImageAction_7);
		}
	}

}