import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.podoltseva.shell.CommandRm;
import ru.fizteh.fivt.students.podoltseva.shell.State;
import ru.fizteh.fivt.students.podoltseva.storeable.StoreableTableProviderFactory;

import java.io.File;

public class StoreableTableProviderFactoryTest {
    private static final String DIRECTORY = "JavaTry";
    StoreableTableProviderFactory tableProviderFactory;

    @Before
    public void setUp() throws Exception {
        tableProviderFactory = new StoreableTableProviderFactory();
    }

    @After
    public void tearDown() throws Exception {
        File file = new File(DIRECTORY);
        if (file.exists()) {
            State state = new State();
			state.setState(Paths.get(databaseFile.toString()));
			Command rm = new CommandRm(state);
			String[] args = new String[1];
			args[0] = name;
			rm.execute(args);
        }
    }

    @Test
    public void createGoodFactory() throws Exception {
        Assert.assertNotNull(tableProviderFactory.create(TESTED_DIRECTORY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullFactory() throws Exception {
        tableProviderFactory.create(null);
    }
}
