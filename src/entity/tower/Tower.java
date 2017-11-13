package entity.tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import main.*;

import java.util.ArrayList;
import java.util.Random;

import entity.*;
import entity.bullet.*;
import entity.job.Novice;
import entity.property.HpBar;
import entity.property.Shootable;
import utility.*;

public class Tower extends Entity implements Shootable {
	private static final int MAX_HP = 3000;
	private static final int CANVAS_SIZE = 200;
	private static final int RADIUS = 75;
	private static final int RELOAD_DONE = 30;
	
	protected HpBar hpBar;
	protected int reloadCount;
	
	public Tower(Pair refPoint, Side side) {
		super(refPoint, MAX_HP, 0, side);
		
		attack = 100;
		reloadCount = RELOAD_DONE;
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
		
		Component.getInstance().removeComponent(hpBar);
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
	
	public void takeDamage(Entity entity) {
		if(hp > entity.getAttack()) {
			hp -= entity.getAttack();
			hpBar.draw();
		} else {
			hp = maxHp;
			
			if(side == Side.NEUTRAL) {
				side = entity.getSide();
			} else {
				side = Side.NEUTRAL;
			}
			
			this.draw();
			hpBar.draw();
		}
	}
	
	public void shoot() {
		if(reloadCount < RELOAD_DONE) {
			return;
		}
		
		double x = refPoint.first + Math.cos(Math.toRadians(direction))*(RADIUS + 32);
		double y = refPoint.second + Math.sin(Math.toRadians(direction))*(RADIUS + 32);
		
		Bullet bullet = new Bullet(this, new Pair(x, y), 10, direction, 500, side);
		Component.getInstance().addComponent(bullet);
		
		reloadCount = 0;
	}
	
	public HpBar getHpBar() {
		return hpBar;
	}
	
	public int getRadius() {
		return RADIUS;
	}
	
	public void reload() {
		reloadCount = Math.min(reloadCount + 1, RELOAD_DONE);
	}
	
	public static void update() {
		for(Tower tower : Component.getInstance().getTowerList()) {
			tower.eachUpdate();
		}
	}
	
	protected void eachUpdate() {
		Random rand = new Random();
		double distance = Double.MAX_VALUE;
		Pair res = null;
		
		for(Novice novice : Component.getInstance().getPlayerList()) {
			if(distance > super.getRefPoint().distance(novice.getRefPoint()) && novice.getSide() != super.getSide()) {
				distance = super.getRefPoint().distance(novice.getRefPoint());
				res = novice.getRefPoint();
			}
		}
		
		if(res != null) {
			double dir = Math.atan2(res.second - super.getRefPoint().second, res.first - super.getRefPoint().first);
			
			dir = dir * 180.0 / Math.PI;
			
			direction = dir + (rand.nextDouble() - 0.5) * 30.0;
			rotate();
			shoot();
		}
	}
}
