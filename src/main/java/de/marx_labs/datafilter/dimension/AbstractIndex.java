package de.marx_labs.datafilter.dimension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import de.marx_labs.datafilter.DataFilter;
import de.marx_labs.datafilter.function.ReturnFunction;

public abstract class AbstractIndex<K, V, M extends NavigableMap<K, List<V>>>
		implements Dimension<K, V> {
	M map;

	protected DataFilter<V> dataFilter;

	public AbstractIndex(M m, DataFilter<V> dataFilter) {
		map = m;
		this.dataFilter = dataFilter;
	}

	protected abstract List<V> createList();

	public synchronized void put(K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			list = createList();
			map.put(key, list);
		}
		list.add(value);
	}

	public V get(K key, int index) {
		List<V> list = map.get(key);
		if (list == null) {
			return null;
		}
		if (index >= list.size() || index < 0) {
			return null;
		}
		return list.get(index);
	}

	public synchronized V remove(K key, int index) {
		List<V> list = map.get(key);
		if (list == null) {
			return null;
		}
		if (index >= list.size() || index < 0) {
			return null;
		}
		V v = list.remove(index);
		if (list.isEmpty()) {
			map.remove(key);
		}
		return v;
	}

	@Override
	public int getValueCount() {
		int size = 0;
		for (List<V> list : map.values()) {
			size += list.size();
		}
		return size;
	}

	public int getValueCount(K key) {
		List<V> list = map.get(key);
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public int getKeyCount() {
		return map.size();
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public boolean containsKey(K key, int index) {
		List<V> list = map.get(key);
		if (list == null) {
			return false;
		}
		return index < list.size() && index >= 0;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsValue(V targetValue) {
		for (List<V> list : map.values()) {
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

	@Override
	public void filter(final K from, final K to,
			final ReturnFunction<Collection<V>> returnFunction) {
		dataFilter.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				returnFunction.handle(filter(from, to));
			}
		});
	}

	public void filter(final K key,
			final ReturnFunction<Collection<V>> returnFunction) {
		dataFilter.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				returnFunction.handle(filter(key));
			}
		});
	}

	public void filter(final ReturnFunction<Collection<V>> returnFunction) {
		dataFilter.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				returnFunction.handle(filterAll());
			}
		});
	}
	
	@Override
	public void add(K key, V value) {
		put(key, value);
	}

	// public int size () {
	// return index.size();
	// }

	/**
	 * same as dim.filter(form, to)
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@Override
	public Collection<V> filterRange(K from, K to) {
		return filter(from, to);
	}

	@Override
	public Collection<V> filter(K from, K to) {
		Map<K, List<V>> items = map.subMap(from, true, to, true);

		List<V> result = new ArrayList<>();
		for (List<V> list : items.values()) {
			result.addAll(list);
		}

		return result;
	}

	/**
	 * same as dim.filter();
	 * 
	 * @return
	 */
	@Override
	public Collection<V> filterAll() {
		return filter();
	}

	@Override
	public Collection<V> filter() {
		List<V> result = new ArrayList<>();
		for (List<V> list : map.values()) {
			result.addAll(list);
		}

		return result;
	}

	/**
	 * same as dim.fitler(key);
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Collection<V> filterExact(K key) {
		return filter(key);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Collection<V> filter(K key) {
		return map.get(key);
	}
	
	/**
	 *
	 * @return
	 */
	@Override
	public Set<K> keys () {
		return map.keySet();
	}
}