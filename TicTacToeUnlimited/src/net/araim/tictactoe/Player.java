package net.araim.tictactoe;

public abstract class Player implements IPlayer {
	protected final XO xo;

	public Player(XO x) {
		if (x == null) {
			throw new IllegalArgumentException("Player has to have an assigned XO");
		}
		xo = x;
	}

	@Override
	public XO getXO() {
		return xo;
	}

}
