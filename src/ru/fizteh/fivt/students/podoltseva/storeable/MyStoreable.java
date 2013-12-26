package ru.fizteh.fivt.students.podoltseva.storeable;

import java.util.ArrayList;
import java.util.List;

import ru.fizteh.fivt.storage.structured.Storeable;

public class MyStoreable implements Storeable {
	List<Class<?>> columnTypes = new ArrayList<Class<?>>();
	List<Object> columnValues = new ArrayList<Object>();
	
	public MyStoreable(List<Class<?>> types) {
		if (types == null) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Null types isn't allowed in constructor");
		}
		columnTypes = types;
		for (int i = 0; i < types.size(); ++i) {
			columnValues.add(null);
		}
	}
	
	@Override
	public void setColumnAt(int columnIndex, Object value)
			throws ColumnFormatException, IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (value != null && 
				!Magic.checkClassValidity(value.getClass(), columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in setColumnAt");
		}
		columnValues.set(columnIndex, value);
	}
	
	@Override
	public Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		return columnValues.get(columnIndex);
	}

	@Override
	public Integer getIntAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(Integer.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getIntAt");
		}
		return (Integer)columnValues.get(columnIndex);
	}

	@Override
	public Long getLongAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(Long.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getLongAt");
		}
		return (Long)columnValues.get(columnIndex);
	}

	@Override
	public Byte getByteAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(Byte.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getByteAt");
		}
		return (Byte)columnValues.get(columnIndex);
	}

	@Override
	public Float getFloatAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(Float.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getFloatAt");
		}
		return (Float)columnValues.get(columnIndex);
	}

	@Override
	public Double getDoubleAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(Double.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getDoubleAt");
		}
		return (Double)columnValues.get(columnIndex);
	}

	@Override
	public Boolean getBooleanAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(Boolean.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getBooleanAt");
		}
		return (Boolean)columnValues.get(columnIndex);
	}

	@Override
	public String getStringAt(int columnIndex) throws ColumnFormatException,
			IndexOutOfBoundsException {
		checkIndexValidity(columnIndex);
		if (!Magic.checkClassValidity(String.class, columnTypes.get(columnIndex))) {
			throw new ColumnFormatException("MyStoreable error: " +
					"Wrong type of value in getStringAt");
		}
		return (String)columnValues.get(columnIndex);
	}
	
	private void checkIndexValidity(int columnIndex) throws IndexOutOfBoundsException {
		if (columnIndex < 0 || columnIndex > columnTypes.size() - 1) {
			throw new IndexOutOfBoundsException("MyStoreable error: " +
					"Index out of bound");
		}
	}

}
