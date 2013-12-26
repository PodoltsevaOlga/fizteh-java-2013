package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class MyTableProviderTest {
	private static MyTableProvider provider;
    private static File databaseDir;
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		databaseDir = new File("database").getCanonicalFile();
        databaseDir.delete();
        databaseDir.mkdir();
        MyTableProviderFactory factory = new MyTableProviderFactory();
        provider = factory.create(databaseDir.toString());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		databaseDir.delete();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNullPointerTable() throws Exception {
		provider.createTable(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateSpaceNameTable() throws Exception {
		provider.createTable("			  ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateBadNameTable() throws Exception {
		provider.createTable("kislenko_style:(^ー^)ノ");
	}
	
	@Test
	public void testCreateGoodTable() throws Exception {
		provider.createTable("table");
	}
	
	@Test
	public void testCreateExistingTable() throws Exception {
		//provider.createTable("table");
		Assert.assertNull(provider.createTable("table"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetNullPointerTable() throws Exception {
		provider.getTable(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetSpaceNameTable() throws Exception {
		provider.getTable("			  ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetBadNameTable() throws Exception {
		provider.getTable("(シ_ _)シ");
	}
	
	@Test
	public void testGetGoodTable() throws Exception {
		MultiFileMapTable table1 = provider.getTable("table");
		Assert.assertEquals(table1, provider.getTable("table"));
		provider.createTable("table2");
		MultiFileMapTable table2 = provider.getTable("table2");
		Assert.assertEquals(table2, provider.getTable("table2"));
		provider.createTable("table3");
	}
	
	@Test
	public void testGetNonExistingTable() throws Exception {
		//provider.createTable("table");
		Assert.assertNull(provider.getTable("table1"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveNullPointerTable() throws Exception {
		provider.removeTable(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSpaceNameTable() throws Exception {
		provider.removeTable("			  ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveBadNameTable() throws Exception {
		provider.removeTable("(∪｡∪)｡｡｡zzZ");
	}
	
	@Test
	public void testRemoveExistingTable() throws Exception {
		provider.removeTable("table");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testRemoveNonExistingTable() throws Exception {
		provider.removeTable("table");
	}

}
