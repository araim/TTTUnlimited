package net.araim.tictactoe;

public class Move {
	private final int x;
	private final int y;
	private final XO xo;

	public Move(int x, int y, XO xo) {
		this.x = x;
		this.y = y;
		this.xo = xo;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public XO getXo() {
		return xo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Move [x=").append(x).append(", y=").append(y).append(", xo=").append(xo).append("]");
		return builder.toString();
	}

}
