package ru.fizteh.fivt.students.podoltseva.storeable;

public class StoreableModification {
	private Storeable value;
	/* 0 - removed key (value = null)
	 * 1 - overwrite key 
	 * 2 - new key
	 */
	private int change; 
	public static final int REMOVED  = 0;
	public static final int OVERWRITE = 1;
	public static final int NEW = 2;
	
	public StoreableModification(Storeable newValue, int newChange) {
		value = newValue;
		change = newChange;
	}
	
	public Storeable getValue() {
		return value;
	}
	public void setValue(Storeable newValue) {
		value = newValue;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int newChange) {
		change = newChange;
	}
}