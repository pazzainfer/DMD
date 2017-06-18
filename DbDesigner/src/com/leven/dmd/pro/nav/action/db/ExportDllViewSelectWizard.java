package com.leven.dmd.pro.nav.action.db;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.pro.Messages;

public class ExportDllViewSelectWizard extends Wizard {
	
	private ExportDllViewSelectWizardPage exportDllViewWizardPage;
	private Schema schema;
	private List<DBView> spaceList;
	private Map<String, DBView> selectedMap;
	

	public ExportDllViewSelectWizard(Schema schema, List<DBView> spaceList,
			Map<String, DBView> selectedMap) {
		super();
		this.schema = schema;
		this.spaceList = spaceList;
		this.selectedMap = selectedMap;
		this.setWindowTitle(Messages.ExportDllViewSelectWizard_0);
	}

	@Override
	public void addPages() {
		exportDllViewWizardPage = new ExportDllViewSelectWizardPage(schema,
				spaceList,selectedMap);
		addPage(exportDllViewWizardPage);
	}

	@Override
	public boolean performFinish() {
		spaceList = exportDllViewWizardPage.getSelectedList();
		selectedMap = exportDllViewWizardPage.getSelectedMap();
		return true;
	}

}
