package de.marx_labs.datafilter.dimension;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import de.marx_labs.datafilter.DataFilter;

public class NoSynchedDimension<K, V> extends
		AbstractIndex<K, V, TreeMap<K, List<V>>> {

	public NoSynchedDimension(DataFilter<V> dataFilter) {
		super(new TreeMap<K, List<V>>(), dataFilter);
	}

	@Override
	protected List<V> createList() {
		return new ArrayList<>();
	}
}
