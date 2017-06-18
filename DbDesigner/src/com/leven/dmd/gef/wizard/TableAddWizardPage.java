package com.leven.dmd.gef.wizard;

import java.util.ArrayList;
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
public class TableAddWizardPage extends WizardPage {
	private TableAddWizard wizard;
	private Table table;
	private ArrayList<Column> columns;
	private ArrayList<TableIndex> indexes;
	
	private org.eclipse.swt.widgets.Table columnTable;
	private Text tableName;
	private Text tableCnName;
	private Text tableDescription;
	private Text tableSeqno;
	
	private Text tableSpace;
	private Text tableOtherSql;
	
	private CustomIndexViewer indexViewer;
	private CustomTableViewer columnViewer;
	
	protected TableAddWizardPage(String pageName,TableAddWizard wizard) {
		super(pageName);
		this.table = wizard.getTable();
		columns = table.getColumns();
		indexes = table.getIndexes();
		this.wizard = wizard;
		this.setTitle(Messages.TableAddWizardPage_0);
		this.setMessage(Messages.TableAddWizardPage_1, 1);
		this.setPageComplete(false);
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
		textInputGrid.heightHint = 25;
		
		GridData groupGrid = new GridData(GridData.FILL_HORIZONTAL);
		groupGrid.heightHint = 35;

		GridData btnGrid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		btnGrid.widthHint = 60;
// �����Ϣtabҳ
		CTabItem mainItem = new CTabItem(tabFolder, SWT.NONE);
		mainItem.setText(Messages.TableAddWizardPage_2);
		mainItem.setImage(Activator.getImage(ImageKeys.TABLE_MODEL));

		Group mainGroup = new Group(tabFolder, SWT.NONE);
		mainGroup.setLayout(layout1);
		
		Group nameGroup = new Group(mainGroup, SWT.NONE);
		nameGroup.setLayout(layout2);
		nameGroup.setLayoutData(groupGrid);
		
		Label tableNameLab = new Label(nameGroup, SWT.NONE);
		tableNameLab.setText(Messages.TableAddWizardPage_3);
		tableNameLab.setLayoutData(labGrid);
		tableName = new Text(nameGroup, SWT.BORDER);
		tableName.setText(table.getName()==null?"":table.getName()); //$NON-NLS-1$
		tableName.setLayoutData(textGrid);
		tableName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		//
		Group cnNameGroup = new Group(mainGroup, SWT.NONE);
		cnNameGroup.setLayout(layout2);
		cnNameGroup.setLayoutData(groupGrid);
		
		Label tableCnNameLab = new Label(cnNameGroup, SWT.NONE);
		tableCnNameLab.setText(Messages.TableAddWizardPage_5);
		tableCnNameLab.setLayoutData(labGrid);
		
		tableCnName = new Text(cnNameGroup, SWT.BORDER);
		tableCnName.setText(table.getCnName()==null?"":table.getCnName()); //$NON-NLS-1$
		tableCnName.setLayoutData(textGrid);
		tableCnName.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		
		//
		Group seqnoGroup = new Group(mainGroup, SWT.NONE);
		seqnoGroup.setLayout(layout2);
		seqnoGroup.setLayoutData(groupGrid);
		
		Label tableSeqnoLab = new Label(seqnoGroup, SWT.NONE);
		tableSeqnoLab.setText(Messages.TableAddWizardPage_15);
		tableSeqnoLab.setLayoutData(labGrid);
		
		tableSeqno = new Text(seqnoGroup, SWT.BORDER);
		tableSeqno.setText("0"); //$NON-NLS-1$
		tableSeqno.setLayoutData(textGrid);
		tableSeqno.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				if(!tableSeqno.getText().equals("") && tableSeqno.getText().matches("[0-9]*")){ //$NON-NLS-1$ //$NON-NLS-2$
					wizard.setTableSeqno(tableSeqno.getText().trim());
					updateComplete();
				}else {
					TableAddWizardPage.this.setMessage(Messages.TableAddWizardPage_21, 3);
					TableAddWizardPage.this.setPageComplete(false);
				}
			}
		});
		//
		Group descriptionGroup = new Group(mainGroup, SWT.NONE);
		descriptionGroup.setLayout(layout2);
		descriptionGroup.setLayoutData(textGrid);
		
		Label tableDescriptionLab = new Label(descriptionGroup, SWT.NONE);
		tableDescriptionLab.setText(Messages.TableAddWizardPage_7);
		GridData despGrid = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		despGrid.widthHint = 50;
		despGrid.heightHint = 80;
		despGrid.horizontalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		tableDescriptionLab.setLayoutData(despGrid);
		
		tableDescription = new Text(descriptionGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		tableDescription.setText(table.getDescription()==null?"":table.getDescription());//$NON-NLS-1$
		tableDescription.setLayoutData(textGrid);
		tableDescription.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				wizard.setTableDescription(tableDescription.getText().trim());
			}
		});
		
		mainItem.setControl(mainGroup);
