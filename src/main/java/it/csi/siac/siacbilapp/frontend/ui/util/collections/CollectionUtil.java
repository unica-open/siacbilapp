/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;

/**
 * Utility class for collection usage.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/11/2014
 *
 */
public final class CollectionUtil {
	
	/** Just an util class with static methods */
	private CollectionUtil() {
	}
	
	/**
	 * Collects a collection given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceCollection the source collection to extract the data from
	 * @param mapper           the mapping function
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D> Collection<D> map(Collection<S> sourceCollection, Function<S, D> mapper) {
		Collection<D> result = new ArrayList<D>();
		if(sourceCollection == null) {
			return result;
		}
		map(sourceCollection.iterator(), mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection backed by an iterator given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceIterator the source iterator to extract the data from
	 * @param mapper         the mapping function
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D> Collection<D> map(Iterator<S> sourceIterator, Function<S, D> mapper) {
		Collection<D> result = new ArrayList<D>();
		if(sourceIterator == null) {
			return result;
		}
		map(sourceIterator, mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection given the collector into a specified class.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param <C> the collection type
	 * @param sourceCollection the source collection to extract the data from
	 * @param mapper           the mapping function
	 * @param collectionClass  the resulting collection class
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D, C extends Collection<D>> C map(Collection<S> sourceCollection, Function<S, D> mapper, Class<C> collectionClass) {
		C result;
		try {
			result = collectionClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation exception for class " + collectionClass.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Access exception for class " + collectionClass.getSimpleName(), e);
		}
		if(sourceCollection == null) {
			return result;
		}
		map(sourceCollection.iterator(), mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection backed by an iterator given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param <C> the collection type
	 * @param sourceIterator  the source iterator to extract the data from
	 * @param mapper          the mapping function
	 * @param collectionClass the resulting collection class
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D, C extends Collection<D>> Collection<D> map(Iterator<S> sourceIterator, Function<S, D> mapper, Class<C> collectionClass) {
		C result;
		try {
			result = collectionClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation exception for class " + collectionClass.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Access exception for class " + collectionClass.getSimpleName(), e);
		}
		if(sourceIterator == null) {
			return result;
		}
		map(sourceIterator, mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection given the collector into a specified collection.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceCollection      the source collection to extract the data from
	 * @param mapper                the mapping function
	 * @param destinationCollection the destination collection to collect the data into
	 * 
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D> void map(Collection<S> sourceCollection, Function<S, D> mapper, Collection<D> destinationCollection) {
		if(sourceCollection == null) {
			return;
		}
		map(sourceCollection.iterator(), mapper, destinationCollection);
	}
	
	/**
	 * Collects a collection backed by an iterator given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceIterator        the source iterator to extract the data from
	 * @param mapper                the mapping function
	 * @param destinationCollection the destination collection to collect the data into
	 * 
	 * @throws UnsupportedOperationException if the destination exception does not support the {@link Collection#add(Object)} method
	 */
	public static <S, D> void map(Iterator<S> sourceIterator, Function<S, D> mapper, Collection<D> destinationCollection) {
		if(sourceIterator == null || mapper == null) {
			return;
		}
		while(sourceIterator.hasNext()) {
			D collectedValue = mapper.map(sourceIterator.next());
			destinationCollection.add(collectedValue);
		}
	}
	
