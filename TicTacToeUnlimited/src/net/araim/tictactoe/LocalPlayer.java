package net.araim.tictactoe;

import android.graphics.Point;
import android.util.Log;

public class LocalPlayer extends Player implements IBoardOperationDispatcher {

	protected IGameInterface igi;
	private static final String TAG = "TTT.LocalPlayer";

	public LocalPlayer(XO x) {
		super(x);
	}

	@Override
	public void notifyOponentMove(Point p) {

	}

	@Override
	public void notifyMoveWaiting() {

	}

	@Override
	public void setGameInterface(IGameInterface i) {
		igi = i;
	}

	@Override
	public boolean dispatchMove(Point p) {
		if (this.xo == igi.getCurrentPlayer()) {
			try {
				igi.requestMove(p);
			} catch (IllegalMoveException ime) {
				Log.d(TAG, String.format("Player attempted to make an check an already occupied cell (%s)", p));
			}
			return true;
		}
		return false;
	}
}
