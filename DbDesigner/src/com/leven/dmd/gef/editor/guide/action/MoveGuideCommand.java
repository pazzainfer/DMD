package com.leven.dmd.gef.editor.guide.action;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Rectangle;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.guide.ElementBase;
import com.leven.dmd.gef.editor.guide.Guide;

public class MoveGuideCommand extends Command {
	/** ���Ƕ�λ */
	private int pDelta;
	/** �� */
	private Guide guide;

	public MoveGuideCommand(Guide guide, int positionDelta) {
		super(Messages.MoveGuideCommand_0);
		this.guide = guide;
		pDelta = positionDelta;
	}

	public void execute() {
		guide.setPosition(guide.getPosition() + pDelta);
		Iterator iter = guide.getParts().iterator();
		while (iter.hasNext()) {
			ElementBase element = (ElementBase) iter.next();
			Rectangle layout = element.getLayout();
			if (guide.isHorizontal()) {
				layout.y += pDelta;
			} else {
				layout.x += pDelta;
			}
			element.setLayout(layout);
		}
	}

	public void undo() {
		guide.setPosition(guide.getPosition() - pDelta);
		Iterator iter = guide.getParts().iterator();
		while (iter.hasNext()) {
			ElementBase element = (ElementBase) iter.next();
			Rectangle layout = element.getLayout();
			if (guide.isHorizontal()) {
				layout.y -= pDelta;
			} else {
				layout.x -= pDelta;
			}
			element.setLayout(layout);
		}
	}
}