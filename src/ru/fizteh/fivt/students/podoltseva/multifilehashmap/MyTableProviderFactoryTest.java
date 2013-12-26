package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MyTableProviderFactoryTest {
    private MyTableProviderFactory factory = new MyTableProviderFactory();
    private static File file;
    private static File dir;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	file = new File("shit").getCanonicalFile();
    	file.delete();
    	file.createNewFile();
    	dir = new File("Okay").getCanonicalFile();
    	dir.delete();
    	dir.mkdir();
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        dir.delete();
        file.delete();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullPointerException() throws Exception {
        factory.create(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBadDatabaseName() throws Exception {
        factory.create("   	");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDatabaseAsFile() throws Exception {
    	
        factory.create(file.toString());
    }
    
    @Test
    public void testGoodDatabase() throws Exception {
        factory.create(dir.toString());
    }
    
}
