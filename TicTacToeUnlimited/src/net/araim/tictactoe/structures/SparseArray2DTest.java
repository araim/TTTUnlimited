package net.araim.tictactoe.structures;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;
import net.araim.tictactoe.testutils.TestParcelable;
import android.graphics.Point;
import android.test.AndroidTestCase;

public class SparseArray2DTest extends AndroidTestCase {

	public void testIsInitialyClear() {
		SparseArray2D<TestParcelable> sa2 = new SparseArray2D<TestParcelable>();
		assertEquals(true, sa2.isClear(0, 0));
	}

	public void testDoesTheArrayContainZeroElementsInitially() {
		SparseArray2D<TestParcelable> sa2 = new SparseArray2D<TestParcelable>();
		assertEquals(0, sa2.getAll().size());
	}

	public void testDoesItStoreTheElementsCorrectly() {
		SparseArray2D<TestParcelable> sa2 = new SparseArray2D<TestParcelable>();
		TestParcelable tp = new TestParcelable("TESTSTRING");
		sa2.put(tp, 100, 102);
		assertEquals(tp, sa2.get(100, 102));
	}

	public void testIsElementRemovedCorrectly() {
		SparseArray2D<TestParcelable> sa2 = new SparseArray2D<TestParcelable>();
		TestParcelable tp = new TestParcelable("TESTSTRING");
		sa2.put(tp, 100, 102);
		sa2.clear(100, 102);
		assertEquals(null, sa2.get(100, 102));
	}

	public void testIsArrayEmptyAfterRemovingOnlyElement() {
		SparseArray2D<TestParcelable> sa2 = new SparseArray2D<TestParcelable>();
		TestParcelable tp = new TestParcelable("TESTSTRING");
		sa2.put(tp, 100, 102);
		sa2.clear(100, 102);
		assertEquals(0, sa2.getAll().size());
	}

