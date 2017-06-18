package com.leven.dmd.gef.wizard.tableeditor.action;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.tmpfile.model.ColumnTemplate;

public class ColumnImportWizard extends Wizard {
	private ColumnImportWizardPage columnImportWizardPage;
	private Schema schema;
	private TableViewer viewer;
	private Map<String, Column> columnMap;
	
	public ColumnImportWizard(Schema schema, TableViewer viewer, Map<String, Column> columnMap) {
		super();
		this.schema = schema;
		this.viewer = viewer;
		this.columnMap = columnMap;
		this.setWindowTitle(Messages.ColumnImportWizard_0);
	}

	@Override
	public void addPages() {
		columnImportWizardPage = new ColumnImportWizardPage(schema,columnMap);
		addPage(columnImportWizardPage);
	}
	
	@Override
	public boolean performFinish() {
		ArrayList columnList = ((ArrayList)viewer.getInput());
		Column p;
		for(Column c : columnImportWizardPage.getSelectedList()){
			p = getColumn(c);
			viewer.add(p);
		    columnList.add(p);
		    columnMap.put(p.getName(), p);
		}
		return true;
	}
	
	private Column getColumn(Column c){
		Column p = new Column();
		p.setName(c.getName());
		p.setType(c.getType());
		p.setCnName(c.getCnName());
		p.setLength(c.getLength());
		p.setScale(c.getScale());
		if(c.getColumnTemplate()!=null){
			ColumnTemplate temp = schema.getSchemaTemplate().getColumnTemplatesMap().get(c.getColumnTemplate().getCode());
			if(temp != null){
				p.setType(temp.getColumnType().getType());
				p.setLength(temp.getColumnLength());
				p.setScale(temp.getColumnScale());
				p.setCnName(temp.getColumnCnName());
			}
		}
		return p;
	}

}
