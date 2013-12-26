package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandPut implements Command {
	private MultiFileMapState table;
	
	public CommandPut(MultiFileMapState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "put";
	}

	@Override
	public int getArgsCount() {
		return 2;
	}

	@Override
	public void execute(String[] args) throws IOException {
		if (table.getStateTable() == null) {
			System.out.println("no table");
			return;
		}
		String returnValue = table.getStateTable().put(args[0], args[1]);
		if (returnValue == null) {
			System.out.println("new");
		} else {
			System.out.println("overwrite ");
			System.out.println(returnValue);
		}
	}

}
