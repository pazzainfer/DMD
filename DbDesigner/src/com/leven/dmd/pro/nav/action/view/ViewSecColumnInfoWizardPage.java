package com.leven.dmd.pro.nav.action.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.pref.dialog.DBConfigSelectDialog;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;

public class ViewSecColumnInfoWizardPage extends WizardPage {
	private ViewAddEditWizard viewAddEditWizard;
	private Text sqlTxt;
	private Button testSqlBtn;

	protected ViewSecColumnInfoWizardPage(ViewAddEditWizard viewAddEditWizard) {
		super("ViewFstDespWizardPage"); //$NON-NLS-1$
		this.viewAddEditWizard = viewAddEditWizard;
		this.setPageComplete(true);
		if(viewAddEditWizard.isAddPage()){
			this.setTitle(Messages.ViewSecColumnInfoWizardPage_0);
		}else {
			this.setTitle(Messages.ViewSecColumnInfoWizardPage_2);
		}
	}

	public void createControl(Composite parent) {
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		GridData labDta = new GridData(65,14);
		labDta.verticalAlignment = SWT.BEGINNING;
		GridData nameTxtDta = new GridData(GridData.FILL_HORIZONTAL);
		nameTxtDta.heightHint = 14;
		GridData btnDta = new GridData();
		btnDta.heightHint = 25;
		btnDta.horizontalSpan = 2;
		
		Label sqlLab = new Label(group,SWT.NONE);
		sqlLab.setText(Messages.ViewSecColumnInfoWizardPage_1);
		sqlLab.setLayoutData(labDta);
		sqlTxt = new Text(group, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		sqlTxt.setLayoutData(new GridData(GridData.FILL_BOTH));
		if(!viewAddEditWizard.isAddPage() && viewAddEditWizard.getData()!=null){
			sqlTxt.setText(viewAddEditWizard.getData().getQuerySql()==null?"":viewAddEditWizard.getData().getQuerySql()); //$NON-NLS-1$
		}
		
		testSqlBtn = new Button(group,SWT.NONE);
		testSqlBtn.setText(Messages.ViewSecColumnInfoWizardPage_3);
		testSqlBtn.setLayoutData(btnDta);
		testSqlBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSQLTest();
			}
		});
		
		setControl(group);
	}
	
	private void doSQLTest(){
		String sqlstr = sqlTxt.getText().trim();
		if("".equals(sqlstr)){ //$NON-NLS-1$
			this.setErrorMessage(Messages.ViewSecColumnInfoWizardPage_5);
			return;
		}
		DBConfigSelectDialog dialog = new DBConfigSelectDialog(Display.getCurrent().getActiveShell());
		dialog.create();
		if(dialog.open()!=Dialog.OK){
			return;
		}
		DBConfigModel config = dialog.getDbConfigModel();
		if(DBConfigUtil.isConfigAvailable(config)){
			String result = DBConfigUtil.checkSQL(sqlstr, config);
			if(result!=null){
				MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
				mb.setText(Messages.ViewSecColumnInfoWizardPage_6);
				mb.setMessage(result);
				mb.open();
			}else {
				MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_INFORMATION);
				mb.setText(getTitle());
				mb.setMessage(Messages.ViewSecColumnInfoWizardPage_4);
				mb.open();
			}
		}else {
			MessageBox mb = new MessageBox(this.getShell(), SWT.ICON_ERROR);
			mb.setText(Messages.ViewSecColumnInfoWizardPage_6);
			mb.setMessage(Messages.ViewSecColumnInfoWizardPage_8);
			mb.open();
		}
	}

	public ViewAddEditWizard getViewAddEditWizard() {
		return viewAddEditWizard;
	}
	public void setViewAddEditWizard(ViewAddEditWizard viewAddEditWizard) {
		this.viewAddEditWizard = viewAddEditWizard;
	}
	public String getSqlStr(){
		return sqlTxt.getText();
	}
}