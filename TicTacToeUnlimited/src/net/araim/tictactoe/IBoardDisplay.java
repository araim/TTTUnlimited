package net.araim.tictactoe;

import java.io.Serializable;

import android.graphics.Point;

public interface IBoardDisplay<T extends Serializable> {
	T get(int x, int y);
	T get(Point p);
}
