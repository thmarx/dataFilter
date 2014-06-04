package de.marx_labs.datafilter.dimension;

import static org.junit.Assert.assertEquals;

import java.util.List;

import de.marx_labs.datafilter.AbstractTest;
import de.marx_labs.datafilter.DataFilter;
import de.marx_labs.datafilter.function.ValueFunktion;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParallelDimensionTest2 extends AbstractTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Test
	public void testCreateNameDimesion_1000() {

        int count = 10000000;
        
		List<Person> persons = createPersons(count);
		DataFilter<Person> personFilter = DataFilter.builder(Person.class)
				.parallel(true).synched(true).build();
		personFilter.addAll(persons);

        long before = System.currentTimeMillis();
		Dimension<String, Person> nameDim = personFilter.dimension(
				new ValueFunktion<Person, String>() {

                    @Override
					public String value(Person type) {
						return type.name;
					}
				}, String.class);

        long after = System.currentTimeMillis();
        System.out.println( (after-before) + "ms");
        
		assertEquals(count, nameDim.getValueCount());
	}
}
