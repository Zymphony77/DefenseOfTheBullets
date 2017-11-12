package entity.bullet;

import javafx.scene.canvas.GraphicsContext;

import entity.*;
import entity.job.*;
import main.*;
import utility.*;

public class Bullet extends Entity {
	private static int RADIUS = 7;
	private static int MAX_LIFE_CYCLE = 5000 * Main.FRAME_RATE;
	
	private Novice shooter;
	private int lifeCycleCount;
	
	public Bullet(Novice shooter, Pair refPoint, double maxHp, double direction, double speed, Side side) {
		super(refPoint, maxHp, direction, speed, side);
		
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
	
	public int getRadius() {
		return RADIUS;
	}
}
