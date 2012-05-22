package net.mad.data.datafilter.dimension;

import net.mad.data.datafilter.DataFilter;
import net.mad.data.datafilter.function.ReturnFunction;

import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;

public abstract class AbstractIndex<K, V, M extends NavigableMap<K, L>, L extends List<V>>
		implements Dimension<K, V> {
	M map;

	protected DataFilter<V> dataFilter;

	public AbstractIndex(M m, DataFilter<V> dataFilter) {
		map = m;
		this.dataFilter = dataFilter;
	}

	protected abstract L createList();

	public synchronized void put(K key, V value) {
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

	public synchronized V remove(K key, int index) {
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
}