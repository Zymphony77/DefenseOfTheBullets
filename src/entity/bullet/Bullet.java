package entity.bullet;

import javafx.scene.canvas.GraphicsContext;

import entity.*;
import entity.job.*;
import main.*;
import utility.*;

public class Bullet extends Entity {
	private static int RADIUS = 7;
	
	Novice shooter;
	
	public Bullet(Novice shooter, Pair refPoint, double maxHp, double direction, double speed, Side side) {
		super(refPoint, maxHp, direction, speed, side);
		
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
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - RADIUS - center.first);
		canvas.setTranslateY(refPoint.second - RADIUS - center.second);
	}
	
	public int getRadius() {
		return RADIUS;
	}
}
