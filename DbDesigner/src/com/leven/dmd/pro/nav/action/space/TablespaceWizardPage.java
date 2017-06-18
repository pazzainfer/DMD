package com.leven.dmd.pro.nav.action.space;

import java.util.ArrayList;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.model.TablespaceFile;
import com.leven.dmd.gef.tmpfile.util.StringUtil;
import com.leven.dmd.pro.Messages;

public class TablespaceWizardPage extends WizardPage {
	private TablespaceAddEditWizard tablespaceAddEditWizard;
	private Text nameTxt;
	private Text sizeTxt;
	private Text descriptionTxt;
	
	private Text filePathTxt;
	private Text fileSizeTxt;
	private Table fileList;
	private ArrayList<TablespaceFile> list = new ArrayList<TablespaceFile>();

	protected TablespaceWizardPage(TablespaceAddEditWizard tablespaceAddEditWizard) {
		super("TablespaceWizardPage"); //$NON-NLS-1$
		this.tablespaceAddEditWizard = tablespaceAddEditWizard;
		if(tablespaceAddEditWizard.isAddPage()){
			this.setTitle(Messages.TablespaceWizardPage_0);
		}else {
			this.setTitle(Messages.TablespaceWizardPage_1);
			for(TablespaceFile file : tablespaceAddEditWizard.getData().getFileList()){
				list.add(new TablespaceFile(file.getFilePath(),file.getSize()));
			}
		}
		setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent,SWT.NONE);
		comp.setLayout(new GridLayout(2,false));
		GridData labDta = new GridData(65,14);
		labDta.verticalAlignment = SWT.BEGINNING;
		GridData nameTxtDta = new GridData(GridData.FILL_HORIZONTAL);
		nameTxtDta.heightHint = 16;
		
		GridData areaTxtDta = new GridData(GridData.FILL_HORIZONTAL);
		areaTxtDta.heightHint = 50;
		
