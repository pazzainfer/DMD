package com.leven.dmd.pro.util.control.palette;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.internal.InternalGEFPlugin;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

public abstract class CustomGraphicalEditorWithPalette extends GraphicalEditor {

	private PaletteViewerProvider provider;
	private CustomPaletteComposite splitter;
	private CustomPalettePage page;
	private String paletteTitle;
	private Image paletteIcon;
	
	private CommandStack fCommandStack = new CommandStack(){
		public void execute(Command command){
			if ((command == null) || (!command.canExecute())) {
				return;
			}
			if (!((PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()) instanceof CustomGraphicalEditorWithPalette)) {
				return;
			}
			super.execute(command);
		}
	};
	public CommandStack getCommandStack(){
		return fCommandStack;
	}
	
	public void locateToEditPart(EditPart editPart) {
		if (editPart != null)
			getGraphicalViewer().reveal(editPart);
	}

	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}
	public abstract Object getModel();
	/**
	 * �Զ����������ɫ���ͼ�α༭��
	 * @param paletteTitle ��ɫ�����
	 * @param paletteIcon ��ɫ��ͼ�ζ���
	 */
	public CustomGraphicalEditorWithPalette(String paletteTitle,Image paletteIcon) {
		super();
		this.paletteTitle = paletteTitle;
		this.paletteIcon = paletteIcon;
	}
	
	protected void initializeGraphicalViewer() {
		splitter.hookDropTargetListener(getGraphicalViewer());
	}

	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain());
	}
	
	protected CustomPalettePage createPalettePage() {
		return new CustomPalettePage(getPaletteViewerProvider());
	}

	public void createPartControl(Composite parent) {
		splitter = new CustomPaletteComposite(parent, SWT.NONE, getSite()
				.getPage(), getPaletteViewerProvider(), getPalettePreferences(),
				paletteTitle,paletteIcon);
		super.createPartControl(splitter);
		splitter.setGraphicalControl(getGraphicalControl());
		if (page != null) {
			splitter.setExternalViewer(page.getPaletteViewer());
			page = null;
		}
	}

	public Object getAdapter(Class type) {
		if (type == PalettePage.class) {
			if (splitter == null) {
				page = createPalettePage();
				return page;
			}
			return createPalettePage();
		}
		return super.getAdapter(type);
	}

	protected Control getGraphicalControl() {
		return getGraphicalViewer().getControl();
	}

	protected CustomPaletteComposite.FlyoutPreferences getPalettePreferences() {
		return CustomPaletteComposite.createFlyoutPreferences(InternalGEFPlugin
				.getDefault().getPluginPreferences());
	}

	protected abstract PaletteRoot getPaletteRoot();

	protected final PaletteViewerProvider getPaletteViewerProvider() {
		if (provider == null)
			provider = createPaletteViewerProvider();
		return provider;
	}
	
	protected void setEditDomain(DefaultEditDomain ed) {
		super.setEditDomain(ed);
		getEditDomain().setPaletteRoot(getPaletteRoot());
	}

	protected class CustomPalettePage extends PaletteViewerPage {
		public CustomPalettePage(PaletteViewerProvider provider) {
			super(provider);
		}

		public void createControl(Composite parent) {
			super.createControl(parent);
			if (splitter != null)
				splitter.setExternalViewer(viewer);
		}

		public void dispose() {
			if (splitter != null){
				splitter.setExternalViewer(null);
			}
			if(fCommandStack!=null){
				fCommandStack.dispose();
			}
			super.dispose();
		}

		public PaletteViewer getPaletteViewer() {
			return viewer;
		}
	}

}