package de.marx_labs.datafilter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTest {
	public class Person {
		public String name;
		public int age;
	}

	protected Person createPerson() {
		Person p = new Person();
		p.name = TestHelper.randomString();
		p.age = TestHelper.randomInt(50);

		return p;
	}

	protected List<Person> createPersons(int count) {
		List<Person> persons = new ArrayList<Person>();

		for (int i = 0; i < count; i++) {
			persons.add(createPerson());
		}

		return persons;
	}
}
