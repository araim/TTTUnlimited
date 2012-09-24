package net.araim.tictactoe.AI;

import net.araim.tictactoe.XO;
import android.graphics.Point;

public class Score implements Comparable<Score> {
	public int length;
	public int chains;
	public int blocks = 0;
	public int openEnds;
	public XO owner;
	public Point p;

	public Point getP() {
		return p;
	}

	public void setP(Point p) {
		this.p = p;
	}

	public Score(int len, int opens, boolean blocks) {
		this(len, opens);
		this.blocks = 1;
	}

	public Score(int len, int opens) {
		this.length = len;
		this.openEnds = opens;
	}

	@Override
	public int compareTo(Score another) {
		int cmp = length - another.length;
		if (cmp != 0) {
			return cmp;
		}
		cmp = openEnds - another.openEnds;
		if (cmp != 0) {
			return cmp;
		}
		cmp = blocks - another.blocks;
		if (cmp != 0) {
			return cmp;
		}
		cmp = chains - another.chains;
		if (cmp != 0) {
			return cmp;
		}

		return 0;
	}

	@Override
	public String toString() {
		return "Score [length=" + length + ", chains=" + chains + ", blocks=" + blocks + ", openEnds=" + openEnds + ", owner=" + owner
				+ ", p=" + p + "]";
	}
}
