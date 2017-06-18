package com.leven.dmd.pro.nav.action.db;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.pro.Messages;

public class ExportDllTablespaceSelectWizard extends Wizard {
	
	private ExportDllTablespaceSelectWizardPage exportDllTablespaceWizardPage;
	private Schema schema;
	private List<Tablespace> spaceList;
	private Map<String, Tablespace> selectedMap;
	

	public ExportDllTablespaceSelectWizard(Schema schema, List<Tablespace> spaceList,
			Map<String, Tablespace> selectedMap) {
		super();
		this.schema = schema;
		this.spaceList = spaceList;
		this.selectedMap = selectedMap;
		this.setWindowTitle(Messages.ExportDllTablespaceSelectWizard_0);
	}

	@Override
	public void addPages() {
		exportDllTablespaceWizardPage = new ExportDllTablespaceSelectWizardPage(schema,
				spaceList,selectedMap);
		addPage(exportDllTablespaceWizardPage);
	}

	@Override
	public boolean performFinish() {
		spaceList = exportDllTablespaceWizardPage.getSelectedList();
		selectedMap = exportDllTablespaceWizardPage.getSelectedMap();
		return true;
	}

}
