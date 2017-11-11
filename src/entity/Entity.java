package entity;

import javafx.scene.canvas.Canvas;
import utility.Pair;
import utility.Side;

public abstract class Entity {
	protected Canvas canvas;
	protected Pair refPoint;
	protected double maxHp;
	protected double hp;
	protected double attack;
	protected double direction;		// Angle against X-axis
	protected double speed;
	protected Side side;
	protected boolean isDead;
	
	public Entity(Pair refPoint, double maxHp, double direction, double speed, Side side) {
		this.refPoint = new Pair(refPoint);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.direction = direction;
		this.speed = speed;
		this.side = side;
		this.isDead = false;
		
		this.canvas = new Canvas();
		
		draw();
	}
	
	public abstract void draw();
	public abstract void changeCenter(Pair center);
	
	public void attack(Entity entity) {
		entity.takeDamage(this, attack);
	}
	
	public abstract void takeDamage(Entity entity, double damage);
	
	public void die() {
		isDead = true;
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
	
	public Pair getRefPoint() {
		return refPoint;
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
	
	public Side getSide() {
		return side;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public abstract int getRadius();
}
