package net.araim.tictactoe;

import java.util.List;

import net.araim.tictactoe.utils.TestUtils;

import junit.framework.TestCase;
import android.graphics.Point;

public class ChainTest extends TestCase {

	public void testGetChainsFromEmptyBoard() {
		IBoard<XO> board = new Board();
		assertEquals(0, Chain.GetChains(board, new Point(0, 0), XO.X, false).size());
		assertEquals(0, Chain.GetChains(board, new Point(0, 0), XO.O, false).size());
	}

	public void testGetNotOwnedChainProducesEmptyList() {
		IBoard<XO> board = new Board();
		board.put(XO.X, 0, 0);
		List<Chain> list = Chain.GetChains(board, new Point(0, 0), XO.O, false);
		assertEquals(0, list.size());
	}

	public void testGetChainsFromSinglePoint() {
		IBoard<XO> board = new Board();
		board.put(XO.X, 0, 0);
		List<Chain> list = Chain.GetChains(board, new Point(0, 0), XO.X, false);
		assertEquals(1, list.size());
		Chain c = list.get(0);
		assertEquals(new Point(0, 0), c.getStart());
		assertEquals(new Point(0, 0), c.getEnd());
		assertEquals(1, c.getLength());
		assertEquals(XO.X, c.getOwner());
	}

	public void testGetChainsFromSinglePointHas8OpenEnds() {
		IBoard<XO> board = new Board();
		board.put(XO.X, 0, 0);
		List<Chain> list = Chain.GetChains(board, new Point(0, 0), XO.X, false);
		Chain c = list.get(0);
		assertEquals(8, c.getOpenEnds().size());
		List<Point> oe = c.getOpenEnds();
		assertEquals(true, TestUtils.containsAllAndNoMore(oe, new Point(-1, -1), new Point(-1, 0), new Point(-1, 1), new Point(0, -1),
				new Point(0, 1), new Point(1, -1), new Point(1, 0), new Point(1, 1)));
	}

	public void testGetChainsFromSinglePointHasZeroOpenEndsAndIsNotReturnedWhenOpenOnly() {
		IBoard<XO> board = new Board();
		board.put(XO.X, 0, 0);

		board.put(XO.O, -1, -1);
		board.put(XO.O, -1, 0);
		board.put(XO.O, -1, 1);
		board.put(XO.O, 0, -1);
		board.put(XO.O, 0, 1);
		board.put(XO.O, 1, -1);
		board.put(XO.O, 1, 0);
		board.put(XO.O, 1, 1);

		List<Chain> list = Chain.GetChains(board, new Point(0, 0), XO.X, false);
		Chain c = list.get(0);
		assertEquals(0, c.getOpenEnds().size());
		assertEquals(0, Chain.GetChains(board, new Point(0, 0), XO.X, true).size());
	}

	public void testGetColumnChainSelectedFromTheBeginning() {
		IBoard<XO> board = new Board();
		board.put(XO.X, -1, 0);
		board.put(XO.X, 0, 0);
		board.put(XO.X, 1, 0);
		List<Chain> list = Chain.GetChains(board, new Point(-1, 0), XO.X, false);
		assertEquals(2, list.size());
		Chain c = list.get(0);
		if (c instanceof SingletonChain) {
			c = list.get(1);
		}
		assertEquals(true, c instanceof ColumnChain);
		assertEquals(new Point(-1, 0), c.getStart());
		assertEquals(new Point(1, 0), c.getEnd());
		assertEquals(3, c.getLength());
		assertEquals(XO.X, c.getOwner());
		assertEquals(2, c.getOpenEnds().size());
		List<Point> oe = c.getOpenEnds();
		assertEquals(true, TestUtils.containsAllAndNoMore(oe, new Point(-2, 0), new Point(2, 0)));
	}

	public void testGetColumChainSelectedFromTheMiddle() {
		IBoard<XO> board = new Board();
		board.put(XO.X, -1, 0);
		board.put(XO.X, 0, 0);
		board.put(XO.X, 1, 0);
		List<Chain> list = Chain.GetChains(board, new Point(0, 0), XO.X, false);
		assertEquals(2, list.size());
		Chain c = list.get(0);
		if (c instanceof SingletonChain) {
			c = list.get(1);
		}
		assertEquals(true, c instanceof ColumnChain);
		assertEquals(new Point(-1, 0), c.getStart());
		assertEquals(new Point(1, 0), c.getEnd());
		assertEquals(3, c.getLength());
		assertEquals(XO.X, c.getOwner());
		assertEquals(2, c.getOpenEnds().size());
		List<Point> oe = c.getOpenEnds();
		assertEquals(true, TestUtils.containsAllAndNoMore(oe, new Point(-2, 0), new Point(2, 0)));
	}

