package net.mad.data.datafilter;

import java.util.ArrayList;
import java.util.Collection;

import net.mad.data.datafilter.helper.Dimension;
import net.mad.data.datafilter.helper.ValueAccessorFunktion;

public class DataFilter<T> {
	
	private Collection<T> items = new ArrayList<T>();
	
	public DataFilter() {
		
	}
	
	public void add (T item) {
		items.add(item);
	}
	
	public void remove (T item) {
		items.remove(item);
	}
	
	public void addAll (Collection<T> items) {
		this.items.addAll(items);
	}
	public void removeAll (Collection<T> items) {
		this.items.removeAll(items);
	}
	public void clear () {
		this.items.clear();
	}
	
	public <X> Dimension<X, T> dimension (ValueAccessorFunktion<T, X> vaf, Class<X> clazz) {
		
		Dimension<X, T> dim = new Dimension<X, T>();
		
		for (T value : items) {
			X key = vaf.apply(value);
			
			dim.add(key, value);
		}
		return dim;
	}
	
	public int size () {
		return items.size();
	}
}
