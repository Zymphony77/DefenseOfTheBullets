package entity.food;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Main;
import main.game.GameComponent;

import java.util.Random;

import entity.*;
import entity.bullet.*;
import entity.job.*;
import entity.property.*;
import utility.*;

public class Food extends Entity implements Rotatable {
	public static final int CANVAS_SIZE = 20;
	public static final int DEFAULT_MAX_HP = 150;
	public static final int DEFAULT_ATTACK = 150;
	public static final int FOOD_EXP = 100;
	
	private int rotateDirection;
	
	public Food(Pair refPoint) {
		super(refPoint, DEFAULT_MAX_HP, (new Random()).nextInt(360), Side.NEUTRAL);
		
		attack = DEFAULT_ATTACK;
		rotateDirection = ((new Random()).nextInt(2) * 2) - 1;
	}
	
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		
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
		if(hp - entity.getAttack() > 1e-4) {
			hp -= entity.getAttack();
		} else {
			hp = 0;
			die(entity);
		}
	}

	public void die(Entity killer) {
		super.die();
		
		Novice realKiller = null;
		
		if(killer instanceof Bullet) {
			if(((Bullet) killer).getShooter() instanceof Novice) {
				realKiller = (Novice) ((Bullet) killer).getShooter();
			}
		} else if(killer instanceof Novice) {
			realKiller = (Novice) killer;
		}
		
		if(realKiller != null) {
			realKiller.gainExp(FOOD_EXP);
		}
		
		GameComponent.getInstance().getFoodPane().getChildren().remove(canvas);
	}
	
	public int getRadius() {
		return CANVAS_SIZE / 2;
	}
	
	public int getMaxRadius() {
		return getRadius();
	}
}
