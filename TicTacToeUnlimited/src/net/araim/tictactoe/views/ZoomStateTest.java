package net.araim.tictactoe.views;

import android.test.AndroidTestCase;

public class ZoomStateTest extends AndroidTestCase {

	public void NewZoomStateConsecutiveCallsReturnTheSameValue() {
		ZoomState zs = new ZoomState(1f);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(1f, zs.getCurrent());
	}

	public void NewZoomStateAdjustedReachesAppropriateLevel() {
		ZoomState zs = new ZoomState(1f);
		zs.adjustTarget(2f, true);
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2f, zs.getCurrent());
	}

	public void NewZoomStateAdjustedReachesGoesThroughIntermediateSteps() {
		ZoomState zs = new ZoomState(1f);
		zs.adjustTarget(2f, false);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double z = zs.getCurrent();
		if (z < 1f || z >= 1.5f) {
			fail("To sudden change of zoom value");
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2f, zs.getCurrent());
	}

	public void NewZoomStateAdjustedWithZeroTimeoutSetsTheLevelImmediately() {
		ZoomState zs = new ZoomState(1f);
		zs.adjustTarget(2f, true);
		assertEquals(2f, zs.getCurrent());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2f, zs.getCurrent());
	}

}
