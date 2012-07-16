package net.araim.tictactoe.activities;

import net.araim.tictactoe.R;
import net.araim.tictactoe.configuration.Settings;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class TicTacToeConfiguration extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String TAG = "TicTacToeConfiguration";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "TicTacToeConfiguration activity creating");
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "TicTacToeConfiguration activity saving");
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Settings.synchronize(this);
	}
}
