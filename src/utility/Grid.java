package utility;

import entity.Entity;

public class Grid {
	private int x, y;
	private double time;
	private boolean chk;
	
	public Grid() {
		time = 0.0;
		chk = false;
	}
	
	public Grid(int x, int y, double time, boolean chk) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.chk = chk;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public boolean isChk() {
		return chk;
	}

	public void setChk(boolean chk) {
		this.chk = chk;
	}

}
