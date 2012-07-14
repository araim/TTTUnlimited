package net.araim.tictactoe;

import android.graphics.Point;

public class GameInterface implements IGameInterface {
	private final IPlayer plr;
	private final IGameController ctrl;


	public GameInterface(IGameController control, IPlayer player) {
		ctrl = control;
		plr = player;
	}

	@Override
	public void requestMove(Point p) throws IllegalMoveException {
		ctrl.requestMove(p, plr);
	}

	@Override
	public XO getCurrentPlayer() {
		return ctrl.getCurrentPlayer();
	}
}
