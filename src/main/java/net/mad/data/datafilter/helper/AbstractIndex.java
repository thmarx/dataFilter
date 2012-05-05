package net.mad.data.datafilter.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractIndex<K, V, M extends Map<K, L>, L extends List<V>> {
	M map;

	public AbstractIndex(M m) {
		map = m;
	}

	protected abstract L createList();

	public void put(K key, V value) {
		L list = map.get(key);
		if (list == null) {
			list = createList();
			map.put(key, list);
		}
		list.add(value);
	}

	public V get(K key, int index) {
		L list = map.get(key);
		if (list == null) {
			return null;
		}
		if (index >= list.size() || index < 0) {
			return null;
		}
		return list.get(index);
	}

	public V remove(K key, int index) {
		L list = map.get(key);
		if (list == null) {
			return null;
		}
		if (index >= list.size() || index < 0) {
			return null;
		}
		V v = list.remove(index);
		if (list.size() == 0) {
			map.remove(key);
		}
		return v;
	}

	public int getValueCount() {
		int size = 0;
		for (L list : map.values()) {
			size += list.size();
		}
		return size;
	}

	public int getValueCount(K key) {
		L list = map.get(key);
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	public int getKeyCount() {
		return map.size();
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public boolean containsKey(K key, int index) {
		L list = map.get(key);
		if (list == null) {
			return false;
		}
		if (index >= list.size() || index < 0) {
			return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	
	public boolean containsValue(V targetValue) {
		for (L list : map.values()) {
			for (V value : list) {
				if (targetValue == value)
					return true;
			}
		}
		return false;
	}

	public void clear() {
		map.clear();
	}
}