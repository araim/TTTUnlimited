package net.araim.tictactoe.configuration;

import net.araim.tictactoe.XO;

public class Settings {
	public static int winSize = 5;
	public static int confirmationTime = 3;
	public static boolean requiresConfirmation = true;
	public static XO startingPlayer = XO.X;
	public static int cacheOffset = 10;
	public static boolean misclickPrevention = true;
	public static int misclickPreventionTimer = 1000;

}
