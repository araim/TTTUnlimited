package net.araim.tictactoe;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.graphics.Point;
import android.os.Parcel;

class AsyncPlayer implements IPlayer {
	private final IPlayer plr;
	private Executor eventExecutor;

	public AsyncPlayer(IPlayer player) {
		if (player == null) {
			throw new NullPointerException("Player cannot be null");
		}
		plr = player;
		eventExecutor = Executors.newSingleThreadExecutor();
	}

	@Override
	public XO getXO() {
		return plr.getXO();
	}

	@Override
	public void setGameInterface(IGameInterface i) {
		plr.setGameInterface(i);
	}

	@Override
	public synchronized void notifyOponentMove(Point p) {
		final Point fp = p;
		eventExecutor.execute(new Runnable() {

			@Override
			public void run() {
				plr.notifyOponentMove(fp);
			}
		});
	}

	@Override
	public synchronized void notifyMoveWaiting() {
		eventExecutor.execute(new Runnable() {
			@Override
			public void run() {
				plr.notifyMoveWaiting();
			}
		});
	}

	IPlayer getPlayer() {
		return plr;
	}

	@Override
	public int describeContents() {
		return plr.describeContents();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		plr.writeToParcel(dest, flags);
	}

	@Override
	public String toString() {
		return "AsyncPlayer [plr=" + plr + "]";
	}

}
