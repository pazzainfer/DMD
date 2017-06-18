package com.leven.dmd.gef.wizard.tableeditor;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.model.Column;

public class ColumnSelectWizardPage extends WizardPage {
	String[] itemLeft; // ���屣������б��е�����
	String[] itemRight = new String[0]; // ���屣���Ҳ��б��е�����
	private List right;
	private String data;

	/**
	 * �Զ����ֶ�ѡ��Ի���
	 * @param columns ���а����������ֶ��б�
	 * @param itemRight ��ѡ����ֶ�����
	 */
	protected ColumnSelectWizardPage(ArrayList<Column> columns,String[] itemRight) {
		super("ColumnSelectWizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.ColumnSelectWizardPage_1);
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
		this.setPageComplete(true);
		this.setMessage(Messages.ColumnSelectWizardPage_2, 1);
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FormLayout());
		
		int midBtnWidth = 100;
		
		GridData btnGrid = new GridData(midBtnWidth - 10, 23);
		
		FormData leftFormData = new FormData();
		leftFormData.left = new FormAttachment(0, 0);
		leftFormData.top = new FormAttachment(0, 16);
		leftFormData.bottom = new FormAttachment(100, -1);
		leftFormData.right = new FormAttachment(50, -(midBtnWidth/2));

		FormData rightFormData = new FormData();
		//ʹ�ұ�����λ��������ƫ�ư����ť���
		rightFormData.left = new FormAttachment(50, midBtnWidth/2);
		rightFormData.top = new FormAttachment(0, 20);
		rightFormData.bottom = new FormAttachment(100, -1);
		rightFormData.right = new FormAttachment(100, -1);
		
		FormData midFormData = new FormData();
		//ʹ�ұ�����λ��������ƫ�ư����ť���
		midFormData.left = new FormAttachment(50, -(midBtnWidth/2));
		midFormData.top = new FormAttachment(0, 20);
		midFormData.bottom = new FormAttachment(100, -1);
		midFormData.right = new FormAttachment(50, midBtnWidth/2);
		
		FormData leftTitleFormData = new FormData();
		leftTitleFormData.left = new FormAttachment(0, 5);
		leftTitleFormData.top = new FormAttachment(0, 1);
		leftTitleFormData.bottom = new FormAttachment(0, 16);
		leftTitleFormData.right = new FormAttachment(50, -(midBtnWidth/2));
		
		FormData rightTitleFormData = new FormData();
		//ʹ�ұ�����λ��������ƫ�ư����ť���
		rightTitleFormData.left = new FormAttachment(50, midBtnWidth/2);
		rightTitleFormData.top = new FormAttachment(0, 1);
		rightTitleFormData.bottom = new FormAttachment(0, 20);
		rightTitleFormData.right = new FormAttachment(100, -1);
		
		Label leftTitle = new Label(comp,SWT.NONE);
		leftTitle.setText(Messages.ColumnSelectWizardPage_3);
		leftTitle.setLayoutData(leftTitleFormData);
		
		Label rightTitle = new Label(comp,SWT.NONE);
		rightTitle.setText(Messages.ColumnSelectWizardPage_4);
		rightTitle.setLayoutData(rightTitleFormData);
		
		Composite leftComp = new Composite(comp, SWT.NONE);
		leftComp.setLayoutData(leftFormData);
		leftComp.setLayout(new GridLayout(1,false));
		// ��������б���ѡ�������Ҵ��д�ֱ������
		final List left = new List(leftComp, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		left.setLayoutData(new GridData(GridData.FILL_BOTH));
		left.setItems(itemLeft);// ����ѡ������
		left.setToolTipText(Messages.ColumnSelectWizardPage_5);// ������ʾ
		// �����¼������࣬Ϊ�ڲ���
		SelectionAdapter listener = new SelectionAdapter() {
			// ��ť�����¼��Ĵ�����
			public void widgetSelected(SelectionEvent e) {
				// ȡ�ô����¼��Ŀؼ�����
				Button b = (Button) e.widget;
				if (b.getText().equals(Messages.ColumnSelectWizardPage_6))// �����">"��ť
					verifyValue(left.getSelection(), left, right);
				else if (b.getText().equals(Messages.ColumnSelectWizardPage_7))// �����">>"��ť
					verifyValue(left.getItems(), left, right);
				else if (b.getText().equals(Messages.ColumnSelectWizardPage_8))// �����"<"��ť
					verifyValue(right.getSelection(), right, left);
				else if (b.getText().equals(Messages.ColumnSelectWizardPage_9))// �����"<"��ť
					verifyValue(right.getItems(), right, left);
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
		
		moveComp.setLayoutData(midFormData);
		// �������ư�ť
		Button bt1 = new Button(moveComp, SWT.NONE);
		bt1.setText(Messages.ColumnSelectWizardPage_6);
		bt1.setLayoutData(btnGrid);
		// ��Ϊ��ťע���¼��������İ�ť����
		bt1.addSelectionListener(listener);
		Button bt2 = new Button(moveComp, SWT.NONE);
		bt2.setText(Messages.ColumnSelectWizardPage_7);
		bt2.setLayoutData(btnGrid);
		bt2.addSelectionListener(listener);
		Button bt3 = new Button(moveComp, SWT.NONE);
		bt3.setText(Messages.ColumnSelectWizardPage_8);
		bt3.setLayoutData(btnGrid);
		bt3.addSelectionListener(listener);
		Button bt4 = new Button(moveComp, SWT.NONE);
		bt4.setText(Messages.ColumnSelectWizardPage_9);
		bt4.setLayoutData(btnGrid);
		bt4.addSelectionListener(listener);
		
		

		// �����Ҳ��б���ѡ�������Ҵ��д�ֱ������
		right = new List(comp, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		right.setLayoutData(rightFormData);
		right.setItems(itemRight);
		right.setToolTipText(Messages.ColumnSelectWizardPage_10);

		setControl(comp);
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String[] getItemLeft() {
		return itemLeft;
	}
	public void setItemLeft(String[] itemLeft) {
		this.itemLeft = itemLeft;
	}
	public String[] getItemRight() {
		return itemRight;
	}
	public void setItemRight(String[] itemRight) {
		this.itemRight = itemRight;
	}
	public List getRight() {
		return right;
	}
	public void setRight(List right) {
		this.right = right;
	}
}