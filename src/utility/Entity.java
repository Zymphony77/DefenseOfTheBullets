package utility;

public abstract class Entity {
	protected Pair refPoint;
	protected double maxHp;
	protected double hp;
	protected double direction;					// Angle against X-axis
	protected double speed;
	
	public Entity(Pair refPoint, double maxHp, double direction, double speed) {
		this.refPoint = new Pair(refPoint);
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.direction = direction;
		this.speed = speed;
	}
}
