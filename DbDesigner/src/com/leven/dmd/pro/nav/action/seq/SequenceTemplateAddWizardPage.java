package com.leven.dmd.pro.nav.action.seq;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Messages;

public class SequenceTemplateAddWizardPage extends WizardPage {
	private Text codeText;
	private Text minvalueText;
	private Text maxvalueText;
	private Text startwithText;
	private Text incrementbyText;
	private Text cacheText;
	private Button yesCycleBtn;
	private Button noCycleBtn;
	private Text descriptionText;
	
	private Button selectTypeTemplate;
	private Text selectTypeText;
	private Button selectTypeButton;
	private SchemaTemplate schemaTemplate;


	protected SequenceTemplateAddWizardPage(SchemaTemplate schemaTemplate) {
		super("ColumnTypeTemplateEditWizardPage"); //$NON-NLS-1$
		this.schemaTemplate = schemaTemplate;
		this.setTitle(Messages.SequenceTemplateAddWizardPage_1);
		this.setMessage(Messages.SequenceTemplateAddWizardPage_2,2);
		this.setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		
		GridData labData = new GridData(60,14);
		GridData textData = new GridData(GridData.FILL_HORIZONTAL);
		textData.heightHint = 14;
		
		Label codeLab = new Label(group,SWT.NONE);
		codeLab.setText(Messages.SequenceTemplateAddWizardPage_3);
		codeLab.setLayoutData(labData);
		
		codeText = new Text(group,SWT.BORDER);
		codeText.setLayoutData(textData);
		codeText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label minvalueLabel = new Label(group,SWT.NONE);
		minvalueLabel.setText(Messages.SequenceTemplateAddWizardPage_4);
		minvalueLabel.setLayoutData(labData);
		
		minvalueText = new Text(group,SWT.BORDER);
		minvalueText.setLayoutData(textData);
		minvalueText.setText("NOMINVALUE"); //$NON-NLS-1$
		minvalueText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(minvalueText.getText().trim().equals("")){ //$NON-NLS-1$
					minvalueText.setText("NOMINVALUE"); //$NON-NLS-1$
				}
				updateComplete();
			}
		});
		
		Label maxvalueLabel = new Label(group,SWT.NONE);
		maxvalueLabel.setText(Messages.SequenceTemplateAddWizardPage_8);
		maxvalueLabel.setLayoutData(labData);
		
		maxvalueText = new Text(group,SWT.BORDER);
		maxvalueText.setLayoutData(textData);
		maxvalueText.setText("NOMAXVALUE"); //$NON-NLS-1$
		maxvalueText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(maxvalueText.getText().trim().equals("")){ //$NON-NLS-1$
					maxvalueText.setText("NOMAXVALUE"); //$NON-NLS-1$
				}
				updateComplete();
			}
		});

		Label startwithLabel = new Label(group,SWT.NONE);
		startwithLabel.setText(Messages.SequenceTemplateAddWizardPage_12);
		startwithLabel.setLayoutData(labData);
		
		startwithText = new Text(group,SWT.BORDER);
		startwithText.setLayoutData(textData);
		startwithText.setText("1"); //$NON-NLS-1$
		startwithText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label incrementbyLabel = new Label(group,SWT.NONE);
		incrementbyLabel.setText(Messages.SequenceTemplateAddWizardPage_14);
		incrementbyLabel.setLayoutData(labData);
		
		incrementbyText = new Text(group,SWT.BORDER);
		incrementbyText.setLayoutData(textData);
		incrementbyText.setText("1"); //$NON-NLS-1$
		incrementbyText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label cacheLabel = new Label(group,SWT.NONE);
		cacheLabel.setText(Messages.SequenceTemplateAddWizardPage_16);
		cacheLabel.setLayoutData(labData);
		
		cacheText = new Text(group,SWT.BORDER);
		cacheText.setLayoutData(textData);
		cacheText.setText("10"); //$NON-NLS-1$
		cacheText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(cacheText.getText().trim().equals("")){ //$NON-NLS-1$
					cacheText.setText("NOCACHE"); //$NON-NLS-1$
				}
				updateComplete();
			}
		});
		
		Label cycleLabel = new Label(group,SWT.NONE);
		cycleLabel.setText(Messages.SequenceTemplateAddWizardPage_20);
		cycleLabel.setLayoutData(labData);
		Composite cycleComp = new Composite(group,SWT.NONE);
		cycleComp.setLayoutData(textData);
		cycleComp.setLayout(new RowLayout());
		yesCycleBtn = new Button(cycleComp, SWT.RADIO);
		yesCycleBtn.setText(Messages.SequenceTemplateAddWizardPage_21);
		yesCycleBtn.setSelection(false);
		noCycleBtn = new Button(cycleComp, SWT.RADIO);
		noCycleBtn.setText(Messages.SequenceTemplateAddWizardPage_22);
		noCycleBtn.setSelection(true);
		
		
		Label descriptionLabel = new Label(group,SWT.NONE);
		descriptionLabel.setText(Messages.SequenceTemplateAddWizardPage_23);
		GridData labData1 = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		labData1.widthHint = 60;
		descriptionLabel.setLayoutData(labData1);
		
		descriptionText = new Text(group,SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		setControl(group);
	}

	private void updateComplete(){
		String code = codeText.getText();
		String minvalue = minvalueText.getText();
		String maxvalue = maxvalueText.getText();
		String startwith = startwithText.getText();
		String incrementby = incrementbyText.getText();
		String cache = cacheText.getText();

		
		if(schemaTemplate.getSequenceTemplatesMap().containsKey(code)){
			this.setMessage(Messages.SequenceTemplateAddWizardPage_24, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		
		if(StringUtil.isEmpty(code)){
			this.setMessage(Messages.SequenceTemplateAddWizardPage_25, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(!StringUtil.isNumber(minvalue) && !minvalue.equals("NOMINVALUE")){ //$NON-NLS-1$
			this.setMessage(Messages.SequenceTemplateAddWizardPage_27, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(!StringUtil.isNumber(maxvalue) && !maxvalue.equals("NOMAXVALUE")){ //$NON-NLS-1$
			this.setMessage(Messages.SequenceTemplateAddWizardPage_29, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(StringUtil.isEmpty(startwith)){
			this.setMessage(Messages.SequenceTemplateAddWizardPage_30, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}else if(!StringUtil.isNumber(startwith)){
			this.setMessage(Messages.SequenceTemplateAddWizardPage_31, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(StringUtil.isEmpty(incrementby)){
			this.setMessage(Messages.SequenceTemplateAddWizardPage_32, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}else if(!StringUtil.isNumber(incrementby)){
			this.setMessage(Messages.SequenceTemplateAddWizardPage_33, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(!StringUtil.isNumber(cache) && !cache.equals("NOCACHE")){ //$NON-NLS-1$
			this.setMessage(Messages.SequenceTemplateAddWizardPage_35, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		
		this.setMessage(Messages.SequenceTemplateAddWizardPage_36,DialogPage.INFORMATION);
		this.setPageComplete(true);
	}
	

	public Text getDescriptionText() {
		return descriptionText;
	}

	public void setDescriptionText(Text descriptionText) {
		this.descriptionText = descriptionText;
	}

	public Button getSelectTypeTemplate() {
		return selectTypeTemplate;
	}

	public void setSelectTypeTemplate(Button selectTypeTemplate) {
		this.selectTypeTemplate = selectTypeTemplate;
	}

	public Text getSelectTypeText() {
		return selectTypeText;
	}

	public void setSelectTypeText(Text selectTypeText) {
		this.selectTypeText = selectTypeText;
	}

	public Button getSelectTypeButton() {
		return selectTypeButton;
	}

	public void setSelectTypeButton(Button selectTypeButton) {
		this.selectTypeButton = selectTypeButton;
	}

	public Text getCodeText() {
		return codeText;
	}

	public void setCodeText(Text codeText) {
		this.codeText = codeText;
	}
	public Text getMinvalueText() {
		return minvalueText;
	}

	public void setMinvalueText(Text minvalueText) {
		this.minvalueText = minvalueText;
	}

	public Text getMaxvalueText() {
		return maxvalueText;
	}

	public void setMaxvalueText(Text maxvalueText) {
		this.maxvalueText = maxvalueText;
	}

	public Text getStartwithText() {
		return startwithText;
	}

	public void setStartwithText(Text startwithText) {
		this.startwithText = startwithText;
	}

	public Text getIncrementbyText() {
		return incrementbyText;
	}

	public void setIncrementbyText(Text incrementbyText) {
		this.incrementbyText = incrementbyText;
	}

	public Text getCacheText() {
		return cacheText;
	}

	public void setCacheText(Text cacheText) {
		this.cacheText = cacheText;
	}
	public Button getYesCycleBtn() {
		return yesCycleBtn;
	}
	public void setYesCycleBtn(Button yesCycleBtn) {
		this.yesCycleBtn = yesCycleBtn;
	}
	public Button getNoCycleBtn() {
		return noCycleBtn;
	}
	public void setNoCycleBtn(Button noCycleBtn) {
		this.noCycleBtn = noCycleBtn;
	}
}
