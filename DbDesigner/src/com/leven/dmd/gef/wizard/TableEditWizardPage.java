package com.leven.dmd.gef.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;
import com.leven.dmd.gef.model.Relationship;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.TableIndex;
import com.leven.dmd.gef.util.ImageKeys;
import com.leven.dmd.gef.util.control.CustomTabFolder;
import com.leven.dmd.gef.wizard.tableeditor.CustomIndexViewer;
import com.leven.dmd.gef.wizard.tableeditor.CustomTableViewer;
import com.leven.dmd.pro.Activator;
/**
 * ������ݱ����ҳ
 * @author lifeng
 * 2012-8-15 ����01:37:59
 */
public class TableEditWizardPage extends WizardPage {
	private TableEditWizard wizard;
	private ArrayList<Column> columns;
	private ArrayList<TableIndex> indexes;
	private String oldName;
	
	private Table table;
	
	private org.eclipse.swt.widgets.Table columnTable;
	private Text tableName;
	private Text tableCnName;
	private Text tableDescription;
	private Text tableSeqno;
	
	private Text tableSpace;
	private Text tableOtherSql;
	private CustomIndexViewer indexViewer;
	private CustomTableViewer columnViewer;
	
	protected TableEditWizardPage(String pageName,TableEditWizard wizard) {
		super(pageName);
		this.table = wizard.getTable();
		this.oldName = wizard.getTableName();
		columns = wizard.getColumns();
		indexes = wizard.getIndexes();
		this.wizard = wizard;
		this.setTitle(wizard.getTableName());
		this.setMessage(Messages.TableEditWizardPage_0 + wizard.getTableName() + Messages.TableEditWizardPage_1, 1);
		this.setPageComplete(true);
	}

	public void createControl(final Composite parent) {
		CustomTabFolder tabFolder = new CustomTabFolder(parent);
        tabFolder.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
// ��񲼾���ʽ����
		GridLayout layout1 = new GridLayout(1, false);
		layout1.verticalSpacing = 0;
		GridLayout layout2 = new GridLayout(2, false);

		GridData labGrid = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		labGrid.widthHint = 50;
		labGrid.heightHint = 23;

		GridData btnCompGrid = new GridData(GridData.FILL_VERTICAL);
		btnCompGrid.widthHint = 70;
		btnCompGrid.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;

		GridData textGrid = new GridData(GridData.FILL_BOTH);
		
		GridData textInputGrid = new GridData(GridData.FILL_HORIZONTAL);
		textInputGrid.heightHint = 30;

		GridData btnGrid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		btnGrid.widthHint = 60;
// �����Ϣtabҳ
		CTabItem mainItem = new CTabItem(tabFolder, SWT.NONE);
		mainItem.setText(Messages.TableEditWizardPage_2);
		mainItem.setImage(Activator.getImage(ImageKeys.TABLE_MODEL));

		Group mainGroup = new Group(tabFolder, SWT.NONE);
		mainGroup.setLayout(layout1);
		
		Group nameGroup = new Group(mainGroup, SWT.NONE);
		nameGroup.setLayout(layout2);
		nameGroup.setLayoutData(textInputGrid);
		
		Label tableNameLab = new Label(nameGroup, SWT.NONE);
		tableNameLab.setText(Messages.TableEditWizardPage_3);
		tableNameLab.setLayoutData(labGrid);
		
		tableName = new Text(nameGroup, SWT.BORDER);
		tableName.setText(wizard.getTableName()==null?"":wizard.getTableName()); //$NON-NLS-1$
		tableName.setLayoutData(textGrid);
		tableName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		//
		Group cnNameGroup = new Group(mainGroup, SWT.NONE);
		cnNameGroup.setLayout(layout2);
		cnNameGroup.setLayoutData(textInputGrid);
		
		Label tableCnNameLab = new Label(cnNameGroup, SWT.NONE);
		tableCnNameLab.setText(Messages.TableEditWizardPage_5);
		tableCnNameLab.setLayoutData(labGrid);
		
		tableCnName = new Text(cnNameGroup, SWT.BORDER);
		tableCnName.setText(wizard.getTableCnName()==null?"":wizard.getTableCnName()); //$NON-NLS-1$
		tableCnName.setLayoutData(textGrid);
		tableCnName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});

		//
		Group seqnoGroup = new Group(mainGroup, SWT.NONE);
		seqnoGroup.setLayout(layout2);
		seqnoGroup.setLayoutData(textInputGrid);
		
		Label tableSeqnoLab = new Label(seqnoGroup, SWT.NONE);
		tableSeqnoLab.setText(Messages.TableEditWizardPage_14);
		tableSeqnoLab.setLayoutData(labGrid);
		
		tableSeqno = new Text(seqnoGroup, SWT.BORDER);
		tableSeqno.setText(wizard.getTableSeqno()+""); //$NON-NLS-1$
		tableSeqno.setLayoutData(textGrid);
		tableSeqno.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				if(!tableSeqno.getText().equals("") && tableSeqno.getText().matches("[0-9]*")){ //$NON-NLS-1$ //$NON-NLS-2$
					wizard.setTableSeqno(Integer.parseInt(tableSeqno.getText().trim()));
					updateComplete();
				}else {
					TableEditWizardPage.this.setMessage(Messages.TableEditWizardPage_19, 3);
					TableEditWizardPage.this.setPageComplete(false);
				}
			}
		});
		//
		Group descriptionGroup = new Group(mainGroup, SWT.NONE);
		descriptionGroup.setLayout(layout2);
		descriptionGroup.setLayoutData(textGrid);
		
		Label tableDescriptionLab = new Label(descriptionGroup, SWT.NONE);
		tableDescriptionLab.setText(Messages.TableEditWizardPage_7);
		GridData despGrid = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		despGrid.widthHint = 50;
		despGrid.heightHint = 80;
		despGrid.horizontalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		tableDescriptionLab.setLayoutData(despGrid);
		
		tableDescription = new Text(descriptionGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		tableDescription.setText(wizard.getTableDescription()==null?"":wizard.getTableDescription());//$NON-NLS-1$
		tableDescription.setLayoutData(textGrid);
		tableDescription.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				wizard.setTableDescription(tableDescription.getText().trim());
			}
		});
		
		mainItem.setControl(mainGroup);
