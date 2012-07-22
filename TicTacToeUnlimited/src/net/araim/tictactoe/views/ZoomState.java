package net.araim.tictactoe.views;


class ZoomState {
	private double startZoom;
	private double currentZoom;
	private double targetZoom;
	private long startTime;
	private long endTime;
	private boolean targetReached = true;

	ZoomState(float zoom) {
		startZoom = zoom;
		currentZoom = zoom;
		targetZoom = zoom;
	}

	public boolean targetReached() {
		return targetReached;
	}

	public void adjustTarget(float factor, boolean instant) {
		if (instant) {
			targetZoom *= factor;
			currentZoom = targetZoom;
			targetReached = true;
			startZoom = currentZoom;
		} else {
			targetZoom *= factor;
			targetReached = false;
			startZoom = currentZoom;
			startTime = System.currentTimeMillis();
			endTime = startTime + net.araim.tictactoe.configuration.Settings.zoomAnimationTime;
		}
	}

	public double getCurrent() {
		if (targetReached) {
			return currentZoom;
		} else {
			float percent = (float) (System.currentTimeMillis() - startTime) / (endTime - startTime);
			if (percent > 1) {
				currentZoom = targetZoom;
				targetReached = true;
			} else {
				currentZoom = startZoom + (targetZoom - startZoom) * percent;
			}
		}
		return currentZoom;
	}

	@Override
	public String toString() {
		return "ZoomState [startZoom=" + startZoom + ", currentZoom=" + currentZoom + ", targetZoom=" + targetZoom + ", startTime="
				+ startTime + ", endTime=" + endTime + ", targetReached=" + targetReached + "]";
	}

}
