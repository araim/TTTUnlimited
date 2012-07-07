package net.araim.tictactoe.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * 
 * @author araim
 * 
 *         Ther sparse 2D array is like an ordinary 2D array only it doesn't
 *         require one to specify its size and it doesn't store the elements in
 *         between filled indices (i.e. it only stores the values inputed)
 * @param <T> serializable type held in the array
 */
public final class SparseArray2D<T extends Serializable> implements Parcelable {
	private SparseArray<SparseArray<T>> map = new SparseArray<SparseArray<T>>();

	private static int shift = Integer.MAX_VALUE / 2;

	/**
	 * 
	 * @author araim
	 * 
	 *         Helper class to describe Array Elements without connection to the
	 *         array itself using the coordinates and value
	 * 
	 * @param <T> a serializable type
	 */
	static class PointValue<T> {
		/**
		 * get the x coordinate
		 * 
		 * @return row coordinate
		 */
		public int getX() {
			return p.x;
		}

		/**
		 * get the y coordinate
		 * 
		 * @return column coordinate
		 */
		public int getY() {
			return p.y;
		}

		/**
		 * get the value
		 * 
		 * @return value at given coordinates
		 */
		public T getValue() {
			return value;
		}

		private final Point p;
		private final T value;

		/**
		 * 
		 * creates the pointvalue object using given coordinates and the value
		 * 
		 * @param x row coordinate
		 * @param y column coordinate
		 * @param val object value
		 */
		public PointValue(int x, int y, T val) {
			p = new Point(x, y);
			value = val;
		}
	}

	/**
	 * creates empty sparse array
	 */
	public SparseArray2D() {
	}

	/**
	 * Create SparseArray2D from previously written Parcel
	 * 
	 * @param in the parcel
	 */
	public SparseArray2D(Parcel in) {
		int amount = in.readInt();
		while (amount-- > 0) {
			int x = in.readInt();
			int y = in.readInt();
			@SuppressWarnings("unchecked")
			T value = (T) in.readSerializable();
			put(value, x, y);
		}
	}

	/**
	 * put the item in the desired coordinates
	 * 
	 * @param obj the item to put
	 * @param x row coordinate
	 * @param y column coordinate
	 */
	public void put(T obj, int x, int y) {
		SparseArray<T> temp;
		synchronized (map) {
			temp = map.get(shift + x);
			if (temp == null) {
				temp = new SparseArray<T>();
				map.put(shift + x, temp);
			}
			temp.put(shift + y, obj);
		}
	}

	/**
	 * 
	 * get the item from specific corrdinates
	 * 
	 * @param x row coordinate
	 * @param y column coordinate
	 * @return the value at given coordinates (null if empty)
	 */
	public T get(int x, int y) {
		SparseArray<T> temp;
		synchronized (map) {
			temp = map.get(shift + x);
			if (temp == null) {
				return null;
			}
			return temp.get(shift + y);
		}
	}

	/**
	 * 
	 * Remove the value at given coordinates
	 * 
	 * @param x row coordinate
	 * @param y column coordinate
	 * @return true if the item was removed, false if the cell was empty
	 */
	public boolean clear(int x, int y) {
		SparseArray<T> temp;
		synchronized (map) {
			temp = map.get(shift + x);
			if (temp == null) {
				return false;
			}
			T t = temp.get(shift + y);
			if (t == null) {
				return false;
			}
			temp.remove(shift + y);
			if (temp.size() == 0) {
				map.remove(shift + x);
			}
			return true;
		}
	}

	/**
	 * 
	 * Check if the cell at given coordinates has any value assigned
	 * 
	 * @param x row coordinate
	 * @param y column coordinate
	 * @return true - has value / false - has no value
	 */
	public boolean isClear(int x, int y) {
		SparseArray<T> temp;
		synchronized (map) {
			temp = map.get(shift + x);
			if (temp != null) {
				T t = temp.get(shift + y);
				return (t == null);
			} else {
				return true;
			}
		}
	}

	/**
	 * Get All values from the whole array
	 * 
	 * @return an array of PointValue objects holiding coordinates and the value
	 */
	List<PointValue<T>> getAll() {
		List<PointValue<T>> lpv = new ArrayList<PointValue<T>>();
		synchronized (map) {
			for (int i = 0; i < map.size(); i++) {
				int keyX = map.keyAt(i);
				SparseArray<T> ar = map.valueAt(i);
				for (int j = 0; j < ar.size(); j++) {
					int keyY = ar.keyAt(j);
					lpv.add(new PointValue<T>((keyX - shift), (keyY - shift), ar.valueAt(j)));
				}
			}
		}
		return lpv;
	}

	/**
	 * Get a all values from the specified sub-range of the array
	 * 
	 * @return an array of PointValue objects holiding coordinates and the value
	 *         in the specified range
	 */
	List<PointValue<T>> getRange(int x, int xcount, int y, int ycount) {
		List<PointValue<T>> lpv = new ArrayList<PointValue<T>>();
		synchronized (map) {
			for (int i = 0; i < map.size(); i++) {
				int keyX = map.keyAt(i);
				if (keyX - shift >= x && keyX - shift < x + xcount) {
					SparseArray<T> ar = map.valueAt(i);
					for (int j = 0; j < ar.size(); j++) {
						int keyY = ar.keyAt(j);
						if (keyY - shift >= y && keyY - shift < y + ycount) {
							lpv.add(new PointValue<T>((keyX - shift), (keyY - shift), ar.valueAt(j)));
						}
					}
				}
			}
		}
		return lpv;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("SparseArray2D [map:");
		synchronized (map) {
			for (int i = 0; i < map.size(); i++) {
				int keyX = map.keyAt(i);
				SparseArray<T> ar = map.valueAt(i);
				for (int j = 0; j < ar.size(); j++) {
					int keyY = ar.keyAt(j);
					sb.append("[" + (keyX - shift) + "," + (keyY - shift) + "] => " + ar.valueAt(j));
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		List<PointValue<T>> pvs = getAll();
		dest.writeInt(pvs.size());
		for (PointValue<T> pv : pvs) {
			dest.writeInt(pv.getX());
			dest.writeInt(pv.getY());
			dest.writeSerializable(pv.getValue());
		}
	}
}
