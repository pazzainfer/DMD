package com.leven.dmd.gef.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Table;
/**
 * ������������ҳ
 * @author lifeng
 * 2012-8-15 ����01:37:59
 */
public class RelationshipAddWizardPage extends WizardPage {
	private Table fTable;
	private Table pTable;
	
	private RelationshipAddWizard wizard;
	
	private Text foreignNameText;
	private Combo foreignColumn;
	private Text primaryNameText;
	private Combo primaryColumn;
	
	protected RelationshipAddWizardPage(RelationshipAddWizard wizard,String pageName,Table targetTable,Table sourceTable) {
		super(pageName);
		this.wizard = wizard;
		this.fTable = targetTable;
		this.pTable = sourceTable;
		this.setPageComplete(false);
		this.setTitle(Messages.RelationshipAddWizardPage_0);
	}

	public void createControl(final Composite parent) {
		Group comp = new Group(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1,false));
		GridLayout groupLayout = new GridLayout(2, false);
		
		//���ֶ�λ������
		GridData groupGrid = new GridData(GridData.FILL_HORIZONTAL);
		
		GridData labGrid = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		labGrid.widthHint = 70;
		labGrid.heightHint = 23;

		GridData textGrid = new GridData(GridData.FILL_BOTH);
		//
		Group foreignGroup = new Group(comp,SWT.NONE);
		foreignGroup.setLayoutData(groupGrid);
		foreignGroup.setLayout(groupLayout);
		
		Label foreignNameLab = new Label(foreignGroup,SWT.NONE);
		foreignNameLab.setText(Messages.RelationshipAddWizardPage_1);
		foreignNameLab.setLayoutData(labGrid);
		
		foreignNameText = new Text(foreignGroup, SWT.BORDER);
		foreignNameText.setLayoutData(textGrid);
		foreignNameText.setEditable(false);
		foreignNameText.setText(fTable.getName());

		Label foreignColumnLab = new Label(foreignGroup,SWT.NONE);
		foreignColumnLab.setText(Messages.RelationshipAddWizardPage_2);
		foreignColumnLab.setLayoutData(labGrid);
		
		foreignColumn = new Combo(foreignGroup, SWT.READ_ONLY | SWT.DROP_DOWN);
		foreignColumn.setItems(fTable.getComboValues());
		foreignColumn.setLayoutData(textGrid);
		foreignColumn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				updateComplete();
			}
		});
		
		
		//
		Group primaryGroup = new Group(comp,SWT.NONE);
		primaryGroup.setLayoutData(groupGrid);
		primaryGroup.setLayout(groupLayout);
		
		Label primaryNameLab = new Label(primaryGroup,SWT.NONE);
		primaryNameLab.setText(Messages.RelationshipAddWizardPage_3);
		primaryNameLab.setLayoutData(labGrid);
		
		primaryNameText = new Text(primaryGroup, SWT.BORDER);
		primaryNameText.setLayoutData(textGrid);
		primaryNameText.setEditable(false);
		primaryNameText.setText(pTable.getName());

		Label primaryColumnLab = new Label(primaryGroup,SWT.NONE);
		primaryColumnLab.setText(Messages.RelationshipAddWizardPage_4);
		primaryColumnLab.setLayoutData(labGrid);
		
		
		primaryColumn = new Combo(primaryGroup, SWT.READ_ONLY | SWT.DROP_DOWN);
		primaryColumn.setItems(pTable.getComboValues());
		primaryColumn.setLayoutData(textGrid);
		primaryColumn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				updateComplete();
			}
		});

		setControl(comp);
		updateComplete();
	}

	public void updateComplete(){
		String fColumn = foreignColumn.getText();
		if(fColumn.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.RelationshipAddWizardPage_6,3);
			this.setPageComplete(false);
			return;
		}
		if(primaryColumn.getText().equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.RelationshipAddWizardPage_8,3);
			this.setPageComplete(false);
			return;
		}
		if(!fTable.getColumnMap().containsKey(fColumn)){
			boolean contain = false;
			for(Column temp : fTable.getColumns()){
				if(temp.getName().equals(fColumn)){
					if(temp.isPk()){
						contain = true;
					}
					break;
				}
			}
			if(!contain){
				this.setMessage(Messages.RelationshipAddWizardPage_9,3);
				this.setPageComplete(false);
				return;
			}
		} else {
			if(!fTable.getColumnMap().get(fColumn).isPk()){
				this.setMessage(Messages.RelationshipAddWizardPage_9,3);
				this.setPageComplete(false);
				return;
			}
		}
		wizard.setForignColumn(fColumn);
		wizard.setPrimaryColumn(primaryColumn.getText());
		this.setMessage(Messages.RelationshipAddWizardPage_11,1);
		this.setPageComplete(true);
	}

	public RelationshipAddWizard getWizard() {
		return wizard;
	}
	public void setWizard(RelationshipAddWizard wizard) {
		this.wizard = wizard;
	}
}
