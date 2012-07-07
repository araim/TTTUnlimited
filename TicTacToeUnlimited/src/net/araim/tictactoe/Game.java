package net.araim.tictactoe;

import net.araim.tictactoe.configuration.Settings;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

	private final Board board;
	private volatile XO currentPlayer;
	private volatile boolean won;
	private volatile XO wonBy;

	public Game() {
		board = new Board();
		setCurrentPlayer(net.araim.tictactoe.configuration.Settings.startingPlayer);
	}

	public XO getWonBy() {
		if (!won) {
			return null;
		}
		return wonBy;
	}

	public Game(Parcel in) {
		board = in.readParcelable(Board.class.getClassLoader());
		setCurrentPlayer(XO.parse(in.readInt()));
	}

	public boolean isWon() {
		return won;
	}

	public XO getCurrentPlayer() {
		return currentPlayer;
	}

	public void switchPlayers() {
		currentPlayer = XO.complementary(currentPlayer);
	}

	public synchronized void mark(int x, int y) {
		if (!won) {
			board.put(currentPlayer, x, y);
			won = checkWinning(new Point(x, y));
			if (won) {
				wonBy = currentPlayer;
			}
		}

	}

	IBoard<XO> getBoard() {
		return board;
	}

	private boolean checkWinning(Point p) {
		for (Chain c : Chain.GetChains(board, p, currentPlayer, false)) {
			if (c.getLength() == Settings.winSize) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(currentPlayer.intValue());
		dest.writeParcelable(board, flags);
	}

	public void setCurrentPlayer(XO currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
		public Game createFromParcel(Parcel in) {
			return new Game(in);
		}

		public Game[] newArray(int size) {
			return new Game[size];
		}
	};
}