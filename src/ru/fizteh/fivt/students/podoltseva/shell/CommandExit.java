package ru.fizteh.fivt.students.podoltseva.shell;

public class CommandExit implements Command {
	private State state;
	
	public CommandExit(State newState) {
		state = newState;
	}
	
	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public int getArgsCount() {
		return 0;
	}

	@Override
	public void execute(String[] args) {
		System.exit(0);
	}

}
