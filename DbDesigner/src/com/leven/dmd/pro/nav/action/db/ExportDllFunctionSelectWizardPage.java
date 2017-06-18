package com.leven.dmd.pro.nav.action.db;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.leven.dmd.gef.model.Schema;
import com.leven.dmd.gef.model.Table;
import com.leven.dmd.gef.model.Tablespace;
import com.leven.dmd.gef.tmpfile.model.DBView;
import com.leven.dmd.gef.tmpfile.model.SequenceTemplate;
import com.leven.dmd.pro.Messages;
import com.leven.dmd.pro.domain.DBTypeConstant;

public class ExportDllFunctionSelectWizardPage extends WizardPage {
	private static final String[] EXTENSIONS = new String[] { "*.ddl", "*.sql", "*.txt"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	private Combo dbTypeCombo;
	
	private Button allSeqBtn;
	private Button noSeqBtn;
	private Button partSeqBtn;
	private Button partSeqSelcBtn;
	
	private Button allViewBtn;
	private Button noViewBtn;
	private Button partViewBtn;
	private Button partViewSelcBtn;
	
	private Button allSpaceBtn;
	private Button noSpaceBtn;
	private Button partSpaceBtn;
	private Button partSpaceSelcBtn;
	
	private Button allTableBtn;
	private Button partTableBtn;
	private Button partTableSelcBtn;
	
	private Text pathText;
	private Button pathBtn;
	
	private List<SequenceTemplate> sequenceList = new ArrayList<SequenceTemplate>();
	private Map<String, SequenceTemplate> sequenceMap = new HashMap<String, SequenceTemplate>();;
	
	private List<DBView> viewList = new ArrayList<DBView>();
	private Map<String, DBView> viewMap = new HashMap<String, DBView>();;
	
	private List<Tablespace> spaceList = new ArrayList<Tablespace>();
	private Map<String, Tablespace> spaceMap = new HashMap<String, Tablespace>();;
	
	private List<Table> tableList = new ArrayList<Table>();
	private Map<String, Table> tableMap = new HashMap<String, Table>();;
	private Schema schema;

	protected ExportDllFunctionSelectWizardPage(Schema schema) {
		super("ExportDllFunctionSelectWizardPage"); //$NON-NLS-1$
		this.schema = schema;
		this.setTitle(Messages.ExportDllFunctionSelectWizardPage_4);
		this.setMessage(Messages.ExportDllFunctionSelectWizardPage_5,2);
		this.setPageComplete(false);
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(2,false));
		
		GridData labData = new GridData(70,14);
		labData.verticalAlignment = SWT.CENTER;
		GridData hFill = new GridData(GridData.FILL_HORIZONTAL);
		hFill.heightHint = 14;
		hFill.verticalAlignment = SWT.CENTER;
		
		GridData hlineData = new GridData(GridData.FILL_HORIZONTAL);
		hlineData.horizontalSpan = 2;
		hlineData.heightHint = 14;
		
		Label dbTypeLab = new Label(comp, SWT.NONE);
		dbTypeLab.setText(Messages.ExportDllFunctionSelectWizardPage_0);
		dbTypeLab.setLayoutData(labData);
		
		dbTypeCombo = new Combo(comp, SWT.READ_ONLY);
		dbTypeCombo.setItems(DBTypeConstant.dbTypeArray);
		dbTypeCombo.setLayoutData(hFill);
		dbTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		Label tableLine = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
		tableLine.setLayoutData(hlineData);

		RowLayout seqLayout = new RowLayout();
		seqLayout.center = true;
		GridData seqCompData = new GridData(GridData.FILL_HORIZONTAL);
		seqCompData.horizontalSpan = 2;
		seqCompData.heightHint = 30;
		
		Composite tableComp = new Composite(comp, SWT.NONE);
		tableComp.setLayoutData(seqCompData);
		tableComp.setLayout(seqLayout);
		
		Label tableLab = new Label(tableComp, SWT.NONE);
		tableLab.setText(Messages.ExportDllFunctionSelectWizardPage_7);
		allTableBtn = new Button(tableComp, SWT.RADIO);
		allTableBtn.setSelection(true);
		allTableBtn.setText(Messages.ExportDllFunctionSelectWizardPage_8);
		allTableBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partTableSelcBtn.setEnabled(!allTableBtn.getSelection());
			}
		});
		
		partTableBtn = new Button(tableComp, SWT.RADIO);
		partTableBtn.setSelection(false);
		partTableBtn.setText(Messages.ExportDllFunctionSelectWizardPage_9);
		partTableBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partTableSelcBtn.setEnabled(partTableBtn.getSelection());
			}
		});
		
		partTableSelcBtn = new Button(tableComp, SWT.PUSH);
		partTableSelcBtn.setText(Messages.ExportDllFunctionSelectWizardPage_10);
		partTableSelcBtn.setEnabled(false);
		partTableSelcBtn.setLayoutData(new RowData(40,23));
		partTableSelcBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openTableSelectDialog();
			}
		});
		
		
		Label hLine = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
		hLine.setLayoutData(hlineData);
		
		Composite seqComp = new Composite(comp, SWT.NONE);
		seqComp.setLayoutData(seqCompData);
		seqComp.setLayout(seqLayout);
		
		Label seqLab = new Label(seqComp, SWT.NONE);
		seqLab.setText(Messages.ExportDllFunctionSelectWizardPage_11);
		allSeqBtn = new Button(seqComp, SWT.RADIO);
		allSeqBtn.setSelection(true);
		allSeqBtn.setText(Messages.ExportDllFunctionSelectWizardPage_8);
		allSeqBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partSeqSelcBtn.setEnabled(!allSeqBtn.getSelection());
			}
		});
		
		noSeqBtn = new Button(seqComp, SWT.RADIO);
		noSeqBtn.setSelection(false);
		noSeqBtn.setText(Messages.ExportDllFunctionSelectWizardPage_13);
		noSeqBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partSeqSelcBtn.setEnabled(!noSeqBtn.getSelection());
			}
		});
		
		partSeqBtn = new Button(seqComp, SWT.RADIO);
		partSeqBtn.setSelection(false);
		partSeqBtn.setText(Messages.ExportDllFunctionSelectWizardPage_9);
		partSeqBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partSeqSelcBtn.setEnabled(partSeqBtn.getSelection());
			}
		});
		
		partSeqSelcBtn = new Button(seqComp, SWT.PUSH);
		partSeqSelcBtn.setText(Messages.ExportDllFunctionSelectWizardPage_15);
		partSeqSelcBtn.setEnabled(false);
		partSeqSelcBtn.setLayoutData(new RowData(40,23));
		partSeqSelcBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openSequenceDialog();
			}
		});
		//视图选项
		new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(hlineData);
		
		Composite viewComp = new Composite(comp, SWT.NONE);
		viewComp.setLayoutData(seqCompData);
		viewComp.setLayout(seqLayout);
		
		Label viewLab = new Label(viewComp, SWT.NONE);
		viewLab.setText(Messages.ExportDllFunctionSelectWizardPage_1);
		allViewBtn = new Button(viewComp, SWT.RADIO);
		allViewBtn.setSelection(true);
		allViewBtn.setText(Messages.ExportDllFunctionSelectWizardPage_8);
		allViewBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partViewSelcBtn.setEnabled(!allViewBtn.getSelection());
			}
		});
		
		noViewBtn = new Button(viewComp, SWT.RADIO);
		noViewBtn.setSelection(false);
		noViewBtn.setText(Messages.ExportDllFunctionSelectWizardPage_13);
		noViewBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partViewSelcBtn.setEnabled(!noViewBtn.getSelection());
			}
		});
		
		partViewBtn = new Button(viewComp, SWT.RADIO);
		partViewBtn.setSelection(false);
		partViewBtn.setText(Messages.ExportDllFunctionSelectWizardPage_9);
		partViewBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partViewSelcBtn.setEnabled(partViewBtn.getSelection());
			}
		});
		
		partViewSelcBtn = new Button(viewComp, SWT.PUSH);
		partViewSelcBtn.setText(Messages.ExportDllFunctionSelectWizardPage_15);
		partViewSelcBtn.setEnabled(false);
		partViewSelcBtn.setLayoutData(new RowData(40,23));
		partViewSelcBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openViewDialog();
			}
		});
		//表空间选项
		new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(hlineData);
		
		Composite spaceComp = new Composite(comp, SWT.NONE);
		spaceComp.setLayoutData(seqCompData);
		spaceComp.setLayout(seqLayout);
		
		Label spaceLab = new Label(spaceComp, SWT.NONE);
		spaceLab.setText(Messages.ExportDllFunctionSelectWizardPage_2);
		allSpaceBtn = new Button(spaceComp, SWT.RADIO);
		allSpaceBtn.setSelection(false);
		allSpaceBtn.setText(Messages.ExportDllFunctionSelectWizardPage_8);
		allSpaceBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partSpaceSelcBtn.setEnabled(!allSpaceBtn.getSelection());
			}
		});
		
		noSpaceBtn = new Button(spaceComp, SWT.RADIO);
		noSpaceBtn.setSelection(true);
		noSpaceBtn.setText(Messages.ExportDllFunctionSelectWizardPage_13);
		noSpaceBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partSpaceSelcBtn.setEnabled(!noSpaceBtn.getSelection());
			}
		});
		
		partSpaceBtn = new Button(spaceComp, SWT.RADIO);
		partSpaceBtn.setSelection(false);
		partSpaceBtn.setText(Messages.ExportDllFunctionSelectWizardPage_9);
		partSpaceBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				partSpaceSelcBtn.setEnabled(partSpaceBtn.getSelection());
			}
		});
		
		partSpaceSelcBtn = new Button(spaceComp, SWT.PUSH);
		partSpaceSelcBtn.setText(Messages.ExportDllFunctionSelectWizardPage_15);
		partSpaceSelcBtn.setEnabled(false);
		partSpaceSelcBtn.setLayoutData(new RowData(40,23));
		partSpaceSelcBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openSpaceDialog();
			}
		});
		
		Label pathLine = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
		pathLine.setLayoutData(hlineData);
		
		Composite pathComp = new Composite(comp, SWT.NONE);
		pathComp.setLayoutData(seqCompData);
		pathComp.setLayout(new GridLayout(3,false));
		
		Label pathLab = new Label(pathComp, SWT.NONE);
		pathLab.setText(Messages.ExportDllFunctionSelectWizardPage_16);
		pathLab.setLayoutData(new GridData(60,14));
		
		GridData txtData = new GridData(GridData.FILL_HORIZONTAL);
		
		pathText = new Text(pathComp, SWT.BORDER);
		pathText.setLayoutData(txtData);
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateComplete();
			}
		});
		
		pathBtn = new Button(pathComp, SWT.PUSH);
		pathBtn.setText(Messages.ExportDllFunctionSelectWizardPage_17);
		pathBtn.setLayoutData(new GridData(40,24));
		pathBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openPathDialog();
			}
		});
		
		setControl(comp);
	}
	
	private void openSequenceDialog(){
		ExportDllSequenceSelectWizard wizard = new ExportDllSequenceSelectWizard(schema,sequenceList,sequenceMap);
		WizardDialog dialog = new WizardDialog(this.getShell(),wizard);
		dialog.create();
		dialog.open();
	}
	
	private void openSpaceDialog(){
		ExportDllTablespaceSelectWizard wizard = new ExportDllTablespaceSelectWizard(schema,spaceList,spaceMap);
		WizardDialog dialog = new WizardDialog(this.getShell(),wizard);
		dialog.create();
		dialog.open();
	}
	
	private void openViewDialog(){
		ExportDllViewSelectWizard wizard = new ExportDllViewSelectWizard(schema,viewList,viewMap);
		WizardDialog dialog = new WizardDialog(this.getShell(),wizard);
		dialog.create();
		dialog.open();
	}
	
	private void openTableSelectDialog(){
		ExportDllTableSelectWizard wizard = new ExportDllTableSelectWizard(schema,tableList,tableMap);
		WizardDialog dialog = new WizardDialog(this.getShell(),wizard);
		dialog.create();
		dialog.open();
	}
	
	private void openPathDialog(){
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(Messages.ExportDllFunctionSelectWizardPage_18);
		dialog.setOverwrite(true);
		String savePath = null;
		try {
			savePath = dialog.open(); //��ȡ����·��
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					Messages.ExportDllFunctionSelectWizardPage_18, Messages.ExportDllFunctionSelectWizardPage_20);
			return;
		}
		if(savePath!=null){
			pathText.setText(savePath);
		}
	}
	
	private void updateComplete(){
		String path = pathText.getText().trim();
		String dbType = dbTypeCombo.getText();
		if(dbType.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.ExportDllFunctionSelectWizardPage_22,3);
			this.setPageComplete(false);
			return;
		}
		if(path.equals("")){ //$NON-NLS-1$
			this.setMessage(Messages.ExportDllFunctionSelectWizardPage_24,3);
			this.setPageComplete(false);
			return;
		}else if(!path.endsWith(".txt") && !path.endsWith(".sql") && !path.endsWith(".ddl")){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.setMessage(Messages.ExportDllFunctionSelectWizardPage_28,3);
			this.setPageComplete(false);
			return;
		} else {
			File f = new File(path);
			if(!f.exists() && (f.getParentFile() ==null || !f.getParentFile().exists())){
				this.setMessage(Messages.ExportDllFunctionSelectWizardPage_28,3);
				this.setPageComplete(false);
				return;
			}
		}
		this.setMessage("OK!",1); //$NON-NLS-1$
		this.setPageComplete(true);
		/*IWizardPage page = getNextPage();
		if (page!=null && page.getControl() != null){
			page.dispose();
		}*/
	}
	
	@Override
	public IWizardPage getNextPage() {
		return getWizard().getNextPage(this);
	}

	public Combo getDbTypeCombo() {
		return dbTypeCombo;
	}

	public void setDbTypeCombo(Combo dbTypeCombo) {
		this.dbTypeCombo = dbTypeCombo;
	}
	public Button getAllSeqBtn() {
		return allSeqBtn;
	}
	public void setAllSeqBtn(Button allSeqBtn) {
		this.allSeqBtn = allSeqBtn;
	}
	public Button getNoSeqBtn() {
		return noSeqBtn;
	}
	public void setNoSeqBtn(Button noSeqBtn) {
		this.noSeqBtn = noSeqBtn;
	}
	public Button getPartSeqBtn() {
		return partSeqBtn;
	}
	public void setPartSeqBtn(Button partSeqBtn) {
		this.partSeqBtn = partSeqBtn;
	}
	public Button getAllTableBtn() {
		return allTableBtn;
	}
	public void setAllTableBtn(Button allTableBtn) {
		this.allTableBtn = allTableBtn;
	}
	public Button getPartTableBtn() {
		return partTableBtn;
	}
	public void setPartTableBtn(Button partTableBtn) {
		this.partTableBtn = partTableBtn;
	}
	public Text getPathText() {
		return pathText;
	}
	public void setPathText(Text pathText) {
		this.pathText = pathText;
	}
	public List<SequenceTemplate> getSequenceList() {
		return sequenceList;
	}
	public void setSequenceList(List<SequenceTemplate> sequenceList) {
		this.sequenceList = sequenceList;
	}
	public List<Table> getTableList() {
		return tableList;
	}
	public void setTableList(List<Table> tableList) {
		this.tableList = tableList;
	}
	public Button getAllViewBtn() {
		return allViewBtn;
	}
	public Button getNoViewBtn() {
		return noViewBtn;
	}
	public Button getAllSpaceBtn() {
		return allSpaceBtn;
	}
	public Button getNoSpaceBtn() {
		return noSpaceBtn;
	}
	public Button getPartViewBtn() {
		return partViewBtn;
	}
	public Button getPartSpaceBtn() {
		return partSpaceBtn;
	}
	public List<DBView> getViewList() {
		return viewList;
	}
	public void setViewList(List<DBView> viewList) {
		this.viewList = viewList;
	}
	public List<Tablespace> getSpaceList() {
		return spaceList;
	}
	public void setSpaceList(List<Tablespace> spaceList) {
		this.spaceList = spaceList;
	}
}
