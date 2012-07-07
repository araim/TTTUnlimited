package net.araim.tictactoe.AI;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SortedHashSet<E> implements Set<E> {
	private TreeSet<E> ts;
	private HashSet<E> hs;

	public SortedHashSet() {
		ts = new TreeSet<E>();
		hs = new HashSet<E>();
	}

	@Override
	public boolean add(E object) {
		ts.add(object);
		return hs.add(object);
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		ts.addAll(arg0);
		return hs.addAll(arg0);
	}

	@Override
	public void clear() {
		ts.clear();
		hs.clear();
	}

	@Override
	public boolean contains(Object object) {
		return hs.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return hs.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return hs.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return new SortedHashSetIterator<E>(this);
	}

	@Override
	public boolean remove(Object object) {
		ts.remove(object);
		return hs.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		ts.removeAll(arg0);
		return hs.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		ts.retainAll(arg0);
		return hs.retainAll(arg0);
	}

	@Override
	public int size() {
		return hs.size();
	}

	@Override
	public Object[] toArray() {
		return ts.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return ts.toArray(array);
	}

	class SortedHashSetIterator<T> implements Iterator<T> {
		private SortedHashSet<T> iteratedSet;
		private Iterator<T> i;
		private T current;

		public SortedHashSetIterator(SortedHashSet<T> shs) {
			iteratedSet = shs;
			i = iteratedSet.ts.iterator();
		}

		@Override
		public boolean hasNext() {
			return i.hasNext();
		}

		@Override
		public T next() {
			current = i.next();
			return current;
		}

		@Override
		public void remove() {
			i.remove();
			iteratedSet.hs.remove(current);
		}

	}

}
