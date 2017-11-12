package entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import main.*;
import entity.*;
import entity.bullet.*;
import entity.property.HpBar;
import entity.property.Shootable;
import utility.*;

public class Tower extends Entity implements Shootable {
	private static final int MAX_HP = 10000;
	private static final int CANVAS_SIZE = 200;
	private static final int RADIUS = 75;
	
	protected HpBar hpBar;
	
	public Tower(Pair refPoint, Side side) {
		super(refPoint, MAX_HP, 0, side);
	}
	
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(CANVAS_SIZE / 2, CANVAS_SIZE / 2 - 10, CANVAS_SIZE, 20);
		if(side == Side.RED) {
			gc.setFill(Color.ORANGERED);
		} else if(side == Side.BLUE) {
			gc.setFill(Color.CORNFLOWERBLUE);
		} else {
			gc.setFill(Color.GOLD);
		}
		gc.fillOval(25, 25, 2*RADIUS, 2*RADIUS);
		
		hpBar = new HpBar(this);
		Component.getInstance().addComponent(hpBar);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE / 2 - center.first + Main.SCREEN_SIZE / 2);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE / 2 - center.second + Main.SCREEN_SIZE / 2);
	}
	
	public void rotate() {
		canvas.setRotate(direction);
	}
	
	public void takeDamage(Entity entity, double damage) {
		if(hp > damage) {
			hp -= damage;
		} else {
			hp = maxHp;
			side = entity.getSide();
		}
	}
	
	public void shoot() {
		double x = refPoint.first + Math.cos(Math.toRadians(direction))*(RADIUS + 32);
		double y = refPoint.second + Math.sin(Math.toRadians(direction))*(RADIUS + 32);
		
		Bullet bullet = new Bullet(this, new Pair(x, y), 10, direction, 700, side);
		Component.getInstance().addComponent(bullet);
	}
	
	public HpBar getHpBar() {
		return hpBar;
	}
	
	public int getRadius() {
		return RADIUS;
	}
}
