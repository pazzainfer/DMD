package com.leven.dmd.pro.pref.action;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.domain.DBType;
import com.leven.dmd.pro.domain.DBTypeConstant;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;

public class DBConfigAddWizardPage extends WizardPage {

	private Text nameText;
	private Text driverClassNameText;
	private Text driverUrlText;
	private Link driverUrlTemp;
	private Text driverUserNameText;
	private Text driverPasswordText;
	private Combo driverTypeCombo;
	private Text driverJarPath;

	private DBConfigModel dbConfig;
	private DBConfigAddWizard wizard;
	
	protected DBConfigAddWizardPage(DBConfigAddWizard wizard) {
		super("DBConfigAddWizardPage"); //$NON-NLS-1$
		this.wizard = wizard;
		this.setTitle(Messages.DBConfigAddWizardPage_1);
		this.setMessage(Messages.DBConfigAddWizardPage_2, 2);
		setPageComplete(false);
	}

	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);
		setControl(container);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 14;
		
		GridData labData = new GridData(65,14);
		
		Label nameLab = new Label(container, SWT.NONE);
		nameLab.setText(Messages.DBConfigAddWizardPage_3);
		nameLab.setLayoutData(labData);
		nameText = new Text(container,SWT.BORDER);
		nameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updatePageComplete();
			}
		});
		nameText.setLayoutData(gd);
		
		final Label driveTypeLabel = new Label(container, SWT.NONE);
		driveTypeLabel.setLayoutData(labData);
		driveTypeLabel.setText(Messages.DBConfigAddWizardPage_4);
		driverTypeCombo = new Combo(container, SWT.READ_ONLY);
		driverTypeCombo.setLayoutData(gd);
		driverTypeCombo.setItems(DBTypeConstant.dbTypeArray);
		driverTypeCombo.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				if(!driverTypeCombo.getText().equals("")){ //$NON-NLS-1$
					DBType dbType = DBTypeConstant.getDBType(driverTypeCombo.getText());
					if(dbType!=null){
						driverClassNameText.setText(dbType.getDriver());
						driverUrlTemp.setText("<A>" + dbType.getUrl() + "</A>"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				updatePageComplete();
			}
		});
		

		GridData gridData = new GridData();
	      gridData.horizontalSpan = 3;
	      gridData.grabExcessHorizontalSpace = true;
	      gridData.grabExcessVerticalSpace = true;
	      gridData.horizontalAlignment = GridData.FILL;
	      

	      Group group = new Group(container,SWT.FULL_SELECTION|SWT.NONE);
	      group.setLayoutData(gridData);
	      group.setLayout(new GridLayout(4, false)); 
			final Label driverJarPathLabel = new Label(group, SWT.NONE);
			driverJarPathLabel.setLayoutData(new GridData(80,14));
			driverJarPathLabel.setText(Messages.DBConfigAddWizardPage_8);
			driverJarPath = new Text(group, SWT.BORDER);
			driverJarPath.setEditable(false);
			driverJarPath.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					updatePageComplete();
				}
			});
			driverJarPath.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		              | GridData.GRAB_VERTICAL | GridData.FILL_HORIZONTAL
		              | GridData.FILL_VERTICAL));
			final Button chooseDriverJarPathButton = new Button(group, SWT.PUSH);
			chooseDriverJarPathButton.setLayoutData(new GridData(100,23));
			final Button clearDriverJarPathButton = new Button(group, SWT.PUSH);
			clearDriverJarPathButton.setLayoutData(new GridData(80,23));
			clearDriverJarPathButton.setText(Messages.DBConfigAddWizardPage_9);
			clearDriverJarPathButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					driverJarPath.setText(""); //$NON-NLS-1$
					chooseDriverJarPathButton.setText(Messages.DBConfigAddWizardPage_11);
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
			});
			String oldJarPath = driverJarPath.getText();
			if(oldJarPath==null || oldJarPath.equals("")) { //$NON-NLS-1$
				chooseDriverJarPathButton.setText(Messages.DBConfigAddWizardPage_11);
			}else {
				chooseDriverJarPathButton.setText(Messages.DBConfigAddWizardPage_14);
			}
			
			chooseDriverJarPathButton.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					String oldJarPath = driverJarPath.getText();
					FileDialog fileDialog = new FileDialog(container.getShell());
					fileDialog.setText(Messages.DBConfigAddWizardPage_15);
					fileDialog.setFilterExtensions(new String[]{"*.jar"}); //$NON-NLS-1$
					String jarFilePath = fileDialog.open();
					if(oldJarPath==null || oldJarPath.equals("")) { //$NON-NLS-1$
						driverJarPath.setText(jarFilePath);
					}else {
						if(jarFilePath!= null) {
							driverJarPath.setText(oldJarPath+";"+jarFilePath); //$NON-NLS-1$
						}
					}
					if(driverJarPath.getText()!=null && !driverJarPath.getText().equals("")) { //$NON-NLS-1$
						chooseDriverJarPathButton.setText(Messages.DBConfigAddWizardPage_14);
					}
					
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
			});
		
		final Label driverClassNameLabel = new Label(container, SWT.NONE);
		driverClassNameLabel.setLayoutData(labData);
		driverClassNameLabel.setText(Messages.DBConfigAddWizardPage_21);
		driverClassNameText = new Text(container, SWT.BORDER);
		driverClassNameText.setLayoutData(gd);
		driverClassNameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updatePageComplete();
			}
		});
		
		final Label driverUrlLabel = new Label(container, SWT.NONE);
		driverUrlLabel.setLayoutData(labData);
		driverUrlLabel.setText(Messages.DBConfigAddWizardPage_22);
		driverUrlText = new Text(container, SWT.BORDER);
		driverUrlText.setLayoutData(gd);
		driverUrlText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updatePageComplete();
			}
		});
		
		final Label driverUrlFormatLabel = new Label(container, SWT.NONE);
		driverUrlFormatLabel.setLayoutData(labData);
		Composite formatComp = new Composite(container, SWT.NONE);
		formatComp.setLayoutData(gd);
		formatComp.setLayout(new GridLayout(2,false));
		Label formatLab = new Label(formatComp, SWT.NONE);
		formatLab.setText(Messages.DBConfigAddWizardPage_23);
		GridData formatLabData = new GridData(60,14);
		formatLabData.verticalAlignment = SWT.TOP;
		formatLab.setLayoutData(formatLabData);
		
		GridData formatLinkData = new GridData(GridData.FILL_HORIZONTAL);
		formatLinkData.verticalAlignment = SWT.TOP;
		driverUrlTemp = new Link(formatComp, SWT.NONE);
		driverUrlTemp.setLayoutData(formatLinkData);
		DBType dbType = DBTypeConstant.getDBType(driverTypeCombo.getText());
		if(dbType!=null){
			driverUrlTemp.setText("<A>" + dbType.getUrl() + "</A>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		driverUrlTemp.setToolTipText(Messages.DBConfigAddWizardPage_26);
		driverUrlTemp.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String temp;
				if(!(temp = driverUrlTemp.getText()).equals("")){ //$NON-NLS-1$
					driverUrlText.setText(temp.substring(3,temp.length()-4));
					updatePageComplete();
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		final Label driverUserNameLabel = new Label(container, SWT.NONE);
		driverUserNameLabel.setLayoutData(labData);
		driverUserNameLabel.setText(Messages.DBConfigAddWizardPage_28);
		driverUserNameText = new Text(container, SWT.BORDER);
		driverUserNameText.setLayoutData(gd);
		driverUserNameText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updatePageComplete();
			}
		});
		
		final Label driverPasswordLabel = new Label(container, SWT.NONE);
		driverPasswordLabel.setLayoutData(labData);
		driverPasswordLabel.setText(Messages.DBConfigAddWizardPage_29);
		driverPasswordText = new Text(container, SWT.BORDER);
		driverPasswordText.setLayoutData(gd);
		driverPasswordText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updatePageComplete();
			}
		});

		
		Button testButton = new Button(container, SWT.PUSH);
		testButton.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,false,2,1));
		testButton.setText(Messages.DBConfigAddWizardPage_30);
		final MessageBox errorBox = new MessageBox(container.getShell(), SWT.ICON_ERROR);
		final MessageBox inForBox = new MessageBox(container.getShell(),SWT.ICON_INFORMATION);
		testButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(DBConfigAddWizardPage.this.isTextNull(driverClassNameText)){
					errorBox.setMessage(Messages.DBConfigAddWizardPage_31);
					errorBox.open();
					return;
					
				}
				if(DBConfigAddWizardPage.this.isTextNull(driverUrlText)){
					errorBox.setMessage(Messages.DBConfigAddWizardPage_32);
					errorBox.open();
					return;
				}
				if(DBConfigAddWizardPage.this.isTextNull(driverUserNameText)){
					errorBox.setMessage(Messages.DBConfigAddWizardPage_33);
					errorBox.open();
					return;
				}
				if(DBConfigAddWizardPage.this.isTextNull(driverPasswordText)){
					errorBox.setMessage(Messages.DBConfigAddWizardPage_34);
					errorBox.open();
					return;
				}
				if(DBConfigAddWizardPage.this.isTextNull(driverJarPath)){
					errorBox.setMessage(Messages.DBConfigAddWizardPage_35);
					errorBox.open();
					return;
				}
				if(driverTypeCombo.getText()==null || driverTypeCombo.getText().equals("")){ //$NON-NLS-1$
					errorBox.setMessage(Messages.DBConfigAddWizardPage_37);
					errorBox.open();
					return;
				}
				
				DBConfigModel dhccDatasourceInfo = DBConfigAddWizardPage.this.getDBConfigModel();
				if(!DBConfigUtil.isConfigAvailable(dhccDatasourceInfo)) {
					errorBox.setMessage(Messages.DBConfigAddWizardPage_38);
					errorBox.open();
				}else {
					inForBox.setMessage(Messages.DBConfigAddWizardPage_39);
					inForBox.open();
				}
				
			}
			
		});
		this.updatePageComplete();
	}
	public DBConfigModel getDBConfigModel() {
		if(dbConfig==null){
			dbConfig = new DBConfigModel();
		}
		dbConfig.setName(nameText.getText());
		dbConfig.setDriver(driverClassNameText.getText());
		dbConfig.setUrl(driverUrlText.getText());
		dbConfig.setUsername(driverUserNameText.getText());
		dbConfig.setPassword(driverPasswordText.getText());
		dbConfig.setType(driverTypeCombo.getText());
		dbConfig.setJarPath(driverJarPath.getText());
		return dbConfig;
	}
	
	private boolean isTextNull(Text text) {
		if(text.getText()==null || text.getText().equals("")) { //$NON-NLS-1$
			return true;
		}else {
			return false;
		}
	}
	
	
	public void updatePageComplete() {
		this.setPageComplete(false);
		if(DBConfigAddWizardPage.this.isTextNull(nameText)){
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_41,3);
			return;
		} else if(wizard.getConfigMap().containsKey(nameText.getText().trim())) {
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_42,3);
			return;
		}
		if(DBConfigAddWizardPage.this.isTextNull(driverClassNameText)){
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_43,3);
			return;
		}
		if(DBConfigAddWizardPage.this.isTextNull(driverUrlText)){
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_44,3);
			return;
		}
		if(DBConfigAddWizardPage.this.isTextNull(driverUserNameText)){
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_45,3);
			return;
		}
		if(DBConfigAddWizardPage.this.isTextNull(driverPasswordText)){
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_46,3);
			return;
		}
		if(DBConfigAddWizardPage.this.isTextNull(driverJarPath)){
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_47,3);
			return;
		}
		if(driverTypeCombo.getText()==null || driverTypeCombo.getText().equals("")){ //$NON-NLS-1$
			setPageComplete(false);
			setMessage(Messages.DBConfigAddWizardPage_49,3);
			return;
		}
		setPageComplete(true);
		setMessage(Messages.DBConfigAddWizardPage_50,1);
	}

	public DBConfigModel getDbConfig() {
		return dbConfig;
	}
	public void setDbConfig(DBConfigModel dbConfig) {
		this.dbConfig = dbConfig;
	}
}
