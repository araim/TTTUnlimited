package net.araim.tictactoe.AI;

import android.graphics.Point;
import net.araim.tictactoe.IGameInterface;
import net.araim.tictactoe.IPlayer;
import net.araim.tictactoe.XO;

public class AiPlayer implements IPlayer {
//	private static final String TAG = "AiPlayer";

	// private IMoveObserver observer;
	// private XO playersXO;

	// private void LogState(String info) {
	// LogState(info, null);
	// }

//	private void LogState(String info, List<Chain> chains) {
//		StringBuffer sb = new StringBuffer();
//		// if (chains == null) {
//		// sb.append("Player state (" + info + "): \nPromising Chains: ");
//		// for (Chain c : promisingChains) {
//		// sb.append("\n" + c);
//		// }
//		// sb.append("\nOponent chains: ");
//		// for (Chain c : oponentChains) {
//		// sb.append("\n" + c);
//		// }
//		// sb.append("\n\n");
//		// } else
//		sb.append("Pointvals:\n");
//		for (Point p : vpoints.keySet()) {
//			if (vpoints.get(p) > 5) {
//				sb.append(p + " => " + vpoints.get(p) + "\n");
//			}
//		}
//		sb.append("\n");
//		if (chains != null) {
//			sb.append(info);
//			for (Chain c : chains) {
//				sb.append("\n" + c);
//			}
//		}
//		Log.d(TAG, sb.toString());
//	}

//	private Random rand = new Random();
//	private static Comparator<Chain> chainComparator = new ChainComparator();
//	private TreeSet<Chain> promisingChains = new TreeSet<Chain>(chainComparator);
//	private TreeSet<Chain> oponentChains = new TreeSet<Chain>(chainComparator);
//	private HashMap<Point, Integer> vpoints = new HashMap<Point, Integer>();
//	private HashMap<Point, List<Chain>> crossings = new HashMap<Point, List<Chain>>();

