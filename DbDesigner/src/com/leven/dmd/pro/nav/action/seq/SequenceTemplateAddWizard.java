package com.leven.dmd.pro.nav.action.seq;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.pro.Messages;

public class SequenceTemplateAddWizard extends Wizard {
	private SequenceTemplateAddWizardPage page;
	private SchemaTemplate schemaTemplate;

	public SequenceTemplateAddWizard(SchemaTemplate schemaTemplate) {
		super();
		this.schemaTemplate = schemaTemplate;
		this.setWindowTitle(Messages.SequenceTemplateAddWizard_0);
	}

	
	@Override
	public void addPages() {
		page = new SequenceTemplateAddWizardPage(schemaTemplate);
		addPage(page);
	}


	@Override
	public boolean performFinish() {
		if(!page.isPageComplete()){
			return false;
		}
		SequenceTemplate sequenceTemplate = new SequenceTemplate();
		
		String code = page.getCodeText().getText().trim();
		String description = page.getDescriptionText().getText().trim();
		String maxvalue = page.getMaxvalueText().getText().trim();
		String minvalue = page.getMinvalueText().getText().trim();
		String startwith = page.getStartwithText().getText().trim();
		String incrementby = page.getIncrementbyText().getText().trim();
		String cache = page.getCacheText().getText().trim();
		
		sequenceTemplate.setCode(code);
		sequenceTemplate.setDescription(description);
		sequenceTemplate.setMinvalue(minvalue);
		sequenceTemplate.setMaxvalue(maxvalue);
		sequenceTemplate.setStartwith(startwith);
		sequenceTemplate.setIncrementby(incrementby);
		sequenceTemplate.setCycle(page.getYesCycleBtn().getSelection());
		sequenceTemplate.setCache(cache);
		
		schemaTemplate.addSequenceTemplate(sequenceTemplate);
		return true;
	}

}
