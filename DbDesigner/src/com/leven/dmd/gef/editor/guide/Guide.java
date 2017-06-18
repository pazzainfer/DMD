package com.leven.dmd.gef.editor.guide;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * ��
 */
public class Guide implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ����һ���򵼣� ���������ֱ��ı�ʱ���������ͨ������֪ͨ������
	 */
	public static final String PROPERTY_CHILDREN = "subparts changed";

	/**
	 * �ض�λ��������������
	 */
	public static final String PROPERTY_POSITION = "position changed";
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	private Map map;

	/**
	 * ��λ
	 */
	private int position;

	/**
	 * �Ƿ�ˮƽ
	 */
	private boolean horizontal;

	public Guide() {
		
	}

	/**
	 * ������
	 * @param isHorizontal �Ƿ��Ǻ����
	 */
	public Guide(boolean isHorizontal) {
		setHorizontal(isHorizontal);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/**
	 * Attaches the given part along the given edge to this guide. The
	 * LogicSubpart is also updated to reflect this attachment.
	 * @param part
	 *            The part that is to be attached to this guide; if the part is
	 *            already attached, its alignment is updated
	 * @param alignment
	 *            -1 is left or top; 0, center; 1, right or bottom
	 */
	public void attachElement(ElementBase element, int alignment) {
		if (getMap().containsKey(element) && getAlignment(element) == alignment)
			return;
		getMap().put(element, alignment);
		Guide parent = isHorizontal() ? element.getHorizontalGuide() : element
				.getVerticalGuide();
		if (parent != null && parent != this) {
			parent.detachElement(element);
		}
		if (isHorizontal()) {
			element.setHorizontalGuide(this);
		} else {
			element.setVerticalGuide(this);
		}
		listeners.firePropertyChange(PROPERTY_CHILDREN, null, element);
	}

	/**
	 * Detaches the given part from this guide. The LogicSubpart is also updated
	 * to reflect this change.
	 * @param part
	 *            the part that is to be detached from this guide
	 */
	public void detachElement(ElementBase element) {
		if (getMap().containsKey(element)) {
			getMap().remove(element);
			if (isHorizontal()) {
				element.setHorizontalGuide(null);
			} else {
				element.setVerticalGuide(null);
			}
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, element);
		}
	}

	/**
	 * This methods returns the edge along which the given part is attached to
	 * this guide. This information is used by
	 * {@link org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy
	 * LogicXYLayoutEditPolicy} to determine whether to attach or detach a part
	 * from a guide during resize operations.
	 * @param part
	 *            The part whose alignment has to be found
	 * @return an int representing the edge along which the given part is
	 *         attached to this guide; 1 is bottom or right; 0, center; -1, top
	 *         or left; -2 if the part is not attached to this guide
	 * @see org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest,
	 *      EditPart, Object)
	 */
	public int getAlignment(ElementBase element) {
		if (getMap().get(element) != null)
			return ((Integer) getMap().get(element)).intValue();
		return -2;
	}

	/**
	 * @return The Map containing all the parts attached to this guide, and
	 *         their alignments; the keys are LogicSubparts and values are
	 *         Integers
	 */
	public Map getMap() {
		if (map == null) {
			map = new Hashtable();
		}
		return map;
	}

	/**
	 * @return the set of all the parts attached to this guide; a set is used
	 *         because a part can only be attached to a guide along one edge.
	 */
	public Set getParts() {
		return getMap().keySet();
	}

	/**
	 * @return the position/location of the guide (in pixels)
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return <code>true</code> if the guide is horizontal (i.e., placed on a
	 *         vertical ruler)
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Sets the orientation of the guide
	 * @param isHorizontal
	 *            <code>true</code> if this guide is to be placed on a vertical
	 *            ruler
	 */
	public void setHorizontal(boolean isHorizontal) {
		horizontal = isHorizontal;
	}

	/**
	 * Sets the location of the guide
	 * @param offset
	 *            The location of the guide (in pixels)
	 */
	public void setPosition(int offset) {
		if (position != offset) {
			int oldValue = position;
			position = offset;
			listeners.firePropertyChange(PROPERTY_POSITION, new Integer(
			oldValue), new Integer(position));
		}
	}
}