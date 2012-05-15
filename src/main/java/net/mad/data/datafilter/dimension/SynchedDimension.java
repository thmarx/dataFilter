package net.mad.data.datafilter.dimension;

import net.mad.data.datafilter.DataFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class SynchedDimension<K, V> extends AbstractIndex<K, V, ConcurrentSkipListMap<K,ArrayList<V>>,ArrayList<V>> /*implements Dimension<K, V> */{
	
	public SynchedDimension (DataFilter<V> dataFilter) {
		super(new ConcurrentSkipListMap<K, ArrayList<V>>(), dataFilter);
	}
	
	
	public void add (K key, V value) {
		put(key, value);
	}
	
//	public int size () {
//		return index.size();
//	}
	
	/**
	 * same as dim.filter(form, to)
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Collection<V> filterRange (K from, K to) {
		return filter(from, to);
	}
	public Collection<V> filter (K from, K to) {
		Map<K, ArrayList<V>> items = map.subMap(from, true, to, true);
		
		List<V> result = new ArrayList<V>();
		for (List<V> list : items.values()) {
			result.addAll(list);
		}
		
		return result;
	}
	/**
	 * same as dim.filter();
	 * @return
	 */
	public Collection<V> filterAll () {
		return filter();
	}
	public Collection<V> filter () {
		List<V> result = new ArrayList<V>();
		for (List<V> list : map.values()) {
			result.addAll(list);
		}
		
		return result;
	}
	/**
	 * same as dim.fitler(key);
	 * @param key
	 * @return
	 */
	public Collection<V> filterExact (K key) {
		return filter(key);
	}
	public Collection<V> filter (K key) {
		return map.get(key);
	}

	@Override
	protected ArrayList<V> createList() {
		return new ArrayList<V>();
	}
}
