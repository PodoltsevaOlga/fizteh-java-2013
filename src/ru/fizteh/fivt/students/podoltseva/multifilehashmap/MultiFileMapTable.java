package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ru.fizteh.fivt.storage.strings.Table;

public class MultiFileMapTable implements Table {
	private String tableName;
	private Map<String, String> fileMap = new HashMap<String, String>();
	private Map<String, Modification> modificationsMap = new HashMap<String, Modification>();
	private int size;
	private final int REMOVED  = 0, OVERWRITE = 1, NEW = 2;

 	private class Modification {
		private String value;
		/* 0 - removed key (value = null)
		 * 1 - overwrite key 
		 * 2 - new key
		 */
		private int change; 
		
		public Modification(String newValue, int newChange) {
			value = newValue;
			change = newChange;
		}
		
		public String getValue() {
			return value;
		}
		public void setValue(String newValue) {
			value = newValue;
		}
		public int getChange() {
			return change;
		}
		public void setChange(int newChange) {
			change = newChange;
		}
	}
	
	public MultiFileMapTable(String name) {
		File tableDirectory = new File(name);
		if (!tableDirectory.exists()) {
			tableDirectory.mkdir();
			size = 0;
		} 
		tableName = tableDirectory.getName();
		
	}
	
	public void readMultiFileTable(File tableDirectory) throws IOException {
		if (fileMap == null) {
			fileMap = new HashMap<String, String>();
		}
		if (tableDirectory.exists()) {
			if (tableDirectory.listFiles() == null) {
				return;
			}
			for (File i : tableDirectory.listFiles()) {
				if (i.listFiles() == null) {
					continue;
				}
				for (File j : i.listFiles()) {
					FileRW.readFileToTable(j, fileMap);
				}
			}
		}
		size = fileMap.size();
	}
	
	@Override
	public String getName() {
		return tableName;
	}

	@Override
	public String get(String key) {
		if (key == null) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid key in get.");
		}
		key = key.trim();
		if (key.isEmpty()) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid key in get.");
		}
		Modification getKey = modificationsMap.get(key);
		if (getKey == null) {
			return fileMap.get(key);
		} else if (getKey.getChange() == REMOVED) {
			return null; 
		} else {
			return getKey.getValue();
		}
	}

	@Override
	public String put(String key, String value) {
		if (key == null) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid key in put.");
		}
		key = key.trim();
		if (key.isEmpty()) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid key in put.");
		}
		if (value == null) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid value in put.");
		}
		value = value.trim();
		if (value.isEmpty()) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid value in put.");
		}
		Modification currentValue = modificationsMap.get(key);
		String returnValue = null;
		String savedValue = fileMap.get(key);
		if (currentValue == null) {
			if (savedValue == null) {
				modificationsMap.put(key, new Modification(value, NEW));
				size++;
			} else {
				returnValue = savedValue;
				if (!savedValue.equals(value)) {
					modificationsMap.put(key, new Modification(value, OVERWRITE));
				}
			}
		} else {
			switch(currentValue.getChange()) {
			case REMOVED:
				size++;
				if (savedValue != null && savedValue.equals(value)) {
					modificationsMap.remove(key);
				} else {
					modificationsMap.put(key, new Modification(value, OVERWRITE));
				}
				break;
			case OVERWRITE:
				returnValue = currentValue.getValue();
				if (savedValue != null && savedValue.equals(value)) {
					modificationsMap.remove(key);
				} else {
					currentValue.setValue(value);
					modificationsMap.put(key, currentValue);
				}
				break;
			case NEW:
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
	public String remove(String key) {
		if (key == null) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid key in remove.");
		}
		key = key.trim();
		if (key.isEmpty()) { 
			throw new IllegalArgumentException("MultiFileMapTable error: Invalid key in remove.");
		}
		Modification getKey = modificationsMap.get(key);
		if (getKey == null) {
			if (fileMap.containsKey(key)) {
				modificationsMap.put(key, new Modification(null, REMOVED));
				size--;
				return fileMap.get(key);
			} else {
				return null;
			}
		} else if (getKey.getChange() == REMOVED) {
			return null; 
		} else if (getKey.getChange() == NEW) {
			String s = getKey.getValue();
			modificationsMap.remove(key);
			size--;
			return s;
		} else {
			String s = getKey.getValue();
			getKey.setChange(REMOVED);
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
	public int commit() {
		int returnValue = modificationsMap.size();
		Set<Map.Entry<String, Modification>> ModifiedKeys = modificationsMap.entrySet();
		for (Map.Entry<String, Modification> i : ModifiedKeys) {
			if (i.getValue().getChange() == REMOVED) {
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
	
	public int changesCount() {
		return modificationsMap.size();
	}

	public Map<String, String> getMap() {
		return fileMap;
	}
}
