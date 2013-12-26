package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.File;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {
		State state = new State();
		state.setState(Paths.get(new File(".").getAbsolutePath()).normalize());
		Command cd = new CommandCd(state);
        Command cp = new CommandCp(state);
        Command dir = new CommandDir(state);
        Command exit = new CommandExit(state);
        Command mkdir = new CommandMkdir(state);
        Command mv = new CommandMv(state);
        Command pwd = new CommandPwd(state);
        Command rm = new CommandRm(state);
        CommandExecutor exec = new CommandExecutor();
        exec.addCommand(cd);
        exec.addCommand(cp);
        exec.addCommand(dir);
        exec.addCommand(exit);
        exec.addCommand(mkdir);
        exec.addCommand(mv);
        exec.addCommand(pwd);
        exec.addCommand(rm);		
		Shell shell = new Shell(exec);
		if (args.length == 0) {
			shell.interactiveMode();
		} else {
			shell.batchMode(args);
		}
		System.exit(0);
	}

}
