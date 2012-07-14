package net.araim.tictactoe;

import android.graphics.Point;

public interface IBoardOperationDispatcher {
	boolean dispatchMove(Point p);
}
