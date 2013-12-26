package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import ru.fizteh.fivt.students.podoltseva.shell.Command;
import ru.fizteh.fivt.students.podoltseva.shell.CommandRm;
import ru.fizteh.fivt.students.podoltseva.multifilehashmap.FileRW;

public class CommandCommit implements Command {
	private StoreableState table;

	public CommandCommit(StoreableState newTable) {
		table = newTable;
	}
	
	@Override
	public String getName() {
		return "commit";
	}

	@Override
	public int getArgsCount() {
		return 0;
	}

	@Override
	public void execute(String[] args) throws FileNotFoundException,
			IOException, ParseException {
		if (table.getStateTable() == null) {
			System.out.println("no table");
			return;
		}
		Path path = Paths.get(table.getStateProvider().getDatabaseFile().toString(), table.getStateTable().getName());
		File tableDir = path.toFile();
		if (tableDir.listFiles() != null) {
			State state = new State();
			Command rm = new CommandRm(state);
			for (File i : tableDir.listFiles()) {
				if (!i.getName().equals("signature.tsv")) {
					state.setState(path);
					String[] args1 = new String[1];
					args1[0] = i.getName();
					rm.execute(args1);
				}
			}
		}
		System.out.println(table.getStateTable().commit());
		Map<String, Storeable> fileMapStoreable = table.getStateTable().getMap();
		if (fileMapStoreable.size() == 0) {
			return;
		}
		TreeSet<String> forHashCode = new TreeSet<String>(fileMapStoreable.keySet());
		Map<String, String> fileMap = new HashMap<String, String>();
		for (String i : forHashCode) {
			fileMap.put(i, table.getStateProvider().serialize(table.getStateTable(), fileMapStoreable.get(i)));
		}
		Map<String, String> sameSymbol = new HashMap<String, String>();
		byte firstByte = forHashCode.first().getBytes()[0];
		for (String i : forHashCode) {
			if (i.getBytes()[0] == firstByte) {
				sameSymbol.put(i, fileMap.get(i));
			} else {
				writeSameHashCode(firstByte, sameSymbol);
				firstByte = i.getBytes()[0];
				sameSymbol.put(i, fileMap.get(i));
			}
		}
		writeSameHashCode(firstByte, sameSymbol);
	}
	
	private void writeSameHashCode(byte firstByte, Map<String, String> sameSymbol) throws IOException {
		byte b = (byte)(Math.abs(firstByte));
		int nDirectory = b % 16;
		int nFile = b / 16 % 16;
		String dirName = Paths.get(table.getStateProvider().getDatabaseFile().toString(), 
				table.getStateTable().getName(), nDirectory + ".dir").toString();
		File directory = new File(dirName);
		if (!directory.exists()) {
			directory.mkdir();
		}
		File file = new File(directory, nFile + ".dat");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileRW.writePartOfTableToFile(file, sameSymbol);
		sameSymbol.clear();
	}
}
