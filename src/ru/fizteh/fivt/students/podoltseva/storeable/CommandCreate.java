package ru.fizteh.fivt.students.podoltseva.storeable;

import ru.fizteh.fivt.students.podoltseva.shell.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandCreate implements Command {
	StoreableState table;

	public CommandCreate(StoreableState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "create";
	}
	
	@Override
	public int getArgsCount() {
		return -1;
	}

	@Override
	public void execute(String[] args) throws FileNotFoundException,
			IOException {
		if (args.length < 2) {
			throw new IllegalArgumentException("Error in create: No column types");
		}
		String name = args[0];
		StringBuilder columns = new StringBuilder();
		for (int i = 1; i < args.length; ++ i) {
			columns = columns.append(args[i]).append(" ");
		}
		String columnsToString = columns.toString();
		columnsToString = columnsToString.trim();
		if (columnsToString.charAt(0) != '(' || columnsToString.charAt(columnsToString.length() - 1) != ')') {
			throw new IllegalArgumentException("Error in create: Invalid arguments");
		}
		columnsToString = columnsToString.substring(1, columnsToString.length() - 1);
		String[] colTypes = columnsToString.split("\\s*,\\s*");
		List<Class<?>> columnTypes = new ArrayList<Class<?>>();
		for (String i : colTypes) {
			columnTypes.add(Magic.convertStringToClass(i.trim()));
		}
		if (table.getStateProvider().createTable(name, columnTypes) == null) {
			System.out.println(args[0] + " exists");
		} else {
			System.out.println("created");
		}
	}

}
