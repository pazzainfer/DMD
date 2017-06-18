package com.leven.dmd.gef.wizard.tableeditor.action;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class ColumnAddWizardPage extends WizardPage {
	private Button inputButton;
	private Button selectTemplateButton;
	private Button selectTableButton;
	private Text selectedText;
	private Button selectButton;
	
	private Text columnNameText;
	private Combo columnTypeCombo;
	private Text columnLengthText;
	private Text columnCnNameText;
	private Text columnScaleText;
	private Text columnDespText;
	private Link addToSchemaLink;
	private Button yesPkButton;
	private Button noPkButton;
	
	private ColumnAddWizard wizard;
	private SchemaTemplate schemaTemplate;
	private Schema schema;
	
	private ColumnTemplate template;
	/**
	 * ѡ��ť����,0:�ֶ�����;1:��ģ�����ѡ;2:�ӱ��ֶ���ѡ
	 */
	private int optionFlag = 0;
	
	protected ColumnAddWizardPage(ColumnAddWizard wizard,Schema schema) {
		super("ColumnAddWizardPage"); //$NON-NLS-1$
		this.wizard = wizard;
		this.schema = schema;
		schemaTemplate = schema.getSchemaTemplate();
		this.setTitle(Messages.ColumnAddWizardPage_1);
		this.setMessage(Messages.ColumnAddWizardPage_2, 2);
		this.setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1,false));
		
		GridData labGrid = new GridData(70,14);
		GridData textGrid = new GridData(GridData.FILL_HORIZONTAL);
		textGrid.heightHint = 14;
		
		Group selectGroup = new Group(comp,SWT.NONE);
		GridData selectGrid = new GridData(GridData.FILL_HORIZONTAL);
		selectGrid.heightHint = 60;
		selectGroup.setLayoutData(selectGrid);
		selectGroup.setLayout(new GridLayout(2,false));
		Composite selcComp = new Composite(selectGroup, SWT.NONE);
		GridData selcCompData = new GridData(GridData.FILL_HORIZONTAL);
		selcCompData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		selcCompData.horizontalSpan = 2;
		selcCompData.heightHint = 20;
		selcComp.setLayoutData(selcCompData);
		selcComp.setLayout(new RowLayout());
		inputButton = new Button(selcComp, SWT.RADIO);
		inputButton.setText(Messages.ColumnAddWizardPage_3);
		inputButton.setLayoutData(new RowData(70,14));
		inputButton.setSelection(true);
		inputButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedText.setEnabled(false);
				selectedText.setText(""); //$NON-NLS-1$
				selectButton.setEnabled(false);
				optionFlag = 0;
				columnNameText.setEnabled(true);
				columnTypeCombo.setEnabled(true);
				String selcText = columnTypeCombo.getText();
				if(!ColumnType.hasLength(selcText)){
					columnLengthText.setEnabled(false);
					columnLengthText.setText(""); //$NON-NLS-1$
				}else {
					columnLengthText.setEnabled(true);
				}
				if(!ColumnType.hasScale(selcText)){
					columnScaleText.setEnabled(false);
					columnScaleText.setText(""); //$NON-NLS-1$
				}else {
					columnScaleText.setEnabled(true);
				}
				columnCnNameText.setEnabled(true);
				updateComplete();
			}
		});
		selectTemplateButton = new Button(selcComp, SWT.RADIO);
		selectTemplateButton.setText(Messages.ColumnAddWizardPage_7);
		selectTemplateButton.setLayoutData(new RowData(110,14));
		selectTemplateButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedText.setEnabled(true);
				selectButton.setEnabled(true);
				optionFlag = 1;
				columnTypeCombo.setEnabled(false);
				columnLengthText.setEnabled(false);
				columnLengthText.setText(""); //$NON-NLS-1$
				columnScaleText.setEnabled(false);
				columnScaleText.setText(""); //$NON-NLS-1$
				columnNameText.setEnabled(true);
				columnCnNameText.setEnabled(true);
				updateComplete();
			}
		});
		/*selectTableButton = new Button(selcComp, SWT.RADIO);
		selectTableButton.setText(Messages.ColumnAddWizardPage_10);
		selectTableButton.setLayoutData(new RowData(130,14));
		selectTableButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedText.setEnabled(true);
				selectButton.setEnabled(true);
				optionFlag = 2;
				columnNameText.setEnabled(false);
				columnTypeCombo.setEnabled(false);
				columnLengthText.setEnabled(false);
				columnLengthText.setText(""); //$NON-NLS-1$
				columnScaleText.setEnabled(false);
				columnScaleText.setText(""); //$NON-NLS-1$
				columnCnNameText.setEnabled(false);
				updateComplete();
			}
		});*/
		
		selectedText = new Text(selectGroup, SWT.BORDER);
		selectedText.setEditable(false);
		selectedText.setEnabled(false);
		selectedText.setLayoutData(textGrid);
		selectedText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				updateComplete();
			}
		});
		selectButton = new Button(selectGroup, SWT.NONE);
		selectButton.setText(Messages.ColumnAddWizardPage_13);
		selectButton.setEnabled(false);
		selectButton.setLayoutData(new GridData(50,23));
		selectButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				openColumnSelectDialog();
			}
		});
		
		Composite pkComp = new Composite(comp,SWT.NONE);
		GridData pkData = new GridData(GridData.FILL_HORIZONTAL);
		pkData.heightHint = 18;
		pkComp.setLayoutData(pkData);
		pkComp.setLayout(new RowLayout());
		Label pkLab = new Label(pkComp, SWT.NONE);
		pkLab.setText(Messages.ColumnAddWizardPage_14);
		yesPkButton = new Button(pkComp, SWT.RADIO);
		yesPkButton.setText(Messages.ColumnAddWizardPage_15);
		yesPkButton.setSelection(false);
		
		noPkButton = new Button(pkComp, SWT.RADIO);
		noPkButton.setText(Messages.ColumnAddWizardPage_16);
		noPkButton.setSelection(true);
		
		Group textGroup = new Group(comp,SWT.NONE);
		textGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		textGroup.setLayout(new GridLayout(2,false));
		
		Label columnNameLab = new Label(textGroup, SWT.NONE);
		columnNameLab.setText(Messages.ColumnAddWizardPage_17);
		columnNameLab.setLayoutData(labGrid);
		
		columnNameText = new Text(textGroup, SWT.BORDER);
		columnNameText.setLayoutData(textGrid);
		columnNameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				updateComplete();
			}
		});
		
		Label columnCnNameLab = new Label(textGroup, SWT.NONE);
		columnCnNameLab.setText(Messages.ColumnAddWizardPage_18);
		columnCnNameLab.setLayoutData(labGrid);
		
		columnCnNameText = new Text(textGroup, SWT.BORDER);
		columnCnNameText.setLayoutData(textGrid);
		columnCnNameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				updateComplete();
			}
		});
		
		Label columnTypeLab = new Label(textGroup, SWT.NONE);
		columnTypeLab.setText(Messages.ColumnAddWizardPage_19);
		columnTypeLab.setLayoutData(labGrid);
		columnTypeCombo = new Combo(textGroup, SWT.READ_ONLY);
		columnTypeCombo.setItems(ColumnType.typeArray);
		columnTypeCombo.setLayoutData(textGrid);
		columnTypeCombo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selcText = columnTypeCombo.getText();
				if(!ColumnType.hasLength(selcText)){
					columnLengthText.setEnabled(false);
					columnLengthText.setText(""); //$NON-NLS-1$
				}else {
					columnLengthText.setEnabled(true);
				}
				if(!ColumnType.hasScale(selcText)){
					columnScaleText.setEnabled(false);
					columnScaleText.setText(""); //$NON-NLS-1$
				}else {
					columnScaleText.setEnabled(true);
				}
				updateComplete();
			}
		});
		
		
		Label columnLengthLab = new Label(textGroup, SWT.NONE);
		columnLengthLab.setText(Messages.ColumnAddWizardPage_22);
		columnLengthLab.setLayoutData(labGrid);
		
		columnLengthText = new Text(textGroup, SWT.BORDER);
		columnLengthText.setLayoutData(textGrid);
		columnLengthText.setEnabled(false);
		columnLengthText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				updateComplete();
			}
		});
		
		Label columnScaleLab = new Label(textGroup, SWT.NONE);
		columnScaleLab.setText(Messages.ColumnAddWizardPage_23);
		columnScaleLab.setLayoutData(labGrid);
		
		columnScaleText = new Text(textGroup, SWT.BORDER);
		columnScaleText.setEnabled(false);
		columnScaleText.setLayoutData(textGrid);
		columnScaleText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				updateComplete();
			}
		});

		addToSchemaLink = new Link(textGroup, SWT.NONE);
		addToSchemaLink.setText(Messages.ColumnAddWizardPage_24);
		addToSchemaLink.setLayoutData(selcCompData);
		addToSchemaLink.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				addColumnToTemplate();
			}
		});
		
		Label columnDespLab = new Label(textGroup, SWT.NONE);
		columnDespLab.setText(Messages.ColumnAddWizardPage_0);
		columnDespLab.setLayoutData(labGrid);
		columnDespText = new Text(textGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		GridData despTxtGrid = new GridData(GridData.FILL_BOTH);
		despTxtGrid.minimumHeight = 50;
		columnDespText.setLayoutData(despTxtGrid);
		
		setControl(comp);
	}

	private void updateComplete(){
		String message;
		if(optionFlag==0){
			if((message=validateInput())==null){
				this.setMessage(Messages.ColumnAddWizardPage_25, 1);
				this.setPageComplete(true);
			}else {
				this.setMessage(message, 3);
				this.setPageComplete(false);
			}
		} else {
			if((message=validateInput())!=null){
				this.setMessage(message, 3);
				this.setPageComplete(false);
				return;
			}
			if(selectedText.getText().trim().equals("")){ //$NON-NLS-1$
				this.setMessage(Messages.ColumnAddWizardPage_27 + (optionFlag==1?Messages.ColumnAddWizardPage_28:Messages.ColumnAddWizardPage_29) + Messages.ColumnAddWizardPage_30, 3);
				this.setPageComplete(false);
			}else{
				this.setMessage(Messages.ColumnAddWizardPage_31, 1);
				this.setPageComplete(true);
			}
		}
	}
	/**
	 * ��֤�û�������
	 * @author leven
	 * 2012-11-1 ����03:17:22
	 * @return
	 */
	private String validateInput(){
		String columnName = columnNameText.getText().trim();
		if(columnName.equals("")){ //$NON-NLS-1$
			return Messages.ColumnAddWizardPage_33;
		}else if(!StringUtil.isRightDBName(columnName)){
			return Messages.ColumnAddWizardPage_4;
		}
		if(wizard.getColumnMap().containsKey(columnName.toLowerCase())){
			return Messages.ColumnAddWizardPage_34+columnName+Messages.ColumnAddWizardPage_35;
		}
		if(columnTypeCombo.getText().trim().equals("")){ //$NON-NLS-1$
			return Messages.ColumnAddWizardPage_37;
		}
		if(ColumnType.hasLength(columnTypeCombo.getText().trim())){
			if(columnLengthText.getText().trim().equals("")){ //$NON-NLS-1$
				return Messages.ColumnAddWizardPage_39;
			}else {
				String val = columnLengthText.getText().trim();
				if(!val.matches("[\\d]{1,}") || Double.parseDouble(val) <= 0){ //$NON-NLS-1$
					return Messages.ColumnAddWizardPage_41;
				}
			}
		}
		if(ColumnType.hasScale(columnScaleText.getText().trim())){
			if(columnScaleText.getText().trim().equals("")){ //$NON-NLS-1$
				return Messages.ColumnAddWizardPage_43;
			}else {
				String val = columnScaleText.getText().trim();
				if(!val.matches("[\\d]{1,}") || Double.parseDouble(val) <= 0){ //$NON-NLS-1$
					return Messages.ColumnAddWizardPage_45;
				}
			}
		}
		
		if(columnCnNameText.getText().trim().equals("")){ //$NON-NLS-1$
			return Messages.ColumnAddWizardPage_47;
		}
		if(wizard.getColumnMap().get(columnName)!=null){
			return Messages.ColumnAddWizardPage_48+columnName+Messages.ColumnAddWizardPage_49;
		}
		return null;
	}
	/**
	 * ���ֶ�ѡ��Ի���
	 * @author leven
	 * 2012-11-9 ����04:37:30
	 */
	private void openColumnSelectDialog(){
		if(optionFlag==1){
			ColumnAddTemplateSelectDialog dialog = new ColumnAddTemplateSelectDialog(
					this.getShell(), schemaTemplate);
			dialog.create();
			if(dialog.open()==Dialog.OK){
				if((template=dialog.getData())!=null){
					selectedText.setText(template.getCode()==null?"":template.getCode()); //$NON-NLS-1$
					columnNameText.setText(template.getColumnName()==null?"":template.getColumnName()); //$NON-NLS-1$
					columnCnNameText.setText(template.getColumnCnName()==null?"":template.getColumnCnName()); //$NON-NLS-1$
					columnLengthText.setText(template.getColumnLength()+""); //$NON-NLS-1$
					columnScaleText.setText(template.getColumnScale()+""); //$NON-NLS-1$
					columnTypeCombo.setText(template.getColumnType().getType());
					
				}
			}
		}else {
			ColumnAddTableColumnSelectDialog dialog = new ColumnAddTableColumnSelectDialog(
					this.getShell(), schema);
			dialog.create();
			if(dialog.open()==Dialog.OK){
				Column c = dialog.getData();
				if(c!=null){
					selectedText.setText(c.getName());
					columnNameText.setText(c.getName());
					columnLengthText.setText(c.getLength()+""); //$NON-NLS-1$
					columnScaleText.setText(c.getScale()+""); //$NON-NLS-1$
					columnCnNameText.setText(c.getCnName());
					columnTypeCombo.setText(c.getType());
				}
			}
		}
		updateComplete();
	}
	/**
	 * ����ֶ���ģ�����
	 * @author leven
	 * 2012-11-9 ����04:37:09
	 */
	private void addColumnToTemplate(){
		String message;
		if((message=validateInput())==null){
			Column column = wizard.getColumn();
			ColumnTemplate temp = column.copyToTemplate();
			if(!schemaTemplate.addColumnTemplate(temp)){
				MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				mb.setText(Messages.ColumnAddWizardPage_57);
				mb.setMessage(Messages.ColumnAddWizardPage_58+columnNameText.getText().trim()+Messages.ColumnAddWizardPage_59);
				mb.open();
			}else {
				MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				mb.setText(Messages.ColumnAddWizardPage_60);
				mb.setMessage(Messages.ColumnAddWizardPage_61);
				mb.open();
				NavigatorViewUtil.refreshRootFolder(INavigatorNodeTypeConstants.FOLDER_INDEX_TEMP);
			}
		} else {
			MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
			mb.setText(Messages.ColumnAddWizardPage_57);
			mb.setMessage(message);
			mb.open();
		}
	}
	
	public Text getSelectedText() {
		return selectedText;
	}
	public void setSelectedText(Text selectedText) {
		this.selectedText = selectedText;
	}
	public Text getColumnNameText() {
		return columnNameText;
	}
	public void setColumnNameText(Text columnNameText) {
		this.columnNameText = columnNameText;
	}
	public Combo getColumnTypeCombo() {
		return columnTypeCombo;
	}
	public void setColumnTypeCombo(Combo columnTypeCombo) {
		this.columnTypeCombo = columnTypeCombo;
	}
	public Text getColumnLengthText() {
		return columnLengthText;
	}
	public void setColumnLengthText(Text columnLengthText) {
		this.columnLengthText = columnLengthText;
	}
	public Text getColumnCnNameText() {
		return columnCnNameText;
	}
	public void setColumnCnNameText(Text columnCnNameText) {
		this.columnCnNameText = columnCnNameText;
	}
	public SchemaTemplate getSchemaTemplate() {
		return schemaTemplate;
	}
	public void setSchemaTemplate(SchemaTemplate schemaTemplate) {
		this.schemaTemplate = schemaTemplate;
	}
	public Text getColumnScaleText() {
		return columnScaleText;
	}
	public void setColumnScaleText(Text columnScaleText) {
		this.columnScaleText = columnScaleText;
	}
	/**
	 * ѡ��ť����,0:�ֶ�����;1:��ģ�����ѡ;2:�ӱ��ֶ���ѡ
	 * @author leven
	 * 2012-11-2 ����09:13:19
	 * @return
	 */
	public int getOptionFlag() {
		return optionFlag;
	}
	public void setOptionFlag(int optionFlag) {
		this.optionFlag = optionFlag;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public ColumnTemplate getTemplate() {
		return template;
	}
	public void setTemplate(ColumnTemplate template) {
		this.template = template;
	}
	public Button getYesPkButton() {
		return yesPkButton;
	}
	public void setYesPkButton(Button yesPkButton) {
		this.yesPkButton = yesPkButton;
	}
	public Button getNoPkButton() {
		return noPkButton;
	}
	public void setNoPkButton(Button noPkButton) {
		this.noPkButton = noPkButton;
	}

	public Text getColumnDespText() {
		return columnDespText;
	}

	public void setColumnDespText(Text columnDespText) {
		this.columnDespText = columnDespText;
	}
}