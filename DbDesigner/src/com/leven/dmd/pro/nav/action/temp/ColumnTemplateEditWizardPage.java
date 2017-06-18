package com.leven.dmd.pro.nav.action.temp;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Messages;

public class ColumnTemplateEditWizardPage extends WizardPage {
	private ColumnTemplate columnTemplate;
	private Text columnNameText;
	private Text columnChineseName;
	private Combo typeCombo;
	private Text columnLengthText;
	private Text columnScaleText;
	private Text descriptionText;
	
	protected ColumnTemplateEditWizardPage(ColumnTemplate obj) {
		super(Messages.ColumnTemplateEditWizardPage_0);
		this.setTitle(Messages.ColumnTemplateEditWizardPage_1);
		this.setMessage(Messages.ColumnTemplateEditWizardPage_2,2);
		this.columnTemplate = obj;
		this.setPageComplete(true);
	}

	public void createControl(Composite parent) {
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		
		GridData labData = new GridData(60,14);
		GridData textData = new GridData(GridData.FILL_HORIZONTAL);
		textData.heightHint = 14;
		
		Label columnNameLab = new Label(group,SWT.NONE);
		columnNameLab.setText(Messages.ColumnTemplateEditWizardPage_3);
		columnNameLab.setLayoutData(labData);
		
		columnNameText = new Text(group,SWT.BORDER);
		columnNameText.setLayoutData(textData);
		columnNameText.setEnabled(false);
		columnNameText.setText(columnTemplate.getColumnName()==null?"":columnTemplate.getColumnName());
		columnNameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		
		Label columnChineseNameLab = new Label(group,SWT.NONE);
		columnChineseNameLab.setText(Messages.ColumnTemplateEditWizardPage_5);
		columnChineseNameLab.setLayoutData(labData);
		
		columnChineseName = new Text(group,SWT.BORDER);
		columnChineseName.setLayoutData(textData);
		columnChineseName.setText(columnTemplate.getColumnCnName()==null?"":columnTemplate.getColumnCnName());
		columnChineseName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label typeLab = new Label(group,SWT.NONE);
		typeLab.setText(Messages.ColumnTemplateEditWizardPage_7);
		typeLab.setLayoutData(labData);
		typeCombo = new Combo(group, SWT.READ_ONLY);
		typeCombo.setItems(ColumnType.typeArray);
		typeCombo.setLayoutData(textData);
		typeCombo.setText(columnTemplate.getColumnType().getType());
		typeCombo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String type = typeCombo.getText();
				if(ColumnType.hasLength(type)){
					columnLengthText.setEnabled(true);
				}else {
					columnLengthText.setEnabled(false);
					columnLengthText.setText("");
				}
				if(ColumnType.hasScale(type)){
					columnScaleText.setEnabled(true);
				}else {
					columnScaleText.setEnabled(false);
					columnScaleText.setText("");
				}
				updateComplete();
			}
		});
		
		
		Label lengthLab = new Label(group,SWT.NONE);
		lengthLab.setText(Messages.ColumnTemplateEditWizardPage_10);
		lengthLab.setLayoutData(labData);
		
		columnLengthText = new Text(group,SWT.BORDER);
		columnLengthText.setLayoutData(textData);
		columnLengthText.setText("");
		columnLengthText.setText(String.valueOf(columnTemplate.getColumnLength()));
		if(!ColumnType.hasLength(columnTemplate.getColumnType().getType())){
			columnLengthText.setEnabled(false);
		}else {
			columnLengthText.setEnabled(true);
		}
		columnLengthText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label scaleLab = new Label(group,SWT.NONE);
		scaleLab.setText(Messages.ColumnTemplateEditWizardPage_12);
		scaleLab.setLayoutData(labData);
		
		columnScaleText = new Text(group,SWT.BORDER);
		columnScaleText.setLayoutData(textData);
		columnScaleText.setText(String.valueOf(columnTemplate.getColumnScale()));
		columnScaleText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
			columnScaleText.setText("");
			columnScaleText.setText(String.valueOf(columnTemplate.getColumnScale()));
			if(!ColumnType.hasScale(columnTemplate.getColumnType().getType())){
				columnScaleText.setEnabled(false);
			}else{
				columnScaleText.setEnabled(true);
			}
		
		Label descriptionLabel = new Label(group,SWT.NONE);
		descriptionLabel.setText(Messages.ColumnTemplateEditWizardPage_14);
		GridData labData1 = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		labData1.widthHint = 60;
		descriptionLabel.setLayoutData(labData1);
		
		descriptionText = new Text(group,SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionText.setText(columnTemplate.getDescription()==null?"":columnTemplate.getDescription());
		descriptionText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		setControl(group);
	}

	private void updateComplete(){
		String codeName = columnNameText.getText();
		String codeChineseName = columnChineseName.getText();
		String length = columnLengthText.getText();
		String scale = columnScaleText.getText();
		int typeSelectedIndex = typeCombo.getSelectionIndex();
		if(typeSelectedIndex==-1){
			this.setMessage(Messages.ColumnTemplateEditWizardPage_15, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(StringUtil.isEmpty(codeName)){
			this.setMessage(Messages.ColumnTemplateEditWizardPage_16, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		
		if(StringUtil.isEmpty(codeChineseName)){
			this.setMessage(Messages.ColumnTemplateEditWizardPage_17, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(ColumnType.hasLength(typeCombo.getText())){
			if(StringUtil.isEmpty(length)){
				this.setMessage(Messages.ColumnTemplateEditWizardPage_18, DialogPage.ERROR);
				this.setPageComplete(false);
				return;
			}else if(!StringUtil.isNumber(length)){
				this.setMessage(Messages.ColumnTemplateEditWizardPage_19, DialogPage.ERROR);
				this.setPageComplete(false);
				return;
			}
		}
		if(ColumnType.hasScale(typeCombo.getText())){
			if(StringUtil.isEmpty(scale)){
				this.setMessage(Messages.ColumnTemplateEditWizardPage_20, DialogPage.ERROR);
				this.setPageComplete(false);
				return;
			}else if(!StringUtil.isNumber(scale)){
				this.setMessage(Messages.ColumnTemplateEditWizardPage_21, DialogPage.ERROR);
				this.setPageComplete(false);
				return;
			}
		}
		
		this.setMessage(Messages.ColumnTemplateEditWizardPage_22,DialogPage.INFORMATION);
		this.setPageComplete(true);
	}

	public Text getColumnNameText() {
		return columnNameText;
	}

	public void setColumnNameText(Text columnNameText) {
		this.columnNameText = columnNameText;
	}


	public Text getColumnChineseName() {
		return columnChineseName;
	}

	public void setColumnChineseName(Text columnChineseName) {
		this.columnChineseName = columnChineseName;
	}

	public Combo getTypeCombo() {
		return typeCombo;
	}

	public void setTypeCombo(Combo typeCombo) {
		this.typeCombo = typeCombo;
	}


	public Text getDescriptionText() {
		return descriptionText;
	}

	public void setDescriptionText(Text descriptionText) {
		this.descriptionText = descriptionText;
	}

	public Text getColumnLengthText() {
		return columnLengthText;
	}

	public void setColumnLengthText(Text columnLengthText) {
		this.columnLengthText = columnLengthText;
	}

	public Text getColumnScaleText() {
		return columnScaleText;
	}

	public void setColumnScaleText(Text columnScaleText) {
		this.columnScaleText = columnScaleText;
	}

}
