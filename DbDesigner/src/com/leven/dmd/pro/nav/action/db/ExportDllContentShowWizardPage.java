package com.leven.dmd.pro.nav.action.db;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.domain.DBTypeConstant;

public class ExportDllContentShowWizardPage extends WizardPage {
	
	private Combo dbTypeCombo;

	protected ExportDllContentShowWizardPage() {
		super("ExportDllFunctionSelectWizardPage.java"); //$NON-NLS-1$
		this.setTitle(Messages.ExportDllContentShowWizardPage_1);
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(2,false));
		
		GridData labData = new GridData(65,14);
		labData.verticalAlignment = SWT.CENTER;
		GridData hFill = new GridData(GridData.FILL_HORIZONTAL);
		hFill.heightHint = 14;
		hFill.verticalAlignment = SWT.CENTER;
		
		Label dbTypeLab = new Label(comp, SWT.NONE);
		dbTypeLab.setText(Messages.ExportDllContentShowWizardPage_2);
		dbTypeLab.setLayoutData(labData);
		
		dbTypeCombo = new Combo(comp, SWT.READ_ONLY);
		dbTypeCombo.setItems(DBTypeConstant.dbTypeArray);
		dbTypeCombo.setLayoutData(hFill);
		dbTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		setControl(comp);
	}
	
	private void updateComplete(){
		String dbType = dbTypeCombo.getText();
		if(dbType.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.ExportDllContentShowWizardPage_4,2);
			this.setPageComplete(false);
			return;
		}
		this.setMessage("page complete!",1); //$NON-NLS-1$
		this.setPageComplete(true);
	}
	
	@Override
	public IWizardPage getNextPage() {
		return getWizard().getNextPage(this);
	}

}
