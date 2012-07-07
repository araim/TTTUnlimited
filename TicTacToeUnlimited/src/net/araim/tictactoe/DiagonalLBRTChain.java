package net.araim.tictactoe;

import java.util.Iterator;

import android.graphics.Point;

final class DiagonalLBRTChain extends Chain {

	public DiagonalLBRTChain(XO owner, Point start, Point end, Point... openEnds) {
		super(owner, start, end, openEnds);
		this.start = new Point();
		this.end = new Point();
		this.start.x = start.x < end.x ? start.x : end.x;
		this.end.x = start.x > end.x ? start.x : end.x;
		this.start.y = start.y < end.y ? start.y : end.y;
		this.end.y = start.y > end.y ? start.y : end.y;
		// don't have to calculate the real length as the number of elements
		// on diagonal is the same as on X and Y axis
		length = this.end.x - this.start.x + 1;
	}

	@Override
	public Iterator<Point> iterator() {
		return new DiagonalLBRTChainIterator();
	}

	class DiagonalLBRTChainIterator implements Iterator<Point> {

		private Point p;
		private boolean endReached = false;

		public DiagonalLBRTChainIterator() {
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
				p = new Point(DiagonalLBRTChain.this.start);
			} else {
				p.x += 1;
				p.y += 1;
				endReached = (p.x >= DiagonalLBRTChain.this.end.x && p.y >= DiagonalLBRTChain.this.end.y);
			}
			return p;
		}

		@Override
		public void remove() {

		}

	}

}
