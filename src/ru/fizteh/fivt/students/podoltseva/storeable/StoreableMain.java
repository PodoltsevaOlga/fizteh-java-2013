package ru.fizteh.fivt.students.podoltseva.storeable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ru.fizteh.fivt.students.podoltseva.shell.Shell;
import ru.fizteh.fivt.students.podoltseva.shell.CommandExecutor;
import ru.fizteh.fivt.students.podoltseva.shell.Command;
import ru.fizteh.fivt.students.podoltseva.shell.Shell;

public class StoreableMain {
	
	public static void main(String[] args) {
		System.setProperty("fizteh.db.dir", "/home/olga/javatry");
		String databaseProperty = System.getProperty("fizteh.db.dir");
		System.out.println(databaseProperty);
        if (databaseProperty == null) {
            System.err.println("No property for database.");
            System.exit(-1);
        }
        try {
        	String databasePath = (new File(databaseProperty)).getCanonicalPath().toString();
        	StoreableTableProviderFactory factory = new StoreableTableProviderFactory();
        	StoreableTableProvider provider = (StoreableTableProvider) factory.create(databasePath);
        	StoreableState state = new StoreableState(provider, null);
        	CommandExecutor exec = new CommandExecutor();
        	exec.addCommand(new CommandCommit(state));
        	exec.addCommand(new CommandCreate(state));
        	exec.addCommand(new CommandDrop(state));
        	exec.addCommand(new CommandExit(state));
        	exec.addCommand(new CommandGet(state));
        	exec.addCommand(new CommandPut(state));
        	exec.addCommand(new CommandRemove(state));
        	exec.addCommand(new CommandRollback(state));
        	exec.addCommand(new CommandSize(state));
        	exec.addCommand(new CommandUse(state));
            Shell shell = new Shell(exec);
    		if (args.length == 0) {
    			shell.interactiveMode();
    		} else {
    			shell.batchMode(args);
    		}
    		System.exit(0);
        } catch (IllegalArgumentException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(3);
		} catch (IllegalStateException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(4);
		} catch (FileNotFoundException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(2);
		} catch (IOException exception) {
			System.err.println("type mismatch (" + exception.getMessage() + ")");
			System.exit(1);
		}
	}
}
