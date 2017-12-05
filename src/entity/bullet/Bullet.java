package entity.bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import entity.*;
import entity.job.*;
import entity.property.Movable;
import entity.property.Side;
import main.*;
import main.game.GameComponent;
import utility.*;

public class Bullet extends Entity implements Movable {
	private static final int RADIUS = 7;
	
	public static final int LIFE_DURATION = 2;
	public static final int MAX_LIFE_CYCLE = LIFE_DURATION * Main.FRAME_RATE;
	
	protected double speed;
	protected Entity shooter;
	protected int lifeCycleCount;
	
	public Bullet(Entity shooter, Pair refPoint, double maxHp, double direction, double attack, double speed, Side side) {
		super(refPoint, maxHp, direction, side);
		
		this.attack = attack;
		this.speed = speed;
		this.lifeCycleCount = 0;
		this.shooter = shooter;
	}
	
	public void takeDamage(Entity entity) {
		if(hp - entity.getAttack() > 1e-4) {
			hp -= entity.getAttack();
		} else {
			hp = 0;
			die();
		}
	}
	
	public void draw() {
		canvas.setWidth(2*RADIUS);
		canvas.setHeight(2*RADIUS);
		
		changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
		
		if(side == Side.RED) {
			gc.setFill(Color.ORANGERED);
		} else if(side == Side.BLUE) {
			gc.setFill(Color.CORNFLOWERBLUE);
		} else {
			gc.setFill(Color.GOLD);
		}
		
		gc.fillOval(0, 0, 2*RADIUS, 2*RADIUS);
	}
	
	public void move() {
		refPoint.first += Math.cos(Math.toRadians(direction)) * speed / Main.FRAME_RATE;
		refPoint.second += Math.sin(Math.toRadians(direction)) * speed / Main.FRAME_RATE;
		
		if(refPoint.first < -50 || refPoint.first > GameComponent.MAX_SIZE + 50) {
			die();
		}
		
		if(refPoint.second < -50 || refPoint.second > GameComponent.MAX_SIZE + 50) {
			die();
		}
		
		++lifeCycleCount;
		if(lifeCycleCount > MAX_LIFE_CYCLE) {
			die();
		}
	}
	
	@Override
	public void die() {
		super.die();
		GameComponent.getInstance().getBulletPane().getChildren().remove(canvas);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - RADIUS - center.first + Main.SCREEN_SIZE / 2);
		canvas.setTranslateY(refPoint.second - RADIUS - center.second + Main.SCREEN_SIZE / 2);
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public int getRadius() {
		return RADIUS;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getLifeCycleCount() {
		return lifeCycleCount;
	}
	
	public Entity getShooter() {
		return shooter;
	}
}
