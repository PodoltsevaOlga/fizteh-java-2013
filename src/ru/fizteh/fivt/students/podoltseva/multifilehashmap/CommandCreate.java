package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandCreate implements Command {
	private MultiFileMapState table;
	
	public CommandCreate(MultiFileMapState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "create";
	}

	@Override
	public int getArgsCount() {
		return 1;
	}

	@Override
	public void execute(String[] args) throws FileNotFoundException,
			IOException {
		if (table.getStateProvider().createTable(args[0]) == null) {
			System.out.println(args[0] + " exists");
		} else {
			System.out.println("created");
		}

	}

}
