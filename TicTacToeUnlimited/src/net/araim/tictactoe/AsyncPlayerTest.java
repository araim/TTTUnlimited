package net.araim.tictactoe;

import android.test.AndroidTestCase;

public class AsyncPlayerTest extends AndroidTestCase {

	public void testNullPlayerThrowsNullPoiter() {
		try {
			new AsyncPlayer(null);
			fail();
		} catch (NullPointerException npe) {

		}
	}
	
}
