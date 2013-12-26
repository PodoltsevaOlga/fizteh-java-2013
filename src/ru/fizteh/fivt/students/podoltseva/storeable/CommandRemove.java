package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandRemove implements Command {
	private StoreableState table;
	
	public CommandRemove(StoreableState newTable) {
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
		Storeable returnValue = table.getStateTable().remove(args[0]);
		if (returnValue == null) {
			System.out.println("not found");
		} else {
			System.out.println("removed");
		}
	}

}
