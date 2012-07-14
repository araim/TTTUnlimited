package net.araim.tictactoe;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class LocalPlayer extends Player implements IBoardOperationDispatcher {

	protected IGameInterface igi;
	private static final String TAG = "TTT.LocalPlayer";

	public LocalPlayer(Parcel in) {
		super(XO.parse(in.readInt()));
	}

	public LocalPlayer(XO x) {
		super(x);
	}

	@Override
	public void notifyOponentMove(Point p) {

	}

	@Override
	public void notifyMoveWaiting() {

	}

	@Override
	public void setGameInterface(IGameInterface i) {
		igi = i;
	}

	@Override
	public boolean dispatchMove(Point p) {
		if (this.xo == igi.getCurrentPlayer()) {
			try {
				igi.requestMove(p);
			} catch (IllegalMoveException ime) {
				Log.d(TAG, String.format("Player attempted to make an check an already occupied cell (%s)", p));
			}
			return true;
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(xo.intValue());
	}

	public static final Parcelable.Creator<LocalPlayer> CREATOR = new Parcelable.Creator<LocalPlayer>() {
		public LocalPlayer createFromParcel(Parcel in) {
			return new LocalPlayer(in);
		}

		public LocalPlayer[] newArray(int size) {
			return new LocalPlayer[size];
		}
	};

}
