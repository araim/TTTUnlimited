package net.araim.tictactoe;

public interface IGameObserver {
	void notifyWon(XO xo);

	void notifyPlayerMove(int x, int y, XO xo);

	void notifyPlayerChange(XO xo);

}
