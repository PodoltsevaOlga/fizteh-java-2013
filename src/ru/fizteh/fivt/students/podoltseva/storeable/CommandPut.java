package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.IOException;
import java.text.ParseException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandPut implements Command {
	private StoreableState table;
	
	public CommandPut(StoreableState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "put";
	}

	@Override
	public int getArgsCount() {
		return -1;
	}

	@Override
	public void execute(String[] args) throws IOException, ParseException {
		if (table.getStateTable() == null) {
			System.out.println("no table");
			return;
		}
		String key = args[0];
		StringBuilder xmlValue = new StringBuilder();
		for (int i = 1; i < args.length; ++ i) {
			xmlValue = xmlValue.append(args[i]);
		}
		Storeable returnValue = table.getStateTable().put(key, table.getStateProvider().deserialize(table.getStateTable(), xmlValue.toString().trim()));
		if (returnValue == null) {
			System.out.println("new");
		} else {
			System.out.println("overwrite ");
			System.out.println(table.getStateProvider().serialize(table.getStateTable(), returnValue));
		}
	}
}

