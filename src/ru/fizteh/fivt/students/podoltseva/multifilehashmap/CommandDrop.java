package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandDrop implements Command {
	private MultiFileMapState table;
	
	public CommandDrop(MultiFileMapState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "drop";
	}

	@Override
	public int getArgsCount() {
		return 1;
	}

	@Override
	public void execute(String[] args) throws IOException {
		try {
			table.getStateProvider().removeTable(args[0]);
			System.out.println("dropped");
		}
		catch (IllegalStateException e) {
			System.out.println(args[0] + " not exists");
		}
	}

}
