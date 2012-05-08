package net.mad.data.datafilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import net.mad.data.datafilter.dimension.Dimension;
import net.mad.data.datafilter.dimension.NoSynchedDimension;
import net.mad.data.datafilter.dimension.SynchedDimension;
import net.mad.data.datafilter.function.FilterFunction;
import net.mad.data.datafilter.function.ValueAccessorFunktion;

public class DataFilter<T> {

	private Collection<T> items = null;

	/**
	 * use snychronized instances of collection and map
	 */
	boolean synched = false;
	/**
	 * use the java fork/join framework for processing the dimensions
	 */
	boolean parallel = false;
	/**
	 * the max size of the collection, collections greater will be split
	 */
	int parallelCollectionSize = 50;

	public static class Builder<T> {
		private boolean synched = false;
		boolean parallel = false;
		int parallelCollectionSize = 50;
		
		public Builder () {
			
		}
		
		public Builder<T> synched(boolean synched) {
			this.synched = synched;
			return this;
		}
		public Builder<T> parallelCollectionSize(int parallelCollectionSize) {
			this.parallelCollectionSize = parallelCollectionSize;
			return this;
		}
		public Builder<T> parallel(boolean parallel) {
			this.parallel = parallel;
			this.synched = true;
			return this;
		}
		
		public DataFilter<T> build () {
			if (parallel && !synched) {
				throw new IllegalArgumentException("");
			}
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
		this.parallel = builder.parallel;
		
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
		
		if (parallel) {
			ForkJoinPool pool = new ForkJoinPool();
			
			DimensionAction<X> action = new DimensionAction<X>(vaf, dim, new ArrayList<T>(items));
			pool.invoke(action);
			
			try {
				action.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} finally {
				pool.shutdown();
			}
		} else {
			for (T value : items) {

				X key = vaf.value(value);
				dim.add(key, value);
			}
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
	
	class DimensionAction<X> extends RecursiveAction {

		private ValueAccessorFunktion<T, X> vaf;
		private Dimension<X, T> dim;
		
		private int start = -1;
		private int end;
		
		private List<T> itemList;
		
		public DimensionAction(ValueAccessorFunktion<T, X> vaf,
				Dimension<X, T> dim, List<T> itemList) {
			this.vaf = vaf;
			this.dim = dim;
			this.itemList = itemList;
		}
		
		private DimensionAction(
				ValueAccessorFunktion<T, X> vaf,
				Dimension<X, T> dim, 
				int start, 
				int end, 
				List<T> itemList) {
			
			this.vaf = vaf;
			this.dim = dim;
			this.start = start;
			this.end = end;
			this.itemList = itemList;
		}
		
		@Override
		protected void compute() {
			if ((start != -1) && (end - start) <= parallelCollectionSize) {
//				System.out.println(start + " - " + end);
				for (int i = start; i <= end; i++) {
					T value = itemList.get(i);
					X key = vaf.value(value);
					dim.add(key, value);
				}
			} else {
				if (start == -1) {
					start = 0;
					end = itemList.size() - 1;
				}
				int range = end - start;
				int part = range / 2;
				
				invokeAll(new DimensionAction<X>(vaf, dim, start, start+part, itemList),
						new DimensionAction<X>(vaf, dim, start+part+1, end, itemList));
			}
			
		}
		
	}

}
