package utility;

public class Grid implements Comparable<Grid> {
	private int x, y;
	private int gridX, gridY;
	private double time;
	private boolean chk;
	private int firstDirection;
	
	public Grid() {
		time = 0.0;
		chk = false;
	}
	
	public Grid(int x, int y, int gridX, int gridY, double time, boolean chk, int firstDirection) {
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		this.time = time;
		this.chk = chk;
		this.firstDirection = firstDirection;
	}
	
	public Grid(Grid o) {
		this.x = o.x;
		this.y = o.y;
		this.gridX = o.gridX;
		this.gridY = o.gridY;
		this.time = o.time;
		this.chk = o.chk;
		this.firstDirection = o.firstDirection;
	}
	
	@Override
	public int compareTo(Grid other) {
		return (int) (this.getTime() - other.getTime());
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getGridX() {
		return gridX;
	}

	public void setGridX(int gridX) {
		this.gridX = gridX;
	}

	public int getGridY() {
		return gridY;
	}

	public void setGridY(int gridY) {
		this.gridY = gridY;
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
