package net.araim.tictactoe;

import android.test.AndroidTestCase;

public class GameTest extends AndroidTestCase {

	public void testPuttingValueWhenWonDoesNothing() {
		Game g = new Game();
		g.mark(0, 1);
		g.mark(3, 1);
		g.mark(0, 2);
		g.mark(3, 2);
		g.mark(0, 3);
		g.mark(3, 3);
		g.mark(0, 4);
		g.mark(3, 4);
		g.mark(0, 5);
		g.mark(0, 7);
		assertEquals(true, g.getBoard().isEmpty(0, 7));
	}

	public void testIsWonAfter5InColumn() {
		Game g = new Game();
		g.mark(0, 1);
		g.mark(3, 1);
		g.mark(0, 2);
		g.mark(3, 2);
		g.mark(0, 3);
		g.mark(3, 3);
		g.mark(0, 4);
		g.mark(3, 4);
		g.mark(0, 5);
		assertEquals(true, g.isWon());
	}
}
