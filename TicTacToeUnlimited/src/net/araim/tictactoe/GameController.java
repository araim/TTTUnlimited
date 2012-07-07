package net.araim.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.graphics.Point;
import android.util.Log;

public final class GameController implements IGameController {
	private final Game game;
	private final IPlayer player1;
	private final IPlayer player2;
	private final List<IGameObserver> observers = new ArrayList<IGameObserver>();
	private static Executor eventExecutor;
	static {
		eventExecutor = Executors.newSingleThreadExecutor();
	}

	public GameController(IPlayer p1, IPlayer p2) {
		verifyPlayers(p1, p2);

		player1 = new AsyncPlayer(p1);
		player2 = new AsyncPlayer(p2);

		setupGameInterfaces();

		game = new Game();
	}

	public GameController(IPlayer p1, IPlayer p2, Game g) {
		verifyPlayers(p1, p2);
		player1 = new AsyncPlayer(p1);
		player2 = new AsyncPlayer(p2);
		setupGameInterfaces();
		if (g == null) {
			throw new IllegalArgumentException("Null game was assigned to controller");
		}
		game = g;
	}

	private void setupGameInterfaces() {
		IGameInterface igip1 = new GameInterface(this, player1);
		player1.setGameInterface(igip1);

		IGameInterface igip2 = new GameInterface(this, player2);
		player2.setGameInterface(igip2);

	}

	public void addGameObserver(IGameObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public IBoardDisplay<XO> getBoardView() {
		return game.getBoard();
	}

	@Override
	public XO getCurrentPlayer() {
		return game.getCurrentPlayer();
	}

	public boolean isRunning() {
		return !game.isWon();
	}

	@Override
	public synchronized void requestMove(Point p, IPlayer moveMaker) {
		Log.i("TTT.GameController",
				String.format("Move requested (%s), player: %s, currenPlayer: %s", p, moveMaker, game.getCurrentPlayer()));
		IPlayer current = player1.getXO() == game.getCurrentPlayer() ? player1 : player2;
		if (moveMaker != current) {
			Log.e("GameController", "Cheat attempt - player not-in-turn trying to make a move");
			return;
		}
		IPlayer oponent = player1.getXO() == game.getCurrentPlayer() ? player2 : player1;
		game.mark(p.x, p.y);
		notifyPlayerMove(p.x, p.y, game.getCurrentPlayer());
		oponent.notifyOponentMove(p);
		if (game.isWon()) {
			Log.i("TTT.GameController", String.format("The game has been won by %s", current));
			notifyWon(game.getCurrentPlayer());
		} else {
			game.switchPlayers();
			notifyPlayerChange(game.getCurrentPlayer());
			oponent.notifyMoveWaiting();
		}
	}

	private void notifyPlayerChange(XO xo) {
		final XO fxo = xo;
		for (IGameObserver o : observers) {
			final IGameObserver fgo = o;
			eventExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						fgo.notifyPlayerChange(fxo);
					} catch (Exception e) {
						Log.e("Game", e.toString());
					}
				}
			});
		}
	}

	private void notifyPlayerMove(int x, int y, XO xo) {
		final int fx = x;
		final int fy = y;
		final XO fxo = xo;
		for (IGameObserver o : observers) {
			final IGameObserver fgo = o;
			eventExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						fgo.notifyPlayerMove(fx, fy, fxo);
					} catch (Exception e) {
						Log.e("Game", e.toString());
					}
				}
			});
		}
	}

	private void notifyWon(XO xo) {
		final XO fxo = xo;
		for (IGameObserver o : observers) {
			final IGameObserver fgo = o;
			eventExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						fgo.notifyWon(fxo);
					} catch (Exception e) {
						Log.e("Game", e.toString());
					}
				}
			});
		}
	}

	public boolean removeGameObserver(IGameObserver observer) {
		return observers.remove(observer);
	}

	@Override
	public void start() {
		if (!game.isWon()) {
			IPlayer current = player1.getXO() == game.getCurrentPlayer() ? player1 : player2;
			current.notifyMoveWaiting();
		}
	}

	private void verifyPlayers(IPlayer p1, IPlayer p2) {
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException("Players cannot be null");
		}
		if (p1.getXO() == p2.getXO()) {
			throw new IllegalArgumentException("Player cannot have the same XO");
		}
	}

}