// �ֶ���Ϣtabҳ
		CTabItem columnItem = new CTabItem(tabFolder, SWT.NONE);
		columnItem.setText(Messages.TableEditWizardPage_9);
		columnItem.setImage(Activator.getImage(ImageKeys.COLUMN_MODEL));
		ArrayList<String> associates = new ArrayList<String>();
		for(Iterator<Relationship> it = wizard.getTable().getForeignKeyRelationships().iterator();it.hasNext();){
			associates.add(it.next().getPrimaryKeyColumn());
		}
		for(Iterator<Relationship> it = wizard.getTable().getPrimaryKeyRelationships().iterator();it.hasNext();){
			associates.add(it.next().getForeignKeyColumn());
		}
		columnViewer = new CustomTableViewer(tabFolder,columns,associates,wizard.getSchema(),tableName,!table.isQuote(),indexViewer);
		columnItem.setControl(columnViewer);
		
		
// ������Ϣtabҳ
		CTabItem indexItem = new CTabItem(tabFolder, SWT.NONE);
		indexItem.setText(Messages.TableEditWizardPage_10);
		indexItem.setImage(Activator.getImage(ImageKeys.INDEX_MODEL));
		
		indexViewer = new CustomIndexViewer(tabFolder,indexes,columnViewer.getViewer(),!table.isQuote());
		indexItem.setControl(indexViewer);
		columnViewer.setIndexViewer(indexViewer);
//表空间
		CTabItem spaceItem = new CTabItem(tabFolder, SWT.NONE);
		spaceItem.setText(Messages.TableEditWizardPage_4);
		spaceItem.setImage(Activator.getImage(ImageKeys.TABLESPACE));
		Group spaceGroup = new Group(tabFolder, SWT.NONE);
		spaceGroup.setLayout(layout2);
		Label spaceLab = new Label(spaceGroup, SWT.NONE);
		spaceLab.setText(Messages.TableEditWizardPage_6);
		spaceLab.setLayoutData(labGrid);
		tableSpace = new Text(spaceGroup, SWT.BORDER);
		tableSpace.setText(wizard.getTableSpace()==null?"":wizard.getTableSpace()); //$NON-NLS-1$
		tableSpace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableSpace.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		if(table.isQuote()){
			tableSpace.setEditable(false);
		}
		Label otherSqlLab = new Label(spaceGroup, SWT.NONE);
		otherSqlLab.setText(Messages.TableEditWizardPage_15);
		otherSqlLab.setLayoutData(labGrid);
		tableOtherSql = new Text(spaceGroup, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		tableOtherSql.setText(wizard.getOtherSql()==null?"":wizard.getOtherSql()); //$NON-NLS-1$
		tableOtherSql.setLayoutData(textGrid);
		tableOtherSql.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		if(table.isQuote()){
			tableOtherSql.setEditable(false);
		}
		spaceItem.setControl(spaceGroup);

		setControl(tabFolder);
	}

	public void updateComplete(){
		String name = tableName.getText().trim();

		MessageBox messageBox = new MessageBox(this.getShell(),SWT.ICON_WARNING);
		messageBox.setText(Messages.TableEditWizardPage_11);
		List schemaTables = wizard.getSchema().getTables();
		for(int i=0;i<schemaTables.size();i++){
			if(name.equals(((Table)schemaTables.get(i)).getName()) && !name.equals(oldName)){
				this.setMessage(Messages.TableEditWizardPage_12+name+Messages.TableEditWizardPage_13,3);
				this.setPageComplete(false);
				return ;
			}
		}
		
		String reg = ".*[\\/\\\\\\?:\\*<>|\"!^()-=\\+{}]{1}.*"; //$NON-NLS-1$
		if(name.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.TableEditWizardPage_16,3);
			this.setPageComplete(false);
			return ;
		}else if(tableCnName.getText().trim().equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.TableEditWizardPage_18,3);
			this.setPageComplete(false);
			return ;
		}  else if(name.matches(reg)){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.setMessage(Messages.TableEditWizardPage_22,3);
			this.setPageComplete(false);
			return ;
		}
		wizard.setTableName(name);
		wizard.setTableCnName(tableCnName.getText().trim());
		wizard.setColumns(columns);
		wizard.setTableSpace(tableSpace.getText().trim());
		wizard.setOtherSql(tableOtherSql.getText().trim());
		this.setMessage(Messages.TableEditWizardPage_23,1);
		this.setPageComplete(true);
	}
	
	public ArrayList<Column> getColumns() {
		return columns;
	}
	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}
	public org.eclipse.swt.widgets.Table getColumnTable() {
		return columnTable;
	}
	public void setColumnTable(org.eclipse.swt.widgets.Table columnTable) {
		this.columnTable = columnTable;
	}
	public TableEditWizard getWizard() {
		return wizard;
	}
	public void setWizard(TableEditWizard wizard) {
		this.wizard = wizard;
	}
}