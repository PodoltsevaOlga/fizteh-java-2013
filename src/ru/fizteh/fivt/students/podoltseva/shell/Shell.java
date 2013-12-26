package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Shell {
	private CommandExecutor exec;
	
	public Shell(CommandExecutor newExec) {
		exec = newExec;
	}
	
	public void batchMode(String[] args) {
		String inputString = join(args, " ");
		inputString = inputString.trim();
		String[] commands = inputString.split("\\s*;\\s*");
		try {
			for (String i : commands) {
				exec.execute(i);
			}
		} catch (IllegalArgumentException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(3);
		} catch (IllegalStateException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(4);
		} catch (FileNotFoundException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(2);
		} catch (IOException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(1);
		} catch (ParseException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(5);
		}
	}
	
	public void interactiveMode() {
		String inputString;
		String[] commands;
		Scanner scan = new Scanner(System.in);
		do {
			try {
				System.out.print("$ ");
				inputString = scan.nextLine();
				inputString = inputString.trim();
				commands = inputString.split("\\s*;\\s*");
				for (String i : commands) {
						exec.execute(i);
				}
			} catch (Exception exception) {
				System.err.println("type mismatch (" + exception.getMessage() + ")");
			}
		}
		while(!Thread.currentThread().isInterrupted());
		
	}
	
	private static String join(/*Iterable<?>*/ String[] objects, String Separator) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Object o : objects) {
			if (first) {
				result.append(o.toString());
				first = false;
			} else {
				result.append(Separator).append(o.toString());
			}
		}
		return result.toString();
	}
}
