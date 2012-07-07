package net.araim.tictactoe;

import net.araim.tictactoe.configuration.Settings;
import android.os.Parcel;
import android.os.Parcelable;

public class CurrentGameSettings implements Parcelable {
	public int winSize = 5;
	public int confirmationTime = 3;
	public boolean requiresConfirmation = true;
	
	private static Object lock = new Object();
	private static CurrentGameSettings instance;
	
	public static CurrentGameSettings getInstance(){
		if(instance == null){
			synchronized(lock){
				if(instance == null){
					instance = new CurrentGameSettings();
				}
			}
		}
		return instance;
	}
	
	private CurrentGameSettings() {
		winSize = Settings.winSize;
		confirmationTime = Settings.confirmationTime;
		requiresConfirmation = Settings.requiresConfirmation;
	}

	public static void adopt(CurrentGameSettings settings){
		synchronized (lock) {
			instance = settings;
		}
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(winSize);
		dest.writeInt(confirmationTime);
		dest.writeInt(requiresConfirmation ? 1 : 0);
	}

	private CurrentGameSettings(Parcel in) {
		winSize = in.readInt();
		confirmationTime = in.readInt();
		requiresConfirmation = in.readInt() == 1;
	}

	public static final Parcelable.Creator<CurrentGameSettings> CREATOR = new Parcelable.Creator<CurrentGameSettings>() {
		public CurrentGameSettings createFromParcel(Parcel in) {
			return new CurrentGameSettings(in);
		}

		public CurrentGameSettings[] newArray(int size) {
			return new CurrentGameSettings[size];
		}
	};
}
