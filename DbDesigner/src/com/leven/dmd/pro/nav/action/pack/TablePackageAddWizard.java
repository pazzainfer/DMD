package com.leven.dmd.pro.nav.action.pack;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.Messages;

public class TablePackageAddWizard extends Wizard {
	private TablePackageAddWizardPage page;
	private TablePackage tablePackage;
	private List<TablePackage> list;

	public TablePackageAddWizard(List<TablePackage> list) {
		super();
		this.list = list;
		this.setNeedsProgressMonitor(false);
		this.setWindowTitle(Messages.TablePackageAddWizard_0);
	}

	public void addPages() {
		page = new TablePackageAddWizardPage(this);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		TablePackage tablePackage = new TablePackage(page.getNameTxt().getText());
		tablePackage.setDescription(page.getDescriptionTxt().getText().trim());
		setTablePackage(tablePackage);
		return true;
	}

	public TablePackageAddWizardPage getPage() {
		return page;
	}
	public void setPage(TablePackageAddWizardPage page) {
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
