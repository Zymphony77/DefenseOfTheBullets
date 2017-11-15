package entity.job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import main.*;
import entity.*;
import entity.bullet.*;
import entity.property.*;
import utility.*;

public class Novice extends Entity implements Movable, Shootable {
	private static final double DEFAULT_MAX_HP = 5000;
	private static final double DEFAULT_SPEED = 150;
	private static final int CANVAS_SIZE = 60;
	private static final int RADIUS = 20;
	
	protected boolean isMoving;
	protected double moveDirection;
	protected double speed;
	protected int reloadDone;
	
	protected HpBar hpBar;
	protected Experience experience;
	protected int reloadCount;
	
	public Novice(Pair refPoint, Side side) {
		super(refPoint, DEFAULT_MAX_HP, 0, side);
		
		speed = DEFAULT_SPEED;
		attack = 100;
		isMoving = false;
		moveDirection = 0;
		
		reloadDone = 15;
		reloadCount = 15;
		
		experience = new Experience(1, 0);
	}
	
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if(Component.getInstance().getPlayer() != null) {
			changeCenter(Component.getInstance().getPlayer().getRefPoint());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(2*RADIUS, 25, RADIUS, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
		
		Component.getInstance().removeComponent(hpBar);
		hpBar = new HpBar(this);
		Component.getInstance().addComponent(hpBar);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE/2 - center.first + Main.SCREEN_SIZE / 2);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE/2 - center.second + Main.SCREEN_SIZE / 2);
	}
	
	public void rotate() {
		canvas.setRotate(direction);
	}
	
	public void setMoving(double moveDirection) {
		this.moveDirection = moveDirection;
		this.isMoving = true;
	}
	
	public void stopMoving() {
		this.isMoving = false;
	}
	
	public void move() {
		if(!isMoving) {
			return;
		}
		
		refPoint.first += Math.cos(Math.toRadians(moveDirection)) * speed / Main.FRAME_RATE;
		refPoint.first = Math.min(refPoint.first, (double) Component.MAX_SIZE);
		refPoint.first = Math.max(refPoint.first, (double) 0);
		
		refPoint.second += Math.sin(Math.toRadians(moveDirection)) * speed / Main.FRAME_RATE;
		refPoint.second = Math.min(refPoint.second, (double) Component.MAX_SIZE);
		refPoint.second = Math.max(refPoint.second, (double) 0);
	}
	
	public void heal(double amount) {
		hp = Math.min(hp + amount, maxHp);
	}
	
	public void takeDamage(Entity entity) {
		if(hp > entity.getAttack()) {
			hp -= entity.getAttack();
			hpBar.draw();
		} else {
			hp = 0;
			die(entity);
			// Give EXP to shooter
		}
	}
	
	public void die(Entity killer) {
		super.die();
		hpBar.die();
		reloadCount = 0;
		
		Novice realKiller = null;
		
		if(killer instanceof Bullet) {
			if(((Bullet) killer).getShooter() instanceof Novice) {
				realKiller = (Novice) ((Bullet) killer).getShooter();
			}
		} else if(killer instanceof Novice) {
			realKiller = (Novice) killer;
		}
		
		if(realKiller != null) {
			realKiller.gainExp(experience.getGainedExperience() / 3);
		}
	}
	
	public void shoot() {
		if(reloadCount < reloadDone) {
			return;
		}
		
		double x = refPoint.first + Math.cos(Math.toRadians(direction))*(RADIUS + 17);
		double y = refPoint.second + Math.sin(Math.toRadians(direction))*(RADIUS + 17);
		
		Bullet bullet = new Bullet(this, new Pair(x, y), 10, direction, 100, 200, side);
		Component.getInstance().addComponent(bullet);
		
		reloadCount = 0;
	}
	
	public void reload() {
		reloadCount = Math.min(reloadCount + 1, reloadDone);
	}
	
	public void gainExp(double exp) {
		experience.addExp(exp);
	}
	
	@Override
	public String toString() {
		return "Novice";
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setReloadDone(int reloadDone) {
		this.reloadDone = reloadDone;
	}
	
	public int getRadius() {
		return 20;
	}
	
	public Experience getExperience() {
		return experience;
	}
	
	public int getLevel() {
		return experience.getLevel();
	}
	
	public HpBar getHpBar() {
		return hpBar;
	}
	
	public double getSpeed() {
		return speed;
	}
}
