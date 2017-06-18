package com.leven.dmd.pro.nav.action.pack;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.model.TablePackage;
import com.leven.dmd.pro.Messages;

public class TablePackageEditWizardPage extends WizardPage {
	
	private Text nameTxt;
	private Text descriptionTxt;
	private TablePackageEditWizard tablePackageEditWizard;

	protected TablePackageEditWizardPage(TablePackageEditWizard tablePackageEditWizard) {
		super("TablePackageEditWizardPage"); //$NON-NLS-1$
		this.tablePackageEditWizard = tablePackageEditWizard;
		this.setTitle(Messages.TablePackageEditWizardPage_1);
		this.setMessage(Messages.TablePackageEditWizardPage_2, 2);
		this.setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		GridData labDta = new GridData(65,14);
		labDta.verticalAlignment = SWT.BEGINNING;
		GridData nameTxtDta = new GridData(GridData.FILL_HORIZONTAL);
		nameTxtDta.heightHint = 14;
		
		Label nameLab = new Label(group,SWT.NONE);
		nameLab.setText(Messages.TablePackageEditWizardPage_3);
		nameLab.setLayoutData(labDta);
		nameTxt = new Text(group, SWT.BORDER);
		nameTxt.setText(tablePackageEditWizard.getTablePackage().getName());
		nameTxt.setLayoutData(nameTxtDta);
		nameTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		Label desLab = new Label(group,SWT.NONE);
		desLab.setText(Messages.TablePackageEditWizardPage_4);
		desLab.setLayoutData(labDta);
		descriptionTxt = new Text(group, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		descriptionTxt.setText(tablePackageEditWizard.getTablePackage().getDescription());
		descriptionTxt.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		setControl(group);
	}
	
	private void updateComplete(){
		String oldName = tablePackageEditWizard.getTablePackage().getName();
		String name = nameTxt.getText().trim();
		String description = descriptionTxt.getText().trim();
		if(name.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.TablePackageEditWizardPage_6, 3);
			this.setPageComplete(false);
			return;
		}
		for(TablePackage temp : tablePackageEditWizard.getList()){
			if(temp.getName().equals(name) && !name.equals(oldName)){
				this.setMessage(Messages.TablePackageEditWizardPage_7, 3);
				this.setPageComplete(false);
				return;
			}
		}
		if(description.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.TablePackageEditWizardPage_9, 3);
			this.setPageComplete(false);
			return;
		}
		this.setMessage(Messages.TablePackageEditWizardPage_10, 1);
		this.setPageComplete(true);
	}

	public Text getNameTxt() {
		return nameTxt;
	}

	public void setNameTxt(Text nameTxt) {
		this.nameTxt = nameTxt;
	}

	public Text getDescriptionTxt() {
		return descriptionTxt;
	}

	public void setDescriptionTxt(Text descriptionTxt) {
		this.descriptionTxt = descriptionTxt;
	}
}
