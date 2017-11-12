package entity.bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import entity.*;
import entity.job.*;
import entity.property.Movable;
import main.*;
import utility.*;

public class Bullet extends Entity implements Movable {
	private static int RADIUS = 7;
	public static int MAX_LIFE_CYCLE = 5000 * Main.FRAME_RATE;
	
	protected double speed;
	protected Entity shooter;
	protected int lifeCycleCount;
	
	public Bullet(Entity shooter, Pair refPoint, double maxHp, double direction, double speed, Side side) {
		super(refPoint, maxHp, direction, side);
		
		this.speed = speed;
		lifeCycleCount = 0;
		this.shooter = shooter;
	}
	
	public void takeDamage(Entity entity, double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			hp = 0;
			die();
		}
	}
	
	public void draw() {
		canvas.setWidth(2*RADIUS);
		canvas.setHeight(2*RADIUS);
		
		canvas.setTranslateX(refPoint.first - RADIUS);
		canvas.setTranslateY(refPoint.second - RADIUS);
		
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
		
		if(side == Side.RED) {
			gc.setFill(Color.ORANGERED);
		} else if(side == Side.BLUE) {
			gc.setFill(Color.CORNFLOWERBLUE);
		} else {
			gc.setFill(Color.GOLD);
		}
		
		gc.fillOval(0, 0, 2*RADIUS, 2*RADIUS);
	}
	
	public void move() {
		refPoint.first += Math.cos(Math.toRadians(direction)) * speed / Main.FRAME_RATE;
		refPoint.second += Math.sin(Math.toRadians(direction)) * speed / Main.FRAME_RATE;
		
		++lifeCycleCount;
		if(lifeCycleCount > MAX_LIFE_CYCLE) {
			die();
		}
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - RADIUS - center.first + Main.SCREEN_SIZE / 2);
		canvas.setTranslateY(refPoint.second - RADIUS - center.second + Main.SCREEN_SIZE / 2);
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public int getRadius() {
		return RADIUS;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getLifeCycleCount() {
		return lifeCycleCount;
	}
}
