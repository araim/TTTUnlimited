package net.araim.tictactoe;

import java.util.Iterator;

import android.graphics.Point;

final class DiagonalLTRBChain extends Chain {

	public DiagonalLTRBChain(XO owner, Point start, Point end, Point... openEnds) {
		super(owner, start, end, openEnds);
		this.start = new Point();
		this.end = new Point();
		this.start.x = start.x < end.x ? start.x : end.x;
		this.end.x = start.x > end.x ? start.x : end.x;
		this.start.y = start.y > end.y ? start.y : end.y;
		this.end.y = start.y < end.y ? start.y : end.y;
		// don't have to calculate the real length as the number of elements
		// on diagonal is the same as on X and Y axis
		length = this.end.x - this.start.x + 1;
	}

	@Override
	public Iterator<Point> iterator() {
		return new DiagonalLTRBChainIterator();
	}

	class DiagonalLTRBChainIterator implements Iterator<Point> {
		private Point p;
		private boolean endReached = false;

		public DiagonalLTRBChainIterator() {
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
				p = new Point(DiagonalLTRBChain.this.start);
			} else {
				p.x += 1;
				p.y -= 1;
				endReached = (p.x >= DiagonalLTRBChain.this.end.x && p.y <= DiagonalLTRBChain.this.end.y);
			}
			return p;

		}

		@Override
		public void remove() {

		}

	}

}
