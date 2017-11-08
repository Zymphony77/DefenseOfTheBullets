package utility;

import javafx.scene.canvas.Canvas;

public abstract class Entity {
	protected Canvas canvas;
	
	protected Pair refPoint;
	protected double maxHp;
	protected double hp;
	protected double attack;
	protected double direction;		// Angle against X-axis
	protected double speed;
	protected boolean hpBarVisible;
	
	public Entity(Pair refPoint, double maxHp, double direction, double speed, boolean hpBarVisible) {
		this.refPoint = new Pair(refPoint);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.direction = direction;
		this.speed = speed;
		this.hpBarVisible = hpBarVisible;
		
		this.canvas = new Canvas();
	}
	
	public void takeDamage(double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			die();
		}
	}
	
	public void die() {
		canvas.setOpacity(0);
	}
	
	public void setRefPoint(Pair refPoint) {
		this.refPoint = new Pair(refPoint);
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public double getMaxHp() {
		return maxHp;
	}
	
	public double getHp() {
		return hp;
	}
	
	public double getDirection() {
		return direction;
	}
	
	public double getSpeed() {
		return speed;
	}
}
