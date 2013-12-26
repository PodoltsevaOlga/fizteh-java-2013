package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public interface Command {
	public String getName();
	public int getArgsCount();
	public void execute(String[] args) 
			throws FileNotFoundException, IOException, ParseException;
	
}
