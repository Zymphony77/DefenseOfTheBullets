package entity;

import entity.property.Side;
import javafx.scene.canvas.Canvas;
import main.game.GameComponent;
import utility.Pair;

public abstract class Entity {
	protected Canvas canvas;
	protected Pair refPoint;
	protected double maxHp;
	protected double hp;
	protected double attack;
	protected double direction;		// Angle against X-axis
	protected Side side;
	protected boolean isDead;
	
	public Entity(Pair refPoint, double maxHp, double direction, Side side) {
		this.refPoint = new Pair(refPoint);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.direction = direction;
		this.side = side;
		
		this.canvas = new Canvas();
		this.isDead = false;
		
		draw();
	}
	
	public abstract void draw();
	public abstract void changeCenter(Pair center);
	
	public boolean isCollided(Entity entity) {
		if(this.getRadius() + entity.getRadius() > this.getRefPoint().distance(entity.getRefPoint())) {
			return true;
		}
		return false;
	}
	
	public abstract void takeDamage(Entity entity);
	
	public void die() {
		isDead = true;
		canvas.setOpacity(0);
	}
	
	public void setRefPoint(Pair refPoint) {
		this.refPoint = new Pair(refPoint);
	}
	
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public void setAttack(double attack) {
		this.attack = attack;
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
	
	public double getAttack() {
		return attack;
	}
	
	public double getDirection() {
		return direction;
	}
	
	public Side getSide() {
		return side;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public abstract int getRadius();
	public abstract int getMaxRadius();
}
