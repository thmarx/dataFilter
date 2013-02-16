package de.marx_labs.datafilter.function;
/**
 * 
 * @author marx
 *
 * @param <T>
 * @param <V>
 */
public interface ValueFunktion<T, V> {
	public V value(T type);
}
