package net.araim.tictactoe.activities;

import net.araim.tictactoe.GameController;
import net.araim.tictactoe.IBoardOperationDispatcher;
import net.araim.tictactoe.IPlayer;
import net.araim.tictactoe.LocalPlayer;
import net.araim.tictactoe.R;
import net.araim.tictactoe.XO;
import net.araim.tictactoe.views.BoardView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class TicTacToeMain extends Activity {

	private static final String TAG = "TicTacToeMain";
	private BoardView bv;
	private GameController gc;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// TODO resume game from state

		IPlayer p1 = new LocalPlayer(XO.X);
		IPlayer p2 = new LocalPlayer(XO.O);
		gc = new GameController(p1, p2);

		// bind the board view with the game controller view
		bv = new BoardView(this, gc.getBoardView());
		
		
		if (p1 instanceof IBoardOperationDispatcher) {
			bv.addOperationListemer((IBoardOperationDispatcher) p1);
		}

		if (p2 instanceof IBoardOperationDispatcher) {
			bv.addOperationListemer((IBoardOperationDispatcher) p2);
		}


		gc.start();

		((RelativeLayout) findViewById(R.id.MainLayout)).addView(bv);
		findViewById(R.id.zoomPlus).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "zoomclicked");
				bv.setZoom(bv.getZoom() + 0.1f);
			}
		});
		findViewById(R.id.zoomPlus).bringToFront();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putParcelable(Consts.CURRENT_GAME_BOARD, board);
		// outState.putParcelable(Consts.CURRENT_GAME_SETTINGS,
		// CurrentGameSettings.getInstance());
		// outState.putInt(Consts.CURRENT_GAME_TURN, turn.intValue());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}