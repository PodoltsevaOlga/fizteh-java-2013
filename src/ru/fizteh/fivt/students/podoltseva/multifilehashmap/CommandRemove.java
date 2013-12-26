package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandRemove implements Command {
	private MultiFileMapState table;
	
	public CommandRemove(MultiFileMapState newTable) {
		table = newTable;
	}
	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public int getArgsCount() {
		return 1;
	}

	@Override
	public void execute(String[] args) throws FileNotFoundException,
			IOException {
		if (table.getStateTable() == null) {
			System.out.println("no table");
			return;
		}
		String returnValue = table.getStateTable().remove(args[0]);
		if (returnValue == null) {
			System.out.println("not found");
		} else {
			System.out.println("removed");
		}
	}

}
