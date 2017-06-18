package com.leven.dmd.pro.nav.action.temp;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.nav.util.NavigatorViewUtil;

public class ColumnTemplateEditWizard extends Wizard {
	private ColumnTemplateEditWizardPage page;
	private ColumnTemplate columnTemplate;

	public ColumnTemplateEditWizard(ColumnTemplate columnTemplate) {
		super();
		this.columnTemplate = columnTemplate;
		this.setWindowTitle(Messages.ColumnTemplateEditWizard_0);
	}
	public ColumnTemplateEditWizard() {
		super();
	}

	
	@Override
	public void addPages() {
		page = new ColumnTemplateEditWizardPage(columnTemplate);
		addPage(page);
	}


	@Override
	public boolean performFinish() {
		if(!page.isPageComplete()){
			return false;
		}
		
		String columntype = page.getTypeCombo().getText();
		ColumnType columnType = ColumnType.getColumnType(columntype);
		String description = page.getDescriptionText().getText().trim();
		String cnName = page.getColumnChineseName().getText().trim();
		String length = page.getColumnLengthText().getEnabled()?page.getColumnLengthText().getText().trim():"0"; //$NON-NLS-1$
		String scale = page.getColumnScaleText().getEnabled()?page.getColumnScaleText().getText().trim():"0"; //$NON-NLS-1$
		
		columnTemplate.setColumnType(columnType);
		columnTemplate.setColumnLength(Integer.parseInt(length));
		columnTemplate.setColumnScale(Integer.parseInt(scale));
		columnTemplate.setColumnCnName(cnName);
		columnTemplate.setDescription(description);
		
		NavigatorViewUtil.refresh(columnTemplate);
		NavigatorViewUtil.setEditorDirty(true);
		return true;
	}
}
