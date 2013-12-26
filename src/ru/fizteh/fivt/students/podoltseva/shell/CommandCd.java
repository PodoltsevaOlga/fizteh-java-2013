package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandCd implements Command {
	private State state;
	
	public CommandCd(State newState) {
		state = newState;
	}

	@Override
	public String getName() {
		return "cd";
	}

	@Override
	public int getArgsCount() {
		return 1;
	}

	@Override
	public void execute(String[] args) 
			throws FileNotFoundException {
		String dir = new String(args[0]);
		Path absoluteDirPath = Paths.get(dir);
		if (!absoluteDirPath.isAbsolute()) { 
			absoluteDirPath = state.getState().resolve(absoluteDirPath);
		}
		File newDir = new File(absoluteDirPath.toString());
		if (!newDir.exists()) {
			throw new FileNotFoundException("cd: '" + dir + "': No such file or directory");
		}
		if (newDir.isFile()) {
			throw new FileNotFoundException("cd: '" + dir + "': It is not a directory");
		}
		state.setState(absoluteDirPath.normalize());
	}

}
