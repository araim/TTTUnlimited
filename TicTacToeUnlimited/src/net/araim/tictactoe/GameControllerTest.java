package net.araim.tictactoe;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.test.AndroidTestCase;
import android.util.Log;

public class GameControllerTest extends AndroidTestCase {

	public void testCreatingNewGameStartsIt() {
		IPlayer p1 = new LocalPlayer(XO.X);
		IPlayer p2 = new LocalPlayer(XO.O);
		GameController gc = new GameController(p1, p2);
		assertEquals(true, gc.isRunning());
	}

	public void testCreatingNewGameWithTheSamePlayerThrowsException() {
		IPlayer p1 = new LocalPlayer(XO.O);
		try {
			new GameController(p1, p1);
			fail("Using two players with the same XO should have thrown an Exception");
		} catch (IllegalArgumentException iae) {
			assertEquals("Player cannot have the same XO", iae.getMessage());
		} catch (Exception e) {
			fail("GameController Initialization has thrown an unexpected exception: " + e.toString());
		}
	}

	public void testCreatingNewGameWithNullPlayerThrowsException() {
		IPlayer p1 = new LocalPlayer(XO.O);
		try {
			new GameController(p1, null);
			fail("Using two players with the same XO should have thrown an Exception");
		} catch (IllegalArgumentException iae) {
			assertEquals("Players cannot be null", iae.getMessage());
		} catch (Exception e) {
			fail("GameController Initialization has thrown an unexpected exception: " + e.toString());
		}
	}

	private static class GameObserverCounter implements IGameObserver {

		public int wins = 0;
		public final List<Move> moves = new ArrayList<Move>();
		public int changes = 0;
		public Object winHandle = new Object();

		@Override
		public synchronized void notifyWon(XO xo) {
			wins++;
			synchronized (winHandle) {
				winHandle.notifyAll();
			}
		}

		@Override
		public synchronized void notifyPlayerMove(int x, int y, XO xo) {
			moves.add(new Move(x, y, xo));
		}

		@Override
		public synchronized void notifyPlayerChange(XO xo) {
			changes++;
		}

	}

	public void testFullEasyGame() {
		IPlayer p1 = new LocalPlayer(XO.X) {
			private int x = 0;
			private int y = 0;

			@Override
			public void notifyMoveWaiting() {
				igi.requestMove(new Point(x, y));
				x += 1;
				Log.i("TTT.PLAYER1", "move request");

			}
		};

		IPlayer p2 = new LocalPlayer(XO.O) {
			private int x = -1;
			private int y = 0;

			@Override
			public void notifyMoveWaiting() {
				igi.requestMove(new Point(x, y));
				y += 1;
				Log.i("TTT.PLAYER2", "move request");
			}
		};

		try {
			GameController gc = new GameController(p1, p2);
			GameObserverCounter goc = new GameObserverCounter();
			gc.addGameObserver(goc);
			synchronized (goc.winHandle) {
				gc.start();
				goc.winHandle.wait(10000);
			}

			assertEquals(1, goc.wins);
			assertEquals(8, goc.changes);
			assertEquals(9, goc.moves.size());

		} catch (IllegalArgumentException iae) {
			assertEquals("Players cannot be null", iae.getMessage());
		} catch (Exception e) {
			fail("GameController Initialization has thrown an unexpected exception: " + e.toString());
		}
	}
}
