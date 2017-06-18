package com.leven.dmd.pro.nav.action.space;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.pro.Messages;

public class TablespaceAddEditWizard extends Wizard {
	private Schema schema;
	private Tablespace data;
	
	private TablespaceWizardPage tablespaceWizardPage;
	
	private boolean isAddPage;

	public TablespaceAddEditWizard(Schema schema, Tablespace data) {
		this.schema = schema;
		this.data = data;
		this.isAddPage = data==null?true:false;
		if(isAddPage){
			this.setWindowTitle(Messages.TablespaceAddEditAction_1);
		}else {
			this.setWindowTitle(Messages.TablespaceAddEditAction_0);
		}
	}
	
	@Override
	public void addPages() {
		tablespaceWizardPage = new TablespaceWizardPage(this);
		this.addPage(tablespaceWizardPage);
	}

	@Override
	public boolean performFinish() {
		if(isAddPage){
			Tablespace view = new Tablespace(tablespaceWizardPage.getNameValue(), schema);
			view.setDescription(tablespaceWizardPage.getDespValue());
			view.setSize(tablespaceWizardPage.getSizeValue());
			view.setFileList(tablespaceWizardPage.getFileList());
			schema.addTablespace(view);
			setData(view);
		}else {
			data.setDescription(tablespaceWizardPage.getDespValue());
			data.setSize(tablespaceWizardPage.getSizeValue());
			data.setFileList(tablespaceWizardPage.getFileList());
		}
		return true;
	}

	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public TablespaceWizardPage getTablespaceWizardPage() {
		return tablespaceWizardPage;
	}
	public void setTablespaceWizardPage(TablespaceWizardPage tablespaceWizardPage) {
		this.tablespaceWizardPage = tablespaceWizardPage;
	}
	public boolean isAddPage() {
		return isAddPage;
	}
	public void setAddPage(boolean isAddPage) {
		this.isAddPage = isAddPage;
	}
	public Tablespace getData() {
		return data;
	}
	public void setData(Tablespace data) {
		this.data = data;
	}
}