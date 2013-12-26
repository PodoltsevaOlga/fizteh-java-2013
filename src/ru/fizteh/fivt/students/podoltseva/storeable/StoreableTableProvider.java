package ru.fizteh.fivt.students.podoltseva.storeable;

import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.students.podoltseva.shell.Command;
import ru.fizteh.fivt.students.podoltseva.shell.CommandRm;
import ru.fizteh.fivt.students.podoltseva.shell.State;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreableTableProvider implements TableProvider {
	private final String NAME_VALIDATION = "[A-Za-zА-Яа-я0-9]+";
	private File databaseFile;
	private Map<String, StoreableTable> tableList = new HashMap<String, StoreableTable>();
	
	public StoreableTableProvider(File path) throws IOException {
		databaseFile = path;
		if (databaseFile.listFiles() == null) {
			return;
		}
		for (File i : databaseFile.listFiles()) {
			if (!i.isDirectory()) {
				throw new IllegalArgumentException("StoreableTableProvider error: " +
						"database contains unexpected files.");
			}
			String fullPath = Paths.get(databaseFile.toString(), i.getName()).toString();
			if (i.listFiles() == null) {
				return;
			}
			if (i.listFiles() == null) {
				return;
			}
			if (i.listFiles().length > 17) {
				throw new IllegalArgumentException("StoreableTableProvider error: " +
						"database " + i.getName() + " contains extra files.");
			}
			for (File j : i.listFiles()) {
				if (!j.isDirectory() && !j.getName().equals("signature.tsv")) {
					throw new IllegalArgumentException("StoreableTableProvider error: " +
							"database " + i.getName() + " contains unexpected files.");
				}
				if (j.listFiles() != null) {
					if (j.listFiles().length > 16) {
						throw new IllegalArgumentException("StoreableTableProvider error: " +
								"database " + i.getName() + " contains extra directories.");
					}	
					for (File k : j.listFiles()) {
						if (!k.isFile()) {
							throw new IllegalArgumentException("StoreableTableProvider error: " +
									"database " + i.getName() + " contains unexpected directories.");
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
			File signature = new File(fullPath, "signature.tsv");
			if (!signature.isFile()) {
				throw new IllegalArgumentException("StoreableTableProvider error: " +
						"database " + i.getName() + " doesn't contain signature");
			}
			StoreableTable newTable = new StoreableTable(fullPath, this, Magic.readSignature(signature));
			tableList.put(i.getName(), newTable);
		}
	}
	
	@Override
	public Table getTable(String name) {
		if (name == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table name in getTable");
		}
		name = name.trim();
		if (name.isEmpty() || !name.matches(NAME_VALIDATION)) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Invalid table name in getTable");
		}
		return tableList.get(name);
	}

	@Override
	public Table createTable(String name, List<Class<?>> columnTypes)
			throws IOException {
		if (name == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table name in createTable");
		}
		name = name.trim();
		if (name.isEmpty() || !name.matches(NAME_VALIDATION)) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Invalid table name in createTable");
		}
		if (tableList.containsKey(name)) {
			return null;
		}
		if (columnTypes == null || columnTypes.isEmpty()) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Invalid columnTypes list in createTable");
		}
		for (Class<?> type : columnTypes) {
			if (!Magic.correctSignature(type)) {
				throw new ColumnFormatException("StoreableTableProvider error: " +
						"Illegal column type in createTable");
			}
		}
		File fullPath = new File(databaseFile, name).getAbsoluteFile();
		StoreableTable table = new StoreableTable(fullPath.toString(), this, columnTypes);
		tableList.put(name, table);
		return table;
	}

	@Override
	public void removeTable(String name) throws IOException {
		if (name == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table name in removeTable");
		}
		name = name.trim();
		if (name.isEmpty() || !name.matches(NAME_VALIDATION)) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Invalid table name in removeTable");
		}
		if (!tableList.containsKey(name)) {
			throw new IllegalStateException("StoreableTableProvider error: " +
					"No table with such name in removeTable");
		}
		State state = new State();
		state.setState(Paths.get(databaseFile.toString()));
		Command rm = new CommandRm(state);
		String[] args = new String[1];
		args[0] = name;
		try {
			rm.execute(args);
		}
		catch (IOException e) {
			throw new IllegalStateException("StoreableTableProvider error: " +
					"Problems with removing table " + name);
		} catch (ParseException e) {
			//do nothing
		}
		tableList.remove(name);
	}

	@Override
	public Storeable deserialize(Table table, String value)
			throws ParseException {
		if (table == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table in deserialize");
		}
		if (value == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null value in deserialize");
		}
		Storeable returnStore = createFor(table);
		XMLReadWrite.read(table, returnStore, value);
		return returnStore;
	}

	@Override
	public String serialize(Table table, Storeable value)
			throws ColumnFormatException {
		if (table == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table in serialize");
		}
		if (value == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null value in serialize");
		}
		Object[] values = new Object[table.getColumnsCount()];
		try {
			for (int i = 0; i < table.getColumnsCount(); ++i) {
				values[i] = value.getColumnAt(i);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new ColumnFormatException("StoreableTableProvider error: " +
					"value has less columns than table.");
		}
		return XMLReadWrite.write(values);
	}

	@Override
	public Storeable createFor(Table table) {
		if (table == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table in createFor");
		}
		return new MyStoreable(Magic.getColumnTypesFromTable(table));
	}

	@Override
	public Storeable createFor(Table table, List<?> values)
			throws ColumnFormatException, IndexOutOfBoundsException {
		if (table == null) {
			throw new IllegalArgumentException("StoreableTableProvider error: " +
					"Null table in createFor");
		}
		MyStoreable returnStore = new MyStoreable(Magic.getColumnTypesFromTable(table));
		for (int i = 0; i < table.getColumnsCount(); ++i) {
			returnStore.setColumnAt(i, values.get(i));
		}
		return returnStore;
	}
	
	public File getDatabaseFile() {
		return databaseFile;
	}

}
