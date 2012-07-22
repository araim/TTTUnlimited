package net.araim.tictactoe.views;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.araim.tictactoe.IBoardDisplay;
import net.araim.tictactoe.IBoardOperationDispatcher;
import net.araim.tictactoe.IBoardUpdateListener;
import net.araim.tictactoe.IPlayerView;
import net.araim.tictactoe.XO;
import net.araim.tictactoe.configuration.Settings;
import net.araim.tictactoe.structures.AugmentedPoint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public final class BoardView extends View implements IPlayerView, IBoardUpdateListener {

	private static final String TAG = "BoardView";
	private Set<IBoardOperationDispatcher> operationDispatchers = new HashSet<IBoardOperationDispatcher>();
	private Map<Point, XO> boardCut;
	private ZoomState zs = new ZoomState(1f);
	private Object zoomStateGuard = new Object();

	private int initialPxCellSize = 90;
	private int currentPxCellSize = 90;
	private double zoom = 1.0f;

	public BoardView(Context context) {
		super(context);
		boardDisplay = new IBoardDisplay<XO>() {

			@Override
			public XO get(int x, int y) {
				return null;
			}

			@Override
			public XO get(Point p) {
				return null;
			}

			@Override
			public void addBoardUpdateListener(IBoardUpdateListener lsnr) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeBoardUpdateListener(IBoardUpdateListener lsnr) {
				// TODO Auto-generated method stub

			}

			@Override
			public java.util.Set<AugmentedPoint<XO>> getCut(Point min, Point max) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<Point, XO> getCutAsMap(Point min, Point max) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isLocked() {
				return false;
			}
		};
		if (!isInEditMode()) {
			throw new IllegalStateException("This constructor should only be used in edit mode by automated tools");
		}
	}

	public BoardView(Context context, IBoardDisplay<XO> display) {
		super(context);
		boardDisplay = display;
		boardDisplay.addBoardUpdateListener(this);
	}

	private final IBoardDisplay<XO> boardDisplay;

	private static class BoardViewPaints {
		public static Paint BOARD_PAINT = new Paint();
		public static Paint XO_GENERAL_PAINT = new Paint();
		public static Paint XO_RED_PAINT;
		public static Paint GENERAL_RED = new Paint();

		static {
			BOARD_PAINT.setColor(Color.WHITE);

			GENERAL_RED.setColor(Color.RED);
			GENERAL_RED.setStrokeWidth(7f);
			GENERAL_RED.setAntiAlias(true);

			XO_GENERAL_PAINT.setColor(Color.WHITE);
			XO_GENERAL_PAINT.setStrokeWidth(4f);
			XO_GENERAL_PAINT.setAntiAlias(true);
			XO_GENERAL_PAINT.setStyle(Style.STROKE);

			XO_RED_PAINT = new Paint(BOARD_PAINT);
			XO_RED_PAINT.setStyle(Style.STROKE);
			XO_RED_PAINT.setColor(Color.RED);
			XO_RED_PAINT.setAntiAlias(true);
			XO_RED_PAINT.setStrokeWidth(2.5f);
		}
	}

	private int pxTouchX = 0;
	private int pxTouchY = 0;
	private int pxXMoveOffset = 0;
	private int pxYMoveOffset = 0;
	private float xoSizeFactor = 0.6f;

	private Point cutMin = new Point(0, 0);
	private Point cutMax = new Point(0, 0);

	public Point getPointByCoords(int x, int y) {
		// 0,0 is left top.
		// 1,1 is towards right and bottom
		// both sides of 0 (-1,0] and [0,1) are rounded to 0. To help with that
		// we substract one cell from negative ones
		int pxXCellStart = x - pxXMoveOffset;
		if (pxXCellStart < 0) {
			pxXCellStart -= currentPxCellSize;
		}
		int pxYCellStart = y - pxYMoveOffset;
		if (pxYCellStart < 0) {
			pxYCellStart -= currentPxCellSize;
		}
		return new Point(pxXCellStart / currentPxCellSize, pxYCellStart / currentPxCellSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		double currentZoom = zs.getCurrent();
		if (zoom != currentZoom) {
			Log.d(TAG, String.format("pxXMoveOff:%d, currnetZ:%f, zoom:%f", pxXMoveOffset, currentZoom, zoom));
			pxXMoveOffset = (int) Math.round(pxXMoveOffset * (currentZoom / zoom));
			pxXMoveOffset -= (int) Math.round((getWidth() / 2 * (currentZoom - zoom)));
			pxYMoveOffset = (int) Math.round(pxYMoveOffset * (currentZoom / zoom));
			pxYMoveOffset -= (int) Math.round((getHeight() / 2 * (currentZoom - zoom)));
			zoom = currentZoom;
		}
		currentPxCellSize = (int) (initialPxCellSize * zoom);

		int numberOfVerticals = (getWidth() + currentPxCellSize - 1) / currentPxCellSize;
		int numberOfHorizontals = (getHeight() + currentPxCellSize - 1) / currentPxCellSize;

		int pxXoffset = pxXMoveOffset % currentPxCellSize;
		int pxYoffset = pxYMoveOffset % currentPxCellSize;

		float[] lines = new float[4 * (numberOfVerticals + 1 + numberOfHorizontals + 1)];
		Log.d(TAG, String.format("Drawing: %d width, %d verticals", getWidth(), numberOfVerticals));

		for (int i = 0; i <= numberOfVerticals; i++) {
			lines[i * 4 + 0] = (float) i * currentPxCellSize + pxXoffset;
			lines[i * 4 + 1] = 0f;
			lines[i * 4 + 2] = lines[i * 4];
			lines[i * 4 + 3] = getHeight();
		}
		int xyArrayOffset = (numberOfVerticals + 1) * 4;
		for (int i = 0; i <= numberOfHorizontals; i++) {
			lines[xyArrayOffset + i * 4 + 0] = 0f;
			lines[xyArrayOffset + i * 4 + 1] = (float) i * currentPxCellSize + pxYoffset;
			lines[xyArrayOffset + i * 4 + 2] = getWidth();
			lines[xyArrayOffset + i * 4 + 3] = lines[xyArrayOffset + i * 4 + 1];
		}
		canvas.drawLines(lines, BoardViewPaints.BOARD_PAINT);

		//
		// Log.w(TAG, "Coords: " + leftmost + " ; " + bottom + " ; " + rightmost
		// + " ; " + top);
		// Log.w(TAG, "Board:" + this.toString());
		Point minPoint = getPointByCoords(0, 0);
		Point maxPoint = getPointByCoords(getWidth(), getHeight());
		//
		if (boardCut == null || (minPoint.x < cutMin.x || minPoint.y < cutMin.y) || (maxPoint.x > cutMax.x || maxPoint.y > cutMax.y)) {
			Log.v(TAG, String.format("Board cache miss: (%s,%s), cached: (%s,%s)", minPoint, maxPoint, cutMin, cutMax));
			cutMin = new Point(minPoint.x - Settings.cacheOffset, minPoint.y - Settings.cacheOffset);
			cutMax = new Point(maxPoint.x + Settings.cacheOffset, maxPoint.y + Settings.cacheOffset);
			Map<Point, XO> cut = boardDisplay.getCutAsMap(cutMin, cutMax);
			boardCut = cut;
		} else {
			Log.v(TAG, String.format("Board cache hit: %s,%s", minPoint, maxPoint));
		}
		//
		for (Point p : boardCut.keySet()) {
			// filter out the outside of view ones (cached)
			if (p.x >= minPoint.x && p.x <= maxPoint.x && p.y >= minPoint.y && p.y <= maxPoint.y) {
				Point xoCoords = getCoordsByPoint(p.x, p.y);
				drawXO(canvas, boardCut.get(p), xoCoords.x - currentPxCellSize / 2, xoCoords.y - currentPxCellSize / 2,
						BoardViewPaints.XO_GENERAL_PAINT);
			}
		}

		Point p = getCoordsByPoint(4, 4);
		Point p2 = getCoordsByPoint(12, 12);

		drawXO(canvas, XO.X, p.x - currentPxCellSize / 2, p.y - currentPxCellSize / 2, BoardViewPaints.XO_GENERAL_PAINT);
		drawXO(canvas, XO.X, p2.x - currentPxCellSize / 2, p2.y - currentPxCellSize / 2, BoardViewPaints.XO_GENERAL_PAINT);

		// if (!zs.targetReached()) {
		// invalidate();
		// }
		// if (debugMode) {
		// for (int i = -1; i <= xnum + 1; i++) {
		// for (int j = -1; j <= ynum + 1; j++) {
		// int l = (int) ((i - 1) * cellSize + xLineOffset + 1);
		// int t = (int) (j * cellSize + yLineOffset + 1);
		// Point p = getPointByCoords(l, t);
		// canvas.drawText(p.x + "," + p.y, l + 3, t + 15,
		// BoardViewPaints.GENERAL_RED);
		// }
		// }
		// }
		if (!zs.targetReached()) {
			invalidate();
		}
	}

	private void drawXO(Canvas canvas, XO xo, int left, int top, Paint paint) {
		int xoSize = (int) (xoSizeFactor * currentPxCellSize);
		int pad = (currentPxCellSize - xoSize) / 2;
		if (xo == XO.X) {
			left += pad;
			top += pad;
			float[] pts = new float[] { left, top, left + xoSize, top + xoSize, left + xoSize, top, left, top + xoSize };
			canvas.drawLines(pts, paint);
		} else if (xo == XO.O) {
			canvas.drawCircle(left + currentPxCellSize / 2, top + currentPxCellSize / 2, xoSize / 2, paint);
		}
	}

	private Point getCoordsByPoint(int x, int y) {
		// center of a cell
		Point coords = new Point(pxXMoveOffset + x * currentPxCellSize + currentPxCellSize / 2, pxYMoveOffset + y * currentPxCellSize
				+ currentPxCellSize / 2);
		Log.d(TAG, String.format("Getcoords for: (%d,%d) => %s ", x, y, coords));
		return coords;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// two fingers - this is zoom
			if (event.getPointerCount() == 2) {
				// get the positions
				int x1 = (int) event.getX(0);
				int y1 = (int) event.getY(0);
				int x2 = (int) event.getX(1);
				int y2 = (int) event.getY(1);
				// calculate the distance
				int newDist = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
				// if the previous movement of 2 fingers was registered
				// if (dist != 0) {
				// Log.d(TAG, "zoom+ " + ((float) newDist - (float) dist) /
				// (float) getWidth());
				// adjustZoom(1f + ((float) newDist - (float) dist) / (float)
				// getWidth(), true);
				// }
				// // register new distance between fingers
				// dist = newDist;
			}

			else if (event.getAction() == MotionEvent.ACTION_UP) {
				Log.d(TAG, "Point Clicked: " + getPointByCoords((int) event.getX(), (int) event.getY()));

				// // no more pinching
				// if (event.getPointerCount() < 2) {
				// dist = 0;
				// }
				// this is touch - not move. also checking if the board isn't
				// locked and checking if this is not the end of multitouch (if
				// it is, original touch points won't be the same as the current
				// ones)
				// if (!isMoving && !boardDisplay.isLocked() && touchX ==
				// event.getX() && touchY == event.getY()) {
				// dist = 0;
				// Point p = getPointByCoords((int) event.getX(), (int)
				// event.getY());
				// if (!Settings.misclickPrevention ||
				// handleMisclickPrevention(p)) {
				// clickBoard(p);
				// }
				// invalidate();
				// } else if (isMoving) {
				// isMoving = false;
				// }
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				pxTouchX = (int) event.getX();
				pxTouchY = (int) event.getY();
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				int xoff = (int) (event.getX() - pxTouchX);
				int yoff = (int) (event.getY() - pxTouchY);
				Log.d(TAG, String.format("Move by %d, %d", xoff, yoff));
				pxXMoveOffset += xoff;
				pxYMoveOffset += yoff;

				// if (Math.abs(xoff) > 6 || Math.abs(yoff) > 6) {
				// isMoving = true;
				// }
				// xoffset += reduceMoveX(xoff);
				pxTouchX = (int) event.getX();
				// yoffset += reduceMoveY(yoff);
				pxTouchY = (int) event.getY();
				invalidate();
			}
		}
		return true;
	}

	@Override
	public void addOperationListemer(IBoardOperationDispatcher lsnr) {
		operationDispatchers.add(lsnr);
	}

	@Override
	public boolean removeOperationListemer(IBoardOperationDispatcher lsnr) {
		return operationDispatchers.remove(lsnr);
	}

	@Override
	public void updateBoard() {
		Log.d(TAG, "Board Update requested...");
		invalidate();
	}

	public void adjustZoom(float factor, boolean instant) {
		Log.d(TAG, "Zoom adjust, xoffset: " + this.pxXMoveOffset);
		synchronized (zoomStateGuard) {
			zs.adjustTarget(factor, instant);
		}
		invalidate();
	}

}
