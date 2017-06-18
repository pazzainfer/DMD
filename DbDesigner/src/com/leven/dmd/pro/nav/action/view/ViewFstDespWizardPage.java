package com.leven.dmd.pro.nav.action.view;

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

import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Messages;

public class ViewFstDespWizardPage extends WizardPage {
	private ViewAddEditWizard viewAddEditWizard;
	private Text nameTxt;
	private Text descriptionTxt;

	protected ViewFstDespWizardPage(ViewAddEditWizard viewAddEditWizard) {
		super("ViewFstDespWizardPage"); //$NON-NLS-1$
		this.viewAddEditWizard = viewAddEditWizard;
		if(viewAddEditWizard.isAddPage()){
			this.setTitle(Messages.ViewFstDespWizardPage_0);
		}else {
			this.setTitle(Messages.ViewFstDespWizardPage_4);
		}
	}

	public void createControl(Composite parent) {
		Group group = new Group(parent,SWT.NONE);
		group.setLayout(new GridLayout(2,false));
		GridData labDta = new GridData(65,14);
		labDta.verticalAlignment = SWT.BEGINNING;
		GridData nameTxtDta = new GridData(GridData.FILL_HORIZONTAL);
		nameTxtDta.heightHint = 14;
		
		Label nameLab = new Label(group,SWT.NONE);
		nameLab.setText(Messages.ViewFstDespWizardPage_1);
		nameLab.setLayoutData(labDta);
		nameTxt = new Text(group, SWT.BORDER);
		nameTxt.setLayoutData(nameTxtDta);
		if(!viewAddEditWizard.isAddPage()){
			nameTxt.setEditable(false);
			nameTxt.setText(viewAddEditWizard.getData().getName()==null?"":viewAddEditWizard.getData().getName());//$NON-NLS-1$
		}else {
			nameTxt.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					updateComplete();
				}
			});
		}
		
		Label desLab = new Label(group,SWT.NONE);
		desLab.setText(Messages.ViewFstDespWizardPage_2);
		desLab.setLayoutData(labDta);
		descriptionTxt = new Text(group, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		descriptionTxt.setLayoutData(new GridData(GridData.FILL_BOTH));
		if(!viewAddEditWizard.isAddPage()){
			descriptionTxt.setText(viewAddEditWizard.getData().getDescription()==null?"":viewAddEditWizard.getData().getDescription());//$NON-NLS-1$
		}
		descriptionTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		setControl(group);
	}
	
	private void updateComplete(){
		String name = nameTxt.getText().trim();
		if(viewAddEditWizard.isAddPage()){
			if(name.equals("")){ //$NON-NLS-1$
				this.setMessage(Messages.ViewFstDespWizardPage_3, 3);
				this.setPageComplete(false);
				return;
			}else if(!StringUtil.isRightDBName(name)){
				this.setMessage(Messages.ViewFstDespWizardPage_5, 3);
				this.setPageComplete(false);
				return;
			}
		}
		this.setMessage(Messages.TablePackageAddWizardPage_10, 1);
		this.setPageComplete(true);
	}

	public ViewAddEditWizard getViewAddEditWizard() {
		return viewAddEditWizard;
	}
	public void setViewAddEditWizard(ViewAddEditWizard viewAddEditWizard) {
		this.viewAddEditWizard = viewAddEditWizard;
	}
	public String getNameValue(){
		return nameTxt.getText();
	}
	public String getDespValue(){
		return descriptionTxt.getText();
	}
}