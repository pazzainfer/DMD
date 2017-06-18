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

import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Messages;

public class SequenceTemplateEditWizardPage extends WizardPage {
	private SequenceTemplate sequenceTemplate;
	private Text codeText;
	private Text nameText;
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
	
	protected SequenceTemplateEditWizardPage() {
		super("ColumnTypeTemplateEditWizardPage"); //$NON-NLS-1$
	}
	protected SequenceTemplateEditWizardPage(SequenceTemplate obj) {
		super("SequenceTemplateEditWizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.SequenceTemplateEditWizardPage_2);
		this.setMessage(Messages.SequenceTemplateEditWizardPage_3,2);
		this.sequenceTemplate = obj;
		this.setPageComplete(true);
	}
	public void createControl(Composite parent) {
		Group group = new Group(parent,SWT.NONE);
		GridLayout layout = new GridLayout(2,false);
		layout.verticalSpacing = 5;
		group.setLayout(layout);
		
		GridData labData = new GridData(60,14);
		GridData textData = new GridData(GridData.FILL_HORIZONTAL);
		
		Label codeLab = new Label(group,SWT.NONE);
		codeLab.setText(Messages.SequenceTemplateEditWizardPage_4);
		codeLab.setLayoutData(labData);
		
		codeText = new Text(group,SWT.BORDER);
		codeText.setLayoutData(textData);
		codeText.setEnabled(false);
		codeText.setText(this.sequenceTemplate.getCode());
		codeText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label minvalueLabel = new Label(group,SWT.NONE);
		minvalueLabel.setText(Messages.SequenceTemplateEditWizardPage_5);
		minvalueLabel.setLayoutData(labData);
		
		minvalueText = new Text(group,SWT.BORDER);
		minvalueText.setLayoutData(textData);
		minvalueText.setText(this.sequenceTemplate.getMinvalue());
		minvalueText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(minvalueText.getText().trim().equals("")){ //$NON-NLS-1$
					minvalueText.setText("NOMINVALUE"); //$NON-NLS-1$
				}
				updateComplete();
			}
		});
		
		Label maxvalueLabel = new Label(group,SWT.NONE);
		maxvalueLabel.setText(Messages.SequenceTemplateEditWizardPage_8);
		maxvalueLabel.setLayoutData(labData);
		
		maxvalueText = new Text(group,SWT.BORDER);
		maxvalueText.setLayoutData(textData);
		maxvalueText.setText(this.sequenceTemplate.getMaxvalue());
		maxvalueText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(maxvalueText.getText().trim().equals("")){ //$NON-NLS-1$
					maxvalueText.setText("NOMAXVALUE"); //$NON-NLS-1$
				}
				updateComplete();
			}
		});

		Label startwithLabel = new Label(group,SWT.NONE);
		startwithLabel.setText(Messages.SequenceTemplateEditWizardPage_11);
		startwithLabel.setLayoutData(labData);
		
		startwithText = new Text(group,SWT.BORDER);
		startwithText.setLayoutData(textData);
		startwithText.setText(this.sequenceTemplate.getStartwith());
		startwithText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label incrementbyLabel = new Label(group,SWT.NONE);
		incrementbyLabel.setText(Messages.SequenceTemplateEditWizardPage_12);
		incrementbyLabel.setLayoutData(labData);
		
		incrementbyText = new Text(group,SWT.BORDER);
		incrementbyText.setLayoutData(textData);
		incrementbyText.setText(this.sequenceTemplate.getIncrementby());
		incrementbyText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		Label cacheLabel = new Label(group,SWT.NONE);
		cacheLabel.setText(Messages.SequenceTemplateEditWizardPage_13);
		cacheLabel.setLayoutData(labData);
		
		cacheText = new Text(group,SWT.BORDER);
		cacheText.setLayoutData(textData);
		cacheText.setText(this.sequenceTemplate.getCache());
		cacheText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(cacheText.getText().trim().equals("")){ //$NON-NLS-1$
					cacheText.setText("NOCACHE"); //$NON-NLS-1$
				}
				updateComplete();
			}
		});
		
		Label cycleLabel = new Label(group,SWT.NONE);
		cycleLabel.setText(Messages.SequenceTemplateEditWizardPage_16);
		cycleLabel.setLayoutData(labData);
		Composite cycleComp = new Composite(group,SWT.NONE);
		cycleComp.setLayoutData(textData);
		cycleComp.setLayout(new RowLayout());
		yesCycleBtn = new Button(cycleComp, SWT.RADIO);
		yesCycleBtn.setText(Messages.SequenceTemplateEditWizardPage_17);
		yesCycleBtn.setSelection(sequenceTemplate.isCycle());
		noCycleBtn = new Button(cycleComp, SWT.RADIO);
		noCycleBtn.setText(Messages.SequenceTemplateEditWizardPage_18);
		noCycleBtn.setSelection(!sequenceTemplate.isCycle());
		
		Label descriptionLabel = new Label(group,SWT.NONE);
		descriptionLabel.setText(Messages.SequenceTemplateEditWizardPage_19);
		GridData labData1 = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		labData1.widthHint = 60;
		descriptionLabel.setLayoutData(labData1);
		
		descriptionText = new Text(group,SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionText.setText(this.sequenceTemplate.getDescription());
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

		if(StringUtil.isEmpty(code)){
			this.setMessage(Messages.SequenceTemplateEditWizardPage_20, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		
		if(!StringUtil.isNumber(minvalue) && !minvalue.equals("NOMINVALUE")){ //$NON-NLS-1$
			this.setMessage(Messages.SequenceTemplateEditWizardPage_22, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(!StringUtil.isNumber(maxvalue) && !minvalue.equals("NOMAXVALUE")){ //$NON-NLS-1$
			this.setMessage(Messages.SequenceTemplateEditWizardPage_24, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(StringUtil.isEmpty(startwith)){
			this.setMessage(Messages.SequenceTemplateEditWizardPage_25, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}else if(!StringUtil.isNumber(startwith)){
			this.setMessage(Messages.SequenceTemplateEditWizardPage_26, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		if(StringUtil.isEmpty(incrementby)){
			this.setMessage(Messages.SequenceTemplateEditWizardPage_27, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}else if(!StringUtil.isNumber(incrementby)){
			this.setMessage(Messages.SequenceTemplateEditWizardPage_28, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}

		if(!StringUtil.isNumber(cache) && !cache.equals("NOCACHE")){ //$NON-NLS-1$
			this.setMessage(Messages.SequenceTemplateEditWizardPage_30, DialogPage.ERROR);
			this.setPageComplete(false);
			return;
		}
		
		this.setMessage(Messages.SequenceTemplateEditWizardPage_31,DialogPage.INFORMATION);
		this.setPageComplete(true);
	}
	public SequenceTemplate getSequenceTemplate() {
		return sequenceTemplate;
	}
	public void setSequenceTemplate(SequenceTemplate sequenceTemplate) {
		this.sequenceTemplate = sequenceTemplate;
	}
	public Text getCodeText() {
		return codeText;
	}
	public void setCodeText(Text codeText) {
		this.codeText = codeText;
	}
	public Text getNameText() {
		return nameText;
	}
	public void setNameText(Text nameText) {
		this.nameText = nameText;
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
