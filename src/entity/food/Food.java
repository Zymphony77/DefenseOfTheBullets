package entity.food;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Component;
import main.Main;

import java.util.Random;

import entity.*;
import entity.property.*;
import utility.*;

public class Food extends Entity implements Rotatable {
	public static final int CANVAS_SIZE = 20;
	
	private int rotateDirection;
	
	public Food(Pair refPoint) {
		super(refPoint, 10, (new Random()).nextInt(360), Side.NEUTRAL);
		
		rotateDirection = ((new Random()).nextInt(2) * 2) - 1;
	}
	
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		changeCenter(Component.getInstance().getPlayer().getRefPoint());
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.GOLD);
		gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
	}
	
	public void rotate() {
		canvas.setRotate(direction);
		
		direction += 1.5 * rotateDirection;
		if(direction < 0) {
			direction += 360;
		} else if(direction >= 360) {
			direction -= 360;
		}
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE / 2 - center.first + Main.SCREEN_SIZE / 2);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE / 2 - center.second + Main.SCREEN_SIZE / 2);
	}
	
	public void takeDamage(Entity entity) {
		if(hp > entity.getAttack()) {
			hp -= entity.getAttack();
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
