package com.proquest.interview.phonebook;

import com.proquest.interview.util.DatabaseUtil;
import com.proquest.interview.util.LegacyPerson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PhoneBookImplTest {

	@Before
	public void setup() {
		DatabaseUtil.initDB();
	}

	@After
	public void teardown() {
	}

	@Test
	public void testAssembleName() {
		PhoneBookImpl instance = new PhoneBookImpl();
		String first = "John";
		String last = "Deacon";
		Assert.assertEquals("John Deacon", instance.assembleFullName(first, last));
	}

	@Test
	public void testFindPersonInCache() {
		PhoneBookImpl instance = new PhoneBookImpl();
		Assert.assertEquals(new LegacyPerson("Chris Johnson", "(321) 231-7876", "452 Freeman Drive, Algonac, MI"), instance.findPersonInCache("Chris Johnson"));
	}

	@Test
	public void testFindPersonNotExist() {
		PhoneBookImpl instance = new PhoneBookImpl();
		Assert.assertEquals(null, instance.findPersonInCache("Dont Exist"));
	}

	@Test
	public void testAddPerson() {
		PhoneBookImpl instance = new PhoneBookImpl();
		Assert.assertEquals(null, instance.findPersonInCache("Dont ExistYet"));

		Person added = new Person("Dont ExistYet", "1111111111", "123 No Way");
		instance.addPerson(added);
		Assert.assertEquals(added, instance.findPersonInCache("Dont ExistYet"));
	}
}
