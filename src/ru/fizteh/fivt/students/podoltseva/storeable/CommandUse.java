package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandUse implements Command {
	private StoreableState table;
	
	public CommandUse(StoreableState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "use";
	}

	@Override
	public int getArgsCount() {
		return 1;
	}

	@Override
	public void execute(String[] args) throws FileNotFoundException,
			IOException, ParseException {
		if (table.getStateTable() != null && table.getStateTable().changesCount() != 0) {
			System.out.println(table.getStateTable().changesCount() + " unsaved changes");
			return;
		}
		StoreableTable returnValue = (StoreableTable) table.getStateProvider().getTable(args[0]);
		if (returnValue == null) {
			System.out.println(args[0] + " not exists");
		} else {
			File returnValueFile = new File(table.getStateProvider().getDatabaseFile(), 
					returnValue.getName());
			returnValue.readTable(returnValueFile);
			table.setStateTable(returnValue);
			System.out.println("using " + args[0]);
		}
	}

}
