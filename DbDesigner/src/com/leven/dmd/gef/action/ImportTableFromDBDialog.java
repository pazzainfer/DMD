package com.leven.dmd.gef.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.pro.db.DBOperator;
import com.leven.dmd.pro.domain.TableDomain;
import com.leven.dmd.pro.pref.model.DBConfigModel;
import com.leven.dmd.pro.pref.util.DBConfigUtil;

public class ImportTableFromDBDialog extends TitleAreaDialog {
	
	private Tree tableTree;
	
	private DBConfigModel config;
	private boolean isMulti;
	private Text searchText;
	
	private List<TableDomain> allTableList = new ArrayList<TableDomain>();
	private ArrayList<String[]> data = new ArrayList<String[]>();

	public ImportTableFromDBDialog(Shell parentShell,DBConfigModel config,boolean isMulti) {
		super(parentShell);
		this.isMulti = isMulti;
		this.config = config;
		this.allTableList = getTables(config);
	}
	
	@Override
	public void create() {
		super.create();
		this.setTitle(Messages.ImportTableFromDBDialog_0);
		this.setMessage(Messages.ImportTableFromDBDialog_1, 2);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(Messages.ImportTableFromDBDialog_2);
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1,false));
		
		searchText = new Text(container, SWT.BORDER);
		GridData textDate = new GridData(GridData.FILL_HORIZONTAL);
		searchText.setLayoutData(textDate);
		searchText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				searchTable();
			}
		});
		
		GridData tableTreeGridDate = new GridData(GridData.FILL_BOTH);
		tableTreeGridDate.widthHint = 480;
		tableTreeGridDate.heightHint = 260;
		int type;
		if(isMulti){
			type = SWT.BORDER | SWT.MULTI;
		}else {
			type = SWT.BORDER;
		}
		tableTree = new Tree(container, type);
		tableTree.setLayoutData(tableTreeGridDate);
		tableTree.setLinesVisible(true);
		
		TreeItem child;
		TableDomain temp;
		for(int i=0;i<allTableList.size();i++){
			child = new TreeItem(tableTree, SWT.NONE);
			temp = allTableList.get(i);
			String cnName = (temp.getCnName()==null||"".equals(temp.getCnName()))?"":("("+temp.getTableName()+")");
			child.setText(temp.getTableName() + cnName);
		}
		
		return container;
	}
	
	private List<TableDomain> getTables(DBConfigModel config){
		Connection conn = null;
		ResultSet rs = null;
		List<TableDomain> tables = new ArrayList<TableDomain>();
		try {
			conn = DBConfigUtil.getConnection(config);
			return DBOperator.getTableList(config, conn);
		} catch (Exception e) {
			return tables;
		} finally{
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(conn!=null){
					conn.close();
					conn=null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void searchTable() {
		String str = searchText.getText().trim();
		if(!"".equals(str)){ //$NON-NLS-1$
			StringBuffer sb = new StringBuffer();
			for(char c : str.toCharArray()){
				if(c>='a' && c <='z'){
					sb.append("[" + c  + ((char)(c-32)) + "]{1}"); //$NON-NLS-1$ //$NON-NLS-2$
				}else if(c>='A' && c <='Z'){
					sb.append("[" + c  + ((char)(c+32)) + "]{1}"); //$NON-NLS-1$ //$NON-NLS-2$
				}else {
					sb.append(c);
				}
			}
			String reg = sb.toString().replaceAll("\\*",".*").replaceAll("\\?",".{1}").replaceAll("\\(","\\(").replaceAll("\\)","\\)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			reg = "^" + reg + ".*$"; //$NON-NLS-1$ //$NON-NLS-2$
			
			tableTree.removeAll();
			TreeItem child;
			for(int i=0;i<this.getAllTableList().size();i++){
				String tablename = this.getAllTableList().get(i).getTableName();
				String cnName = this.getAllTableList().get(i).getCnName();
				boolean match = false;
				try{
					match = tablename.matches(reg) || cnName.matches(reg);
				} catch(Exception e){
					match = false;
				}
				if(match){
					child = new TreeItem(tableTree, SWT.NONE);
					
					StringBuffer content = new StringBuffer();
					content.append(tablename);
					if(cnName!=null && !"".equals(cnName)){ //$NON-NLS-1$
						content.append("(").append(cnName).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
					}
						
					child.setText(content.toString());
				}
			}
		}else {
			tableTree.removeAll();
			TreeItem child;
			for(int i=0;i<this.getAllTableList().size();i++){
				String tablename = this.getAllTableList().get(i).getTableName();
				String cnName = this.getAllTableList().get(i).getCnName();
				child = new TreeItem(tableTree, SWT.NONE);
				
				StringBuffer content = new StringBuffer();
				content.append(tablename);
				if(!tablename.equals(cnName)){
					content.append("(").append(cnName).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
					
				child.setText(content.toString());
			}
		}
	}

	@Override
	protected void okPressed() {
		data.clear();
		TreeItem[] items = tableTree.getSelection();
		for(int i=0;i<items.length;i++){
			String $value = items[i].getText();
			String[] values = new String[2];
			if($value.indexOf("(")>-1){ //$NON-NLS-1$
				values[0] = $value.substring(0,$value.indexOf("(")); //$NON-NLS-1$
				values[1] = $value.substring($value.indexOf("(") + 1,$value.length()-1); //$NON-NLS-1$
			} else {
				values[0] = $value;
				values[1] = $value;
			}
			data.add(values);
		}
		super.okPressed();
	}
	
	protected boolean isResizable() {
		return false;
	}
	public Tree getTableTree() {
		return tableTree;
	}
	public void setTableTree(Tree tableTree) {
		this.tableTree = tableTree;
	}
	public DBConfigModel getConfig() {
		return config;
	}
	public void setConfig(DBConfigModel config) {
		this.config = config;
	}
	public boolean isMulti() {
		return isMulti;
	}
	public void setMulti(boolean isMulti) {
		this.isMulti = isMulti;
	}
	public Text getSearchText() {
		return searchText;
	}
	public void setSearchText(Text searchText) {
		this.searchText = searchText;
	}
	public List<TableDomain> getAllTableList() {
		return allTableList;
	}
	public void setAllTableList(List<TableDomain> allTableList) {
		this.allTableList = allTableList;
	}
	public ArrayList<String[]> getData() {
		return data;
	}
	public void setData(ArrayList<String[]> data) {
		this.data = data;
	}
}