package com.leven.dmd.pro.nav.action.db;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.db.DBOperator;
import com.leven.dmd.pro.nav.domain.INavigatorTreeNode;
import com.leven.dmd.pro.util.DbTableUtil;
import com.leven.dmd.pro.util.FileUtil;


public class ExportDllWizard extends Wizard {
	private Object obj;
	private ExportDllFunctionSelectWizardPage functionSelectWizardPage;
	private Schema schema;

	public ExportDllWizard(Object obj) {
		this.obj = obj;
		schema = (Schema)((INavigatorTreeNode)obj).getRoot();
		this.setWindowTitle(Messages.ExportDllWizard_0);
	}
	public ExportDllWizard(Schema schema) {
		this.schema = schema;
		this.setWindowTitle(Messages.ExportDllWizard_0);
	}

	@Override
	public void addPages() {
		functionSelectWizardPage = new ExportDllFunctionSelectWizardPage(schema);
		addPage(functionSelectWizardPage);
	}
	@Override
	public boolean performFinish() {
		Table table;
		String dbType = functionSelectWizardPage.getDbTypeCombo().getText();
		StringBuffer content = new StringBuffer();
		if(functionSelectWizardPage.getAllSpaceBtn().getSelection()){
			for(Tablespace space : schema.getTablespaces()){
				content.append(DbTableUtil.getTablespaceSql(space,dbType));
				content.append(";\n"); //$NON-NLS-1$
			}
		}else if(functionSelectWizardPage.getPartSpaceBtn().getSelection()){
			for(Tablespace space : functionSelectWizardPage.getSpaceList()){
				content.append(DbTableUtil.getTablespaceSql(space,dbType));
				content.append(";\n"); //$NON-NLS-1$
			}
		}
		if(functionSelectWizardPage.getAllTableBtn().getSelection()){
			List tables = schema.getAllTables();
			for (int i = 0; i < tables.size(); i++) {
				table = (Table)tables.get(i);
				try {
//					content.append(DbTableUtil.getTableDllContent(table,dbType));
					List<String> list = DBOperator.getTableSqls(table, true, dbType);
					for(String sql : list){
						content.append(sql);
						content.append(";\n"); //$NON-NLS-1$
					}
				} catch (Exception e) {
					continue;
				}
			}
		} else if (functionSelectWizardPage.getPartTableBtn().getSelection()){
			for (Table table1 : functionSelectWizardPage.getTableList()) {
				try {
					content.append(DBOperator.getTableDDL(table1,dbType));
					content.append("\n"); //$NON-NLS-1$
				} catch (Exception e) {
					continue;
				}
			}
		}
		
		if(functionSelectWizardPage.getAllSeqBtn().getSelection()){
			for(SequenceTemplate sequence : schema.getSchemaTemplate().getSequenceTemplates()){
				content.append(DbTableUtil.getSequenceSql(sequence,dbType));
				content.append(";\n"); //$NON-NLS-1$
			}
		}else if(functionSelectWizardPage.getPartSeqBtn().getSelection()){
			for(SequenceTemplate sequence : functionSelectWizardPage.getSequenceList()){
				content.append(DbTableUtil.getSequenceSql(sequence,dbType));
				content.append(";\n"); //$NON-NLS-1$
			}
		}
		
		if(functionSelectWizardPage.getAllViewBtn().getSelection()){
			for(DBView view : schema.getDbViews()){
				content.append(DbTableUtil.getViewSql(view,dbType));
				content.append(";\n"); //$NON-NLS-1$
			}
		}else if(functionSelectWizardPage.getPartViewBtn().getSelection()){
			for(DBView view : functionSelectWizardPage.getViewList()){
				content.append(DbTableUtil.getViewSql(view,dbType));
				content.append(";\n"); //$NON-NLS-1$
			}
		}
		if(FileUtil.makeNewFileWithContent(content.toString()
				, functionSelectWizardPage.getPathText().getText().trim())){
			MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
			mb.setText(this.getWindowTitle());
			mb.setMessage(Messages.ExportDllWizard_1);
			mb.open();
			return true;
		}else {
			MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
			mb.setText(this.getWindowTitle());
			mb.setMessage(Messages.ExportDllWizard_2);
			mb.open();
			return true;
		}
	}
	
	@Override
	public void createPageControls(Composite pageContainer) {
//	    super.createPageControls(pageContainer);
	}

}
