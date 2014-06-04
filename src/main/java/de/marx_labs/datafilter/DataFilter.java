package de.marx_labs.datafilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import de.marx_labs.datafilter.dimension.Dimension;
import de.marx_labs.datafilter.dimension.NoSynchedDimension;
import de.marx_labs.datafilter.dimension.SynchedDimension;
import de.marx_labs.datafilter.function.FilterFunction;
import de.marx_labs.datafilter.function.ReturnFunction;
import de.marx_labs.datafilter.function.ValueFunktion;

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

	int poolSize = 1;
	ExecutorService executorService;

	public static class Builder<T> {
		private boolean synched = false;
		boolean parallel = false;
		int parallelCollectionSize = 50;
		int poolSize = 1;

		public Builder() {

		}

		public Builder<T> synched(boolean synched) {
			this.synched = synched;
			return this;
		}

		public Builder<T> poolSize(int size) {
			this.poolSize = size;
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

		public DataFilter<T> build() {
			if (parallel && !synched) {
				throw new IllegalArgumentException("");
			}
			return new DataFilter<>(this);
		}
	}

	/**
	 * @param clazz
	 *            the type
	 * @return
	 */
	public static <BT> Builder<BT> builder(Class<BT> clazz) {
		return new Builder<BT>();
	}

	/**
	 * private constructor
	 * 
	 * @param builder
	 */
	private DataFilter(Builder<T> builder) {
        this.parallel = builder.parallel;
		this.synched = builder.synched || builder.parallel;

		if (this.synched) {
			items = Collections.synchronizedCollection(new ArrayList<T>());
		} else {
			items = new ArrayList<>();
		}
		executorService = Executors.newFixedThreadPool(1);

		/*
		 * Shutting down the ExecutorService on vm exit
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				executorService.shutdownNow();
			}
		});
	}

	/**
	 * Add a single item to the datafilter.
	 * @param item
	 */
	public void add(T item) {
		items.add(item);
	}
	/**
	 * Remove a single item from the datafilter
	 * @param item
	 */
	public void remove(T item) {
		items.remove(item);
	}
	/**
	 * Add a collection of items to the datafilter
	 * @param items
	 */
	public void addAll(Collection<T> items) {
		this.items.addAll(items);
	}
	/**
	 * Remove a collection if items from the datafilter
	 * @param items
	 */
	public void removeAll(Collection<T> items) {
		this.items.removeAll(items);
	}
	/**
	 * Clear the items.
	 */
	public void clear() {
		this.items.clear();
	}

	public ExecutorService getExecutorService() {
		return this.executorService;
	}

	/**
	 * Create a new dimension for the data in the datafilter
	 * 
     * @param <X>
	 * @param vaf
	 * @param clazz
	 * @return
	 */
	public <X> Dimension<X, T> dimension(ValueFunktion<T, X> vaf,
			Class<X> clazz) {
		Dimension<X, T> dim = null;

		if (synched) {
			dim = new SynchedDimension<>(this);
		} else {
			dim = new NoSynchedDimension<>(this);
		}

		if (parallel) {
			ForkJoinPool pool = new ForkJoinPool();

			DimensionAction<X> action = new DimensionAction<>(vaf, dim,
					new ArrayList<>(items));
			pool.invoke(action);

			try {
				action.get();
			} catch (InterruptedException | ExecutionException e) {
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

	public <X> void dimension(final ValueFunktion<T, X> vaf,
			final Class<X> clazz,
			final ReturnFunction<Dimension<X, T>> returnFunction) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Dimension<X, T> dim = dimension(vaf, clazz);
				returnFunction.handle(dim);
			}
		});
	}

	private static <T> Collection<T> filter(Collection<T> target,
			FilterFunction<T> predicate) {
		Collection<T> result = new ArrayList<>();
		for (T element : target) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}

	public Collection<T> filter(FilterFunction<T> predicate) {
		return filter(items, predicate);
	}

	public void filter(final FilterFunction<T> predicate,
			final ReturnFunction<Collection<T>> returnFunction) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Collection<T> result = new ArrayList<>();
				for (T element : items) {
					if (predicate.apply(element)) {
						result.add(element);
					}
				}
			}
		});
	}

	public int size() {
		return items.size();
	}

	class DimensionAction<X> extends RecursiveAction {

		private ValueFunktion<T, X> vaf;
		private Dimension<X, T> dim;

		private int start = -1;
		private int end;

		private final List<T> itemList;

		public DimensionAction(ValueFunktion<T, X> vaf,
				Dimension<X, T> dim, List<T> itemList) {
			this.vaf = vaf;
			this.dim = dim;
			this.itemList = itemList;
		}

		private DimensionAction(ValueFunktion<T, X> vaf,
				Dimension<X, T> dim, int start, int end, List<T> itemList) {

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
//					System.out.print(i + "-");
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

				invokeAll(new DimensionAction<>(vaf, dim, start, start + part,
						itemList), new DimensionAction<>(vaf, dim, start
						+ part + 1, end, itemList));
			}

		}

	}

}
