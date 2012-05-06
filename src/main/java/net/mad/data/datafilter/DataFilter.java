package net.mad.data.datafilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.mad.data.datafilter.dimension.Dimension;
import net.mad.data.datafilter.dimension.NoSynchedDimension;
import net.mad.data.datafilter.dimension.SynchedDimension;
import net.mad.data.datafilter.function.FilterFunction;
import net.mad.data.datafilter.function.ValueAccessorFunktion;

public class DataFilter<T> {

	private Collection<T> items = null;

	boolean synched = false;

	public static class Builder<T> {
		private boolean synched = false;
		
		public Builder () {
			
		}
		
		public Builder<T> synched(boolean synched) {
			this.synched = synched;
			return this;
		}
		
		public DataFilter<T> build () {
			return new DataFilter<T>(this);
		}
	}
	
	/**
	 * 
	 * @param clazz the type
	 * @return 
	 */
	public static <BT> Builder<BT> builder (Class<BT> clazz) {
		return new Builder<BT>();
	}
	
	/**
	 * private constructor
	 * 
	 * @param builder
	 */
	private DataFilter(Builder builder) {
		this.synched = builder.synched;
		
		if (this.synched) {
			items = Collections.synchronizedCollection(new ArrayList<T>());
		} else {
			items = new ArrayList<T>();
		}
	}

	public void add(T item) {
		items.add(item);
	}

	public void remove(T item) {
		items.remove(item);
	}

	public void addAll(Collection<T> items) {
		this.items.addAll(items);
	}

	public void removeAll(Collection<T> items) {
		this.items.removeAll(items);
	}

	public void clear() {
		this.items.clear();
	}

	public <X> Dimension<X, T> dimension(ValueAccessorFunktion<T, X> vaf,
			Class<X> clazz) {
		Dimension<X, T> dim = null;
		
		if (synched) {
			dim = new SynchedDimension<X, T>();
		} else {
			dim = new NoSynchedDimension<X, T>();
		}

		for (T value : items) {

			X key = vaf.value(value);
			dim.add(key, value);
		}

		return dim;
	}
	
	public static <T> Collection<T> filter(Collection<T> target, FilterFunction<T> predicate) {
	    Collection<T> result = new ArrayList<T>();
	    for (T element: target) {
	        if (predicate.apply(element)) {
	            result.add(element);
	        }
	    }
	    return result;
	}

	

	public int size() {
		return items.size();
	}

}
