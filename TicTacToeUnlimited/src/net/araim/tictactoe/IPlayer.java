package net.araim.tictactoe;

import android.graphics.Point;

public interface IPlayer {

	public XO getXO();
	
	public void setGameInterface(IGameInterface i);

	public void notifyOponentMove(Point p);

	/**
	 * Notifies player that it's their turn.
	 * 
	 * Upon this method begin called, the implementing player should mark and notify their respective users that it's their turn to make a move.
	 * Unless this method returns, requesting the actual move will not be possible
	 * 
	 *   This method can be called more than once before player requests the move. All subsequent calls however should be ignored.
	 */
	public void notifyMoveWaiting();

}
