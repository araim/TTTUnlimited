package net.araim.tictactoe;

import java.io.Serializable;

import android.os.Parcelable;

public interface IBoard<T extends Serializable> extends IBoardDisplay<T>, Parcelable {
	boolean isEmpty(int x, int y);
	void put(T xo, int x, int y);
	int getElementCount();
}
