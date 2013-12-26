package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandUse implements Command {
	private MultiFileMapState table;
	
	public CommandUse(MultiFileMapState newTable) {
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
			IOException {
		if (table.getStateTable() != null && table.getStateTable().changesCount() != 0) {
			System.out.println(table.getStateTable().changesCount() + " unsaved changes");
			return;
		}
		MultiFileMapTable returnValue = table.getStateProvider().getTable(args[0]);
		if (returnValue == null) {
			System.out.println(args[0] + " not exists");
		} else {
			File returnValueFile = new File(table.getStateProvider().getDatabaseFile(), 
					returnValue.getName());
			returnValue.readMultiFileTable(returnValueFile);
			table.setStateTable(returnValue);
			System.out.println("using " + args[0]);
		}
	}

}
