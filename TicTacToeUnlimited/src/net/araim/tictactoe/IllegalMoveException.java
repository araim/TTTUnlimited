package net.araim.tictactoe;

public class IllegalMoveException extends IllegalStateException {

	public IllegalMoveException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * serial ID
	 */
	private static final long serialVersionUID = 7520978584908587226L;

}
