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
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public final class BoardView extends View implements IPlayerView, IBoardUpdateListener {

	private static final String TAG = "BoardView";
	private Set<IBoardOperationDispatcher> operationDispatchers = new HashSet<IBoardOperationDispatcher>();
	private MisclickClearer misclickTask = new MisclickClearer();
	private Handler handler = new Handler();
	private Point lastMin = new Point(0, 0);
	private Point lastMax = new Point(0, 0);
	private Map<Point, XO> boardCut;

	// private Executor executor = Executors.newSingleThreadExecutor();

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

	private Point tempPoint = null;
	private boolean temp = false;

	private static int minPxSize = 30;
	private int cellSize = minPxSize;
	private boolean any = false;
	private boolean debugMode = false;

	private float zoom = 1;
	private int xoffset = -20;
	private int yoffset = -20;
	private int xLineOffset = 0;
	private int yLineOffset = 0;
	private int dist = 0;
	private int xosize = 0;
	private int padsize = 0;

	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;

	private boolean isMoving = false;

	private float touchX = 0f;
	private float touchY = 0f;

	private int xnum;
	private int ynum;

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

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		// TODO: zoom animation
		Log.d(TAG, "ZoomChange: " + this.zoom + " => " + zoom + " xxchg: " + (getWidth() * (zoom - this.zoom)) + " yxchg: "
				+ (getHeight() * (zoom - this.zoom)));
		Point p1 = getCoordsByPoint(new Point(0, 0));
		this.zoom = zoom;
		calculateSizes();
		Point p2 = getCoordsByPoint(new Point(0, 0));
		xoffset -= (p2.x - p1.x);
		yoffset -= (p2.y - p1.y);
		reduceMoveX(0);
		reduceMoveY(0);
		invalidate();
	}

	private void calculateSizes() {
		int xsize = getWidth();
		int ysize = getHeight();
		minPxSize = Math.min(xsize, ysize) / 10;
		minPxSize = minPxSize < 30 ? 30 : minPxSize;
		this.cellSize = Math.round(minPxSize * zoom);
		this.xnum = (int) (xsize / cellSize);
		this.ynum = (int) (ysize / cellSize) + 1;
		this.xosize = (int) (cellSize * 0.6f);
		this.padsize = (cellSize - xosize) / 2;
		BoardViewPaints.BOARD_PAINT.setStrokeWidth(cellSize / 8);
		BoardViewPaints.BOARD_PAINT.setShader(new RadialGradient(xsize / 2, ysize / 2, xsize, new int[] { Color.WHITE, Color.BLACK,
				Color.BLACK }, new float[] { 0.0f, 0.9f, 0.9f }, Shader.TileMode.CLAMP));
		BoardViewPaints.XO_GENERAL_PAINT.setShader(new RadialGradient(xsize / 2, ysize / 2, xsize, new int[] { Color.WHITE, Color.BLACK },
				new float[] { 0.0f, 0.9f }, Shader.TileMode.CLAMP));

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		calculateSizes();

		float[] lines = new float[(xnum + 1) * 4 + (ynum + 1) * 4];
		xLineOffset = (int) (xoffset % cellSize);
		yLineOffset = (int) (yoffset % cellSize);
		for (int i = 0; i <= xnum; i++) {
			lines[i * 4 + 0] = (float) i * cellSize + xLineOffset;
			lines[i * 4 + 1] = 0f;
			lines[i * 4 + 2] = lines[i * 4 + 0];
			lines[i * 4 + 3] = getHeight();
		}
		int xyArrayOffset = (xnum + 1) * 4;
		for (int i = 0; i <= ynum; i++) {
			lines[xyArrayOffset + i * 4 + 0] = 0f;
			lines[xyArrayOffset + i * 4 + 1] = (float) i * cellSize + yLineOffset;
			lines[xyArrayOffset + i * 4 + 2] = getWidth();
			lines[xyArrayOffset + i * 4 + 3] = lines[xyArrayOffset + i * 4 + 1];
		}
		canvas.drawLines(lines, BoardViewPaints.BOARD_PAINT);

		int leftmost = (int) ((-2) * cellSize + xLineOffset + 1);
		int bottom = (int) ((ynum + 1) * cellSize + yLineOffset + 1);

		int rightmost = (int) (xnum * cellSize + xLineOffset + 1);
		int top = (int) (-1 * cellSize + yLineOffset + 1);

		Point minPoint = getPointByCoords(leftmost, bottom);
		Point maxPoint = getPointByCoords(rightmost, top);

		if (boardCut == null || (minPoint.x < lastMin.x || minPoint.y < lastMin.y) || (maxPoint.x > lastMax.x || maxPoint.y > lastMax.y)) {
			Log.v(TAG, String.format("Board cache miss: (%s,%s), cached: (%s,%s)", minPoint, maxPoint, lastMin, lastMax));
			lastMin = new Point(minPoint.x - Settings.cacheOffset, minPoint.y - Settings.cacheOffset);
			lastMax = new Point(maxPoint.x + Settings.cacheOffset, maxPoint.y + Settings.cacheOffset);
			Map<Point, XO> cut = boardDisplay.getCutAsMap(lastMin, lastMax);
			boardCut = cut;
		} else {
			Log.v(TAG, String.format("Board cache hit: %s,%s", minPoint, maxPoint));
		}

		// draw XO here if applicable
		if (temp) {
			Point tempCoords = getCoordsByPoint(tempPoint);
			canvas.drawCircle(tempCoords.x, tempCoords.y, cellSize / 5, BoardViewPaints.XO_RED_PAINT);
			canvas.drawCircle(tempCoords.x, tempCoords.y, cellSize / 3, BoardViewPaints.XO_RED_PAINT);
			canvas.drawCircle(tempCoords.x, tempCoords.y, 1, BoardViewPaints.XO_RED_PAINT);
		}

		for (Point p : boardCut.keySet()) {
			// filter out the outside of view ones (cached)
			if (p.x >= minPoint.x && p.x <= maxPoint.x && p.y >= minPoint.y && p.y <= maxPoint.y) {
				Point xoCoords = getCoordsByPoint(p);
				setBorderItems(xoCoords.x, xoCoords.y);
				drawXO(canvas, boardCut.get(p), xoCoords.x - cellSize / 2, xoCoords.y - cellSize / 2, BoardViewPaints.XO_GENERAL_PAINT);
			}
		}
		if (debugMode) {
			for (int i = -1; i <= xnum + 1; i++) {
				for (int j = -1; j <= ynum + 1; j++) {
					int l = (int) ((i - 1) * cellSize + xLineOffset + 1);
					int t = (int) (j * cellSize + yLineOffset + 1);
					Point p = getPointByCoords(l, t);
					canvas.drawText(p.x + "," + p.y, l + 3, t + 15, BoardViewPaints.GENERAL_RED);
				}
			}
		}
	}

	private void drawXO(Canvas canvas, XO xo, int left, int top, Paint paint) {
		if (xo == XO.X) {
			float[] pts = new float[] { left + padsize, top + padsize, left + padsize + xosize, top + padsize + xosize,
					left + xosize + padsize, top + padsize, left + padsize, top + padsize + xosize };
			canvas.drawLines(pts, paint);
		} else if (xo == XO.O) {
			canvas.drawCircle(left + cellSize / 2, top + cellSize / 2, xosize / 2, paint);
		}
	}

	/**
	 * Return the center coordinates of a given cell
	 * 
	 * @param p cell numbers (row,col)
	 * @return Point containing center of a cell specified in input
	 */
	private Point getCoordsByPoint(Point p) {
		// 0,0 is normaly centered: width/2, height/2,all we need is an offset
		// and element nuber..
		int xBase = (int) Math.floor(xnum / 2) * cellSize + (cellSize / 2) + xoffset + 1;
		int yBase = (int) Math.ceil(ynum / 2) * cellSize + (cellSize / 2) + yoffset + 1;

		return new Point(xBase + (p.x * cellSize), yBase - (p.y * cellSize));
	}

	private Point getPointByCoords(int x, int y) {

		int xoff = 0;
		int yoff = 0;
		// mirror zero
		if (x - xoffset < 0) {
			xoff = -1;
		}
		if (y - yoffset < 0) {
			yoff = -1;
		}
		return new Point((int) Math.ceil(((x - xoffset) / cellSize) + xoff - xnum / 2), -1
				* (int) Math.floor(((y - yoffset) / cellSize) + yoff - ynum / 2));
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
				if (dist != 0) {
					Log.d(TAG, "zoom+ " + ((float) newDist - (float) dist) / (float) getWidth());
					setZoom(zoom + ((float) newDist - (float) dist) / (float) getWidth());
				}
				// register new distance between fingers
				dist = newDist;
			}

			else if (event.getAction() == MotionEvent.ACTION_UP) {
				// no more pinching
				if (event.getPointerCount() < 2) {
					dist = 0;
				}
				// this is touch - not move. also checking if the board isn't
				// locked and checking if this is not the end of multitouch (if
				// it is, original touch points won't be the same as the current
				// ones)
				if (!isMoving && !boardDisplay.isLocked() && touchX == event.getX() && touchY == event.getY()) {
					dist = 0;
					Point p = getPointByCoords((int) event.getX(), (int) event.getY());
					if (!Settings.misclickPrevention || handleMisclickPrevention(p)) {
						clickBoard(p);
					}
					invalidate();
				} else if (isMoving) {
					isMoving = false;
				}
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				touchX = event.getX();
				touchY = event.getY();
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				int xoff = (int) (event.getX() - touchX);
				int yoff = (int) (event.getY() - touchY);
				if (Math.abs(xoff) > 6 || Math.abs(yoff) > 6) {
					isMoving = true;
				}
				xoffset += reduceMoveX(xoff);
				touchX = event.getX();
				yoffset += reduceMoveY(yoff);
				touchY = event.getY();
				invalidate();
			}
		}
		return true;
	}

	private synchronized boolean handleMisclickPrevention(Point p) {
		handler.removeCallbacks(misclickTask);
		// second click in the same temp
		if (temp && tempPoint.x == p.x && tempPoint.y == p.y) {
			temp = false;
			return true;
		}
		XO xo = boardDisplay.get(p.x, p.y);
		if (xo == null) {
			tempPoint = new Point(p.x, p.y);
			setTempShader(p);
			temp = true;
			handler.postDelayed(misclickTask, Settings.misclickPreventionTimer);
		}
		return false;
	}

	private void setTempShader(Point p) {
		Point tempCoords = getCoordsByPoint(p);
		BoardViewPaints.XO_RED_PAINT.setShader(new RadialGradient(tempCoords.x, tempCoords.y, cellSize / 2, Color.rgb(250, 0, 0), Color
				.rgb(90, 0, 0), Shader.TileMode.CLAMP));
	}

	private int reduceMoveY(int yoff) {
		if (any) {
			int ysize = getHeight();
			if (yoff < 0 && yoffset + yoff <= -((ysize / 2 + ((maxY - 1.5f) * cellSize)))) {
				return (int) -(ysize / 2 + ((maxY - 1.5f) * cellSize) + yoffset);
			} else if (yoff > 0 && yoffset + yoff >= (ysize / 2 - ((minY + 2.5f) * cellSize))) {
				return (int) (ysize / 2 - ((minY + 2.5f) * cellSize) - yoffset);
			}
		}
		return yoff;
	}

	private int reduceMoveX(int xoff) {
		if (any) {
			int xsize = getWidth();
			if (xoff < 0 && xoffset + xoff <= -((xsize / 2 + ((maxX - 1.5f) * cellSize)))) {
				return (int) -(xsize / 2 + ((maxX - 1.5f) * cellSize) + xoffset);
			} else if (xoff > 0 && xoffset + xoff >= (xsize / 2 - ((minX + 2f) * cellSize))) {
				return (int) (xsize / 2 - ((minX + 2f) * cellSize) - xoffset);
			}
		}
		return xoff;
	}

	private class MisclickClearer implements Runnable {
		@Override
		public void run() {
			temp = false;
			invalidate();
			Log.d(TAG, "Clearing temp");
		}

	}

	private void clickBoard(final Point p) {
		Log.d(TAG, String.format("BoardClicked: (x:%d, y:%d)", p.x, p.y));
		boardCut = null;
		for (final IBoardOperationDispatcher dsp : operationDispatchers) {
			if (dsp.dispatchMove(p)) {
				return;
			}
		}
	}

	private void setBorderItems(int x, int y) {
		minX = Math.min(x, minX);
		minY = Math.min(y, minY);
		maxX = Math.max(x, maxX);
		maxY = Math.max(y, maxY);
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
}