	/**
	 * Filters a given collection <em>in situ</em>, modifying the original.
	 * @param <S> the source type
	 * @param sourceCollection the collection to filter
	 * @param filter           the filtering function
	 */
	public static <S> void filter(Collection<S> sourceCollection, Filter<S> filter) {
		if(sourceCollection == null || filter == null) {
			return;
		}
		Iterator<S> iterator = sourceCollection.iterator();
		while(iterator != null && iterator.hasNext()) {
			if(!filter.isAcceptable(iterator.next())) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Filters a given collection and applies the data in the given destination collection.
	 * @param <S> the source type
	 * @param sourceCollection      the collection to filter
	 * @param filter                the filtering function
	 * @param destinationCollection the collection in which to add the filtered data
	 * 
	 * @return the filtered collection
	 */
	public static <S> Collection<S> filter(Collection<S> sourceCollection, Filter<S> filter, Collection<S> destinationCollection) {
		if(sourceCollection == null) {
			return destinationCollection;
		}
		if(filter == null) {
			destinationCollection.addAll(sourceCollection);
			return destinationCollection;
		}
		for(S source : sourceCollection) {
			if(filter.isAcceptable(source)) {
				destinationCollection.add(source);
			}
		}
		return destinationCollection;
	}
	
	/**
	 * Filters a given collection and applies the data in a collection of the given class.
	 * @param <S> the source type
	 * @param <C> the collection type
	 * @param sourceCollection the collection to filter
	 * @param filter           the filtering function
	 * @param collectionClass  the class of the resulting collection
	 * 
	 * @return the filtered collection
	 * @see CollectionUtil#filter(Collection, Filter, Collection)
	 */
	@SuppressWarnings("unchecked")
	public static <S, C extends Collection<S>> C filter(Collection<S> sourceCollection, Filter<S> filter, Class<C> collectionClass) {
		C destinationCollection;
		try {
			destinationCollection = collectionClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation exception for class " + collectionClass.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Access exception for class " + collectionClass.getSimpleName(), e);
		}
		if(sourceCollection == null) {
			return destinationCollection;
		}
		return (C) filter(sourceCollection, filter, destinationCollection);
	}
	
	/**
	 * Applies the given predicate to the given collection.
	 * @param <S> the source type
	 * @param collection the collection to filter
	 * @param predicate  the filtering function
	 */
	public static <S> void apply(Collection<S> collection, Predicate<S> predicate) {
		if(collection == null || predicate == null) {
			return;
		}
		for(S source : collection) {
			predicate.apply(source);
		}
	}
	
	// TODO: Not yet implemented
//	public static <S, D> D reduce(Collection<S> collection, Reductor<S, D> reductor) {
//		return reduce(collection.iterator(), reductor);
//	}
//	public static <S, D> D reduce(Iterator<S> iterator, Reductor<S, D> reductor) {
//		return reduce(iterator, reductor, null);
//	}
//	public static <S, D> D reduce(Iterator<S> iterator, Reductor<S, D> reductor, D initialValue) {
//		return null;
//	}
	
	/**
	 * Reduces the values in the given collection to a single accumulated value.
	 * <br/>
	 * The initial value is equals to the first element of the collection.
	 * @param <S> the source type
	 * @param collection the collection to reduce
	 * @param reductor   the reductor
	 * 
	 * @return the reduced value
	 */
	public static <S> S reduce(Collection<S> collection, Reductor<S, S> reductor) {
		if(collection == null || reductor == null) {
			return null;
		}
		Iterator<S> iterator = collection.iterator();
		S initialValue = iterator.next();
		return reduce(iterator, collection, reductor, initialValue);
	}
	
	/**
	 * Reduces the values in the given collection to a single accumulated value.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param collection   the collection to reduce
	 * @param reductor     the reductor
	 * @param initialValue the initial value
	 * 
	 * @return the reduced value
	 */
	public static <S, D> D reduce(Collection<S> collection, Reductor<S, D> reductor, D initialValue) {
		if(collection == null || reductor == null) {
			return null;
		}
		return reduce(collection.iterator(), collection, reductor, initialValue);
	}
	
	/**
	 * Reduces the value of the iterator backing the collection to a single accumulated value.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param iterator     the iterator
	 * @param collection   the collection backing the iterator
	 * @param reductor     the reductor
	 * @param initialValue the initial value
	 * 
	 * @return the reduced value
	 */
	private static <S, D> D reduce(Iterator<S> iterator, Collection<S> collection, Reductor<S, D> reductor, D initialValue) {
		D accumulator = initialValue;
		for(int index = 0; iterator.hasNext(); index++) {
			S currentValue = iterator.next();
			accumulator = reductor.reduce(accumulator, currentValue, index, collection);
		}
		return accumulator;
	}
	
	/**
	 * Adds a char sequence to the collection if it is not null nor empty.
	 * @param <C> the char sequence type
	 * @param collection the collection to populate
	 * @param added      the term to add
	 */
	public static <C extends CharSequence> void addIfNotNullNorEmpty(Collection<C> collection, C added) {
		if(StringUtils.isNotBlank(added)) {
			collection.add(added);
		}
	}
	
	/**
	 * Joins char sequences if they are not null nor empty
	 * @param <C> the char sequence type
	 * @param separator the chunks separator
	 * @param chunks    the chunks to join
	 * @return the joined chunks
	 */
	public static <C extends CharSequence> String join(String separator, C... chunks) {
		Collection<C> coll = new ArrayList<C>();
		for(C chunk : chunks) {
			addIfNotNullNorEmpty(coll, chunk);
		}
		return StringUtils.join(coll, separator);
	}
	
	/**
	 * Obtains a paginated list from a canonical collection, the page number and the total number of records.
	 * @param <T> the collected type
	 * @param collection   the collection to paginate
	 * @param pageNumber   the page number
	 * @param totalRecords the total number of records
	 * 
	 * @return the paginated list as wrapper
	 */
	public static <T> ListaPaginata<T> toListaPaginata(Collection<T> collection, int pageNumber, int totalRecords) {
		ListaPaginataImpl<T> listaPaginata = new ListaPaginataImpl<T>();
		if(collection != null) {
			listaPaginata.addAll(collection);
		}
		
		listaPaginata.setTotaleElementi(totalRecords);
		listaPaginata.setPaginaCorrente(pageNumber);
		
		return listaPaginata;
	}
	
	/**
	 * Rewraps an iterable source in a list, to prevent any unsupported operations on the given iterable
	 * @param <T> the iterable type
	 * @param iterable the iterable source
	 * @return the list rewrapping the iterable source's content
	 */
	public static <T> List<T> rewrap(Iterable<T> iterable) {
		List<T> res = new ArrayList<T>();
		for(T t : iterable) {
			res.add(t);
		}
		return res;
	}
	
	/**
	 * Checks whether the given collection has a single element
	 * @param coll the collection to check
	 * @return <code>true</code> if the collection is not-null and has a single element; <code>false</code> otherwise
	 */
	public static boolean hasSingoloElemento(Collection<?> coll) {
		return coll != null && coll.size() == 1;
	}
	
	/**
	 * Checks whether the given collection has at most a single element
	 * @param coll the collection to check
	 * @return <code>true</code> if the collection is not-null and has at most a single element; <code>false</code> otherwise
	 */
	public static boolean hasAtMostSingoloElemento(Collection<?> coll) {
		return coll != null && coll.size() <= 1;
	}
	
	/**
	 * Gets the first element of a collection (useful for collections without the 'get' method)
	 * @param <E> the collection type
	 * @param coll the collection
	 * @return the first element, if present; <code>null</code> otherwise
	 */
	public static <E> E getFirst(Collection<E> coll) {
		if(coll == null || coll.isEmpty()) {
			// Empty collection: return null
			return null;
		}
		for(E e : coll) {
			// Returns the first element found in the collection
			return e;
		}
		// No elements found: return null (should never get here)
		return null;
	}
}
