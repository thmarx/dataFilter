package net.mad.data.datafilter.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Dimension<K, V> extends AbstractIndex<K, V, TreeMap<K,ArrayList<V>>,ArrayList<V>> {
//	private TreeMap<K, V> index = new TreeMap<K, V>();
	
	public Dimension () {
		super(new TreeMap<K, ArrayList<V>>());
	}
	
	public void add (K key, V value) {
		put(key, value);
	}
	
//	public int size () {
//		return index.size();
//	}
	
	public Collection<V> filterRange (K from, K to) {
		Map<K, ArrayList<V>> items = map.subMap(from, true, to, true);
		
		List<V> result = new ArrayList<V>();
		for (List<V> list : items.values()) {
			result.addAll(list);
		}
		
		return result;
	}

	@Override
	protected ArrayList<V> createList() {
		return new ArrayList<V>();
	}
}