	// @Override
	// public Point turnChange(Board b) {
	// try {
	// if (oponentChains.size() == 0) {
	// Log.d(TAG, "default 0,0");
	// MakeMove(new Point(0, 0), b);
	// observer.moveMade(new Point(0, 0));
	// return new Point(0, 0);
	// } else {
	// // deal with impending wins.
	// for (Chain c : promisingChains) {
	// if (c.getLength() == CurrentGameSettings.getInstance().winSize - 1) {
	// Point p = new Point(c.getOpenEnds().get(0));
	// Log.d(TAG, "finishing promising chain: " + c);
	// MakeMove(p, b);
	// observer.moveMade(p);
	// return p;
	// }
	// }
	//
	// // deal with immediate threats
	// for (Chain c : oponentChains) {
	// if (c.getLength() == (CurrentGameSettings.getInstance().winSize - c
	// .getOpenEnds().size())) {
	// Log.d(TAG, "Blocing oponent chain (impending win): "
	// + c);
	// Point p = Block(c);
	// MakeMove(p, b);
	// observer.moveMade(p);
	// return p;
	// }
	// }
	// Point put = null;
	// int curr = 5;
	// for (Point p : vpoints.keySet()) {
	// if (vpoints.get(p) > curr) {
	// put = p;
	// curr = vpoints.get(p);
	// }
	// }
	// if (curr > 5 && put != null) {
	// Log.d(TAG, "Picking valued point: " + put);
	// MakeMove(put, b);
	// observer.moveMade(put);
	// return put;
	// }
	// if (promisingChains.size() > 0) {
	// Chain c = promisingChains.first();
	// if (c.getLength() == CurrentGameSettings.getInstance().winSize
	// - c.getOpenEnds().size()) {
	// Point p = new Point(c.getOpenEnds().get(0));
	// Log.d(TAG, "continuing promising chain: " + c);
	// MakeMove(p, b);
	// observer.moveMade(p);
	// return p;
	// }
	// }
	//
	// }
	// if (promisingChains.size() != 0) {
	// Chain c = promisingChains.first();
	// Log.d(TAG, "Promising chain selected for next move:\n" + c);
	// promisingChains.remove(c);
	// Point p = c.getOpenEnds().get(
	// rand.nextInt(c.getOpenEnds().size()));
	// MakeMove(p, b);
	// observer.moveMade(p);
	// return p;
	// } else {
	// if (oponentChains.size() != 0) {
	// Log.d(TAG,
	// "blocking oponent chain: " + oponentChains.first());
	// Point p = Block(oponentChains.first());
	// MakeMove(p, b);
	// observer.moveMade(p);
	// return p;
	// }
	// }
	// // temp
	// return new Point(0, 0);
	// } finally {
	// LogState("After My Turn");
	// }
	// }
	//
	// private void ProcessCrossings(Point p, Board b) {
	//
	// for (int x = -1; x <= 1; x++) {
	// for (int y = -1; y <= 1; y++) {
	// if (x == 0 && y == 0) {
	// continue;
	// }
	// if (b.isEmpty(p.x + x, p.y + y)) {
	// Point crossing = new Point(p.x + x, p.y + y);
	// RemoveCrossing(crossing);
	// for (Chain c : promisingChains) {
	// if (c.getOpenEnds().contains(crossing)) {
	// AddCrossing(crossing, c);
	// }
	// }
	// for (Chain c : oponentChains) {
	// if (c.getOpenEnds().contains(crossing)) {
	// AddCrossing(crossing, c);
	// }
	// }
	// ScoreCrossing(crossing);
	// }
	// }
	// }
	// }
	//
	// private void MakeMove(Point p, Board b) {
	// Log.d(TAG, "My move: " + p.toString());
	// RemoveCrossing(p);
	// b.Put(playersXO, p.x, p.y);
	// Iterator<Chain> i = promisingChains.iterator();
	// List<Chain> chains = b.GetChains(p, playersXO, true);
	// while (i.hasNext()) {
	// Chain c = i.next();
	// c.getOpenEnds().remove(p);
	// for (Chain newC : chains) {
	// if (newC.contains(c)) {
	// i.remove();
	// }
	// }
	// }
	// LogState("Adding promising chains", chains);
	// promisingChains.addAll(chains);
	//
	// i = oponentChains.iterator();
	// while (i.hasNext()) {
	// Chain c = i.next();
	// c.getOpenEnds().remove(p);
	// if (c.getOpenEnds().size() == 0) {
	// i.remove();
	// }
	// }
	// ProcessCrossings(p, b);
	// }
	//
	// private void ScoreCrossing(Point crossing) {
	// try {
	// ChainScore cs = new ChainScore();
	// for (Chain c : crossings.get(crossing)) {
	// cs.RegisterChain(c);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private void RemoveCrossing(Point p) {
	// vpoints.remove(p);
	// crossings.remove(p);
	// }
	//
	// private void AddCrossing(Point crossing, Chain c) {
	// List<Chain> chains = null;
	// chains = crossings.get(crossing);
	// if (chains == null) {
	// chains = new ArrayList<Chain>();
	// crossings.put(crossing, chains);
	// }
	// chains.add(c);
	// }
	//
	// private Point Block(Chain c) {
	// Point toBlock = c.getOpenEnds().get(
	// rand.nextInt(c.getOpenEnds().size()));
	// int maxval = 0;
	//
	// for (Point p : c.getOpenEnds()) {
	// if (vpoints.containsKey(p) && vpoints.get(p) > maxval) {
	// maxval = vpoints.get(p);
	// toBlock = p;
	// }
	// }
	// return toBlock;
	// }

	@Override
	public XO getXO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyOponentMove(Point p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyMoveWaiting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameInterface(IGameInterface i) {
		// TODO Auto-generated method stub
		
	}

	// @Override
	// public void notifyOponentMove(Point p, Board b) {
	// Log.d(TAG, "Oponent move: " + p.toString());
	// Iterator<Chain> i = oponentChains.iterator();
	// List<Chain> chains = b.GetChains(p, XO.complementary(playersXO), true);
	// while (i.hasNext()) {
	// Chain c = i.next();
	// c.getOpenEnds().remove(p);
	// for (Chain newC : chains) {
	// if (newC.contains(c)) {
	// i.remove();
	// }
	// }
	// }
	// LogState("Adding oponent chains", chains);
	// oponentChains.addAll(chains);
	//
	// i = promisingChains.iterator();
	// while (i.hasNext()) {
	// Chain c = i.next();
	// if (c.getOpenEnds().remove(p))
	// if (c.getOpenEnds().size() == 0) {
	// i.remove();
	// }
	// }
	// ProcessCrossings(p, b);
	// LogState("After Oponent's move");
	// }

}
