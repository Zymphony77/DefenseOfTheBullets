package entity.job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import buff.*;
import main.*;
import main.game.GameComponent;
import entity.*;
import entity.bullet.*;
import entity.property.*;
import skill.*;
import utility.*;

public class Novice extends Entity implements Movable, Shootable {
	private static final double DEFAULT_MAX_HP = 5000;
	private static final double DEFAULT_ATTACK = 50;
	private static final double DEFAULT_SPEED = 150;
	private static final double DEFAULT_BULLET_DAMAGE = 100;
	private static final double DEFAULT_BULLET_SPEED = 200;
	private static final double DEFAULT_BULLET_HP = 10;
	private static final double DEFAULT_HEALTH_REGEN = 10;
	private static final int DEFAULT_RELOAD = 20;
	private static final int CANVAS_SIZE = 60;
	private static final int RADIUS = 20;
	
	protected ArrayList<Skill> skillList;
	protected ArrayList<Buff> buffList;
	
	protected double bulletDamage;
	protected double healthRegen;
	protected double bulletHP;
	protected double bulletSpeed;
	protected double speed;
	protected double damageFactor;
	protected int reloadDone;
	protected int criticalDamage;
	protected double criticalChance;
	protected Status status;
	
	protected boolean isMoving;
	protected double moveDirection;
	
	protected HpBar hpBar;
	protected Experience experience;
	protected int reloadCount;
	
	/* ------------------- Constructor ------------------- */
	public Novice(Pair refPoint, Side side) {
		super(refPoint, DEFAULT_MAX_HP, 0, side);
		
		buffList = new ArrayList<Buff>();
		
		status = new Status();
		upgradeAbility();
		
		hp = maxHp;
		
		isMoving = false;
		moveDirection = 0;
		
		skillList = new ArrayList<Skill>();
		skillList.add(new Haste());
		skillList.add(new Frenzy());
		
		reloadDone = DEFAULT_RELOAD;
		reloadCount = DEFAULT_RELOAD;

		experience = new Experience(1, 0);
	}
	
	/* ------------------- UI ------------------- */
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(2*RADIUS, 25, RADIUS, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
		
		GameComponent.getInstance().removeComponent(hpBar);
		hpBar = new HpBar(this);
		GameComponent.getInstance().addComponent(hpBar);
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
		refPoint.first = Math.min(refPoint.first, (double) GameComponent.MAX_SIZE);
		refPoint.first = Math.max(refPoint.first, (double) 0);
		
		refPoint.second += Math.sin(Math.toRadians(moveDirection)) * speed / Main.FRAME_RATE;
		refPoint.second = Math.min(refPoint.second, (double) GameComponent.MAX_SIZE);
		refPoint.second = Math.max(refPoint.second, (double) 0);
		
		for(Buff buff: buffList) {
			if(buff instanceof FrenzyBuff && ((FrenzyBuff) buff).isActive()) {
				((FrenzyBuff) buff).deactivateBuff();
				buffList.remove(buff);
				// updateStatus();
				break;
			}
		}
	}
	
	public void heal(double amount) {
		hp = Math.min(hp + amount, maxHp);
		hpBar.draw();
	}
	
	public void takeDamage(Entity entity) {
		takeDamage(entity, entity.getAttack());
	}
	
	protected void takeDamage(Entity entity, double damage) {
		damage *= damageFactor;
		
		if(hp > damage) {
			hp -= damage;
			hpBar.draw();
		} else {
			hp = 0;
			die(entity);
		}
	}
	
	public void spawn() {
		isDead = false;
		canvas.setOpacity(1);
		buffList.add(new InvincibleBuff(this));
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
		
		for(Buff buff: buffList) {
			buff.deactivateBuff();
		}
	}
	
