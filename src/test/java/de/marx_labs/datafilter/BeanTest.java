package de.marx_labs.datafilter;

import java.util.Collection;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import de.marx_labs.datafilter.AbstractTest.Person;
import de.marx_labs.datafilter.dimension.Dimension;
import de.marx_labs.datafilter.function.FilterFunction;
import de.marx_labs.datafilter.function.ValueFunktion;

public class BeanTest extends AbstractTest {

	@Test
	public void noneUniqueName () {
		DataFilter<Person> persons = DataFilter.builder(Person.class).build();
		
		persons.add(new Person("thorsten", 25));
		persons.add(new Person("thorsten", 26));
		
		Collection<Person> namedThorsten = persons.filter(new FilterFunction<Person>() {
			
			@Override
			public boolean apply(Person target) {
				if ("thorsten".equals(target.name)) {
					return true;
				}
				return false;
			}
		});
		
		assertThat(namedThorsten.size(), is(2));
		
		Dimension<String, Person> byName = persons.dimension(new ValueFunktion<AbstractTest.Person, String>() {

			@Override
			public String value(Person type) {
				return type.name;
			}
		}, String.class);
		
		assertThat(byName.filterExact("thorsten").size(), is(2));
	}
}
