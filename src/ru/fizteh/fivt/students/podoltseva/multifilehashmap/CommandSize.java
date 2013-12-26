package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

public class CommandSize implements Command {
	private MultiFileMapState table;
	
	public CommandSize(MultiFileMapState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "size";
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
		System.out.println(table.getStateTable().size());
	}

}
