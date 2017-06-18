package com.leven.dmd.gef.wizard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.ControlEnableState;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer2;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import com.leven.dmd.gef.Messages;

public class CustomWizardDialog extends TitleAreaDialog implements IWizardContainer2,
	IPageChangeProvider  {

	public static final String WIZ_IMG_ERROR = "dialog_title_error_image"; //$NON-NLS-1$

	private IWizard wizard;

	private ArrayList createdWizards = new ArrayList();

	private ArrayList nestedWizards = new ArrayList();

	private IWizardPage currentPage = null;

	private long activeRunningOperations = 0;

	private long timeWhenLastJobFinished= -1;

	private boolean useCustomProgressMonitorPart= true;

	private String pageMessage;

	private int pageMessageType = IMessageProvider.NONE;

	private boolean needProcess = false;
	private String pageDescription;
	private ProgressMonitorPart progressMonitorPart;

	private Cursor waitCursor;

	private Cursor arrowCursor;

	private MessageDialog windowClosingDialog;

	private Button backButton;

	private Button nextButton;

	private Button finishButton;
	
	private Button applyButton;

	private Button cancelButton;

	private Button helpButton;

	private SelectionAdapter cancelListener;

	private boolean isMovingToPreviousPage = false;

	private Composite pageContainer;

	private PageContainerFillLayout pageContainerLayout = new PageContainerFillLayout(
			5, 5, 300, 225);

	private int pageWidth = SWT.DEFAULT;

	private int pageHeight = SWT.DEFAULT;

	private static final String FOCUS_CONTROL = "focusControl"; //$NON-NLS-1$

	private static final int RESTORE_ENTER_DELAY= 500;

	private boolean lockedUI = false;

	private ListenerList pageChangedListeners = new ListenerList();

	private ListenerList pageChangingListeners = new ListenerList();

	protected class PageContainerFillLayout extends Layout {
		public int marginWidth = 5;
		public int marginHeight = 5;
		public int minimumWidth = 0;
		public int minimumHeight = 0;

		public PageContainerFillLayout(int mw, int mh, int minW, int minH) {
			marginWidth = mw;
			marginHeight = mh;
			minimumWidth = minW;
			minimumHeight = minH;
		}

		public Point computeSize(Composite composite, int wHint, int hHint,
				boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point result = null;
			Control[] children = composite.getChildren();
			if (children.length > 0) {
				result = new Point(0, 0);
				for (int i = 0; i < children.length; i++) {
					Point cp = children[i].computeSize(wHint, hHint, force);
					result.x = Math.max(result.x, cp.x);
					result.y = Math.max(result.y, cp.y);
				}
				result.x = result.x + 2 * marginWidth;
				result.y = result.y + 2 * marginHeight;
			} else {
				Rectangle rect = composite.getClientArea();
				result = new Point(rect.width, rect.height);
			}
			result.x = Math.max(result.x, minimumWidth);
			result.y = Math.max(result.y, minimumHeight);
			if (wHint != SWT.DEFAULT) {
				result.x = wHint;
			}
			if (hHint != SWT.DEFAULT) {
				result.y = hHint;
			}
			return result;
		}

		public Rectangle getClientArea(Composite c) {
			Rectangle rect = c.getClientArea();
			rect.x = rect.x + marginWidth;
			rect.y = rect.y + marginHeight;
			rect.width = rect.width - 2 * marginWidth;
			rect.height = rect.height - 2 * marginHeight;
			return rect;
		}

		public void layout(Composite composite, boolean force) {
			Rectangle rect = getClientArea(composite);
			Control[] children = composite.getChildren();
			for (int i = 0; i < children.length; i++) {
				children[i].setBounds(rect);
			}
		}
		public void layoutPage(Control w) {
			w.setBounds(getClientArea(w.getParent()));
		}
		public void setPageLocation(Control w) {
			w.setLocation(marginWidth, marginHeight);
		}
	}
	public CustomWizardDialog(Shell parentShell, IWizard newWizard,boolean needProcess) {
		super(parentShell);
		this.needProcess = needProcess;
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
		setWizard(newWizard);
		cancelListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				cancelPressed();
			}
		};
	}

	private Object aboutToStart(boolean enableCancelButton) {
		Map savedState = null;
		if (getShell() != null) {
			// Save focus control
			Control focusControl = getShell().getDisplay().getFocusControl();
			if (focusControl != null && focusControl.getShell() != getShell()) {
				focusControl = null;
			}
			boolean needsProgressMonitor = wizard.needsProgressMonitor();
			
			Display d = getShell().getDisplay();
			waitCursor = new Cursor(d, SWT.CURSOR_WAIT);
			setDisplayCursor(waitCursor);
			
			if (useCustomProgressMonitorPart) {
				cancelButton.removeSelectionListener(cancelListener);
				arrowCursor = new Cursor(d, SWT.CURSOR_ARROW);
				cancelButton.setCursor(arrowCursor);
			}
			
			savedState = saveUIState(useCustomProgressMonitorPart && needsProgressMonitor && enableCancelButton);
			if (focusControl != null) {
				savedState.put(FOCUS_CONTROL, focusControl);
			}
			if (needsProgressMonitor) {
				if (enableCancelButton || useCustomProgressMonitorPart) {
					progressMonitorPart.attachToCancelComponent(cancelButton);
				}
				progressMonitorPart.setVisible(true);
			}
			
			if (timeWhenLastJobFinished == -1) {
				timeWhenLastJobFinished= 0;
				getShell().addTraverseListener(new TraverseListener() {
					public void keyTraversed(TraverseEvent e) {
						if (e.detail == SWT.TRAVERSE_RETURN || (e.detail == SWT.TRAVERSE_MNEMONIC && e.keyCode == 32)) {
							if (timeWhenLastJobFinished != 0 && System.currentTimeMillis() - timeWhenLastJobFinished < RESTORE_ENTER_DELAY) {
								e.doit= false;
								return;
							}
							timeWhenLastJobFinished= 0;
						}}
				});
			}
		}
		return savedState;
	}
	protected void backPressed() {
		IWizardPage page = currentPage.getPreviousPage();
		if (page == null) {
			return;
		}
		isMovingToPreviousPage = true;
		showPage(page);
	}

	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case IDialogConstants.HELP_ID: {
			helpPressed();
			break;
		}
		case IDialogConstants.BACK_ID: {
			backPressed();
			break;
		}
		case IDialogConstants.NEXT_ID: {
			nextPressed();
			break;
		}
		case IDialogConstants.FINISH_ID: {
			finishPressed();
			break;
		}
		}
	}

	private Point calculatePageSizeDelta(IWizardPage page) {
		Control pageControl = page.getControl();
		if (pageControl == null) {
			// control not created yet
			return new Point(0, 0);
		}
		Point contentSize = pageControl.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true);
		Rectangle rect = pageContainerLayout.getClientArea(pageContainer);
		Point containerSize = new Point(rect.width, rect.height);
		return new Point(Math.max(0, contentSize.x - containerSize.x), Math
				.max(0, contentSize.y - containerSize.y));
	}

	protected void cancelPressed() {
		if (activeRunningOperations <= 0) {
			setReturnCode(CANCEL);
			close();
		} else {
			cancelButton.setEnabled(false);
		}
	}

	public boolean close() {
		if (okToClose()) {
			return hardClose();
		}
		return false;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.addHelpListener(new HelpListener() {
			public void helpRequested(HelpEvent event) {
				if (currentPage != null) {
					currentPage.performHelp();
				}
			}
		});
	}

	protected void createButtonsForButtonBar(Composite parent) {
		((GridLayout) parent.getLayout()).makeColumnsEqualWidth = false;
		if (wizard.isHelpAvailable()) {
			helpButton = createButton(parent, IDialogConstants.HELP_ID,
					IDialogConstants.HELP_LABEL, false);
		}
		if (wizard.needsPreviousAndNextButtons()) {
			createPreviousAndNextButtons(parent);
		}
		applyButton = createApplyButton(parent);
		finishButton = createButton(parent, IDialogConstants.FINISH_ID,
				Messages.CustomWizardDialog_0, true);
		cancelButton = createCancelButton(parent);
		
		if (parent.getDisplay().getDismissalAlignment() == SWT.RIGHT) {
			finishButton.moveBelow(null);
		}
	}

	protected void setButtonLayoutData(Button button) {
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);

		widthHint = Math.min(widthHint,
				button.getDisplay().getBounds().width / 5);
		Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);

		button.setLayoutData(data);
	}

	private Button createCancelButton(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(Messages.CustomWizardDialog_1);
		setButtonLayoutData(button);
		button.setFont(parent.getFont());
		button.setData(new Integer(IDialogConstants.CANCEL_ID));
		button.addSelectionListener(cancelListener);
		return button;
	}
	
	private Button createApplyButton(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;
		applyButton = new Button(parent, SWT.PUSH);
		applyButton.setText(Messages.CustomWizardDialog_2);
		setButtonLayoutData(applyButton);
		applyButton.setFont(parent.getFont());
		applyButton.setData(new Integer(IDialogConstants.OK_ID));
		applyButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(2);
				finishPressed();
			}
		});
		return applyButton;
	}

	protected Button getButton(int id) {
		if (id == IDialogConstants.CANCEL_ID) {
			return cancelButton;
		}
		return super.getButton(id);
	}

	protected Control createContents(Composite parent) {
		wizard.addPages();
		Control contents = super.createContents(parent);
		createPageControls();
		showStartingPage();
		return contents;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		pageContainer = createPageContainer(composite);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = pageWidth;
		gd.heightHint = pageHeight;
		pageContainer.setLayoutData(gd);
		pageContainer.setFont(parent.getFont());
		if(needProcess){
			progressMonitorPart= createProgressMonitorPart(composite, new GridLayout());
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			progressMonitorPart.setLayoutData(gridData);
			progressMonitorPart.setVisible(false);
			Label separator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
			separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		applyDialogFont(progressMonitorPart);
		return composite;
	}

	protected ProgressMonitorPart createProgressMonitorPart(
			Composite composite, GridLayout pmlayout) {
		useCustomProgressMonitorPart= false;
		return new ProgressMonitorPart(composite, pmlayout) {
			String currentTask = null;

			public void setBlocked(IStatus reason) {
				super.setBlocked(reason);
				if (!lockedUI) {
					getBlockedHandler().showBlocked(getShell(), this, reason,
							currentTask);
				}
			}

			public void clearBlocked() {
				super.clearBlocked();
				if (!lockedUI) {
					getBlockedHandler().clearBlocked();
				}
			}

			public void beginTask(String name, int totalWork) {
				super.beginTask(name, totalWork);
				currentTask = name;
			}

			public void setTaskName(String name) {
				super.setTaskName(name);
				currentTask = name;
			}

			public void subTask(String name) {
				super.subTask(name);
				if (currentTask == null) {
					currentTask = name;
				}
			}
		};
	}

	private Composite createPageContainer(Composite parent) {
		Composite result = new Composite(parent, SWT.NULL);
		result.setLayout(pageContainerLayout);
		return result;
	}

	private void createPageControls() {
		wizard.createPageControls(pageContainer);
		IWizardPage[] pages = wizard.getPages();
		for (int i = 0; i < pages.length; i++) {
			IWizardPage page = pages[i];
			if (page.getControl() != null) {
				page.getControl().setVisible(false);
			}
		}
	}

	private Composite createPreviousAndNextButtons(Composite parent) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Composite composite = new Composite(parent, SWT.NONE);
		// create a layout with spacing and margins appropriate for the font
		// size.
		GridLayout layout = new GridLayout();
		layout.numColumns = 0; // will be incremented by createButton
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_CENTER
				| GridData.VERTICAL_ALIGN_CENTER);
		composite.setLayoutData(data);
		composite.setFont(parent.getFont());
		backButton = createButton(composite, IDialogConstants.BACK_ID,
				IDialogConstants.BACK_LABEL, false);
		nextButton = createButton(composite, IDialogConstants.NEXT_ID,
				IDialogConstants.NEXT_LABEL, false);
		return composite;
	}

	private MessageDialog createWizardClosingDialog() {
		MessageDialog result = new MessageDialog(getShell(),
				JFaceResources.getString("WizardClosingDialog.title"), //$NON-NLS-1$
				null,
				JFaceResources.getString("WizardClosingDialog.message"), //$NON-NLS-1$
				MessageDialog.QUESTION,
				new String[] { IDialogConstants.OK_LABEL }, 0) {
			protected int getShellStyle() {
				return super.getShellStyle() | SWT.SHEET;
			}
		};
		return result;
	}

	protected void finishPressed() {
		if (wizard.performFinish()) {
			for (int i = 0; i < nestedWizards.size() - 1; i++) {
				((IWizard) nestedWizards.get(i)).performFinish();
			}
			setReturnCode(OK);
			hardClose();
		} else {
			hardClose();
		}
	}

	public IWizardPage getCurrentPage() {
		return currentPage;
	}
	protected IProgressMonitor getProgressMonitor() {
		return progressMonitorPart;
	}

	protected IWizard getWizard() {
		return wizard;
	}

	private boolean hardClose() {
		for (int i = 0; i < createdWizards.size(); i++) {
			IWizard createdWizard = (IWizard) createdWizards.get(i);
			createdWizard.dispose();
			createdWizard.setContainer(null);
		}
		setTitleImage(null);
		return super.close();
	}

	protected void helpPressed() {
		if (currentPage != null) {
			currentPage.performHelp();
		}
	}

	protected void nextPressed() {
		IWizardPage page = currentPage.getNextPage();
		if (page == null) {
			return;
		}

		showPage(page);
	}

	private boolean doPageChanging(IWizardPage targetPage) {
		PageChangingEvent e = new PageChangingEvent(this, getCurrentPage(),
				targetPage);
		firePageChanging(e);
		// Prevent navigation if necessary
		return e.doit;
	}

	private boolean okToClose() {
		if (activeRunningOperations > 0) {
			synchronized (this) {
				windowClosingDialog = createWizardClosingDialog();
			}
			windowClosingDialog.open();
			synchronized (this) {
				windowClosingDialog = null;
			}
			return false;
		}
		return wizard.performCancel();
	}

	private void restoreEnableState(Control w, Map h, String key) {
		if (w != null) {
			Boolean b = (Boolean) h.get(key);
			if (b != null) {
				w.setEnabled(b.booleanValue());
			}
		}
	}

	private void restoreUIState(Map state) {
		restoreEnableState(backButton, state, "back"); //$NON-NLS-1$
		restoreEnableState(nextButton, state, "next"); //$NON-NLS-1$
		restoreEnableState(finishButton, state, "finish"); //$NON-NLS-1$
		restoreEnableState(cancelButton, state, "cancel"); //$NON-NLS-1$
		restoreEnableState(helpButton, state, "help"); //$NON-NLS-1$
		Object pageValue = state.get("page"); //$NON-NLS-1$
		if (pageValue != null) {
			((ControlEnableState) pageValue).restore();
		}
	}

	public void run(boolean fork, boolean cancelable,
			IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		// The operation can only be canceled if it is executed in a separate
		// thread.
		// Otherwise the UI is blocked anyway.
		Object state = null;
		if (activeRunningOperations == 0) {
			state = aboutToStart(fork && cancelable);
		}
		activeRunningOperations++;
		try {
			if (!fork) {
				lockedUI = true;
			}
			ModalContext.run(runnable, fork, getProgressMonitor(), getShell()
					.getDisplay());
			lockedUI = false;
		} finally {
			// explicitly invoke done() on our progress monitor so that its
			// label does not spill over to the next invocation, see bug 271530
			if (getProgressMonitor() != null) {
				getProgressMonitor().done();
			}
			// Stop if this is the last one
			if (state != null) {
				timeWhenLastJobFinished= System.currentTimeMillis();
				stopped(state);
			}
			activeRunningOperations--;
		}
	}

	private void saveEnableStateAndSet(Control w, Map h, String key,
			boolean enabled) {
		if (w != null) {
			h.put(key, w.getEnabled() ? Boolean.TRUE : Boolean.FALSE);
			w.setEnabled(enabled);
		}
	}

	private Map saveUIState(boolean keepCancelEnabled) {
		Map savedState = new HashMap(10);
		saveEnableStateAndSet(backButton, savedState, "back", false); //$NON-NLS-1$
		saveEnableStateAndSet(nextButton, savedState, "next", false); //$NON-NLS-1$
		saveEnableStateAndSet(finishButton, savedState, "finish", false); //$NON-NLS-1$
		saveEnableStateAndSet(cancelButton, savedState,	"cancel", keepCancelEnabled); //$NON-NLS-1$
		saveEnableStateAndSet(helpButton, savedState, "help", false); //$NON-NLS-1$
		if (currentPage != null) {
			savedState
					.put(
							"page", ControlEnableState.disable(currentPage.getControl())); //$NON-NLS-1$
		}
		return savedState;
	}

	private void setDisplayCursor(Cursor c) {
		Shell[] shells = getShell().getDisplay().getShells();
		for (int i = 0; i < shells.length; i++) {
			shells[i].setCursor(c);
		}
	}

	public void setMinimumPageSize(int minWidth, int minHeight) {
		Assert.isTrue(minWidth >= 0 && minHeight >= 0);
		pageContainerLayout.minimumWidth = minWidth;
		pageContainerLayout.minimumHeight = minHeight;
	}

	public void setMinimumPageSize(Point size) {
		setMinimumPageSize(size.x, size.y);
	}

	public void setPageSize(int width, int height) {
		pageWidth = width;
		pageHeight = height;
	}

	public void setPageSize(Point size) {
		setPageSize(size.x, size.y);
	}

	protected void setWizard(IWizard newWizard) {
		wizard = newWizard;
		wizard.setContainer(this);
		if (!createdWizards.contains(wizard)) {
			createdWizards.add(wizard);
			nestedWizards.add(wizard);
			if (pageContainer != null) {
				createPageControls();
				updateSizeForWizard(wizard);
				pageContainer.layout(true);
			}
		} else {
			int size = nestedWizards.size();
			if (size >= 2 && nestedWizards.get(size - 2) == wizard) {
				nestedWizards.remove(size - 1);
			} else {
				nestedWizards.add(wizard);
			}
		}
	}

	public void showPage(IWizardPage page) {
		if (page == null || page == currentPage) {
			return;
		}

		if (!isMovingToPreviousPage) {
			// remember my previous page.
			page.setPreviousPage(currentPage);
		} else {
			isMovingToPreviousPage = false;
		}

		// If page changing evaluation unsuccessful, do not change the page
		if (!doPageChanging(page))
			return;

		// Update for the new page in a busy cursor if possible
		if (getContents() == null) {
			updateForPage(page);
		} else {
			final IWizardPage finalPage = page;
			BusyIndicator.showWhile(getContents().getDisplay(), new Runnable() {
				public void run() {
					updateForPage(finalPage);
				}
			});
		}
	}

	private void updateForPage(IWizardPage page) {
		// ensure this page belongs to the current wizard
		if (wizard != page.getWizard()) {
			setWizard(page.getWizard());
		}
		if (page.getControl() == null) {
			page.createControl(pageContainer);
			Assert.isNotNull(page.getControl(), JFaceResources.format(
					JFaceResources.getString("WizardDialog.missingSetControl"), //$NON-NLS-1$
					new Object[] { page.getName() }));
			updateSize(page);
		}
		IWizardPage oldPage = currentPage;
		currentPage = page;

		currentPage.setVisible(true);
		if (oldPage != null) {
			oldPage.setVisible(false);
		}
		update();
	}

	private void showStartingPage() {
		currentPage = wizard.getStartingPage();
		if (currentPage == null) {
			return;
		}
		if (currentPage.getControl() == null) {
			currentPage.createControl(pageContainer);
			Assert.isNotNull(currentPage.getControl());
		}
		currentPage.setVisible(true);
		update();
	}

	private void stopped(Object savedState) {
		if (getShell() != null && !getShell().isDisposed()) {
			if (wizard.needsProgressMonitor()) {
				progressMonitorPart.setVisible(false);
				progressMonitorPart.removeFromCancelComponent(cancelButton);
			}
			Map state = (Map) savedState;
			restoreUIState(state);
			setDisplayCursor(null);
			if (useCustomProgressMonitorPart) {
				cancelButton.addSelectionListener(cancelListener);
				cancelButton.setCursor(null);
				arrowCursor.dispose();
				arrowCursor = null;
			}
			waitCursor.dispose();
			waitCursor = null;
			Control focusControl = (Control) state.get(FOCUS_CONTROL);
			if (focusControl != null && !focusControl.isDisposed()) {
				focusControl.setFocus();
			}
		}
	}

	protected void update() {
		updateWindowTitle();
		updateTitleBar();
		updateButtons();

		firePageChanged(new PageChangedEvent(this, getCurrentPage()));
	}

	public void updateButtons() {
		boolean canFlipToNextPage = false;
		boolean canFinish = wizard.canFinish();
		if (backButton != null) {
			backButton.setEnabled(currentPage.getPreviousPage() != null);
		}
		if (nextButton != null) {
			canFlipToNextPage = currentPage.canFlipToNextPage();
			nextButton.setEnabled(canFlipToNextPage);
		}
		finishButton.setEnabled(canFinish);
		applyButton.setEnabled(canFinish);
		// finish is default unless it is disabled and next is enabled
		if (canFlipToNextPage && !canFinish) {
			getShell().setDefaultButton(nextButton);
		} else {
			getShell().setDefaultButton(finishButton);
		}
	}

	private void updateDescriptionMessage() {
		pageDescription = currentPage.getDescription();
		setMessage(pageDescription);
	}

	public void updateMessage() {

		if (currentPage == null) {
			return;
		}

		pageMessage = currentPage.getMessage();
		if (pageMessage != null && currentPage instanceof IMessageProvider) {
			pageMessageType = ((IMessageProvider) currentPage).getMessageType();
		} else {
			pageMessageType = IMessageProvider.NONE;
		}
		if (pageMessage == null) {
			setMessage(pageDescription);
		} else {
			setMessage(pageMessage, pageMessageType);
		}
		setErrorMessage(currentPage.getErrorMessage());
	}

	private void setShellSize(int width, int height) {
		Rectangle size = getShell().getBounds();
		size.height = height;
		size.width = width;
		getShell().setBounds(getConstrainedShellBounds(size));
	}

	protected void updateSize(IWizardPage page) {
		if (page == null || page.getControl() == null) {
			return;
		}
		updateSizeForPage(page);
		pageContainerLayout.layoutPage(page.getControl());
	}

	public void updateSize() {
		updateSize(currentPage);
	}

	private void updateSizeForPage(IWizardPage page) {
		Point delta = calculatePageSizeDelta(page);
		if (delta.x > 0 || delta.y > 0) {
			Shell shell = getShell();
			Point shellSize = shell.getSize();
			setShellSize(shellSize.x + delta.x, shellSize.y + delta.y);
			constrainShellSize();
		}
	}

	private void updateSizeForWizard(IWizard sizingWizard) {
		Point delta = new Point(0, 0);
		IWizardPage[] pages = sizingWizard.getPages();
		for (int i = 0; i < pages.length; i++) {
			Point pageDelta = calculatePageSizeDelta(pages[i]);
			delta.x = Math.max(delta.x, pageDelta.x);
			delta.y = Math.max(delta.y, pageDelta.y);
		}
		if (delta.x > 0 || delta.y > 0) {
			Shell shell = getShell();
			Point shellSize = shell.getSize();
			setShellSize(shellSize.x + delta.x, shellSize.y + delta.y);
		}
	}

	public void updateTitleBar() {
		String s = null;
		if (currentPage != null) {
			s = currentPage.getTitle();
		}
		if (s == null) {
			s = ""; //$NON-NLS-1$
		}
		setTitle(s);
		if (currentPage != null) {
			setTitleImage(currentPage.getImage());
			updateDescriptionMessage();
		}
		updateMessage();
	}

	public void updateWindowTitle() {
		if (getShell() == null) {
			// Not created yet
			return;
		}
		String title = wizard.getWindowTitle();
		if (title == null) {
			title = ""; //$NON-NLS-1$
		}
		getShell().setText(title);
	}

	public Object getSelectedPage() {
		return getCurrentPage();
	}

	public void addPageChangedListener(IPageChangedListener listener) {
		pageChangedListeners.add(listener);
	}

	public void removePageChangedListener(IPageChangedListener listener) {
		pageChangedListeners.remove(listener);
	}

	protected void firePageChanged(final PageChangedEvent event) {
		Object[] listeners = pageChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final IPageChangedListener l = (IPageChangedListener) listeners[i];
			SafeRunnable.run(new SafeRunnable() {
				public void run() {
					l.pageChanged(event);
				}
			});
		}
	}

	public void addPageChangingListener(IPageChangingListener listener) {
		pageChangingListeners.add(listener);
	}

	public void removePageChangingListener(IPageChangingListener listener) {
		pageChangingListeners.remove(listener);
	}

	protected void firePageChanging(final PageChangingEvent event) {
		Object[] listeners = pageChangingListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final IPageChangingListener l = (IPageChangingListener) listeners[i];
			SafeRunnable.run(new SafeRunnable() {
				public void run() {
					l.handlePageChanging(event);
				}
			});
		}
	}
	
}
