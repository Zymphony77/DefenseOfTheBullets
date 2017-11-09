package job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import utility.*;

public class Novice extends Entity implements Shootable {
	private static double MAX_HP = 100;
	private static double SPEED = 5;
	
	public Novice(Pair refPoint, boolean hpBarVisible) {
		super(refPoint, MAX_HP, 0, SPEED, hpBarVisible);
		
		canvas.setWidth(80);
		canvas.setHeight(80);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(40, 35, 25, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(25, 25, 30, 30);
	}
	
	public void attack(Entity entity) {
		
	}
	
	public void takeDamage(double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			isDead = true;
			hp = 0;
			die();
		}
	}
	
	public void shoot() {
		
	}
}
