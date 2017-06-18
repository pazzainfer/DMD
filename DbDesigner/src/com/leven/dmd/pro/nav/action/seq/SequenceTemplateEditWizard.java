package com.leven.dmd.pro.nav.action.seq;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.constant.INavigatorNodeTypeConstants;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class SequenceTemplateEditWizard extends Wizard {
	private SequenceTemplateEditWizardPage page;
	private SequenceTemplate sequenceTemplate;
	
	public SequenceTemplateEditWizard(SequenceTemplate obj) {
		super();
		this.setWindowTitle(Messages.SequenceTemplateEditWizard_0);
		this.sequenceTemplate = obj;
	}
	
	@Override
	public void addPages() {
		page = new SequenceTemplateEditWizardPage(sequenceTemplate);
		addPage(page);
	}


	@Override
	public boolean performFinish() {
		if(!page.isPageComplete()){
			return false;
		}
		sequenceTemplate.setMinvalue(page.getMinvalueText().getText().trim());
		sequenceTemplate.setMaxvalue(page.getMaxvalueText().getText().trim());
		sequenceTemplate.setStartwith(page.getStartwithText().getText().trim());
		sequenceTemplate.setIncrementby(page.getIncrementbyText().getText().trim());
		sequenceTemplate.setCache(page.getCacheText().getText().trim());
		sequenceTemplate.setCycle(page.getYesCycleBtn().getSelection());
		sequenceTemplate.setDescription(page.getDescriptionText().getText().trim());
		
		NavigatorViewUtil.refresh(sequenceTemplate);
		NavigatorViewUtil.setEditorDirty(true);
		return true;
	}

}
