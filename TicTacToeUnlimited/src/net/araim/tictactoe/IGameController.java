package net.araim.tictactoe;

import android.graphics.Point;

public interface IGameController {
	public void requestMove(Point p, IPlayer player);

	public IBoardDisplay<XO> getBoardView();
	public XO getCurrentPlayer();
	public void setUpGame();
	
	public void start();
}
