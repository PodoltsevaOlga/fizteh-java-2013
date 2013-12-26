package ru.fizteh.fivt.students.podoltseva.storeable;

public class StoreableState {
	private StoreableTableProvider currentProvider;
	private StoreableTable currentTable;
	
	StoreableState(StoreableTableProvider newProvider, StoreableTable newTable) {
		currentProvider = newProvider;
		currentTable = newTable;
	}
	
	void setStateTable(StoreableTable newTable) {
		currentTable = newTable;
	}
	
	void setStateProvider(StoreableTableProvider newProvider) {
		currentProvider = newProvider;
	}
	
	StoreableTable getStateTable() {
		return currentTable;
	}
	
	StoreableTableProvider getStateProvider() {
		return currentProvider;
	}
}
