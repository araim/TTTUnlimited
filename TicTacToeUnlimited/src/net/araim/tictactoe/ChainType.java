package net.araim.tictactoe;

import android.graphics.Point;

enum ChainType {
	SINGLETON {
		@Override
		GrowthVector getVector() {
			return null;
		}

		@Override
		Chain create(XO owner, Point start, Point end, Point... openEnds) {
			return new SingletonChain(owner, start, end, openEnds);
		}

	},
	ROW {
		@Override
		GrowthVector getVector() {
			return ROW_GROWTH_VECTOR;
		}

		@Override
		Chain create(XO owner, Point start, Point end, Point... openEnds) {
			return new RowChain(owner, start, end, openEnds);
		}
	},
	COLUMN {
		@Override
		GrowthVector getVector() {
			return COLUMN_GROWTH_VECTOR;
		}

		@Override
		Chain create(XO owner, Point start, Point end, Point... openEnds) {
			return new ColumnChain(owner, start, end, openEnds);
		}
	},
	DIAGONALLTRB {
		@Override
		GrowthVector getVector() {
			return DIAGONALLTRB_GROWTH_VECTOR;
		}

		@Override
		Chain create(XO owner, Point start, Point end, Point... openEnds) {
			return new DiagonalLTRBChain(owner, start, end, openEnds);
		}
	},
	DIAGONALLBRT

	{
		@Override
		GrowthVector getVector() {
			return DIAGONALLBRTGROWTH_VECTOR;
		}

		@Override
		Chain create(XO owner, Point start, Point end, Point... openEnds) {
			return new DiagonalLBRTChain(owner, start, end, openEnds);
		}
	};

	static class GrowthVector {
		private final int startX;
		private final int endX;
		private final int startY;
		private final int endY;

		public GrowthVector(int[] vector) {
			this(vector[0], vector[1], vector[2], vector[3]);
		}

		public GrowthVector(int startX, int startY, int endX, int endY) {
			super();
			this.startX = startX;
			this.endX = endX;
			this.startY = startY;
			this.endY = endY;
		}

		public int getStartX() {
			return startX;
		}

		public int getEndX() {
			return endX;
		}

		public int getStartY() {
			return startY;
		}

		public int getEndY() {
			return endY;
		}

	}

	private static final GrowthVector ROW_GROWTH_VECTOR = new GrowthVector(0, -1, 0, 1);
	private static final GrowthVector COLUMN_GROWTH_VECTOR = new GrowthVector(-1, 0, 1, 0);
	private static final GrowthVector DIAGONALLTRB_GROWTH_VECTOR = new GrowthVector(-1, 1, 1, -1);
	private static final GrowthVector DIAGONALLBRTGROWTH_VECTOR = new GrowthVector(-1, -1, 1, 1);

	abstract GrowthVector getVector();

	abstract Chain create(XO owner, Point start, Point end, Point... openEnds);

}
