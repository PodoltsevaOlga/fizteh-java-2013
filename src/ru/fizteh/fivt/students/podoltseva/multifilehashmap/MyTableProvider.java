package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.podoltseva.shell.CommandRm;
import ru.fizteh.fivt.students.podoltseva.shell.Command;
import ru.fizteh.fivt.students.podoltseva.shell.State;

public class MyTableProvider implements TableProvider {
	private final String NAME_VALIDATION = "[A-Za-zА-Яа-я0-9]+";
	private File databaseFile;
	private Map<String, MultiFileMapTable> tableList = new HashMap<String, MultiFileMapTable>();
	
	public MyTableProvider(File newDatabaseFile) throws IllegalArgumentException {
		databaseFile = newDatabaseFile;
		if (databaseFile.listFiles() == null) {
			return;
		}
		for (File i : databaseFile.listFiles()) {
			if (!i.isDirectory()) {
				throw new IllegalArgumentException("MyTableProvider error: database contains unexpected files.");
			}
			String fullPath = Paths.get(databaseFile.toString(), i.getName()).toString();
			MultiFileMapTable newTable = new MultiFileMapTable(fullPath.toString());
			tableList.put(i.getName(), newTable);
			if (i.listFiles() == null) {
				return;
			}
			if (i.listFiles().length > 16) {
				throw new IllegalArgumentException("MyTableProvider error: database " + i.getName() + "" +
						" contains extra files.");
			}
			for (File j : i.listFiles()) {
				if (!j.isDirectory()) {
					throw new IllegalArgumentException("MyTableProvider error: database " + i.getName() + "" +
							" contains unexpected files.");
				}
				if (j.listFiles() == null) {
					j.delete();
				}
				if (j.listFiles().length > 16) {
					throw new IllegalArgumentException("MyTableProvider error: database " + i.getName() + 
							" in database contains extra directories.");
				}	
				for (File k : j.listFiles()) {
					if (!k.isFile()) {
						throw new IllegalArgumentException("MyTableProvider error: directory " + i.getName() + 
								" in database contains unexpected directories.");
					}
					if (k.length() == 0) {
						k.delete();
					}
				}
				if (j.listFiles() == null) {
					j.delete();
				}
			}
		}
	}
	
	public boolean containsTable(String name) {
		return tableList.containsKey(name);
	}
	
	@Override
	public MultiFileMapTable getTable(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("MyTableProvider error: Invalid table name in getTable");
		}
		String newName = name.trim();
		if (newName.length() == 0 || !newName.matches(NAME_VALIDATION)) {
			throw new IllegalArgumentException("MyTableProvider error: Invalid table name in getTable");
		}
		return tableList.get(newName);
	}

	@Override
	public Table createTable(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("MyTableProvider error: Invalid table name in createTable");
		}
		String newName = name.trim();
		if (newName.length() == 0 || !newName.matches(NAME_VALIDATION)) {
			throw new IllegalArgumentException("MyTableProvider error: Invalid table name in createTable");
		}
		if (tableList.containsKey(newName)) {
			return null;
		} 
		File fullPath = new File(databaseFile, newName).getAbsoluteFile();
		//boolean c = (new File("/home/forJava/t1").mkdir());
		boolean b = fullPath.mkdir();
		MultiFileMapTable newTable;
		newTable = new MultiFileMapTable(fullPath.getName());
		tableList.put(newName, newTable);
		return newTable;
	}

	@Override
	public void removeTable(String name) throws IllegalArgumentException, IllegalStateException {
		if (name == null) {
			throw new IllegalArgumentException("MyTableProvider error: Invalid table name in removeTable");
		}
		String newName = name.trim();
		if (newName.length() == 0 || !newName.matches(NAME_VALIDATION)) {
			throw new IllegalArgumentException("MyTableProvider error: Invalid table name in removeTable");
		}
		if (!tableList.containsKey(newName)) {
			throw new IllegalStateException("MyTableProvider error: No table with such name in removeTable");
		}
		State state = new State();
		state.setState(Paths.get(databaseFile.toString()));
		Command rm = new CommandRm(state);
		String[] args = new String[1];
		args[0] = newName;
		try {
			rm.execute(args);
		}
		catch (IOException e) {
			throw new IllegalStateException("MyTableProvider error: Problems with removing table " + name);
		}
		tableList.remove(newName);
	}
	
	public File getDatabaseFile() {
		return databaseFile;
	}

}