		Label nameLab = new Label(comp,SWT.NONE);
		nameLab.setText(Messages.TablespaceWizardPage_4);
		nameLab.setLayoutData(labDta);
		nameTxt = new Text(comp, SWT.BORDER);
		nameTxt.setLayoutData(nameTxtDta);
		if(!tablespaceAddEditWizard.isAddPage()){
			nameTxt.setEditable(false);
			nameTxt.setText(tablespaceAddEditWizard.getData().getName()==null?"":tablespaceAddEditWizard.getData().getName());//$NON-NLS-1$
		}else {
			nameTxt.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					updateComplete();
				}
			});
		}
		
		Label sizeLab = new Label(comp,SWT.NONE);
		sizeLab.setText(Messages.TablespaceWizardPage_9);
		sizeLab.setLayoutData(labDta);
		sizeTxt = new Text(comp, SWT.BORDER);
		sizeTxt.setLayoutData(nameTxtDta);
		sizeTxt.setEditable(false);
		if(!tablespaceAddEditWizard.isAddPage()){
			sizeTxt.setText(tablespaceAddEditWizard.getData().getSize()+"");//$NON-NLS-1$
		}else {
			sizeTxt.setText("0"); //$NON-NLS-1$
		}
		sizeTxt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		Label desLab = new Label(comp,SWT.NONE);
		desLab.setText(Messages.TablespaceWizardPage_2);
		desLab.setLayoutData(labDta);
		descriptionTxt = new Text(comp, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		descriptionTxt.setLayoutData(areaTxtDta);
		if(!tablespaceAddEditWizard.isAddPage()){
			descriptionTxt.setText(tablespaceAddEditWizard.getData().getDescription()==null?"":tablespaceAddEditWizard.getData().getDescription());//$NON-NLS-1$
		}
		

		GridData compData2 = new GridData(GridData.FILL_BOTH);
		compData2.horizontalSpan = 2;
		compData2.minimumHeight = 150;
		Group fileComp = new Group(comp, SWT.NONE);
		fileComp.setText(Messages.TablespaceWizardPage_10);
		fileComp.setLayoutData(compData2);
		fileComp.setLayout(new FormLayout());
		
		FormData listFormData = new FormData();
		listFormData.left = new FormAttachment(0, 0);
		listFormData.top = new FormAttachment(0, 0);
		listFormData.bottom = new FormAttachment(100, -40);
		listFormData.right = new FormAttachment(100, 0);
		
		fileList = new Table(fileComp, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		fileList.setLayoutData(listFormData);
		TableColumn pathColumn = new TableColumn(fileList,SWT.CENTER);
		pathColumn.setWidth(200);
		pathColumn.setText(Messages.TablespaceWizardPage_13);
		TableColumn sizeColumn = new TableColumn(fileList,SWT.CENTER); 
		sizeColumn.setWidth(100);
		sizeColumn.setText(Messages.TablespaceWizardPage_14);
		fileList.setHeaderVisible(true);
		
		if(!tablespaceAddEditWizard.isAddPage()){
			TableItem item;
			for(TablespaceFile file : list){
				item = new TableItem(fileList, SWT.NONE);
				item.setText(new String[]{file.getFilePath(),file.getSize()+""}); //$NON-NLS-1$
			}
		}
		
		Composite inputComp = new Composite(fileComp, SWT.NONE);
		FormData inputFormData = new FormData();
		inputFormData.left = new FormAttachment(0, 0);
		inputFormData.top = new FormAttachment(100, -40);
		inputFormData.bottom = new FormAttachment(100, 0);
		inputFormData.right = new FormAttachment(100, 0);
		inputComp.setLayoutData(inputFormData);
		inputComp.setLayout(new GridLayout(5,false));

		GridData btnData = new GridData(75,23);
		
		Label fileInputLab = new Label(inputComp, SWT.NONE);
		fileInputLab.setLayoutData(new GridData(100,23));
		fileInputLab.setText(Messages.TablespaceWizardPage_11);
		
		filePathTxt = new Text(inputComp, SWT.BORDER);
		filePathTxt.setLayoutData(nameTxtDta);
		
		fileSizeTxt = new Text(inputComp, SWT.BORDER);
		fileSizeTxt.setLayoutData(new GridData(40,16));
		
		
		Button addBtn = new Button(inputComp, SWT.NONE);
		addBtn.setText(Messages.TablespaceWizardPage_3);
		addBtn.setLayoutData(btnData);
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String input = filePathTxt.getText().trim();
				String size = fileSizeTxt.getText().trim();
				if(!"".equals(input) && !"".equals(size)){ //$NON-NLS-1$ //$NON-NLS-2$
					for(TablespaceFile file : list){
						if(input.equals(file.getFilePath())){
							MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
							mb.setText(Messages.TablespaceWizardPage_17);
							mb.setMessage(Messages.TablespaceWizardPage_12);
							mb.open();
							return;
						}
					}
					int rsize = 0;
					try {
						rsize = Integer.parseInt(size);
					} catch (NumberFormatException e1) {
						MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
						mb.setText(Messages.TablespaceWizardPage_17);
						mb.setMessage(Messages.TablespaceWizardPage_18);
						mb.open();
						return;
					}
					TableItem item = new TableItem(fileList,SWT.NONE);
					item.setText(0, input);
					item.setText(1, size);
					filePathTxt.setText("");	//$NON-NLS-1$
					fileSizeTxt.setText("");	//$NON-NLS-1$
					sizeTxt.setText((Integer.parseInt(sizeTxt.getText()) + Integer.parseInt(size))+""); //$NON-NLS-1$
					list.add(new TablespaceFile(input,rsize));
				}
			}
		});
		
		Button removeBtn = new Button(inputComp, SWT.NONE);
		removeBtn.setText(Messages.TablespaceWizardPage_5);
		removeBtn.setLayoutData(btnData);
		removeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String removedSize = fileList.getSelection()[0].getText(1);
				String removedPath = fileList.getSelection()[0].getText(0);
				fileList.remove(fileList.getSelectionIndex());
				sizeTxt.setText((Integer.parseInt(sizeTxt.getText()) - Integer.parseInt(removedSize))+""); //$NON-NLS-1$
				int beRemove = -1;
				for(int i=0;i<list.size();i++){
					if(removedPath.equals(list.get(i).getFilePath())){
						beRemove = i;
						break;
					}
				}
				if(beRemove>-1){
					list.remove(beRemove);
				}
			}
		});
		updateComplete();
		
		setControl(comp);
	}
	
	private void updateComplete(){
		String name = nameTxt.getText().trim();
		String size = sizeTxt.getText().trim();
		if(tablespaceAddEditWizard.isAddPage()){
			if(name.equals("")){ //$NON-NLS-1$
				this.setMessage(Messages.TablespaceWizardPage_6, 3);
				this.setPageComplete(false);
				return;
			}else if(!StringUtil.isRightDBName(name)){
				this.setMessage(Messages.TablespaceWizardPage_15, 3);
				this.setPageComplete(false);
				return;
			}
		}
		if(!StringUtil.isNumber(size)){
			this.setMessage(Messages.TablespaceWizardPage_7, 3);
			this.setPageComplete(false);
			return;
		}
		this.setMessage(Messages.TablespaceWizardPage_8, 1);
		this.setPageComplete(true);
	}

	public TablespaceAddEditWizard getTablespaceAddEditWizard() {
		return tablespaceAddEditWizard;
	}
	public void setTablespaceAddEditWizard(TablespaceAddEditWizard tablespaceAddEditWizard) {
		this.tablespaceAddEditWizard = tablespaceAddEditWizard;
	}
	public String getNameValue(){
		return nameTxt.getText();
	}
	public String getDespValue(){
		return descriptionTxt.getText();
	}
	public int getSizeValue(){
		try {
			return Integer.parseInt(sizeTxt.getText().trim());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public ArrayList<TablespaceFile> getFileList(){
		return list;
	}
}