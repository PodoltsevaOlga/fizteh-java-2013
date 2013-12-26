package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import ru.fizteh.fivt.storage.strings.TableProviderFactory;

import java.io.File;

public class MyTableProviderFactory implements TableProviderFactory {

	@Override
	public MyTableProvider create(String dir) throws IllegalArgumentException {
		if (dir == null) {
			throw new IllegalArgumentException("MyTableProviderFactory error: Invalid database name.");
		}
		dir = dir.trim();
		if (dir.isEmpty()) {
			throw new IllegalArgumentException("MyTableProviderFactory error: Invalid database name.");
		} 
		File databaseDirectory = new File(dir);
		if (!databaseDirectory.isDirectory()) {
			throw new IllegalArgumentException("MyTableProviderFactory error: Invalid database name.");
		}
		if (!databaseDirectory.isDirectory()) {
			throw new IllegalArgumentException("MyTableProviderFactory error: Database with such name is not a directory.");
		}
		return new MyTableProvider(databaseDirectory);
	}

}