	public void testGetColumnChainSelectedWithoutOpenEnds() {
		IBoard<XO> board = new Board();
		board.put(XO.X, -1, 0);
		board.put(XO.X, 0, 0);
		board.put(XO.X, 1, 0);

		board.put(XO.O, -2, 0);
		board.put(XO.O, 2, 0);

		List<Chain> list = Chain.GetChains(board, new Point(-1, 0), XO.X, false);
		assertEquals(2, list.size());
		Chain c = list.get(0);
		if (c instanceof SingletonChain) {
			c = list.get(1);
		}
		assertEquals(true, c instanceof ColumnChain);
		assertEquals(new Point(-1, 0), c.getStart());
		assertEquals(new Point(1, 0), c.getEnd());
		assertEquals(3, c.getLength());
		assertEquals(XO.X, c.getOwner());
		assertEquals(0, c.getOpenEnds().size());
		// only singleton one
		assertEquals(1, Chain.GetChains(board, new Point(-1, 0), XO.X, true).size());
	}

	public void testGetRowChainSelectedFromTheBeginning() {
		IBoard<XO> board = new Board();
		board.put(XO.X, 0, -1);
		board.put(XO.X, 0, 0);
		board.put(XO.X, 0, 1);
		List<Chain> list = Chain.GetChains(board, new Point(0, -1), XO.X, false);
		assertEquals(2, list.size());
		Chain c = list.get(0);
		if (c instanceof SingletonChain) {
			c = list.get(1);
		}
		assertEquals(true, c instanceof RowChain);
		assertEquals(new Point(0, -1), c.getStart());
		assertEquals(new Point(0, 1), c.getEnd());
		assertEquals(3, c.getLength());
		assertEquals(XO.X, c.getOwner());
		assertEquals(2, c.getOpenEnds().size());
		List<Point> oe = c.getOpenEnds();
		assertEquals(true, TestUtils.containsAllAndNoMore(oe, new Point(0, -2), new Point(0, 2)));
	}

	public void testGetRowChainSelectedFromTheMiddle() {
		IBoard<XO> board = new Board();
		board.put(XO.X, 0, -1);
		board.put(XO.X, 0, 0);
		board.put(XO.X, 0, 1);
		List<Chain> list = Chain.GetChains(board, new Point(0, 0), XO.X, false);
		assertEquals(2, list.size());
		Chain c = list.get(0);
		if (c instanceof SingletonChain) {
			c = list.get(1);
		}
		assertEquals(true, c instanceof RowChain);
		assertEquals(new Point(0, -1), c.getStart());
		assertEquals(new Point(0, 1), c.getEnd());
		assertEquals(3, c.getLength());
		assertEquals(XO.X, c.getOwner());
		assertEquals(2, c.getOpenEnds().size());
		List<Point> oe = c.getOpenEnds();
		assertEquals(true, TestUtils.containsAllAndNoMore(oe, new Point(0, -2), new Point(0, 2)));
	}

	public void testGetRowChainSelectedWithoutOpenEnds() {
		IBoard<XO> board = new Board();
		board.put(XO.O, 0, -2);
		board.put(XO.X, 0, -1);
		board.put(XO.X, 0, 0);
		board.put(XO.X, 0, 1);
		board.put(XO.O, 0, 2);
		List<Chain> list = Chain.GetChains(board, new Point(0, -1), XO.X, false);
		assertEquals(2, list.size());
		Chain c = list.get(0);
		if (c instanceof SingletonChain) {
			c = list.get(1);
		}
		assertEquals(true, c instanceof RowChain);
		assertEquals(new Point(0, -1), c.getStart());
		assertEquals(new Point(0, 1), c.getEnd());
		assertEquals(3, c.getLength());
		assertEquals(XO.X, c.getOwner());
		assertEquals(0, c.getOpenEnds().size());
		// only the singleton has open ends
		assertEquals(1, Chain.GetChains(board, new Point(0, -1), XO.X, true).size());
	}

	public void testGetDiagonalLBRTChain() {

	}

	public void testGetDiagonalLTRBChain() {

	}

	public void testGetMixedChains() {

	}

	public void testIncludeChainsOnlyOfSpecificXO() {

	}

	public void testDoNotIncludeClosedChainsIfSoChosen() {
		// IBoard<XO> board = prepareEmptyBoard();
		// assertEquals(0, Chain.GetChains(board, new Point(0, 0), XO.X,
		// false));
	}

}
