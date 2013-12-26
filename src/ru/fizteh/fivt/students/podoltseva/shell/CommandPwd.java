package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.FileNotFoundException;

public class CommandPwd implements Command {
	private State state;
	
	public CommandPwd(State newState) {
		state = newState;
	}
	
	@Override
	public String getName() {
		return "pwd";
	}

	@Override
	public int getArgsCount() {
		return 0;
	}

	@Override
	public void execute(String[] args)
			throws FileNotFoundException {
		System.out.println(state.getState().toString());
	}

}
