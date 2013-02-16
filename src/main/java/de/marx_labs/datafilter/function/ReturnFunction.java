package de.marx_labs.datafilter.function;

/**
 * 
 * @author marx
 *
 * @param <T>
 */
public interface ReturnFunction<T> {
	public void handle(T element);
}
