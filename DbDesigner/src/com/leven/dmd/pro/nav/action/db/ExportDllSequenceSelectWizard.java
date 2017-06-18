package com.leven.dmd.pro.nav.action.db;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.pro.Messages;

public class ExportDllSequenceSelectWizard extends Wizard {
	
	private ExportDllSequenceSelectWizardPage exportDllSequenceWizardPage;
	private Schema schema;
	private List<SequenceTemplate> sequenceList;
	private Map<String, SequenceTemplate> selectedMap;
	

	public ExportDllSequenceSelectWizard(Schema schema, List<SequenceTemplate> sequenceList,
			Map<String, SequenceTemplate> selectedMap) {
		super();
		this.schema = schema;
		this.sequenceList = sequenceList;
		this.selectedMap = selectedMap;
		this.setWindowTitle(Messages.ExportDllSequenceSelectWizard_0);
	}

	@Override
	public void addPages() {
		exportDllSequenceWizardPage = new ExportDllSequenceSelectWizardPage(schema.getSchemaTemplate(),
				sequenceList,selectedMap);
		addPage(exportDllSequenceWizardPage);
	}

	@Override
	public boolean performFinish() {
		sequenceList = exportDllSequenceWizardPage.getSelectedList();
		selectedMap = exportDllSequenceWizardPage.getSelectedMap();
		return true;
	}

}
