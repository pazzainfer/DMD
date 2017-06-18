package com.leven.dmd.gef.wizard.tableeditor;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;

public class ColumnSelectWizard extends Wizard {

	private ColumnSelectWizardPage columnSelectWizardPage;
	private ArrayList<Column> columns;
	private String[] itemRight;
	private Map<String,String> map;
	
	public ColumnSelectWizard(ArrayList<Column> columns,String[] itemRight,Map<String,String> map) {
		super();
		this.setWindowTitle(Messages.ColumnSelectWizard_0);
		this.columns = columns;
		this.itemRight = itemRight;
		this.map = map;
	}

	@Override
	public void addPages() {
		columnSelectWizardPage = new ColumnSelectWizardPage(columns, itemRight);
		addPage(columnSelectWizardPage);
	}

	@Override
	public boolean performFinish() {
		String[] results = columnSelectWizardPage.getRight().getItems();
		StringBuffer result = new StringBuffer(""); //$NON-NLS-1$
		for(int i=0;i<results.length;i++){
			result.append(results[i]);
			result.append(","); //$NON-NLS-1$
		}
		if(result.length()>0){
			result.replace(result.length()-1, result.length(), ""); //$NON-NLS-1$
		}
		map.put("result",result.toString()); //$NON-NLS-1$
		return true;
	}

	public ColumnSelectWizardPage getColumnSelectWizardPage() {
		return columnSelectWizardPage;
	}
	public void setColumnSelectWizardPage(
			ColumnSelectWizardPage columnSelectWizardPage) {
		this.columnSelectWizardPage = columnSelectWizardPage;
	}
}
