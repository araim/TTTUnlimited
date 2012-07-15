package net.araim.tictactoe.activities;

import net.araim.tictactoe.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TicTacToeConfiguration extends Activity{
	private static final String TAG = "TicTacToeConfiguration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "TicTacToeStartup activity creating");
		setContentView(R.layout.configuration);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "TicTacToeStartup activity saving");
	}
}
