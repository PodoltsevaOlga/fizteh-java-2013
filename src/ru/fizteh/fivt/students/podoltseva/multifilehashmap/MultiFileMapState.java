package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

public class MultiFileMapState {
	private MyTableProvider currentProvider;
	private MultiFileMapTable currentTable;
	
	MultiFileMapState(MyTableProvider newProvider, MultiFileMapTable newTable) {
		currentProvider = newProvider;
		currentTable = newTable;
	}
	
	void setStateTable(MultiFileMapTable newTable) {
		currentTable = newTable;
	}
	
	void setStateProvider(MyTableProvider newProvider) {
		currentProvider = newProvider;
	}
	
	MultiFileMapTable getStateTable() {
		return currentTable;
	}
	
	MyTableProvider getStateProvider() {
		return currentProvider;
	}
}
