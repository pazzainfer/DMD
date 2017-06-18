package com.leven.dmd.pro.console;

import java.io.PrintStream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.leven.dmd.pro.Activator;
import com.leven.dmd.pro.IImagePath;
import com.leven.dmd.pro.Messages;

public class ConsoleFactory implements IConsoleFactory {

	private static MessageConsole console = new MessageConsole(
			Messages.ConsoleFactory_0, Activator.getImageDescriptor(IImagePath.OBJ_CONSOLE));
	static boolean exists = false;

	/**
	 * 描述:打开控制台
	 * */
	public void openConsole() {
		showConsole();
	}

	/** */
	/**
	 * 描述:显示控制台
	 * */
	public static void showConsole() {
		if (console != null) {
			IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
			IConsole[] existing = manager.getConsoles();
			boolean exists = false;
			for (int i = 0; i < existing.length; i++) {
				if (console == existing[i])
					exists = true;
			}
			if (!exists) {
				manager.addConsoles(new IConsole[] { console });
			}
			manager.showConsoleView(console);

			MessageConsoleStream stream = console.newMessageStream();
			System.setOut(new PrintStream(stream));
		}
	}

	/** */
	/**
	 * 描述:关闭控制台
	 * */
	public static void closeConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault()
				.getConsoleManager();
		if (console != null) {
			manager.removeConsoles(new IConsole[] { console });
		}
	}

	/**
	 * 获取控制台
	 * 
	 * @return
	 */
	public static MessageConsole getConsole() {

		showConsole();

		return console;
	}
}