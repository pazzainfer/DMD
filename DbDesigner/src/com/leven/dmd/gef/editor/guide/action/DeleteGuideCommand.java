package com.leven.dmd.gef.editor.guide.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.guide.ElementBase;
import com.leven.dmd.gef.editor.guide.Guide;
import com.leven.dmd.gef.editor.ruler.Ruler;

public class DeleteGuideCommand extends Command {
	/**
	 * ���
	 */
	private Ruler parent;
	/**
	 * ��
	 */
	private Guide guide;
	/**
	 * �Ѵ����Ŀ�����
	 */
	private Map oldParts;

	public DeleteGuideCommand(Guide guide, Ruler parent) {
		super(Messages.DeleteGuideCommand_0);
		this.guide = guide;
		this.parent = parent;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		oldParts = new HashMap(guide.getMap());
		Iterator iter = oldParts.keySet().iterator();
		while (iter.hasNext()) {
			guide.detachElement((ElementBase) iter.next());
		}
		parent.removeGuide(guide);
	}

	public void undo() {
		parent.addGuide(guide);
		Iterator iter = oldParts.keySet().iterator();
		while (iter.hasNext()) {
			ElementBase element = (ElementBase) iter.next();
			guide.attachElement(element,
					((Integer) oldParts.get(element)).intValue());
		}
	}
}