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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestParcelable other = (TestParcelable) obj;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

}