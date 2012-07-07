package net.araim.tictactoe;

import java.security.InvalidParameterException;

import android.os.Parcel;
import android.os.Parcelable;

public enum XO implements Parcelable {
	X(0), O(1);

	public static XO complementary(XO xo) {
		if (xo == X)
			return XO.O;
		else
			return XO.X;

	}

	public static XO parse(int i) {
		if (XO.O.intval == i) {
			return XO.O;
		} else if (XO.X.intval == i) {
			return XO.X;
		} else {
			throw new InvalidParameterException("Cannot parse XO value " + i);
		}
	}

	private int intval;

	private XO(int i) {
		intval = i;
	}

	private XO(Parcel in) {
		intval = in.readInt();
	}

	public XO complementary() {
		return complementary(this);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public int intValue() {
		return intval;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(intval);
	}
}
