package net.araim.tictactoe;

import java.util.Iterator;

import android.graphics.Point;

final class SingletonChain extends Chain {

	public SingletonChain(XO owner, Point start, Point end, Point... openEnds) {
		super(owner, start, end, openEnds);
		this.start = start;
		this.end = end;
		this.length = 1;
	}

	@Override
	public Iterator<Point> iterator() {
		return new SingletonChainIterator();
	}

	class SingletonChainIterator implements Iterator<Point> {

		private boolean endReached = false;

		public SingletonChainIterator() {

		}

		@Override
		public boolean hasNext() {
			return !endReached;
		}

		@Override
		public Point next() {
			if (!hasNext()) {
				return null;
			}
			endReached = true;
			return SingletonChain.this.start;
		}

		@Override
		public void remove() {
		}
	}
}
