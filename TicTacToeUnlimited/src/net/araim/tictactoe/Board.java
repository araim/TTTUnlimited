package net.araim.tictactoe;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.araim.tictactoe.structures.AugmentedPoint;
import net.araim.tictactoe.structures.SparseArray2D;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public final class Board implements IBoard<XO> {
	SparseArray2D<XO> board = new SparseArray2D<XO>();
	Set<IBoardUpdateListener> lsnrs = new HashSet<IBoardUpdateListener>();

	private int elementCount = 0;

	public boolean isEmpty(int x, int y) {
		return board.isClear(x, y);
	}

	public XO get(int x, int y) {
		return board.get(x, y);
	}

	public XO get(Point p) {
		return board.get(p.x, p.y);
	}

	public void put(XO xo, int x, int y) {
		if (board.isClear(x, y)) {
			elementCount++;
		}
		board.put(xo, x, y);
		for (IBoardUpdateListener lsnr : lsnrs) {
			lsnr.updateBoard();
		}
	}

	@Override
	public String toString() {
		return "Board [board=" + board + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(board, flags);
	}

	private Board(Parcel in) {
		this.board = in.readParcelable(SparseArray2D.class.getClassLoader());

	}

	public Board() {

	}

	public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
		public Board createFromParcel(Parcel in) {
			return new Board(in);
		}

		public Board[] newArray(int size) {
			return new Board[size];
		}
	};

	@Override
	public int getElementCount() {
		return elementCount;
	}

	@Override
	public void addBoardUpdateListener(IBoardUpdateListener lsnr) {
		lsnrs.add(lsnr);
	}

	@Override
	public void removeBoardUpdateListener(IBoardUpdateListener lsnr) {
		lsnrs.remove(lsnr);
	}

	@Override
	public Set<AugmentedPoint<XO>> getCut(Point min, Point max) {
		return board.getCut(min, max);
	}

	@Override
	public Map<Point, XO> getCutAsMap(Point min, Point max) {
		return board.getCutAsMap(min, max);
	}

}
