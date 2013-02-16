package de.marx_labs.datafilter.function;
/**
 * 
 * @author marx
 *
 * @param <T>
 */
public interface FilterFunction<T> {
	public boolean apply(T target);
}
