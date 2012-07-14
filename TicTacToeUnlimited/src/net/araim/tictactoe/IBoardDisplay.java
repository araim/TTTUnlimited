package net.araim.tictactoe;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import net.araim.tictactoe.structures.AugmentedPoint;
import android.graphics.Point;

public interface IBoardDisplay<T extends Serializable> {
	T get(int x, int y);

	T get(Point p);

	Set<AugmentedPoint<T>> getCut(Point min, Point max);

	Map<Point, T> getCutAsMap(Point min, Point max);

	void addBoardUpdateListener(IBoardUpdateListener lsnr);

	void removeBoardUpdateListener(IBoardUpdateListener lsnr);
}
