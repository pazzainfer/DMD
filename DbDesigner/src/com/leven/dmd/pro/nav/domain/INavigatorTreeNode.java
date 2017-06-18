package com.leven.dmd.pro.nav.domain;

import java.util.List;

public interface INavigatorTreeNode {
	/**
	 * ���ظ�������
	 * @author leven
	 * 2012-11-28 ����02:20:45
	 * @return
	 */
	public INavigatorTreeNode getParent();
	/**
	 * ���������ӽ���������
	 * @author leven
	 * 2012-11-28 ����02:21:01
	 * @return
	 */
	public List getChildren();
	/**
	 * �����ӽ���������
	 * @author leven
	 * 2012-11-28 ����02:21:12
	 * @param children
	 */
	public void setChildren(List children);
	/**
	 * ���ؽ������
	 * @author leven
	 * 2012-11-28 ����02:21:27
	 * @return
	 */
	public String getName();
	/**
	 * ���ظ��������
	 * @author leven
	 * 2012-11-28 ����02:21:27
	 * @return
	 */
	public Object getRoot();
	/**
	 * ���ؽ��������������
	 * @author leven
	 * 2012-11-28 ����02:21:27
	 * @return
	 */
	public Object getData();
	/**
	 * ���ظý���Ƿ�������ӽ��
	 * @author leven
	 * 2012-11-28 ����02:22:03
	 * @return
	 */
	public boolean hasChildren();
	/**
	 * ���ظý�������
	 * @author leven
	 * 2012-11-28 ����02:32:19
	 * @return
	 */
	public int getNodeType();
}
