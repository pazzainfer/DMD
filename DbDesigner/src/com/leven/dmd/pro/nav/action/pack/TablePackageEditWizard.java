package com.leven.dmd.pro.nav.action.pack;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.Messages;

public class TablePackageEditWizard extends Wizard {
	private TablePackageEditWizardPage page;
	private TablePackage tablePackage;
	private List<TablePackage> list;

	public TablePackageEditWizard(TablePackage tablePackage, List<TablePackage> list) {
		super();
		this.list = list;
		this.tablePackage = tablePackage;
		this.setNeedsProgressMonitor(false);
		this.setWindowTitle(Messages.TablePackageEditWizard_0);
	}

	public void addPages() {
		page = new TablePackageEditWizardPage(this);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		TablePackage tablePackage1 = new TablePackage(page.getNameTxt().getText().trim());
		tablePackage1.setDescription(page.getDescriptionTxt().getText().trim());
		setTablePackage(tablePackage1);
		return true;
	}

	public TablePackageEditWizardPage getPage() {
		return page;
	}
	public void setPage(TablePackageEditWizardPage page) {
		this.page = page;
	}
	public TablePackage getTablePackage() {
		return tablePackage;
	}
	public void setTablePackage(TablePackage tablePackage) {
		this.tablePackage = tablePackage;
	}
	public List<TablePackage> getList() {
		return list;
	}
	public void setList(List<TablePackage> list) {
		this.list = list;
	}
}
