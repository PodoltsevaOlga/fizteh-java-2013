package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FileRW {
	public static void readFileToTable(File inputFile, Map<String, String> tableMap) throws IOException {
		if (inputFile.length() == 0) {
			return;
		}
		InputStream is = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(is, 4096);
        DataInputStream dis = new DataInputStream(bis);
        try {
	        TreeMap<Integer, String> keysWithOffsets = readKeys(dis, inputFile);
			readValues(keysWithOffsets, dis, inputFile, tableMap);
        }
        catch (IOException exception) {
        	throw exception;
        }
		finally {
			dis.close();
		}
	}
	
	private static TreeMap<Integer, String> readKeys(DataInputStream dis, File inputFile) 
									throws IOException {
		String key;
		int offset;
		List<Byte> keyBuilder = new ArrayList<Byte>();
		Byte currentSymbol;
		TreeMap<Integer, String> keysWithOffsets = new TreeMap<Integer, String>();
		int minOffset = 1;
		int currentStreamPointer = 0;
		int length = (int)inputFile.length();
		while (currentStreamPointer < minOffset && currentStreamPointer < length) {
			currentSymbol = dis.readByte();
			++currentStreamPointer;
			if (currentSymbol == '\0') {
				throw new IOException("File '" + inputFile.getName() + "' is wrong: some key equals null.");
			}
			while (currentSymbol != '\0' && currentStreamPointer < length) {
				keyBuilder.add(currentSymbol);
				currentSymbol = dis.readByte();
				++currentStreamPointer;
			}
			if (currentSymbol != '\0' && currentStreamPointer == length) {
				throw new IOException("Unexpected end of file: '" + inputFile.getName() + "'.");
			}
			try {
				offset = dis.readInt();
			}
			catch (EOFException exception) {
				throw new IOException("Unexpected end of file: '" + inputFile.getName() + "'.");
			}
			if (offset < minOffset || minOffset == 1) {
				minOffset = offset;
			}
			currentStreamPointer += 4;
			byte[] keyByte = new byte[keyBuilder.size()];
			int i = 0;
			for (Byte b : keyBuilder) {
				keyByte[i] = b;
				++i;
			}
			key = new String(keyByte, StandardCharsets.UTF_8);
			keysWithOffsets.put(new Integer(offset), key);
		}
		if (currentStreamPointer >= length || currentStreamPointer > minOffset) {
			throw new IOException("Unexpected end of file: '" + inputFile.getName() + "'.");
		}
		return keysWithOffsets;
	}
	
	private static void readValues(TreeMap<Integer, String> keysWithOffsets, 
								DataInputStream dis, File inputFile, Map<String, String> tableMap) throws IOException {
		int countKeys =  keysWithOffsets.size();
		Map.Entry<Integer, String> firstPair = keysWithOffsets.firstEntry();
		Integer secondOffset;
		for (int i = 0; i < countKeys; ++i) {
			secondOffset = (i < countKeys - 1) ? keysWithOffsets.firstKey() 
											   : new Integer((int)inputFile.length());
			byte[] valueByte = new byte[secondOffset - firstPair.getKey()];
			try {
				dis.read(valueByte);
			}
			catch (IOException exception) {
				throw new IOException("Unexpected end of file: '" + inputFile.getName() + "'.");
			}
			tableMap.put(firstPair.getValue(), new String(valueByte, StandardCharsets.UTF_8));
			firstPair = keysWithOffsets.firstEntry();
			keysWithOffsets.remove(firstPair.getKey());
		}
		if (dis.available() > 0) {
			throw new IOException("There is some dust after the map in the file: '" + inputFile.getName() + "'.");
		}
	}
	
	public static void writePartOfTableToFile(File outputFile, Map<String, String> tableMap) throws IOException {
		try {
			OutputStream os = new FileOutputStream(outputFile);
	        BufferedOutputStream bos = new BufferedOutputStream(os, 4096);
	        DataOutputStream dos = new DataOutputStream(bos);
	        try {
		        int offset = 0;
		        Set<String> keys = tableMap.keySet();
		        for (String i : keys) {
		        	offset += i.getBytes(StandardCharsets.UTF_8).length;
		        	offset += 5; 		//1 - '\0', 4 - sizeof(int)
		        }
		        for (String i : keys) {
		        	dos.write(i.getBytes(StandardCharsets.UTF_8));
		        	dos.write('\0');
		        	dos.writeInt(offset);
		        	offset += tableMap.get(i).getBytes(StandardCharsets.UTF_8).length;
		        }
		        for (String i : keys) {
		        	dos.write(tableMap.get(i).getBytes(StandardCharsets.UTF_8));
		        }
	        }
	        catch (IOException exception) {
	        	throw new IOException("Problems with writing map to a file '" + outputFile.getName() + "'.");
	        }
	        finally {
	        	dos.close();
	        }
		}
		catch (IOException exception) {
			throw exception;
		}
        
	}
}
