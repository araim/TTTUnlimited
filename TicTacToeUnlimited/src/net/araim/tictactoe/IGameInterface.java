package net.araim.tictactoe;

import android.graphics.Point;

public interface IGameInterface {
	public void requestMove(Point p) throws IllegalMoveException;
	public XO getCurrentPlayer();
}
