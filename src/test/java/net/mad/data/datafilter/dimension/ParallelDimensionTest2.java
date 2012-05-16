package net.mad.data.datafilter.dimension;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.mad.data.datafilter.AbstractTest;
import net.mad.data.datafilter.DataFilter;
import net.mad.data.datafilter.dimension.Dimension;
import net.mad.data.datafilter.function.ValueAccessorFunktion;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParallelDimensionTest2 extends AbstractTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testCreateNameDimesion_1000() {

		List<Person> persons = createPersons(1000);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueAccessorFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, String.class);

		assertEquals(1000, nameDim.getValueCount());
	}
}
