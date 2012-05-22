package net.mad.data.datafilter.dimension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import net.mad.data.datafilter.DataFilter;

public class SynchedDimension<K, V>
		extends
		AbstractIndex<K, V, ConcurrentSkipListMap<K, List<V>>> {

	public SynchedDimension(DataFilter<V> dataFilter) {
		super(new ConcurrentSkipListMap<K, List<V>>(), dataFilter);
	}

	@Override
	protected List<V> createList() {
		return Collections.synchronizedList(new ArrayList<V>());
	}
}
