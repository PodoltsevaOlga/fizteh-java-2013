package ru.fizteh.fivt.students.podoltseva.multifilehashmap;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.students.podoltseva.shell.State;
import ru.fizteh.fivt.students.podoltseva.shell.Command;
import ru.fizteh.fivt.students.podoltseva.shell.CommandRm;

public class MultiFileMapTableTest {
	private static MultiFileMapTable table = new MultiFileMapTable("table");

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File tableDir = new File("table");
		State state = new State();
		state.setState(tableDir.getCanonicalFile().toPath());
		Command rm = new CommandRm(state);
		String[] args = new String[1];
		args[0] = tableDir.getCanonicalFile().toPath().toString();
		rm.execute(args);
	}
	
	@Test
	public void testGetName() {
		String name = table.getName();
		Assert.assertEquals(name, "table");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPutNullPointerKey() {
		table.put(null, "value");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPutSpaceKey() {
		table.put("        	  ", "value");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPutNullPointerValue() {
		table.put("key", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPutSpaceValue() {
		table.put("key", "   		 ");
	}
	
	@Test
	public void testPutGoodNewKey() {
		Assert.assertNull(table.put("key1", "value1"));
		Assert.assertNull(table.put("key2", "value2"));
		Assert.assertNull(table.put("key3", "value3"));
	}
	
	@Test
	public void testPutExistingKey() {
		Assert.assertEquals(table.put("key1", "value2"), "value1");
		Assert.assertEquals(table.put("key1", "value2"), "value2");
		table.put("key1", "value1");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetNullPointerKey() {
		table.get(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetSpaceKey() {
		table.get("        	  ");
	}
	
	@Test
	public void testGetNonExistingKey() {
		Assert.assertNull(table.get("non-exists"));
	}
	
	@Test
	public void testGetExistingKey() {
		Assert.assertEquals(table.get("key1"), "value1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveNullPointerKey() {
		table.remove(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveSpaceKey() {
		table.remove("        	  ");
	}
	
	@Test
	public void testRemoveNonExistingKey() {
		Assert.assertNull(table.remove("key0"));
	}
	
	@Test
	public void testRemoveExistingKey() {
		Assert.assertEquals(table.remove("key1"), "value1");
		Assert.assertNull(table.get("key1"));
	}
	
	@Test
	public void testSize() {
		Assert.assertEquals(table.size(), 2);
	}
	
	@Test
	public void testComit() {
		Assert.assertEquals(table.commit(), 2);
	}

	@Test
	public void testRollback() {
		Assert.assertEquals(table.rollback(), 0);
	}
	
	@Test
	public void testPutRemoveRollback() {
		table.put("бебебе", "\\(._.)/");
		table.remove("бебебе");
		Assert.assertEquals(table.rollback(), 0);
	}
	
	@Test
	public void testRemovePutRollback() {
		table.remove("key2");
		table.put("key2", "value2");
		Assert.assertEquals(table.rollback(), 0);
	}
	
	@Test
	public void testPutCommitRemoveRollbackGet() {
		table.put("I_want", "to_sleep");
		table.commit();
		table.remove("I_want");
		table.rollback();
		Assert.assertEquals(table.get("I_want"), "to_sleep");
	}
	
	@Test
	public void testPutRollbackGet() {
		table.put("kill", "me");
		table.rollback();
		Assert.assertNull(table.get("kill"));
	}
	
	@Test
	public void testRemoveCommitGet() {
		table.remove("key2");
		table.commit();
		Assert.assertNull(table.get("key2"));
	}
	
	@Test
	public void testPutRemoveSize() {
		table.put("key1", "value1");
		table.put("key2", "value2");
		Assert.assertEquals(table.size(), 4);
		table.remove("I_want");
		Assert.assertEquals(table.size(), 3);
		table.put("key1", "value4");
		Assert.assertEquals(table.size(), 3);
		table.remove("key0");
		Assert.assertEquals(table.size(), 3);
	}
	
	@Test
	public void testPutRemoveCommitRollbackSize() {
		table.commit();
		table.remove("key1");
		table.remove("key2");
		Assert.assertEquals(table.size(), 1);
		Assert.assertEquals(table.rollback(), 2);
		Assert.assertEquals(table.size(), 3);
		table.put("key0", "value0");
		Assert.assertEquals(table.commit(), 1);
		Assert.assertEquals(table.size(), 4);
		Assert.assertEquals(table.commit(), 0);
		Assert.assertEquals(table.rollback(), 0);
	}
	
}