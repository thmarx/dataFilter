package net.mad.data.datafilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.mad.data.datafilter.dimension.Dimension;
import net.mad.data.datafilter.dimension.NoSynchedDimension;
import net.mad.data.datafilter.function.ValueAccessorFunktion;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

public class ParallelSynchedDateFilterPerformanceTest {
	
	class Person {
		public String name;
		public int age;
	}
	
	private Person createPerson () {
		Person p = new Person();
		p.name = TestHelper.randomString(8);
		p.age = TestHelper.randomInt(50);
		
		return p;
	}
	
	private List<Person> createPersons (int count) {
		List<Person> persons = new ArrayList<Person>();
		
		for (int i = 0; i < count; i++) {
			persons.add(createPerson());
		}
		
		return persons;
	}
	
	@Test
	public void testCreateDateFilter () {
		
		List<Person> persons = createPersons(1000);
		
		StopWatch stopWatch = new LoggingStopWatch("createDateFilter 1000");

		DataFilter<Person> personFilter = DataFilter.builder(Person.class).parallel(true).synched(true).build();
		personFilter.addAll(persons);
		
		stopWatch.stop();
	}
	
	@Test
	public void testCreateNameDimesion_1000 () {
		
		List<Person> persons = createPersons(1000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class).parallel(true).synched(true).build();
		personFilter.addAll(persons);
		
		StopWatch stopWatch = new LoggingStopWatch("testCreateNameDimesion_1000");

		Dimension<String, Person> nameDim = personFilter.dimension(new ValueAccessorFunktion<Person, String>() {

			public String value(Person type) {
				return type.name;
			}
		}, String.class);
		
		stopWatch.stop();
	}

	@Test
	public void testCreateNameDimesion_10000 () {
		
		List<Person> persons = createPersons(10000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class).parallel(true).synched(true).build();
		personFilter.addAll(persons);
		
		StopWatch stopWatch = new LoggingStopWatch("testCreateNameDimesion_10000");

		Dimension<String, Person> nameDim = personFilter.dimension(new ValueAccessorFunktion<Person, String>() {

			public String value(Person type) {
				return type.name;
			}
		}, String.class);
		
		stopWatch.stop();
	}
	
	@Test
	public void testCreateNameDimesion_100000 () {
		
		List<Person> persons = createPersons(100000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class).parallel(true).synched(true).build();
		personFilter.addAll(persons);
		
		StopWatch stopWatch = new LoggingStopWatch("testCreateNameDimesion_100000");

		Dimension<String, Person> nameDim = personFilter.dimension(new ValueAccessorFunktion<Person, String>() {

			public String value(Person type) {
				return type.name;
			}
		}, String.class);
		
		
		stopWatch.stop();
	}
	
	@Test
	public void testCreateNameDimesion_500000 () {
		
		List<Person> persons = createPersons(500000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class).parallel(true).synched(true).build();
		personFilter.addAll(persons);
		
		StopWatch stopWatch = new LoggingStopWatch("testCreateNameDimesion_500000");

		Dimension<String, Person> nameDim = personFilter.dimension(new ValueAccessorFunktion<Person, String>() {

			public String value(Person type) {
				return type.name;
			}
		}, String.class);
		
		
		stopWatch.stop();
	}
	
	@Test
	public void testCreateNameDimesion_1000000 () {
		
		List<Person> persons = createPersons(1000000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class).parallel(true).synched(true).build();
		personFilter.addAll(persons);
		
		StopWatch stopWatch = new LoggingStopWatch("testCreateNameDimesion_1000000");

		Dimension<String, Person> nameDim = personFilter.dimension(new ValueAccessorFunktion<Person, String>() {

			public String value(Person type) {
				return type.name;
			}
		}, String.class);
		
		stopWatch.stop();
	}

}