	public void shoot() {
		if(reloadCount < reloadDone) {
			return;
		}
		
		double x = refPoint.first + Math.cos(Math.toRadians(direction))*(RADIUS + 17);
		double y = refPoint.second + Math.sin(Math.toRadians(direction))*(RADIUS + 17);
		
		Bullet bullet = new Bullet(this, new Pair(x, y), bulletHP, direction, bulletDamage, bulletSpeed, side);
		GameComponent.getInstance().addComponent(bullet);
		
		reloadCount = 0;
	}
	
	public void reload() {
		reloadCount = Math.min(reloadCount + 1, reloadDone);
	}
	
	public void gainExp(double exp) {
		experience.addExp(exp);
	}
	
	/* ------------------- Skill ------------------- */
	public void useSkill(int position) {
		if(position > skillList.size()) {
			return;
		}
		
		Skill skill = skillList.get(position - 1);
		
		if(!(skill instanceof ActiveSkill)) {
			return;
		}
		
		if(!skill.isReady()) {
			return;
		}
		
		((ActiveSkill) skill).activateSkill();
	}
	
	public void upgradeSkill(int position) {
		experience.decreaseSkillPoint();
		skillList.get(position - 1).upgrade();
	}
	
	/* ------------------- Buff ------------------- */
	public void addBuff(Buff buff) {
		for(Buff currentBuff: buffList) {
			if(currentBuff.getClass() == buff.getClass()) {
				removeBuff(currentBuff);
			}
		}
		
		buffList.add(buff);
		
		if(GameComponent.getInstance().getPlayer() == this) {
			GameComponent.getInstance().getBuffPane().addBuff(buff);
		}
	}
	
	public void removeBuff(Buff buff) {
		buff.deactivateBuff();
		buffList.remove(buff);
		
		if(GameComponent.getInstance().getPlayer() == this) {
			GameComponent.getInstance().getBuffPane().removeBuff(buff);
		}
	}
	
	/* ------------------- Status ------------------- */
	public void upgradeAbility() {
		// Deactivate Buff
		for(Buff buff: buffList) {
			buff.deactivateBuff();
		}
		
		// Status
		bulletDamage = DEFAULT_BULLET_DAMAGE + 5 * status.getStatus(0);
		healthRegen = DEFAULT_HEALTH_REGEN + 5 * status.getStatus(1);
		bulletHP = DEFAULT_BULLET_HP + 1.5 * status.getStatus(2);
		bulletSpeed = DEFAULT_BULLET_SPEED + 10 * status.getStatus(2);
		maxHp = DEFAULT_MAX_HP + 500 * status.getStatus(1);
		attack = DEFAULT_ATTACK + 10 * status.getStatus(1);
		speed = DEFAULT_SPEED + 10 * status.getStatus(4);
		reloadDone = DEFAULT_RELOAD - status.getStatus(4);
//		criticalDamage;
//		criticalChance;
		damageFactor = 1;
		
		// Re-activate Buff
		for(Buff buff: buffList) {
			buff.activateBuff();
		}
	}
	
	public void upgradeStatus(int status) {
		if(this.status.canUpgradeStatus(status) == false) {
			return;
		}
		if(experience.decreasePointStatus()) {
			this.status.updateStatus(status);
			upgradeAbility();
		}
	}
	
	/* ------------------- Getters&Setters ------------------- */
	public Status getStatus() {
		return status;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setDamageFactor(double damageFactor) {
		this.damageFactor = damageFactor;
	}
	
	public void setReloadDone(int reloadDone) {
		this.reloadDone = reloadDone;
	}
	
	public int getRadius() {
		return 20;
	}
	
	public ArrayList<Skill> getSkillList() {
		return skillList;
	}
	
	public ArrayList<Buff> getBuffList() {
		return buffList;
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
	
	public double getDamageFactor() {
		return damageFactor;
	}
	
	public int getReloadDone() {
		return reloadDone;
	}
	
	public Job getJob() {
		return Job.NOVICE;
	}
	
	@Override
	public String toString() {
		return "Novice";
	}
}
