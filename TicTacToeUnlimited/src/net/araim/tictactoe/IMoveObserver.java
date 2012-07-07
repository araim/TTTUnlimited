package net.araim.tictactoe;

import android.graphics.Point;

public interface IMoveObserver {
	public void moveMade(Point p, IPlayer player);
}
