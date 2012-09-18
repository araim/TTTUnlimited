package net.araim.tictactoe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.araim.tictactoe.ChainType.GrowthVector;

import android.graphics.Point;

public abstract class Chain implements Iterable<Point> {

	static class ChainComparator implements Comparator<Chain> {

		@Override
		public int compare(Chain c1, Chain c2) {
			int comp = c2.getLength() - c1.getLength();
			if (comp != 0) {
				return comp;
			}
			comp = c2.getOpenEnds().size() - c1.getOpenEnds().size();
			if (comp != 0) {
				return comp;
			}
			comp = c2.getStart().x - c1.getStart().x;
			if (comp != 0) {
				return comp;
			}
			comp = c2.getEnd().x - c1.getEnd().x;
			if (comp != 0) {
				return comp;
			}
			comp = c2.getStart().y - c1.getStart().y;
			if (comp != 0) {
				return comp;
			}
			comp = c2.getEnd().y - c1.getEnd().y;
			if (comp != 0) {
				return comp;
			}
			return 0;
		}
	}

	private static int getChainLength(IBoardDisplay<XO> board, Point p, XO xo, int xvector, int yvector) {
		int offset = 0;
		while (board.get(p.x + (offset + 1) * xvector, (offset + 1) * yvector + p.y) == xo) {
			offset += 1;
		}
		return offset;
	}

	private static List<Chain> getColumnChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly) {
		return getLineChains(board, p, xo, openOnly, ChainType.COLUMN);
	}

	private static List<Chain> getRowChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly) {
		return getLineChains(board, p, xo, openOnly, ChainType.ROW);
	}

	private static List<Chain> getDiagonalLTBRChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly) {
		return getLineChains(board, p, xo, openOnly, ChainType.DIAGONALLTRB);
	}

	private static List<Chain> getDiagonalLBRTChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly) {
		return getLineChains(board, p, xo, openOnly, ChainType.DIAGONALLBRT);
	}

	private static List<Chain> getLineChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly, ChainType type) {
		List<Chain> chains = new ArrayList<Chain>();

		Point start = new Point();
		Point end = new Point();

		GrowthVector gv = type.getVector();
		// check row
		int offset = getChainLength(board, p, xo, gv.getStartX(), gv.getStartY());
		start.x = p.x + offset * gv.getStartX();
		start.y = p.y + offset * gv.getStartY();
		offset = getChainLength(board, p, xo, gv.getEndX(), gv.getEndY());
		end.x = p.x + offset * gv.getEndX();
		end.y = p.y + offset * gv.getEndY();
		// non zero chain

		Point open1 = new Point(start.x + gv.getStartX(), start.y + gv.getStartY());
		Point open2 = new Point(end.x + gv.getEndX(), end.y + gv.getEndY());

		if (board.get(open1) != null) {
			open1 = null;
		}
		if (board.get(open2) != null) {
			open2 = null;
		}

		if (!start.equals(end) && (!openOnly || (open1 != null || open2 != null))) {
			Chain c = null;

			if (open1 != null && open2 != null) {
				c = type.create(xo, start, end, open1, open2);
			} else if (open1 != null) {
				c = type.create(xo, start, end, open1);
			} else if (open2 != null) {
				c = type.create(xo, start, end, open2);
			} else {
				c = type.create(xo, start, end);
			}
			if (c != null) {
				chains.add(c);
			}
		}
		return chains;
	}

	private static List<Chain> getSingletonChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly) {
		List<Chain> chains = new ArrayList<Chain>();

		boolean isOpen1 = false;
		ArrayList<Point> l = new ArrayList<Point>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i != 0 || j != 0) {
					XO roundXO = board.get(p.x + i, p.y + j);
					if (roundXO == null) {
						isOpen1 = true;
						l.add(new Point(p.x + i, p.y + j));
					}
				}
			}
		}
		if (!(openOnly && !isOpen1)) {
			chains.add(new SingletonChain(xo, p, p, l.toArray(new Point[0])));
		}
		return chains;
	}

	public static Chain GetChain(IBoardDisplay<XO> board, Point p, XO xo) {
		return new SingletonChain(xo, p, p, new Point[0]);
	}

	public static List<Chain> GetChains(IBoardDisplay<XO> board, Point p, XO xo, boolean openOnly) {

		List<Chain> chains = new ArrayList<Chain>();

		if (board.get(p.x, p.y) == xo) {
			chains.addAll(getColumnChains(board, p, xo, openOnly));
			chains.addAll(getRowChains(board, p, xo, openOnly));
			chains.addAll(getDiagonalLTBRChains(board, p, xo, openOnly));
			chains.addAll(getDiagonalLBRTChains(board, p, xo, openOnly));
			chains.addAll(getSingletonChains(board, p, xo, openOnly));
		}
		return chains;
	}

	public List<Chain> GetChains(IBoardDisplay<XO> board, Point p, boolean openOnly) {
		XO xo = board.get(p.x, p.y);
		if (xo == null) {
			return new ArrayList<Chain>();
		}
		return GetChains(board, p, xo, openOnly);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Chain [start=" + start + ", end=" + end + ", length=" + length + ", openEnds=" + openEnds + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Chain)) {
			return false;
		}
		Chain other = (Chain) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	protected Point start;
	protected Point end;
	protected int length;
	protected final List<Point> openEnds;
	protected final XO owner;

	public XO getOwner() {
		return owner;
	}

	public Chain(XO owner, Point start, Point end, Point... openEnds) {
		super();
		this.length = 0;
		this.owner = owner;
		this.openEnds = new ArrayList<Point>();
		if (openEnds != null) {
			for (Point p : openEnds) {
				if (p != null) {
					this.openEnds.add(p);
				}
			}
		}
	}

	public List<Point> getOpenEnds() {
		return openEnds;
	}

	public boolean contains(Chain c) {
		// let's try to match type always, assume no non-single chain contains
		// single chains
		if (c.getClass() == this.getClass()) {
			Iterator<Point> i = this.iterator();
			boolean startFound = false;
			boolean endFound = false;
			while (i.hasNext()) {
				Point p = i.next();
				if (p.x == c.start.x && p.y == c.start.y) {
					startFound = true;
				}
				if (p.x == c.end.x && p.y == c.end.y) {
					endFound = true;
				}
				if (startFound && endFound) {
					return true;
				}
			}
		}
		return false;
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public int getLength() {
		return length;
	}

	@Override
	public abstract Iterator<Point> iterator();

}
