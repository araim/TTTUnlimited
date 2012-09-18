package net.araim.tictactoe.AI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.graphics.Point;
import net.araim.tictactoe.IBoardDisplay;
import net.araim.tictactoe.IBoardUpdateListener;
import net.araim.tictactoe.XO;
import net.araim.tictactoe.structures.AugmentedPoint;

public class AttachableProxyBoardView implements IBoardDisplay<XO> {

	private final IBoardDisplay<XO> view;
	private final HashMap<Point, XO> attaches = new HashMap<Point, XO>();

	public AttachableProxyBoardView(IBoardDisplay<XO> original) {
		view = original;
	}

	public void attach(Point p, XO xo) {
		attaches.put(p, xo);
	}

	public void detach(Point p) {
		attaches.remove(p);
	}

	@Override
	public XO get(int x, int y) {
		Point p = new Point(x, y);
		return get(p);
	}

	@Override
	public XO get(Point p) {
		if (attaches.containsKey(p)) {
			return attaches.get(p);
		} else {
			return view.get(p);
		}
	}

	@Override
	public Set<AugmentedPoint<XO>> getCut(Point min, Point max) {
		Set<AugmentedPoint<XO>> s = view.getCut(min, max);
		for (Point p : attaches.keySet()) {
			if (p.x >= min.x && p.x <= max.x && p.y >= min.y && p.y <= max.y) {
				s.add(new AugmentedPoint<XO>(p, attaches.get(p)));
			}
		}
		return s;
	}

	@Override
	public Map<Point, XO> getCutAsMap(Point min, Point max) {
		Map<Point, XO> s = view.getCutAsMap(min, max);
		for (Point p : attaches.keySet()) {
			if (p.x >= min.x && p.x <= max.x && p.y >= min.y && p.y <= max.y) {
				s.put(p, attaches.get(p));
			}
		}
		return s;
	}

	@Override
	public void addBoardUpdateListener(IBoardUpdateListener lsnr) {
		view.addBoardUpdateListener(lsnr);

	}

	@Override
	public void removeBoardUpdateListener(IBoardUpdateListener lsnr) {
		view.removeBoardUpdateListener(lsnr);
	}

	@Override
	public boolean isLocked() {
		return view.isLocked();
	}

}
