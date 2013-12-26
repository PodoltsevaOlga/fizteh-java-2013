package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandRollback implements Command {
	private StoreableState table;
	
	public CommandRollback(StoreableState newTable) {
		table = newTable;
	}

	@Override
	public String getName() {
		return "rollback";
	}

	@Override
	public int getArgsCount() {
		return 0;
	}

	@Override
	public void execute(String[] args) throws FileNotFoundException,
			IOException {
		if (table.getStateTable() == null) {
			System.out.println("no table");
			return;
		}
		System.out.println(table.getStateTable().rollback());
	}

}
