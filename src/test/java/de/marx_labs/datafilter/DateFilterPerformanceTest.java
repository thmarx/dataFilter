package de.marx_labs.datafilter;

import java.util.List;

import de.marx_labs.datafilter.dimension.Dimension;
import de.marx_labs.datafilter.function.ValueFunktion;

import org.junit.Test;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

public class DateFilterPerformanceTest extends AbstractTest {

	@Test
	public void testCreateDateFilter() {

		List<Person> persons = createPersons(1000);

		StopWatch stopWatch = new LoggingStopWatch("createDateFilter 1000");

		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.build();
		personFilter.addAll(persons);

		stopWatch.stop();
	}

	@Test
	public void testCreateNameDimesion_1000() {

		List<Person> persons = createPersons(1000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.build();
		personFilter.addAll(persons);

		StopWatch stopWatch = new LoggingStopWatch(
				"testCreateNameDimesion_1000");

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		stopWatch.stop();
	}

	@Test
	public void testCreateNameDimesion_10000() {

		List<Person> persons = createPersons(10000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.build();
		personFilter.addAll(persons);

		StopWatch stopWatch = new LoggingStopWatch(
				"testCreateNameDimesion_10000");

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		stopWatch.stop();
	}

	@Test
	public void testCreateNameDimesion_100000() {

		List<Person> persons = createPersons(100000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.build();
		personFilter.addAll(persons);

		StopWatch stopWatch = new LoggingStopWatch(
				"testCreateNameDimesion_100000");

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		stopWatch.stop();
	}

	@Test
	public void testCreateNameDimesion_500000() {

		List<Person> persons = createPersons(500000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.build();
		personFilter.addAll(persons);

		StopWatch stopWatch = new LoggingStopWatch(
				"testCreateNameDimesion_500000");

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		stopWatch.stop();
	}

	@Test
	public void testCreateNameDimesion_1000000() {

		List<Person> persons = createPersons(1000000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.build();
		personFilter.addAll(persons);

		StopWatch stopWatch = new LoggingStopWatch(
				"testCreateNameDimesion_1000000");

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		stopWatch.stop();
	}

}
