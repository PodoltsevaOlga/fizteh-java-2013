package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandExit implements Command {
	private MultiFileMapState table;
	
	public CommandExit(MultiFileMapState newTable) {
		table = newTable;
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
	public void execute(String[] args) throws FileNotFoundException,
			IOException {
		if (table.getStateTable() != null) {
			table.getStateTable().commit();
		}
		System.exit(0);
	}

}
