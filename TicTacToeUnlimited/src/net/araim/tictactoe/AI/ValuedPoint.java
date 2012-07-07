package net.araim.tictactoe.AI;

import android.graphics.Point;

public class ValuedPoint extends Point implements Comparable<ValuedPoint> {
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int compareTo(ValuedPoint another) {
		if (this.value != another.value) {
			return another.value - this.value;
		} else if (another.x != this.x) {
			return this.x - another.x;
		} else if (another.y != this.y) {
			return this.y - another.y;
		}
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void substract(int diff) {
		value -= diff;
	}

	public void add(int diff) {
		value += diff;
	}

}
