package net.mad.data.datafilter.dimension;

import java.util.Collection;
import java.util.Set;

import net.mad.data.datafilter.function.ReturnFunction;

public interface Dimension<K, V> {
	/**
	 * same as dim.filter(form, to)
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Collection<V> filterRange(K from, K to);

	public Collection<V> filter(K from, K to);

	/**
	 * same as dim.filter();
	 * 
	 * @return
	 */
	public Collection<V> filterAll();

	public Collection<V> filter();

	/**
	 * same as dim.fitler(key);
	 * 
	 * @param key
	 * @return
	 */
	public Collection<V> filterExact(K key);

	public Collection<V> filter(K key);

	/**
	 *
	 */
	public void filter(K from, K to,
			ReturnFunction<Collection<V>> returnFunction);

	/**
	 * Add a element to the dimension
	 * 
	 * @param key
	 * @param value
	 */
	public void add(K key, V value);

	/**
	 * Gets the value count
	 * 
	 * @return
	 */
	public int getValueCount();

	/**
	 * Gets the key count
	 * 
	 * @return
	 */
	public int getKeyCount();
	
	public Set<K> keys();

	public boolean isEmpty();
}
