package net.araim.tictactoe.utils;

import java.util.List;

public class TestUtils {

	public static <T> boolean containsAll(List<T> list, T... elements) {
		for (T t : elements) {
			if (!list.contains(t)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean containsAllAndNoMore(List<T> list, T... elements) {
		if (list.size() != elements.length) {
			return false;
		}
		return containsAll(list, elements);

	}
}
