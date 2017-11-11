package entity.bullet;

import entity.Entity;
import javafx.scene.canvas.GraphicsContext;

import main.Main;
import utility.*;

public class Bullet extends Entity {
	private static int RADIUS = 7;
	
	public Bullet(Pair refPoint, double maxHp, double direction, double speed, Side side) {
		super(refPoint, maxHp, direction, speed, side);
	}
	
	public void attack(Entity entity) {
		
	}
	
	public void takeDamage(double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			hp = 0;
			die();
		}
	}
	
	public void draw() {
		System.out.println("RefPoint: " + refPoint.first + " " + refPoint.second);
		
		canvas.setWidth(2*RADIUS);
		canvas.setHeight(2*RADIUS);
		
		canvas.setTranslateX(refPoint.first - RADIUS);
		canvas.setTranslateY(refPoint.second - RADIUS);
		
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
		gc.fillOval(0, 0, 2*RADIUS, 2*RADIUS);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - RADIUS - center.first);
		canvas.setTranslateY(refPoint.second - RADIUS - center.second);
	}
	
	public int getRadius() {
		return RADIUS;
	}
}