	public void testIsTheBoardInitiallyEmptyZeroZero() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Assert.assertEquals(true, board.isClear(0, 0));
	}

	public void testIsTheBoardInitiallyEmptyZeroHundred() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Assert.assertEquals(true, board.isClear(0, 100));
	}

	public void testIsTheBoardInitiallyEmptyHundredZero() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Assert.assertEquals(true, board.isClear(100, 0));
	}

	public void testIsTheTenZeroEmptyIfSomeElementsAreFilled() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("a"), 1, 0);
		board.put(new TestParcelable("b"), 0, 1);
		board.put(new TestParcelable("c"), 1, 1);
		board.put(new TestParcelable("d"), -1, -1);
		Assert.assertEquals(true, board.isClear(10, 0));
	}

	public void testAreElementsAroundOnlyOneFilledEmpty() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("a"), 1, 1);
		Assert.assertEquals(true, board.isClear(0, 2));
		Assert.assertEquals(true, board.isClear(1, 2));
		Assert.assertEquals(true, board.isClear(2, 2));
		Assert.assertEquals(true, board.isClear(0, 1));
		Assert.assertEquals(true, board.isClear(2, 1));
		Assert.assertEquals(true, board.isClear(0, 0));
		Assert.assertEquals(true, board.isClear(0, 1));
		Assert.assertEquals(true, board.isClear(0, 2));
	}

	public void testIsTheRandomPositiveFilledNonEmpty() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Random r = new Random();
		int x = r.nextInt(100) + 1;
		int y = r.nextInt(100) + 1;
		board.put(new TestParcelable("a"), x, y);
		Assert.assertEquals(false, board.isClear(x, y));
	}

	public void testIsTheRandomNegativeFilledNonEmpty() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Random r = new Random();
		int x = -(r.nextInt(100) + 1);
		int y = -(r.nextInt(100) + 1);
		board.put(new TestParcelable("a"), x, y);
		Assert.assertEquals(false, board.isClear(x, y));
	}

	public void testIsTheRandomMixedFilledNonEmpty() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Random r = new Random();
		int x = -(r.nextInt(100) + 1);
		int y = r.nextInt(100);
		board.put(new TestParcelable("a"), x, y);
		Assert.assertEquals(false, board.isClear(x, y));
	}

	public void testgetParcelFromOneOneIfOnlyValue() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE"), 1, 1);
		Assert.assertEquals("TESTVALUE", board.get(1, 1).getS());
	}

	public void testgetParcelFromOneOneIfBoardWithManyValues() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE1"), 0, 0);
		board.put(new TestParcelable("TESTVALUE2"), 1, 1);
		board.put(new TestParcelable("TESTVALUE2"), 2, 2);
		Assert.assertEquals("TESTVALUE2", board.get(1, 1).getS());
	}

	public void testToStringEmptyBoard() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Assert.assertEquals("SparseArray2D [map:]", board.toString());
	}

	public void testPuttingElementTwiceReplacesTheOldOne() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE00a"), 0, 0);
		board.put(new TestParcelable("TESTVALUE00b"), 0, 0);
		assertEquals("TESTVALUE00b", board.get(0, 0).getS());
	}

	public void testgetRangeOnlyReturnsSpecifiedRows() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE01"), 0, 1);
		board.put(new TestParcelable("TESTVALUE10"), 1, 0);
		board.put(new TestParcelable("TESTVALUE11"), 1, 1);
		board.put(new TestParcelable("TESTVALUE12"), 1, 2);
		board.put(new TestParcelable("TESTVALUE21"), 2, 1);
		Assert.assertEquals(1, board.getRange(1, 1, 1, 1).size());
		Assert.assertEquals("TESTVALUE11", board.getRange(1, 1, 1, 1).get(0).getValue().getS());
	}

	public void testgetRangeReturnsNothingIfZeroLengthRowIsProvided() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE01"), 0, 1);
		board.put(new TestParcelable("TESTVALUE10"), 1, 0);
		board.put(new TestParcelable("TESTVALUE11"), 1, 1);
		board.put(new TestParcelable("TESTVALUE12"), 1, 2);
		board.put(new TestParcelable("TESTVALUE21"), 2, 1);
		Assert.assertEquals(0, board.getRange(1, 0, 1, 0).size());
		Assert.assertEquals(0, board.getRange(1, 0, 1, 1).size());
		Assert.assertEquals(0, board.getRange(1, 1, 1, 0).size());
	}

	public void testgetEmptyCut() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		Assert.assertEquals(0, board.getCut(new Point(-100, -100), new Point(100, 100)).size());
	}

	public void testgetCutThrowsExceptionIfNullPoints() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		try {
			Assert.assertEquals(0, board.getCut(null, new Point(100, 100)).size());
			fail();
		} catch (IllegalArgumentException iae) {

		}
	}

	public void testgetCutThrowsExceptionIfMaxPointSmallerThanMinPoint() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		try {
			Assert.assertEquals(0, board.getCut(new Point(101, 10), new Point(100, 100)));
			fail();
		} catch (IllegalArgumentException iae) {

		}
	}

	public void testgetCut() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE01"), 0, 1);
		board.put(new TestParcelable("TESTVALUE10"), 1, 0);
		board.put(new TestParcelable("TESTVALUE11"), 1, 1);
		board.put(new TestParcelable("TESTVALUE12"), 1, 2);
		board.put(new TestParcelable("TESTVALUE21"), 2, 1);
		Set<AugmentedPoint<TestParcelable>> set = board.getCut(new Point(0, 0), new Point(1, 1));
		Assert.assertEquals(3, set.size());
		Assert.assertEquals(true, set.contains(new AugmentedPoint<TestParcelable>(new Point(0, 1), new TestParcelable("TESTVALUE01"))));
		Assert.assertEquals(true, set.contains(new AugmentedPoint<TestParcelable>(new Point(1, 0), new TestParcelable("TESTVALUE10"))));
		Assert.assertEquals(true, set.contains(new AugmentedPoint<TestParcelable>(new Point(1, 1), new TestParcelable("TESTVALUE11"))));
	}

	public void testgetCutAsMapThrowsExceptionIfNullPoints() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		try {
			Assert.assertEquals(0, board.getCutAsMap(null, new Point(100, 100)).size());
			fail();
		} catch (IllegalArgumentException iae) {

		}
	}

	public void testgetCutAsMapThrowsExceptionIfMaxPointSmallerThanMinPoint() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		try {
			Assert.assertEquals(0, board.getCutAsMap(new Point(101, 10), new Point(100, 100)));
			fail();
		} catch (IllegalArgumentException iae) {

		}
	}

	public void testgetCutAsMap() {
		SparseArray2D<TestParcelable> board = new SparseArray2D<TestParcelable>();
		board.put(new TestParcelable("TESTVALUE01"), 0, 1);
		board.put(new TestParcelable("TESTVALUE10"), 1, 0);
		board.put(new TestParcelable("TESTVALUE11"), 1, 1);
		board.put(new TestParcelable("TESTVALUE12"), 1, 2);
		board.put(new TestParcelable("TESTVALUE21"), 2, 1);
		Map<Point, TestParcelable> set = board.getCutAsMap(new Point(0, 0), new Point(1, 1));
		Assert.assertEquals(3, set.size());
		Assert.assertEquals(true, set.get(new Point(0, 1)).equals(new TestParcelable("TESTVALUE01")));
		Assert.assertEquals(true, set.get(new Point(1, 0)).equals(new TestParcelable("TESTVALUE10")));
		Assert.assertEquals(true, set.get(new Point(1, 1)).equals(new TestParcelable("TESTVALUE11")));

	}
}
