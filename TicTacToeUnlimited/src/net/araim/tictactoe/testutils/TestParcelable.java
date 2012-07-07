package net.araim.tictactoe.testutils;

import java.io.Serializable;

import android.os.Parcel;

public class TestParcelable implements Serializable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 2431742541751559769L;
	private String s;

	public TestParcelable(String testString) {
		s = testString;
	}

	public TestParcelable(Parcel in) {
		s = in.readString();
	}

	public String getS() {
		return s;
	}

}