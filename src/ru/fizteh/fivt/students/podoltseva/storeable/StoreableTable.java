package ru.fizteh.fivt.students.podoltseva.storeable;

import ru.fizteh.fivt.storage.structured .Table;
import ru.fizteh.fivt.students.podoltseva.multifilehashmap.FileRW;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class StoreableTable implements Table {
	private StoreableTableProvider tableProvider;
	private List<Class<?>> columnTypes;
	private String tableName;
	private Map<String, Storeable> fileMap = new HashMap<String, Storeable>();
	private Map<String, StoreableModification> modificationsMap = new HashMap<String, StoreableModification>();
	private int size;
	
	public StoreableTable(String path, StoreableTableProvider provider, 
			List<Class<?>> columnTypes) throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
			File signature = new File(dir.toString(), "signature.tsv");
			Magic.writeSignature(signature, columnTypes);
		}
		tableName = dir.getName();
		tableProvider = provider;
		this.columnTypes = new ArrayList<Class<?>>(columnTypes);
		size = 0;
	}

	@Override
	public String getName() {
		return tableName;
	}

	@Override
	public Storeable get(String key) {
		if (key == null) { 
			throw new IllegalArgumentException("StoreableTable error: Invalid key in get.");
		}
		key = key.trim();
		if (key.isEmpty()) { 
			throw new IllegalArgumentException("StoreableTable error: Invalid key in get.");
		}
		StoreableModification getKey = modificationsMap.get(key);
		if (getKey == null) {
			return fileMap.get(key);
		} else if (getKey.getChange() == StoreableModification.REMOVED) {
			return null; 
		} else {
			return (Storeable)getKey.getValue();
		}
	}

	@Override
	public Storeable put(String key, Storeable value)
			throws ColumnFormatException {
		if (key == null) { 
			throw new IllegalArgumentException("StoreableTable error: Null key in put.");
		}
		key = key.trim();
		if (key.isEmpty()) { 
			throw new IllegalArgumentException("StoreableTable error: Invalid key in put.");
		}
		if (value == null) { 
			throw new IllegalArgumentException("StoreableTable error: Null value in put.");
		}
		if (!Magic.checkStoreableValidity(value, columnTypes)) {
			throw new ColumnFormatException("type mismatch");
		}
		StoreableModification currentValue = modificationsMap.get(key);
		Storeable returnValue = null;
		Storeable savedValue = fileMap.get(key);
		if (currentValue == null) {
			if (savedValue == null) {
				modificationsMap.put(key, new StoreableModification(value, StoreableModification.NEW));
				size++;
			} else {
				returnValue = savedValue;
				if (!savedValue.equals(value)) {
					modificationsMap.put(key, new StoreableModification(value, StoreableModification.OVERWRITE));
				}
			}
		} else {
			switch(currentValue.getChange()) {
			case StoreableModification.REMOVED:
				size++;
				if (savedValue != null && savedValue.equals(value)) {
					modificationsMap.remove(key);
				} else {
					modificationsMap.put(key, 
							new StoreableModification(value, StoreableModification.OVERWRITE));
				}
				break;
			case StoreableModification.OVERWRITE:
				returnValue = currentValue.getValue();
				if (savedValue != null && savedValue.equals(value)) {
					modificationsMap.remove(key);
				} else {
					currentValue.setValue(value);
					modificationsMap.put(key, currentValue);
				}
				break;
			case StoreableModification.NEW:
				returnValue = currentValue.getValue();
				if (!returnValue.equals(value)) {
					currentValue.setValue(value);
					modificationsMap.put(key, currentValue);
				}
				break;
			}
		}
		return returnValue;
	}

	@Override
	public Storeable remove(String key) {
		if (key == null) { 
			throw new IllegalArgumentException("StoreableTable error: Invalid key in remove.");
		}
		key = key.trim();
		if (key.isEmpty()) { 
			throw new IllegalArgumentException("StoreableTable error: Invalid key in remove.");
		}
		StoreableModification getKey = modificationsMap.get(key);
		if (getKey == null) {
			if (fileMap.containsKey(key)) {
				modificationsMap.put(key, 
						new StoreableModification(null, StoreableModification.REMOVED));
				size--;
				return fileMap.get(key);
			} else {
				return null;
			}
		} else if (getKey.getChange() == StoreableModification.REMOVED) {
			return null; 
		} else if (getKey.getChange() == StoreableModification.NEW) {
			Storeable s = getKey.getValue();
			modificationsMap.remove(key);
			size--;
			return s;
		} else {
			Storeable s = getKey.getValue();
			getKey.setChange(StoreableModification.REMOVED);
			getKey.setValue(null);
			modificationsMap.put(key, getKey);
			size--;
			return s;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int commit() throws IOException {
		int returnValue = modificationsMap.size();
		Set<Map.Entry<String, StoreableModification>> ModifiedKeys = modificationsMap.entrySet();
		for (Map.Entry<String, StoreableModification> i : ModifiedKeys) {
			if (i.getValue().getChange() == StoreableModification.REMOVED) {
				fileMap.remove(i.getKey());
			} else {
				fileMap.put(i.getKey(), i.getValue().getValue());
			}
		}
		size = fileMap.size();
		modificationsMap.clear();
		return returnValue;
	}

	@Override
	public int rollback() {
		int returnValue = modificationsMap.size();
		size = fileMap.size();
		modificationsMap.clear();
		return returnValue;
	}

	@Override
	public int getColumnsCount() {
		return columnTypes.size();
	}

	@Override
	public Class<?> getColumnType(int columnIndex)
			throws IndexOutOfBoundsException {
		if (columnIndex < 0 || columnIndex >= columnTypes.size()) {
			throw new IndexOutOfBoundsException("StoreableTable error: " +
					"Index out of bound in getColumnType");
		}
		return columnTypes.get(columnIndex);
	}
	
	public int changesCount() {
		return modificationsMap.size();
	}
	
	public void readTable(File tableDirectory) throws IOException, ParseException {
		Map<String, String> fileMapString = new HashMap<String, String>(); 
		if (tableDirectory.exists()) {
			if (tableDirectory.listFiles() == null) {
				return;
			}
			for (File i : tableDirectory.listFiles()) {
				if (i.listFiles() == null) {
					continue;
				}
				for (File j : i.listFiles()) {
					FileRW.readFileToTable(j, fileMapString);
				}
			}
		}
		Set<String> keys = fileMapString.keySet();
		for (String i : keys) {
			fileMap.put(i, tableProvider.deserialize(this, fileMapString.get(i)));
		}
		size = fileMap.size();
	}
	
	public Map<String, Storeable> getMap() {
		return fileMap;
	}

}
