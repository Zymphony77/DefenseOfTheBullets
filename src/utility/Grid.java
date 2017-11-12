package utility;

public class Grid {
	private int x, y;
	private double time;
	private boolean chk;
	private int firstDirection;
	
	public Grid() {
		time = 0.0;
		chk = false;
	}
	
	public Grid(int x, int y, double time, boolean chk, int firstDirection) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.chk = chk;
		this.firstDirection = firstDirection;
	}
	
	public Grid(Grid o) {
		this.x = o.x;
		this.y = o.y;
		this.time = o.time;
		this.chk = o.chk;
		this.firstDirection = o.firstDirection;
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
	
	public int getFirstDirection() {
		return firstDirection;
	}

	public void setFirstDirection(int firstDirection) {
		this.firstDirection = firstDirection;
	}

}
