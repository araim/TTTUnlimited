package net.araim.tictactoe;

import net.araim.tictactoe.structures.SparseArray2D;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public final class Board implements IBoard<XO> {
	SparseArray2D<XO> board = new SparseArray2D<XO>();
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

}
