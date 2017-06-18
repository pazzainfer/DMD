package com.leven.dmd.pro.nav.action.temp;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.ColumnType;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;
import com.leven.dmd.gef.tmpfile.model.SchemaTemplate;
import com.leven.dmd.pro.Messages;

public class ColumnTemplateAddWizard extends Wizard {
	private ColumnTemplateAddWizardPage page;
	private SchemaTemplate schemaTemplate;

	public ColumnTemplateAddWizard(SchemaTemplate schemaTemplate) {
		super();
		this.schemaTemplate = schemaTemplate;
		this.setWindowTitle(Messages.ColumnTemplateAddWizard_0);
	}

	
	@Override
	public void addPages() {
		page = new ColumnTemplateAddWizardPage(schemaTemplate);
		addPage(page);
	}


	@Override
	public boolean performFinish() {
		if(!page.isPageComplete()){
			return false;
		}
		ColumnTemplate columnTemplate = new ColumnTemplate();
		
		String columnname = page.getColumnNameText().getText().trim();
		String columnchinesename = page.getColumnChineseName().getText().trim();
		String columntype = page.getTypeCombo().getItem(page.getTypeCombo().getSelectionIndex());
		ColumnType columnType = ColumnType.getColumnType(columntype);
		String description = page.getDescriptionText().getText().trim();
		String length = page.getColumnLengthText().getEnabled()?page.getColumnLengthText().getText().trim():"0"; //$NON-NLS-1$
		String scale = page.getColumnScaleText().getEnabled()?page.getColumnScaleText().getText().trim():"0"; //$NON-NLS-1$
		
		columnTemplate.setCode(System.currentTimeMillis()+""); //$NON-NLS-1$
		columnTemplate.setColumnName(columnname);
		columnTemplate.setColumnCnName(columnchinesename);
		columnTemplate.setColumnType(columnType);
		columnTemplate.setColumnLength(Integer.parseInt(length));
		columnTemplate.setColumnScale(Integer.parseInt(scale));
		columnTemplate.setDescription(description);
		if(!schemaTemplate.addColumnTemplate(columnTemplate)){
			page.setMessage(Messages.ColumnTemplateAddWizard_2, 3);
			page.setPageComplete(false);
			return false;
		}
		
		return true;
	}
	
	public SchemaTemplate getSchemaTemplate() {
		return schemaTemplate;
	}
	public void setSchemaTemplate(SchemaTemplate schemaTemplate) {
		this.schemaTemplate = schemaTemplate;
	}
}
