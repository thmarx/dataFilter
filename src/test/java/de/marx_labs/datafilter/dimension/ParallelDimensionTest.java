package de.marx_labs.datafilter.dimension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.marx_labs.datafilter.AbstractTest;
import de.marx_labs.datafilter.DataFilter;
import de.marx_labs.datafilter.function.ValueFunktion;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParallelDimensionTest extends AbstractTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testFilterRange() {
		Collection<Integer> items = new ArrayList<Integer>();
		items.add(1);
		items.add(2);
		items.add(3);
		items.add(4);
		items.add(5);

		DataFilter<Integer> df = DataFilter.builder(Integer.class)
				.parallel(true).build();
		df.addAll(items);
		DataFilter.builder(Integer.class).build();

		Dimension<Integer, Integer> dimInt = df.dimension(
				new ValueFunktion<Integer, Integer>() {

					public Integer value(Integer type) {
						return type;
					}
				}, Integer.class);

		Collection<Integer> filtered = dimInt.filterRange(1, 5);
		assertEquals(5, filtered.size());
		assertTrue(contains(filtered, new int[] { 1, 2, 3, 4, 5 }));
		assertTrue(ordered(filtered, new int[] { 1, 2, 3, 4 }));

		filtered = dimInt.filterRange(1, 4);
		assertEquals(4, filtered.size());
		assertTrue(contains(filtered, new int[] { 1, 2, 3, 4 }));
		assertTrue(ordered(filtered, new int[] { 1, 2, 3, 4 }));

		filtered = dimInt.filterRange(2, 4);
		assertEquals(3, filtered.size());
		assertTrue(contains(filtered, new int[] { 2, 3, 4 }));
		assertTrue(ordered(filtered, new int[] { 2, 3, 4 }));

		df.add(3);
		dimInt = df.dimension(new ValueFunktion<Integer, Integer>() {

			public Integer value(Integer type) {
				return type;
			}
		}, Integer.class);

		filtered = dimInt.filterRange(2, 4);
		assertEquals(4, filtered.size());
		assertTrue(contains(filtered, new int[] { 2, 3, 3, 4 }));
		assertTrue(ordered(filtered, new int[] { 2, 3, 3, 4 }));
	}

	@Test
	public void testFilterExact() {
		Collection<Integer> items = new ArrayList<Integer>();
		items.add(1);
		items.add(2);
		items.add(3);
		items.add(4);
		items.add(5);

		DataFilter<Integer> df = DataFilter.builder(Integer.class)
				.parallel(true).build();
		df.addAll(items);

		Dimension<Integer, Integer> dimInt = df.dimension(
				new ValueFunktion<Integer, Integer>() {

					public Integer value(Integer type) {
						return type;
					}
				}, Integer.class);

		Collection<Integer> filtered = dimInt.filterExact(1);
		assertEquals(1, filtered.size());
		assertTrue(contains(filtered, new int[] { 1 }));

		df.add(3);
		dimInt = df.dimension(new ValueFunktion<Integer, Integer>() {

			public Integer value(Integer type) {
				return type;
			}
		}, Integer.class);

		filtered = dimInt.filterExact(3);
		assertEquals(2, filtered.size());
		assertTrue(contains(filtered, new int[] { 3, 3 }));
	}

	@Test
	public void testFilterAll() {
		Collection<Integer> items = new ArrayList<Integer>();
		items.add(1);
		items.add(2);
		items.add(3);
		items.add(4);
		items.add(5);

		DataFilter<Integer> df = DataFilter.builder(Integer.class).build();
		df.addAll(items);

		Dimension<Integer, Integer> dimInt = df.dimension(
				new ValueFunktion<Integer, Integer>() {

					public Integer value(Integer type) {
						return type;
					}
				}, Integer.class);

		Collection<Integer> filtered = dimInt.filterAll();
		assertEquals(5, filtered.size());
		assertTrue(contains(filtered, new int[] { 1, 2, 3, 4, 5 }));
		assertTrue(ordered(filtered, new int[] { 1, 2, 3, 4, 5 }));
	}

	
	
	@Test
	public void testCreateNameDimesion_1000() {

		List<Person> persons = createPersons(1000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		assertEquals(1000, nameDim.getValueCount());
	}

	@Test
	public void testCreateNameDimesion_10000() {

		List<Person> persons = createPersons(10000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		assertEquals(10000, nameDim.getValueCount());
	}

	@Test
	public void testCreateNameDimesion_100000() {

		List<Person> persons = createPersons(100000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		assertEquals(100000, nameDim.getValueCount());
	}

	@Test
	public void testCreateNameDimesion_500000() {

		List<Person> persons = createPersons(500000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		assertEquals(500000, nameDim.getValueCount());
	}

	@Test
	public void testCreateNameDimesion_1000000() {

		List<Person> persons = createPersons(1000000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		assertEquals(1000000, nameDim.getValueCount());
	}

	private boolean contains(Collection<Integer> toTest, int[] cList) {
		for (int i : cList) {
			if (!toTest.contains(i)) {
				return false;
			}
		}

		return true;
	}

	private boolean ordered(Collection<Integer> toTest, int[] cList) {
		int[] testList = toIntArray(toTest);
		for (int i = 0; i < cList.length; i++) {
			if (testList[i] != cList[i]) {
				return false;
			}
		}

		return true;
	}

	private int[] toIntArray(Collection<Integer> list) {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)
			ret[i++] = e.intValue();
		return ret;
	}
}
