package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.File;

public class CommandDir implements Command {

	private State state;
	
	public CommandDir(State newState) {
		state = newState;
	}
	
	@Override
	public String getName() {
		return "dir";
	}

	@Override
	public int getArgsCount() {
		return 0;
	}

	@Override
	public void execute(String[] args) {
		File currentDir = new File(state.getState().toString());
		File[] filesInCurrentDir = currentDir.listFiles();
		for (File i : filesInCurrentDir) {
			System.out.println(i.getName());
		}
	}

}
