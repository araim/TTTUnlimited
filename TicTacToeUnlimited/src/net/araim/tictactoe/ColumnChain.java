package net.araim.tictactoe;

import java.util.Iterator;

import android.graphics.Point;

final class ColumnChain extends Chain {

	public ColumnChain(XO owner, Point start, Point end, Point... openEnds) {
		super(owner, start, end, openEnds);
		this.start = new Point();
		this.end = new Point();
		this.start.x = start.x < end.x ? start.x : end.x;
		this.end.x = start.x > end.x ? start.x : end.x;
		this.start.y = start.y;
		this.end.y = start.y;
		length = this.end.x - this.start.x + 1;
	} 
 
	@Override
	public Iterator<Point> iterator() {
		return new ColumnChainIterator();
	}

	private class ColumnChainIterator implements Iterator<Point> {

		private Point point;
		private boolean endReached = false;

		public ColumnChainIterator() {
			super();
			point = null;
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
			if (point == null) {
				point = new Point(ColumnChain.this.start);
			} else {
				point.y += 1;
				endReached = point.y >= ColumnChain.this.end.y;
			}
			return point;

		}

		@Override
		public void remove() {

		}
	}

}
