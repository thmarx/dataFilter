package net.mad.data.datafilter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import net.mad.data.datafilter.helper.Dimension;
import net.mad.data.datafilter.helper.ValueAccessorFunktion;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataFilterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testAddRemove() {
		DataFilter<Integer> df = new DataFilter<Integer>();
		
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
		
		DataFilter<Integer> df = new DataFilter<Integer>();
		
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
		
		DataFilter<Integer> df = new DataFilter<Integer>();
		df.addAll(items);
		
		Dimension<Integer, Integer> dimInt = df.dimension(new ValueAccessorFunktion<Integer, Integer>() {

			public Integer apply(Integer type) {
				return type;
			}
		}, Integer.class);
		
		assertNotNull(dimInt);
		
		assertEquals(3, dimInt.size());
	}

}
