package net.araim.tictactoe;

import android.graphics.Point;

public class LocalPlayer extends Player {

	protected IGameInterface igi;

	public LocalPlayer(XO x) {
		super(x);
	}

	@Override
	public void notifyOponentMove(Point p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyMoveWaiting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGameInterface(IGameInterface i) {
		igi = i;
	}

}
