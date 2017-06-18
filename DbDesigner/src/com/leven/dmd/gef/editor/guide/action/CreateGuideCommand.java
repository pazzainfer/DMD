package com.leven.dmd.gef.editor.guide.action;

import org.eclipse.gef.commands.Command;

import com.leven.dmd.gef.Messages;
import com.leven.dmd.gef.editor.guide.Guide;
import com.leven.dmd.gef.editor.ruler.Ruler;

public class CreateGuideCommand extends Command {

	/** �� */
	private Guide guide;
	/** ��� */
	private Ruler parent;
	/** λ�� */
	private int position;

	public CreateGuideCommand(Ruler parent, int position) {
		super(Messages.CreateGuideCommand_0);
		this.parent = parent;
		this.position = position;
	}

	public boolean canUndo() {
		return true;
	}

	public void execute() {
		if (guide == null)
			guide = new Guide(!parent.isHorizontal());
		guide.setPosition(position);
		parent.addGuide(guide);
	}

	public void undo() {
		parent.removeGuide(guide);
	}
}