package com.leven.dmd.pro.util.control.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.domain.TableDomain;

public class DBTableSelectDialog extends TitleAreaDialog {
	
	private Tree tableTree;
	
	private List<TableDomain> userTableDomainList;
	
	private Text chooseTableText;
	
	private int treeType;

	public DBTableSelectDialog(Shell parentShell,List<TableDomain> userTableDomainList
			,Text chooseTableText,int treeType) {
		super(parentShell);
		this.setUserTableDomainList(userTableDomainList);
		this.chooseTableText = chooseTableText;
		this.treeType = treeType;
	}
	
	public void create() {
		super.create();
		this.setTitle(Messages.UserTableDialogForMainTable_0);
		this.setTitleImage(Activator.getImage("icons/dhcc/dhcc_wizard.png")); //$NON-NLS-1$
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1,false));
		
		GridData tableTreeGridDate = new GridData(GridData.FILL_BOTH);
		tableTreeGridDate.widthHint = 480;
		tableTreeGridDate.heightHint = 260;
		
		tableTree = new Tree(container, treeType);
		tableTree.setLayoutData(tableTreeGridDate);
		tableTree.setHeaderVisible(true);
		tableTree.setLinesVisible(true);
		
		TreeItem child;
		for(int i=0;i<this.getUserTableDomainList().size();i++){
			child = new TreeItem(tableTree, SWT.NONE);
			
			String tablename = this.getUserTableDomainList().get(i).getTableName();
			String tablechinesename = this.getUserTableDomainList().get(i).getCnName();
			StringBuffer content = new StringBuffer();
			content.append(tablename);
			if(!tablename.equals(tablechinesename)){
				content.append("(").append(tablechinesename).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
				
			child.setText(content.toString());
		}
		
		return container;
	}

	@Override
	protected void okPressed() {
		TreeItem[] items = tableTree.getSelection();
		String value = ""; //$NON-NLS-1$
		List<TableDomain> list = new ArrayList<TableDomain>();
		TableDomain temp = null;
		for(int i=0;i<items.length;i++){
			temp = new TableDomain();
			String $value = items[i].getText();
			if($value.indexOf("(")>-1){ //$NON-NLS-1$
				temp.setTableName($value.substring(0,$value.indexOf("("))); //$NON-NLS-1$
				temp.setCnName($value.substring($value.indexOf("("))); //$NON-NLS-1$
			} else {
				temp.setTableName($value);
				temp.setCnName(""); //$NON-NLS-1$
			}
			list.add(temp);
			if(i == (items.length-1)){
				value += $value;
			}else{
				value += $value + ","; //$NON-NLS-1$
			}
		}
		chooseTableText.setText(value);
		super.okPressed();
	}

	protected boolean isResizable() {
		return false;
	}

	public List<TableDomain> getUserTableDomainList() {
		return userTableDomainList;
	}

	public void setUserTableDomainList(List<TableDomain> userTableDomainList) {
		this.userTableDomainList = userTableDomainList;
	}
}
