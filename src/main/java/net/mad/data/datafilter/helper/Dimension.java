package net.mad.data.datafilter.helper;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Dimension<K, V> {
	private TreeMap<K, V> index = new TreeMap<K, V>();
	
	public Dimension () {
		
	}
	
	public void add (K key, V value) {
		index.put(key, value);
	}
	
	public int size () {
		return index.size();
	}
	
	public Collection<V> filterRange (K from, K to) {
		Map<K, V> items = index.subMap(from, true, to, true);
		
		return items.values();
	}
}
