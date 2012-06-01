package net.mad.data.datafilter.function;
/**
 * 
 * @author marx
 *
 * @param <T>
 */
public interface FilterFunction<T> {
	public boolean apply(T target);
}
