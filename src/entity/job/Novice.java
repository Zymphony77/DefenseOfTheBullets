package entity.job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

import main.*;
import entity.*;
import entity.bullet.*;
import entity.property.*;
import skill.*;
import utility.*;

public class Novice extends Entity implements Movable, Shootable {
	private static final double DEFAULT_MAX_HP = 5000;
	private static final double DEFAULT_SPEED = 150;
	private static final int CANVAS_SIZE = 60;
	private static final int RADIUS = 20;
	
	protected LinkedList<Skill> skillList;
	
	protected boolean isMoving;
	protected double moveDirection;
	protected double speed;
	protected int reloadDone;
	
	protected HpBar hpBar;
	protected Experience experience;
	protected int reloadCount;
	
	/* ------------------- Constructor ------------------- */
	public Novice(Pair refPoint, Side side) {
		super(refPoint, DEFAULT_MAX_HP, 0, side);
		
		speed = DEFAULT_SPEED;
		attack = 100;
		isMoving = false;
		moveDirection = 0;
		
		skillList = new LinkedList<Skill>();
		skillList.add(new Haste());
		
		reloadDone = 15;
		reloadCount = 15;
		
		experience = new Experience(1, 0);
	}
	
	/* ------------------- UI ------------------- */
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
	
	/* ------------------- Game Function ------------------- */
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
			realKiller.gainExp(experience.getGainedExperience() / 3);		// Adjust given EXP here
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
	
	/* ------------------- Skill ------------------- */
	public void useHaste() {
		Haste haste = null;
		for(Skill each: skillList) {
			if(each instanceof Haste) {
				haste = (Haste) each;
				break;
			}
		}
		
		if(haste.isReady()) {
			haste.activateSkill(this);
		}
	}
	
	/* ------------------- Getters&Setters ------------------- */	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setReloadDone(int reloadDone) {
		this.reloadDone = reloadDone;
	}
	
	public int getRadius() {
		return 20;
	}
	
	public LinkedList<Skill> getSkillList() {
		return skillList;
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
	
	public Job getJob() {
		return Job.NOVICE;
	}
	
	@Override
	public String toString() {
		return "Novice";
	}
}
