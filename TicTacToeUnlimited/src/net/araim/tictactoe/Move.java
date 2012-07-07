package net.araim.tictactoe;

public class Move {
	private final int x;
	private final int y;
	private final XO xo;
	private int hashCode;

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

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Move)) {
			return false;
		}
		Move other = (Move) o;
		return x == other.x && y == other.y && xo == other.xo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = hashCode;
		if (result == 0) {
			result = 17;
			result = 31 * result + x;
			result = 31 * result + y;
			result = 31 * result + xo.intValue();
			hashCode = result;
		}
		return result;
	}
}