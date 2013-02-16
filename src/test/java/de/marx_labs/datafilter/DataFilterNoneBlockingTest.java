package de.marx_labs.datafilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

import de.marx_labs.datafilter.dimension.Dimension;
import de.marx_labs.datafilter.function.ReturnFunction;
import de.marx_labs.datafilter.function.ValueFunktion;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataFilterNoneBlockingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testAddRemove() {
		DataFilter<Integer> df = DataFilter.builder(Integer.class)
				.synched(false).build();

		df.add(1);

		assertEquals(1, df.size());

		df.remove(1);

		assertEquals(0, df.size());
	}

	@Test
	public void testAddAllRemoveAll() {
		Collection<Integer> items = new ArrayList<Integer>();
		items.add(1);
		items.add(2);
		items.add(3);

		DataFilter<Integer> df = DataFilter.builder(Integer.class)
				.synched(false).build();

		df.addAll(items);
		assertEquals(3, df.size());

		df.removeAll(items);
		assertEquals(0, df.size());
	}

	@Test
	public void testDimension() {
		Collection<Integer> items = new ArrayList<Integer>();
		items.add(1);
		items.add(2);
		items.add(3);

		DataFilter<Integer> df = DataFilter.builder(Integer.class)
				.synched(false).build();
		df.addAll(items);

		df.dimension(new ValueFunktion<Integer, Integer>() {

			public Integer value(Integer type) {
				return type;
			}
		}, Integer.class, new ReturnFunction<Dimension<Integer, Integer>>() {
			@Override
			public void handle(Dimension<Integer, Integer> dimension) {
				assertNotNull(dimension);

				assertEquals(3, dimension.getValueCount());
			}
		});
	}

}
