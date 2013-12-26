package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandGet implements Command {
	private StoreableState table;
	
	public CommandGet(StoreableState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "get";
	}

	@Override
	public int getArgsCount() {
		return 1;
	}

	@Override
	public void execute(String[] args) throws IOException {
		if (table.getStateTable() == null) {
			System.out.println("no table");
			return;
		}
		String returnValue = table.getStateProvider().
				serialize(table.getStateTable(), table.getStateTable().get(args[0]));
		if (returnValue == null) {
			System.out.println("not found");
		} else {
			System.out.println("found ");
			System.out.println(returnValue);
		}
	}

}
