package net.araim.tictactoe.configuration;

import net.araim.tictactoe.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	public static int winSize = 5;
	public static int cacheOffset = 10;

	public static volatile int startingPlayer = 0;
	public static volatile boolean misclickPrevention = true;
	public static volatile int misclickPreventionTimer = 3000;

	public static void synchronize(Context ctx) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		misclickPrevention = sp.getBoolean(ctx.getString(R.string.Preferences_Gameplay_MisclickPreventionEnabled_Key), true);
		misclickPreventionTimer = Integer.parseInt(sp.getString(ctx.getString(R.string.Preferences_Gameplay_MisclickTimer_Key), "3000"));
		// misclickPreventionTimer =
		// sp.getInt(ctx.getString(R.string.Preferences_Gameplay_MisclickTimer_Key),
		// 1000);
		startingPlayer = Integer.parseInt(sp.getString(ctx.getString(R.string.Preferences_Gameplay_StartingPlayer_Key), "0"));
		// startingPlayer =
		// sp.getInt(ctx.getString(R.string.Preferences_Gameplay_StartingPlayer_Key),
		// 0);
	}
}
