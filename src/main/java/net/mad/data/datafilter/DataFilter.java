package net.mad.data.datafilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.mad.data.datafilter.helper.Dimension;
import net.mad.data.datafilter.helper.NoSynchedDimension;
import net.mad.data.datafilter.helper.SynchedDimension;
import net.mad.data.datafilter.helper.ValueAccessorFunktion;

public class DataFilter<T> {

	private Collection<T> items = new ArrayList<T>();

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
	
	public static <BT> Builder<BT> builder (Class<BT> clazz) {
		return new Builder<BT>();
	}
	
	private DataFilter(Builder builder) {
		this.synched = builder.synched;
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
		if (synched) {
			return dimension_synched(vaf, clazz);
		} else {
			return dimension_nosynched(vaf, clazz);
		}
	}

	private <X> Dimension<X, T> dimension_synched(
			ValueAccessorFunktion<T, X> vaf, Class<X> clazz) {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		Set<Future<X>> set = new HashSet<Future<X>>();
		try {
			Dimension<X, T> dim = new SynchedDimension<X, T>();

			for (T value : items) {

				DimensionCollector<X> callable = new DimensionCollector<X>(vaf,
						dim, value);
				Future<X> future = (Future<X>) pool.submit(callable);
				set.add(future);

				// X key = vaf.apply(value);
				// dim.add(key, value);
			}

			for (Future<X> future : set) {
				future.get();
			}

			return dim;
		} catch (ExecutionException ee) {
			ee.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			pool.shutdown();
		}

		return null;
	}

	private <X> Dimension<X, T> dimension_nosynched(
			ValueAccessorFunktion<T, X> vaf, Class<X> clazz) {

		NoSynchedDimension<X, T> dim = new NoSynchedDimension<X, T>();

		for (T value : items) {

			X key = vaf.apply(value);
			dim.add(key, value);
		}

		return dim;

	}

	public int size() {
		return items.size();
	}

	class DimensionCollector<X> implements Callable<Void> {

		private ValueAccessorFunktion<T, X> vaf;
		private Dimension<X, T> dim;
		private T value;

		public DimensionCollector(ValueAccessorFunktion<T, X> vaf,
				Dimension<X, T> dim, T value) {
			this.vaf = vaf;
			this.dim = dim;
			this.value = value;
		}

		public Void call() throws Exception {
			X key = vaf.apply(value);
			dim.add(key, value);

			return null;
		}

	}
}
