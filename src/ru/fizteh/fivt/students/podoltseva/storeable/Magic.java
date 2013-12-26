package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Magic {
	public static boolean correctSignature(Class<?> columnType) {
		if (columnType == null) {
			return false;
		}
		if (columnType == Integer.class 
				|| columnType == Byte.class
				|| columnType == Long.class
				|| columnType == Float.class
				|| columnType == Double.class
				|| columnType == Boolean.class
				|| columnType == String.class) {
			return true;
		} else {
			return false;
		}
	}

	public static Object convertStringToObject(String currentValue, Class<?> columnType) {
		if (currentValue.equals("null")) {
			return null;
		} else if (columnType == Integer.class) {
			return Integer.parseInt(currentValue);
		} else if (columnType == Long.class) {
			return Long.parseLong(currentValue);
		} else if (columnType == Byte.class) {
			return Byte.parseByte(currentValue);
		} else if (columnType == Double.class) {
			return Double.parseDouble(currentValue);
		} else if (columnType == Float.class) {
			return Float.parseFloat(currentValue);
		} else if (columnType == Boolean.class) {
			return Boolean.parseBoolean(currentValue);
		} else if (columnType == String.class) {
			return currentValue;
		} 
		throw new ColumnFormatException("ConvertStringToObject error: No such type");
	}
	
	public static Class<?> convertStringToClass(String type) {
		if (type.equals("int")) {
			return Integer.class;
		} else if (type.equals("long")) {
			return Long.class;
		} else if (type.equals("byte")) {
			return Byte.class;
		} else if (type.equals("float")) {
			return Float.class;
		} else if (type.equals("double")) {
			return Double.class;
		} else if (type.equals("boolean")) {
			return Boolean.class;
		} else if (type.equals("String")) {
			return String.class;
		}
		throw new ColumnFormatException("ConvertStringToClass error: No such type: " + type);
	}
	
	public static String convertClassToString(Class<?> type) {
		if (type == Integer.class) {
			return "int";
		} else if (type == Long.class) {
			return "long";
		} else if (type == Byte.class) {
			return "byte";
		} else if (type == Float.class) {
			return "float";
		} else if (type == Double.class) {
			return "double";
		} else if (type == Boolean.class) {
			return "boolean";
		} else if (type == String.class) {
			return "String";
		} 
		throw new ColumnFormatException("ConvertClassToString error: No such type");
	}
	public static boolean checkClassValidity(Class<?> first, Class<?> second) {
		return (first.isAssignableFrom(second));
	}

	public static List<Class<?>> getColumnTypesFromTable(Table table) {
		if (table == null) {
			throw new IllegalArgumentException("Magic error: Null table in getColumnTypes");
		}
		List<Class<?>> types = new ArrayList<Class<?>>();
		for (int i = 0; i < table.getColumnsCount(); ++i) {
			types.add(table.getColumnType(i));
		}
		return types;
	}
	
	public static boolean checkStoreableValidity(Storeable value, List<Class<?>> columnTypes){
		for (int i = 0; i < columnTypes.size(); ++i) {
			if (value.getColumnAt(i) != null && value.getColumnAt(i).getClass() != columnTypes.get(i)) {
				return false;
			}
		}
		try {
			value.getColumnAt(columnTypes.size());
			return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public static List<Class<?>> readSignature(File file) throws IOException {
		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream(file));
			long fileLength = file.length();
			if (fileLength == 0) {
				throw new IllegalArgumentException("StoreableTableProvider error: " +
						"file with signature can't me empty");
			}
			int currentPosition = 0;
			List<Class<?>> types = new ArrayList<Class<?>>();
			List<Byte> bytes = new ArrayList<Byte>();
			Byte currentByte;
			String currentType;
			while (currentPosition < fileLength) {
				currentByte = in.readByte();
				if (currentByte != ' ') {
					bytes.add(currentByte);
				}
				if (currentByte == ' ' || currentPosition == fileLength - 1) {
					byte[] convertType = new byte[bytes.size()];
					for (int i = 0; i < bytes.size(); ++i) {
						convertType[i] = bytes.get(i);
					}
					currentType = new String(convertType, StandardCharsets.UTF_8);
					types.add(convertStringToClass(currentType));
					bytes.clear();
				}
				++currentPosition;
			}
			return types;
		} catch (ColumnFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("Problems with reading 'siganure.tsv'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new IOException("Problems with closing 'siganure.tsv'");
				}
			}
		}
	}
	
	public static void writeSignature(File file, List<Class<?>> columnTypes) throws IOException {
		DataOutputStream out = null;
		if (columnTypes == null || columnTypes.isEmpty()) {
			throw new IllegalArgumentException("columnTypes can't be empty");
		}
		try {
			out = new DataOutputStream(new FileOutputStream(file));
			for (Class<?> i : columnTypes) {
				out.write((convertClassToString(i) + " ").getBytes(StandardCharsets.UTF_8));
			}
		} catch (ColumnFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("Problems with reading 'siganure.tsv'");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new IOException("Problems with closing 'siganure.tsv'");
				}
			}
		}
	}
}