// �ֶ���Ϣtabҳ
		CTabItem columnItem = new CTabItem(tabFolder, SWT.NONE);
		columnItem.setText(Messages.TableAddWizardPage_9);
		columnItem.setImage(Activator.getImage(ImageKeys.COLUMN_MODEL));
		columnViewer = new CustomTableViewer(tabFolder,columns,null,wizard.getSchema(),tableName,!table.isQuote(),indexViewer);
		columnItem.setControl(columnViewer);
		
//������Ϣtabҳ
		CTabItem indexItem = new CTabItem(tabFolder, SWT.NONE);
		indexItem.setText(Messages.TableAddWizardPage_10);
		indexItem.setImage(Activator.getImage(ImageKeys.INDEX_MODEL));
		indexViewer = new CustomIndexViewer(tabFolder,indexes,columnViewer.getViewer(),!table.isQuote());
		indexItem.setControl(indexViewer);
		columnViewer.setIndexViewer(indexViewer);
//表空间分页
		CTabItem spaceItem = new CTabItem(tabFolder, SWT.NONE);
		spaceItem.setText(Messages.TableAddWizardPage_4);
		spaceItem.setImage(Activator.getImage(ImageKeys.TABLESPACE));
		Group spaceGroup = new Group(tabFolder, SWT.NONE);
		spaceGroup.setLayout(layout2);
		Label spaceLab = new Label(spaceGroup, SWT.NONE);
		spaceLab.setText(Messages.TableAddWizardPage_6);
		spaceLab.setLayoutData(labGrid);
		tableSpace = new Text(spaceGroup, SWT.BORDER);
		tableSpace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if(table.isQuote()){
			tableSpace.setEditable(false);
		}
		tableSpace.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent arg0) {
				updateComplete();
			}
		});
		Label otherSqlLab = new Label(spaceGroup, SWT.NONE);
		otherSqlLab.setText(Messages.TableAddWizardPage_17);
		otherSqlLab.setLayoutData(labGrid);
		tableOtherSql = new Text(spaceGroup, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
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
		updateComplete();
	}

	public void updateComplete(){
		String name = tableName.getText().trim();

		MessageBox messageBox = new MessageBox(this.getShell(),SWT.ICON_WARNING);
		messageBox.setText(Messages.TableAddWizardPage_11);
		List<Table> schemaTables = wizard.getSchema().getTables();
		for(int i=0;i<schemaTables.size();i++){
			if(name.equals(((Table)schemaTables.get(i)).getName())){
				this.setMessage(Messages.TableAddWizardPage_12+name+Messages.TableAddWizardPage_13,3);
				this.setPageComplete(false);
				return ;
			}
		}
		
		String reg = Messages.TableAddWizardPage_14;
		if(name.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.TableAddWizardPage_16,3);
			this.setPageComplete(false);
			return ;
		}else if(tableCnName.getText().trim().equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.TableAddWizardPage_18,3);
			this.setPageComplete(false);
			return ;
		}  else if(name.indexOf(" ")>=0 || name.replaceAll("\\d", "").matches(reg)){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.setMessage(Messages.TableAddWizardPage_22,3);
			this.setPageComplete(false);
			return ;
		}
		this.getTable().setName(name);
		this.getTable().setCnName(tableCnName.getText().trim());
		this.getTable().setDescription(tableDescription.getText().trim());
		this.getTable().setColumns(getColumns());
		this.getTable().setTablespace(tableSpace.getText().trim());
		this.getTable().setOtherSql(tableOtherSql.getText().trim());
		table.setColumns(columns);
		this.setMessage(Messages.TableAddWizardPage_23,1);
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
	public TableAddWizard getWizard() {
		return wizard;
	}
	public void setWizard(TableAddWizard wizard) {
		this.wizard = wizard;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public ArrayList<TableIndex> getIndexes() {
		return indexes;
	}
	public void setIndexes(ArrayList<TableIndex> indexes) {
		this.indexes = indexes;
	}
}