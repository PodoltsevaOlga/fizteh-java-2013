package ru.fizteh.fivt.students.podoltseva.storeable;

import ru.fizteh.fivt.storage.structured.TableProviderFactory;
import ru.fizteh.fivt.storage.structured.TableProvider;

import java.io.File;
import java.io.IOException;

public class StoreableTableProviderFactory implements TableProviderFactory {
	
	public StoreableTableProviderFactory() {
	}
	
	@Override
	public TableProvider create(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("StoreableTableProviderFactory error: Invalid database name.");
		}
		path = path.trim();
		if (path.isEmpty()) {
			throw new IllegalArgumentException("StoreableTableProviderFactory error: Invalid database name.");
		} 
		File databaseDirectory = new File(path);
		if (!databaseDirectory.exists()) {
			throw new IllegalArgumentException("StoreableTableProviderFactory error: No such directory: " + path);
		}
		if (!databaseDirectory.isDirectory()) {
			throw new IllegalArgumentException("StoreableTableProviderFactory error: Database with such name is not a directory.");
		}
		return new StoreableTableProvider(databaseDirectory);
	}

}
