package entity.food;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import entity.*;
import utility.*;

public class Food extends Entity {
	public static final int CANVAS_SIZE = 20; 
	
	public Food(Pair refPoint) {
		super(refPoint, 10, 0, 0, Side.NEUTRAL);
	}
	
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE/2);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE/2);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.LIGHTYELLOW);
		gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE / 2 - center.first);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE / 2 - center.second);
	}
	
	public void takeDamage(Entity entity, double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			hp = 0;
			die();
			// Give EXP to shooter
		}
	}
	
	public int getRadius() {
		return CANVAS_SIZE / 2;
	}
}
