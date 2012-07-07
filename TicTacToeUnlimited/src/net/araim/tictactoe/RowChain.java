package net.araim.tictactoe;

import java.util.Iterator;

import android.graphics.Point;

final class RowChain extends Chain {

	public RowChain(XO owner, Point start, Point end, Point... openEnds) {
		super(owner, start, end, openEnds);
		this.start = new Point();
		this.end = new Point();
		this.start.x = start.x;
		this.end.x = start.x;
		this.start.y = start.y < end.y ? start.y : end.y;
		this.end.y = start.y > end.y ? start.y : end.y;
		length = this.end.y - this.start.y + 1;
	}

	@Override
	public Iterator<Point> iterator() {
		return new RowChainIterator();
	}

	class RowChainIterator implements Iterator<Point> {

		private Point p;
		private boolean endReached = false;

		public RowChainIterator() {
			p = null;
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
			if (p == null) {
				p = new Point(RowChain.this.start);
			} else {
				p.x += 1;
				endReached = p.x >= RowChain.this.end.x;
			}
			return p;
		}

		@Override
		public void remove() {

		}
	}
}
