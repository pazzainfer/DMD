package com.leven.dmd.pro.nav.action.view;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.pro.Messages;

public class ViewAddEditWizard extends Wizard {
	private Schema schema;
	private DBView data;
	
	private ViewFstDespWizardPage viewFstDespWizardPage;
	private ViewSecColumnInfoWizardPage viewSecColumnInfoWizardPage;
	
	private boolean isAddPage;

	public ViewAddEditWizard(Schema schema, DBView data) {
		this.schema = schema;
		this.data = data;
		this.isAddPage = data==null?true:false;
		if(isAddPage){
			this.setWindowTitle(Messages.ViewAddEditWizard_0);
		}else {
			this.setWindowTitle(Messages.ViewAddEditAction_2);
		}
	}
	
	@Override
	public void addPages() {
		viewFstDespWizardPage = new ViewFstDespWizardPage(this);
		this.addPage(viewFstDespWizardPage);
		viewSecColumnInfoWizardPage = new ViewSecColumnInfoWizardPage(this);
		this.addPage(viewSecColumnInfoWizardPage);
	}

	@Override
	public boolean performFinish() {
		if(isAddPage){
			DBView view = new DBView(viewFstDespWizardPage.getNameValue(), schema);
			view.setDescription(viewFstDespWizardPage.getDespValue());
			view.setQuerySql(viewSecColumnInfoWizardPage.getSqlStr());
			schema.addDBView(view);
			setData(view);
		}else {
			data.setDescription(viewFstDespWizardPage.getDespValue());
			data.setQuerySql(viewSecColumnInfoWizardPage.getSqlStr());
		}
		return true;
	}

	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public ViewFstDespWizardPage getViewFstDespWizardPage() {
		return viewFstDespWizardPage;
	}
	public void setViewFstDespWizardPage(ViewFstDespWizardPage viewFstDespWizardPage) {
		this.viewFstDespWizardPage = viewFstDespWizardPage;
	}
	public boolean isAddPage() {
		return isAddPage;
	}
	public void setAddPage(boolean isAddPage) {
		this.isAddPage = isAddPage;
	}
	public DBView getData() {
		return data;
	}
	public void setData(DBView data) {
		this.data = data;
	}
}