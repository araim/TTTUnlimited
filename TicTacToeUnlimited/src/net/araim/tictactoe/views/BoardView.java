package net.araim.tictactoe.views;

import net.araim.tictactoe.IBoardDisplay;
import net.araim.tictactoe.IMoveObserver;
import net.araim.tictactoe.XO;
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

public final class BoardView extends View {

	private static final String TAG = "BoardView";
	private MisclickClearer misclickTask = new MisclickClearer();
	private Handler handler = new Handler();
	private IMoveObserver observer;

	public BoardView(Context context, IBoardDisplay<XO> display) {
		super(context);
		boardDisplay = display;
	}

	private final IBoardDisplay<XO> boardDisplay;

	private Point tempPoint = null;
	private XO tempXO = null;

	private static int minPxSize = 30;
	private int cellSize = minPxSize;
	private boolean any = false;
	private boolean debugMode = true;

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
	private boolean locked = false;

	private float touchX = 0f;
	private float touchY = 0f;

	private int xsize;
	private int ysize;

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
			XO_RED_PAINT.setColor(Color.RED);
		}
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		// TODO: zoom animation
		Log.d(TAG, "ZoomChange: " + this.zoom + " => " + zoom + " xxchg: " + ((xsize * zoom - xsize * this.zoom)) + " yxchg: "
				+ ((ysize * zoom - ysize * this.zoom)));
		Point p1 = getCoordsByPoint(0, 0);
		this.zoom = zoom;
		calculateSizes();
		Point p2 = getCoordsByPoint(0, 0);
		xoffset -= (p2.x - p1.x);
		yoffset -= (p2.y - p1.y);
		reduceMoveX(0);
		reduceMoveY(0);
		invalidate();
	}

	private void calculateSizes() {
		this.xsize = this.getWidth();
		this.ysize = this.getHeight();
		minPxSize = Math.min(this.xsize, this.ysize) / 10;
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

		float[] lines = new float[(xnum + 1) * 4];
		xLineOffset = (int) (xoffset % cellSize);
		for (int i = 0; i <= xnum; i++) {
			lines[i * 4 + 0] = (float) i * cellSize + xLineOffset;
			lines[i * 4 + 1] = 0f;
			lines[i * 4 + 2] = lines[i * 4 + 0];
			lines[i * 4 + 3] = ysize;
		}

		canvas.drawLines(lines, BoardViewPaints.BOARD_PAINT);

		lines = new float[(ynum + 1) * 4];
		yLineOffset = (int) (yoffset % cellSize);
		for (int i = 0; i <= ynum; i++) {
			lines[i * 4 + 0] = 0f;
			lines[i * 4 + 1] = (float) i * cellSize + yLineOffset;
			lines[i * 4 + 2] = xsize;
			lines[i * 4 + 3] = lines[i * 4 + 1];
		}

		canvas.drawLines(lines, BoardViewPaints.BOARD_PAINT);

		for (int i = -1; i <= xnum + 1; i++) {
			for (int j = -1; j <= ynum + 1; j++) {
				int left = (int) (((i - 1)) * cellSize + xLineOffset + 1);
				int top = (int) (j * cellSize + yLineOffset + 1);

				Point p = getPointByCoords(left, top);

				// draw XO here if applicable
				if (tempXO != null && tempPoint.x == p.x && tempPoint.y == p.y) {
					drawXO(canvas, tempXO, top, left, BoardViewPaints.XO_RED_PAINT);
				}

				XO xo = boardDisplay.get(p);
				if (xo != null) {
					drawXO(canvas, xo, top, left, BoardViewPaints.XO_GENERAL_PAINT);
				}
				if (debugMode) {
					canvas.drawText(p.x + "," + p.y, left + 3, top + 15, BoardViewPaints.GENERAL_RED);
				}
			}
		}
	}

	private void drawXO(Canvas canvas, XO xo, int top, int left, Paint paint) {

		if (xo == XO.X) {
			float[] pts = new float[] { left + padsize, top + padsize, left + padsize + xosize, top + padsize + xosize,
					left + xosize + padsize, top + padsize, left + padsize, top + padsize + xosize };
			canvas.drawLines(pts, paint);
		} else if (xo == XO.O) {
			canvas.drawCircle(left + cellSize / 2, top + cellSize / 2, xosize / 2, paint);
		}
	}

	private Point getCoordsByPoint(int x, int y) {
		Point p = new Point();
		// 0,0 is normaly centered: width/2, height/2,all we need is an offset
		// and element nuber..
		y = -y;
		p.set((int) ((Math.floor((xnum / 2))) * cellSize + (cellSize / 2) + xoffset + (x * cellSize) + 1),
				(int) Math.ceil(((ynum / 2) * cellSize + (cellSize / 2) + yoffset + (y * cellSize)) + 1));
		return p;
	}

	private Point getPointByCoords(int x, int y) {
		Point p = new Point();

		int xoff = 0;
		int yoff = 0;
		if (x - xoffset < 0) {
			xoff = -1;
		}
		if (y - yoffset < 0) {
			yoff = -1;
		}
		p.set((int) Math.ceil(((x - xoffset) / cellSize) + xoff - xnum / 2),
				-1 * (int) Math.floor(((y - yoffset) / cellSize) + yoff - ynum / 2));
		return p;
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
					Log.d(TAG, "zoom+ " + ((float) newDist - (float) dist) / (float) xsize);
					setZoom(zoom + ((float) newDist - (float) dist) / (float) xsize);
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
				if (!isMoving && !locked && touchX == event.getX() && touchY == event.getY()) {
					dist = 0;
					Point p = getPointByCoords((int) event.getX(), (int) event.getY());
					clickBoard(p.x, p.y);
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

	private int reduceMoveY(int yoff) {
		if (any) {
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
			if (xoff < 0 && xoffset + xoff <= -((xsize / 2 + ((maxX - 1.5f) * cellSize)))) {
				return (int) -(xsize / 2 + ((maxX - 1.5f) * cellSize) + xoffset);
			} else if (xoff > 0 && xoffset + xoff >= (xsize / 2 - ((minX + 2f) * cellSize))) {
				return (int) (xsize / 2 - ((minX + 2f) * cellSize) - xoffset);
			}
		}
		return xoff;
	}

	private synchronized void clearTemp() {
		// getBoard().put(null, tempPoint.x, tempPoint.y);
		tempPoint = null;
		tempXO = null;
	}

	public synchronized boolean Set(int x, int y, XO who, boolean temp, int timeoutMsec) {

		Log.d(TAG, " Set(" + x + " , " + y + ", " + who + ", " + temp + ", " + timeoutMsec + " )");
		handler.removeCallbacks(misclickTask);
		// second click in the same temp
		if (tempXO != null && tempPoint.x == x && tempPoint.y == y) {
			temp = false;
			clearTemp();
		}
		// first click in temp or new temp
		if (temp && tempXO != null) {
			clearTemp();
			invalidate();
		}
		if (!temp) {
			setBorderItems(x, y);
			any = true;
		}
		XO xo = boardDisplay.get(x, y);
		if (xo == null) {
			if (temp) {
				tempPoint = new Point(x, y);
				tempXO = who;
				handler.postDelayed(misclickTask, timeoutMsec);
				Log.d(TAG, "timer scheduled in " + timeoutMsec + " msecs");
			} else {
				// getBoard().put(who, x, y);
			}

			invalidate();
			return !temp;
		} else {
			return false;
		}
	}

	private class MisclickClearer implements Runnable {

		@Override
		public void run() {
			clearTemp();
			invalidate();
			Log.d(TAG, "Clearing temp");
		}

	}

	private void clickBoard(int x, int y) {
		Log.d(TAG, "BoardClicked: (" + x + "," + y + ")");
		if (observer != null) {
			// observer.moveMade(new Point(x, y));
		}
	}

	private void setBorderItems(int x, int y) {
		if (x < this.minX) {
			this.minX = x;
		}
		if (x > this.maxX) {
			this.maxX = x;
		}
		if (y < this.minY) {
			this.minY = y;
		}
		if (y > this.maxY) {
			this.maxY = y;
		}
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}
}
