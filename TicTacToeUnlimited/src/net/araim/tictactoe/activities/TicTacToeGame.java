package net.araim.tictactoe.activities;

import net.araim.tictactoe.Game;
import net.araim.tictactoe.GameController;
import net.araim.tictactoe.IBoardOperationDispatcher;
import net.araim.tictactoe.IPlayer;
import net.araim.tictactoe.LocalPlayer;
import net.araim.tictactoe.R;
import net.araim.tictactoe.XO;
import net.araim.tictactoe.utils.Consts;
import net.araim.tictactoe.views.BoardView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

public class TicTacToeGame extends Activity {

	private static final String TAG = "TicTacToeMain";
	private volatile View menu = null;
	private BoardView bv;
	private GameController gc;
	private Object menuLock = new Object();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.game);
		IPlayer p1 = null;
		IPlayer p2 = null;
		boolean restore = false;
		if (savedInstanceState != null) {
			// attempt to restore
			Game g = savedInstanceState.getParcelable(Consts.CURRENT_GAME);
			p1 = savedInstanceState.getParcelable(Consts.PLAYER1);
			p2 = savedInstanceState.getParcelable(Consts.PLAYER2);
			gc = new GameController(p1, p2, g);
			restore = true;
		}
		if (!restore) {
			p1 = new LocalPlayer(XO.X);
			p2 = new LocalPlayer(XO.O);
			gc = new GameController(p1, p2);
		}
		// bind the board view with the game controller view
		bv = new BoardView(this, gc.getBoardView());

		if (p1 instanceof IBoardOperationDispatcher) {
			bv.addOperationListemer((IBoardOperationDispatcher) p1);
		}

		if (p2 instanceof IBoardOperationDispatcher) {
			bv.addOperationListemer((IBoardOperationDispatcher) p2);
		}

		if (!restore) {
			gc.start();
		}

		final RelativeLayout mainLayout = ((RelativeLayout) findViewById(R.id.MainLayout));
		mainLayout.addView(bv);

		ZoomControls zc = (ZoomControls) findViewById(R.id.zoomControls);
		zc.setOnZoomInClickListener(new ZoomController(1.15f));
		zc.setOnZoomOutClickListener(new ZoomController(1 / 1.15f));

		View optionsButton = findViewById(R.id.gameOptionsButton);
		optionsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				synchronized (menuLock) {
					if (menu == null) {
						createIngameMenu();
					}
					Animation fadeInAnimation = AnimationUtils.loadAnimation(TicTacToeGame.this, R.anim.fadein);
					menu.startAnimation(fadeInAnimation);
					menu.setVisibility(View.VISIBLE);
				}
			}
		});
		optionsButton.bringToFront();
		zc.bringToFront();
	}

	private void createIngameMenu() {
		ViewStub stub = (ViewStub) findViewById(R.id.inGameMenuStub);
		menu = stub.inflate();
		menu.bringToFront();
		menu.findViewById(R.id.InGameMenu_Resume).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				synchronized (menuLock) {
					menu.clearAnimation();
					menu.setVisibility(View.GONE);
				}
			}
		});
		menu.findViewById(R.id.InGameMenu_Abandon).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(TicTacToeGame.this);
				builder.setMessage(R.string.abandon_game_confirm).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton(R.string.no, null).show();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(Consts.CURRENT_GAME, gc.getGame());
		outState.putParcelable(Consts.PLAYER1, gc.getPlayer1());
		outState.putParcelable(Consts.PLAYER2, gc.getPlayer2());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class ZoomController implements OnClickListener {
		private final float zoomFactor;

		public ZoomController(float factor) {
			zoomFactor = factor;
		}

		@Override
		public void onClick(View v) {
			Log.d(TAG, String.format("Zoom Clicked, zooming %f", zoomFactor));
			if (bv != null) {
				bv.adjustZoom(zoomFactor, false);
			}
		}
	}
}