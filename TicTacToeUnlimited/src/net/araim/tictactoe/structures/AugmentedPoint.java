package net.araim.tictactoe.structures;

import android.graphics.Point;

public class AugmentedPoint<T> {
	private final Point p;
	private final T t;

	public AugmentedPoint(Point p, T t) {
		super();
		if (p == null) {
			throw new IllegalArgumentException("Point cannot be null in Augment Point construction");
		}
		this.p = p;
		this.t = t;
	}

	public T getT() {
		return t;
	}

	public int getX() {
		return p.x;
	}

	public int getY() {
		return p.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AugmentedPoint<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		AugmentedPoint<T> other = (AugmentedPoint<T>) obj;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
}
