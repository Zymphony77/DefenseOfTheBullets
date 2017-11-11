package entity.job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import main.*;
import entity.Entity;
import entity.bullet.Bullet;
import utility.*;

public class Novice extends Entity {
	private static final double DEFAULT_MAX_HP = 100;
	private static final double DEFAULT_SPEED = 5;
	private static final int CANVAS_SIZE = 60;
	private static final int MAX_LEVEL = 50;
	private static final int RADIUS = 20;
	
	protected double exp;
	protected int level;
	
	public Novice(Pair refPoint, Side side) {
		super(refPoint, DEFAULT_MAX_HP, 0, DEFAULT_SPEED, side);
		
		level = 1;
	}
	
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE/2);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE/2);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(2*RADIUS, 25, RADIUS, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE/2 - center.first);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE/2 - center.second);
	}
	
	public void rotate() {
		canvas.setRotate(direction);
	}
	
	public void heal(double amount) {
		hp = Math.min(hp + amount, maxHp);
	}
	
	public void takeDamage(Entity entity, double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			isDead = true;
			hp = 0;
			die();
		}
		
		// Give EXP to shooter
	}
	
	public void shoot() {
		double x = refPoint.first + (int) (Math.cos(Math.toRadians(direction))*(RADIUS + 17));
		double y = refPoint.second + (int) (Math.sin(Math.toRadians(direction))*(RADIUS + 17));
		
		Bullet bullet = new Bullet(this, new Pair(x, y), 10, direction, 1, side);
		Component.getInstance().addEntity(bullet);
	}
	
	public int getRadius() {
		return 20;
	}
}