package net.mad.data.datafilter.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Dimension<K, V> {
	/**
	 * same as dim.filter(form, to)
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Collection<V> filterRange (K from, K to);
	public Collection<V> filter (K from, K to);
	/**
	 * same as dim.filter();
	 * @return
	 */
	public Collection<V> filterAll ();
	public Collection<V> filter ();
	/**
	 * same as dim.fitler(key);
	 * @param key
	 * @return
	 */
	public Collection<V> filterExact (K key);
	public Collection<V> filter (K key);
	
	public void add (K key, V value);
	public int getValueCount();
}
