package com.leven.dmd.gef.wizard.tableeditor;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;

public class ColumnSelectDialog extends Dialog {
	String[] itemLeft; // ���屣������б��е�����
	String[] itemRight = new String[0]; // ���屣���Ҳ��б��е�����
	private List right;
	private String data;

	/**
	 * �Զ����ֶ�ѡ��Ի���
	 * @param parentShell ������
	 * @param columns ���а����������ֶ��б�
	 * @param itemRight ��ѡ����ֶ�����
	 */
	protected ColumnSelectDialog(Shell parentShell,ArrayList<Column> columns,String[] itemRight) {
		super(parentShell);
		ArrayList<String> leftList = new ArrayList<String>();
		ArrayList<String> rightList = new ArrayList<String>();
		String temp;
		for(int i=0;i<columns.size();i++){
			temp = columns.get(i).getName();
			boolean has = false;
			for(int j=0;j<itemRight.length;j++){
				if(itemRight[j].equals(temp)){
					has = true;
					break;
				}
			}
			if(has){
				rightList.add(new String(temp));
			} else {
				leftList.add(new String(temp));
			}
		}
		itemLeft = new String[leftList.size()];
		this.itemRight = new String[rightList.size()];
		for(int i=0;i<leftList.size();i++){
			itemLeft[i] = leftList.get(i);
		}
		for(int i=0;i<rightList.size();i++){
			this.itemRight[i] = rightList.get(i);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText(Messages.ColumnSelectDialog_0);
		parent.setLayout(new GridLayout(1,false));
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(4,false);
		layout.horizontalSpacing = 0;
		comp.setLayout(layout);
		
		GridData listData = new GridData(GridData.FILL_VERTICAL);
		listData.widthHint = 175;
		GridData btnData = new GridData(GridData.FILL_VERTICAL);
		btnData.widthHint = 30;
		
		GridData listTitleData = new GridData();
		listTitleData.widthHint = 175;
		listTitleData.heightHint = 20;
		listTitleData.verticalAlignment = GridData.VERTICAL_ALIGN_END;
		GridData btnTitleData = new GridData();
		btnTitleData.widthHint = 30;
		btnTitleData.heightHint = 20;
		
		Label leftTitle = new Label(comp,SWT.NONE);
		leftTitle.setText(Messages.ColumnSelectDialog_1);
		leftTitle.setLayoutData(listTitleData);
		Label btnTitle1 = new Label(comp,SWT.NONE);
		btnTitle1.setLayoutData(btnTitleData);
		Label rightTitle = new Label(comp,SWT.NONE);
		rightTitle.setText(Messages.ColumnSelectDialog_2);
		rightTitle.setLayoutData(listTitleData);
		Label btnTitle2 = new Label(comp,SWT.NONE);
		btnTitle2.setLayoutData(btnTitleData);
		
		
		// ��������б���ѡ�������Ҵ��д�ֱ������
		final List left = new List(comp, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		left.setLayoutData(listData);
		left.setItems(itemLeft);// ����ѡ������
		left.setToolTipText(Messages.ColumnSelectDialog_3);// ������ʾ
		// �����¼������࣬Ϊ�ڲ���
		SelectionAdapter listener = new SelectionAdapter() {
			// ��ť�����¼��Ĵ�����
			public void widgetSelected(SelectionEvent e) {
				// ȡ�ô����¼��Ŀؼ�����
				Button b = (Button) e.widget;
				if (b.getText().equals(">"))// �����">"��ť //$NON-NLS-1$
					verifyValue(left.getSelection(), left, right);
				else if (b.getText().equals(">>"))// �����">>"��ť //$NON-NLS-1$
					verifyValue(left.getItems(), left, right);
				else if (b.getText().equals("<"))// �����"<"��ť //$NON-NLS-1$
					verifyValue(right.getSelection(), right, left);
				else if (b.getText().equals("<<"))// �����"<"��ť //$NON-NLS-1$
					verifyValue(right.getItems(), right, left);
				else if (b.getText().equals(Messages.ColumnSelectDialog_8))// �����"��"��ť
				{
					// ��õ�ǰѡ��ѡ�������ֵ
					int index = right.getSelectionIndex();
					if (index <= 0)// ���û��ѡ�У��򷵻�
						return;
					// ���ѡ����ѡ��ֵ����õ�ǰѡ���ֵ
					String currentValue = right.getItem(index);
					// ��ѡ�е�ѡ������һ��ѡ���ֵ
					right.setItem(index, right.getItem(index - 1));
					right.setItem(index - 1, currentValue);
					// �趨��һ��ѡ��Ϊѡ��״̬
					right.setSelection(index - 1);
				} else if (b.getText().equals(Messages.ColumnSelectDialog_9))// �����"��"��ť
				{
					// �����ư�ť���߼���ͬ
					int index = right.getSelectionIndex();
					if (index >= right.getItemCount() - 1)
						return;
					String currentValue = right.getItem(index);
					right.setItem(index, right.getItem(index + 1));
					right.setItem(index + 1, currentValue);
					right.setSelection(index + 1);
				}
			}

			// �ı������б�ֵ
			public void verifyValue(String[] select, List from, List to) {
				// ѭ������ѡ�е�ѡ��ֵ
				for (int i = 0; i < select.length; i++) {
					// ��һ���б����Ƴ���ѡ��ֵ
					from.remove(select[i]);
					// ��ӵ���һ���б���
					to.add(select[i]);
				}
			}
		};
		Composite moveComp = new Composite(comp, SWT.NONE);
		moveComp.setLayout(new GridLayout(1,true));
		
		moveComp.setLayoutData(btnData);
		// �������ư�ť
		Button bt1 = new Button(moveComp, SWT.NONE);
		bt1.setText(">"); //$NON-NLS-1$
		// ��Ϊ��ťע���¼��������İ�ť����
		bt1.addSelectionListener(listener);
		Button bt2 = new Button(moveComp, SWT.NONE);
		bt2.setText(">>"); //$NON-NLS-1$
		bt2.addSelectionListener(listener);
		Button bt3 = new Button(moveComp, SWT.NONE);
		bt3.setText("<<"); //$NON-NLS-1$
		bt3.addSelectionListener(listener);
		Button bt4 = new Button(moveComp, SWT.NONE);
		bt4.setText("<"); //$NON-NLS-1$
		bt4.addSelectionListener(listener);
		
		

		// �����Ҳ��б���ѡ�������Ҵ��д�ֱ������
		right = new List(comp, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		right.setLayoutData(listData);
		right.setItems(itemRight);
		right.setToolTipText(Messages.ColumnSelectDialog_14);

		Composite upDownComp = new Composite(comp, SWT.NONE);
		upDownComp.setLayoutData(btnData);
		upDownComp.setLayout(new GridLayout(1,true));
		
		Button bt5 = new Button(upDownComp, SWT.NONE);
		bt5.setText(Messages.ColumnSelectDialog_8);
		bt5.addSelectionListener(listener);
		Button bt6 = new Button(upDownComp, SWT.NONE);
		bt6.setText(Messages.ColumnSelectDialog_9);
		bt6.addSelectionListener(listener);
		
		return comp;
	}
	
	@Override
    protected Point getInitialSize() {
        return new Point(500, 350);
    }
	
	@Override
	protected void okPressed() {
		String[] results = right.getItems();
		StringBuffer result = new StringBuffer(""); //$NON-NLS-1$
		for(int i=0;i<results.length;i++){
			result.append(results[i]);
			result.append(","); //$NON-NLS-1$
		}
		result.replace(result.length()-1, result.length(), ""); //$NON-NLS-1$
		setData(result.toString());
		super.okPressed();
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}