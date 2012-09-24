package net.araim.tictactoe.AI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import net.araim.tictactoe.Chain;
import net.araim.tictactoe.IBoardDisplay;
import net.araim.tictactoe.IGameInterface;
import net.araim.tictactoe.Player;
import net.araim.tictactoe.XO;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AiPlayer extends Player {
	private static final String TAG = "TTT.AiPlayer";

	private IGameInterface game;
	private IBoardDisplay<XO> board;

	public AiPlayer(XO xo) {
		super(xo);
	}

	public AiPlayer(Parcel in) {
		this(XO.parse(in.readInt()));
	}

	public static Score calculateScore(AttachableProxyBoardView view, XO perspective, Point p) {
		Score s = new Score(0, 0);
		view.attach(p, perspective);
		for (Chain c : Chain.GetChains(view, p, perspective, true)) {
			if (c.getLength() > s.length) {
				s.length = c.getLength();
				s.openEnds = c.getOpenEnds().size();
				s.owner = perspective;
			}
			s.chains++;
		}
		view.detach(p);
		view.attach(p, perspective.complementary());
		for (Chain c : Chain.GetChains(view, p, perspective.complementary(), true)) {
			s.chains++;
			if (c.getLength() > s.length) {
				s.length = c.getLength();
				s.openEnds = c.getOpenEnds().size();
				s.owner = perspective.complementary();
			}
			s.blocks++;
		}
		view.detach(p);
		s.setP(p);
		return s;
	}

	private List<Chain> myChains = new ArrayList<Chain>();
	private List<Chain> oppChains = new ArrayList<Chain>();

	@Override
	public synchronized void notifyOponentMove(Point p) {
		oppChains.addAll(Chain.GetChains(board, p, xo.complementary(), true));
		closeChains(myChains, p);
		closeChains(oppChains, p);
	}

	public void closeChains(List<Chain> list, Point p) {
		for (Iterator<Chain> it = list.iterator(); it.hasNext();) {
			Chain c = it.next();
			if (c.getOpenEnds().remove(p)) {
				if (c.getOpenEnds().size() == 0) {
					it.remove();
				}
			}
		}
	}

	@Override
	public synchronized void notifyMoveWaiting() {
		Point p = null;
		AttachableProxyBoardView view = new AttachableProxyBoardView(board);
		if (myChains.size() > 0) {
			TreeSet<Score> scores = new TreeSet<Score>();
			for (Chain c : myChains) {
				for (Point oe : c.getOpenEnds()) {
					scores.add(calculateScore(view, xo, oe));
				}
			}
			Score s = scores.last();
			Log.d(TAG, String.format("Scores: \n first: %s\n last: %s", scores.first(), scores.last()));
			p = s.getP();

			if (oppChains.size() > 0) {
				TreeSet<Score> opScores = new TreeSet<Score>();
				for (Chain c : oppChains) {
					for (Point oe : c.getOpenEnds()) {
						opScores.add(calculateScore(view, xo.complementary(), oe));
						// detect the situation of shared OpenEnds.
					}
				}
				Score os = opScores.last();
				if (os.compareTo(s) > 0) {
					p = os.getP();
				}
				Log.d(TAG, String.format("Scores: \n my: %s\n op: %s", s, os));
			}
		} else if (oppChains.size() > 0) {
			p = oppChains.get(0).getOpenEnds().remove(0);
			if (oppChains.get(0).getOpenEnds().size() == 0) {
				oppChains.remove(0);
			}
		} else {
			p = new Point(0, 0);
		}
		view.attach(p, xo);
		myChains.addAll(Chain.GetChains(view, p, xo, true));
		view.detach(p);
		closeChains(myChains, p);
		closeChains(oppChains, p);
		game.requestMove(p);
	}

	@Override
	public void setGameInterface(IGameInterface i) {
		game = i;
		board = game.getCurrentBoardView();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(xo.intValue());
	}

	public static final Parcelable.Creator<AiPlayer> CREATOR = new Parcelable.Creator<AiPlayer>() {
		public AiPlayer createFromParcel(Parcel in) {
			return new AiPlayer(in);
		}

		public AiPlayer[] newArray(int size) {
			return new AiPlayer[size];
		}
	};
}
