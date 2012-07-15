package net.araim.tictactoe.activities;

import net.araim.tictactoe.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class TicTacToeStartup extends Activity {
	private static final String TAG = "TicTacToeStartup";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "TicTacToeStartup activity creating");
		setContentView(R.layout.startup);

		findViewById(R.id.Startup_StartGameButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TicTacToeStartup.this, TicTacToeGame.class);
				startActivity(i);
			}
		});
		findViewById(R.id.Startup_ConfigurationButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TicTacToeStartup.this, TicTacToeConfiguration.class);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "TicTacToeStartup activity saving");
	}
}
