package com.leven.dmd.gef.util.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
/**
 * �Զ���ѡ���
 * @author lifeng
 * 2012-8-15 ����08:41:24
 */
public class CustomTabFolder extends CTabFolder {

	public static final int STYLE = SWT.NONE | SWT.BORDER;
	/**
	 * �Զ���ѡ���
	 * @param parent
	 */
	public CustomTabFolder(Composite parent) {
		super(parent, STYLE);
		
		this.setTabHeight(20);   
        this.marginHeight = 0;   
        this.marginWidth = 0;   
        this.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
        /*Button button=new Button(this,SWT.PUSH);
        button.setText("test");
        this.setTopRight(button);
        //this.setTopRight(button,SWT.FILL);//�����Ҳ�����*/
        Color[] color=new Color[4];
        color[0] = new Color(null,153,180,209);
        color[1] = new Color(null,189,207,225);
        color[2] = new Color(null,191,206,219);
        color[3] = new Color(null,191,206,219);
        int[] intArray=new int[]{25,45,100};
        this.setSelectionBackground(color, intArray);
        //���������˱�����ɫ���������ͬʱ�����˱���ͼƬ�Ļ��Ա���ͼƬ����
        
        this.setSimple(false);//����Բ��
        this.setUnselectedCloseVisible(true);
	}

}
