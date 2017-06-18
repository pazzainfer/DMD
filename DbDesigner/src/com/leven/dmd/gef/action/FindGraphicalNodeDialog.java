package com.leven.dmd.gef.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.util.control.palette.CustomGraphicalEditorWithPalette;

public class FindGraphicalNodeDialog extends Dialog {
	private Button btnChooseNameId;
	private Text txtName;
	private Text txtID;
	private Button caseSensitiveButton;
	private Button upButton;
	private Button downButton;
	private Button wholeWordButton;
	private Label msgLabel;
	private CustomGraphicalEditorWithPalette editor;
	private int curIndex = -1;
	private boolean searchID = true;
	private Button find;

	/**
	 * GEFͼ�λ��༭���ͼԪ���ҶԻ���
	 * @param parentShell
	 * @param editor
	 */
	public FindGraphicalNodeDialog(Shell parentShell,
			CustomGraphicalEditorWithPalette editor) {
		super(parentShell);
		setShellStyle(getShellStyle() ^ 0x10000);
		this.editor = editor;
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);

		this.btnChooseNameId = new Button(container, 32);
		this.btnChooseNameId.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean select = FindGraphicalNodeDialog.this.btnChooseNameId
						.getSelection();
				FindGraphicalNodeDialog.this.searchID = select;
				FindGraphicalNodeDialog.this.txtName.setEditable(!select);
				FindGraphicalNodeDialog.this.txtID.setEditable(select);
			}
		});
		GridData bridData = new GridData();
		bridData.horizontalSpan = 2;
		this.btnChooseNameId.setLayoutData(bridData);
		this.btnChooseNameId.setText(Messages.FindGraphicalNodeDialog_0);
		this.searchID = this.btnChooseNameId.getSelection();

		Label label = new Label(container, 0);
		label.setText(Messages.FindGraphicalNodeDialog_1);

		this.txtName = new Text(container, 2048);
		this.txtName.setLayoutData(new GridData(768));
		this.txtName.setEditable(!this.btnChooseNameId.getSelection());
		addModifyListener(this.txtName);

		label = new Label(container, 0);
		label.setText(Messages.FindGraphicalNodeDialog_2);

		this.txtID = new Text(container, 2048);
		this.txtID.setLayoutData(new GridData(768));
		this.txtID.setEditable(this.btnChooseNameId.getSelection());
		addModifyListener(this.txtID);

		this.caseSensitiveButton = new Button(container, 32);
		GridData gridData = new GridData();
		this.caseSensitiveButton.setLayoutData(gridData);
		this.caseSensitiveButton.setText(Messages.FindGraphicalNodeDialog_3);

		Group group = new Group(container, 0);
		group.setText(Messages.FindGraphicalNodeDialog_4);
		gridData = new GridData();
		gridData.widthHint = 178;
		gridData.verticalSpan = 2;
		group.setLayoutData(gridData);
		GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		group.setLayout(gridLayout_1);

		this.upButton = new Button(group, 16);
		GridData gridData_1 = new GridData();
		this.upButton.setLayoutData(gridData_1);
		this.upButton.setText(Messages.FindGraphicalNodeDialog_5);

		this.downButton = new Button(group, 16);
		this.downButton.setSelection(true);
		this.downButton.setText(Messages.FindGraphicalNodeDialog_6);

		this.wholeWordButton = new Button(container, 32);
		gridData = new GridData(2);
		this.wholeWordButton.setLayoutData(gridData);
		this.wholeWordButton.setText(Messages.FindGraphicalNodeDialog_7);

		this.msgLabel = new Label(container, 0);
		gridData = new GridData(256);
		gridData.horizontalSpan = 2;
		this.msgLabel.setLayoutData(gridData);

		if (this.txtName.getText().length() > 0) {
			this.txtName.selectAll();
		}
		if (this.txtID.getText().length() > 0) {
			this.txtID.selectAll();
		}

		return container;
	}

	private void addModifyListener(final Text text) {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String s = text.getText();
				if (s!=null && !s.trim().equals("")) { //$NON-NLS-1$
					FindGraphicalNodeDialog.this.find.setEnabled(true);
				} else
					FindGraphicalNodeDialog.this.find.setEnabled(false);
			}
		});
	}

	protected void createButtonsForButtonBar(Composite parent) {
		this.find = createButton(parent, 15, Messages.FindGraphicalNodeDialog_9, true);
		this.find.setEnabled(false);
		createButton(parent, 1, Messages.FindGraphicalNodeDialog_10, false);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.FindGraphicalNodeDialog_11);
	}
	/**
	 * ͼԪ���Ҳ���
	 * @author leven
	 * 2012-9-18 ����02:03:10
	 */
	protected void find() {
		boolean isUpDirection = this.upButton.getSelection();

		String keyWords = this.searchID ? this.txtID.getText() : this.txtName
				.getText();
		this.msgLabel.setText(""); //$NON-NLS-1$

		Schema diagram = (Schema) this.editor.getModel();
		GraphicalViewer viewer = this.editor.getGraphicalViewer();
		List tables = diagram.getTables();
		List tablePackages = diagram.getTablePackages();
//		List list = diagram.getTables();
		List list = new ArrayList();
		list.addAll(tables);
		list.addAll(tablePackages);
		int size = list.size();
		if (!isUpDirection) {
			if (this.curIndex == -1) {
				this.curIndex = 0;
			}
			for (int i = this.curIndex; i < size; i++) {
				Object node = list.get(i);
				String labelText = ""; //$NON-NLS-1$
				this.curIndex += 1;
				if(node instanceof Table){
					labelText = this.searchID ? ((Table)node).getName() : ((Table)node).getCnName();
				} else if(node instanceof TablePackage){
					labelText = this.searchID ? ((TablePackage)node).getName() : ((TablePackage)node).getDescription();
				}
				if (compareWords(labelText, keyWords)) {
					Object editpart = viewer.getEditPartRegistry().get(node);
					selectEditPart((EditPart) editpart);
					return;
				}
			}
		} else {
			for (int i = this.curIndex - 1; i >= 0; i--) {
				Object node = list.get(i);
				String labelText = ""; //$NON-NLS-1$
				this.curIndex -= 1;
				if(node instanceof Table){
					labelText = this.searchID ? ((Table)node).getName() : ((Table)node).getCnName();
				} else if(node instanceof TablePackage){
					labelText = this.searchID ? ((TablePackage)node).getName() : ((TablePackage)node).getDescription();
				}
				if (compareWords(labelText, keyWords)) {
					Object editpart = viewer.getEditPartRegistry().get(node);
					selectEditPart((EditPart) editpart);
					return;
				}
			}
		}
		this.msgLabel.setText(Messages.FindGraphicalNodeDialog_15);
	}
	/**
	 * �Ƚϲ�ѯ�ַ��Ƿ�ƥ��
	 * @author leven
	 * 2012-9-18 ����02:02:37
	 * @param label
	 * @param keyWords
	 * @return
	 */
	private boolean compareWords(String label, String keyWords) {
		if (label == null) {
			return false;
		}
		boolean isCaseSensitive = this.caseSensitiveButton.getSelection();
		boolean isWholeWords = this.wholeWordButton.getSelection();

		String compareStr1 = label;
		String compareStr2 = keyWords;
		if (!isCaseSensitive) {
			compareStr1 = compareStr1.toUpperCase();
			compareStr2 = compareStr2.toUpperCase();
		}

		if (isWholeWords) {
			return compareStr1.equals(compareStr2);
		}
		return compareStr1.indexOf(compareStr2) != -1;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == 15) {
			find();
			return;
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * �趨������ָ��EditPart�����ͼ�α�ѡ��
	 * @author leven
	 * 2012-9-18 ����02:00:57
	 * @param editPart
	 */
	private void selectEditPart(EditPart editPart) {
		GraphicalViewer viewer = this.editor.getGraphicalViewer();
		this.editor.locateToEditPart(editPart);
		viewer.select(editPart);
	}

}