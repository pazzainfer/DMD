package com.leven.dmd.pro.nav.action.db;

import java.util.List;
import java.util.Map;
import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.pro.Messages;

public class ExportDllTableSelectWizard extends Wizard {
	
	private ExportDllTableSelectWizardPage exportDllTableSelectWizardPage;
	private Schema schema;
	private List<Table> sequenceList;
	private Map<String, Table> selectedMap;
	

	public ExportDllTableSelectWizard(Schema schema, List<Table> sequenceList,
			Map<String, Table> selectedMap) {
		super();
		this.schema = schema;
		this.sequenceList = sequenceList;
		this.selectedMap = selectedMap;
		this.setWindowTitle(Messages.ExportDllTableSelectWizard_0);
	}

	@Override
	public void addPages() {
		exportDllTableSelectWizardPage = new ExportDllTableSelectWizardPage(schema,
				sequenceList,selectedMap);
		addPage(exportDllTableSelectWizardPage);
	}

	@Override
	public boolean performFinish() {
		sequenceList = exportDllTableSelectWizardPage.getSelectedList();
		selectedMap = exportDllTableSelectWizardPage.getSelectedMap();
		return true;
	}
}
